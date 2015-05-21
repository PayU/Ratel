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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.remoting.caucho.HessianServiceExporter;

import com.payu.ratel.Publish;
import com.payu.ratel.exception.PublishException;
import com.payu.ratel.proxy.monitoring.ServiceInvocationHandler;

public class ServiceRegisterPostProcessor implements MergedBeanDefinitionPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegisterPostProcessor.class);

    private final ConfigurableListableBeanFactory configurableListableBeanFactory;
    private final RegisterStrategy registerStrategy;
    private final String address;

    private final Map<String, Class> beanTypes = new HashMap<>();

    private final Map<String, Object> registeredServices = new HashMap<>();


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
            final String relativeServiceAddress = getPublishInterface(bean, beanName).getCanonicalName();

            final HessianServiceExporter hessianServiceExporter = exportService(bean, relativeServiceAddress);
            registerStrategy.registerService(hessianServiceExporter.getServiceInterface().getCanonicalName(),
                    address + relativeServiceAddress);
            Object previouslyRegistered = registeredServices.put(beanName, bean);
            assert previouslyRegistered == null;
            LOGGER.info("Bean {} published as a service: {}", bean, relativeServiceAddress);
        }
        return bean;
    }

    /**
     * Get a map of Ratel services exported by this post processor.
     *
     * @return the unmodifiable map with entries in form: [bean name] -&gt; [bean].
     * The beans of this map are the providers of the implementation of
     * the service business interface.
     */
    public Map<String, Object> getRegisteredServices() {
        return Collections.unmodifiableMap(registeredServices);
    }

    private HessianServiceExporter exportService(Object bean, String beanName) {
        final HessianServiceExporter hessianExporterService = createHessianExporterService(bean);
        configurableListableBeanFactory.registerSingleton(RATEL_PATH + beanName, hessianExporterService);
        return hessianExporterService;
    }

    private HessianServiceExporter createHessianExporterService(Object bean) {
        HessianServiceExporter hessianServiceExporter = new RatelHessianServiceExporter();
        hessianServiceExporter.setService(decorateWithMonitoring(bean, getPublishInterface(bean)));
        hessianServiceExporter.setServiceInterface(getPublishInterface(bean));
        hessianServiceExporter.prepare();
        return hessianServiceExporter;
    }

    public Object decorateWithMonitoring(final Object object, final Class clazz) {
        return Proxy
                .newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class[]{clazz}, new ServiceInvocationHandler(configurableListableBeanFactory, object, clazz));
    }

    private Class<?> getPublishInterface(Object bean) {
        return getPublishInterface(bean, null);
    }

    private Class<?> getPublishInterface(Object bean, String beanName) {
        Class<? extends Object> realBeanClazz = getRealBeanClass(bean, beanName);

        Publish publish = realBeanClazz.getAnnotation(Publish.class);

        if (publish == null) {
            return getFirstInterfaceOrDefined(bean, null);
        }

        Class publishedInterface = publish.value();

        if (publishedInterface == Void.class) {
            return getFirstInterfaceOrDefined(bean, null);
        }

        return getFirstInterfaceOrDefined(bean, publishedInterface);
    }

    private Class<?> getFirstInterfaceOrDefined(Object bean, Class defInterface) {
        Class<?>[] interfaces = bean.getClass().getInterfaces();
        for (Class<?> clazz : interfaces) {
            if (defInterface == null) {
                return clazz;
            } else {
                if (clazz.equals(defInterface)) {
                    return clazz;
                }
            }
        }

        throw new PublishException(
                bean.getClass().getCanonicalName() + " does not implement interface " + defInterface.getCanonicalName());
    }

    private Class getRealBeanClass(Object o, String beanName) {
        Class<? extends Object> realBeanClazz = o.getClass();

        //check original class of this bean, just in case it is already proxied
        Class rootBeanClazz = beanTypes.get(beanName);
        if (rootBeanClazz != null) {
            realBeanClazz = rootBeanClazz;
        }

        return realBeanClazz;
    }

    private boolean isService(Object o, String beanName) {
        Class realBeanClazz = getRealBeanClass(o, beanName);

        return !realBeanClazz.isInterface()
                && realBeanClazz.isAnnotationPresent(Publish.class);
    }

    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
        //remember original bean class, in case some proxies will hide it
        beanTypes.put(beanName, beanType);
    }

}
