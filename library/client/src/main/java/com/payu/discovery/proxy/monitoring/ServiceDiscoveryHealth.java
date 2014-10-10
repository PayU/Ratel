package com.payu.discovery.proxy.monitoring;


import java.util.Collection;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;

import com.payu.discovery.client.DiscoveryClient;
import com.payu.discovery.model.ServiceDescriptor;

public class ServiceDiscoveryHealth extends AbstractHealthIndicator {

    private final DiscoveryClient discoveryClient;

    public ServiceDiscoveryHealth(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        Collection<ServiceDescriptor> serviceDescriptors = discoveryClient.fetchAllServices();

        int registeredServiceCount = serviceDescriptors.size();
        if(registeredServiceCount > 0){
            builder.withDetail("Number of discovered services", registeredServiceCount).up();
        } else {
            builder.down();
        }
    }
}
