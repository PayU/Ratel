package com.payu.transaction.server.model;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@Service
public class TransactionDatabase {
    private Map<Long, Transaction> orders = new HashMap<>();

    public void createTransaction(Transaction order) {
        Long key = 1L;
        if (!orders.isEmpty())
            key = obtainLastKey();
        orders.put(key, order);

    }

    private Long obtainLastKey() {
        return orders.entrySet().stream().max(Comparator.comparingLong(Map.Entry::getKey)).get().getKey() + 1;
    }

    public Transaction get(Long id) {
        return orders.get(id);
    }

	public int size() {
		return orders.size();
	}

	public void clear() {
		orders.clear();
	}
    
    
}
