package com.payu.discovery.server;

import com.payu.discovery.model.ServiceDescriptor;

import java.util.Collection;

public interface DiscoveryServer {

    void registerService(ServiceDescriptor serviceDescriptor);

    Collection<ServiceDescriptor> fetchAllServices();
    
    void deleteAllServices();

    void ping(String remoteAddress);

}
