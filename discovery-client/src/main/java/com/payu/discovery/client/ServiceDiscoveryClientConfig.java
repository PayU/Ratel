package com.payu.discovery.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ServiceDiscoveryClientConfig {

    public static final String DEFAULT_DISCOVERY_URL = "http://localhost:8090/server/discovery";

    // FIXME

    @Autowired
    private Environment env;

    @Bean
    public MyAutowireCandidateResolver myAutowireCandidateResolver() {
        return new MyAutowireCandidateResolver(discoveryClient());
    }

    @Bean
    public DiscoveryClient discoveryClient() {
        return new DiscoveryClient(env.getProperty("serviceDiscovery.address", DEFAULT_DISCOVERY_URL));
    }

    @Bean
    public AutowireCandidateResolverConfigurer autowireCandidateResolverConfigurer() {
        AutowireCandidateResolverConfigurer autowireCandidateResolverConfigurer = new AutowireCandidateResolverConfigurer();
        autowireCandidateResolverConfigurer.setAutowireCandidateResolver(myAutowireCandidateResolver());
        return autowireCandidateResolverConfigurer;

    }
}
