package com.payu.discovery.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.scheduling.TaskScheduler;

import java.lang.reflect.Method;

public class EventSubscriberPostProcessor implements BeanPostProcessor {

    private static Logger LOGGER = LoggerFactory.getLogger(EventSubscriberPostProcessor.class);

    @Autowired
    EventListener eventListener;

    @Autowired
    TaskScheduler taskScheduler;

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(isSubscriber(bean)) {
            LOGGER.info("Registering event subscriber {}", beanName);
            eventListener.registerSubscriber(bean);
        }
        return bean;
    }

    private boolean isSubscriber(Object o) {
        for(Method method : o.getClass().getMethods()) {
            return method.isAnnotationPresent(Subscribe.class);
        }

        return false;
    }
}
