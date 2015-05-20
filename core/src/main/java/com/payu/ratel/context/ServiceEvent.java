package com.payu.ratel.context;

/**
 * Abstract class representing any event that is risen when communication with
 * service happens on either server or client side. Subclasses represent more
 * specific events.
 *
 */
public abstract class ServiceEvent {

    private final ProcessContext processContext;
    private final long nanoTime;

    public ServiceEvent(ProcessContext processContext, long nanoTime) {
        super();
        this.processContext = processContext.clone();
        this.nanoTime = nanoTime;
    }

    /**
     * The process context that was active when this event was risen.
     * Please note that ProcessContext is mutable, though this class holds a
     * snapshot copy of it taken at the moment when this event was risen.
     */
    public ProcessContext getProcessContext() {
        return processContext;
    }

    /**
     * System time in nano seconds taken when this event was risen.
     * @return time in nano time
     */
    public long getNanoTime() {
        return nanoTime;
    }

    @Override
    public abstract String toString();

}
