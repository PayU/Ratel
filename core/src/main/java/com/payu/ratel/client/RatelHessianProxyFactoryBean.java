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
package com.payu.ratel.client;

import java.util.LinkedList;
import java.util.List;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;

import com.payu.ratel.context.ProcessContext;
import com.payu.ratel.context.RemoteServiceCallEvent;
import com.payu.ratel.context.RemoteServiceResponseEvent;
import com.payu.ratel.context.ServiceCallInput;
import com.payu.ratel.context.ServiceCallResult;

public class RatelHessianProxyFactoryBean extends HessianProxyFactoryBean {

    private List<RemoteServiceCallListener> callListeners = new LinkedList<>();

    public void setCallListeners(List<RemoteServiceCallListener> callListeners) {
        this.callListeners = callListeners;
    }

    public void addCallListeners(RemoteServiceCallListener... callListeners) {
        for (RemoteServiceCallListener cl : callListeners) {
            this.callListeners.add(cl);
        }
    }

    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    public Object invoke(MethodInvocation invocation) throws Throwable {
        beforeServiceCall(invocation);
        try {
            Object result = doInvoke(invocation);
            afterServiceSuccessfulCall(invocation, result);
            return result;
        } catch (Exception e) {
            afterServiceException(invocation, e);
            throw e;
        }
    }

    private Object doInvoke(MethodInvocation invocation) throws Throwable {
        Object result;
        try {
            result = super.invoke(invocation);
            return result;
        } catch (RemoteConnectFailureException e) {
            throw e;
        } catch (RemoteAccessException e) {
            if (e.getRootCause() != null) {
                throw e.getRootCause();
            } else {
                throw e;
            }
        }
    }

    private void beforeServiceCall(MethodInvocation invocation) {
        RemoteServiceCallEvent event = createServiceCallEvent(invocation);
        publishServiceCallEvent(event);
    }

    private void publishServiceCallEvent(RemoteServiceCallEvent event) {
        for (RemoteServiceCallListener callListener : callListeners) {
            callListener.remoteServiceCalled(event);
        }
    }

    private RemoteServiceCallEvent createServiceCallEvent(MethodInvocation invocation) {
        return new RemoteServiceCallEvent(
                ProcessContext.getInstance(),
                System.nanoTime(),
                this.getServiceUrl(),
                new ServiceCallInput(invocation.getMethod().getName(),
                        invocation.getArguments(), invocation.getMethod().getDeclaringClass()));
    }

    private void afterServiceSuccessfulCall(MethodInvocation invocation, Object result) {
        RemoteServiceResponseEvent event = createServiceRespondEvent(invocation, result);
        publishServiceResponseEvent(event);

    }

    private void publishServiceResponseEvent(RemoteServiceResponseEvent event) {
        for (RemoteServiceCallListener callListener : callListeners) {
            callListener.remoteServiceResponded(event);
        }
    }

    private void afterServiceException(MethodInvocation invocation, Exception e) {
        RemoteServiceResponseEvent event = createServiceExceptionEvent(invocation, e);
        publishServiceResponseEvent(event);

    }

    private RemoteServiceResponseEvent createServiceRespondEvent(MethodInvocation invocation, Object result) {
        return new RemoteServiceResponseEvent(
                ProcessContext.getInstance(),
                System.nanoTime(),
                this.getServiceUrl(),
                new ServiceCallInput(invocation.getMethod().getName(), invocation.getArguments(),
                        invocation.getMethod().getDeclaringClass()), ServiceCallResult.success(result));
    }

    private RemoteServiceResponseEvent createServiceExceptionEvent(MethodInvocation invocation,
            Exception exception) {
        return new RemoteServiceResponseEvent(
                ProcessContext.getInstance(),
                System.nanoTime(),
                this.getServiceUrl(),
                new ServiceCallInput(invocation.getMethod().getName(), invocation.getArguments(),
                        invocation.getMethod().getDeclaringClass()), ServiceCallResult.exception(exception));
    }

}
