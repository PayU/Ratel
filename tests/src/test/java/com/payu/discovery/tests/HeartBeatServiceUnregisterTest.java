package com.payu.discovery.tests;

import com.payu.discovery.Discover;
import com.payu.discovery.client.EnableServiceDiscovery;
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

import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;
import static com.payu.discovery.config.ServerDiscoveryConfig.SERVICE_DISCOVERY_ADDRESS;
import static com.payu.discovery.register.config.DiscoveryServiceConfig.JBOSS_BIND_ADDRESS;
import static com.payu.discovery.register.config.DiscoveryServiceConfig.JBOSS_BIND_PORT;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {
        DiscoveryServerMain.class,
        HeartBeatServiceUnregisterTest.class})
@IntegrationTest({
        "server.port:8066",
        SERVICE_DISCOVERY_ADDRESS + ":http://localhost:8066/server/discovery",
        "com.payu.discovery.enabled:false"})
@WebAppConfiguration
@EnableServiceDiscovery
public class HeartBeatServiceUnregisterTest {

    private ConfigurableApplicationContext remoteContext;
    private ConfigurableApplicationContext secondRemoteContext;

    @Autowired
    private InMemoryDiscoveryServer server;

    @Discover
    private TestService testService;

    @Before
    public void before() throws InterruptedException {
        remoteContext = SpringApplication.run(ServiceConfiguration.class,
                "--server.port=8031",
                "--" + JBOSS_BIND_ADDRESS + "=localhost",
                "--" + JBOSS_BIND_PORT + "=8031",
                "--spring.jmx.enabled=false",
                "--" + SERVICE_DISCOVERY_ADDRESS + "=http://localhost:8066/server/discovery");

        secondRemoteContext = SpringApplication.run(ServiceConfiguration.class,
                "--server.port=8032",
                "--" + JBOSS_BIND_ADDRESS + "=localhost",
                "--" + JBOSS_BIND_PORT + "=8032",
                "--spring.jmx.enabled=false",
                "--" + SERVICE_DISCOVERY_ADDRESS + "=http://localhost:8066/server/discovery");
    }

    @After
    public void close() {
        remoteContext.close();
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
    public void shouldUnregisterService() throws InterruptedException {
        await().atMost(5, TimeUnit.SECONDS).until(new Runnable() {

			@Override
			public void run() {
				assertThat(server.fetchAllServices()).hasSize(2);
			}
        	
        });

        //when
        secondRemoteContext.close();

        //then
        await().atMost(70, TimeUnit.SECONDS).until(new Runnable() {

            @Override
            public void run() {
                assertThat(server.fetchAllServices()).hasSize(2);
            }

        });
    }

}
