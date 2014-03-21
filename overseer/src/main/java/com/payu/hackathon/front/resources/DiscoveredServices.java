package com.payu.hackathon.front.resources;

import java.util.Collection;

import org.springframework.hateoas.ResourceSupport;

import com.payu.hackathon.discovery.model.Service;

public class DiscoveredServices extends ResourceSupport {

    Collection<Service> services;

    public DiscoveredServices(Collection<Service> services) {
        this.services = services;
    }
}
