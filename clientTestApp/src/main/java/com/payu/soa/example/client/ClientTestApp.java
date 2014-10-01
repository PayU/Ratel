package com.payu.soa.example.client;

import com.payu.discovery.EnableServiceDiscovery;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = "com.payu.soa")
@Configuration
@EnableAutoConfiguration
@EnableServiceDiscovery
public class ClientTestApp {

    public static void main(String[] args) {
        SpringApplication.run(ClientTestApp.class, args);
    }

}
