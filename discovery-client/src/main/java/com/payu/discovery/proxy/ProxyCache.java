package com.payu.discovery.proxy;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class ProxyCache implements java.lang.reflect.InvocationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyCache.class);

    private Object object;

    Cache<Method, Object> cache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    public ProxyCache(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        final Object cachedResults = cache.getIfPresent(method);
        if(cachedResults == null) {
            LOGGER.info("New invocation of method {}", method.getName());
            final Object results = method.invoke(object, args);
            if(results != null) {
                cache.put(method, results);
            }
            return results;
        }

        LOGGER.info("Cached invocation of method {}", method.getName());
        return cachedResults;
    }

}