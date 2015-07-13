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
package com.payu.ratel.proxy;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class CacheInvocationHandler implements MethodInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheInvocationHandler.class);

    private final Cache<MethodWithArguments, Object> cache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        final MethodWithArguments methodWithArguments = new MethodWithArguments(method, methodInvocation.getArguments());
        final Object cachedResults = cache.getIfPresent(methodWithArguments);
        if (cachedResults == null) {
            LOGGER.info("New invocation of method {}", method.getName());
            final Object results = methodInvocation.proceed();
            if (results != null) {
                cache.put(methodWithArguments, results);
            }
            return results;
        }

        LOGGER.info("Cached invocation of method {}", method.getName());
        return cachedResults;
    }

    private final class MethodWithArguments {
        private final Method method;
        private final Object[] args;

        @SuppressWarnings("PMD.ArrayIsStoredDirectly")
        private MethodWithArguments(Method method, Object[] args) {
            this.method = method;
            this.args = args;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            MethodWithArguments that = (MethodWithArguments) o;

            // Probably incorrect - comparing Object[] arrays with Arrays.equals
            if (!Arrays.equals(args, that.args)) {
                return false;
            }

            return method.equals(that.method);

        }

        @Override
        public int hashCode() {
            int result = method.hashCode();
            result = 31 * result + Arrays.hashCode(args);
            return result;
        }
    }

}
