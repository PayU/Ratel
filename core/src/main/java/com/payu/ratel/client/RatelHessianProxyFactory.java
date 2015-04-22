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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.URL;

import com.caucho.hessian.client.HessianProxyFactory;
import com.caucho.hessian.io.HessianRemoteObject;
import com.payu.ratel.context.RatelHessianProxy;

/**
 * A custom implementation of {@link HessianProxyFactory} that creates instances
 * of {@link RatelHessianProxy} as remote hessian proxies.
 */
public class RatelHessianProxyFactory extends HessianProxyFactory {

    // TODO - remove PMD suppress
    @SuppressWarnings("PMD.AvoidThrowingNullPointerException")
    public Object create(Class<?> api, URL url, ClassLoader loader) {
        if (api == null) {
            throw new NullPointerException("api must not be null for HessianProxyFactory.create()");
        }
        InvocationHandler handler = null;

        handler = new RatelHessianProxy(url, this, api);

        return Proxy.newProxyInstance(loader, new Class[]{api, HessianRemoteObject.class}, handler);
    }
}
