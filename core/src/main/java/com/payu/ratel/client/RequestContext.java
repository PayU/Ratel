package com.payu.ratel.client;

import java.util.HashMap;
import java.util.Map;

public class RequestContext {

    private static final ThreadLocal<RequestContext> instance = new ThreadLocal() {
        @Override
        protected RequestContext initialValue() {
            return new RequestContext();
        }
    };

    private Map<String, String> requestHeaders = new HashMap<String, String>();

    public static RequestContext getInstance() {
        return instance.get();
    }

    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(Map<String, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }
}