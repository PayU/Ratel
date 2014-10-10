package com.payu.order.server.service;

import com.payu.discovery.Publish;
import com.payu.order.server.model.Order;
import com.payu.order.server.model.OrderDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Publish
public class OrderServiceImpl implements OrderService {

	private static final Logger log = LoggerFactory
			.getLogger(OrderServiceImpl.class);

	@Autowired
	private OrderDatabase database;

	@Autowired
	private Environment env;

	public void createOrder(Order order) {
		log.info("Real order service call : create order {}", order);
		database.create(order);
	}

	public Order getOrder(Long id) {
		log.info("Real order service call getById {}", id);
		return database.get(id);
	}

	@Override
	public int deleteOrderById() {
		return database.clear();
	}

	@Override
	public Collection<Order> getOrdersByUserId(long userId) {
		return database.getAllObjects().stream()
				.filter(t -> t.getUserId() == userId)
				.collect(Collectors.toCollection(ArrayList::new));
	}

}
