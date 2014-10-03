package com.payu.order.server.service;

import com.payu.discovery.RemoteService;
import com.payu.order.server.model.Order;
import com.payu.order.server.model.OrderDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@RemoteService
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDatabase database;

    public void createOrder(Order order) {
        database.createOrder(order);
        log.info("Real order service call : create order {}", order);
    }

    public Order getOrder(Long id) {
        log.info("Real order service call getById {}", id);
        return database.get(id);
    }

}
