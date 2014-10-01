package com.payu.discovery.server;

import com.payu.discovery.ServiceDiscoveryApi;
import com.payu.discovery.model.ServiceDescriptor;
import retrofit.RestAdapter;

public class RemoteRestDiscoveryServer {

    private static final String API_URL = "http://localhost:8090/server/discovery";

    private final ServiceDiscoveryApi api;

    public RemoteRestDiscoveryServer() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();
        api = restAdapter.create(ServiceDiscoveryApi.class);
    }

    public void registerService(ServiceDescriptor serviceDescriptor) {
        api.registerService(serviceDescriptor);
    }
}
