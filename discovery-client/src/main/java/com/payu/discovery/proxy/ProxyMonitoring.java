package com.payu.discovery.proxy;

import com.google.common.collect.EvictingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.stream.Collectors;

class ProxyMonitoring implements java.lang.reflect.InvocationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyMonitoring.class);

    private Object object;

    private EvictingQueue lastInvocations = EvictingQueue.create(10);

    public ProxyMonitoring(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        long before = System.currentTimeMillis();
        final Object returned = method.invoke(object, args);
        long after = System.currentTimeMillis();
        long took = after - before;
        LOGGER.info("Invocation of method {} took {} ms", method.getName(), took);
        lastInvocations.add(took);
        double avarage = (Double) lastInvocations.stream().collect(Collectors.averagingLong(inv -> (Long) inv));
        LOGGER.info("Avg for last 10 invocations {}", avarage);
        return returned;
    }



}