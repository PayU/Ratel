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

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.env.Environment;

public class RegistryBeanProviderFactory {

    public static final String SERVICE_DISCOVERY = "serviceDiscovery";
    public static final String SERVICE_DISCOVERY_ADDRESS = SERVICE_DISCOVERY + ".ratelServerAddress";
    public static final String SERVICE_DISCOVERY_ZK_HOST = SERVICE_DISCOVERY + ".zkHost";

    public RegistryStrategiesProvider create(ConfigurableListableBeanFactory beanFactory) {
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
