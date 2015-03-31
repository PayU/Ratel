package com.payu.ratel.tests.service.tracing;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.web.WebAppConfiguration;

import com.payu.ratel.config.ServiceDiscoveryConfig;

@Configuration
@EnableAutoConfiguration
@Import(ServiceDiscoveryConfig.class)
@WebAppConfiguration
public class TracingTestConfiguration {
	
	@Bean
	ProcessIdTargetService targetService(){
		return new ProcessIdTargetServiceImpl();
	}
	
	@Bean
	ProcessIdPassingService PassingService(){
		return new ProcessIdPassingServiceImpl();
	}

}
