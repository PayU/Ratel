package com.payu.ratel.client.standalone;

import static com.payu.ratel.config.beans.RegistryBeanProviderFactory.SERVICE_DISCOVERY_ADDRESS;
import static com.payu.ratel.config.beans.RegistryBeanProviderFactory.SERVICE_DISCOVERY_ZK_HOST;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

import com.payu.ratel.client.RatelClientProducer;
import com.payu.ratel.config.beans.InMemoryRegistryBeanProviderFactory;
import com.payu.ratel.config.beans.RegistryBeanProviderFactory;
import com.payu.ratel.config.beans.RegistryStrategiesProvider;
import com.payu.ratel.config.beans.ZookeeperRegistryBeanProvider;

public class RatelStandaloneFactory implements BeanFactoryAware {

    private RatelClientProducer clientProducer;

    public static RatelStandaloneFactory fromZookeeperServer(String zookeeperAddress) {
        RatelStandaloneFactory result = new RatelStandaloneFactory();

        ConfigurableListableBeanFactory beanFactory = new BeanFactoryBuilder("zookeeperAddr").//
                withProperty(SERVICE_DISCOVERY_ZK_HOST, zookeeperAddress).//
                build();
        ZookeeperRegistryBeanProvider zookeeperStrategy = new ZookeeperRegistryBeanProvider(beanFactory);

        zookeeperStrategy.afterPropertiesSet();

        result.clientProducer = new RatelClientProducer(zookeeperStrategy.getFetchStrategy(),
                zookeeperStrategy.getClientProxyGenerator());
        return result;
    }

    public static RatelStandaloneFactory fromRatelServer(String ratelServerAddr) {
        RatelStandaloneFactory result = new RatelStandaloneFactory();
        ConfigurableListableBeanFactory beanFactory = new BeanFactoryBuilder("ratelAddr").//
                withProperty(SERVICE_DISCOVERY_ADDRESS, ratelServerAddr).//
                build();
        InMemoryRegistryBeanProviderFactory ratelStrategy = new InMemoryRegistryBeanProviderFactory(beanFactory);

        ConfigurableEnvironment env = new StandardEnvironment();
        env.getPropertySources().addFirst(
                new MapPropertySource("ratelCustomConfig", Collections.singletonMap(
                        SERVICE_DISCOVERY_ADDRESS, ((Object) ratelServerAddr))));
        ratelStrategy.afterPropertiesSet();

        result.clientProducer = new RatelClientProducer(ratelStrategy.getFetchStrategy(),
                ratelStrategy.getClientProxyGenerator());
        return result;
    }

    public static RatelStandaloneFactory fromZookeeperServer() {
        String zookeeperHost = System.getProperty(SERVICE_DISCOVERY_ZK_HOST);
        return fromZookeeperServer(zookeeperHost);
    }

    public static RatelStandaloneFactory fromRatelServer() {
        String ratelServer = System.getProperty(SERVICE_DISCOVERY_ADDRESS);
        return fromRatelServer(ratelServer);
    }

    public static RatelStandaloneFactory fromAnyConfiguration() {
        if (System.getProperty(SERVICE_DISCOVERY_ADDRESS) != null) {
            return fromRatelServer();
        }
        if (System.getProperty(SERVICE_DISCOVERY_ZK_HOST) != null) {
            return fromZookeeperServer();
        }
        throw new IllegalStateException("Can't resolve zookeeper address nor ratel server address.");
    }

    public <T> T getServiceProxy(Class<T> serviceContractClass) {
        return clientProducer.produceServiceProxy(serviceContractClass, false, null);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
//        this.beanFactory = beanFactory;
        //DOZRO: remove this cast from here
        RegistryStrategiesProvider create = new RegistryBeanProviderFactory().create((ConfigurableListableBeanFactory) beanFactory);
        this.clientProducer = new RatelClientProducer(create.getFetchStrategy(), create.getClientProxyGenerator());
    }
}

class BeanFactoryBuilder {
    private final  String name;
    private final Map<String, Object> environmentProperties = new HashMap<>();

    public BeanFactoryBuilder(String sourcePropertyName) {
        super();
        this.name = sourcePropertyName;
    }

    BeanFactoryBuilder withProperty(String name, Object value) {
        environmentProperties.put(name, value);
        return this;
    }

    public ConfigurableListableBeanFactory build() {
        ConfigurableListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        ConfigurableEnvironment env = new StandardEnvironment();
        MapPropertySource propertySource = new MapPropertySource(this.name, environmentProperties);
        env.getPropertySources().addFirst(propertySource);
        beanFactory.registerSingleton("environment", env);
        return beanFactory;
    }

}
