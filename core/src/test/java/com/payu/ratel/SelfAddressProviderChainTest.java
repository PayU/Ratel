package com.payu.ratel;

import static com.payu.ratel.HostAndPortAssert.assertThat;
import static com.payu.ratel.HostAndPortAssert.then;
import static com.payu.ratel.config.beans.RatelPropertySelfAddressProvider.RATEL_BIND_ADDRESS;

import org.junit.Test;
import org.springframework.mock.env.MockEnvironment;

import com.google.common.net.HostAndPort;
import com.payu.ratel.config.ServiceDiscoveryConfig;
import com.payu.ratel.config.beans.JbossPropertySelfAddressProvider;
import com.payu.ratel.config.beans.SelfAddressProvider;
import com.payu.ratel.config.beans.SelfAddressProviderChain;

public class SelfAddressProviderChainTest {
    @Test
    public void shouldProvideLocalhostAsDefault() {
        //given
        SelfAddressProviderChain selfAddressProviderChain = new ServiceDiscoveryConfig().selfAddressProviderChain(
                new MockEnvironment()
        );

        //when
        HostAndPort selfHostAndPort = selfAddressProviderChain.getHostAndPort();

        assertThat(selfHostAndPort).hasHost("localhost").hasPort(80);
    }

    @Test
    public void shouldTakeSelfAddressFromDedicatedProperty() {
        //given
        SelfAddressProvider selfAddressProviderChain = new ServiceDiscoveryConfig().selfAddressProviderChain(
                new MockEnvironment()
                        .withProperty(JbossPropertySelfAddressProvider.JBOSS_BIND_ADDRESS, "jboss.example.com")
                        .withProperty(JbossPropertySelfAddressProvider.JBOSS_BIND_PORT, "8080")
                        .withProperty(RATEL_BIND_ADDRESS, "example.com:123")
        );

        //when
        HostAndPort selfHostAndPort = selfAddressProviderChain.getHostAndPort();

        then(selfHostAndPort).hasHost("example.com").hasPort(123);
    }

    @Test
    public void shouldConsiderJbossBindAddressAsSelfAddressIfNoDedicatedPropertyIsGiven() {
        //given
        SelfAddressProvider selfAddressProviderChain = new ServiceDiscoveryConfig().selfAddressProviderChain(
                new MockEnvironment()
                        .withProperty(JbossPropertySelfAddressProvider.JBOSS_BIND_ADDRESS, "jboss.example.com")
                        .withProperty(JbossPropertySelfAddressProvider.JBOSS_BIND_PORT, "8080")
        );

        //when
        HostAndPort selfHostAndPort = selfAddressProviderChain.getHostAndPort();

        then(selfHostAndPort).hasHost("jboss.example.com").hasPort(8080);
    }
}
