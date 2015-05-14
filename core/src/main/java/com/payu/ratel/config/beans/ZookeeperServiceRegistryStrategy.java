package com.payu.ratel.config.beans;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.springframework.core.env.Environment;

import com.payu.ratel.client.ClientProxyGenerator;
import com.payu.ratel.client.zookeeper.ZookeeperFetcher;
import com.payu.ratel.client.zookeeper.ZookeeperProxyGenerator;
import com.payu.ratel.register.zookeeper.ZookeeperRegistry;

public class ZookeeperServiceRegistryStrategy implements RegistryStrategiesProvider {

    private CuratorFramework curatorFramework;
    private ServiceDiscovery<Void> serviceDiscovery;
    private ZookeeperRegistry zookeeperRegistry;
    private ZookeeperFetcher zookeeperFetcher;
    private ClientProxyGenerator zookeeperProxyGenerator;

    public ZookeeperServiceRegistryStrategy() {
    }

    public ServiceDiscovery<Void> getServiceDiscovery() {
        return serviceDiscovery;
    }

    public ZookeeperRegistry getRegisterStrategy() {
        return zookeeperRegistry;
    }

    public ZookeeperFetcher getFetchStrategy() {
        return zookeeperFetcher;
    }

    public ClientProxyGenerator getClientProxyGenerator() {
        return zookeeperProxyGenerator;
    }

    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    public void configure(Environment env) {
        System.setProperty("zookeeper.sasl.client", "false");
        String zkHostAddress = env.getProperty(RegistryBeanProviderFactory.SERVICE_DISCOVERY_ZK_HOST);
        this.curatorFramework = CuratorFrameworkFactory.newClient(zkHostAddress, new ExponentialBackoffRetry(1000, 3));

        curatorFramework.start();

        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(Void.class).client(this.curatorFramework)
                .basePath("services").build();

        try {
            getServiceDiscovery().start();
        } catch (Exception e) {
            throw new IllegalStateException("Service discovery start failed.", e);
        }

        this.zookeeperRegistry = new ZookeeperRegistry(getServiceDiscovery());
        this.zookeeperFetcher = new ZookeeperFetcher(getServiceDiscovery());
        this.zookeeperProxyGenerator = new ZookeeperProxyGenerator(env);
    }

    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    public void destroy() throws Exception {
        getServiceDiscovery().close();
        curatorFramework.close();
    }

}
