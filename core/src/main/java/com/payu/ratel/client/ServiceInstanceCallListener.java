package com.payu.ratel.client;

import com.payu.ratel.context.ServiceCallEvent;
import com.payu.ratel.context.ServiceResponseEvent;

/**
 * Listener invoked when Ratel invokes an implementation of a service on the
 * instance.
 */
public interface ServiceInstanceCallListener {

    void serviceInstanceInvoked(ServiceCallEvent event);

    void serviceInstanceResponded(ServiceResponseEvent event);

}
