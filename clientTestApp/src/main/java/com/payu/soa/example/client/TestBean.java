package com.payu.soa.example.client;

import com.payu.discovery.Cachable;
import com.payu.discovery.Discover;
import com.payu.discovery.RetryPolicy;
import com.payu.order.server.model.Order;
import com.payu.order.server.service.OrderService;

import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TestBean {

	@Discover
	@Cachable
	@RetryPolicy(exception = RemoteConnectFailureException.class)
	private OrderService orderService;

    @Async
    public void createOrderInAsync(Order order) {
        orderService.createOrder(order);
    }

}
