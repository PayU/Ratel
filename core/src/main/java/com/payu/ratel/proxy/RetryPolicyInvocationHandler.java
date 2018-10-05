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

import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.ProxyMethodInvocation;

import com.payu.ratel.config.RetryPolicyConfig;

public class RetryPolicyInvocationHandler implements MethodInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetryPolicyInvocationHandler.class);

    private final RetryPolicyConfig config;

    public RetryPolicyInvocationHandler(RetryPolicyConfig config) {
        this.config = config;
    }

    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    public Object invokeWithRetry(MethodInvocation methodInvocation, int count) throws Throwable {
        MethodInvocation methodInvocationCopy = ((ProxyMethodInvocation) methodInvocation).invocableClone();
        try {
            return methodInvocationCopy.proceed();
        } catch (Throwable thrownException) {
            LOGGER.warn("Ratel - Retry Policy was triggered for service {} because: {} ", methodInvocation.getMethod(),
                    thrownException.getMessage());
            final List<Class<? extends Throwable>> retryOnExceptions = config.getRetryOnExceptions();
            if (shouldRetry(count, thrownException, retryOnExceptions)) {
                Thread.sleep(config.getWaitingTime());

                return invokeWithRetry(methodInvocation, count + 1);
            }

            throw thrownException;
        }
    }

    private boolean shouldRetry(int count, Throwable thrownException, List<Class<? extends Throwable>> retryOnExceptions) {
        for (Class<? extends Throwable> ex : retryOnExceptions) {
            if (count < config.getRetryCount() && shouldRetryException(thrownException, ex)) {
                return true;
            }
        }
        return false;
    }

    private boolean shouldRetryException(Throwable thrownException, Class<? extends Throwable> retryOnException) {
        return retryOnException != null && retryOnException.isAssignableFrom(thrownException.getClass());
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        return invokeWithRetry(methodInvocation, 1);
    }
}
