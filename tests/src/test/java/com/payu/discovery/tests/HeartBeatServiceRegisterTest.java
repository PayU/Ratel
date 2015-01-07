package com.payu.discovery.tests;

import com.payu.discovery.Discover;
import com.payu.discovery.client.EnableServiceDiscovery;
import com.payu.discovery.config.RatelContextInitializer;
import com.payu.discovery.server.DiscoveryServerMain;
import com.payu.discovery.server.InMemoryDiscoveryServer;
import com.payu.discovery.tests.service.TestService;
import com.payu.discovery.tests.service.TestServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.jayway.awaitility.Awaitility.await;
import static com.payu.discovery.config.RatelContextInitializer.SERVICE_DISCOVERY_ADDRESS;
import static com.payu.discovery.config.RatelContextInitializer.SERVICE_DISCOVERY_ENABLED;
import static com.payu.discovery.register.config.DiscoveryServiceConfig.JBOSS_BIND_ADDRESS;
import static com.payu.discovery.register.config.DiscoveryServiceConfig.JBOSS_BIND_PORT;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {DiscoveryServerMain.class, HeartBeatServiceRegisterTest.class},
        initializers = RatelContextInitializer.class)
@IntegrationTest({
        "server.port:8067",
        SERVICE_DISCOVERY_ADDRESS + ":http://localhost:8067/server/discovery",
        SERVICE_DISCOVERY_ENABLED + ":false"})
@WebAppConfiguration
@EnableServiceDiscovery
public class HeartBeatServiceRegisterTest {

    private ConfigurableApplicationContext remoteContext;
    private ConfigurableApplicationContext secondRemoteContext;

    @Autowired
    private InMemoryDiscoveryServer server;

    @Discover
    private TestService testService;

    @Before
    public void before() throws InterruptedException {
        final SpringApplication remoteContextSpringApplication = new SpringApplication(ServiceConfiguration.class);
        remoteContextSpringApplication.addInitializers(new RatelContextInitializer());

        remoteContext = remoteContextSpringApplication.run("--server.port=8031",
                "--" + JBOSS_BIND_ADDRESS + "=localhost",
                "--" + JBOSS_BIND_PORT + "=8031",
                "--spring.jmx.enabled=false",
                "--" + SERVICE_DISCOVERY_ADDRESS + "=http://localhost:8067/server/discovery");
    }

    @After
    public void close() {
        remoteContext.close();
        secondRemoteContext.close();
    }

    @Configuration
    @EnableAutoConfiguration
    @WebAppConfiguration
    public static class ServiceConfiguration {

        @Bean
        public TestService testService() {
            return new TestServiceImpl();
        }

    }

    @Test
    public void shouldRegisterService() throws InterruptedException {
        await().atMost(5, SECONDS).until(new Runnable() {

            @Override
            public void run() {
                assertThat(server.fetchAllServices()).hasSize(1);
            }

        });

        //when
        final SpringApplication secondRemoteContextSpringApplication = new SpringApplication(ServiceConfiguration.class);
        secondRemoteContextSpringApplication.addInitializers(new RatelContextInitializer());

        secondRemoteContext = secondRemoteContextSpringApplication.run("--server.port=8032",
                "--" + JBOSS_BIND_ADDRESS + "=localhost",
                "--" + JBOSS_BIND_PORT + "=8032",
                "--spring.jmx.enabled=false",
                "--" + SERVICE_DISCOVERY_ADDRESS + "=http://localhost:8067/server/discovery");

        //then
        await().atMost(60, SECONDS).until(new Runnable() {

            @Override
            public void run() {
                assertThat(server.fetchAllServices()).hasSize(2);
            }

        });
    }

}
