package com.payu.ratel.context;

import java.net.URL;
import java.util.Arrays;

public class ServiceCallEvent {

    private final String methodName;
    private final Object[] args;
    private final URL url;
    private final Class apiType;
    private final ProcessContext processContext;
    private final long nanoTime;

    public ServiceCallEvent(String methodName, Object[] args, URL url, @SuppressWarnings("rawtypes") Class apiType,
            ProcessContext processContext, long nanoTime) {
        super();
        this.methodName = methodName;
        this.args = copyOf(args);
        this.url = url;
        this.apiType = apiType;
        this.processContext = processContext;
        this.nanoTime = nanoTime;
    }

    private Object[] copyOf(Object[] args) {
        if (args == null) {
            return null;
        } else {
            return Arrays.copyOf(args, args.length);
        }
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getArgs() {
        return copyOf(args);
    }

    public URL getUrl() {
        return url;
    }

    public Class getApiType() {
        return apiType;
    }

    public ProcessContext getProcessContext() {
        return processContext;
    }

    public long getNanoTime() {
        return nanoTime;
    }

    @Override
    public String toString() {
        return "ServiceCallEvent [methodName=" + methodName + ", args=" + Arrays.toString(args) + ", url=" + url
                + ", apiType=" + apiType + ", processContext=" + processContext + ", nanoTime=" + nanoTime + "]";
    }

}
