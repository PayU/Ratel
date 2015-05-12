package com.payu.ratel.proxy;

/**
 * Exception thrown when Ratel cannot find a service to pass a remote
 * invocation to. The exception contains a field with the serviceApi.
 *
 */
public class NoServiceInstanceFound extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final Class<?> serviceApi;

    public NoServiceInstanceFound(Class<?> serviceApi) {
        this.serviceApi = serviceApi;
    }

    @Override
    public String getMessage() {
        return "Could not find instance of a service for contract " + serviceApi.getName();
    }

    /**
     * The service api that was not found by Ratel.
     * @return the interface that represents a contract of a missing service.
     */
    public Class<?> getServiceApi() {
        return serviceApi;
    }

}
