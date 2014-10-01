package com.payu.order.server.service;

import com.payu.order.server.model.Order;

public interface OrderService {

    void createOrder(Order order);

    public Order getOrder(Long id);


}
