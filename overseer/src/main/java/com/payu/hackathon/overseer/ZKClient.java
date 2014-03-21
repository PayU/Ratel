package com.payu.hackathon.overseer;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.payu.hackathon.discovery.server.ZkDiscoveryServer;

@Configuration
public class ZKClient {

    @Autowired
    private ZookeeperProperties zookeeperProperties;

    @Bean
    public CuratorFramework init() {
        return CuratorFrameworkFactory.builder()
                .namespace(ZkDiscoveryServer.NAMESPACE)
                .connectString(zookeeperProperties.getConnectionUrl())
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
    }

}
