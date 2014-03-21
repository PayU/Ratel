package com.payu.hackathon.front.resources;

import org.springframework.hateoas.ResourceSupport;

public class Status extends ResourceSupport {

    private String status;

    public Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
