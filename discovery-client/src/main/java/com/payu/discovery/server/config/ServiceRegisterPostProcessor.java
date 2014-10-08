package com.payu.discovery.server.config;

import com.payu.discovery.Publish;
import com.payu.discovery.model.ServiceDescriptionBuilder;
import com.payu.discovery.model.ServiceDescriptor;
import com.payu.discovery.proxy.ProxyMonitoring;
import com.payu.discovery.server.RemoteRestDiscoveryServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.scheduling.TaskScheduler;

import java.lang.reflect.Proxy;

public class ServiceRegisterPostProcessor implements BeanPostProcessor {

    private static Logger LOGGER = LoggerFactory.getLogger(ServiceRegisterPostProcessor.class);

    public static final int SECONDS_20 = 20000;

    @Value("${app.address:http://localhost:8080}")
    private String address;

    @Autowired
    private RemoteRestDiscoveryServer server;

    @Autowired
    ConfigurableListableBeanFactory configurableListableBeanFactory;

    @Autowired
    TaskScheduler taskScheduler;

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (isService(bean)) {
            final String serviceName = getFirstInterface(bean).getSimpleName();
            final HessianServiceExporter hessianServiceExporter = exportService(bean, serviceName);
            registerService(hessianServiceExporter, "/" + serviceName);
            LOGGER.info("Bean {} published as a service: {}", bean, bean.toString());
        }
        return bean;
    }

    private void registerService(HessianServiceExporter bean, String beanName) {
        ServiceDescriptor serviceDescriptor = buildService(bean, beanName);
        LOGGER.info("Registering service {}", serviceDescriptor);
        taskScheduler.scheduleAtFixedRate(() -> server.registerService(serviceDescriptor), SECONDS_20);
    }

    private ServiceDescriptor buildService(HessianServiceExporter bean, String beanName) {
        String name = bean.getServiceInterface().getCanonicalName();
        return ServiceDescriptionBuilder
                .aService()
                .withName(name)
                .withAddress(address + beanName)
                .build();
    }

    private HessianServiceExporter exportService(Object bean, String beanName) {
        final HessianServiceExporter hessianExporterService = createHessianExporterService(bean);
        configurableListableBeanFactory.registerSingleton("/" + beanName, hessianExporterService);
        return hessianExporterService;
    }

    private HessianServiceExporter createHessianExporterService(Object bean) {
        HessianServiceExporter hessianServiceExporter = new HessianServiceExporter();
        hessianServiceExporter.setService(decorateWithMonitoring(bean, getFirstInterface(bean)));
        hessianServiceExporter.setServiceInterface(getFirstInterface(bean));
        hessianServiceExporter.prepare();
        return hessianServiceExporter;
    }

    public Object decorateWithMonitoring(final Object object, final Class clazz) {
        return Proxy
                .newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class[]{clazz}, new ProxyMonitoring(object));
    }

    private Class<?> getFirstInterface(Object bean) {
        Class<?>[] interfaces = bean.getClass().getInterfaces();
        for (Class<?> clazz : interfaces) {
            return clazz;
        }
        return null;
    }

    private boolean isService(Object o) {
        return !o.getClass().isInterface()
                && o.getClass().isAnnotationPresent(Publish.class);
    }
}
