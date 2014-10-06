package com.payu.discovery.client;

import com.payu.discovery.EnableCache;
import com.payu.discovery.RemoteService;
import com.payu.discovery.proxy.HessianClientProducer;
import com.payu.discovery.proxy.ProxyCache;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;

import java.lang.reflect.Proxy;

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
            if(descriptor.getField().isAnnotationPresent(EnableCache.class)) {
                return decorateWithCaching(hessianClientProducer.
                        produce(descriptor.getDependencyType()), descriptor.getDependencyType());
            }
            return hessianClientProducer.produce(descriptor.getDependencyType());
        }
        return super.buildLazyResolutionProxy(descriptor, beanName);
    }

    public Object decorateWithCaching(final Object object, final Class clazz) {
        return Proxy
                .newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class[]{clazz}, new ProxyCache(object));
    }

}
