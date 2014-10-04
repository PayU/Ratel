package com.payu.soa.example.client;

import com.payu.discovery.RemoteService;
import com.payu.order.server.model.Order;
import com.payu.order.server.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SandboxTestBean {

    @Autowired
    @Lazy
    @RemoteService
    private OrderService orderService;
    
    @Autowired
    private Environment env;

    @PostConstruct
    public void testConnection() {
        orderService.createOrder(new Order());
        orderService.createOrder(new Order());
        orderService.createOrder(new Order());
        orderService.createOrder(new Order());
        orderService.createOrder(new Order());
        orderService.createOrder(new Order());
        orderService.createOrder(new Order());
        orderService.createOrder(new Order());
        
        System.err.println(env.getProperty("dummy.property"));
        System.err.println(env.getProperty("server.port"));
    }

}
