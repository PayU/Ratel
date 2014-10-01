package com.payu.discovery.server.config;

import com.payu.discovery.server.RemoteRestDiscoveryServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiscoveryServiceConfig {

    @Bean
    public ServiceRegisterPostProcessor getServiceRegisterPostProcessor() {
        return new ServiceRegisterPostProcessor();
    }

    @Bean
    public RemoteRestDiscoveryServer getDiscoveryServer() {
        return new RemoteRestDiscoveryServer();
    }

}
