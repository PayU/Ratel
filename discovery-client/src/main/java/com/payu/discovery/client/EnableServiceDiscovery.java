package com.payu.discovery.client;

import com.payu.discovery.client.config.ServiceDiscoveryClientConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ServiceDiscoveryClientConfig.class)
public @interface EnableServiceDiscovery {
}
