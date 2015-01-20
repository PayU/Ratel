package com.payu.discovery.client.inmemory;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.payu.discovery.client.FetchStrategy;
import com.payu.discovery.model.ServiceDescriptor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.util.CollectionUtils.isEmpty;

public class RatelServerFetcher implements FetchStrategy {

    private DiscoveryClient discoveryClient;
    private final AtomicInteger index = new AtomicInteger(0);

    public RatelServerFetcher(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    public String fetchServiceAddress(String serviceName) {
        final ArrayList<String> serviceAddressList = newArrayList(fetchServiceAddresses(serviceName));

        if (isEmpty(serviceAddressList)) {
            return "";
        }

        Collections.sort(serviceAddressList);

        int thisIndex = Math.abs(index.getAndIncrement());
        return serviceAddressList.get(thisIndex % serviceAddressList.size());
    }

    @Override
    public Collection<String> fetchServiceAddresses(final String serviceName) {
        checkNotNull(serviceName, "Please provide service name");

        final Collection<ServiceDescriptor> serviceInstances = filter(discoveryClient.fetchAllServices(), new Predicate<ServiceDescriptor>() {
            @Override
            public boolean apply(ServiceDescriptor serviceDescriptor) {
                return serviceName.equals(serviceDescriptor.getName());
            }
        });

        return transform(serviceInstances, new Function<ServiceDescriptor, String>() {
            @Nullable
            @Override
            public String apply(ServiceDescriptor serviceDescriptor) {
                return serviceDescriptor.getAddress();
            }
        });

    }

}
