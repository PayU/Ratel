package com.payu.discovery.model;

import java.util.Collection;
import java.util.Collections;

public class ServiceBuilder {
    private String name;
    private Collection<Service.Method> methods = Collections.emptySet();
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

    public ServiceBuilder withMethods(Collection<Service.Method> methods) {
        this.methods = methods;
        return this;
    }

    public ServiceBuilder withMethods(Service.Method method) {
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


    public Service build() {
        Service service = new Service(name, methods, address, path);
        return service;
    }
}
