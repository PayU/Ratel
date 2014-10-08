package com.payu.soa.example.client;

import com.payu.discovery.EnableCache;
import com.payu.discovery.EnableRetryPolicy;
import com.payu.discovery.RemoteService;
import com.payu.order.server.model.Order;
import com.payu.order.server.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TestBean {

    @Autowired
    @Lazy
    @RemoteService
    @EnableCache
    @EnableRetryPolicy(exception = RemoteConnectFailureException.class)
    private OrderService orderService;

    @PostConstruct
    public void testConnection() throws InterruptedException {
        try {
            orderService.createOrder(new Order());
        } catch (Throwable e) {
            Thread.sleep(30000);
        }
        orderService.createOrder(new Order());
    }

}
