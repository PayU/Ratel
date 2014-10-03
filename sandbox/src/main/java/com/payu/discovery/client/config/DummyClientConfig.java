package com.payu.discovery.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.payu.discovery.client.DummyAutowireCandidateResolver;
import com.payu.discovery.client.DummyAutowireCandidateResolverConfigurer;

@Configuration
public class DummyClientConfig {

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
