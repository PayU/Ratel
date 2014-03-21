package com.payu.hackathon.overseer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mariusz Smykuła
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableConfigurationProperties({ZookeeperProperties.class})
public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {

        LOG.info("ServiceDiscovery System starting...");

        SpringApplication.run(Application.class, args);
    }

}
