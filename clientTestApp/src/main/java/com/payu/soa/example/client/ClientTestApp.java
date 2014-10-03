package com.payu.soa.example.client;

import com.payu.discovery.EnableServiceDiscovery;
import com.payu.discovery.proxy.HessianClientProducer;
import com.payu.order.server.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(basePackages = "com.payu.soa")
@Configuration
@EnableAutoConfiguration
@EnableServiceDiscovery
@PropertySource("classpath:propertasy.properties")
public class ClientTestApp {

    public static void main(String[] args) {
        SpringApplication.run(ClientTestApp.class, args);
    }

    @Bean
    @Autowired
    @Lazy
    public OrderService orderService(HessianClientProducer hessianClientProducer) {
        return (OrderService) hessianClientProducer.produce(OrderService.class);
    }

}
