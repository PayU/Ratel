package com.payu.discovery.event;

import org.springframework.context.annotation.Import;

import com.payu.discovery.config.ServiceDiscoveryConfig;

@Import({EventDrivenConfig.class, ServiceDiscoveryConfig.class})
public @interface EnableEventDriven {
}
