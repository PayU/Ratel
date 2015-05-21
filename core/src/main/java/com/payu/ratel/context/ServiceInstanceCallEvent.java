package com.payu.ratel.context;

/**
 * An event risen when a particular service instance is called on server side.
 */
public class ServiceInstanceCallEvent extends ServiceCallEvent {

    public ServiceInstanceCallEvent(ProcessContext processContext, long nanoTime, ServiceCallInput input) {
        super(processContext, nanoTime, input);
    }

    @Override
    public String toString() {
        return "ServiceCallEvent [processContext=" + getProcessContext() + ", nanoTime=" + getNanoTime() + ", input="
                + getInput()
                + "]";
    }

}
