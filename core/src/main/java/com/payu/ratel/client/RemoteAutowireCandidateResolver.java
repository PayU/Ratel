/*
 * Copyright 2015 PayU
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.payu.ratel.client;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.payu.ratel.Cachable;
import com.payu.ratel.Discover;
import com.payu.ratel.RetryPolicy;
import com.payu.ratel.config.RetryPolicyConfig;
import com.payu.ratel.config.Timeout;
import com.payu.ratel.config.TimeoutConfig;
import com.payu.ratel.event.EventCannon;

public class RemoteAutowireCandidateResolver extends ContextAnnotationAutowireCandidateResolver implements
        AutowireCandidateResolver {

    private final RatelClientProducer ratelClientProducer;

    public RemoteAutowireCandidateResolver(RatelClientProducer ratelClientProducer) {
        this.ratelClientProducer = ratelClientProducer;
    }

    @Override
    protected Object buildLazyResolutionProxy(DependencyDescriptor descriptor, String beanName) {

        Collection<String> annotationsType = getAnnotationsTypes(descriptor);
        if (annotationsType.contains(Discover.class.getName())) {
            if (descriptor.getDependencyType().equals(EventCannon.class)) {
                return produceEventCannonProxy();
            } else {
                RetryPolicyConfig retryPolicy = null;
                Optional<Annotation> retryPolicyAnn = getAnnotationWithType(descriptor, RetryPolicy.class);
                if (retryPolicyAnn.isPresent()) {
                    retryPolicy = RetryPolicyConfig.fromRetryPolicy((RetryPolicy) retryPolicyAnn.get());
                }
                TimeoutConfig timeout = null;
                Optional<Annotation> timeoutAnn = getAnnotationWithType(descriptor, Timeout.class);
                if (timeoutAnn.isPresent()) {
                    timeout = TimeoutConfig.fromTimeout((Timeout) timeoutAnn.get());
                }

                boolean useCache = annotationsType.contains(Cachable.class.getName());

                return ratelClientProducer.produceServiceProxy(descriptor.getDependencyType(), useCache, retryPolicy,
                        timeout);
            }
        }

        return super.buildLazyResolutionProxy(descriptor, beanName);
    }

    private Collection<String> getAnnotationsTypes(DependencyDescriptor descriptor) {
        Function<Annotation, String> function = new Function<Annotation, String>() {

            @Override
            public String apply(Annotation annotation) {
                return annotation.annotationType().getName();
            }
        };

        return Collections2.transform(Arrays.asList(descriptor.getAnnotations()), function);
    }

    private Object produceEventCannonProxy() {
        return ratelClientProducer.produceBroadcaster();
    }

    private static Optional<Annotation> getAnnotationWithType(DependencyDescriptor descriptor, final Class
            clazz) {
        return Iterables.tryFind(Arrays.asList(descriptor.getAnnotations()), new Predicate<Annotation>() {
            @Override
            public boolean apply(Annotation input) {
                return clazz.getName().equals(input.annotationType().getName());
            }
        });
    }

}
