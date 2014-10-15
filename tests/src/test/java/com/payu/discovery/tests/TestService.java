package com.payu.discovery.tests;

public interface TestService {

    int incrementCounter();

    int cached(String arg);

    void throwsException() throws MyException;
}
