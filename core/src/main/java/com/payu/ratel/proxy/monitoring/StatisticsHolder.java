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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class StatisticsHolder {

    private StatisticsHolder() {
    }

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
