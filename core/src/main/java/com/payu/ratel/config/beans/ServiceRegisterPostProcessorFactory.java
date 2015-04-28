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

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.env.Environment;

import com.payu.ratel.register.RegisterStrategy;
import com.payu.ratel.register.ServiceRegisterPostProcessor;

public class ServiceRegisterPostProcessorFactory {

    public static final String JBOSS_BIND_ADDRESS = "jboss.bind.address";
    public static final String JBOSS_BIND_PORT = "jboss.bind.port";
    public static final String RATEL_PATH = "/ratelServices/";

    // TODO - remove PMD suppress
    @SuppressWarnings("PMD.EmptyCatchBlock")
    public ServiceRegisterPostProcessor create(ConfigurableListableBeanFactory beanFactory, RegisterStrategy registerStrategy) {

        final Environment environment = beanFactory.getBean(Environment.class);

        ServletContext servletContext = null;
        try {
            servletContext = beanFactory.getBean(ServletContext.class);
        } catch (BeansException e) {
        }

        final String host = environment.getProperty(JBOSS_BIND_ADDRESS, "localhost");
        final String port = environment.getProperty(JBOSS_BIND_PORT, "8080");
        final String contextRoot = servletContext != null ? servletContext.getContextPath() : "";

        final String address = String.format("http://%s:%s%s%s", host, port, contextRoot, RATEL_PATH);

        return new ServiceRegisterPostProcessor(beanFactory, registerStrategy, address);
    }
}
