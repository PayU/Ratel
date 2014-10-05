package com.payu.discovery.client;

public class ServiceDiscoveryConfigurationException extends RuntimeException {
    public ServiceDiscoveryConfigurationException() {
        super();
    }

    public ServiceDiscoveryConfigurationException(String message) {
        super(message);
    }

    public ServiceDiscoveryConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceDiscoveryConfigurationException(Throwable cause) {
        super(cause);
    }

}
