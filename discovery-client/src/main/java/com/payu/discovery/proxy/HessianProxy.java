package com.payu.discovery.proxy;

import com.payu.discovery.client.DiscoveryClient;
import com.payu.discovery.model.ServiceDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HessianProxy implements java.lang.reflect.InvocationHandler {

    private class ServiceClient {
        public String address;
        public Object clientProxy;

        private ServiceClient(String address, Object clientProxy) {
            this.address = address;
            this.clientProxy = clientProxy;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(HessianProxy.class);

    private Class<?> serviceApi;

    private LinkedList<ServiceClient> clients = new LinkedList<>();

    private DiscoveryClient discoveryClient;

    private ListIterator<ServiceClient> listIterator;

    private Map<String, List<ServiceDescriptor>> allServices() {

        return discoveryClient
                .fetchAllServices()
                .stream()
                .collect(Collectors.groupingBy(ServiceDescriptor::getName,
                        Collectors.toList()));
    }

    public HessianProxy(DiscoveryClient discoveryClient, Class<?> serviceApi) {
        this.discoveryClient = discoveryClient;
        this.serviceApi = serviceApi;
    }

    @Override
    public synchronized Object invoke(Object o, Method method, Object[] args) throws Throwable {
        final Collection<ServiceDescriptor> fetchesServices = allServices().get(serviceApi.getName());
        if(CollectionUtils.isEmpty(fetchesServices)) {
            throw new RuntimeException("Service " + serviceApi.getName() + " is not available");
        }

        //FIXME: clients always is not null!
        if(clients == null) {
            createAllServices(fetchesServices);
        } else {
            updateServices(fetchesServices);
        }

        if(!listIterator.hasNext()) {
            listIterator = clients.listIterator();
        }

        return method.invoke(listIterator.next().clientProxy, args);
    }

    private void updateServices(Collection<ServiceDescriptor> fetchesServices) {
        final Map<String, ServiceDescriptor> servicesMap = fetchesServices.stream().collect(Collectors.toMap(ServiceDescriptor::getAddress, Function.identity()));
        final Iterator<ServiceClient> iterator = clients.iterator();
        while(iterator.hasNext()) {
            final ServiceClient next = iterator.next();
            if(servicesMap.remove(next.address) == null) {
                iterator.remove();
            }
        }

        if(listIterator == null) {
            listIterator = clients.listIterator();
        }

        servicesMap.values().stream()
                .forEach(serviceDescriptor -> listIterator.add(createNewService(serviceDescriptor)));
    }

    private void createAllServices(Collection<ServiceDescriptor> fetchesServices) {
        fetchesServices.stream().forEach(this::createNewService);
    }

    private ServiceClient createNewService(ServiceDescriptor serviceDescriptor) {
        return new ServiceClient(serviceDescriptor.getAddress(), decorateWithMonitoring(BinaryTransportUtil
                        .createServiceClientProxy(serviceApi, serviceDescriptor.getAddress()),
                serviceApi));
    }

    public Object decorateWithMonitoring(final Object object, final Class clazz) {
        return Proxy
                .newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class[]{clazz}, new ProxyMonitoring(object));
    }

}