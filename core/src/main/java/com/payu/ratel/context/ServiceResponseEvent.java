package com.payu.ratel.context;

/**
 * An event that is risen when a service returns a result (or exception) after
 * handling an event.
 * Particular subclasses represent more concrete situations.
 */
public abstract class ServiceResponseEvent extends ServiceEvent {

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
    public abstract String toString();

}
