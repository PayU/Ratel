package com.payu.soa.example.client;

import com.payu.discovery.client.DiscoveryClient;
import com.payu.discovery.model.Service;
import com.payu.discovery.proxy.HessianClientProducer;
import com.payu.server.service.OrderService;
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
        final Map<String, Service> services = discoveryClient
                .fetchAllServices()
                .stream()
                .collect(Collectors.toMap(Service::getName, Function.<Service>identity()));

        hessianClientProducer = new HessianClientProducer(services);
    }

	@Override
	protected Object buildLazyResolutionProxy(DependencyDescriptor descriptor,
			String beanName) {
		if (descriptor.getField().getType().isAssignableFrom(OrderService.class)) {
            return hessianClientProducer.produce(descriptor.getDependencyType());
		}
		return super.buildLazyResolutionProxy(descriptor, beanName);
	}

}
