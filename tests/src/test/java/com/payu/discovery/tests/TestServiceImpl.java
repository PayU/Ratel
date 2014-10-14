package com.payu.discovery.tests;

import com.payu.discovery.Publish;

@Publish
public class TestServiceImpl implements TestService {

    @Override
    public int testMethod() {
        return 100;
    }
}
