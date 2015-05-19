package com.payu.ratel.context;

public abstract class ServiceEvent {

    private final ProcessContext processContext;
    private final long nanoTime;

    public ServiceEvent(ProcessContext processContext, long nanoTime) {
        super();
        this.processContext = processContext.clone();
        this.nanoTime = nanoTime;
    }

    public ProcessContext getProcessContext() {
        return processContext;
    }

    public long getNanoTime() {
        return nanoTime;
    }

    @Override
    public abstract String toString();

}
