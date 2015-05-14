package com.payu.ratel.client.standalone;

import java.util.Collections;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

import com.payu.ratel.client.RatelClientProducer;
import com.payu.ratel.config.beans.InMemoryServiceRegistryStrategy;
import com.payu.ratel.config.beans.RegistryBeanProviderFactory;
import com.payu.ratel.config.beans.ZookeeperServiceRegistryStrategy;

public class RatelStandaloneFactory {

    private RatelClientProducer clientProducer;

    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    public static RatelStandaloneFactory fromZookeeperServer(String zookeeperAddress) throws Exception {
        RatelStandaloneFactory result = new RatelStandaloneFactory();
        ZookeeperServiceRegistryStrategy zookeeperStrategy = new ZookeeperServiceRegistryStrategy();

        ConfigurableEnvironment env = new StandardEnvironment();
        env.getPropertySources().addFirst(
                new MapPropertySource("zkCustomConfig", Collections.singletonMap(
                        RegistryBeanProviderFactory.SERVICE_DISCOVERY_ZK_HOST, ((Object) zookeeperAddress))));
        zookeeperStrategy.configure(env);

        result.clientProducer = new RatelClientProducer(zookeeperStrategy.getFetchStrategy(),
                zookeeperStrategy.getClientProxyGenerator());
        return result;
    }

    public static RatelStandaloneFactory fromRatelServer(String ratelServerAddr) {
        RatelStandaloneFactory result = new RatelStandaloneFactory();
        InMemoryServiceRegistryStrategy ratelStrategy = new InMemoryServiceRegistryStrategy();

        ConfigurableEnvironment env = new StandardEnvironment();
        env.getPropertySources().addFirst(
                new MapPropertySource("ratelCustomConfig", Collections.singletonMap(
                        RegistryBeanProviderFactory.SERVICE_DISCOVERY_ADDRESS, ((Object) ratelServerAddr))));
        ratelStrategy.configure(env, null);

        result.clientProducer = new RatelClientProducer(ratelStrategy.getFetchStrategy(),
                ratelStrategy.getClientProxyGenerator());
        return result;
    }

    public <T> T getServiceProxy(Class<T> serviceContractClass) {
        return clientProducer.produceServiceProxy(serviceContractClass, false, null);
    }
}

