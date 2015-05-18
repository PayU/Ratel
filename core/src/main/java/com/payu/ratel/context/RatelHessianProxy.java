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
package com.payu.ratel.context;
import com.caucho.hessian.client.HessianConnection;
import com.caucho.hessian.client.HessianProxy;
import com.caucho.hessian.client.HessianProxyFactory;
import com.payu.ratel.context.filter.TracingFilter;

import java.net.URL;

/**
 * A custom implementation of {@link HessianProxy} that passes a {@link ProcessContext} through http headers.
 *
 */
public class RatelHessianProxy extends HessianProxy {
    private static final long serialVersionUID = 1L;

    public RatelHessianProxy(URL url, HessianProxyFactory factory, Class type) {
        super(url, factory, type);
    }

    public RatelHessianProxy(URL url, HessianProxyFactory factory) {
        super(url, factory);
    }


    /**
     * Custom headers, specific to {@link ProcessContext} are added in this method.
     */
    protected void addRequestHeaders(HessianConnection connection) {
        super.addRequestHeaders(connection);
        String processId = ProcessContext.getInstance().getProcessIdentifier();
        if (processId != null) {
          connection.addHeader(TracingFilter.RATEL_HEADER_PROCESS_ID, processId);
        }
    }
}
