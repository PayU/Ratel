package com.payu.discovery.server;

import com.payu.discovery.model.ServiceDescriptor;

import java.util.Collection;
import java.util.Map;

public interface DiscoveryServer {

    void registerService(ServiceDescriptor serviceDescriptor);

    Collection<ServiceDescriptor> fetchAllServices();
    
    void deleteAllServices();

    void collectStatistics(String service, Map<String, Map<String, String>> statistics);

}
