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
package com.payu.ratel.proxy.monitoring;


import com.payu.ratel.client.inmemory.DiscoveryClient;
import com.payu.ratel.model.ServiceDescriptor;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;

import java.util.Collection;

public class ServiceDiscoveryHealth extends AbstractHealthIndicator {

    private final DiscoveryClient discoveryClient;

    public ServiceDiscoveryHealth(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        Collection<ServiceDescriptor> serviceDescriptors = discoveryClient.fetchAllServices();

        int registeredServiceCount = serviceDescriptors.size();
        if(registeredServiceCount > 0){
            builder.withDetail("Number of discovered services", registeredServiceCount).up();
        } else {
            builder.down();
        }
    }
}
