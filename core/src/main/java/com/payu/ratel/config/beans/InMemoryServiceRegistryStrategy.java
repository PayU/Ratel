package com.payu.ratel.config.beans;

import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;

import com.payu.ratel.client.ClientProxyGenerator;
import com.payu.ratel.client.inmemory.DiscoveryClient;
import com.payu.ratel.client.inmemory.RatelServerFetcher;
import com.payu.ratel.client.inmemory.RatelServerProxyGenerator;
import com.payu.ratel.register.inmemory.RatelServerRegistry;
import com.payu.ratel.register.inmemory.RemoteRestDiscoveryServer;

public class InMemoryServiceRegistryStrategy implements RegistryStrategiesProvider {

    private static final String DEFAULT_DISCOVERY_URL = "http://localhost:8090/server/discovery";

    private RatelServerRegistry ratelServerRegistry;
    private ClientProxyGenerator ratelServerProxyGenerator;
    private RatelServerFetcher ratelServerFetcher;

    public InMemoryServiceRegistryStrategy() {
    }

    public RatelServerRegistry getRegisterStrategy() {
        return ratelServerRegistry;
    }

    public ClientProxyGenerator getClientProxyGenerator() {
        return ratelServerProxyGenerator;
    }

    public RatelServerFetcher getFetchStrategy() {
        return ratelServerFetcher;
    }

    public void configure(final Environment env, TaskScheduler taskScheduler) {
        final String inMemoryServerAddress = env.getProperty(
                RegistryBeanProviderFactory.SERVICE_DISCOVERY_ADDRESS, DEFAULT_DISCOVERY_URL);
        final DiscoveryClient discoveryClient = new DiscoveryClient(inMemoryServerAddress);
        this.ratelServerFetcher = new RatelServerFetcher(discoveryClient);

        this.ratelServerProxyGenerator = new RatelServerProxyGenerator(env);

        this.ratelServerRegistry = new RatelServerRegistry(new RemoteRestDiscoveryServer(inMemoryServerAddress),
                taskScheduler);
    }
}
