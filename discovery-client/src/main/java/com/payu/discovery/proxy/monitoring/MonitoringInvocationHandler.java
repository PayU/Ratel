package com.payu.discovery.proxy.monitoring;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MonitoringInvocationHandler implements java.lang.reflect.InvocationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringInvocationHandler.class);

    private Object object;

    final MetricRegistry metrics = new MetricRegistry();

    final Map<String, Timer> meters = new HashMap<>();

    ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.MILLISECONDS)
            .build();

    public MonitoringInvocationHandler(Object object) {
        this.object = object;
        reporter.start(30, TimeUnit.SECONDS);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        Timer timer = fetchTimer(method);

        final Timer.Context context = timer.time();
        final Object returned = method.invoke(object, args);
        context.stop();

        return returned;
    }

    private Timer fetchTimer(Method method) {
        if(!meters.containsKey(method)) {
            meters.put(method.toGenericString(), metrics.timer(method.toGenericString()));
        }

        return meters.get(method.toGenericString());
    }

}