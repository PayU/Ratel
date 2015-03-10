package com.payu.ratel.event;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleEventListener implements EventListener {

    private Map<Class<? extends Serializable>, Collection<Object>> listeners =
            new ConcurrentHashMap<>();

    private Map<Object, Collection<Method>> subscribedMethods =
            new ConcurrentHashMap<>();

    @Override
    public synchronized void registerSubscriber(Object listener) {
        for (Method method : listener.getClass().getMethods()) {
            if(method.isAnnotationPresent(Subscribe.class)) {
                final Class<?>[] parameters = method.getParameterTypes();
                validateParameters(parameters);
                registerSubscriber((Class<? extends Serializable>) parameters[0], listener, method);
            }
        }
    }

    private void validateParameters(Class<?>[] parameters) {
        if(parameters == null || parameters.length > 1) {
            throw new RuntimeException("Subscriber method should declare only one argument");
        }

        if(!Serializable.class.isAssignableFrom(parameters[0])) {
            throw new RuntimeException("Event type should be serializable");
        }
    }

    private void registerSubscriber(Class<? extends Serializable> eventType, Object listener, Method method) {
        if(!listeners.containsKey(eventType)) {
            listeners.put(eventType, new HashSet<>());
        }

        final Collection<Object> eventListeners = listeners.get(eventType);
        eventListeners.add(listener);

        if(!subscribedMethods.containsKey(listener)) {
            subscribedMethods.put(listener, new HashSet<Method>());
        }

        final Collection<Method> methods = subscribedMethods.get(listener);
        methods.add(method);
    }

    @Override
    public void listen(Serializable event) {
        final Collection<Object> eventListeners = listeners.get(event.getClass());
        if(eventListeners != null) {
            for(Object listener : eventListeners){
                Collection<Method> methods = subscribedMethods.get(listener);
                for(Method method : methods){
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
