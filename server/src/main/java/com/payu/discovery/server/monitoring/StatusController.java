package com.payu.discovery.server.monitoring;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.payu.discovery.model.ServiceDescriptor;
import com.payu.discovery.server.DiscoveryServer;

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
