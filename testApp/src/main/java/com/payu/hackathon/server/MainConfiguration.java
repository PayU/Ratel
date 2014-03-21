package com.payu.hackathon.server;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan
@Configuration
@EnableAutoConfiguration
public class MainConfiguration {

    public static void main(String[] args) {
        SpringApplication.run(MainConfiguration.class, args);
    }
}
