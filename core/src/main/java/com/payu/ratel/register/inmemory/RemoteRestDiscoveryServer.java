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
package com.payu.ratel.register.inmemory;

import com.payu.ratel.ServiceDiscoveryApi;
import com.payu.ratel.model.ServiceDescriptor;
import com.payu.ratel.proxy.monitoring.StatisticsHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit.RestAdapter;
import retrofit.RetrofitError;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class RemoteRestDiscoveryServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteRestDiscoveryServer.class);

    private final ServiceDiscoveryApi api;

    private final String serverApiUrl;

    public RemoteRestDiscoveryServer(String apiUrl) {
        this.serverApiUrl = apiUrl;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(apiUrl)
                .build();
        api = restAdapter.create(ServiceDiscoveryApi.class);
    }

    public void registerService(ServiceDescriptor serviceDescriptor) {
        try {
            api.registerService(serviceDescriptor);
            LOGGER.info("Service {} registered  in server {}.", serviceDescriptor, serverApiUrl);
        } catch (RetrofitError e) {
            LOGGER.warn("Retrofit error was thrown while registering service at {}. Try again.", serverApiUrl, e);
        }
    }

    public void collectStatistics(ServiceDescriptor serviceDescriptor) {
        try {
            api.collectStatistics(URLEncoder.encode(serviceDescriptor.getAddress(), "UTF-8"),
                    StatisticsHolder.getStatistics(serviceDescriptor.getName()));
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError("UTF-8 must be supported", e);
        } catch (RetrofitError e) {
            LOGGER.warn("Retrofit error was thrown while collecting statistics. Try again.", e);
        }
    }

}
