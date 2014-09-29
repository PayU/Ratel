package com.payu.server.service;

import com.payu.server.model.Order;

public interface OrderService {

    void createOrder(Order order);

    public Order getOrder(Long id);

}
