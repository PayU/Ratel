package com.payu.discovery.client;

import com.payu.discovery.RemoteService;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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
