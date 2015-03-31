package com.payu.ratel.context;
import com.caucho.hessian.client.HessianConnection;
import com.caucho.hessian.client.HessianProxy;
import com.caucho.hessian.client.HessianProxyFactory;

import java.net.URL;

public class RatelHessianProxy extends HessianProxy {
    private static final long serialVersionUID = 1L;

    public RatelHessianProxy(URL url, HessianProxyFactory factory, Class type) {
        super(url, factory, type);
    }

    public RatelHessianProxy(URL url, HessianProxyFactory factory) {
        super(url, factory);
    }


    protected void addRequestHeaders(HessianConnection connection) {
        super.addRequestHeaders(connection);
        connection.addHeader(ProcessContext.RATEL_HEADER_PROCESS_ID, ProcessContext.getInstance().getOrCreateProcessIdentifier().toString());
    }
}