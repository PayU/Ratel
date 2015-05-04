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

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.env.Environment;

import com.payu.ratel.client.ClientProxyGenerator;
import com.payu.ratel.client.FetchStrategy;
import com.payu.ratel.register.RegisterStrategy;

public class ZookeeperRegistryBeanProvider implements RegistryStrategiesProvider, InitializingBean, DisposableBean {

    private final ConfigurableListableBeanFactory beanFactory;
    private final ZookeeperServiceRegistryStrategy zookeeperStrategy = new ZookeeperServiceRegistryStrategy();

    public ZookeeperRegistryBeanProvider(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    // TODO - remove PMD suppress
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    @Override
    public void afterPropertiesSet() throws Exception {
        final Environment environment = beanFactory.getBean(Environment.class);
        zookeeperStrategy.configureWithZookeeperHost(environment.getProperty(RegistryBeanProviderFactory.SERVICE_DISCOVERY_ZK_HOST));

        beanFactory.registerSingleton(zookeeperStrategy.getServiceDiscovery().getClass().getName(), zookeeperStrategy.getServiceDiscovery());
    }

    // TODO - remove PMD suppress
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    @Override
    public void destroy() throws Exception {
        zookeeperStrategy.destroy();
    }

    @Override
    public RegisterStrategy getRegisterStrategy() {
        return zookeeperStrategy.getRegisterStrategy();
    }

    @Override
    public FetchStrategy getFetchStrategy() {
        return zookeeperStrategy.getFetchStrategy();
    }

    @Override
    public ClientProxyGenerator getClientProxyGenerator() {
        return zookeeperStrategy.getClientProxyGenerator();
    }
}
