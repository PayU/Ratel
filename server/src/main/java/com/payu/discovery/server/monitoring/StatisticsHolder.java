package com.payu.discovery.server.monitoring;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class StatisticsHolder {

    //TODO - refactor this bin for statically typed structure...
    // Pair of server - value for methods of services
    private Map<String, Map<String, Map<String, String>>> statistics = new ConcurrentHashMap<>();

    public void putStatistics(String address, Map<String, Map<String, String>> serviceStatistics) {
        statistics.put(address, serviceStatistics);
    }

    public Map<String, Map<String, String>> getStatistics(String address) {
        return statistics.containsKey(address) ? statistics.get(address) : new HashMap<String, Map<String, String>>();
    }
}
