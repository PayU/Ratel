package com.payu.ratel.client.zookeeper;

import static com.google.common.collect.Collections2.transform;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import org.apache.curator.x.discovery.ProviderStrategy;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.curator.x.discovery.strategies.RoundRobinStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.google.common.base.Function;
import com.payu.ratel.client.FetchStrategy;

public class ZookeeperFetcher implements FetchStrategy {

    private static Logger LOGGER = LoggerFactory.getLogger(ZookeeperFetcher.class);

    public static final Function<ServiceInstance, String> SERVICE_INSTANCE_STRING_FUNCTION = new Function<ServiceInstance, String>() {
        @Override
        public String apply(ServiceInstance serviceInstance) {
            return serviceInstance.getAddress();
        }
    };
    private final ProviderStrategy pickInstanceStrategy = new RoundRobinStrategy();
    private final ServiceProviderHandler serviceProviderHandler = new ServiceProviderHandler();

    private final ServiceDiscovery serviceDiscovery;


    public ZookeeperFetcher(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    @Override
    public String fetchServiceAddress(String serviceName) throws Exception {
        final ServiceInstance serviceInstance = serviceProviderHandler.getProvider(serviceName).getInstance();

        return serviceInstance == null ? "" : serviceInstance.getAddress();
    }

    @Override
    public Collection<String> fetchServiceAddresses(String serviceName) throws Exception {
        final Collection<ServiceInstance> serviceInstances = serviceProviderHandler.getProvider(serviceName).getAllInstances();
        LOGGER.info("Fetching addresses from zkRegistry for {}", serviceName);

        return transform(serviceInstances, SERVICE_INSTANCE_STRING_FUNCTION);
    }

    public void close() throws IOException {
        serviceProviderHandler.close();
    }

    private class ServiceProviderHandler {
        private final Map<String, ServiceProvider> providers = new ConcurrentHashMap<>();

        public ServiceProvider getProvider(String serviceName) throws Exception {
            if (StringUtils.isEmpty(serviceName)) {
                return null;
            }

            ServiceProvider provider = providers.get(serviceName);

            if (provider == null) {
                provider = serviceDiscovery.serviceProviderBuilder().serviceName(serviceName)
                        .providerStrategy(pickInstanceStrategy).build();
                provider.start();

                providers.put(serviceName, provider);
            }

            return provider;
        }

        public void close() throws IOException {
            for (ServiceProvider serviceProvider : providers.values()) {
                serviceProvider.close();
            }
        }
    }
}
