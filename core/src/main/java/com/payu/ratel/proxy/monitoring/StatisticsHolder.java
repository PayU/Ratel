package com.payu.ratel.proxy.monitoring;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StatisticsHolder {

    //TODO - refactor this bin for statically typed structure...
    // Pair of static - value for methods of services
    private static Map<String, Map<String, Map<String, String>>> statistics = new ConcurrentHashMap<>();

    public static void putStatistics(String service, Map<String, Map<String, String>> serviceStatistics) {
        statistics.put(service, serviceStatistics);
    }

    public static Map<String, Map<String, String>> getStatistics(String service) {
        return statistics.containsKey(service) ? statistics.get(service) : new HashMap<String, Map<String, String>>();
    }
}
