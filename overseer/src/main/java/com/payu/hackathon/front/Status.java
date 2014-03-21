package com.payu.hackathon.front;

import org.springframework.hateoas.ResourceSupport;

public class Status extends ResourceSupport {

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
