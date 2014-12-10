package com.payu.discovery.config;

import com.payu.discovery.client.DiscoveryClient;
import com.payu.discovery.proxy.ClientProducer;
import com.payu.discovery.proxy.HessianClientProducer;
import com.payu.discovery.proxy.monitoring.ServiceDiscoveryHealth;
import com.payu.discovery.register.config.RatelServerRegistry;
import com.payu.discovery.register.config.RegisterStrategy;
import com.payu.discovery.server.RemoteRestDiscoveryServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@ConditionalOnProperty(ServerDiscoveryConfig.SERVICE_DISCOVERY_ADDRESS)
@EnableScheduling
public class ServerDiscoveryConfig implements BeanFactoryAware {
    public static final String SERVICE_DISCOVERY_ADDRESS = "serviceDiscovery.address";
    private static final String DEFAULT_DISCOVERY_URL = "http://localhost:8090/server/discovery";

    private Environment env;

    @Bean
    public DiscoveryClient discoveryClient() {
        final String property = env.getProperty(ServerDiscoveryConfig.SERVICE_DISCOVERY_ADDRESS, DEFAULT_DISCOVERY_URL);
        return new DiscoveryClient(property);
    }

    @Bean
    public ServiceDiscoveryHealth serviceDiscoveryHealth(){
        return new ServiceDiscoveryHealth(discoveryClient());
    }

    @Bean
    public RemoteRestDiscoveryServer discoveryServer() {
        return new RemoteRestDiscoveryServer(env.getProperty(SERVICE_DISCOVERY_ADDRESS,
                DEFAULT_DISCOVERY_URL));
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    @Bean
    public ClientProducer clientProducer() {
        return new HessianClientProducer(discoveryClient());
    }

    @Bean
    public RegisterStrategy registerStrategy() {
        return new RatelServerRegistry(discoveryServer(), taskScheduler());
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        env = beanFactory.getBean(Environment.class);
    }
}
