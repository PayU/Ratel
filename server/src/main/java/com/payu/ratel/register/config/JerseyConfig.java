/*
 * Copyright 2015 PayU
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.payu.ratel.register.config;

import java.util.logging.Logger;

import javax.json.stream.JsonGenerator;
import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import com.payu.ratel.server.InMemoryDiscoveryServer;

@ApplicationPath("discovery")
public class JerseyConfig extends ResourceConfig {

    private static final Logger LOGGER = Logger.getLogger("Discovery service");

    public JerseyConfig() {
        register(InMemoryDiscoveryServer.class);
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, false);
        register(new LoggingFilter(LOGGER, true));
        property(JsonGenerator.PRETTY_PRINTING, true);
        register(JacksonFeature.class);
    }



}
