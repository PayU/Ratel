package com.payu.discovery.proxy;


import com.payu.discovery.client.DiscoveryClient;

import java.lang.reflect.Proxy;

public class HessianClientProducer {

    private DiscoveryClient discoveryClient;

    public HessianClientProducer(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public Object produce(Class<?> clazz) {
        return Proxy
                .newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class[]{clazz}, new LoadBalancingInvocationHandler(discoveryClient, clazz));
    }

}
