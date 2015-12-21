package com.payu.ratel.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Import(ServiceDiscoveryConfig.class)
public @interface Timeout {

    int readTimeout() default 30000;

    int connectTimeout() default 30000;
}
