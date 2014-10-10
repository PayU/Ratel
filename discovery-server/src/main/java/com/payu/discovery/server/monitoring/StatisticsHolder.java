package com.payu.discovery.server.monitoring;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StatisticsHolder {

    //TODO - refactor this bin for statically typed structure...
    // Pair of static - value for methods of services
    private Map<String, Map<String, Map<String, String>>> statistics = new ConcurrentHashMap<>();

    public void putStatistics(String service, Map<String, Map<String, String>> serviceStatistics) {
        statistics.put(service, serviceStatistics);
    }

    public Map<String, Map<String, String>> getStatistics(String service) {
        return statistics.containsKey(service) ? statistics.get(service) : new HashMap<>();
    }
}
