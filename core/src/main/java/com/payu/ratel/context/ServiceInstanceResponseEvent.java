package com.payu.ratel.context;

/**
 * An event that is risen when a particular instance of a service returns a
 * result (or throws exception) after handling a request.
 */
public class ServiceInstanceResponseEvent extends ServiceResponseEvent {

    public ServiceInstanceResponseEvent(ProcessContext processContext, long nanoTime, ServiceCallInput input,
            ServiceCallResult output) {
        super(processContext, nanoTime, input, output);
    }

    @Override
    public String toString() {
        return "ServiceResponseEvent [processContext=" + getProcessContext() + ", nanoTime=" + getNanoTime()
                + ", input=" + getInput()
                + ", output=" + getOutput() + "]";
    }
}
