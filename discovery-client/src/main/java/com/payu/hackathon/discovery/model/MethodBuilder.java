package com.payu.hackathon.discovery.model;

import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;

import com.google.common.collect.ImmutableMap;

public class MethodBuilder {

    private static final Map<Class<? extends java.lang.annotation.Annotation>, Service.Method.MethodType> METHOD_MAP =
            ImmutableMap.of(
                    POST.class, Service.Method.MethodType.POST,
                    GET.class, Service.Method.MethodType.GET,
                    PUT.class, Service.Method.MethodType.PUT,
                    DELETE.class, Service.Method.MethodType.DELETE
            );
    private String name;
    private String path;
    private Service.Method.MethodType methodType;


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

    public MethodBuilder withMethodType(Class<? extends java.lang.annotation.Annotation> methodType){

        this.methodType = METHOD_MAP.get(methodType);
        return this;
    }
    public MethodBuilder withMethodType(Service.Method.MethodType methodType){
        this.methodType = methodType;
        return this;
    }

    public Service.Method build() {
        return new Service.Method(name, path, methodType);
    }


}
