package com.payu.discovery.server.monitoring;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.payu.discovery.model.ServiceDescriptor;
import com.payu.discovery.server.DiscoveryServer;

@Controller
public class StatusController {

    private final DiscoveryServer discoveryServer;

    @Autowired
    public StatusController(DiscoveryServer discoveryServer) {
        this.discoveryServer = discoveryServer;
    }

    @RequestMapping(value = "/services")
    public String greeting() {
        return "services";
    }

    @ModelAttribute("services")
    public Collection<ServiceDescriptor> populateFeatures() {
        return discoveryServer.fetchAllServices();
    }
}
