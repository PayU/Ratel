/*
 * Copyright 2015 PayU
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.payu.ratel.tests.zookeeper;

import static com.jayway.awaitility.Awaitility.await;
import static com.payu.ratel.config.beans.RatelPropertySelfAddressProvider.RATEL_BIND_ADDRESS;
import static com.payu.ratel.config.beans.RegistryBeanProviderFactory.SERVICE_DISCOVERY_ZK_HOST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.curator.test.TestingServer;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.curator.x.discovery.strategies.RoundRobinStrategy;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.payu.ratel.Discover;
import com.payu.ratel.client.standalone.RatelClientFactory;
import com.payu.ratel.client.standalone.RatelStandaloneFactory;
import com.payu.ratel.config.ServiceDiscoveryConfig;
import com.payu.ratel.config.beans.RegistryStrategiesProvider;
import com.payu.ratel.server.DiscoveryServerMain;
import com.payu.ratel.tests.service.Test2Service;
import com.payu.ratel.tests.service.TestService;
import com.payu.ratel.tests.service.TestServiceConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { ServiceDiscoveryConfig.class, DiscoveryServerMain.class })
@IntegrationTest({ SERVICE_DISCOVERY_ZK_HOST + ":127.0.0.1:" + ZookeeperServicePublishingTest.ZK_PORT })
@WebAppConfiguration
public class ZookeeperServicePublishingTest {

    public static final String SPRING_JMX_ENABLED_FALSE = "--spring.jmx.enabled=false";
    public static final int ZK_PORT = 2185;
    public static final String ZK_HOST = "127.0.0.1:" + ZK_PORT;
    private static TestingServer zkServer;

    private ServiceProvider<TestService> serviceProvider;

    @Discover
    private TestService testService;

    private ConfigurableApplicationContext remoteContext;

    @Autowired
    private ServiceDiscovery<TestService> serviceDiscovery;

    @Autowired
    private RegistryStrategiesProvider strategiesProvider;

    private int port = 8035;

    @BeforeClass
    public static void startZookeeper() throws Exception {
        zkServer = new TestingServer(ZK_PORT);
        zkServer.start();
    }

    @AfterClass
    public static void closeZookeeper() throws IOException {
        zkServer.stop();
        zkServer.close();
    }

    @Test
    public void shouldReturnListOfServiceNames() throws Exception {
        // when
        testService.hello();
        Collection<String> serviceNames = strategiesProvider.getFetchStrategy().getServiceNames();

        //then
        then(serviceNames).hasSize(2).contains(TestService.class.getCanonicalName(), Test2Service.class.getCanonicalName());
    }

    @Test
    public void shouldDiscoverService() throws InterruptedException {
        //given

        // when
        final String result = testService.hello();

        then(result).isEqualTo("success");
    }

    @Test
    public void shouldCreateStandaloneClientWithZookeeper() {
        //given
        RatelClientFactory ratelStandaloneFactory = RatelStandaloneFactory.fromZookeeperServer(ZK_HOST);

        //when
        TestService testedService = ratelStandaloneFactory.getServiceProxy(TestService.class);

        then(testedService.hello()).isEqualTo("success");
    }

    @Test
    public void shouldResolveZookeeperHostFromCliProperty() {
        //given
        System.setProperty(SERVICE_DISCOVERY_ZK_HOST, ZK_HOST);

        //when
        final TestService testedService = RatelStandaloneFactory.fromZookeeperServer().getServiceProxy(TestService.class);

        then(testedService.hello()).isEqualTo("success");

    }

    @Before
    public void before() throws Exception {
        remoteContext = SpringApplication.run(TestServiceConfiguration.class, "--server.port=" + port, "--"
                + RATEL_BIND_ADDRESS + "=localhost:" + port, "--" + SERVICE_DISCOVERY_ZK_HOST
                + "=" + ZK_HOST, SPRING_JMX_ENABLED_FALSE);

        serviceProvider = serviceDiscovery.serviceProviderBuilder().serviceName(TestService.class.getName())
                .providerStrategy(new RoundRobinStrategy<TestService>()).build();
        serviceProvider.start();
        waitForTestServiceRegistration();
    }

    private void waitForTestServiceRegistration() {
        await().atMost(10, TimeUnit.SECONDS).until(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return serviceProvider.getInstance() != null;
            }
        });
    }

    @After
    public void close() throws Exception {
        remoteContext.close();
        assertThat(serviceProvider.getInstance()).isNull();
        System.clearProperty(SERVICE_DISCOVERY_ZK_HOST);
    }
}
