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
package com.payu.ratel.event;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleEventListener implements EventListener {

    private final Map<Class<? extends Serializable>, Collection<Object>> listeners =
            new ConcurrentHashMap<>();

    private final Map<Object, Collection<Method>> subscribedMethods =
            new ConcurrentHashMap<>();

    // TODO - remove PMD suppress
    @SuppressWarnings("PMD.AvoidSynchronizedAtMethodLevel")
    @Override
    public synchronized void registerSubscriber(Object listener) {
        for (Method method : listener.getClass().getMethods()) {
            if (method.isAnnotationPresent(Subscribe.class)) {
                final Class<?>[] parameters = method.getParameterTypes();
                validateParameters(parameters);
                registerSubscriber((Class<? extends Serializable>) parameters[0], listener, method);
            }
        }
    }

    // TODO - remove PMD suppress
    @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
    private void validateParameters(Class<?>[] parameters) {
        if (parameters == null || parameters.length > 1) {
            throw new RuntimeException("Subscriber method should declare only one argument");
        }

        if (!Serializable.class.isAssignableFrom(parameters[0])) {
            throw new RuntimeException("Event type should be serializable");
        }
    }

    private void registerSubscriber(Class<? extends Serializable> eventType, Object listener, Method method) {
        if (!listeners.containsKey(eventType)) {
            listeners.put(eventType, new HashSet<>());
        }

        final Collection<Object> eventListeners = listeners.get(eventType);
        eventListeners.add(listener);

        if (!subscribedMethods.containsKey(listener)) {
            subscribedMethods.put(listener, new HashSet<Method>());
        }

        final Collection<Method> methods = subscribedMethods.get(listener);
        methods.add(method);
    }

    // TODO - remove PMD suppress
    @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
    @Override
    public void listen(Serializable event) {
        final Collection<Object> eventListeners = listeners.get(event.getClass());
        if (eventListeners != null) {
            for (Object listener : eventListeners) {
                Collection<Method> methods = subscribedMethods.get(listener);
                for (Method method : methods) {
                    try {
                        method.invoke(listener, event);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

}
