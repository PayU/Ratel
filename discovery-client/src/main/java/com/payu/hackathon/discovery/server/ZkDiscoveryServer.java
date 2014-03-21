package com.payu.hackathon.discovery.server;


import com.payu.hackathon.discovery.model.Service;
import com.payu.hackathon.discovery.model.ServiceSerializer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZkDiscoveryServer implements DiscoveryServer {

    private final String zookeeperUrl;

    private static final int BASE_SLEEP_TIME_MS = 1000;

    private static final int MAX_RETRIES = 3;

    public static final String NAMESPACE = "SERVICE_DISCOVERY";

    private static final String ZK_PATH_SEPARATOR = "/";

    private CuratorFramework zkClient;

    ServiceSerializer serviceSerializer = new ServiceSerializer();

    public ZkDiscoveryServer(String zookeeperUrl) {
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
    public void registerService(Service service) {
        try {
            createNodeIfDoesNotExist(service.getName()).setData().forPath(service.getName(), serviceSerializer.toBytes(service));
            registerServiceMethods(service);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void registerServiceMethods(Service service) {
        service.getMethods().forEach(method -> {
            try {
                createNodeIfDoesNotExist(service.getName().concat(ZK_PATH_SEPARATOR).concat(method.getName())).setData()
                        .forPath(service.getName().concat(ZK_PATH_SEPARATOR).concat(method.getName()), serviceSerializer.toBytes(method));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private CuratorFramework createNodeIfDoesNotExist(String node) throws Exception {
        if(zkClient.checkExists().forPath(node) == null) {
            zkClient.create().creatingParentsIfNeeded().forPath(node);
        }

        return zkClient;
    }

}
