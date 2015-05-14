package com.payu.ratel.config.beans;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;

import com.payu.ratel.client.ClientProxyDecorator;
import com.payu.ratel.client.zookeeper.ZookeeperFetcher;
import com.payu.ratel.client.zookeeper.ZookeeperProxyGenerator;
import com.payu.ratel.register.zookeeper.ZookeeperRegistry;

public class ZookeeperServiceRegistryStrategy implements RegistryStrategiesProvider {

    private CuratorFramework curatorFramework;
    private ServiceDiscovery<Void> serviceDiscovery;
    private ZookeeperRegistry zookeeperRegistry;
    private ZookeeperFetcher zookeeperFetcher;
    private ZookeeperProxyGenerator zookeeperProxyGenerator;

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

    public ZookeeperProxyGenerator getClientProxyGenerator() {
        return zookeeperProxyGenerator;
    }

    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    public void configureWithZookeeperHost(String zkHostAddress) {
        System.setProperty("zookeeper.sasl.client", "false");
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
        this.zookeeperProxyGenerator = new ZookeeperProxyGenerator(new ClientProxyDecorator());
    }

    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    public void destroy() throws Exception {
        getServiceDiscovery().close();
        curatorFramework.close();
    }

}
