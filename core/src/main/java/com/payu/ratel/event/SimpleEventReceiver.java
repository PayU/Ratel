package com.payu.ratel.event;

import com.payu.ratel.Publish;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

@Publish
public class SimpleEventReceiver implements EventReceiver {

    private final EventListener eventListener;

    @Autowired
    public SimpleEventReceiver(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void receiveEvent(Serializable event) {
        eventListener.listen(event);
    }
}
