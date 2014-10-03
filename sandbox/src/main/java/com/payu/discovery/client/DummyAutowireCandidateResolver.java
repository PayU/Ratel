package com.payu.discovery.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;

import scala.annotation.meta.getter;

import com.payu.discovery.proxy.RemoteService;

public class DummyAutowireCandidateResolver extends
        ContextAnnotationAutowireCandidateResolver implements
        AutowireCandidateResolver {



    public DummyAutowireCandidateResolver() {

    }

    @Override
    protected Object buildLazyResolutionProxy(DependencyDescriptor descriptor,
                                              String beanName) {
        if (descriptor.getField().isAnnotationPresent(RemoteService.class)) {
            return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{descriptor.getField().getType()}, new InvocationHandler() {
				
				@Override
				public Object invoke(Object proxy, Method method, Object[] args)
						throws Throwable {
					System.out.printf("Method %s called on laizly injected proxy\n ",  method.getName());
					return null;
				}
			});
        }
        return super.buildLazyResolutionProxy(descriptor, beanName);
    }

}
