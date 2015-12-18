package com.payu.ratel.tests.service.timeout;

import org.springframework.stereotype.Service;

import com.payu.ratel.Publish;

@Service
@Publish
public class AnnotatedTimeoutServiceImpl implements AnnotatedTimeoutService {

    public String helloAfter1s() throws InterruptedException {
        waitSeconds(1);
        return "hello";
    }

    public String helloAfter5s() throws InterruptedException {
        waitSeconds(5);
        return "hello";
    }

    private void waitSeconds(int i) throws InterruptedException {
        Thread.sleep(i * 1000);
    }
}
