package com.payu.discovery.server.config;

import com.payu.discovery.server.InMemoryDiscoveryServer;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.moxy.internal.MoxyFilteringFeature;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import javax.json.stream.JsonGenerator;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("discovery")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(InMemoryDiscoveryServer.class);
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        register(LoggingFilter.class);
        property(JsonGenerator.PRETTY_PRINTING, true);
        register(MoxyJsonFeature.class);
        register(MoxyFilteringFeature.class);
    }



}
