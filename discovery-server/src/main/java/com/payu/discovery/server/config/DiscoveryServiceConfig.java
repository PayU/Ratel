package com.payu.discovery.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.payu.discovery.server.RemoteRestDiscoveryServer;
import com.payu.discovery.server.MainConfiguration;

@Configuration
@Import(MainConfiguration.class)
public class DiscoveryServiceConfig {
	
	@Bean
	public ServiceRegisterPostProcessor getServiceRegisterPostProcessor() {
		return new ServiceRegisterPostProcessor();
	}

	@Bean
	public RemoteRestDiscoveryServer getDiscoveryServer() {
		return new RemoteRestDiscoveryServer();
	}
	
}
