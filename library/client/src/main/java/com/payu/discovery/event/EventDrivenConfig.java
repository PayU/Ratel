package com.payu.discovery.event;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({Subscribe.class})
@ConditionalOnExpression("${com.payu.discovery.enabled:true}")
public class EventDrivenConfig {

    @Bean
    public EventListener eventListener() {
        return new SimpleEventListener();
    }

    @Bean
    public EventReceiver eventReceiver() {
        return new SimpleEventReceiver(eventListener());
    }

    @Bean
    public EventSubscriberPostProcessor eventSubscriberPostProcessor() {
        return new EventSubscriberPostProcessor();
    }

}
