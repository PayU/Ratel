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
          connection.addHeader(TracingFilter.RATEL_HEADER_PROCESS_ID, processId.toString());
        }
    }
}