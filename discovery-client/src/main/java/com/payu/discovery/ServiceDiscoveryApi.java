package com.payu.discovery;

import com.payu.discovery.model.ServiceDescriptor;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

import java.util.Collection;

public interface ServiceDiscoveryApi {

    @POST("/")
    Response registerService(@Body ServiceDescriptor serviceDescriptor);

    @GET("/")
    Collection<ServiceDescriptor> fetchAllServices();
}
