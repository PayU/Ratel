/*
 * Copyright 2015 PayU
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.payu.ratel.client.standalone.tests;

import static com.payu.ratel.config.beans.JbossPropertySelfAddressProvider.JBOSS_BIND_ADDRESS;
import static com.payu.ratel.config.beans.JbossPropertySelfAddressProvider.JBOSS_BIND_PORT;
import static com.payu.ratel.config.beans.RegistryBeanProviderFactory.SERVICE_DISCOVERY_ADDRESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jayway.awaitility.Awaitility;
import com.payu.ratel.client.ServiceCallListener;
import com.payu.ratel.client.standalone.RatelStandaloneFactory;
import com.payu.ratel.config.ServiceDiscoveryConfig;
import com.payu.ratel.context.ProcessContext;
import com.payu.ratel.context.RemoteServiceCallEvent;
import com.payu.ratel.context.RemoteServiceResponseEvent;
import com.payu.ratel.context.ServiceEvent;
import com.payu.ratel.context.ServiceInstanceCallEvent;
import com.payu.ratel.context.ServiceInstanceResponseEvent;
import com.payu.ratel.server.DiscoveryServerMain;
import com.payu.ratel.server.InMemoryDiscoveryServer;
import com.payu.ratel.tests.service.TestService;
import com.payu.ratel.tests.service.TestServiceCallListener;
import com.payu.ratel.tests.service.TestServiceConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {RatelStandaloneTestConfig.class})
@IntegrationTest({"serviceDiscovery.ratelServerAddress:http://localhost:19070/server/discovery",
        "ratel.connectTimeout:4000",
        "ratel.readTimeout:4000",
})
public class StandaloneClientTest {

    @Autowired
    private RatelStandaloneFactory standaloneFactory;

    private ConfigurableApplicationContext startedApp;

    @Before
    public void setup() {
        ConfigurableApplicationContext ctx = SpringApplication.run(new Object[] {TestServiceConfiguration.class,
                ServiceDiscoveryConfig.class, DiscoveryServerMain.class}, new String[] {"--server.port=19070",
                "--" + JBOSS_BIND_ADDRESS + "=localhost", "--" + JBOSS_BIND_PORT + "=19070",
                "--spring.jmx.enabled=false", "--" + SERVICE_DISCOVERY_ADDRESS + "=http://localhost:19070"
                        + "/server/discovery"});

        final InMemoryDiscoveryServer server = ctx.getBean(InMemoryDiscoveryServer.class);
        Awaitility.await().atMost(20, TimeUnit.SECONDS).until(new Runnable() {

            @Override
            public void run() {
                assertThat(server.fetchAllServices()).isNotEmpty();
            }
        });

        this.startedApp = ctx;
    }

    @After
    public void tearDown() {
        startedApp.close();
    }

    @Test
    public void shouldDiscoverServiceWithInjectedStandaloneClientFactory() {

        TestService testService = standaloneFactory.getServiceProxy(TestService.class);

        // when
        final String result = testService.hello();

        // then
        assertThat(result).isEqualTo("success");
    }

    @Test
    public void shouldUseAddedServiceCallListenerStandaloneClientFactory() {

        // given
        // client side
        TestService testService = standaloneFactory.getServiceProxy(TestService.class);
        ServiceCallListener listener = mock(ServiceCallListener.class);
        standaloneFactory.addRatelServiceCallListener(listener);
        ProcessContext.getInstance().setProcessIdentifier("123");

        // server side
        TestServiceCallListener serverSideListener = startedApp.getBean(TestServiceCallListener.class);

        // when
        final String result = testService.hello();

        // then
        assertThat(result).isEqualTo("success");
        verify(listener, times(1)).remoteServiceCalled(any(RemoteServiceCallEvent.class));
        verify(listener, times(1)).remoteServiceResponded(any(RemoteServiceResponseEvent.class));

        // Separate context on server side = should not be impacted
        verify(listener, times(0)).serviceInstanceCalled(any(ServiceInstanceCallEvent.class));
        verify(listener, times(0)).serviceInstanceResponded(any(ServiceInstanceResponseEvent.class));

        // events on server side
        assertRecordedProcessId(serverSideListener.getServiceCallEvent(), "123");
        assertRecordedProcessId(serverSideListener.getServiceResponseEvent(), "123");
    }

    private void assertRecordedProcessId(ServiceEvent serviceEvent, String processId) {
        assertThat(serviceEvent.getProcessContext().getProcessIdentifier()).isEqualTo(processId);
    }

}
