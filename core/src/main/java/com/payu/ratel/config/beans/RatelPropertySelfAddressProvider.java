package com.payu.ratel.config.beans;

import static com.payu.ratel.config.beans.RegistryBeanProviderFactory.SERVICE_DISCOVERY;

import org.springframework.core.env.Environment;

import com.google.common.net.HostAndPort;

public class RatelPropertySelfAddressProvider implements SelfAddressProvider {

    public static final String RATEL_BIND_ADDRESS = SERVICE_DISCOVERY + ".selfAddress";
    private final Environment environment;

    public RatelPropertySelfAddressProvider(Environment environment) {
        this.environment = environment;
    }

    @Override
    public boolean canProvide() {
        return environment.containsProperty(RATEL_BIND_ADDRESS);
    }

    @Override
    public HostAndPort getHostAndPort() {
        final String hostAndPort = environment.getProperty(RATEL_BIND_ADDRESS);
        return HostAndPort.fromString(hostAndPort);
    }
}
