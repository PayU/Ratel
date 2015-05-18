package com.payu.ratel.context;

public class ServiceResponseEvent {

    private final ProcessContext processContext;
    private final long nanoTime;

    private final ServiceCallInput input;
    private final ServiceCallResult output;

    public ServiceResponseEvent(ProcessContext processContext, long nanoTime, ServiceCallInput input,
            ServiceCallResult output) {
        super();
        this.processContext = processContext;
        this.nanoTime = nanoTime;
        this.input = input;
        this.output = output;
    }

    public ServiceCallInput getInput() {
        return input;
    }

    public ProcessContext getProcessContext() {
        return processContext;
    }

    public ServiceCallResult getOutput() {
        return output;
    }

    public long getNanoTime() {
        return nanoTime;
    }

    @Override
    public String toString() {
        return "ServiceResponseEvent [processContext=" + processContext + ", nanoTime=" + nanoTime + ", input=" + input
                + ", output=" + output + "]";
    }

}
