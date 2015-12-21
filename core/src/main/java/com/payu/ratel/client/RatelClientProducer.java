package com.payu.ratel.client;

import java.lang.reflect.Proxy;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.ProxyFactory;

import com.payu.ratel.config.TimeoutConfig;
import com.payu.ratel.event.EventCannon;
import com.payu.ratel.proxy.BroadcastingInvocationHandler;
import com.payu.ratel.proxy.CacheInvocationHandler;
import com.payu.ratel.proxy.RetryPolicyInvocationHandler;
import com.payu.ratel.proxy.UnicastingInvocationHandler;

public class RatelClientProducer {

    private final FetchStrategy fetchStrategy;
    private final ClientProxyGenerator clientProxyGenerator;

    public RatelClientProducer(FetchStrategy fetchStrategy, ClientProxyGenerator clientProxyGenerator) {
        super();
        this.fetchStrategy = fetchStrategy;
        this.clientProxyGenerator = clientProxyGenerator;
    }

    public <T> T produceServiceProxy(final Class<T> serviceContractClass, boolean useCache,
                                     Class<? extends Throwable> retryOnException, final TimeoutConfig timeout) {

        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setInterfaces(serviceContractClass);

        if (useCache) {
            proxyFactory.addAdvice(new CacheInvocationHandler());
        }

        if (retryOnException != null) {
            proxyFactory.addAdvice(new RetryPolicyInvocationHandler(retryOnException));
        }

        proxyFactory.setTargetSource(new TargetSource() {
            @Override
            public Class<?> getTargetClass() {
                return serviceContractClass;
            }

            @Override
            public boolean isStatic() {
                return false;
            }

            @Override
            public Object getTarget() {
                return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]
                        {serviceContractClass}, new UnicastingInvocationHandler(fetchStrategy, serviceContractClass,
                        clientProxyGenerator, timeout));
            }

            @Override
            public void releaseTarget(Object o) {
            }
        });

        return (T) proxyFactory.getProxy();

    }

    public Object produceBroadcaster() {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[] {EventCannon.class}, new BroadcastingInvocationHandler(fetchStrategy,
                        clientProxyGenerator, null));
    }

}
