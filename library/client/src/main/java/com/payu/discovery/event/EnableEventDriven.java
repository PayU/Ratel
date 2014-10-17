package com.payu.discovery.event;

import com.payu.discovery.register.config.DiscoveryServiceConfig;
import org.springframework.context.annotation.Import;

@Import({EventDrivenConfig.class, DiscoveryServiceConfig.class})
public @interface EnableEventDriven {
}
