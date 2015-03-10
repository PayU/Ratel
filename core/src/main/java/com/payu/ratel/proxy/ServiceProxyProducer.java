package com.payu.ratel.proxy;

public interface ServiceProxyProducer {

	public abstract <T> T produce(Class<T> clazz);

}