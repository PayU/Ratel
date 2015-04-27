package com.payu.ratel.examples.calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.payu.ratel.config.EnableServiceDiscovery;

@Configuration
@EnableAutoConfiguration
@EnableServiceDiscovery
@ComponentScan(basePackageClasses={CalculatorServiceImpl.class})
public class Calculator {
  
  public static void main(String[] args) {
    SpringApplication.run(Calculator.class, args);
  }

}
