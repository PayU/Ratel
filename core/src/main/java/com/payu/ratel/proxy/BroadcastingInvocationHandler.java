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

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.payu.ratel.client.ClientProxyGenerator;
import com.payu.ratel.client.FetchStrategy;
import com.payu.ratel.config.TimeoutConfig;
import com.payu.ratel.event.EventReceiver;

public class BroadcastingInvocationHandler implements java.lang.reflect.InvocationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BroadcastingInvocationHandler.class);
    private static final Class<EventReceiver> EVENT_RECEIVER_CLASS = EventReceiver.class;

    private final FetchStrategy fetchStrategy;
    private final ClientProxyGenerator clientProxyGenerator;
    private final TimeoutConfig timeout;


    private static final Method RECEIVE_METHOD_HANDLER = getReceiveEventHandler();

    private static Method getReceiveEventHandler() {
        try {
            return EVENT_RECEIVER_CLASS.getDeclaredMethod("receiveEvent", Serializable.class);
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        }
    }


    public BroadcastingInvocationHandler(FetchStrategy fetchStrategy, ClientProxyGenerator clientProxyGenerator, TimeoutConfig timeout) {
        this.fetchStrategy = fetchStrategy;
        this.clientProxyGenerator = clientProxyGenerator;
        this.timeout = timeout;
    }

    @Override
    public synchronized Object invoke(Object o, Method method, Object[] args) throws Throwable {
        Collection<String> serviceAddresses = fetchStrategy.fetchServiceAddresses(EVENT_RECEIVER_CLASS.getName());

        if (CollectionUtils.isEmpty(serviceAddresses)) {
            LOGGER.error("No instance named {}", EVENT_RECEIVER_CLASS.getName());
            return null;
        }


        for (String serviceAddress : serviceAddresses) {
            final Object clientProxy = clientProxyGenerator.generate(EVENT_RECEIVER_CLASS, serviceAddress, timeout);
            RECEIVE_METHOD_HANDLER.invoke(clientProxy, args);
        }

        return null;
    }

}
