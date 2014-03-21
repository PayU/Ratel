package com.payu.hackathon.discovery.client;

import com.payu.hackathon.discovery.model.Service;
import com.payu.hackathon.discovery.model.ServiceSerializer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ZkDiscoveryClient implements DiscoveryClient {

    private final String zookeeperUrl;

    private static final int BASE_SLEEP_TIME_MS = 1000;

    private static final int MAX_RETRIES = 3;

    public static final String NAMESPACE = "SERVICE_DISCOVERY";

    private static final String ZK_PATH_SEPARATOR = "/";

    private CuratorFramework zkClient;

    ServiceSerializer serviceSerializer = new ServiceSerializer();

    public ZkDiscoveryClient(String zookeeperUrl) {
        this.zookeeperUrl = zookeeperUrl;
        initZookeeper();
    }

    private void initZookeeper() {
        zkClient = CuratorFrameworkFactory.builder()
                .namespace(NAMESPACE)
                .connectString(zookeeperUrl)
                .retryPolicy(new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRIES))
                .build();
        zkClient.start();
    }

    @Override
    public Collection<Service> fetchAllServices() {
        try {
            return zkClient.getChildren().forPath("").stream().map(service -> fetchService(service)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void listenForServices(Collection<String> services, Consumer<Service> consumer) {
        services.forEach(service -> {
            try {
                zkClient.getData().usingWatcher(new CuratorWatcher() {
                    @Override
                    public void process(WatchedEvent watchedEvent) throws Exception {
                        consumer.accept(fetchService(watchedEvent.getPath()));
                    }
                }).forPath(service);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Service fetchService(String serviceName) {
        try {
            return serviceSerializer.deserializeService(zkClient.getData().forPath(serviceName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
