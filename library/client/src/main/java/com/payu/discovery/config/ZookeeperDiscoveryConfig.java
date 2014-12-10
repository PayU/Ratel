package com.payu.discovery.config;

import com.payu.discovery.proxy.ClientProducer;
import com.payu.discovery.proxy.ZookeeperClientProducer;
import com.payu.discovery.register.config.RegisterStrategy;
import com.payu.discovery.register.config.ZookeeperRegistry;
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

    @Bean( initMethod = "start", destroyMethod = "close" )
    public CuratorFramework curator() {
        return CuratorFrameworkFactory.newClient(env.getProperty(SERVICE_DISCOVERY_ZK_HOST), new ExponentialBackoffRetry(1000, 3));
    }

    @Bean( initMethod = "start", destroyMethod = "close" )
    public ServiceDiscovery discovery() {
        return ServiceDiscoveryBuilder.builder(Void.class)
                .client(curator())
                .basePath("services")
                .build();
    }

    @Bean
    public ClientProducer clientProducer() {
        return new ZookeeperClientProducer(discovery());
    }

    @Bean
    public RegisterStrategy registerStrategy() {
        return new ZookeeperRegistry(discovery());
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        env = beanFactory.getBean(Environment.class);
    }

}
