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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;

public class EventSubscriberPostProcessor implements BeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventSubscriberPostProcessor.class);

    @Autowired
    private EventListener eventListener;


    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (isSubscriber(bean)) {
            LOGGER.info("Registering event subscriber {}", beanName);
            eventListener.registerSubscriber(bean);
        }
        return bean;
    }

    // TODO - remove PMD suppress
    @SuppressWarnings("PMD.AvoidBranchingStatementAsLastInLoop")
    private boolean isSubscriber(Object o) {
        for (Method method : o.getClass().getMethods()) {
            return method.isAnnotationPresent(Subscribe.class);
        }

        return false;
    }
}
