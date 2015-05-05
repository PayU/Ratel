package com.payu.ratel.config.beans;

import org.springframework.scheduling.TaskScheduler;

import com.payu.ratel.client.ClientProxyDecorator;
import com.payu.ratel.client.inmemory.DiscoveryClient;
import com.payu.ratel.client.inmemory.RatelServerFetcher;
import com.payu.ratel.client.inmemory.RatelServerProxyGenerator;
import com.payu.ratel.register.inmemory.RatelServerRegistry;
import com.payu.ratel.register.inmemory.RemoteRestDiscoveryServer;

public class InMemoryServiceRegistryStrategy implements RegistryStrategiesProvider {
    private RatelServerRegistry ratelServerRegistry;
    private RatelServerProxyGenerator ratelServerProxyGenerator;
    private RatelServerFetcher ratelServerFetcher;

    public InMemoryServiceRegistryStrategy() {
    }

    public RatelServerRegistry getRegisterStrategy() {
        return ratelServerRegistry;
    }

    public RatelServerProxyGenerator getClientProxyGenerator() {
        return ratelServerProxyGenerator;
    }

    public RatelServerFetcher getFetchStrategy() {
        return ratelServerFetcher;
    }

    public void configureWithServiceDiscoveryAddress(final String inMemoryServerAddress, TaskScheduler taskScheduler) {
        final DiscoveryClient discoveryClient = new DiscoveryClient(inMemoryServerAddress);
        this.ratelServerFetcher = new RatelServerFetcher(discoveryClient);

        this.ratelServerProxyGenerator = new RatelServerProxyGenerator(new ClientProxyDecorator());

        this.ratelServerRegistry = new RatelServerRegistry(new RemoteRestDiscoveryServer(inMemoryServerAddress),
                taskScheduler);
    }
}
