package com.payu.ratel.client.standalone;

import com.payu.ratel.client.RatelClientProducer;
import com.payu.ratel.config.beans.InMemoryServiceRegistryStrategy;
import com.payu.ratel.config.beans.ZookeeperServiceRegistryStrategy;

public class RatelStandaloneFactory {

    private RatelClientProducer clientProducer;

    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    public static RatelStandaloneFactory fromZookeeperServer(String zookeeperAddress) throws Exception {
        RatelStandaloneFactory result = new RatelStandaloneFactory();
        ZookeeperServiceRegistryStrategy zookeeperStrategy = new ZookeeperServiceRegistryStrategy();
        zookeeperStrategy.configureWithZookeeperHost(zookeeperAddress);
        result.clientProducer = new RatelClientProducer(zookeeperStrategy.getFetchStrategy(),
                zookeeperStrategy.getClientProxyGenerator());
        return result;
    }

    public static RatelStandaloneFactory fromRatelServer(String ratelServerAddr) {
        RatelStandaloneFactory result = new RatelStandaloneFactory();
        InMemoryServiceRegistryStrategy ratelStrategy = new InMemoryServiceRegistryStrategy();
        ratelStrategy.configureWithServiceDiscoveryAddress(ratelServerAddr, null);
        result.clientProducer = new RatelClientProducer(ratelStrategy.getFetchStrategy(),
                ratelStrategy.getClientProxyGenerator());
        return result;
    }

    public <T> T getServiceProxy(Class<T> serviceContractClass) {
        return clientProducer.produceServiceProxy(serviceContractClass, false, null);
    }
}

