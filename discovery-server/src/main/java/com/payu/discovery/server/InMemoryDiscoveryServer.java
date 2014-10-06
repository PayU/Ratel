package com.payu.discovery.server;


import static com.google.common.base.Preconditions.checkNotNull;

import com.payu.discovery.model.ServiceDescriptor;

import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

    private Map<String, ServiceDescriptor> services = new ConcurrentHashMap<>();

    @Override
    @POST
    @Consumes("application/json")
    public void registerService(ServiceDescriptor serviceDescriptor) {
        checkNotNull(serviceDescriptor.getName(), "Given service name cannot be null");
        services.putIfAbsent(serviceDescriptor.getName(), serviceDescriptor);
    }

    @Override
    @GET
    @Produces("application/json")
    public Collection<ServiceDescriptor> fetchAllServices() {
        return services.values();
    }

	@Override
	@DELETE
	public void deleteAllServices() {
		services.clear();
	}
}
