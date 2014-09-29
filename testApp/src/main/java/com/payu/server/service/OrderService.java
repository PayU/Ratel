package com.payu.server.service;

import com.payu.server.model.Order;
import com.payu.server.model.OrderDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Service
@Path("/order")
public class OrderService {


    @Autowired
    private OrderDatabase database;

    @POST
    @Consumes("application/json")
    @Path("/create")
    public Response createOrder(Order order) {
        database.createOrder(order);
        return createResponse();
    }

    @GET
    @Path("/get")
    @Produces("application/json")
    public Order getOrder(@QueryParam("id") Long id) {
        return database.get(id);
    }

    private Response createResponse() {
        return Response.ok().build();
    }
}
