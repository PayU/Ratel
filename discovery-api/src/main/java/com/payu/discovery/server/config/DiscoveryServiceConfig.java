package com.payu.discovery.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.payu.discovery.server.RemoteRestDiscoveryServer;

@Configuration
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
