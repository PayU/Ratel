package com.payu.ratel.register;


import static com.payu.ratel.config.beans.ServiceRegisterPostProcessorFactory.RATEL_PATH;

import java.lang.reflect.Proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.remoting.caucho.HessianServiceExporter;

import com.payu.ratel.Publish;
import com.payu.ratel.proxy.monitoring.MonitoringInvocationHandler;

public class ServiceRegisterPostProcessor implements BeanPostProcessor {

    private static Logger LOGGER = LoggerFactory.getLogger(ServiceRegisterPostProcessor.class);

    private final ConfigurableListableBeanFactory configurableListableBeanFactory;
    private final RegisterStrategy registerStrategy;
    private final String address;


    public ServiceRegisterPostProcessor(ConfigurableListableBeanFactory configurableListableBeanFactory,
                                        RegisterStrategy registerStrategy, String address) {
        this.configurableListableBeanFactory = configurableListableBeanFactory;
        this.registerStrategy = registerStrategy;
        this.address = address;
    }

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (isService(bean)) {
            final String serviceName = getFirstInterface(bean).getSimpleName();
            final HessianServiceExporter hessianServiceExporter = exportService(bean, serviceName);
            registerStrategy.registerService(hessianServiceExporter.getServiceInterface().getCanonicalName(), address + serviceName);
            LOGGER.info("Bean {} published as a service: {}", bean, bean.toString());
        }
        return bean;
    }

    private HessianServiceExporter exportService(Object bean, String beanName) {
        final HessianServiceExporter hessianExporterService = createHessianExporterService(bean);
        configurableListableBeanFactory.registerSingleton(RATEL_PATH + beanName, hessianExporterService);
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
                        new Class[]{clazz}, new MonitoringInvocationHandler(object));
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
