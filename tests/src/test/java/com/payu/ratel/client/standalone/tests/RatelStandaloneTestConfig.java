package com.payu.ratel.client.standalone.tests;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.payu.ratel.client.ServiceCallListener;
import com.payu.ratel.client.standalone.RatelStandaloneFactory;
import com.payu.ratel.tests.service.TestServiceCallListener;

@Configuration
public class RatelStandaloneTestConfig {

    @Bean
    public RatelStandaloneFactory standaloneFactory() {
        return new RatelStandaloneFactory();
    }

    @Bean
    ServiceCallListener ratelCallListener() {
        return new TestServiceCallListener();
    }

}
