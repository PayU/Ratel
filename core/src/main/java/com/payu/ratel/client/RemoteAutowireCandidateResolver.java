/*
 * Copyright 2015 PayU
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.payu.ratel.client;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;


import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.payu.ratel.Cachable;
import com.payu.ratel.Discover;
import com.payu.ratel.RetryPolicy;
import com.payu.ratel.event.EventCannon;
import com.payu.ratel.proxy.BroadcastingInvocationHandler;
import com.payu.ratel.proxy.CacheInvocationHandler;
import com.payu.ratel.proxy.RetryPolicyInvocationHandler;
import com.payu.ratel.proxy.UnicastingInvocationHandler;

public class RemoteAutowireCandidateResolver extends
        ContextAnnotationAutowireCandidateResolver implements
        AutowireCandidateResolver {

    private final FetchStrategy fetchStrategy;
    private final ClientProxyGenerator clientProxyGenerator;

    public RemoteAutowireCandidateResolver(FetchStrategy fetchStrategy, ClientProxyGenerator clientProxyGenerator) {
        this.fetchStrategy = fetchStrategy;
        this.clientProxyGenerator = clientProxyGenerator;
    }

    @Override
    protected Object buildLazyResolutionProxy(DependencyDescriptor descriptor, String beanName) {

        if (getAnnotationsType(descriptor).contains(Discover.class.getName())) {
            if (descriptor.getDependencyType().equals(EventCannon.class)) {
                return produceEventCannonProxy();
            } else {
                return produceServiceProxy(descriptor);
            }
        }

        return super.buildLazyResolutionProxy(descriptor, beanName);
    }

    private Collection<String> getAnnotationsType(DependencyDescriptor descriptor) {
        Function<Annotation, String> function = new Function<Annotation, String>() {

            @Override
            public String apply(Annotation annotation) {
                return annotation.annotationType().getName();
            }
        };

        return Collections2.transform(Arrays.asList(descriptor.getAnnotations()),
                function);
    }

    private Object produceEventCannonProxy() {
        return produceBroadcaster();
    }

    private Object produceServiceProxy(DependencyDescriptor descriptor) {
        Object client = produceUnicaster(descriptor.getDependencyType());

        if (getAnnotationsType(descriptor).contains(Cachable.class.getName())) {
            client = decorateWithCaching(client, descriptor.getDependencyType());
        }

        if (getAnnotationsType(descriptor).contains(RetryPolicy.class.getName())) {
            final RetryPolicy annotation = (RetryPolicy) (getAnnotationWithType(descriptor).toArray())[0];
            client = decorateWithRetryPolicy(client,
                    descriptor.getDependencyType(),
                    annotation.exception());
        }
        return client;
    }

    private Collection<Annotation> getAnnotationWithType(DependencyDescriptor descriptor) {
        return Collections2.filter(Arrays.asList(descriptor.getAnnotations()), new Predicate<Annotation>() {
            @Override
            public boolean apply(Annotation annotation) {
                return RetryPolicy.class.getName().equals(annotation.annotationType().getName());
            }
        });
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
                        new Class[]{clazz},
                        new UnicastingInvocationHandler(fetchStrategy, clazz, clientProxyGenerator));
    }

    public Object produceBroadcaster() {
        return Proxy
                .newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class[]{EventCannon.class},
                        new BroadcastingInvocationHandler(fetchStrategy, clientProxyGenerator));
    }

}
