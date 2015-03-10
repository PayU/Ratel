package com.payu.ratel.tests.service;

public interface TestService {

    String hello();

    int incrementCounter();

    int cached(String arg);

    void throwsException() throws MyException;
}
