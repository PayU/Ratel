package com.payu.training.database;


import java.io.Serializable;

import com.payu.training.model.LongIdentified;

public class LongIdentifiedDatabase<T extends LongIdentified & Serializable> extends GenericDatabase<T> {

	@Override
	protected void setId(T object, long id) {
		object.setId(id);
	}

	@Override
	protected long getId(T object) {
		return object.getId();
	}
}

