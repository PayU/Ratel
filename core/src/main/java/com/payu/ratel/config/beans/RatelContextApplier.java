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
package com.payu.ratel.config.beans;

import static com.payu.ratel.config.beans.RegistryBeanProviderFactory.SERVICE_DISCOVERY;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import com.payu.ratel.client.RatelClientProducer;
import com.payu.ratel.client.RemoteAutowireCandidateResolver;
import com.payu.ratel.register.ServiceRegisterPostProcessor;

public class RatelContextApplier implements BeanFactoryPostProcessor {


    public static final String SERVICE_DISCOVERY_ENABLED = SERVICE_DISCOVERY + ".enabled";

    private final RegistryBeanProviderFactory registryBeanProviderFactory;
    private final ServiceRegisterPostProcessorFactory serviceRegisterPostProcessorFactory;

    public RatelContextApplier(RegistryBeanProviderFactory registryBeanProviderFactory,
            ServiceRegisterPostProcessorFactory serviceRegisterPostProcessorFactory) {
        this.registryBeanProviderFactory = registryBeanProviderFactory;
        this.serviceRegisterPostProcessorFactory = serviceRegisterPostProcessorFactory;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        final RegistryStrategiesProvider registryBeanProvider = registryBeanProviderFactory.create(beanFactory);
        if (registryBeanProvider == null) {
            //Ratel is disabled, stop further processing
            return;
        }

        final ServiceRegisterPostProcessor serviceRegisterPostProcessor = serviceRegisterPostProcessorFactory.create(beanFactory,
                registryBeanProvider.getRegisterStrategy());
        beanFactory.registerSingleton(serviceRegisterPostProcessor.getClass().getName(), serviceRegisterPostProcessor);

        RatelClientProducer ratelClientProducer = new RatelClientProducer(registryBeanProvider.getFetchStrategy(),
                registryBeanProvider.getClientProxyGenerator());
        final RemoteAutowireCandidateResolver autowireCandidateResolver = new RemoteAutowireCandidateResolver(
                ratelClientProducer);
        ((DefaultListableBeanFactory) beanFactory).setAutowireCandidateResolver(autowireCandidateResolver);

        ((DefaultListableBeanFactory) beanFactory).setAutowireCandidateResolver(autowireCandidateResolver);

    }
}
