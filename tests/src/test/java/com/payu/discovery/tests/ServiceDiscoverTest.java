package com.payu.discovery.tests;

import static com.jayway.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;

import com.payu.discovery.Discover;
import com.payu.discovery.client.EnableServiceDiscovery;
import com.payu.discovery.client.config.ServiceDiscoveryClientConfig;
import com.payu.discovery.server.DiscoveryServerMain;
import com.payu.discovery.server.InMemoryDiscoveryServer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {DiscoveryServerMain.class, ServiceDiscoverTest.class})
@IntegrationTest("server.port:8060")
@WebAppConfiguration
@PropertySource("classpath:propertasy.properties")
@EnableServiceDiscovery
public class ServiceDiscoverTest {

    @Autowired
    private InMemoryDiscoveryServer server;

    @Discover
    private TestService testService;

    @Before
    public void before() throws InterruptedException {
        SpringApplication.run(ServiceConfiguration.class);
    }

    @Configuration
    @PropertySource("classpath:propertasy.properties")
    @EnableAutoConfiguration
    @Import(ServiceDiscoveryClientConfig.class)
    @WebAppConfiguration
    public static class ServiceConfiguration {

        @Bean
        public TestService testService() {
            return new TestServiceImpl();
        }

    }

    @Test
    public void shouldDiscoverService() throws InterruptedException {
        await().atMost(5, TimeUnit.SECONDS).until(() ->
                assertThat(server.fetchAllServices()).hasSize(1));

        //when
        final int result = testService.testMethod();

        //then
        assertThat(result).isEqualTo(100);
    }

}
