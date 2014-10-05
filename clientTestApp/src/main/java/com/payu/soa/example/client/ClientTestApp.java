package com.payu.soa.example.client;

import com.payu.discovery.client.EnableServiceDiscovery;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(basePackages = "com.payu.soa")
@Configuration
@EnableAutoConfiguration
@EnableServiceDiscovery
@PropertySource("classpath:service-discovery.properties")
public class ClientTestApp {

    public static void main(String[] args) {
        SpringApplication.run(ClientTestApp.class, args);
    }

}
