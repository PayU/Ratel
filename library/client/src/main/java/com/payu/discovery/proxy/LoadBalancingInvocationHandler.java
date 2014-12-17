package com.payu.discovery.proxy;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.payu.discovery.client.DiscoveryClient;
import com.payu.discovery.model.ServiceDescriptor;
import com.payu.discovery.proxy.monitoring.MonitoringInvocationHandler;

public class LoadBalancingInvocationHandler implements java.lang.reflect.InvocationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadBalancingInvocationHandler.class);

    private class ServiceClient {
        public String address;

        public Object clientProxy;
        private ServiceClient(String address, Object clientProxy) {
            this.address = address;
            this.clientProxy = clientProxy;
        }

    }

    private Class<?> serviceApi;

    private LinkedList<ServiceClient> clients = new LinkedList<>();

    private DiscoveryClient discoveryClient;

    private ListIterator<ServiceClient> loadBalancingIterator;

    private Map<String, List<ServiceDescriptor>> allServices() {

        Collection<ServiceDescriptor> serviceDescriptors = discoveryClient.fetchAllServices();

        Map<String, List<ServiceDescriptor>> allServices = new HashMap<>();

        for(ServiceDescriptor serviceDescriptor:serviceDescriptors){
            String name = serviceDescriptor.getName();

            if(!allServices.containsKey(name)){
                allServices.put(name, new ArrayList<ServiceDescriptor>());
            }

            allServices.get(name).add(serviceDescriptor);
        }

        return allServices;
    }

    public LoadBalancingInvocationHandler(DiscoveryClient discoveryClient, Class<?> serviceApi) {
        this.discoveryClient = discoveryClient;
        this.serviceApi = serviceApi;
    }

    @Override
    public synchronized Object invoke(Object o, Method method, Object[] args) throws Throwable {
        final Collection<ServiceDescriptor> fetchesServices = allServices().get(serviceApi.getName());
        if(CollectionUtils.isEmpty(fetchesServices)) {
            throw new RuntimeException("Service " + serviceApi.getName() + " is not available");
        }

        if(clients.isEmpty()) {
            createAllServices(fetchesServices);
        } else {
            updateServices(fetchesServices);
        }

        if(loadBalancingIterator == null || !loadBalancingIterator.hasNext()) {
            loadBalancingIterator = clients.listIterator();
        }

        return method.invoke(loadBalancingIterator.next().clientProxy, args);
    }

    private void updateServices(Collection<ServiceDescriptor> fetchesServices) {
        final Map<String, ServiceDescriptor> servicesMap = new HashMap<>();

        for(ServiceDescriptor serviceDescriptor:fetchesServices){
            servicesMap.put(serviceDescriptor.getAddress(), serviceDescriptor);
        }

        final Iterator<ServiceClient> iterator = clients.iterator();
        while(iterator.hasNext()) {
            final ServiceClient next = iterator.next();
            if(servicesMap.remove(next.address) == null) {
                iterator.remove();
            }
        }

        if(loadBalancingIterator == null) {
            loadBalancingIterator = clients.listIterator();
        }

        for(ServiceDescriptor serviceDescriptor: servicesMap.values()){
            loadBalancingIterator.add(createNewService(serviceDescriptor));
        }
    }

    private void createAllServices(Collection<ServiceDescriptor> fetchesServices) {
        for (ServiceDescriptor service : fetchesServices){
            clients.add(createNewService(service));
        }
    }

    private ServiceClient createNewService(ServiceDescriptor serviceDescriptor) {
        return new ServiceClient(serviceDescriptor.getAddress(), decorateWithMonitoring(BinaryTransportUtil
                        .createServiceClientProxy(serviceApi, serviceDescriptor.getAddress()), serviceApi));
    }

    public Object decorateWithMonitoring(final Object object, final Class clazz) {
        return Proxy
                .newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class[]{clazz}, new MonitoringInvocationHandler(object));
    }

}