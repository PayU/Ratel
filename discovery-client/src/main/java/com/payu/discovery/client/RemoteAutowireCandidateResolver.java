package com.payu.discovery.client;

import com.payu.discovery.RemoteService;
import com.payu.discovery.proxy.HessianClientProducer;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;

public class RemoteAutowireCandidateResolver extends
        ContextAnnotationAutowireCandidateResolver implements
        AutowireCandidateResolver {

    private final HessianClientProducer hessianClientProducer;

    public RemoteAutowireCandidateResolver(DiscoveryClient discoveryClient) {
        hessianClientProducer = new HessianClientProducer(discoveryClient);
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
