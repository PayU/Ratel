package com.payu.discovery.server;

import com.payu.discovery.model.ServiceDescriptor;
import org.springframework.stereotype.Service;

import com.payu.discovery.scanner.ServiceAnnotationScanner;

public class ServiceRegister {

    private ServiceAnnotationScanner scanner;

    private RemoteRestDiscoveryServer discoveryServer = new RemoteRestDiscoveryServer();

    public ServiceRegister(String packageToScan, String appAddress) {
        scanner = new ServiceAnnotationScanner(packageToScan, Service.class, appAddress);
    }

    public void registerServices() {
        scanner.scan().forEach(it -> discoveryServer.registerService((ServiceDescriptor)it));
    }
}
