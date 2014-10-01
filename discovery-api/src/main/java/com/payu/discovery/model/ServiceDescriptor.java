package com.payu.discovery.model;

import java.util.Collection;

public class ServiceDescriptor {

    private String name;


    private String address;

    private String path;

    public ServiceDescriptor() {

    }

    public ServiceDescriptor(String name, String address, String path) {
        this.name = name;
        this.address = address;
        this.path = path;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Service{" +
                "name='" + name + '\'' +
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

        public void setName(String name) {
            this.name = name;
        }

        public void setPath(String path) {
            this.path = path;
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
