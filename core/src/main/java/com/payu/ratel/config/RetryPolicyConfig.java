package com.payu.ratel.config;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Preconditions;
import com.payu.ratel.RetryPolicy;

public class RetryPolicyConfig {

    private static final long WAITING_TIME = 5000;
    private static final int RETRY_COUNT = 5;

    private final long waitingTime;
    private final int retryCount;
    private final List<Class<? extends Throwable>> retryOnExceptions;

    public RetryPolicyConfig(Class<? extends Throwable>... retryOnExceptions) {
        this(WAITING_TIME, RETRY_COUNT, retryOnExceptions);
    }

    public RetryPolicyConfig(long waitingTime, int retryCount, Class<? extends Throwable>... retryOnExceptions) {
        Preconditions.checkNotNull(retryOnExceptions, "Please provide retryOnExceptions");
        this.waitingTime = waitingTime;
        this.retryCount = retryCount;
        this.retryOnExceptions = Arrays.asList(retryOnExceptions);
    }

    public long getRetryCount() {
        return retryCount;
    }

    public long getWaitingTime() {
        return waitingTime;
    }

    public List<Class<? extends Throwable>> getRetryOnExceptions() {
        return retryOnExceptions;
    }

    public static RetryPolicyConfig fromRetryPolicy(RetryPolicy retryPolicyAnnotation) {
        return new RetryPolicyConfig(retryPolicyAnnotation.waitingTime(), retryPolicyAnnotation.retryCount(),
                retryPolicyAnnotation.exception());
    }
}
