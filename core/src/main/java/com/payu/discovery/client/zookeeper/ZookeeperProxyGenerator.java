package com.payu.discovery.client.zookeeper;

import com.payu.discovery.client.ClientProxyDecorator;
import com.payu.discovery.client.ClientProxyGenerator;

public class ZookeeperProxyGenerator implements ClientProxyGenerator {
    private final ClientProxyDecorator clientProxyDecorator;

    public ZookeeperProxyGenerator(ClientProxyDecorator clientProxyDecorator) {
        this.clientProxyDecorator = clientProxyDecorator;
    }

    @Override
    public Object generate(Class<?> serviceClazz, String serviceAddress) {
        return clientProxyDecorator.createServiceClientProxy(serviceClazz, serviceAddress);
    }
}
