package com.payu.ratel.context;

/**
 * Event raised on client side when Ratel client receives a response from a
 * remote service.
 * You can determine input parameters, and if the response provided a return
 * value or if a service threw
 * an exception.
 */
public class RemoteServiceResponseEvent extends ServiceResponseEvent {

    private final String url;

    public RemoteServiceResponseEvent(ProcessContext processContext, long nanoTime, String url, ServiceCallInput input,
            ServiceCallResult output) {
        super(processContext, nanoTime, input, output);
        this.url = url;
    }

    @Override
    public String toString() {
        return "RemoteServiceResponseEvent [processContext=" + getProcessContext()
                + ", nanoTime=" + getNanoTime()
                + ", url=" + url
                + ", input=" + getInput()
                + ", output=" + getOutput() + "]";
    }

}
