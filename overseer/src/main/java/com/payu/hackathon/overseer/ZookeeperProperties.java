package com.payu.hackathon.overseer;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(name = "service.zookeeper")
public class ZookeeperProperties {

    private String connectionUrl;

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }
}


