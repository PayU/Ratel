package com.payu.ratel.tests;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.payu.ratel.config.ServiceDiscoveryConfig;
import com.payu.ratel.server.DiscoveryServerMain;

@Configuration
@Import({ServiceDiscoveryConfig.class, DiscoveryServerMain.class})
public class TestRatelConfiguration {

    @Bean
    public RatelTestContext testContext() {
        return new RatelTestContext();
    }

}
