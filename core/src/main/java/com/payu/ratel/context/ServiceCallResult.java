package com.payu.ratel.context;

/**
 * Event with payload of remote service call, already serialized
 * to java form.
 */
public final class ServiceCallResult {

    private Exception exception;
    private Object result;
    private boolean successful;

    private ServiceCallResult() {
    }

    public static ServiceCallResult success(Object result) {
        ServiceCallResult res = new ServiceCallResult();
        res.result = result;
        res.successful = true;
        return res;
    }

    public static ServiceCallResult exception(Exception exception) {
        ServiceCallResult res = new ServiceCallResult();
        res.exception = exception;
        res.successful = false;
        return res;
    }

    public Exception getException() {
        return exception;
    }

    public Object getResult() {
        return result;
    }

    /**
     * True iff service responded with a return value, false if it threw an
     * exception.
     * In the former case property result is set to a valid value, otherwise
     * proprety exception is set.
     * There is <b>NO</b> distinction between business exception intentionally
     * thrown by the service
     * and some infrastructure exceptions e.g. network timeouts.
     *
     * @return if the service responded with proper return value;
     */
    public boolean isSuccessful() {
        return successful;
    }

    @Override
    public String toString() {
        if (successful) {
            return "ServiceCallResult [result=" + result + "]";
        } else {
            return "ServiceCallResult [exception=" + exception + "]";
        }
    }

}
