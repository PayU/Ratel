package com.payu.soa.example.client;

import com.payu.discovery.proxy.RemoteService;
import com.payu.server.model.Order;
import com.payu.server.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    @Lazy
    @RemoteService
    private OrderService orderService;

    public void test() {
        createOrder(new Order());
    }

    @Override
    public void createOrder(Order order) {
        orderService.createOrder(order);
    }

    @Override
    public Order getOrder(Long id) {
        return orderService.getOrder(id);
    }

}
