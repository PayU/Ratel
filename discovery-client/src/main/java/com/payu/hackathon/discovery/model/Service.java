package com.payu.hackathon.discovery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.net.URI;
import java.util.Collection;

public class Service {

    private String name;

    @JsonIgnore
    private Collection<Method> methods;

    private URI address;

    private String path;

    public Service(String name, Collection<Method> methods, URI address, String path) {
        this.name = name;
        this.methods = methods;
        this.address = address;
        this.path = path;
    }

    public Collection<Method> getMethods() {
        return methods;
    }

    public URI getAddress() {
        return address;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public static class Method {

        public enum MethodType {
            POST, GET, PUT, DELETE, TRACE, HEAD
        }

        private String name;

        private String path;

        private MethodType methodType;

        public Method(String name, String path, MethodType methodType) {
            this.name = name;
            this.path = path;
            this.methodType = methodType;
        }

        public String getPath() {
            return path;
        }

        public MethodType getMethodType() {
            return methodType;
        }

        public String getName() {
            return name;
        }
    }
}
