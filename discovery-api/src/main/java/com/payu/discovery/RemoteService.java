package com.payu.discovery;

import org.springframework.context.annotation.Lazy;

import java.lang.annotation.ElementType;

@java.lang.annotation.Target({ElementType.TYPE, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.ANNOTATION_TYPE})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
@Lazy
public @interface RemoteService {
}
