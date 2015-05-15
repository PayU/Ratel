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
package com.payu.ratel.client;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.env.Environment;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;

public abstract class AbstractClientProxyGenerator implements ClientProxyGenerator {

    private static final long DEFAULT_CONNECT_READ_TIMEOUT = 30000;

    public static final String PROP_CONNECT_TIMEOUT = "ratel.connectTimeout";
    public static final String PROP_READ_TIMEOUT = "ratel.readTimeout";

    private final BeanFactory beanFactory;
    private final Environment env;

    public AbstractClientProxyGenerator(BeanFactory beanFactory) {
        super();
        this.beanFactory = beanFactory;
        this.env = beanFactory.getBean(Environment.class);
    }

    protected <T> T createServiceClientProxy(Class<T> clazz, String serviceUrl) {
        checkNotNull(clazz, "Given service class cannot be null");
        checkArgument(!isNullOrEmpty(serviceUrl), "Given serviceUrl class cannot be blank");

        HessianProxyFactoryBean proxyFactory = new RatelHessianProxyFactoryBean();
        proxyFactory.setServiceUrl(serviceUrl);
        proxyFactory.setServiceInterface(clazz);

        RatelHessianProxyFactory ratelProxyFactory = new RatelHessianProxyFactory();
        ratelProxyFactory.setConnectTimeout(getConnectTimeout());
        ratelProxyFactory.setReadTimeout(getReadTimeout());
        ratelProxyFactory.setOverloadEnabled(true);
        proxyFactory.setProxyFactory(ratelProxyFactory);
        beanFactory.containsBean("abc");

        proxyFactory.afterPropertiesSet();
        return (T) proxyFactory.getObject();

    }

    private long getReadTimeout() {
        return env.getProperty(PROP_READ_TIMEOUT, Long.class, DEFAULT_CONNECT_READ_TIMEOUT);
    }

    private long getConnectTimeout() {
        return env.getProperty(PROP_CONNECT_TIMEOUT, Long.class, DEFAULT_CONNECT_READ_TIMEOUT);
    }

    @Override
    public final <T> T generate(Class<T> serviceClazz, String serviceAddress) {
        T bareServiceProxy = createServiceClientProxy(serviceClazz, serviceAddress);
        return decorate(bareServiceProxy, serviceClazz);
    }

    protected <T> T decorate(T bareServiceProxy, Class<T> serviceClass) {
        return bareServiceProxy;
    }

}
