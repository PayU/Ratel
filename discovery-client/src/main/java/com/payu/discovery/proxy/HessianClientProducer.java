package com.payu.discovery.proxy;


import com.payu.discovery.model.ServiceDescriptor;

import java.lang.reflect.Proxy;
import java.util.Map;

public class HessianClientProducer {

    private final Map<String, ServiceDescriptor> allServices;

    public HessianClientProducer(Map<String, ServiceDescriptor> allServices) {
        this.allServices = allServices;
    }

    public Object produce(Class<?> clazz, boolean monitoring) {
        String serviceName = clazz.getName();
        return monitoring ? decorateWithMonitoring(BinaryTransportUtil.
                createServiceClientProxy(clazz, allServices.get(serviceName).getAddress()), clazz)
                : BinaryTransportUtil
                .createServiceClientProxy(clazz, allServices.get(serviceName).getAddress());
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
