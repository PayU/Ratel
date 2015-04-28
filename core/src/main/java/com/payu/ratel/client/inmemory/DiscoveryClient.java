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
package com.payu.ratel.client.inmemory;

import com.payu.ratel.ServiceDiscoveryApi;
import com.payu.ratel.model.ServiceDescriptor;
import retrofit.RestAdapter;

import java.util.Collection;

public class DiscoveryClient {

    private final ServiceDiscoveryApi api;

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
