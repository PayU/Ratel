package com.payu.hackathon.discovery.server;


import com.payu.hackathon.discovery.model.Service;
import com.payu.hackathon.discovery.model.ServiceSerializer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.Optional;

public class ZkDiscoveryServer implements DiscoveryServer {

    private final String zookeeperUrl;

    private static final int BASE_SLEEP_TIME_MS = 1000;

    private static final int MAX_RETRIES = 3;

    public static final String NAMESPACE = "SERVICE_DISCOVERY";

    private static final String ZK_PATH_SEPARATOR = "/";

    Optional<CuratorFramework> zkClient = Optional.empty();

    ServiceSerializer serviceSerializer = new ServiceSerializer();

    public ZkDiscoveryServer(String zookeeperUrl) {
        this.zookeeperUrl = zookeeperUrl;
    }

    private CuratorFramework initZookeeper() {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .namespace(NAMESPACE)
                .connectString(zookeeperUrl)
                .retryPolicy(new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRIES))
                .build();
        curatorFramework.start();
        return curatorFramework;
    }

    @Override
    public void registerService(Service service) {
        try {
            zkClient.orElseGet(this::initZookeeper).setData().forPath(service.getName(), serviceSerializer.toBytes(service));
            registerServiceMethods(service);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void registerServiceMethods(Service service) {
        service.getMethods().forEach(method -> {
            try {
                zkClient.get().setData()
                        .forPath(service.getName().concat(ZK_PATH_SEPARATOR).concat(method.getName()), serviceSerializer.toBytes(method));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

}
