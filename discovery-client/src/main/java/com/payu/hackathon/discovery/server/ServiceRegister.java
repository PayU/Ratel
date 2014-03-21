package com.payu.hackathon.discovery.server;

import com.payu.hackathon.discovery.scanner.AnnotationScanner;

public class ServiceRegister {

    private AnnotationScanner scanner;
    private DiscoveryServer discoveryServer;

    public ServiceRegister(String packageToScan, String appAddress, String zooKeeperUri) {
        scanner = new AnnotationScanner(packageToScan, appAddress);
        discoveryServer = new ZkDiscoveryServer(zooKeeperUri);
    }

    public void registerServices() {
        scanner.scan().forEach(discoveryServer::registerService);
    }
}
