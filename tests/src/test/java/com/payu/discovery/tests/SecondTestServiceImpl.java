package com.payu.discovery.tests;

import com.payu.discovery.Publish;

@Publish
public class SecondTestServiceImpl implements SecondTestService {

    @Override
    public int testMethod() {
        return 200;
    }
}
