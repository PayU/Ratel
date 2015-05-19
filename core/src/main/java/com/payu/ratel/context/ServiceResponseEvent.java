package com.payu.ratel.context;

public class ServiceResponseEvent extends ServiceEvent {

    private final ServiceCallInput input;
    private final ServiceCallResult output;

    public ServiceResponseEvent(ProcessContext processContext, long nanoTime, ServiceCallInput input,
            ServiceCallResult output) {
        super(processContext, nanoTime);
        this.input = input;
        this.output = output;
    }

    public ServiceCallInput getInput() {
        return input;
    }

    public ServiceCallResult getOutput() {
        return output;
    }

    @Override
    public String toString() {
        return "ServiceResponseEvent [processContext=" + getProcessContext() + ", nanoTime=" + getNanoTime()
                + ", input=" + input
                + ", output=" + output + "]";
    }

}
