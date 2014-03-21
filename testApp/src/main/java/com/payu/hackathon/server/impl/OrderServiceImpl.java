package com.payu.hackathon.server.impl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.payu.hackathon.server.model.Order;
import com.payu.hackathon.server.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    private Map<Long, Order> orders = new HashMap<>();

    private Long obtainLastKey() {
        return orders.entrySet().stream().max(Comparator.comparingLong(Map.Entry<Long,
                Order>::getKey)).get().getKey() + 1;
    }

    @Override
    public void createOrder(Order order) {
        Long key = 1L;
        if (!orders.isEmpty())
            key = obtainLastKey();
        orders.put(key, order);
    }

    @Override
    public Order getOrder(Long id) {
        return orders.get(id);
    }
}
