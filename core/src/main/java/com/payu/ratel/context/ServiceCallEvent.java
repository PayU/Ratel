package com.payu.ratel.context;

public class ServiceCallEvent extends ServiceEvent {

    private final ServiceCallInput input;

    public ServiceCallEvent(ProcessContext processContext, long nanoTime, ServiceCallInput input) {
        super(processContext, nanoTime);
        this.input = input;
    }

    public ServiceCallInput getInput() {
        return input;
    }

    @Override
    public String toString() {
        return "ServiceCallEvent [processContext=" + getProcessContext() + ", nanoTime=" + getNanoTime() + ", input=" + input
                + "]";
    }

}
