package com.payu.ratel.event;

import java.io.Serializable;

public interface EventListener {

    void registerSubscriber(Object listener);

    void listen(Serializable event);

}
