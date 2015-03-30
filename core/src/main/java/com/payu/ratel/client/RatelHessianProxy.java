package com.payu.ratel.client;

import com.caucho.hessian.client.HessianConnection;
import com.caucho.hessian.client.HessianProxy;
import com.caucho.hessian.client.HessianProxyFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RatelHessianProxy extends HessianProxy {
    private static final long serialVersionUID = 1L;
    private Map<String, String> requestHeaders = new HashMap();

    public RatelHessianProxy(URL url, HessianProxyFactory factory, Class type) {
        super(url, factory, type);
    }

    public RatelHessianProxy(URL url, HessianProxyFactory factory) {
        super(url, factory);
    }

    void setRequestHeaders(Map<String, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    protected void addRequestHeaders(HessianConnection connection) {
        super.addRequestHeaders(connection);
        for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
            connection.addHeader(header.getKey(), header.getValue());
        }
    }
}