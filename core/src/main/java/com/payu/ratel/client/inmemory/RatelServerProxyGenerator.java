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
package com.payu.ratel.client.inmemory;

import com.payu.ratel.client.ClientProxyDecorator;
import com.payu.ratel.client.ClientProxyGenerator;

import java.util.HashMap;
import java.util.Map;

public class RatelServerProxyGenerator implements ClientProxyGenerator {
    private final ClientProxyDecorator clientProxyDecorator;

    public RatelServerProxyGenerator(ClientProxyDecorator clientProxyDecorator) {
        this.clientProxyDecorator = clientProxyDecorator;
    }

    @Override
    public Object generate(Class<?> serviceClazz, String serviceAddress) {
        return generate(serviceClazz, serviceAddress, new HashMap<String, String>());
    }

    @Override
    public Object generate(Class<?> serviceClazz, String serviceAddress, Map<String, String> requestHeaders) {
        return clientProxyDecorator.decorateWithMonitoring(clientProxyDecorator.createServiceClientProxy(serviceClazz, serviceAddress, requestHeaders),
                                                           serviceClazz);
    }
}
