package com.payu.transaction.server.service;

import java.util.Collection;

import com.payu.transaction.server.model.Transaction;

public interface TransactionService {

    void createTransaction(Transaction transaction);

    public Transaction getTransactionById(Long id);

	public abstract int deletTransactions();
	
	public Collection<Transaction> getTransactionByOrderId(long orderId);


}
