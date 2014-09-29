package com.payu.discovery.server;

import com.payu.discovery.model.Service;

public interface DiscoveryServer {

    void registerService(Service service);

}
