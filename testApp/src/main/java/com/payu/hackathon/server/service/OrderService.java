package com.payu.hackathon.server.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.payu.hackathon.server.model.Order;

@Path("/order")
public interface OrderService {

    @POST
    public void createOrder(Order order);

    @GET
    public com.payu.hackathon.server.model.Order getOrder(Long id);
}
