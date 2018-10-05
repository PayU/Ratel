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
package com.payu.ratel.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.ImmutableList;
import com.payu.ratel.Discover;
import com.payu.ratel.RetryPolicy;
import com.payu.ratel.tests.service.MyCheckedException;
import com.payu.ratel.tests.service.TestService;
import com.payu.ratel.tests.service.TestServiceConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@RatelTest(registerServices = TestServiceConfiguration.class)
public class RetryPolicyWithConfigurationTest {


    @Discover
    @RetryPolicy(exception = {MyCheckedException.class, RuntimeException.class, IllegalArgumentException.class},
            waitingTime = 1000, retryCount = 4)
    private TestService testService;

    @Test
    public void shouldRetryServiceCallAfterExceptionExactly4Times() throws MyCheckedException {

        //when
        testService.countableThrowsException(4);

        //then
        //nothing
    }

    @Test(expected = MyCheckedException.class)
    public void shouldThrowExceptionAfterRetryServiceCallBecauseExpectException4TimesButCallServiceThrows5Times()
            throws MyCheckedException {

        //when
        testService.countableThrowsException(5);

        //then
        //nothing
    }

    @Test
    public void shouldRetryServiceCallAfter3DifferentExceptionsThrown() throws Exception {

        //when
        testService.throwsExceptionsInOrder(ImmutableList.of(
                new RuntimeException(), new IllegalArgumentException(), new MyCheckedException()));

        //then
        //nothing
    }

}
