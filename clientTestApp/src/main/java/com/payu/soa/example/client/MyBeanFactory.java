package com.payu.soa.example.client;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.AutowireCandidateResolver;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public class MyBeanFactory extends DefaultListableBeanFactory implements AutowireCapableBeanFactory {

	private MyAutowireCandidateResolver myAutowireCandidateResolver = new MyAutowireCandidateResolver(); {
		myAutowireCandidateResolver.setBeanFactory(this);
	}

    @Override
	public AutowireCandidateResolver getAutowireCandidateResolver() { 
		return myAutowireCandidateResolver;
	}
}
