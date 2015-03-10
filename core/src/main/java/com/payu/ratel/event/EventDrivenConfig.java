package com.payu.ratel.event;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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
