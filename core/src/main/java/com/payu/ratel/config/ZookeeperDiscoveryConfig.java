package com.payu.ratel.config;

import com.payu.ratel.client.ClientProxyDecorator;
import com.payu.ratel.client.zookeeper.ZookeeperFetcher;
import com.payu.ratel.client.zookeeper.ZookeeperProxyGenerator;
import com.payu.ratel.register.zookeeper.ZookeeperRegistry;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import static com.payu.ratel.config.RatelContextInitializer.SERVICE_DISCOVERY_ZK_HOST;

@Configuration
@Profile(SERVICE_DISCOVERY_ZK_HOST)
public class ZookeeperDiscoveryConfig implements BeanFactoryAware {

    private Environment env;

    //TODO refactor property
    @Bean(initMethod = "start", destroyMethod = "close")
    public CuratorFramework curator() {
        System.setProperty("zookeeper.sasl.client", "false");
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
