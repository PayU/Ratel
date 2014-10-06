package com.payu.discovery.proxy;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

import org.springframework.remoting.caucho.HessianProxyFactoryBean;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.web.HttpRequestHandler;

public final class BinaryTransportUtil {

    private BinaryTransportUtil() {
        // hidden utility class constructor
    }

    public static <T> T createServiceClientProxy(Class<T> clazz, String serviceUrl) {
        checkNotNull(clazz, "Given service class cannot be null");
        checkArgument(!isNullOrEmpty(serviceUrl), "Given serviceUrl class cannot be blank");

        HessianProxyFactoryBean proxyFactory = new HessianProxyFactoryBean();
        proxyFactory.setServiceUrl(serviceUrl);
        proxyFactory.setServiceInterface(clazz);
        proxyFactory.afterPropertiesSet();
        proxyFactory.setConnectTimeout(5000);
        proxyFactory.setReadTimeout(5000);
        return (T) proxyFactory.getObject();

    }

    public static HttpRequestHandler createServiceRequestHandler(Object service) {
        checkNotNull(service, "Given service object cannot be null");
        Class<?>[] interfaces = service.getClass().getInterfaces();
        checkArgument(interfaces.length == 1, "Given service object implements none or more than one interface");
        HessianServiceExporter exporter = new HessianServiceExporter();
        exporter.setService(service);
        exporter.setServiceInterface(interfaces[0]);
        return exporter;

    }

}