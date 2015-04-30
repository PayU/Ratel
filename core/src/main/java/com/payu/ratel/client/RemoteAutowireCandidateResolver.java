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

public class RemoteAutowireCandidateResolver extends
        ContextAnnotationAutowireCandidateResolver implements
        AutowireCandidateResolver {

    final RatelClientProducer ratelClientProducer;
    
    

    public RemoteAutowireCandidateResolver(RatelClientProducer ratelClientProducer) {
      this.ratelClientProducer = ratelClientProducer;
    }

    @Override
    protected Object buildLazyResolutionProxy(DependencyDescriptor descriptor, String beanName) {

        if (getAnnotationsType(descriptor).contains(Discover.class.getName())) {
            if (descriptor.getDependencyType().equals(EventCannon.class)) {
                return produceEventCannonProxy();
            } else {
              Class retryOnException = null;
              if (getAnnotationsType(descriptor).contains(RetryPolicy.class.getName())) {
                final RetryPolicy annotation = (RetryPolicy) (getAnnotationWithType(descriptor)
                    .toArray())[0];
                retryOnException = annotation.exception();
              }
              boolean useCache = getAnnotationsType(descriptor)
                  .contains(Cachable.class.getName());
              
                return ratelClientProducer.produceServiceProxy(this,descriptor.getDependencyType(), useCache, retryOnException);
            }
        }

        return super.buildLazyResolutionProxy(descriptor, beanName);
    }

    Collection<String> getAnnotationsType(DependencyDescriptor descriptor) {
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

    Collection<Annotation> getAnnotationWithType(DependencyDescriptor descriptor) {
        return Collections2.filter(Arrays.asList(descriptor.getAnnotations()), new Predicate<Annotation>() {
            @Override
            public boolean apply(Annotation annotation) {
                return RetryPolicy.class.getName().equals(annotation.annotationType().getName());
            }
        });
    }


    private Object produceBroadcaster() {
        return Proxy
                .newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class[]{EventCannon.class},
                        new BroadcastingInvocationHandler(ratelClientProducer.getFetchStrategy(), ratelClientProducer.getClientProxyGenerator()));
    }

}
