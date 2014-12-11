package com.payu.discovery.tests.service;

import com.payu.discovery.Publish;

@Publish
public class TestServiceImpl implements TestService {

    private int counter = 0;

    @Override
    public String hello() {
        return "success";
    }

    @Override
    public int incrementCounter() {
        return ++counter;
    }

    @Override
    public int cached(String arg) {
        return counter;
    }

    @Override
    public void throwsException() throws MyException {
        incrementCounter();
        if(counter % 3 != 0)
            throw new MyException();
    }
}
