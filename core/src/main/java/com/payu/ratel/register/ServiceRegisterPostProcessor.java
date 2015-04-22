/*
 * Copyright 2015 PayU
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.payu.ratel.register;


import static com.payu.ratel.config.beans.ServiceRegisterPostProcessorFactory.RATEL_PATH;

import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.remoting.caucho.HessianServiceExporter;

import com.payu.ratel.Publish;
import com.payu.ratel.proxy.monitoring.MonitoringInvocationHandler;

public class ServiceRegisterPostProcessor implements MergedBeanDefinitionPostProcessor {

    private static Logger LOGGER = LoggerFactory.getLogger(ServiceRegisterPostProcessor.class);

    private final ConfigurableListableBeanFactory configurableListableBeanFactory;
    private final RegisterStrategy registerStrategy;
    private final String address;
    
    private final Map<String, Class> beanTypes = new HashMap<>();

    private HashMap<Object, Object> registeredServices  = new HashMap<>();


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
        if (isService(bean, beanName)) {
            final String serviceName = getFirstInterface(bean).getSimpleName();
            final HessianServiceExporter hessianServiceExporter = exportService(bean, serviceName);
            registerStrategy.registerService(hessianServiceExporter.getServiceInterface().getCanonicalName(), address + serviceName);
            registeredServices.put(beanName, bean);
            LOGGER.info("Bean {} published as a service: {}", bean, bean.toString());
        }
        return bean;
    }

    public Map<Object, Object> getRegisteredServices() {
      return Collections.unmodifiableMap(registeredServices);
    }

    private HessianServiceExporter exportService(Object bean, String beanName) {
        final HessianServiceExporter hessianExporterService = createHessianExporterService(bean);
        configurableListableBeanFactory.registerSingleton(RATEL_PATH + beanName, hessianExporterService);
        return hessianExporterService;
    }

    private HessianServiceExporter createHessianExporterService(Object bean) {
        HessianServiceExporter hessianServiceExporter = new RatelHessianServiceExporter();
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

    private boolean isService(Object o, String beanName) {
        Class<? extends Object> realBeanClazz = o.getClass();

        //check original class of this bean, just in case it is already proxied 
        Class rootBeanClazz = beanTypes.get(beanName);
        if (rootBeanClazz != null) {
            realBeanClazz = rootBeanClazz;
        }

        return !realBeanClazz.isInterface()
            && realBeanClazz.isAnnotationPresent(Publish.class);
    }

    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
        //remember original bean class, in case some proxies will hide it
        beanTypes.put(beanName, beanType);
    }

}
