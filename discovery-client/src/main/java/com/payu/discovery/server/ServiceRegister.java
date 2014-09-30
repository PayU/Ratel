package com.payu.discovery.server;

import org.springframework.stereotype.Service;

import com.payu.discovery.scanner.ServiceAnnotationScanner;

public class ServiceRegister {

    private ServiceAnnotationScanner scanner;

    private RemoteRestDiscoveryServer discoveryServer;

    public ServiceRegister(String packageToScan, String appAddress) {
        scanner = new ServiceAnnotationScanner(packageToScan, Service.class, appAddress);
    }

    public void registerServices() {
        scanner.scan().forEach(it -> discoveryServer.registerService(( com.payu.discovery.model.Service)it));
    }
}
