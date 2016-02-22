package com.payu.ratel.config;

import com.payu.ratel.RetryPolicy;

public class RetryPolicyConfig {

    private static final long WAITING_TIME = 5000;
    private static final int RETRY_COUNT = 5;

    private final long waitingTime;
    private final int retryCount;
    private final Class<? extends Throwable> retryOnException;

    public RetryPolicyConfig(Class<? extends Throwable> retryOnException, long waitingTime, int retryCount) {
        this.retryOnException = retryOnException;
        this.waitingTime = waitingTime;
        this.retryCount = retryCount;
    }

    public RetryPolicyConfig(Class<? extends Throwable> retryOnException) {
        this.waitingTime = WAITING_TIME;
        this.retryCount = RETRY_COUNT;
        this.retryOnException = retryOnException;
    }

    public long getRetryCount() {
        return retryCount;
    }

    public long getWaitingTime() {
        return waitingTime;
    }

    public Class<? extends Throwable> getRetryOnException() {
        return retryOnException;
    }

    public static RetryPolicyConfig fromRetryPolicy(RetryPolicy retryPolicyAnnotation) {
        return new RetryPolicyConfig(retryPolicyAnnotation.exception(), retryPolicyAnnotation.waitingTime(),
                retryPolicyAnnotation.retryCount());
    }
}
