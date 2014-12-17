package com.payu.discovery.client;

public interface ClientProxyGenerator {
    Object generate(Class<?> serviceClazz, String serviceAddress);
}
