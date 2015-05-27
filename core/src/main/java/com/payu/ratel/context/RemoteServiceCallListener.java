package com.payu.ratel.context;


/**
 * Listener notified when Ratel client invokes a remote service and receives
 * the response.
 */
public interface RemoteServiceCallListener {

    void remoteServiceCalled(RemoteServiceCallEvent event);

    void remoteServiceResponded(RemoteServiceResponseEvent event);

}
