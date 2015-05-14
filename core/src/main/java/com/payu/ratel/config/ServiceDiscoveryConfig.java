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
package com.payu.ratel.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.google.common.collect.Lists;
import com.payu.ratel.config.beans.JbossPropertySelfAddressProvider;
import com.payu.ratel.config.beans.LocalhostSelfAddressProvider;
import com.payu.ratel.config.beans.RatelContextApplier;
import com.payu.ratel.config.beans.RatelPropertySelfAddressProvider;
import com.payu.ratel.config.beans.RegistryBeanProviderFactory;
import com.payu.ratel.config.beans.SelfAddressProvider;
import com.payu.ratel.config.beans.SelfAddressProviderChain;
import com.payu.ratel.config.beans.ServiceRegisterPostProcessorFactory;

@Configuration
public class ServiceDiscoveryConfig {

    @Bean
    public RatelContextApplier ratelContextApplier() {
        return new RatelContextApplier(new RegistryBeanProviderFactory(), new ServiceRegisterPostProcessorFactory());
    }

    List<SelfAddressProvider> selfAddressProviders(Environment environment) {
        return Lists.newArrayList(
                new RatelPropertySelfAddressProvider(environment),
                new JbossPropertySelfAddressProvider(environment),
                new LocalhostSelfAddressProvider()
        );
    }

    @Bean
    public SelfAddressProviderChain selfAddressProviderChain(Environment environment) {
        SelfAddressProviderChain selfAddressProviderChain = new SelfAddressProviderChain(selfAddressProviders(environment));
        selfAddressProviderChain.selectProvider();
        return selfAddressProviderChain;
    }


}
