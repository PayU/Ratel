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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.payu.ratel.tests.service.TestServiceConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@RatelTest(registerServices=TestServiceConfiguration.class)
public class HeartBeatServiceRegisterTest {

    private ConfigurableApplicationContext remoteContext;
    private ConfigurableApplicationContext secondRemoteContext;

    @Autowired  
    private RatelTestContext ratelTestContext;

    @Test
    public void shouldRegisterService() throws InterruptedException {
        //given
        ratelTestContext.waitForServicesRegistration(1);

        //when
        ratelTestContext.startService(TestServiceConfiguration.class);
        
        //then
        ratelTestContext.waitForServicesRegistration(2);
    }

}
