package com.payu.discovery.model;

public class MethodBuilder {

    private String name;

    private String path;

    private MethodBuilder() {
    }

    public static MethodBuilder aMethod() {
        return new MethodBuilder();
    }

    public MethodBuilder withName(String name){
        this.name = name;
        return this;
    }

    public MethodBuilder withPath(String path){
        this.path = path;
        return this;
    }

    public com.payu.discovery.model.Service.Method build() {
        return new com.payu.discovery.model.Service.Method(name, path);
    }


}
