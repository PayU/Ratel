package com.payu.discovery.tests;

import static com.jayway.awaitility.Awaitility.await;
import static com.payu.discovery.config.RatelContextInitializer.SERVICE_DISCOVERY_ADDRESS;
import static com.payu.discovery.config.ServiceDiscoveryConfig.JBOSS_BIND_ADDRESS;
import static com.payu.discovery.config.ServiceDiscoveryConfig.JBOSS_BIND_PORT;
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

import com.payu.discovery.Discover;
import com.payu.discovery.client.EnableServiceDiscovery;
import com.payu.discovery.config.RatelContextInitializer;
import com.payu.discovery.config.ServiceDiscoveryConfig;
import com.payu.discovery.server.DiscoveryServerMain;
import com.payu.discovery.server.InMemoryDiscoveryServer;
import com.payu.discovery.tests.service.provider.ProviderConfiguration;
import com.payu.discovery.tests.service.provider.RatelServiceDiscoveredByConstructor;
import com.payu.discovery.tests.service.ServiceConfiguration;
import com.payu.discovery.tests.service.TestService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ServiceDiscoveryConfig.class, DiscoveryServerMain.class,
        ProviderConfiguration.class},
        initializers = RatelContextInitializer.class)
@IntegrationTest({
        "server.port:8061",
        SERVICE_DISCOVERY_ADDRESS + ":http://localhost:8061/server/discovery"})
@WebAppConfiguration
@EnableServiceDiscovery
public class ServiceDiscoverTest {

    private ConfigurableApplicationContext remoteContext;

    @Autowired
    private InMemoryDiscoveryServer server;

    @Autowired
    private RatelServiceDiscoveredByConstructor ratelServiceDiscoveredByConstructor;

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
                "--" + SERVICE_DISCOVERY_ADDRESS + "=http://localhost:8061/server/discovery");
    }

    @After
    public void close() {
        remoteContext.close();
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

    @Test
    public void shouldDiscoverServiceByConstructor() throws InterruptedException {
        TestService testService1 = ratelServiceDiscoveredByConstructor.getTestService1();
        await().atMost(10, TimeUnit.SECONDS).until(new Runnable() {

            @Override
            public void run() {
                assertThat(server.fetchAllServices()).hasSize(1);
            }

        });

        //when
        final int result = testService1.incrementCounter();

        //then
        assertThat(result).isEqualTo(1);
        assertThat(ratelServiceDiscoveredByConstructor.getEnvironment1()).isNotNull();
        assertThat(ratelServiceDiscoveredByConstructor.getEnvironment2()).isNotNull();
    }

}
