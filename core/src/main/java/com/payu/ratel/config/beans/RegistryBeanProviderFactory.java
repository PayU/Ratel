package com.payu.ratel.config.beans;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.env.Environment;

public class RegistryBeanProviderFactory {

    public static final String SERVICE_DISCOVERY_ADDRESS = "serviceDiscovery.address";
    public static final String SERVICE_DISCOVERY_ZK_HOST = "serviceDiscovery.zkHost";

    public RegistryBeanProvider create(ConfigurableListableBeanFactory beanFactory) {
        final Environment environment = beanFactory.getBean(Environment.class);

        if (environment.containsProperty(SERVICE_DISCOVERY_ZK_HOST)) {
            return new ZookeeperRegistryBeanProvider(beanFactory);
        } else if (environment.containsProperty(SERVICE_DISCOVERY_ADDRESS)) {
            return new InMemoryRegistryBeanProviderFactory(beanFactory);
        }

        throw new IllegalStateException(String.format("Provide one of props with registry either %s or %s",
                SERVICE_DISCOVERY_ZK_HOST, SERVICE_DISCOVERY_ADDRESS));
    }
}
