package com.payu.discovery.model;

public class ServiceDescriptionBuilder {
    private String name;
    private String address;
    private String path;

    private ServiceDescriptionBuilder() {
    }

    public static ServiceDescriptionBuilder aService() {
        return new ServiceDescriptionBuilder();
    }

    public ServiceDescriptionBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ServiceDescriptionBuilder withAddress(String address) {
        this.address = address;
        return this;
    }

    public ServiceDescriptionBuilder withPath(String path) {
        this.path = path;
        return this;
    }


    public ServiceDescriptor build() {
        return new ServiceDescriptor(name, address, path);
    }
}
