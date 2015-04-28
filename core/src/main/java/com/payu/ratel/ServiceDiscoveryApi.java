/*
 * Copyright 2015 PayU
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.payu.ratel;

import com.payu.ratel.model.ServiceDescriptor;
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
    Response collectStatistics(@Path("service") String address,
                               @Body Map<String, Map<String, String>> statistics);

}
