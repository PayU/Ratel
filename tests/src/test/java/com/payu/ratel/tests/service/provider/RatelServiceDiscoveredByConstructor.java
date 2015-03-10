/*
 * Copyright 2015 PayU
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
