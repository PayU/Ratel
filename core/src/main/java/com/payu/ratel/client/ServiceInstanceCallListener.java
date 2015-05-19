package com.payu.ratel.client;

import com.payu.ratel.context.ServiceEvent;
import com.payu.ratel.context.ServiceResponseEvent;

/**
 * Listener invoked when Ratel invokes an implementation of a service on the
 * instance.
 */
public interface ServiceInstanceCallListener {

    void serviceInstanceInvoked(ServiceEvent event);

    void serviceInstanceResponded(ServiceResponseEvent event);

}
