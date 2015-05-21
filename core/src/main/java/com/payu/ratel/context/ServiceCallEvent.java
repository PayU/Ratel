package com.payu.ratel.context;

/**
 * An event that is risen when a service is called.
 * Subclasses represent particular cases of calling it on server and client
 * side.
 */
public abstract class ServiceCallEvent extends ServiceEvent {

    private final ServiceCallInput input;

    public ServiceCallEvent(ProcessContext processContext, long nanoTime, ServiceCallInput input) {
        super(processContext, nanoTime);
        this.input = input;
    }

    /**
     * Then input (method name, parameters, contract class) that was used when
     * calling a service.
     *
     * @return the input sent to the server.
     */
    public ServiceCallInput getInput() {
        return input;
    }

    public abstract String toString();

}
