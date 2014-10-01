package com.payu.discovery.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@Import(DiscoveryServiceConfig.class)
public class ServiceDiscoveryClientConfig {

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
