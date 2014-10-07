package com.payu.transaction.server;


import com.payu.discovery.server.config.DiscoveryServiceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(basePackages = {"com.payu.transaction.server", "com.payu.training"})
@Configuration
@EnableAutoConfiguration
@PropertySource("classpath:propertasy.properties")
@Import(DiscoveryServiceConfig.class)
public class TransactionServiceConfig {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(TransactionServiceConfig.class, args);
    }

}
