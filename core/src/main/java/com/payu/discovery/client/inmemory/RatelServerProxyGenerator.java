package com.payu.discovery.client.inmemory;

import com.payu.discovery.client.ClientProxyDecorator;
import com.payu.discovery.client.ClientProxyGenerator;

public class RatelServerProxyGenerator implements ClientProxyGenerator {
    private final ClientProxyDecorator clientProxyDecorator;

    public RatelServerProxyGenerator(ClientProxyDecorator clientProxyDecorator) {
        this.clientProxyDecorator = clientProxyDecorator;
    }

    @Override
    public Object generate(Class<?> serviceClazz, String serviceAddress) {
        return clientProxyDecorator.decorateWithMonitoring(clientProxyDecorator.createServiceClientProxy(serviceClazz, serviceAddress),
                serviceClazz);
    }
}
