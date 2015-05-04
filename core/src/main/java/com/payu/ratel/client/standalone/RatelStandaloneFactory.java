package com.payu.ratel.client.standalone;

import com.payu.ratel.client.RatelClientProducer;
import com.payu.ratel.config.beans.ZookeeperServiceRegistryStrategy;

public class RatelStandaloneFactory {


    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    public static RatelStandaloneFactory fromZookeeperServer(String zookeeperAddress) throws Exception {
        RatelStandaloneFactory result = new RatelStandaloneFactory();
        ZookeeperServiceRegistryStrategy zookeeperStrategy = new ZookeeperServiceRegistryStrategy();
        zookeeperStrategy.configureWithZookeeperHost(zookeeperAddress);
        result.clientProducer = new RatelClientProducer(zookeeperStrategy.getFetchStrategy(), zookeeperStrategy.getClientProxyGenerator());
        return result;
    }

    private RatelClientProducer clientProducer;

    public <T> T getServiceProxy(Class<T> serviceContractClass) {
        return clientProducer.produceServiceProxy(serviceContractClass, false, null);
    }
}

