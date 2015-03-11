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

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.env.Environment;

import com.payu.ratel.client.ClientProxyDecorator;
import com.payu.ratel.client.ClientProxyGenerator;
import com.payu.ratel.client.FetchStrategy;
import com.payu.ratel.client.zookeeper.ZookeeperFetcher;
import com.payu.ratel.client.zookeeper.ZookeeperProxyGenerator;
import com.payu.ratel.register.RegisterStrategy;
import com.payu.ratel.register.zookeeper.ZookeeperRegistry;

public class ZookeeperRegistryBeanProvider implements RegistryBeanProvider, InitializingBean, DisposableBean {

    private final ConfigurableListableBeanFactory beanFactory;
    private CuratorFramework curatorFramework;
    private ServiceDiscovery<Void> serviceDiscovery;
    private ZookeeperRegistry zookeeperRegistry;
    private ZookeeperFetcher zookeeperFetcher;
    private ZookeeperProxyGenerator zookeeperProxyGenerator;

    public ZookeeperRegistryBeanProvider(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final Environment environment = beanFactory.getBean(Environment.class);

        System.setProperty("zookeeper.sasl.client", "false");
        curatorFramework = CuratorFrameworkFactory.newClient(
                environment.getProperty(RegistryBeanProviderFactory.SERVICE_DISCOVERY_ZK_HOST),
                new ExponentialBackoffRetry(1000, 3));

        curatorFramework.start();

        serviceDiscovery = ServiceDiscoveryBuilder.builder(Void.class)
                .client(curatorFramework)
                .basePath("services")
                .build();

        serviceDiscovery.start();
        beanFactory.registerSingleton(serviceDiscovery.getClass().getName(), serviceDiscovery);

        zookeeperRegistry = new ZookeeperRegistry(serviceDiscovery);
        zookeeperFetcher = new ZookeeperFetcher(serviceDiscovery);
        zookeeperProxyGenerator = new ZookeeperProxyGenerator(new ClientProxyDecorator());
    }

    @Override
    public void destroy() throws Exception {
        serviceDiscovery.close();
        curatorFramework.close();
    }

    @Override
    public RegisterStrategy getRegisterStrategy() {
        return zookeeperRegistry;
    }

    @Override
    public FetchStrategy getFetchStrategy() {
        return zookeeperFetcher;
    }

    @Override
    public ClientProxyGenerator getClientProxyGenerator() {
        return zookeeperProxyGenerator;
    }
}
