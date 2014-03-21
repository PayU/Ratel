package com.payu.hackathon.discovery.client;


import com.payu.hackathon.discovery.model.Service;

import java.util.Collection;
import java.util.function.Consumer;

public interface DiscoveryClient {

    Collection<Service> fetchAllServices();

    void listenForServices(Collection<String> services, Consumer<Service> consumer);

}
