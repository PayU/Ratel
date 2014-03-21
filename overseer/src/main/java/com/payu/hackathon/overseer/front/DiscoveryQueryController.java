package com.payu.hackathon.overseer.front;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.payu.hackathon.discovery.client.DiscoveryClient;
import com.payu.hackathon.discovery.model.Service;
import com.payu.hackathon.overseer.front.resources.DiscoveredServices;

@RestController
@RequestMapping("/services")
public class DiscoveryQueryController {

    private static final Logger LOG = LoggerFactory.getLogger(DiscoveryQueryController.class);

    private final DiscoveryClient discoveryClient;

    @Autowired
    public DiscoveryQueryController(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<DiscoveredServices> services() {

        LOG.info("services");

        Collection<Service> services = discoveryClient.fetchAllServices();

        final DiscoveredServices discoveredServices = new DiscoveredServices(services);
        discoveredServices.add(linkTo(methodOn(DiscoveryQueryController.class).services()).withSelfRel());

        return new ResponseEntity<>(discoveredServices, HttpStatus.OK);

    }


}
