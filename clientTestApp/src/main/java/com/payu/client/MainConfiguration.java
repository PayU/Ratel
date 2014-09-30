package com.payu.client;


import com.payu.discovery.proxy.SpringRemoteBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = "com.payu.client")
@Configuration
@EnableAutoConfiguration
public class MainConfiguration extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(new Object[]{MainConfiguration.class}, args);
    }

    @Bean
    public SpringRemoteBeanFactory springRemoteBeanFactory() {
        return new SpringRemoteBeanFactory();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MainConfiguration.class);
    }

}
