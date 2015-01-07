package com.payu.discovery.tests;

import com.payu.discovery.Cachable;
import com.payu.discovery.Discover;
import com.payu.discovery.client.EnableServiceDiscovery;
import com.payu.discovery.config.RatelContextInitializer;
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
import static com.payu.discovery.config.RatelContextInitializer.SERVICE_DISCOVERY_ADDRESS;
import static com.payu.discovery.register.config.DiscoveryServiceConfig.JBOSS_BIND_ADDRESS;
import static com.payu.discovery.register.config.DiscoveryServiceConfig.JBOSS_BIND_PORT;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {DiscoveryServerMain.class, ClientCacheTest.class},
        initializers = RatelContextInitializer.class)
@IntegrationTest({
        "server.port:8063",
        SERVICE_DISCOVERY_ADDRESS + ":http://localhost:8063/server/discovery"})
@WebAppConfiguration
@EnableServiceDiscovery
public class ClientCacheTest {

    private ConfigurableApplicationContext remoteContext;

    @Autowired
    private InMemoryDiscoveryServer server;

    @Discover
    @Cachable
    private TestService testService;

    @Before
    public void before() throws InterruptedException {
        final SpringApplication remoteContextSpringApplication = new SpringApplication(ServiceConfiguration.class);
        remoteContextSpringApplication.addInitializers(new RatelContextInitializer());

        remoteContext = remoteContextSpringApplication.run("--server.port=8031",
                "--" + JBOSS_BIND_ADDRESS + "=localhost",
                "--" + JBOSS_BIND_PORT + "=8031",
                "--spring.jmx.enabled=false",
                "--" + SERVICE_DISCOVERY_ADDRESS + "=http://localhost:8063/server/discovery");
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
    public void shouldCacheResults() throws InterruptedException {
        await().atMost(10, TimeUnit.SECONDS).until(new Runnable() {
			@Override
			public void run() {
				assertThat(server.fetchAllServices()).hasSize(1);
			}
        	
        });

        //when
        final int result = testService.incrementCounter();
        final int firstResult = testService.cached("cached");
        final int result2 = testService.incrementCounter();
        final int cachedResult = testService.cached("cached");
        final int newResult = testService.cached("new");

        //then
        assertThat(firstResult).isEqualTo(cachedResult).isEqualTo(result);
        assertThat(result2).isEqualTo(newResult);
    }

}
