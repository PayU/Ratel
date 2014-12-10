package com.payu.discovery.register.config;

import com.payu.discovery.model.ServiceDescriptionBuilder;
import com.payu.discovery.model.ServiceDescriptor;
import com.payu.discovery.server.RemoteRestDiscoveryServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;

public class RatelServerRegistry implements RegisterStrategy {

    private static Logger LOGGER = LoggerFactory.getLogger(RatelServerRegistry.class);

    public static final int SECONDS_20 = 20000;

    private RemoteRestDiscoveryServer server;

    TaskScheduler taskScheduler;

    public RatelServerRegistry(RemoteRestDiscoveryServer server, TaskScheduler taskScheduler) {
        this.server = server;
        this.taskScheduler = taskScheduler;
    }

    @Override
    public void registerService(String name, String address) {
        ServiceDescriptor serviceDescriptor = buildService(name, address);
        LOGGER.info("Registering service {}", serviceDescriptor);
        applyHeartBeat(serviceDescriptor);
    }

    private void applyHeartBeat(ServiceDescriptor serviceDescriptor) {
        taskScheduler.scheduleAtFixedRate(() -> {
            server.registerService(serviceDescriptor);
            server.collectStatistics(serviceDescriptor);
        }, SECONDS_20);
    }

    private ServiceDescriptor buildService(String name, String address) {
        return ServiceDescriptionBuilder
                .aService()
                .withName(name)
                .withAddress(address)
                .build();
    }
}
