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
package com.payu.ratel.proxy.monitoring;

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

    final Map<String, Map<String, String>> statistics = new HashMap<>();

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

        collectStatistics(method, timer);
        return returned;
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
        if(!meters.containsKey(method)) {
            meters.put(method.toGenericString(), metrics.timer(method.toGenericString()));
        }

        return meters.get(method.toGenericString());
    }

}