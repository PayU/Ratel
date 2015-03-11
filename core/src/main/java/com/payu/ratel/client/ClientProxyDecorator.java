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
package com.payu.ratel.client;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

import java.lang.reflect.Proxy;

import org.springframework.remoting.caucho.HessianProxyFactoryBean;

import com.payu.ratel.proxy.monitoring.MonitoringInvocationHandler;

public class ClientProxyDecorator {

    public static final int CONNECT_READ_TIMEOUT = 30000;

    public Object decorateWithMonitoring(final Object object, final Class clazz) {
        return Proxy
                .newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class[]{clazz}, new MonitoringInvocationHandler(object));
    }

    public <T> T createServiceClientProxy(Class<T> clazz, String serviceUrl) {
        checkNotNull(clazz, "Given service class cannot be null");
        checkArgument(!isNullOrEmpty(serviceUrl), "Given serviceUrl class cannot be blank");

        HessianProxyFactoryBean proxyFactory = new RatelHessianProxyFactoryBean();
        proxyFactory.setServiceUrl(serviceUrl);
        proxyFactory.setServiceInterface(clazz);
        proxyFactory.afterPropertiesSet();
        proxyFactory.setConnectTimeout(CONNECT_READ_TIMEOUT);
        proxyFactory.setReadTimeout(CONNECT_READ_TIMEOUT);
        return (T) proxyFactory.getObject();

    }

}
