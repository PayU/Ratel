package com.payu.hackathon.discovery.sampledomain.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/order")
public class RestServiceImpl implements RestService  {


    @Override
    @GET
    public List<String> getAllOrders() {
        return null;
    }

    @Override
    @GET
    @Path("/user")
    public List<String> getUserOrders(String userId) {
        return null;
    }

    @Override
    @POST
    public String addOrder(String order) {
        return null;
    }
}
