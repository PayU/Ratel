package com.payu.soa.example.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.payu.discovery.EnableServiceDiscovery;
import com.payu.discovery.client.SandboxConfig;
import com.payu.discovery.client.config.DummyClientConfig;

@ComponentScan(basePackages = "com.payu.soa")
@Configuration
@EnableAutoConfiguration
@EnableServiceDiscovery
@PropertySource("classpath:propertasy.properties")
public class SandboxApp {

    public static void main(String[] args) {
        SpringApplication.run(new Class[]{SandboxApp.class, SandboxConfig.class, DummyClientConfig.class}, args);
    }

//    @Bean
//    @Lazy
//    public OrderService orderService(HessianClientProducer hessianClientProducer) {
//        return (OrderService) hessianClientProducer.produce(OrderService.class);
//    }

}
