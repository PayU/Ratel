package com.payu.ratel.tests;

import static com.jayway.awaitility.Awaitility.await;
import static com.payu.ratel.config.RatelContextInitializer.SERVICE_DISCOVERY_ADDRESS;
import static com.payu.ratel.config.ServiceDiscoveryConfig.JBOSS_BIND_ADDRESS;
import static com.payu.ratel.config.ServiceDiscoveryConfig.JBOSS_BIND_PORT;
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

import com.payu.ratel.Cachable;
import com.payu.ratel.Discover;
import com.payu.ratel.client.EnableServiceDiscovery;
import com.payu.ratel.config.RatelContextInitializer;
import com.payu.ratel.config.ServiceDiscoveryConfig;
import com.payu.ratel.server.DiscoveryServerMain;
import com.payu.ratel.server.InMemoryDiscoveryServer;
import com.payu.ratel.tests.service.ServiceConfiguration;
import com.payu.ratel.tests.service.TestService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ServiceDiscoveryConfig.class, DiscoveryServerMain.class},
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
