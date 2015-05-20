package com.payu.ratel.client;

import com.payu.ratel.context.ServiceInstanceCallEvent;
import com.payu.ratel.context.ServiceInstanceResponseEvent;

/**
 * Listener invoked when Ratel invokes an implementation of a service on a
 * particular instance of a service.
 */
public interface ServiceInstanceCallListener {

    void serviceInstanceCalled(ServiceInstanceCallEvent event);

    void serviceInstanceResponded(ServiceInstanceResponseEvent event);

}
