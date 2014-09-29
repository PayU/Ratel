package com.payu.server.service;

import com.payu.server.model.Order;
import com.payu.server.model.OrderDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDatabase database;

    public void createOrder(Order order) {
        database.createOrder(order);
    }

    public Order getOrder(Long id) {
        return database.get(id);
    }

}
