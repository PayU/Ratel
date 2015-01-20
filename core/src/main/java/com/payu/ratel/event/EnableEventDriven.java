package com.payu.ratel.event;

import org.springframework.context.annotation.Import;

import com.payu.ratel.config.ServiceDiscoveryConfig;

@Import({EventDrivenConfig.class, ServiceDiscoveryConfig.class})
public @interface EnableEventDriven {
}
