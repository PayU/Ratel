package com.payu.soa.example.client;

import com.payu.discovery.Cachable;
import com.payu.discovery.Discover;
import com.payu.discovery.RetryPolicy;
import com.payu.order.server.model.Order;
import com.payu.order.server.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TestBean {

    @Autowired
    @Discover
    @Cachable
    @RetryPolicy(exception = RemoteConnectFailureException.class)
    private OrderService orderService;

    @PostConstruct
    public void testConnection() throws InterruptedException {
       orderService.createOrder(new Order());
       orderService.createOrder(new Order());
       orderService.createOrder(new Order());
       orderService.createOrder(new Order());
       orderService.createOrder(new Order());
       orderService.createOrder(new Order());
       orderService.createOrder(new Order());
    }

}
