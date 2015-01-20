package com.payu.ratel.event;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static com.payu.ratel.config.RatelContextInitializer.SERVICE_DISCOVERY_ENABLED;

@Configuration
@Profile(SERVICE_DISCOVERY_ENABLED)
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
