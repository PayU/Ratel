package com.payu.ratel.tests;

import static com.payu.ratel.config.beans.RegistryBeanProviderFactory.SERVICE_DISCOVERY_ADDRESS;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import com.payu.ratel.config.EnableServiceDiscovery;

@IntegrationTest({
    "server.port:8069",
    SERVICE_DISCOVERY_ADDRESS + ":http://localhost:8069/server/discovery"})
@Retention(RetentionPolicy.RUNTIME)
@WebAppConfiguration
@EnableServiceDiscovery
@SpringApplicationConfiguration(classes={TestRatelConfiguration.class})
public @interface RatelIntegrationTest {

}
