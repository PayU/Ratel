package com.payu.soa.example.client;

import com.payu.discovery.proxy.SpringRemoteBeanFactory;
import com.payu.server.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@ComponentScan(basePackages = "com.payu.soa")
@Configuration
@EnableAutoConfiguration
public class ClientTestApp extends SpringBootServletInitializer {

	@Autowired
	@Lazy
	private OrderService orderService;

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = new MySpringApplication(new Object[] { ClientTestApp.class, SoaFramework.class }).run(args);
	}
	
	@Bean
	public AutowireCapableBeanFactory getBeanFactory() { 
		return new MyBeanFactory();
	}
	
    @Bean
    public SpringRemoteBeanFactory springRemoteBeanFactory() {
        return new SpringRemoteBeanFactory();
    }

    @Bean
    public TestBean testBean() {
        return new TestBean();
    }

}
