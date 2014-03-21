package com.payu.hackathon.overseer;

import java.util.Collection;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.payu.hackathon.discovery.client.DiscoveryClient;
import com.payu.hackathon.discovery.client.ZkDiscoveryClient;
import com.payu.hackathon.discovery.model.Service;

@Component
public class DiscoveryClientImpl implements DiscoveryClient {

    private DiscoveryClient zkDiscoveryClient;

    @PostConstruct
    public void init() {

        zkDiscoveryClient = new ZkDiscoveryClient();

    }

    @Override
    public Collection<Service> fetchAllServices() {
        return zkDiscoveryClient.fetchAllServices();
    }

    @Override
    public void listenForServices(Collection<Service> services, Consumer<Collection<Service>> consumer) {
        zkDiscoveryClient.listenForServices(services, consumer);
    }
}
