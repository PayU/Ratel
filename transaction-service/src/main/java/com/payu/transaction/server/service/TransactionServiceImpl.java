package com.payu.transaction.server.service;

import com.payu.discovery.RemoteService;
import com.payu.transaction.server.model.Transaction;
import com.payu.transaction.server.model.TransactionDatabase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;


@Service
@RemoteService
public class TransactionServiceImpl implements TransactionService {
	
	private int counter; 

    private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private TransactionDatabase database;
    
    @Autowired
    private Environment env;

    public void createTransaction(Transaction transaction) {
    	forceTimeoutIfNeeded();
        database.createTransaction(transaction);
        log.info("Real Transaction service call : create Transaction {}", transaction);
    }

	private void forceTimeoutIfNeeded() {
		int exceptionPeriod = env.getProperty("force.exception.period", Integer.class, 0);
		if (exceptionPeriod > 0 && ((++counter) % exceptionPeriod  != 0) ){
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {}
        }
	}

    public Transaction getTransactionById(Long id) {
        log.info("Real Transaction service call getById {}", id);
        return database.get(id);
    }
    
    @Override
	public int deletTransactions() {
    	int result = database.size();
    	database.clear();
    	counter = 0;
    	return result;
    }

}
