package com.payu.soa.example.client;

import com.payu.server.model.Order;
import com.payu.server.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TestBean {

    private final OrderService orderService;

    @Autowired
    public TestBean(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostConstruct
    public void testConnection() {
        orderService.createOrder(new Order());
    }

}
