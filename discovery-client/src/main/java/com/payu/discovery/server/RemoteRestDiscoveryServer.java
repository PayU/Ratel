package com.payu.discovery.server;

import com.payu.discovery.ServiceDiscoveryApi;
import com.payu.discovery.model.ServiceDescriptor;
import retrofit.RestAdapter;

public class RemoteRestDiscoveryServer {

    private final ServiceDiscoveryApi api;

    public RemoteRestDiscoveryServer(String apiUrl) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(apiUrl)
                .build();
        api = restAdapter.create(ServiceDiscoveryApi.class);
    }

    public void registerService(ServiceDescriptor serviceDescriptor) {
        api.registerService(serviceDescriptor);
    }
}
