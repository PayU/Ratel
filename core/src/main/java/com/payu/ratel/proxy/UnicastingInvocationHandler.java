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
package com.payu.ratel.proxy;

import com.payu.ratel.client.ClientProxyGenerator;
import com.payu.ratel.client.FetchStrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UnicastingInvocationHandler implements InvocationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnicastingInvocationHandler.class);

    private final FetchStrategy fetchStrategy;
    private final Class<?> serviceApi;
    private final ClientProxyGenerator clientProxyGenerator;


    public UnicastingInvocationHandler(FetchStrategy fetchStrategy, Class<?> serviceApi, ClientProxyGenerator clientProxyGenerator) {
        this.fetchStrategy = fetchStrategy;
        this.serviceApi = serviceApi;
        this.clientProxyGenerator = clientProxyGenerator;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String serviceAddress = fetchStrategy.fetchServiceAddress(serviceApi.getName());

        if (StringUtils.isEmpty(serviceAddress)) {
            LOGGER.error("No instance named {}", serviceApi.getName());
            throw new NoServiceInstanceFound(serviceApi);
        }

        final Object clientProxy = clientProxyGenerator.generate(serviceApi, serviceAddress);

        LOGGER.debug("Calling {} on address {}", serviceApi.getName(), serviceAddress);

        try {
            return method.invoke(clientProxy, args);
        } catch (InvocationTargetException e) {
            // Invoked method threw a checked exception.
            // We must rethrow it. The client won't see the interceptor.
            throw e.getTargetException();
        }
    }
}
