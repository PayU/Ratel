package com.payu.discovery.tests.zookeeper;

import static com.jayway.awaitility.Awaitility.await;
import static com.payu.discovery.config.ZookeeperDiscoveryConfig.SERVICE_DISCOVERY_ZK_HOST;
import static org.assertj.core.api.BDDAssertions.then;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.curator.test.TestingServer;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.curator.x.discovery.strategies.RoundRobinStrategy;
import org.junit.After;
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

import com.payu.discovery.Discover;
import com.payu.discovery.client.EnableServiceDiscovery;
import com.payu.discovery.server.DiscoveryServerMain;
import com.payu.discovery.tests.service.ServiceConfiguration;
import com.payu.discovery.tests.service.TestService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {DiscoveryServerMain.class})
@IntegrationTest({
        "server.port:8061",
        SERVICE_DISCOVERY_ZK_HOST + ":127.0.0.1:" + ZookeeperServicePublishingTest.ZK_PORT
})
@WebAppConfiguration
@EnableServiceDiscovery
public class ZookeeperServicePublishingTest {
    public static final String SPRING_JMX_ENABLED_FALSE = "--spring.jmx.enabled=false";
    public static final int ZK_PORT = 2181;

    private ConfigurableApplicationContext remoteContext;

    @Discover
    private TestService testService;

    private static TestingServer zkServer;

    @Autowired
    ServiceDiscovery<TestService> serviceDiscovery;
    private ServiceProvider<TestService> serviceProvider;

    @BeforeClass
    public static void startZookeeper() throws Exception {
        zkServer = new TestingServer(ZK_PORT);
    }

    @Before
    public void before() throws Exception {
        remoteContext = SpringApplication.run(ServiceConfiguration.class,
                "--server.port=8031",
                "--app.address=http://localhost:8031",
                "--" + SERVICE_DISCOVERY_ZK_HOST + "=127.0.0.1:" + ZK_PORT,
                SPRING_JMX_ENABLED_FALSE);

        serviceProvider = serviceDiscovery.serviceProviderBuilder()
                .serviceName(TestService.class.getName())
                .providerStrategy(new RoundRobinStrategy<>())
                .build();
        serviceProvider.start();
    }

    @After
    public void close() throws IOException {
        remoteContext.close();
        zkServer.stop();
    }

    @Test
    public void shouldDiscoverService() throws InterruptedException {
        await().atMost(10, TimeUnit.SECONDS).until(() -> serviceProvider.getInstance() != null);

        //when
        final String result = testService.hello();

        then(result).isEqualTo("success");
    }

}
