package com.payu.ratel.client;

import com.caucho.hessian.client.HessianProxyFactory;
import com.caucho.hessian.io.HessianRemoteObject;

import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RatelHessianProxyFactory extends HessianProxyFactory {

    private final Map<String, String> requestHeaders = new HashMap();

    public RatelHessianProxyFactory(Map<String, String> requestHeaders) {
        requestHeaders.putAll(requestHeaders);
    }

    public Map getRequestHeaders() {
        return requestHeaders;
    }

    public String putContext(String key, String value) {
        return requestHeaders.put(key, value);
    }

    public Object create(Class api, URL url, ClassLoader loader) {
        if (api == null) {
            throw new NullPointerException("api must not be null for HessianProxyFactory.create()");
        }
        RatelHessianProxy handler = new RatelHessianProxy(url, this, api);
        handler.setRequestHeaders(requestHeaders);
        return Proxy.newProxyInstance(loader, new Class[]{api, HessianRemoteObject.class}, handler);
    }
}