package com.payu.discovery.server;


import com.payu.discovery.ServiceDiscoveryApi;
import com.payu.discovery.model.Service;
import retrofit.RestAdapter;


public class DiscoveryServer {

    private static final String API_URL = "http://localhost:8080/server/discovery";

    final ServiceDiscoveryApi api;

    public DiscoveryServer() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();
        api = restAdapter.create(ServiceDiscoveryApi.class);
    }

    public void registerService(Service service) {
        api.registerService(service);
    }
}
