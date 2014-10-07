package com.payu.discovery.proxy;

public interface ServiceProxyProducer {

	public abstract <T> T produce(Class<T> clazz);

}