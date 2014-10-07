package com.payu.transaction.server.model;

import org.springframework.stereotype.Service;

import com.payu.training.database.GenericDatabase;

@Service
public class TransactionDatabase extends GenericDatabase<Transaction> {
	@Override
	protected void setId(Transaction object, long id) {
		object.setId(id);
	}

	@Override
	protected long getId(Transaction object) {
		return object.getId();
	}
}