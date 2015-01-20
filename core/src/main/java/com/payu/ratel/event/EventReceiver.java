package com.payu.ratel.event;

import java.io.Serializable;

public interface EventReceiver {

    void receiveEvent(Serializable event);

}
