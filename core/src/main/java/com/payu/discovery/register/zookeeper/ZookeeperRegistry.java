package com.payu.discovery.register.zookeeper;

import com.payu.discovery.register.RegisterStrategy;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZookeeperRegistry implements RegisterStrategy {

    private static Logger LOGGER = LoggerFactory.getLogger(ZookeeperRegistry.class);

    private ServiceDiscovery discovery;

    public ZookeeperRegistry(ServiceDiscovery discovery) {
        this.discovery = discovery;
    }

    @Override
    public void registerService(String name, String address) {
        try {
            final ServiceInstance<Object> serviceInstance = ServiceInstance.builder()
                    .name(name)
                    .address(address)
                    .build();
            discovery.registerService(serviceInstance);
        } catch (Exception e) {
            LOGGER.error("Zk registering failed", e);
        }
    }
}
