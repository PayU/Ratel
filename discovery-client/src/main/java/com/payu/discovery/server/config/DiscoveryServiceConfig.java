package com.payu.discovery.server.config;

import com.payu.discovery.Publish;
import com.payu.discovery.server.RemoteRestDiscoveryServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration Auto-configuration} to enable/disable Spring's
 * {@link com.payu.discovery.client.EnableServicePublish} mechanism based on configuration properties.
 * <p/>
 * To disable auto export of annotation beans set <code>com.payu.discovery.enabled: false</code>.
 */
@Configuration
@ConditionalOnClass({Publish.class})
@ConditionalOnExpression("${com.payu.discovery.enabled:true}")
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
                DEFAULT_DISCOVERY_URL));
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

}
