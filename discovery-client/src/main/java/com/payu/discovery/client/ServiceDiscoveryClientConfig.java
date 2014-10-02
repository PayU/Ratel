package com.payu.discovery.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceDiscoveryClientConfig {

    public static final String DEFAULT_DISCOVERY_URL = "http://localhost:8090/server/discovery";

    @Bean
    public MyAutowireCandidateResolver myAutowireCandidateResolver() {
        return new MyAutowireCandidateResolver(discoveryClient());
    }

    @Bean
    public DiscoveryClient discoveryClient() {
        return new DiscoveryClient(DEFAULT_DISCOVERY_URL);
    }

    @Bean
    public AutowireCandidateResolverConfigurer autowireCandidateResolverConfigurer() {
        AutowireCandidateResolverConfigurer autowireCandidateResolverConfigurer = new AutowireCandidateResolverConfigurer();
        autowireCandidateResolverConfigurer.setAutowireCandidateResolver(myAutowireCandidateResolver());
        return autowireCandidateResolverConfigurer;

    }
}
