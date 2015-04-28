package com.payu.ratel.tests.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Just adds a proxy before and after initialization of all beans of type
 * {@link ProxableService}, so that we can verify if all mechanisms are
 * reasistant to substitution of bean with a proxy.
 */
public class ProxyingPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(final Object bean, String beanName) throws BeansException {
        if (shouldProxy(bean)) {
            return wrapWithProxy(bean);
        } else {
            return bean;
        }

    }

    private Object wrapWithProxy(final Object bean) {
        Class<?>[] interfaces = bean.getClass().getInterfaces();
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), interfaces,
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object arg0, Method method, Object[] args) throws Throwable {
                        return method.invoke(bean, args);
                    }
                });
    }

    private boolean shouldProxy(final Object bean) {
        return bean instanceof ProxableService;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (shouldProxy(bean)) {
            return wrapWithProxy(bean);
        } else {
            return bean;
        }

    }

}
