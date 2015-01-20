package com.payu.ratel.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class RatelContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RatelContextInitializer.class);

    public static final String SERVICE_DISCOVERY_ENABLED = "serviceDiscovery.enabled";
    public static final String SERVICE_DISCOVERY_ADDRESS = "serviceDiscovery.address";
    public static final String SERVICE_DISCOVERY_ZK_HOST = "serviceDiscovery.zkHost";


    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        final ConfigurableEnvironment environment = applicationContext.getEnvironment();

        if ("false".equals(environment.getProperty(SERVICE_DISCOVERY_ENABLED))) {
            LOGGER.info("Ratel is disabled");
            return;
        }

        environment.addActiveProfile(SERVICE_DISCOVERY_ENABLED);
        LOGGER.info("Ratel is enabled");

        tryToActivateProfile(environment, SERVICE_DISCOVERY_ADDRESS);
        tryToActivateProfile(environment, SERVICE_DISCOVERY_ZK_HOST);
    }

    private void tryToActivateProfile(ConfigurableEnvironment environment, String profileName) {
        if (environment.containsProperty(profileName)) {
            environment.addActiveProfile(profileName);
        }
    }
}
