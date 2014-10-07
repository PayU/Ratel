package com.payu.discovery.client;

import java.io.IOException;
import java.util.Properties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.payu.discovery.RemoteService;

@Configuration
@ConditionalOnClass({RemoteService.class})
public class ServiceDiscoveryClientConfig {

    public static final String DEFAULT_DISCOVERY_URL = "http://localhost:8090/server/discovery";

    @Bean
    public DiscoveryClient discoveryClient() {

        DiscoveryClient discoveryClient = null;
        try {
            // environment is injected too late
            Properties properties = PropertiesLoaderUtils.loadAllProperties("service-discovery.properties");
            final String property = properties.getProperty("serviceDiscovery.address", DEFAULT_DISCOVERY_URL);
            discoveryClient = new DiscoveryClient(property);
        } catch (IOException e) {
            throw new ServiceDiscoveryConfigurationException("Error while reading from service-discovery.properties",
                    e);
        }

        return discoveryClient;
    }

    @Bean
    public RemoteAutowireCandidateResolver remoteAutowireCandidateResolver() {
        return new RemoteAutowireCandidateResolver(discoveryClient());
    }

    @Bean
    public AutowireCandidateResolverConfigurer autowireCandidateResolverConfigurer() {
        AutowireCandidateResolverConfigurer autowireCandidateResolverConfigurer = new AutowireCandidateResolverConfigurer();
        autowireCandidateResolverConfigurer.setAutowireCandidateResolver(remoteAutowireCandidateResolver());
        return autowireCandidateResolverConfigurer;

    }
}
