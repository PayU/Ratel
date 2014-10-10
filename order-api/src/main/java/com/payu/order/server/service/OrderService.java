package com.payu.order.server.service;

import java.util.Collection;

import com.payu.order.server.model.Order;

public interface OrderService {

    void createOrder(Order order);

    public Order getOrder(Long id);

	public abstract int deleteOrderById();
	
	public Collection<Order> getOrdersByUserId(long userId);


}
