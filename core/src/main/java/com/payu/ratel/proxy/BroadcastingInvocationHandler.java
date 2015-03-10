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
import com.payu.ratel.event.EventReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;

public class BroadcastingInvocationHandler implements java.lang.reflect.InvocationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BroadcastingInvocationHandler.class);
    private static Class<EventReceiver> eventReceiverClazz = EventReceiver.class;

    private final FetchStrategy fetchStrategy;
    private final ClientProxyGenerator clientProxyGenerator;


    private static final Method RECEIVE_METHOD_HANDLER = getReceiveEventHandler();

    private static Method getReceiveEventHandler() {
        try {
            return eventReceiverClazz.getDeclaredMethod("receiveEvent", Serializable.class);
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        }
    }


    public BroadcastingInvocationHandler(FetchStrategy fetchStrategy, ClientProxyGenerator clientProxyGenerator) {
        this.fetchStrategy = fetchStrategy;
        this.clientProxyGenerator = clientProxyGenerator;
    }

    @Override
    public synchronized Object invoke(Object o, Method method, Object[] args) throws Throwable {
        Collection<String> serviceAddresses = fetchStrategy.fetchServiceAddresses(eventReceiverClazz.getName());

        if (CollectionUtils.isEmpty(serviceAddresses)) {
            LOGGER.error("No instance named {}", eventReceiverClazz.getName());
            return null;
        }


        for (String serviceAddress : serviceAddresses) {
            final Object clientProxy = clientProxyGenerator.generate(eventReceiverClazz, serviceAddress);
            RECEIVE_METHOD_HANDLER.invoke(clientProxy, args);
        }

        return null;
    }

}