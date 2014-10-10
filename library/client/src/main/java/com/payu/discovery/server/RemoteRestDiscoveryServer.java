package com.payu.discovery.server;

import com.payu.discovery.ServiceDiscoveryApi;
import com.payu.discovery.model.ServiceDescriptor;
import com.payu.discovery.proxy.monitoring.StatisticsHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.RestAdapter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class RemoteRestDiscoveryServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteRestDiscoveryServer.class);

    private final ServiceDiscoveryApi api;

    public RemoteRestDiscoveryServer(String apiUrl) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(apiUrl)
                .build();
        api = restAdapter.create(ServiceDiscoveryApi.class);
    }

    public void registerService(ServiceDescriptor serviceDescriptor) {
        try {
            api.registerService(serviceDescriptor);
        } catch (Exception e) {
            //nothing ... will be invoker at fixed rate
        }
    }

    public void collectStatistics(ServiceDescriptor serviceDescriptor) {
        try {
            api.collectStatistics(URLEncoder.encode(serviceDescriptor.getAddress(), "UTF-8"),
                    StatisticsHolder.getStatistics(serviceDescriptor.getName()));
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError("UTF-8 must be supported");
        }
    }

}
