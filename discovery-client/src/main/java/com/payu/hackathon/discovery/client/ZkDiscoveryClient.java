package com.payu.hackathon.discovery.client;

import com.payu.hackathon.discovery.model.Service;

import java.util.Collection;
import java.util.function.Consumer;

public class ZkDiscoveryClient implements DiscoveryClient {

    @Override
    public Collection<Service> fetchAllServices() {
        return null;
    }

    @Override
    public void listenForServices(Collection<Service> services, Consumer<Collection<Service>> consumer) {

    }
}
