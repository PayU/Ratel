package com.payu.discovery.tests.zookeeper;

import com.payu.discovery.Discover;
import com.payu.discovery.client.EnableServiceDiscovery;
import com.payu.discovery.config.RatelContextInitializer;
import com.payu.discovery.config.ZookeeperDiscoveryConfig;
import com.payu.discovery.server.DiscoveryServerMain;
import com.payu.discovery.tests.service.ServiceConfiguration;
import com.payu.discovery.tests.service.TestService;
import org.apache.curator.test.TestingServer;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.curator.x.discovery.strategies.RoundRobinStrategy;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;
import static com.payu.discovery.config.RatelContextInitializer.SERVICE_DISCOVERY_ZK_HOST;
import static com.payu.discovery.register.config.DiscoveryServiceConfig.JBOSS_BIND_ADDRESS;
import static com.payu.discovery.register.config.DiscoveryServiceConfig.JBOSS_BIND_PORT;
import static org.assertj.core.api.BDDAssertions.then;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {DiscoveryServerMain.class, ZookeeperDiscoveryConfig.class},
        initializers = RatelContextInitializer.class)
@IntegrationTest({
        SERVICE_DISCOVERY_ZK_HOST + ":127.0.0.1:" + ZookeeperServicePublishingTest.ZK_PORT
})
@WebAppConfiguration
@EnableServiceDiscovery
public class ZookeeperServicePublishingTest {
    public static final String SPRING_JMX_ENABLED_FALSE = "--spring.jmx.enabled=false";
    public static final int ZK_PORT = 2185;

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
        final SpringApplication remoteContextSpringApplication = new SpringApplication(ServiceConfiguration.class);
        remoteContextSpringApplication.addInitializers(new RatelContextInitializer());

        remoteContext = remoteContextSpringApplication.run("--server.port=8035",
                "--" + JBOSS_BIND_ADDRESS + "=localhost",
                "--" + JBOSS_BIND_PORT + "=8035",
                "--" + SERVICE_DISCOVERY_ZK_HOST + "=127.0.0.1:" + ZK_PORT,
                SPRING_JMX_ENABLED_FALSE);

        serviceProvider = serviceDiscovery.serviceProviderBuilder()
                .serviceName(TestService.class.getName())
                .providerStrategy(new RoundRobinStrategy<TestService>())
                .build();
        serviceProvider.start();
    }

    @After
    public void close() throws IOException {
        remoteContext.close();
    }

    @AfterClass
    public static void closeZookeeper() throws IOException {
        zkServer.stop();
    }

    @Test
    public void shouldDiscoverService() throws InterruptedException {
        await().atMost(10, TimeUnit.SECONDS).until(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return serviceProvider.getInstance() != null;
            }
        });

        //when
        final String result = testService.hello();

        then(result).isEqualTo("success");
    }

}
