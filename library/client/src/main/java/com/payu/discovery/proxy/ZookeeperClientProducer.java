package com.payu.discovery.proxy;

import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.curator.x.discovery.strategies.RoundRobinStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ZookeeperClientProducer implements ClientProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperClientProducer.class);

    private final ServiceDiscovery serviceDiscovery;

    public ZookeeperClientProducer(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    @Override
    public Object produceLoadBalancer(Class<?> clazz) {
        final ServiceProvider provider = serviceDiscovery.serviceProviderBuilder().serviceName(clazz.getName())
                .providerStrategy(new RoundRobinStrategy()).build();
        try {
            provider.start();

            return Proxy
                    .newProxyInstance(Thread.currentThread().getContextClassLoader(),
                            new Class[]{clazz}, new ZkLoadBalancing(provider, clazz));
        } catch (Exception e) {
            throw new IllegalStateException("Cannot start discovery provider");
        }
    }

    @Override
    public Object produceBroadcaster() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class ZkLoadBalancing implements InvocationHandler {

        private final ServiceProvider provider;
        private final Class<?> serviceApi;


        public ZkLoadBalancing(ServiceProvider provider, Class<?> serviceApi) {
            this.provider = provider;
            this.serviceApi = serviceApi;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            final ServiceInstance serviceInstance = provider.getInstance();
            if (serviceInstance == null) {
                LOGGER.error("No instance named {}", serviceApi.getName());
                return null;
            }

            final Object clientProxy = BinaryTransportUtil
                    .createServiceClientProxy(serviceApi, serviceInstance.getAddress());

            return method.invoke(clientProxy, args);
        }
    }
}
