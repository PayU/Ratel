package com.payu.discovery.proxy;

import com.payu.discovery.client.DiscoveryClient;
import com.payu.discovery.event.EventReceiver;
import com.payu.discovery.model.ServiceDescriptor;
import com.payu.discovery.proxy.monitoring.MonitoringInvocationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BroadcastingInvocationHandler implements java.lang.reflect.InvocationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BroadcastingInvocationHandler.class);

    private static final Method RECEIVE_METHOD_HANDLER = getReceiveEventHandler();

    private static Method getReceiveEventHandler() {
        try {
            return EventReceiver.class.getDeclaredMethod("receiveEvent", Serializable.class);
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        }
    }

    private class ServiceClient {

        public String address;

        public Object clientProxy;

        private ServiceClient(String address, Object clientProxy) {
            this.address = address;
            this.clientProxy = clientProxy;
        }

    }

    private LinkedList<ServiceClient> clients = new LinkedList<>();

    private DiscoveryClient discoveryClient;

    private Map<String, List<ServiceDescriptor>> allServices() {

        return discoveryClient
                .fetchAllServices()
                .stream()
                .collect(Collectors.groupingBy(ServiceDescriptor::getName,
                        Collectors.toList()));
    }

    public BroadcastingInvocationHandler(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    public synchronized Object invoke(Object o, Method method, Object[] args) throws Throwable {
        final Collection<ServiceDescriptor> fetchesServices = allServices().get(EventReceiver.class.getName());
        if(CollectionUtils.isEmpty(fetchesServices)) {
            return null;
        }

        if(clients.isEmpty()) {
            createAllServices(fetchesServices);
        } else {
            updateServices(fetchesServices);
        }

        clients.stream().forEach(client -> {
            try {
                LOGGER.info("Sending events to ");
                RECEIVE_METHOD_HANDLER.invoke(client.clientProxy, args);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        return null;
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
    }

    private void createAllServices(Collection<ServiceDescriptor> fetchesServices) {
        fetchesServices.stream().forEach(service -> clients.add(createNewService(service)));
    }

    private ServiceClient createNewService(ServiceDescriptor serviceDescriptor) {
        return new ServiceClient(serviceDescriptor.getAddress(), decorateWithMonitoring(BinaryTransportUtil
                        .createServiceClientProxy(EventReceiver.class, serviceDescriptor.getAddress()), EventReceiver.class));
    }

    public Object decorateWithMonitoring(final Object object, final Class<EventReceiver> clazz) {
        return Proxy
                .newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class[]{clazz}, new MonitoringInvocationHandler(object));
    }

}