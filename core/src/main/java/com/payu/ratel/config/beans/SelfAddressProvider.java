package com.payu.ratel.config.beans;

import com.google.common.net.HostAndPort;

/**
 * When service is registered it is crucial to be aware of address it is available at.
 * Ratel already knows the context but still needs to be provided with a host:port pair.
 * Ratel will try to guess application address based on most common configurations
 * such as jboss.bind.address.
 * The priority of sources that the address is taken from is defined in
 * {@link com.payu.ratel.config.ServiceDiscoveryConfig#selfAddressProviders(org.springframework.core.env.Environment)}.
 */
public interface SelfAddressProvider {

    /**
     * @return true if this provider can provide with a correct self address
     */
    boolean canProvide();

    /**
     * @return external address visible for other services in the network
     */
    HostAndPort getHostAndPort();

}
