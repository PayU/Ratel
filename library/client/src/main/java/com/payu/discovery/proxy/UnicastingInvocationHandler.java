package com.payu.discovery.proxy;

import com.payu.discovery.client.ClientProxyGenerator;
import com.payu.discovery.client.FetchStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UnicastingInvocationHandler implements InvocationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnicastingInvocationHandler.class);

    private final FetchStrategy fetchStrategy;
    private final Class<?> serviceApi;
    private final ClientProxyGenerator clientProxyGenerator;


    public UnicastingInvocationHandler(FetchStrategy fetchStrategy, Class<?> serviceApi, ClientProxyGenerator clientProxyGenerator) {
        this.fetchStrategy = fetchStrategy;
        this.serviceApi = serviceApi;
        this.clientProxyGenerator = clientProxyGenerator;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String serviceAddress = fetchStrategy.fetchServiceAddress(serviceApi.getName());

        if (StringUtils.isEmpty(serviceAddress)) {
            LOGGER.error("No instance named {}", serviceApi.getName());
            return null;
        }

        final Object clientProxy = clientProxyGenerator.generate(serviceApi, serviceAddress);

        LOGGER.debug("Calling {} on address {}", serviceApi.getName(), serviceAddress);

        return method.invoke(clientProxy, args);
    }
}
