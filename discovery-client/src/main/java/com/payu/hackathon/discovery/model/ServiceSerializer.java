package com.payu.hackathon.discovery.model;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ServiceSerializer {

    ObjectMapper mapper = new ObjectMapper();

    public byte[] toBytes(Object object) {
        try {
            return mapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Service deserializeService(byte[] bytes) {
        try {
            return mapper.readValue(bytes, Service.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Service.Method deserializeMethod(byte[] bytes) {
        try {
            return mapper.readValue(bytes, Service.Method.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
