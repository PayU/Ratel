package com.payu.discovery.server;

import com.payu.discovery.ServiceDiscoveryApi;
import com.payu.discovery.model.ServiceDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import retrofit.RestAdapter;

public class RemoteRestDiscoveryServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteRestDiscoveryServer.class);

    public static final int SECONDS_20 = 20000;

    private final ServiceDiscoveryApi api;

    private final String appUrl;

    public RemoteRestDiscoveryServer(String apiUrl, String appUrl) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(apiUrl)
                .build();
        api = restAdapter.create(ServiceDiscoveryApi.class);
        this.appUrl = appUrl;
    }

    public void registerService(ServiceDescriptor serviceDescriptor) {
        api.registerService(serviceDescriptor);
    }

    @Scheduled(fixedRate = SECONDS_20)
    public void ping() {
        LOGGER.info("Pinging {}", appUrl);
        api.ping(appUrl);
    }

}
