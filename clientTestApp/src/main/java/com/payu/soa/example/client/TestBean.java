package com.payu.soa.example.client;

import com.payu.discovery.EnableCache;
import com.payu.discovery.EnableRetryPolicy;
import com.payu.discovery.RemoteService;
import com.payu.order.server.model.Order;
import com.payu.order.server.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TestBean {

    @Autowired
    @RemoteService
    @EnableCache
    @EnableRetryPolicy(exception = RemoteConnectFailureException.class)
    private OrderService orderService;

    @PostConstruct
    public void testConnection() {
        orderService.createOrder(new Order());
    }

}
