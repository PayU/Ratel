package com.payu.soa.example.client;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ResourceLoader;

public class MySpringApplication extends SpringApplication {

	public MySpringApplication(Object... sources) {
		super(sources);
	}

	public MySpringApplication(ResourceLoader resourceLoader, Object... sources) {
		super(resourceLoader, sources);
	}

	@Override
	public ConfigurableApplicationContext run(String... args) {
		return super.run(args);
	}


}
