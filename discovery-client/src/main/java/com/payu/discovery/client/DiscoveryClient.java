package com.payu.discovery.client;

import com.payu.discovery.ServiceDiscoveryApi;
import com.payu.discovery.model.ServiceDescriptor;
import retrofit.RestAdapter;

import java.util.Collection;

public class DiscoveryClient {

    final ServiceDiscoveryApi api;

    public DiscoveryClient(String urlApi) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(urlApi)
                .build();
        api = restAdapter.create(ServiceDiscoveryApi.class);
    }

    public Collection<ServiceDescriptor> fetchAllServices() {
        return api.fetchAllServices();
    }
    
    public void deleteAllServices() {
    	api.deleteAllServices();
    }

}
