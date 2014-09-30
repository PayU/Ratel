package com.payu.discovery.model;

import com.google.common.collect.Sets;

import java.util.Collection;

public class Service {

    private String name;

    private Collection<Method> methods = Sets.newHashSet();

    private String address;

    private String path;

    public Service() {

    }

    public Service(String name, Collection<Method> methods, String address, String path) {
        this.name = name;
        this.methods = methods;
        this.address = address;
        this.path = path;
    }

    public Collection<Method> getMethods() {
        return methods;
    }

    public String getAddress() {
        return address;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Service{" +
                "name='" + name + '\'' +
                ", methods=" + methods +
                ", address='" + address + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

    public static class Method {

        private String name;

        private String path;

        public Method() {

        }

        public Method(String name, String path) {
            this.name = name;
            this.path = path;
        }

        public String getPath() {
            return path;
        }


        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Method{" +
                    "name='" + name + '\'' +
                    ", path='" + path + '\'' +
                    '}';
        }
    }
}
