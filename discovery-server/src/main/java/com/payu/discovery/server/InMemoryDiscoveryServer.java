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
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Component
@Path("/discovery")
public class InMemoryDiscoveryServer implements DiscoveryServer {

    private Map<String, Collection<ServiceDescriptor>> services = new ConcurrentHashMap<>();

    @Override
    @POST
    @Consumes("application/json")
    public void registerService(ServiceDescriptor serviceDescriptor) {
        checkNotNull(serviceDescriptor.getName(), "Given service name cannot be null");
        synchronized (services) {
            final Collection<ServiceDescriptor> serviceDescriptors = services.get(serviceDescriptor.getName());
            if(serviceDescriptors != null) {
                serviceDescriptors.add(serviceDescriptor);
            } else {
                final Collection<ServiceDescriptor> newServiceDescriptors = new ConcurrentLinkedQueue<>();
                newServiceDescriptors.add(serviceDescriptor);
                services.putIfAbsent(serviceDescriptor.getName(), newServiceDescriptors);
            }
        }
    }

    @Override
    @GET
    @Produces("application/json")
    public Collection<ServiceDescriptor> fetchAllServices() {
        return services.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

	@Override
	@DELETE
	public void deleteAllServices() {
		services.clear();
	}
}
