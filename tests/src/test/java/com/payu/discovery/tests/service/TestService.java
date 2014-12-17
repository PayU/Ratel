package com.payu.discovery.tests.service;

public interface TestService {

    String hello();

    int incrementCounter();

    int cached(String arg);

    void throwsException() throws MyException;
}
