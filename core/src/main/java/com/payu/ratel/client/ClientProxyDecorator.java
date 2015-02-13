package com.payu.ratel.client;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

import java.lang.reflect.Proxy;

import org.springframework.remoting.caucho.HessianProxyFactoryBean;

import com.payu.ratel.proxy.monitoring.MonitoringInvocationHandler;

public class ClientProxyDecorator {

    public static final int CONNECT_READ_TIMEOUT = 30000;

    public Object decorateWithMonitoring(final Object object, final Class clazz) {
        return Proxy
                .newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class[]{clazz}, new MonitoringInvocationHandler(object));
    }

    public <T> T createServiceClientProxy(Class<T> clazz, String serviceUrl) {
        checkNotNull(clazz, "Given service class cannot be null");
        checkArgument(!isNullOrEmpty(serviceUrl), "Given serviceUrl class cannot be blank");

        HessianProxyFactoryBean proxyFactory = new RatelHessianProxyFactoryBean();
        proxyFactory.setServiceUrl(serviceUrl);
        proxyFactory.setServiceInterface(clazz);
        proxyFactory.afterPropertiesSet();
        proxyFactory.setConnectTimeout(CONNECT_READ_TIMEOUT);
        proxyFactory.setReadTimeout(CONNECT_READ_TIMEOUT);
        return (T) proxyFactory.getObject();

    }

}
