package com.payu.discovery.server;


import com.payu.discovery.model.Service;
import com.payu.discovery.model.ServiceBuilder;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Path("/discovery")
public class InMemoryDiscoveryServer implements DiscoveryServer {

    private Map<String, Service> services = new ConcurrentHashMap<>();

    @Override
    @POST
    @Consumes("application/json")
    public void registerService(Service service) {
        services.putIfAbsent("aaa", ServiceBuilder.aService().withName("aaas").build());
    }

    @Override
    @GET
    @Produces("application/json")
    public Collection<Service> fetchAllServices() {
        return services.values();
    }
}
