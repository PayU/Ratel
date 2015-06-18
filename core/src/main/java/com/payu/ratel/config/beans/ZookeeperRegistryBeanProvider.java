/*
 * Copyright 2015 PayU
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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

import com.payu.ratel.client.ClientProxyGenerator;
import com.payu.ratel.client.zookeeper.ZookeeperFetcher;
import com.payu.ratel.client.zookeeper.ZookeeperProxyGenerator;
import com.payu.ratel.register.zookeeper.ZookeeperRegistry;

public class ZookeeperRegistryBeanProvider implements RegistryStrategiesProvider, InitializingBean, DisposableBean {

    private final ConfigurableListableBeanFactory beanFactory;

    private CuratorFramework curatorFramework;
    private ServiceDiscovery<Void> serviceDiscovery;
    private ZookeeperRegistry zookeeperRegistry;
    private ZookeeperFetcher zookeeperFetcher;
    private ClientProxyGenerator zookeeperProxyGenerator;

    public ZookeeperRegistryBeanProvider(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public ServiceDiscovery<Void> getServiceDiscovery() {
        return serviceDiscovery;
    }

    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    private void configure() {
        System.setProperty("zookeeper.sasl.client", "false");
        Environment env = beanFactory.getBean(Environment.class);
        String zkHostAddress = env.getProperty(RegistryBeanProviderFactory.SERVICE_DISCOVERY_ZK_HOST);
        this.curatorFramework = CuratorFrameworkFactory.newClient(zkHostAddress, new ExponentialBackoffRetry(1000, 3));

        curatorFramework.start();

        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(Void.class).client(this.curatorFramework)
                .basePath("/services").build();

        try {
            getServiceDiscovery().start();
        } catch (Exception e) {
            throw new IllegalStateException("Service discovery start failed.", e);
        }

        this.zookeeperRegistry = new ZookeeperRegistry(getServiceDiscovery());
        this.zookeeperFetcher = new ZookeeperFetcher(getServiceDiscovery());
        this.zookeeperProxyGenerator = new ZookeeperProxyGenerator(beanFactory);
    }

    @Override
    public void afterPropertiesSet() {
        this.configure();
        beanFactory.registerSingleton(this.getServiceDiscovery().getClass().getName(), this.getServiceDiscovery());
    }

    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    public void destroy() throws Exception {
        serviceDiscovery.close();
        curatorFramework.close();
    }

    public ZookeeperRegistry getRegisterStrategy() {
        return zookeeperRegistry;
    }

    public ZookeeperFetcher getFetchStrategy() {
        return zookeeperFetcher;
    }

    public ClientProxyGenerator getClientProxyGenerator() {
        return zookeeperProxyGenerator;
    }
}
