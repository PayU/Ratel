package com.payu.ratel.config.beans;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.payu.ratel.client.ClientProxyDecorator;
import com.payu.ratel.client.ClientProxyGenerator;
import com.payu.ratel.client.FetchStrategy;
import com.payu.ratel.client.inmemory.DiscoveryClient;
import com.payu.ratel.client.inmemory.RatelServerFetcher;
import com.payu.ratel.client.inmemory.RatelServerProxyGenerator;
import com.payu.ratel.proxy.monitoring.ServiceDiscoveryHealth;
import com.payu.ratel.register.RegisterStrategy;
import com.payu.ratel.register.inmemory.RatelServerRegistry;
import com.payu.ratel.register.inmemory.RemoteRestDiscoveryServer;

public class InMemoryRegistryBeanProviderFactory implements RegistryBeanProvider, InitializingBean {

    private static final String DEFAULT_DISCOVERY_URL = "http://localhost:8090/server/discovery";

    private final ConfigurableListableBeanFactory beanFactory;
    private RatelServerRegistry ratelServerRegistry;
    private RatelServerProxyGenerator ratelServerProxyGenerator;
    private RatelServerFetcher ratelServerFetcher;

    public InMemoryRegistryBeanProviderFactory(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final Environment environment = beanFactory.getBean(Environment.class);

        final String inMemoryServerAddress = environment.getProperty(
                RegistryBeanProviderFactory.SERVICE_DISCOVERY_ADDRESS, DEFAULT_DISCOVERY_URL);

        final DiscoveryClient discoveryClient = new DiscoveryClient(inMemoryServerAddress);

        final ServiceDiscoveryHealth serviceDiscoveryHealth = new ServiceDiscoveryHealth(discoveryClient);
        beanFactory.registerSingleton(serviceDiscoveryHealth.getClass().getName(), serviceDiscoveryHealth);

        TaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        final String schedulerBeanName = taskScheduler.getClass().getName();
        beanFactory.registerSingleton(schedulerBeanName, taskScheduler);
        beanFactory.initializeBean(taskScheduler, schedulerBeanName);

        ratelServerFetcher = new RatelServerFetcher(discoveryClient);

        ratelServerProxyGenerator = new RatelServerProxyGenerator(new ClientProxyDecorator());

        ratelServerRegistry = new RatelServerRegistry(new RemoteRestDiscoveryServer(inMemoryServerAddress), taskScheduler);
    }


    @Override
    public RegisterStrategy getRegisterStrategy() {
        return ratelServerRegistry;
    }

    @Override
    public FetchStrategy getFetchStrategy() {
        return ratelServerFetcher;
    }

    @Override
    public ClientProxyGenerator getClientProxyGenerator() {
        return ratelServerProxyGenerator;
    }
}
