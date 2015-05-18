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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.ListableBeanFactory;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.payu.ratel.client.ServiceInstanceCallListener;
import com.payu.ratel.context.ProcessContext;
import com.payu.ratel.context.ServiceCallEvent;
import com.payu.ratel.context.ServiceCallInput;
import com.payu.ratel.context.ServiceCallResult;
import com.payu.ratel.context.ServiceResponseEvent;

public class MonitoringInvocationHandler implements java.lang.reflect.InvocationHandler {

    private final Object object;

    private final MetricRegistry metrics = new MetricRegistry();

    private final Map<String, Timer> meters = new HashMap<>();

    private final Map<String, Map<String, String>> statistics = new HashMap<>();

    private final Collection<ServiceInstanceCallListener> serviceListeners = new LinkedList<>();

    private final Class<?> apiClass;

    public MonitoringInvocationHandler(ListableBeanFactory beanFactory, Object object, Class<?> apiClass) {
        this.object = object;
        serviceListeners.addAll(beanFactory.getBeansOfType(ServiceInstanceCallListener.class).values());
        this.apiClass = apiClass;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        Timer timer = fetchTimer(method);

        final Timer.Context context = timer.time();
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
            context.stop();
            collectStatistics(method, timer);
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
        ServiceResponseEvent responseEvent = new ServiceResponseEvent(processCtx, System.nanoTime(), input,
                serviceResult);
        for (ServiceInstanceCallListener listener : serviceListeners) {
            listener.serviceInstanceResponded(responseEvent);
        }
    }

    private void publishCallEvent(ProcessContext processCtx, ServiceCallInput input) {
        ServiceCallEvent callEvent = new ServiceCallEvent(processCtx, System.nanoTime(), input);
        for (ServiceInstanceCallListener listener : serviceListeners) {
            listener.serviceInstanceInvoked(callEvent);
        }
    }

    private void collectStatistics(Method method, Timer timer) {
        Map<String, String> methodStatistics = new HashMap<>();
        methodStatistics.put("Min", format(timer.getSnapshot().getMin()));
        methodStatistics.put("Max", format(timer.getSnapshot().getMax()));
        methodStatistics.put("Mean", format(timer.getSnapshot().getMean()));
        methodStatistics.put("Median", format(timer.getSnapshot().getMedian()));
        methodStatistics.put("95 Percentile", format(timer.getSnapshot().get95thPercentile()));
        methodStatistics.put("99 Percentile", format(timer.getSnapshot().get99thPercentile()));
        methodStatistics.put("Std Dev", format(timer.getSnapshot().getStdDev()));
        statistics.put(method.getName(), methodStatistics);
        StatisticsHolder.putStatistics(method.getDeclaringClass().getName(), statistics);
    }

    private String format(long time) {
        return String.format("%2.2f", convertDuration(time));
    }

    private String format(double time) {
        return String.format("%2.2f", convertDuration(time));
    }

    protected double convertDuration(double duration) {
        return duration * (1.0 / TimeUnit.MILLISECONDS.toNanos(1));
    }

    private Timer fetchTimer(Method method) {
        if (!meters.containsKey(method.toGenericString())) {
            meters.put(method.toGenericString(), metrics.timer(method.toGenericString()));
        }

        return meters.get(method.toGenericString());
    }

}
