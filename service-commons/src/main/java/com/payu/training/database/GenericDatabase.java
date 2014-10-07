package com.payu.training.database;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.payu.training.service.FailureGenerator;

public abstract class GenericDatabase<T extends Serializable> {
	
	@Autowired
	private FailureGenerator failureGenerator;
	
    private Map<Long, T> objects = new HashMap<>();

    public void create(T object) {
    	forceFailureIfNeeded();
        Long key = 1L;
        if (!objects.isEmpty())
            key = obtainLastKey();
        objects.put(key, object);
    }
    
    protected abstract void setId(T object, long id);
    
    protected abstract long getId(T object);    	
    
    private Long obtainLastKey() {
        return objects.entrySet().stream().max(Comparator.comparingLong(Map.Entry::getKey)).get().getKey() + 1;
    }
    
    public Collection<T> getAllObjects() {
    	return objects.values();
    }
    

    public T get(Long id) {
    	forceFailureIfNeeded();
        return objects.get(id);
    }

	public int size() {
		forceFailureIfNeeded();
		return objects.size();
	}

	public int clear() {
		failureGenerator.reset();
		int size = objects.size();
		objects.clear();
		return size;
	}
    
    public boolean delete(long id) {
    	forceFailureIfNeeded();
    	return objects.remove(id) != null;
    }

	private void forceFailureIfNeeded() {
		failureGenerator.forceFailureIfNeeded();
	}

	public void update(T object) {
		forceFailureIfNeeded();
		long key = getId(object);
		objects.replace(key, object);
	}
    
    
    
}
