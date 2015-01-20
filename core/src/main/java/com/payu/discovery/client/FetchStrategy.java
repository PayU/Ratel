package com.payu.discovery.client;

import java.util.Collection;

public interface FetchStrategy {
    String fetchServiceAddress(String serviceName) throws Exception;

    Collection<String> fetchServiceAddresses(String serviceName) throws Exception;
}
