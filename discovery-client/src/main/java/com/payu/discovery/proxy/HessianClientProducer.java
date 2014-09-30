package com.payu.discovery.proxy;


import com.payu.discovery.model.Service;

import java.util.Map;

public class HessianClientProducer {

    private final Map<String, Service> allServices;

    public HessianClientProducer(Map<String, Service> allServices) {
        this.allServices = allServices;
    }

    public Object produce(Class<?> clazz) {
        String serviceName = clazz.getName();
        return BinaryTransportUtil.
                createServiceClientProxy(clazz,
                        allServices.get(serviceName).getAddress());
    }
}
