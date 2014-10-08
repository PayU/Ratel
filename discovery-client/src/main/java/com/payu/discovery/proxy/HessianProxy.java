package com.payu.discovery.proxy;

import com.payu.discovery.client.DiscoveryClient;
import com.payu.discovery.model.ServiceDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HessianProxy implements java.lang.reflect.InvocationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(HessianProxy.class);

    private Class<?> serviceApi;

    private List<Object> clients = new ArrayList<>();

    private DiscoveryClient discoveryClient;

    private Map<String, Collection<ServiceDescriptor>> allServices() {
        return discoveryClient
                .fetchAllServices()
                .stream()
                .collect(Collectors.groupingBy(ServiceDescriptor::getName,
                        Collectors.toCollection(ArrayList::new)));
    }

    public HessianProxy(DiscoveryClient discoveryClient, Class<?> serviceApi) {
        this.discoveryClient = discoveryClient;
        this.serviceApi = serviceApi;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        if(CollectionUtils.isEmpty(allServices().get(serviceApi.getName()))) {
            throw new RuntimeException("Service " + serviceApi.getName() + " is not available");
        }

        clients.clear();
        clients.addAll(allServices().get(serviceApi.getName()).stream().map(serviceDescriptor -> decorateWithMonitoring(BinaryTransportUtil.
                        createServiceClientProxy(serviceApi, serviceDescriptor.getAddress()),
                serviceApi)).collect(Collectors.toList()));

        return method.invoke(clients.iterator().next(), args);
    }

    public Object decorateWithMonitoring(final Object object, final Class clazz) {
        return Proxy
                .newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class[]{clazz}, new ProxyMonitoring(object));
    }

}