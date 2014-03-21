package com.payu.hackathon.overseer;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.payu.hackathon.discovery.server.ZkDiscoveryServer;

@Component
public class OverseerService {

    public static final String PATH = ZkDiscoveryServer.NAMESPACE;
    private static final Logger LOG = LoggerFactory.getLogger(OverseerService.class);
    private final CuratorFramework client;

    @Autowired
    public OverseerService(CuratorFramework client) {
        this.client = client;
    }

    public static List<String> watchedGetChildren(CuratorFramework client, String path) throws Exception {
        return client.getChildren().watched().forPath(path);
    }

    public static void handleWatchEvents(CuratorFramework client) throws Exception {

        CuratorListener listener = (curatorFramework, event) -> {
            LOG.info("Event received {}", event);
            watchedGetChildren(client, PATH);
        };
        client.getCuratorListenable().addListener(listener);
    }

    @PostConstruct
    public void init() {
        client.start();
        try {

            watchedGetChildren(client, PATH).forEach(LOG::info);

            handleWatchEvents(client);
        } catch (Exception e) {
            LOG.error("Watcher failure {}", e);
        }
    }
}
