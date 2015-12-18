package com.payu.ratel.tests.service.timeout;

import com.payu.ratel.config.Timeout;

@Timeout(connectTimeout = 1000, readTimeout = 2000)
public interface AnnotatedTimeoutService {

    String helloAfter1s() throws InterruptedException;

    String helloAfter5s() throws InterruptedException;

}
