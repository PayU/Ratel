package com.payu.discovery;

import com.payu.discovery.model.Service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Collection;

@Path("/discovery")
public interface ServiceDiscoveryApi {

    @POST
    @Consumes("application/json")
    @Path("/register")
    void registerService(Service service);

    @GET
    @Path("/fetchAllServices")
    @Produces("application/json")
    Collection<Service> fetchAllServices();
}
