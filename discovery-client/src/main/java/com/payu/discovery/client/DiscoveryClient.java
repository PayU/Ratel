package com.payu.discovery.client;


import com.payu.discovery.ServiceDiscoveryApi;
import com.payu.discovery.model.ServiceDescriptor;
import retrofit.RestAdapter;

import java.util.Collection;

public class DiscoveryClient {

    private static final String API_URL = "http://localhost:8090/server/discovery";

    final ServiceDiscoveryApi api;

    public DiscoveryClient() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();
        api = restAdapter.create(ServiceDiscoveryApi.class);
    }

    public Collection<ServiceDescriptor> fetchAllServices() {
        return api.fetchAllServices();
    }

}
