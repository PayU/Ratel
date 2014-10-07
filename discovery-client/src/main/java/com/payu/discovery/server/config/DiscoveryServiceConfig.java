package com.payu.discovery.server.config;

import com.payu.discovery.server.RemoteRestDiscoveryServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class DiscoveryServiceConfig {

    public static final String DEFAULT_DISCOVERY_URL = "http://localhost:8090/server/discovery";

    @Autowired
    private Environment env;

    @Bean
    public ServiceRegisterPostProcessor serviceRegisterPostProcessor() {
        return new ServiceRegisterPostProcessor();
    }

    @Bean
    public RemoteRestDiscoveryServer discoveryServer() {
        return new RemoteRestDiscoveryServer(env.getProperty("serviceDiscovery.address",
                DEFAULT_DISCOVERY_URL), env.getProperty("app.address"));
    }

}
