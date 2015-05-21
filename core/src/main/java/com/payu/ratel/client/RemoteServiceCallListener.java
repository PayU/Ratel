package com.payu.ratel.client;

import com.payu.ratel.context.RemoteServiceCallEvent;
import com.payu.ratel.context.RemoteServiceResponseEvent;

/**
 * Listener invoked when Ratel client invokes a remote service and receives
 * the response.
 */
public interface RemoteServiceCallListener {

    void remoteServiceCalled(RemoteServiceCallEvent event);

    void remoteServiceResponded(RemoteServiceResponseEvent event);

}
