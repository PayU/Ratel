package com.payu.discovery.client;

import com.payu.discovery.model.ServiceDescriptor;
import com.payu.discovery.proxy.HessianClientProducer;
import com.payu.discovery.proxy.RemoteService;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MyAutowireCandidateResolver extends
        ContextAnnotationAutowireCandidateResolver implements
        AutowireCandidateResolver {

    private final HessianClientProducer hessianClientProducer;

    private final DiscoveryClient discoveryClient = new DiscoveryClient();

    public MyAutowireCandidateResolver() {
        final Map<String, ServiceDescriptor> services = discoveryClient
                .fetchAllServices()
                .stream()
                .collect(Collectors.toMap(ServiceDescriptor::getName, Function.<ServiceDescriptor>identity()));

        hessianClientProducer = new HessianClientProducer(services);
    }

    @Override
    protected Object buildLazyResolutionProxy(DependencyDescriptor descriptor,
                                              String beanName) {
        if (descriptor.getField().isAnnotationPresent(RemoteService.class)) {
            return hessianClientProducer.produce(descriptor.getDependencyType());
        }
        return super.buildLazyResolutionProxy(descriptor, beanName);
    }

}
