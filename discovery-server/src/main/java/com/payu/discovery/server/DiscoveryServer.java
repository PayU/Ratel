package com.payu.discovery.server;

import com.payu.discovery.model.Service;

import java.util.Collection;

public interface DiscoveryServer {

    void registerService(Service service);

    Collection<Service> fetchAllServices();

}
