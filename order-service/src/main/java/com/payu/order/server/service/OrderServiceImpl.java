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
	
	private int counter; 

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDatabase database;

    public void createOrder(Order order) {
    	if ((counter++) % 3  == 0 ){
    		throw new RuntimeException();
    	}
        database.createOrder(order);
        log.info("Real order service call : create order {}", order);
    }

    public Order getOrder(Long id) {
        log.info("Real order service call getById {}", id);
        return database.get(id);
    }
    
    @Override
	public int deletOrders() {
    	int result = database.size();
    	database.clear();
    	counter = 0;
    	return result;
    }

}
