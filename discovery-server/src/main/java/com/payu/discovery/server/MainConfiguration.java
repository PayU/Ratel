package com.payu.discovery.server;


import com.payu.discovery.server.config.JerseyConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.ServletException;

@ComponentScan(basePackages = "com.payu.discovery.server")
@Configuration
@EnableAutoConfiguration
@EnableScheduling
public class MainConfiguration  {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MainConfiguration.class, args);
    }

    @Bean
    public ServletRegistrationBean jerseyServlet() throws ServletException {
        ServletContainer servlet = new ServletContainer();
        ServletRegistrationBean registration = new ServletRegistrationBean(servlet, "/server/*");
        registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, JerseyConfig.class.getName());
        return registration;
    }

}
