package com.payu.ratel.tests.service.misconfig;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.web.WebAppConfiguration;

import com.payu.ratel.config.EnableServiceDiscovery;
import com.payu.ratel.tests.service.Test2Service;

@Configuration
@EnableAutoConfiguration
@WebAppConfiguration
@EnableServiceDiscovery
public class WrongServiceConfiguration {

    @Bean
    public Test2Service wrongService() {
        return new WrongServiceImplementation();
    }

}


