package com.payu.hackathon.discovery.model;

import java.net.URI;
import java.util.Collection;

public class Service {

    private Collection<Method> methods;

    private URI address;

    private String path;

    public Service(Collection<Method> methods, URI address, String path) {
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

    public static class Method {

        public enum MethodType {
            POST, GET, PUT, DELETE, TRACE, HEAD
        }

        private String path;

        private MethodType methodType;

        public Method(String path, MethodType methodType) {
            this.path = path;
            this.methodType = methodType;
        }

        public String getPath() {
            return path;
        }

        public MethodType getMethodType() {
            return methodType;
        }
    }
}
