package com.payu.ratel.tests.service;

import com.payu.ratel.client.ServiceCallListner;
import com.payu.ratel.context.ServiceCallEvent;
import com.payu.ratel.context.ServiceResponseEvent;

public class TestServiceCallListener implements ServiceCallListner {

    @Override
    public void remoteServiceCalled(ServiceCallEvent event) {
        System.err.println(event);
    }

    @Override
    public void remoteServiceResponded(ServiceResponseEvent event) {
        System.err.println(event);
    }

    @Override
    public void serviceInstanceInvoked(ServiceCallEvent event) {
        System.err.println(event);
    }

    @Override
    public void serviceInstanceResponded(ServiceResponseEvent event) {
        System.err.println(event);
    }

}
