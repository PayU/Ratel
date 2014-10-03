package com.payu.discovery.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@Import(DiscoveryServiceConfig.class)
public class SandboxConfig {

    @Bean
    public DummyAutowireCandidateResolver myAutowireCandidateResolver() {
        return new DummyAutowireCandidateResolver();
    }

    @Bean
    public DummyAutowireCandidateResolverConfigurer autowireCandidateResolverConfigurer() {
        DummyAutowireCandidateResolverConfigurer autowireCandidateResolverConfigurer = new DummyAutowireCandidateResolverConfigurer();
        autowireCandidateResolverConfigurer.setAutowireCandidateResolver(myAutowireCandidateResolver());
        return autowireCandidateResolverConfigurer;

    }
}
