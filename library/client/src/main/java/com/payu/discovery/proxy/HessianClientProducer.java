package com.payu.discovery.proxy;


import com.payu.discovery.client.DiscoveryClient;
import com.payu.discovery.event.EventCannon;

import java.lang.reflect.Proxy;

public class HessianClientProducer {

    private DiscoveryClient discoveryClient;

    public HessianClientProducer(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public Object produceLoadBalancer(Class<?> clazz) {
        return Proxy
                .newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class[]{clazz}, new LoadBalancingInvocationHandler(discoveryClient, clazz));
    }

    public Object produceBroadcaster() {
        return Proxy
                .newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class[]{EventCannon.class}, new BroadcastingInvocationHandler(discoveryClient));
    }
}
