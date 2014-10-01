package com.payu.soa.example.client;

import com.payu.server.model.Order;
import com.payu.server.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;

public class TestBean {

    @Autowired
    @Lazy
    private OrderService orderService;

    @PostConstruct
    public void test() {
        orderService.createOrder(new Order());
    }


}
