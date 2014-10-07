package com.payu.order.server.model;

import org.springframework.stereotype.Service;

import com.payu.training.database.GenericDatabase;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrderDatabase extends GenericDatabase<Order> {

	@Override
	protected void setId(Order object, long id) {
		object.setId(id);
	}

	@Override
	protected long getId(Order object) {
		return object.getId();
	}
	
}
