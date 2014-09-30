package com.payu.soa.example.client;

import com.payu.discovery.proxy.SpringRemoteBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.payu.server.model.Order;
import com.payu.server.service.OrderService;

@ComponentScan(basePackages = "com.payu.soa")
@Configuration
@EnableAutoConfiguration
public class ClientTestApp extends SpringBootServletInitializer {

	@Autowired
	private OrderService orderService;

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(
				new Object[] { ClientTestApp.class, SoaFramework.class }, args);

		
		callOrderService(ctx);
	}

    @Bean
    public SpringRemoteBeanFactory springRemoteBeanFactory() {
        return new SpringRemoteBeanFactory();
    }


    private static void callOrderService(ConfigurableApplicationContext ctx) {
		ctx.getBeansOfType(OrderService.class).values().iterator().next()
				.createOrder(new Order());
	}

	

}
