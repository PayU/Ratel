package com.payu.order.server;


import com.payu.discovery.server.config.DiscoveryServiceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan(basePackages = {"com.payu.order.server", "com.payu.training"}	)
@Configuration
@EnableAutoConfiguration
@EnableScheduling
@PropertySource("classpath:propertasy.properties")
@Import(DiscoveryServiceConfig.class)
public class MainConfiguration {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MainConfiguration.class, args);
    }

}
