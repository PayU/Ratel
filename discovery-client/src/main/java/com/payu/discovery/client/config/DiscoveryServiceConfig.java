package com.payu.discovery.client.config;

import com.payu.discovery.server.AutowireCandidateResolverConfigurer;
import com.payu.discovery.server.MyAutowireCandidateResolver;
import com.payu.discovery.server.RemoteRestDiscoveryServer;
import com.payu.discovery.server.config.ServiceRegisterPostProcessor;
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

    @Bean
    public MyAutowireCandidateResolver myAutowireCandidateResolver() {
        return new MyAutowireCandidateResolver();
    }

    @Bean
    public AutowireCandidateResolverConfigurer autowireCandidateResolverConfigurer() {
        AutowireCandidateResolverConfigurer autowireCandidateResolverConfigurer = new AutowireCandidateResolverConfigurer();
        autowireCandidateResolverConfigurer.setAutowireCandidateResolver(myAutowireCandidateResolver());
        return autowireCandidateResolverConfigurer;

    }

}
