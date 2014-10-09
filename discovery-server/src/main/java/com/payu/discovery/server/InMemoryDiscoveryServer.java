package com.payu.discovery.server;


import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Sets;
import com.payu.discovery.model.ServiceDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Path("/discovery")
public class InMemoryDiscoveryServer implements DiscoveryServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryDiscoveryServer.class);

    public static final int SECONDS_20 = 20000;

    public static final int SECONDS_25 = 25000;

    private Set<ServiceDescriptor> services = Sets.newSetFromMap((Map<ServiceDescriptor, Boolean>)
            new ConcurrentHashMap<ServiceDescriptor, Boolean>());

    private Map<ServiceDescriptor, Long> pingedServers = new ConcurrentHashMap<>();

    @Override
    @POST
    @Consumes("application/json")
    public void registerService(ServiceDescriptor serviceDescriptor) {
        checkNotNull(serviceDescriptor.getName(), "Given service name cannot be null");
        services.add(serviceDescriptor);
        pingedServers.put(serviceDescriptor, System.currentTimeMillis());
    }

    @Override
    @GET
    @Produces("application/json")
    public Collection<ServiceDescriptor> fetchAllServices() {
        return services;
    }

	@Override
	@DELETE
	public void deleteAllServices() {
		services.clear();
	}

    @Scheduled(fixedRate = SECONDS_20)
    public void checkActiveServices() {
        pingedServers.entrySet()
                .stream()
                .filter(entry -> entry.getValue() < System.currentTimeMillis() - SECONDS_25)
                .forEach(filtered -> {
                    LOGGER.info("Removing services with address {}", filtered.getKey());
                    services.removeIf(service -> service.equals(filtered.getKey()));
                    pingedServers.remove(filtered.getKey());
                });
    }
}
