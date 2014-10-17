package com.payu.discovery.event;

import java.io.Serializable;

public interface EventReceiver {

    void receiveEvent(Serializable event);

}
