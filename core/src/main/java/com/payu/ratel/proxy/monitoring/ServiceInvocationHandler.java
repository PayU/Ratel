/*
 * Copyright 2015 PayU
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.payu.ratel.proxy.monitoring;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;

import org.springframework.beans.factory.ListableBeanFactory;

import com.payu.ratel.context.ProcessContext;
import com.payu.ratel.context.ServiceCallInput;
import com.payu.ratel.context.ServiceCallResult;
import com.payu.ratel.context.ServiceInstanceCallEvent;
import com.payu.ratel.context.ServiceInstanceCallListener;
import com.payu.ratel.context.ServiceInstanceResponseEvent;

public class ServiceInvocationHandler implements java.lang.reflect.InvocationHandler {

    private final Object object;

    private final Collection<ServiceInstanceCallListener> serviceListeners = new LinkedList<>();

    private final Class<?> apiClass;

    public ServiceInvocationHandler(ListableBeanFactory beanFactory, Object object, Class<?> apiClass) {
        this.object = object;
        serviceListeners.addAll(beanFactory.getBeansOfType(ServiceInstanceCallListener.class).values());
        this.apiClass = apiClass;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {

        final Object returned;
        ServiceCallResult serviceResult = null;
        ProcessContext processCtx = ProcessContext.getInstance();
        ServiceCallInput input = new ServiceCallInput(method.getName(), args, this.apiClass);
        publishCallEvent(processCtx, input);
        try {
            returned = method.invoke(object, args);
            serviceResult = ServiceCallResult.success(returned);
        } catch (InvocationTargetException e) {
            // Invoked method threw a checked exception.
            // We must rethrow it. The client won't see the interceptor.
            Throwable exception = e.getTargetException();
            serviceResult = createExceptionResult(exception);
            throw exception;
        } finally {
            publishResponseEvent(serviceResult, processCtx, input);
        }
        return returned;
    }

    @SuppressWarnings("PMD")
    private ServiceCallResult createExceptionResult(Throwable exception) {
        if (exception instanceof Exception) {
            return ServiceCallResult.exception((Exception) exception);
        } else {
            return ServiceCallResult.exception(new RuntimeException(exception));
        }
    }

    private void publishResponseEvent(ServiceCallResult serviceResult, ProcessContext processCtx, ServiceCallInput input) {
        ServiceInstanceResponseEvent responseEvent = new ServiceInstanceResponseEvent(processCtx, System.nanoTime(),
                input,
                serviceResult);
        for (ServiceInstanceCallListener listener : serviceListeners) {
            listener.serviceInstanceResponded(responseEvent);
        }
    }

    private void publishCallEvent(ProcessContext processCtx, ServiceCallInput input) {
        ServiceInstanceCallEvent callEvent = new ServiceInstanceCallEvent(processCtx, System.nanoTime(), input);
        for (ServiceInstanceCallListener listener : serviceListeners) {
            listener.serviceInstanceCalled(callEvent);
        }
    }

}
