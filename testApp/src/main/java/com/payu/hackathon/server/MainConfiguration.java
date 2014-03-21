package com.payu.hackathon.server;


import com.payu.hackathon.discovery.server.ServiceRegister;
import com.payu.hackathon.server.config.JerseyConfig;
import com.payu.hackathon.server.config.ServiceRegisterPostProcessor;
import com.payu.hackathon.server.model.OrderDatabase;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletException;

@ComponentScan(basePackages = "com.payu.hackathon.server")
@Configuration
@EnableAutoConfiguration
public class MainConfiguration extends SpringBootServletInitializer {

    private static final String appAddress = "http://localhost:8080/";
    private static String zooKeeperUri = "localhost:7777";

    public static void main(String[] args) throws Exception {
        SpringApplication.run(new Object[]{
                MainConfiguration.class}, args);
        ServiceRegister serviceRegister = new ServiceRegister("com.payu.hackathon.server.service",
                appAddress, zooKeeperUri);
        serviceRegister.registerServices();
    }

    @Bean
    public ServletRegistrationBean jerseyServlet() throws ServletException {

        ServletContainer servlet = new ServletContainer();
        ServletRegistrationBean registration = new ServletRegistrationBean(servlet,
                "/server/*");
        registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, JerseyConfig.class.getName());
        return registration;
    }

    @Bean
    public OrderDatabase orderDatabase() {
        return new OrderDatabase();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MainConfiguration.class);
    }

    public ServiceRegisterPostProcessor getServiceRegisterPostProcessor() {
        return new ServiceRegisterPostProcessor();
    }


}
