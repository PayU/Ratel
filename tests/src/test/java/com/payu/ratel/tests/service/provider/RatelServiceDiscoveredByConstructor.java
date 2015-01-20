package com.payu.ratel.tests.service.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.payu.ratel.Discover;
import com.payu.ratel.tests.service.TestService;

@Component
public class RatelServiceDiscoveredByConstructor {

    private final TestService testService;
    private final Environment environment1;
    private final Environment environment2;

    @Autowired
    public RatelServiceDiscoveredByConstructor(Environment environment1, @Discover TestService testService
            , Environment environment2) {
        this.testService = testService;
        this.environment1 = environment1;
        this.environment2 = environment2;
    }

    public TestService getTestService1() {
        return testService;
    }

    public Environment getEnvironment1() {
        return environment1;
    }

    public Environment getEnvironment2() {
        return environment2;
    }
}
