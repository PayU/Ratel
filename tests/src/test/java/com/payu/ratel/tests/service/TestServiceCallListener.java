package com.payu.ratel.tests.service;

import com.payu.ratel.client.ServiceCallListener;
import com.payu.ratel.context.ServiceCallEvent;
import com.payu.ratel.context.ServiceResponseEvent;

public class TestServiceCallListener implements ServiceCallListener {

    @Override
    public void serviceCalled(ServiceCallEvent event) {
        System.err.println(event);
    }

    @Override
    public void serviceResponded(ServiceResponseEvent event) {
        System.err.println(event);
    }

}
