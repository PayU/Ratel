package com.payu.discovery.config;

import com.payu.discovery.client.ClientProxyDecorator;
import com.payu.discovery.client.inmemory.DiscoveryClient;
import com.payu.discovery.client.inmemory.RatelServerFetcher;
import com.payu.discovery.client.inmemory.RatelServerProxyGenerator;
import com.payu.discovery.proxy.monitoring.ServiceDiscoveryHealth;
import com.payu.discovery.register.inmemory.RatelServerRegistry;
import com.payu.discovery.register.inmemory.RemoteRestDiscoveryServer;
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
    public RatelServerFetcher fetchStrategy() {
        return new RatelServerFetcher(discoveryClient());
    }

    @Bean
    public RatelServerProxyGenerator clientProxyGenerator() {
        return new RatelServerProxyGenerator(new ClientProxyDecorator());
    }

    @Bean
    public RatelServerRegistry registerStrategy() {
        return new RatelServerRegistry(discoveryServer(), taskScheduler());
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        env = beanFactory.getBean(Environment.class);
    }
}
