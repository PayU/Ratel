package com.payu.discovery.proxy.monitoring;


import com.payu.discovery.client.inmemory.DiscoveryClient;
import com.payu.discovery.model.ServiceDescriptor;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;

import java.util.Collection;

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
