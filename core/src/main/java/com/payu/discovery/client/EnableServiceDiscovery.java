package com.payu.discovery.client;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.payu.discovery.config.ServiceDiscoveryConfig;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ServiceDiscoveryConfig.class)
public @interface EnableServiceDiscovery {
}
