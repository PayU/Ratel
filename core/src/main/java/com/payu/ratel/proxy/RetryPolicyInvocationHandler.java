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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RetryPolicyInvocationHandler implements java.lang.reflect.InvocationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetryPolicyInvocationHandler.class);

    private static final long RETRY_TIME = 5000;

    private static final long RETRY_COUNT = 5;

    private final Class exception;

    private Object object;

    public RetryPolicyInvocationHandler(Object object, Class exception) {
        this.object = object;
        this.exception = exception;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        return invokeWithRetry(method, args, 1);
    }

    public Object invokeWithRetry(Method method, Object[] args, int count) throws Throwable {
        try {
            return method.invoke(object, args);
        } catch (InvocationTargetException thrownException) {
            LOGGER.info("Service thrown exception");
            if(isInStacktrace(thrownException, exception) && count < RETRY_COUNT) {
                Thread.sleep(RETRY_TIME);
                return invokeWithRetry(method, args, count++);
            }

            throw thrownException;
        }
    }

    private boolean isInStacktrace(Throwable stackTrace, Class target) {
        while(stackTrace != null) {
            if(stackTrace.getClass().equals(target)) {
                return true;
            }

            stackTrace = stackTrace.getCause();
        }

        return false;
    }

}