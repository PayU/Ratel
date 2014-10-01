package com.payu.soa.example.client;

import com.payu.discovery.proxy.SpringRemoteBeanFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.payu.server.model.Order;
import com.payu.server.service.OrderService;

@ComponentScan(basePackages = "com.payu.soa")
@Configuration
@EnableAutoConfiguration
public class ClientTestApp extends SpringBootServletInitializer {

	@Autowired
	@Lazy
	private OrderService orderService;

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = new MySpringApplication(new Object[] { ClientTestApp.class, SoaFramework.class }).run(args);

		
		callOrderService(ctx);
	}
	
	@Bean
	public AutowireCapableBeanFactory getBeanFactory() { 
		return new MyBeanFactory();
	}
	
    @Bean
    public SpringRemoteBeanFactory springRemoteBeanFactory() {
        return new SpringRemoteBeanFactory();
    }
    
//    @Bean
//    public OrderService getService() {
//    	return null;
//    }


    private static void callOrderService(ConfigurableApplicationContext ctx) {
		ctx.getBeansOfType(ClientTestApp.class).values().iterator().next().getOrderService().
				createOrder(new Order());
		
		ctx.getBeansOfType(OrderService.class).values().iterator().next().
		createOrder(new Order());
	}

	public OrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	

}
