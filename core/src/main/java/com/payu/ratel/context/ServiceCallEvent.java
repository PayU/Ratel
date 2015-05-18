package com.payu.ratel.context;

public class ServiceCallEvent {

    private final ProcessContext processContext;
    private final long nanoTime;
    private final ServiceCallInput input;

    public ServiceCallEvent(ProcessContext processContext, long nanoTime, ServiceCallInput input) {
        super();
        this.processContext = processContext;
        this.nanoTime = nanoTime;
        this.input = input;
    }

    public ProcessContext getProcessContext() {
        return processContext;
    }

    public long getNanoTime() {
        return nanoTime;
    }

    public ServiceCallInput getInput() {
        return input;
    }

    @Override
    public String toString() {
        return "ServiceCallEvent [processContext=" + processContext + ", nanoTime=" + nanoTime + ", input=" + input
                + "]";
    }

}
