/*
 * Copyright 2015 PayU
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.payu.ratel.config.beans;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.payu.ratel.client.ClientProxyGenerator;
import com.payu.ratel.client.FetchStrategy;
import com.payu.ratel.register.RegisterStrategy;

public class InMemoryRegistryBeanProviderFactory implements RegistryStrategiesProvider, InitializingBean {


    private final ConfigurableListableBeanFactory beanFactory;
    private InMemoryServiceRegistryStrategy inMemoryServiceRegistryStrategy;

    public InMemoryRegistryBeanProviderFactory(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() {
        final Environment env = beanFactory.getBean(Environment.class);



        TaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        final String schedulerBeanName = taskScheduler.getClass().getName();
        beanFactory.registerSingleton(schedulerBeanName, taskScheduler);
        beanFactory.initializeBean(taskScheduler, schedulerBeanName);

        inMemoryServiceRegistryStrategy = new InMemoryServiceRegistryStrategy();

        inMemoryServiceRegistryStrategy.configure(env, taskScheduler);
    }

    @Override
    public RegisterStrategy getRegisterStrategy() {
        return inMemoryServiceRegistryStrategy.getRegisterStrategy();
    }

    @Override
    public FetchStrategy getFetchStrategy() {
        return inMemoryServiceRegistryStrategy.getFetchStrategy();
    }

    @Override
    public ClientProxyGenerator getClientProxyGenerator() {
        return inMemoryServiceRegistryStrategy.getClientProxyGenerator();
    }
}
