package com.payu.user.server.service;

import org.springframework.stereotype.Component;

import com.payu.training.database.GenericDatabase;
import com.payu.user.server.model.User;

@Component
public class UserDatabase extends GenericDatabase<User>{

	@Override
	protected void setId(User object, long id) {
		object.setId(id);
	}

	@Override
	protected long getId(User object) {
		return object.getId();
	}
	
}
