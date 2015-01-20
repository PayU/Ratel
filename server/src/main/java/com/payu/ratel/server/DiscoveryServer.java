package com.payu.ratel.server;

import com.payu.ratel.model.ServiceDescriptor;

import java.util.Collection;
import java.util.Map;

public interface DiscoveryServer {

    void registerService(ServiceDescriptor serviceDescriptor);

    Collection<ServiceDescriptor> fetchAllServices();
    
    void deleteAllServices();

    void collectStatistics(String service, Map<String, Map<String, String>> statistics);

}
