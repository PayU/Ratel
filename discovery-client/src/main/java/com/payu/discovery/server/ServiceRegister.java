package com.payu.discovery.server;

import com.payu.discovery.scanner.AnnotationScanner;

public class ServiceRegister {

    private AnnotationScanner scanner;

    private DiscoveryServer discoveryServer;

    public ServiceRegister(String packageToScan, String appAddress) {
        scanner = new AnnotationScanner(packageToScan, appAddress);
    }

    public void registerServices() {
        scanner.scan().forEach(discoveryServer::registerService);
    }
}
