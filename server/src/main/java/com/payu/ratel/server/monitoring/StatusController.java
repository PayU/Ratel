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
package com.payu.ratel.server.monitoring;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.payu.ratel.model.ServiceDescriptor;
import com.payu.ratel.server.DiscoveryServer;

@Controller
public class StatusController {

    private final DiscoveryServer discoveryServer;

    private final StatisticsHolder statisticsHolder;

    @Autowired
    public StatusController(DiscoveryServer discoveryServer, StatisticsHolder statisticsHolder) {
        this.discoveryServer = discoveryServer;
        this.statisticsHolder = statisticsHolder;
    }

    @RequestMapping(value = "/services")
    public String greeting() {
        return "services";
    }

    @ModelAttribute("services")
    public Map<ServiceDescriptor, Map<String, Map<String, String>>> populateServices() {
        Map<ServiceDescriptor, Map<String, Map<String, String>>> services = new HashMap<>();

        Collection<ServiceDescriptor> serviceDescriptors = discoveryServer
                .fetchAllServices();
        for(ServiceDescriptor serviceDescriptor:serviceDescriptors){
            services.put(serviceDescriptor, statisticsHolder.getStatistics(serviceDescriptor.getAddress()));
        }

        return services;
    }
}
