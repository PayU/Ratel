package com.payu.ratel.client;

import com.payu.ratel.context.RemoteServiceCallListener;

public interface RatelServiceCallPublisher {

    void addRatelServiceCallListener(RemoteServiceCallListener listener);
}
