package com.payu.discovery.server.monitoring;

import com.payu.discovery.model.ServiceDescriptor;
import com.payu.discovery.server.DiscoveryServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        return discoveryServer
                .fetchAllServices()
                .stream()
                .collect(Collectors
                        .toMap(Function.identity(),
                                serviceDescriptor ->
                                        statisticsHolder.getStatistics(serviceDescriptor.getAddress())));
    }
}
