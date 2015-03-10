package com.payu.ratel.tests.service;

import com.payu.ratel.Publish;

@Publish
public class SecondTestServiceImpl implements SecondTestService {

    @Override
    public int testMethod() {
        return 200;
    }
}
