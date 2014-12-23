package com.payu.discovery.tests;

import com.payu.discovery.config.ServerDiscoveryConfig;
import com.payu.discovery.register.config.DiscoveryServiceConfig;
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
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;
import static com.payu.discovery.config.ServerDiscoveryConfig.SERVICE_DISCOVERY_ADDRESS;
import static com.payu.discovery.register.config.DiscoveryServiceConfig.JBOSS_BIND_ADDRESS;
import static com.payu.discovery.register.config.DiscoveryServiceConfig.JBOSS_BIND_PORT;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DiscoveryServerMain.class)
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
        remoteContext = SpringApplication.run(ServiceConfiguration.class,
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
                .isEqualTo("com.payu.discovery.tests.service.TestService");
    }

}
