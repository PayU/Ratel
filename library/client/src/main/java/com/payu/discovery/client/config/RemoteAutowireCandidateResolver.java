package com.payu.discovery.client.config;

import com.payu.discovery.Cachable;
import com.payu.discovery.Discover;
import com.payu.discovery.RetryPolicy;
import com.payu.discovery.client.DiscoveryClient;
import com.payu.discovery.event.EventCannon;
import com.payu.discovery.proxy.CacheInvocationHandler;
import com.payu.discovery.proxy.HessianClientProducer;
import com.payu.discovery.proxy.RetryPolicyInvocationHandler;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;

import java.lang.reflect.Proxy;

class RemoteAutowireCandidateResolver extends
        ContextAnnotationAutowireCandidateResolver implements
        AutowireCandidateResolver {

    private final HessianClientProducer hessianClientProducer;

    public RemoteAutowireCandidateResolver(DiscoveryClient discoveryClient) {
        hessianClientProducer = new HessianClientProducer(discoveryClient);
    }

    @Override
    protected Object buildLazyResolutionProxy(DependencyDescriptor descriptor,
                                              String beanName) {
        if (descriptor.getField().isAnnotationPresent(Discover.class)) {

            if(descriptor.getDependencyType().equals(EventCannon.class)) {
                return produceEventCannonProxy();
            } else {
                return produceServiceProxy(descriptor);
            }
        }

        return super.buildLazyResolutionProxy(descriptor, beanName);
    }

    private Object produceEventCannonProxy() {
        return hessianClientProducer.produceBroadcaster();
    }

    private Object produceServiceProxy(DependencyDescriptor descriptor) {
        Object client = hessianClientProducer.produceLoadBalancer(descriptor.getDependencyType());

        if (descriptor.getField().isAnnotationPresent(Cachable.class)) {
            client = decorateWithCaching(client, descriptor.getDependencyType());
        }

        if (descriptor.getField().isAnnotationPresent(RetryPolicy.class)) {
            final RetryPolicy annotation = descriptor.
                    getField().getAnnotation(RetryPolicy.class);
            client = decorateWithRetryPolicy(client,
                    descriptor.getDependencyType(),
                    annotation.exception());
        }

        return client;
    }

    public Object decorateWithCaching(final Object object, final Class clazz) {
        return Proxy
                .newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class[]{clazz}, new CacheInvocationHandler(object));
    }

    public Object decorateWithRetryPolicy(final Object object, final Class clazz, final Class exception) {
        return Proxy
                .newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class[]{clazz}, new RetryPolicyInvocationHandler(object, exception));
    }

}
