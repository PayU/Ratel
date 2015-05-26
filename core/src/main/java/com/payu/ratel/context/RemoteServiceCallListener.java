package com.payu.ratel.context;


/**
 * Listener invoked when Ratel client invokes a remote service and receives
 * the response.
 */
public interface RemoteServiceCallListener {

    void remoteServiceCalled(RemoteServiceCallEvent event);

    void remoteServiceResponded(RemoteServiceResponseEvent event);

}
