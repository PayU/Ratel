package com.payu.discovery.model;

import com.google.common.collect.ImmutableMap;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import java.util.Map;

public class MethodBuilder {

    private static final Map<Class<? extends java.lang.annotation.Annotation>, com.payu.discovery.model.Service.Method.MethodType> METHOD_MAP =
            ImmutableMap.of(
                    POST.class, com.payu.discovery.model.Service.Method.MethodType.POST,
                    GET.class, com.payu.discovery.model.Service.Method.MethodType.GET,
                    PUT.class, com.payu.discovery.model.Service.Method.MethodType.PUT,
                    DELETE.class, com.payu.discovery.model.Service.Method.MethodType.DELETE
            );
    private String name;
    private String path;
    private com.payu.discovery.model.Service.Method.MethodType methodType;


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
    public MethodBuilder withMethodType(com.payu.discovery.model.Service.Method.MethodType methodType){
        this.methodType = methodType;
        return this;
    }

    public com.payu.discovery.model.Service.Method build() {
        return new com.payu.discovery.model.Service.Method(name, path, methodType);
    }


}
