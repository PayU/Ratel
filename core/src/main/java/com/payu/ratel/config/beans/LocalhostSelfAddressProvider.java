package com.payu.ratel.config.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.net.HostAndPort;

public class LocalhostSelfAddressProvider implements SelfAddressProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalhostSelfAddressProvider.class);

    public static final String LOCALHOST_80 = "localhost:80";

    @Override
    public boolean canProvide() {
        return true;
    }

    @Override
    public HostAndPort getHostAndPort() {
        LOGGER.warn("No explicit self address given. Assuming localhost:80.");
        return HostAndPort.fromString(LOCALHOST_80);
    }
}
