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

import javax.servlet.ServletContext;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.google.common.net.HostAndPort;
import com.payu.ratel.register.RegisterStrategy;
import com.payu.ratel.register.ServiceRegisterPostProcessor;

public class ServiceRegisterPostProcessorFactory {

    public static final String RATEL_PATH = "/ratelServices/";

    // TODO - remove PMD suppress
    @SuppressWarnings("PMD.EmptyCatchBlock")
    public ServiceRegisterPostProcessor create(ConfigurableListableBeanFactory beanFactory, RegisterStrategy registerStrategy) {

        SelfAddressProviderChain selfAddressProvider = beanFactory.getBean(SelfAddressProviderChain.class);
        if (selfAddressProvider == null) {
            throw new IllegalStateException("No SelfAddressProvider bean in context");
        }
        final HostAndPort hostAndPort = selfAddressProvider.getHostAndPort();

        ServletContext servletContext = null;
        try {
            servletContext = beanFactory.getBean(ServletContext.class);
        } catch (NoSuchBeanDefinitionException e) {

        }
        final String contextRoot = servletContext != null ? servletContext.getContextPath() : "";

        final String address = String
                .format("http://%s:%s%s%s", hostAndPort.getHostText(), hostAndPort.getPort(), contextRoot, RATEL_PATH);

        return new ServiceRegisterPostProcessor(beanFactory, registerStrategy, address);
    }

}
