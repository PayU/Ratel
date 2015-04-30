package com.payu.ratel.config.beans;

import java.util.List;

import javax.annotation.PostConstruct;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.net.HostAndPort;

public class SelfAddressProviderChain implements SelfAddressProvider {

    private final List<SelfAddressProvider> selfAddressProviders;
    private final Predicate<SelfAddressProvider> canProvide = new Predicate<SelfAddressProvider>() {
        @Override public boolean apply(SelfAddressProvider selfAddressProvider) {
            return selfAddressProvider.canProvide();
        }
    };

    private Optional<SelfAddressProvider> selfAddressProvider;

    public SelfAddressProviderChain(List<SelfAddressProvider> selfAddressProviders) {
        this.selfAddressProviders = selfAddressProviders;
    }

    @PostConstruct
    public void selectProvider() {
        selfAddressProvider = Iterables.tryFind(selfAddressProviders, canProvide);

        if (!canProvide()) {
            throw new IllegalStateException("There is no SelfAddressProvider able to provide self address.");
        }
    }

    public boolean canProvide() {
        return selfAddressProvider.isPresent();
    }

    public HostAndPort getHostAndPort() {
        return selfAddressProvider.get().getHostAndPort();
    }
}
