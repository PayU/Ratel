package com.payu.discovery.client;

import com.payu.discovery.proxy.HessianClientProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ServiceDiscoveryClientConfig {

    public static final String DEFAULT_DISCOVERY_URL = "http://localhost:8090/server/discovery";

    @Autowired
    private Environment env;

    @Bean
    public DiscoveryClient discoveryClient() {
        return new DiscoveryClient(env.getProperty("serviceDiscovery.address", DEFAULT_DISCOVERY_URL));
    }

    @Bean
    public HessianClientProducer hessianClientProducer() {
        return new HessianClientProducer(discoveryClient());
    }

}
