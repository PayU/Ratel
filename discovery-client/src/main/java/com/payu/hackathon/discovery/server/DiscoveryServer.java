package com.payu.hackathon.discovery.server;

import com.payu.hackathon.discovery.model.Service;

public interface DiscoveryServer {

    void registerService(Service service);

}
