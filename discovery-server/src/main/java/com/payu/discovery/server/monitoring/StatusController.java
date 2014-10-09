package com.payu.discovery.server.monitoring;


import com.payu.discovery.server.DiscoveryServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class StatusController {

    private final DiscoveryServer discoveryServer;

    @Autowired
    public StatusController(DiscoveryServer discoveryServer) {
        this.discoveryServer = discoveryServer;
    }

    @RequestMapping(value = "/services", method = RequestMethod.GET)
    public String greeting(Model model) {

        model.addAttribute("services", discoveryServer.fetchAllServices());

        return "services";
    }

}
