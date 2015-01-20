package com.payu.discovery.proxy;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class CacheInvocationHandler implements java.lang.reflect.InvocationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheInvocationHandler.class);

    private Object object;

    Cache<MethodWithArguments, Object> cache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    public CacheInvocationHandler(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        final MethodWithArguments methodWithArguments = new MethodWithArguments(method, args);
        final Object cachedResults = cache.getIfPresent(methodWithArguments);
        if(cachedResults == null) {
            LOGGER.info("New invocation of method {}", method.getName());
            final Object results = method.invoke(object, args);
            if(results != null) {
                cache.put(methodWithArguments, results);
            }
            return results;
        }

        LOGGER.info("Cached invocation of method {}", method.getName());
        return cachedResults;
    }

    private class MethodWithArguments {
        Method method;
        Object[] args;

        private MethodWithArguments(Method method, Object[] args) {
            this.method = method;
            this.args = args;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MethodWithArguments that = (MethodWithArguments) o;

            // Probably incorrect - comparing Object[] arrays with Arrays.equals
            if (!Arrays.equals(args, that.args)) return false;
            if (!method.equals(that.method)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = method.hashCode();
            result = 31 * result + Arrays.hashCode(args);
            return result;
        }
    }

}