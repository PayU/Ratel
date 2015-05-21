package com.payu.ratel.context;

/**
 * Event raised on client side when Ratel client invokes a remote service.
 * You can determine input parameters and remote endpoint based on the event.
 */
public class RemoteServiceCallEvent extends ServiceCallEvent {

    private final String url;

    public RemoteServiceCallEvent(ProcessContext processContext, long nanoTime,
            String url, ServiceCallInput input) {
        super(processContext, nanoTime, input);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "RemoteServiceCallEvent [processContext=" + getProcessContext()
                + ", nanoTime=" + getNanoTime()
                + ", url=" + url
                + ", input=" + getInput() + "]";
    }

}
