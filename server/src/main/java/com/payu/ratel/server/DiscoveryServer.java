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
package com.payu.ratel.server;

import com.payu.ratel.model.ServiceDescriptor;

import java.util.Collection;
import java.util.Map;

public interface DiscoveryServer {

    void registerService(ServiceDescriptor serviceDescriptor);

    Collection<ServiceDescriptor> fetchAllServices();

    void deleteAllServices();

    void collectStatistics(String service, Map<String, Map<String, String>> statistics);

}
