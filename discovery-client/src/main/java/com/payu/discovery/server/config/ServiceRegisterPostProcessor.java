package com.payu.discovery.server.config;

import com.payu.discovery.model.ServiceDescriptionBuilder;
import com.payu.discovery.model.ServiceDescriptor;
import com.payu.discovery.server.RemoteRestDiscoveryServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.remoting.caucho.HessianServiceExporter;

public class ServiceRegisterPostProcessor implements BeanPostProcessor {

    @Value("${app.address:http://localhost:8080}")
    private String address;

    @Autowired
    private RemoteRestDiscoveryServer server;

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (isService(bean)) {
            registerService(bean, beanName);
            System.out.println("Bean '" + bean + "' published as a service: " + bean.toString());
        }
        return bean;
    }

    private void registerService(Object bean, String beanName) {
        ServiceDescriptor serviceDescriptor = buildService(bean, beanName);
        System.out.println("Registering service " + serviceDescriptor);
        server.registerService(serviceDescriptor);
    }

    private ServiceDescriptor buildService(Object bean, String beanName) {
        String name = ((HessianServiceExporter)bean).getServiceInterface().getCanonicalName();
        return ServiceDescriptionBuilder
                .aService()
                .withName(name)
                .withAddress(address + beanName)
                .build();
    }

    private boolean isService(Object o) {
        return o.getClass().equals(HessianServiceExporter.class);
    }
}
