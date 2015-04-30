package com.payu.ratel.config.beans;

import org.springframework.core.env.Environment;

import com.google.common.net.HostAndPort;

public class JbossPropertySelfAddressProvider implements SelfAddressProvider {

    public static final String JBOSS_BIND_ADDRESS = "jboss.bind.address";
    public static final String JBOSS_BIND_PORT = "jboss.bind.port";
    private final Environment environment;

    public JbossPropertySelfAddressProvider(Environment environment) {
        this.environment = environment;
    }

    @Override
    public boolean canProvide() {
        return environment.containsProperty(JBOSS_BIND_ADDRESS);
    }

    @Override
    public HostAndPort getHostAndPort() {
        final String host = environment.getProperty(JBOSS_BIND_ADDRESS);
        final String port = environment.getProperty(JBOSS_BIND_PORT, "8080");
        return HostAndPort.fromParts(host, Integer.parseInt(port));
    }
}
