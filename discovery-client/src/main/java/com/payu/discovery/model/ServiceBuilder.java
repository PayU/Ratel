package com.payu.discovery.model;

import java.util.Collection;
import java.util.Collections;

public class ServiceBuilder {
    private String name;
    private Collection<ServiceDescriptor.Method> methods = Collections.emptySet();
    private String address;
    private String path;

    private ServiceBuilder() {
    }

    public static ServiceBuilder aService() {
        return new ServiceBuilder();
    }

    public ServiceBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ServiceBuilder withMethods(Collection<ServiceDescriptor.Method> methods) {
        this.methods = methods;
        return this;
    }

    public ServiceBuilder withMethods(ServiceDescriptor.Method method) {
        this.methods.add(method);
        return this;
    }

    public ServiceBuilder withAddress(String address) {
        this.address = address;
        return this;
    }

    public ServiceBuilder withPath(String path) {
        this.path = path;
        return this;
    }


    public ServiceDescriptor build() {
        ServiceDescriptor serviceDescriptor = new ServiceDescriptor(name, methods, address, path);
        return serviceDescriptor;
    }
}
