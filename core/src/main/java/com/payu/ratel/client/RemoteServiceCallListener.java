package com.payu.ratel.client;

import com.payu.ratel.context.ServiceCallEvent;
import com.payu.ratel.context.ServiceResponseEvent;

/**
 * Listener invoked when Ratel client invokes a remote service and receives
 * the response.
 */
public interface RemoteServiceCallListener {

    void remoteServiceCalled(ServiceCallEvent event);

    void remoteServiceResponded(ServiceResponseEvent event);

}
