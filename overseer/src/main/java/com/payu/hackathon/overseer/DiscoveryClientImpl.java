package com.payu.hackathon.overseer;

import java.util.Collection;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.payu.hackathon.discovery.client.DiscoveryClient;
import com.payu.hackathon.discovery.client.ZkDiscoveryClient;
import com.payu.hackathon.discovery.model.Service;

@Component
public class DiscoveryClientImpl implements DiscoveryClient {

    private final ZookeeperProperties zookeeperProperties;
    private DiscoveryClient zkDiscoveryClient;

    @Autowired
    public DiscoveryClientImpl(ZookeeperProperties zookeeperProperties) {
        this.zkDiscoveryClient = zkDiscoveryClient;
        this.zookeeperProperties = zookeeperProperties;
    }

    @PostConstruct
    public void init() {

        zkDiscoveryClient = new ZkDiscoveryClient(zookeeperProperties.getConnectionUrl());

    }

    @Override
    public Collection<Service> fetchAllServices() {
        return zkDiscoveryClient.fetchAllServices();
    }

    @Override
    public void listenForServices(Collection<String> services, Consumer<Service> consumer) {
        zkDiscoveryClient.listenForServices(services, consumer);
    }

}
