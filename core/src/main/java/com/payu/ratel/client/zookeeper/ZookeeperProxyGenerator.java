package com.payu.ratel.client.zookeeper;

import com.payu.ratel.client.ClientProxyDecorator;
import com.payu.ratel.client.ClientProxyGenerator;

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
