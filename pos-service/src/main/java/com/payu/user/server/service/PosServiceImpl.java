package com.payu.user.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.payu.training.database.GenericDatabase;
import com.payu.user.server.model.Pos;


@Service
public class PosServiceImpl implements PosService {
	

    private static final Logger log = LoggerFactory.getLogger(PosServiceImpl.class);

    @Autowired
    private GenericDatabase<Pos> database;
    

    public void createPos(Pos user) {
        database.create(user);
        log.info("Real Pos service call : create Pos {}", user);
    }

    public Pos getPosById(Long id) {
        log.info("Real Pos service call getById {}", id);
        return database.get(id);
    }
    
    @Override
	public int deletePoses() {
    	log.info("Real Pos service call deletePoss ");
    	return database.clear();
    }

	public void activatePos(long userId) {
		Pos user = database.get(userId);
		user.setActive(true);
		database.update(user);
		
	}

}
