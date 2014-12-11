package com.payu.discovery.tests.service;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.web.WebAppConfiguration;

import com.payu.discovery.register.config.DiscoveryServiceConfig;

@Configuration
@EnableAutoConfiguration
@Import(DiscoveryServiceConfig.class)
@WebAppConfiguration
public class ServiceConfiguration {

    @Bean
    public TestService testService() {
        return new TestServiceImpl();
    }

}
