package com.payu.ratel.config;

public class TimeoutConfig {

    private final int readTimeout;
    private final int connectTimeout;

    public TimeoutConfig(int readTimeout, int connectTimeout) {
        this.readTimeout = readTimeout;
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public static TimeoutConfig fromTimeout(Timeout timeoutAnnotation) {
        return new TimeoutConfig(timeoutAnnotation.readTimeout(), timeoutAnnotation.connectTimeout());
    }
}
