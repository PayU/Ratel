package com.payu.discovery.server;


import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Sets;
import com.payu.discovery.model.ServiceDescriptor;
import com.payu.discovery.server.monitoring.StatisticsHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

    private final Set<ServiceDescriptor> services = Sets.newSetFromMap((Map<ServiceDescriptor, Boolean>)
            new ConcurrentHashMap<ServiceDescriptor, Boolean>());

    private final Map<ServiceDescriptor, Long> pingedServers = new ConcurrentHashMap<>();

    private final GaugeService gaugeService;

    private final StatisticsHolder statisticsHolder;

    @Autowired
    public InMemoryDiscoveryServer(GaugeService gaugeService, StatisticsHolder statisticsHolder) {
        this.gaugeService = gaugeService;
        this.statisticsHolder = statisticsHolder;
    }

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

    @Override
    @PUT
    @Consumes("application/json")
    @Path("/service/{service}")
    public void collectStatistics(@PathParam("service") String address,
                                  Map<String, Map<String, String>> statistics) {
        try {
            statisticsHolder.putStatistics(URLDecoder.decode(address, "UTF-8"), statistics);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError("UTF-8 must be supported");
        }
    }

    @Scheduled(fixedRate = SECONDS_20)
    public void checkActiveServices() {
        pingedServers.entrySet()
                .stream()
                .filter(entry -> isActive(entry.getValue()))
                .forEach(filtered -> {
                    LOGGER.info("Removing services with address {}", filtered.getKey());
                    services.removeIf(service -> service.equals(filtered.getKey()));
                    pingedServers.remove(filtered.getKey());
                });
        gaugeService.submit("registered.services.count", services.size());
        gaugeService.submit("registered.servers.count", pingedServers.size());
    }

    private boolean isActive(Long time) {
        return time < System.currentTimeMillis() - SECONDS_25;
    }
}
