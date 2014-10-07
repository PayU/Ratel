package com.payu.discovery.proxy;


import com.payu.discovery.client.DiscoveryClient;
import com.payu.discovery.model.ServiceDescriptor;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class HessianClientProducer {

    private DiscoveryClient discoveryClient;

    private volatile Map<String, Collection<ServiceDescriptor>> allServices = null;

    public HessianClientProducer(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    private Map<String, Collection<ServiceDescriptor>> allServices() {
        if (allServices == null) {
            synchronized (this) {
                if (allServices == null) {
                    allServices = discoveryClient
                            .fetchAllServices()
                            .stream()
                            .collect(Collectors
                                    .groupingBy(ServiceDescriptor::getName,
                                            Collectors.toCollection(ArrayList::new)));
                }
            }
        }
        return allServices;
    }

    public Object produce(Class<?> clazz, boolean monitoring) {
        String serviceName = clazz.getName();
        return monitoring ? decorateWithMonitoring(BinaryTransportUtil.
                createServiceClientProxy(clazz, allServices().get(serviceName).iterator().next().getAddress()), clazz)
                : BinaryTransportUtil
                .createServiceClientProxy(clazz, allServices().get(serviceName).iterator().next().getAddress());
    }

    public Object produce(Class<?> clazz) {
        return produce(clazz, true);
    }

    public Object decorateWithMonitoring(final Object object, final Class clazz) {
        return Proxy
                .newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class[]{clazz}, new ProxyMonitoring(object));
    }
}
