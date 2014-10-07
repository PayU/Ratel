package com.payu.discovery.server;

import com.payu.discovery.server.config.DiscoveryServiceConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(DiscoveryServiceConfig.class)
public @interface EnableServicePublish {
}
