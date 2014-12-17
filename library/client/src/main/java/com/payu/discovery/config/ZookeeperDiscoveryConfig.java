package com.payu.discovery.config;

import com.payu.discovery.client.ClientProxyDecorator;
import com.payu.discovery.client.zookeeper.ZookeeperFetcher;
import com.payu.discovery.client.zookeeper.ZookeeperProxyGenerator;
import com.payu.discovery.register.zookeeper.ZookeeperRegistry;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@ConditionalOnProperty(ZookeeperDiscoveryConfig.SERVICE_DISCOVERY_ZK_HOST)
public class ZookeeperDiscoveryConfig implements BeanFactoryAware {

    public static final String SERVICE_DISCOVERY_ZK_HOST = "serviceDiscovery.zkHost";

    private Environment env;

    @Bean(initMethod = "start", destroyMethod = "close")
    public CuratorFramework curator() {
        return CuratorFrameworkFactory.newClient(env.getProperty(SERVICE_DISCOVERY_ZK_HOST), new ExponentialBackoffRetry(1000, 3));
    }

    @Bean(initMethod = "start", destroyMethod = "close")
    public ServiceDiscovery discovery() {
        return ServiceDiscoveryBuilder.builder(Void.class)
                .client(curator())
                .basePath("services")
                .build();
    }

    @Bean(destroyMethod = "close")
    public ZookeeperFetcher fetchStrategy() {
        return new ZookeeperFetcher(discovery());
    }

    @Bean
    public ZookeeperProxyGenerator clientProxyGenerator() {
        return new ZookeeperProxyGenerator(new ClientProxyDecorator());
    }

    @Bean
    public ZookeeperRegistry registerStrategy() {
        return new ZookeeperRegistry(discovery());
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        env = beanFactory.getBean(Environment.class);
    }

}
