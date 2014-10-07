package com.payu.transaction.server.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.payu.discovery.RemoteService;
import com.payu.transaction.server.model.Transaction;
import com.payu.transaction.server.model.TransactionDatabase;

@Service
@RemoteService
public class TransactionServiceImpl implements TransactionService {

	private static final Logger log = LoggerFactory
			.getLogger(TransactionServiceImpl.class);

	@Autowired
	private TransactionDatabase database;

	public void createTransaction(Transaction transaction) {
		log.info("Real Transaction service call : create Transaction {}",
				transaction);
		database.create(transaction);
	}

	public Transaction getTransactionById(Long id) {
		log.info("Real Transaction service call getById {}", id);
		return database.get(id);
	}

	@Override
	public int deletTransactions() {
		return database.clear();
	}

	@Override
	public Collection<Transaction> getTransactionByOrderId(long orderId) {
		return database.getAllObjects().stream()
				.filter(t -> t.getOrderId() == orderId)
				.collect(Collectors.toCollection(ArrayList::new));
	}

}
