package com.payu.discovery;

import com.payu.discovery.model.ServiceDescriptor;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

import java.util.Collection;
import java.util.Map;


public interface ServiceDiscoveryApi {

    @POST("/")
    Response registerService(@Body ServiceDescriptor serviceDescriptor);

    @GET("/")
    Collection<ServiceDescriptor> fetchAllServices();
    
    @DELETE("/")
    Response deleteAllServices();

    @PUT("/service/{service}")
    Response collectStatistics(@Path("service") String service,
                               @Body Map<String, Map<String, String>> statistics);

}
