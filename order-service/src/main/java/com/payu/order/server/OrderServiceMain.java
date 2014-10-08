package com.payu.order.server;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(basePackages = {"com.payu.order.server", "com.payu.training"})
@Configuration
@EnableAutoConfiguration
@PropertySource("classpath:propertasy.properties")
public class OrderServiceMain {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(OrderServiceMain.class, args);
    }

}
