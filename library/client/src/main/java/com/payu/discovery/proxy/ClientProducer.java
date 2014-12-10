package com.payu.discovery.proxy;

public interface ClientProducer {
    Object produceLoadBalancer(Class<?> clazz);

    Object produceBroadcaster();
}
