package com.payu.discovery.server;


import java.util.Collection;

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
        Collection<ServiceDescriptor> services = discoveryServer.fetchAllServices();
        if (!services.isEmpty()){
            model.addAttribute("services", services);
        }
        return "services";
    }

}
