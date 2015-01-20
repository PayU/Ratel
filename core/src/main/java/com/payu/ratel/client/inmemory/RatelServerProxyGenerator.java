package com.payu.ratel.client.inmemory;

import com.payu.ratel.client.ClientProxyDecorator;
import com.payu.ratel.client.ClientProxyGenerator;

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
