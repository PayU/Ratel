package com.payu.ratel;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import com.google.common.net.HostAndPort;

public class HostAndPortAssert extends AbstractAssert<HostAndPortAssert, HostAndPort> {

    protected HostAndPortAssert(HostAndPort actual, Class<?> selfType) {
        super(actual, selfType);
    }

    public static HostAndPortAssert assertThat(HostAndPort actual) {
        return new HostAndPortAssert(actual, HostAndPortAssert.class);
    }

    public static HostAndPortAssert then(HostAndPort actual) {
        return assertThat(actual);
    }

    public HostAndPortAssert hasHost(String host) {
        Assertions.assertThat(actual.getHostText()).isEqualTo(host);
        return this;
    }

    public HostAndPortAssert hasPort(int port) {
        Assertions.assertThat(actual.getPort()).isEqualTo(port);
        return this;
    }

}
