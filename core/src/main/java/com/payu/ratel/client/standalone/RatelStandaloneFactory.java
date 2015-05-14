package com.payu.ratel.client.standalone;

import static com.payu.ratel.config.beans.RegistryBeanProviderFactory.SERVICE_DISCOVERY_ADDRESS;
import static com.payu.ratel.config.beans.RegistryBeanProviderFactory.SERVICE_DISCOVERY_ZK_HOST;

import com.payu.ratel.client.RatelClientProducer;
import com.payu.ratel.config.beans.InMemoryServiceRegistryStrategy;
import com.payu.ratel.config.beans.ZookeeperServiceRegistryStrategy;

public class RatelStandaloneFactory {

    private RatelClientProducer clientProducer;

    public static RatelStandaloneFactory fromZookeeperServer(String zookeeperAddress) {
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

    public static RatelStandaloneFactory fromZookeeperServer() {
        String zookeeperHost = System.getProperty(SERVICE_DISCOVERY_ZK_HOST);
        return fromZookeeperServer(zookeeperHost);
    }

    public static RatelStandaloneFactory fromRatelServer() {
        String ratelServer = System.getProperty(SERVICE_DISCOVERY_ADDRESS);
        return fromRatelServer(ratelServer);
    }

    public static RatelStandaloneFactory fromAnyConfiguration() {
        if (System.getProperty(SERVICE_DISCOVERY_ADDRESS) != null) {
            return fromRatelServer();
        }
        if (System.getProperty(SERVICE_DISCOVERY_ZK_HOST) != null) {
            return fromZookeeperServer();
        }
        throw new IllegalStateException("Can't resolve zookeeper address nor ratel server address.");
    }

    public <T> T getServiceProxy(Class<T> serviceContractClass) {
        return clientProducer.produceServiceProxy(serviceContractClass, false, null);
    }
}

