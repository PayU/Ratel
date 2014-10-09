package com.payu.discovery.server;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.payu.discovery.model.ServiceDescriptor;

@Controller
public class StatusController {

    private final DiscoveryServer discoveryServer;

    @Autowired
    public StatusController(DiscoveryServer discoveryServer) {
        this.discoveryServer = discoveryServer;
    }

    @RequestMapping(value = "/services", method = RequestMethod.GET)
    public String greeting(Model model) {

        for (ServiceDescriptor serviceDescriptor : discoveryServer.fetchAllServices()) {
            model.addAttribute("service", serviceDescriptor.getName());
        }

        return "services";
    }

}
