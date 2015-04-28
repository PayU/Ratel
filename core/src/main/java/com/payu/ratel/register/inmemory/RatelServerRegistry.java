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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;

import com.payu.ratel.model.ServiceDescriptionBuilder;
import com.payu.ratel.model.ServiceDescriptor;
import com.payu.ratel.register.RegisterStrategy;

public class RatelServerRegistry implements RegisterStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(RatelServerRegistry.class);

    public static final int SECONDS_20 = 20000;

    private final RemoteRestDiscoveryServer server;

    private final TaskScheduler taskScheduler;

    public RatelServerRegistry(RemoteRestDiscoveryServer server, TaskScheduler taskScheduler) {
        this.server = server;
        this.taskScheduler = taskScheduler;
    }

    @Override
    public void registerService(String name, String address) {
        ServiceDescriptor serviceDescriptor = buildService(name, address);
        LOGGER.info("Registering service in ratel server {}", serviceDescriptor);
        applyHeartBeat(serviceDescriptor);
    }

    private void applyHeartBeat(final ServiceDescriptor serviceDescriptor) {
        taskScheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                server.registerService(serviceDescriptor);
                server.collectStatistics(serviceDescriptor);

            }
        }, SECONDS_20);
    }

    private ServiceDescriptor buildService(String name, String address) {
        return ServiceDescriptionBuilder
                .aService()
                .withName(name)
                .withAddress(address)
                .build();
    }
}
