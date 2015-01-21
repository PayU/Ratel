package com.payu.ratel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.payu.ratel.config.beans.RatelContextApplier;
import com.payu.ratel.config.beans.RegistryBeanProviderFactory;
import com.payu.ratel.config.beans.ServiceRegisterPostProcessorFactory;

@Configuration
public class ServiceDiscoveryConfig {

    @Bean
    public RatelContextApplier ratelContextApplier() {
        return new RatelContextApplier(new RegistryBeanProviderFactory(), new ServiceRegisterPostProcessorFactory());
    }

}
