/*
 * Copyright 2015 PayU
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.payu.ratel.tests;

import static com.jayway.awaitility.Awaitility.await;
import static com.payu.ratel.config.beans.RegistryBeanProviderFactory.SERVICE_DISCOVERY_ADDRESS;
import static com.payu.ratel.config.beans.ServiceRegisterPostProcessorFactory.JBOSS_BIND_ADDRESS;
import static com.payu.ratel.config.beans.ServiceRegisterPostProcessorFactory.JBOSS_BIND_PORT;
import static org.assertj.core.api.Assertions.assertThat;

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
import org.springframework.test.context.web.WebAppConfiguration;

import com.payu.ratel.config.ServiceDiscoveryConfig;
import com.payu.ratel.server.DiscoveryServerMain;
import com.payu.ratel.server.InMemoryDiscoveryServer;
import com.payu.ratel.tests.service.TestServiceConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ServiceDiscoveryConfig.class, DiscoveryServerMain.class})
@IntegrationTest({
        "server.port:8062",
        SERVICE_DISCOVERY_ADDRESS + ":http://localhost:8062/server/discovery"})
@WebAppConfiguration
public class ServiceRegistrationTest {

    private ConfigurableApplicationContext remoteContext;

    @Autowired
    private InMemoryDiscoveryServer server;

    @Before
    public void before() throws InterruptedException {
        remoteContext = SpringApplication.run(TestServiceConfiguration.class,
                "--server.port=8031",
                "--" + JBOSS_BIND_ADDRESS + "=localhost",
                "--" + JBOSS_BIND_PORT + "=8031",
                "--spring.jmx.enabled=false",
                "--" + SERVICE_DISCOVERY_ADDRESS + "=http://localhost:8062/server/discovery");
    }

    @After
    public void close() {
        remoteContext.close();
    }

    @Test
    public void shouldRegisterServices() throws InterruptedException {
        await().atMost(8, TimeUnit.SECONDS).until (
        		new Runnable() {

					@Override
					public void run() {
						assertThat(server.fetchAllServices()).hasSize(1);

					}
				}
        		);
        
        assertThat(server.fetchAllServices().iterator().next().getName())
                .isEqualTo("com.payu.ratel.tests.service.TestService");
    }

}
