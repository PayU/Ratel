package com.payu.ratel.tests.service;

import com.payu.ratel.context.RemoteServiceCallEvent;
import com.payu.ratel.context.RemoteServiceResponseEvent;
import com.payu.ratel.context.ServiceCallListener;
import com.payu.ratel.context.ServiceEvent;
import com.payu.ratel.context.ServiceInstanceCallEvent;
import com.payu.ratel.context.ServiceInstanceResponseEvent;
import com.payu.ratel.context.ServiceResponseEvent;

public class TestServiceCallListener implements ServiceCallListener {

    private RemoteServiceCallEvent remoteServiceCallEvent;
    private RemoteServiceResponseEvent remoteServiceResponseEvent;
    private ServiceEvent serviceCallEvent;
    private ServiceResponseEvent serviceResponseEvent;

    @Override
    public void remoteServiceCalled(RemoteServiceCallEvent event) {
        this.remoteServiceCallEvent = event;
    }

    @Override
    public void remoteServiceResponded(RemoteServiceResponseEvent event) {
        remoteServiceResponseEvent = event;
    }

    @Override
    public void serviceInstanceCalled(ServiceInstanceCallEvent event) {
        this.serviceCallEvent = event;
    }

    @Override
    public void serviceInstanceResponded(ServiceInstanceResponseEvent event) {
        this.serviceResponseEvent = event;
    }

    public void clear() {
        this.remoteServiceCallEvent = null;
        this.remoteServiceResponseEvent = null;
        this.serviceCallEvent = null;
        this.serviceResponseEvent = null;
    }

    public RemoteServiceCallEvent getRemoteServiceCallEvent() {
        return remoteServiceCallEvent;
    }

    public RemoteServiceResponseEvent getRemoteServiceResponseEvent() {
        return remoteServiceResponseEvent;
    }

    public ServiceEvent getServiceCallEvent() {
        return serviceCallEvent;
    }

    public ServiceResponseEvent getServiceResponseEvent() {
        return serviceResponseEvent;
    }

}
