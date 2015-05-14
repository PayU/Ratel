package com.payu.ratel.tests.service.timeout;

public interface TimeoutService {

    String helloAfter1s() throws InterruptedException;

    String helloAfter5s() throws InterruptedException;

}
