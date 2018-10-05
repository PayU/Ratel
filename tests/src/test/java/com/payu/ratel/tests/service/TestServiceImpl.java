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
package com.payu.ratel.tests.service;

import java.util.List;

import com.payu.ratel.Publish;

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
    public void alwaysThrowsCheckedException() throws MyCheckedException {
        throw new MyCheckedException();
    }

    @Override
    public void sometimesThrowsException() throws MyCheckedException {
        incrementCounter();
        if (counter % 3 != 0) {
            throw new MyCheckedException();
        }
    }

    @Override
    public void countableThrowsException(int count) throws MyCheckedException {
        incrementCounter();
        if (counter < count) {
            throw new MyCheckedException();
        }
    }

    @Override
    public void alwaysThrowsRuntimeException() {
        throw new RuntimeException();
    }

    @Override
    public void throwsExceptionsInOrder(List<Exception> exceptions) throws Exception {
        if (counter < exceptions.size()) {
            Exception toBeThrown = exceptions.get(counter);
            incrementCounter();
            throw toBeThrown;
        }
    }

}
