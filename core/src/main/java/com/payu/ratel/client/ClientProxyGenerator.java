package com.payu.ratel.client;

public interface ClientProxyGenerator {
    Object generate(Class<?> serviceClazz, String serviceAddress);
}
