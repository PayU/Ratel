package com.payu.discovery.tests;

import com.payu.discovery.Discover;
import com.payu.discovery.client.EnableServiceDiscovery;
import com.payu.discovery.config.ServerDiscoveryConfig;
import com.payu.discovery.register.config.DiscoveryServiceConfig;
import com.payu.discovery.server.DiscoveryServerMain;
import com.payu.discovery.server.InMemoryDiscoveryServer;
import com.payu.discovery.register.config.DiscoveryServiceConfig;
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
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;
import static com.payu.discovery.config.ServerDiscoveryConfig.SERVICE_DISCOVERY_ADDRESS;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {DiscoveryServerMain.class, ServiceDiscoverTest.class})
@IntegrationTest({
        "server.port:8061",
        SERVICE_DISCOVERY_ADDRESS + ":http://localhost:8061/server/discovery"})
@WebAppConfiguration
@EnableServiceDiscovery
public class ServiceDiscoverTest {

    private ConfigurableApplicationContext remoteContext;

    @Autowired
    private InMemoryDiscoveryServer server;

    @Discover
    private TestService testService;

    @Before
    public void before() throws InterruptedException {
        remoteContext = SpringApplication.run(ServiceConfiguration.class,
                "--server.port=8031",
                "--app.address=http://localhost:8031",
                "--spring.jmx.enabled=false",
                "--" + SERVICE_DISCOVERY_ADDRESS + "=http://localhost:8061/server/discovery");
    }

    @After
    public void close() {
        remoteContext.close();
    }

    @Configuration
    @EnableAutoConfiguration
    @Import({DiscoveryServiceConfig.class, ServerDiscoveryConfig.class})
    @WebAppConfiguration
    public static class ServiceConfiguration {

        @Bean
        public TestService testService() {
            return new TestServiceImpl();
        }

    }

    @Test
    public void shouldDiscoverService() throws InterruptedException {
        await().atMost(10, TimeUnit.SECONDS).until(new Runnable() {

			@Override
			public void run() {
				assertThat(server.fetchAllServices()).hasSize(1);
			}
        	
        });

        //when
        final int result = testService.incrementCounter();

        //then
        assertThat(result).isEqualTo(1);
    }

}
