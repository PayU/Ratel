/*
 * Copyright 2015 PayU
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.payu.ratel.client.inmemory;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.payu.ratel.client.AbstractClientProxyGenerator;
import com.payu.ratel.proxy.monitoring.MonitoringInvocationHandler;

/**
 * An implementation of ClientProxyGenerator that builds
 * service client proxies with use of a Ratel server.
 *
 */
public class RatelServerProxyGenerator extends AbstractClientProxyGenerator {

    public RatelServerProxyGenerator(ConfigurableListableBeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override
    protected <T> T decorate(final T object, final Class<T> clazz) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] {clazz},
                new MonitoringInvocationHandler(object));
    }
}
