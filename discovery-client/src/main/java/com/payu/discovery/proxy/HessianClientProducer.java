package com.payu.discovery.proxy;


import com.payu.discovery.model.ServiceDescriptor;

import java.util.Map;

public class HessianClientProducer {

    private final Map<String, ServiceDescriptor> allServices;

    public HessianClientProducer(Map<String, ServiceDescriptor> allServices) {
        this.allServices = allServices;
    }

    public Object produce(Class<?> clazz) {
        String serviceName = clazz.getName();
        return BinaryTransportUtil.
                createServiceClientProxy(clazz,
                        allServices.get(serviceName).getAddress());
    }
}
