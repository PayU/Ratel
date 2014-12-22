package com.payu.discovery.client;

import com.payu.discovery.Cachable;
import com.payu.discovery.Discover;
import com.payu.discovery.RetryPolicy;
import com.payu.discovery.event.EventCannon;
import com.payu.discovery.proxy.BroadcastingInvocationHandler;
import com.payu.discovery.proxy.CacheInvocationHandler;
import com.payu.discovery.proxy.RetryPolicyInvocationHandler;
import com.payu.discovery.proxy.UnicastingInvocationHandler;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;

import java.lang.reflect.Proxy;

public class RemoteAutowireCandidateResolver extends
        ContextAnnotationAutowireCandidateResolver implements
        AutowireCandidateResolver {

    private FetchStrategy fetchStrategy;
    private ClientProxyGenerator clientProxyGenerator;

    public RemoteAutowireCandidateResolver(FetchStrategy fetchStrategy, ClientProxyGenerator clientProxyGenerator) {
        this.fetchStrategy = fetchStrategy;
        this.clientProxyGenerator = clientProxyGenerator;
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
        return produceBroadcaster();
    }

    private Object produceServiceProxy(DependencyDescriptor descriptor) {
        Object client = produceUnicaster(descriptor.getDependencyType());

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

    public Object produceUnicaster(Class<?> clazz) {
        return Proxy
                .newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class[]{clazz}, new UnicastingInvocationHandler(fetchStrategy, clazz, clientProxyGenerator));
    }

    public Object produceBroadcaster() {
        return Proxy
                .newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class[]{EventCannon.class}, new BroadcastingInvocationHandler(fetchStrategy, clientProxyGenerator));
    }

}
