/*
 * Copyright 2015 PayU
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.payu.ratel.tests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.payu.ratel.Discover;
import com.payu.ratel.tests.service.ProxableService;
import com.payu.ratel.tests.service.ProxableServiceConfiguration;
import com.payu.ratel.tests.service.TestService;
import com.payu.ratel.tests.service.TestServiceConfiguration;
import com.payu.ratel.tests.service.provider.ProviderConfiguration;
import com.payu.ratel.tests.service.provider.RatelServiceDiscoveredByConstructor;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {TestRatelConfiguration.class, ProviderConfiguration.class})
@RatelTest(registerServices = {ProxableServiceConfiguration.class, TestServiceConfiguration.class})
public class ServiceDiscoverTest {

    @Autowired
    private RatelServiceDiscoveredByConstructor ratelServiceDiscoveredByConstructor;

    @Discover
    private TestService testService;

    @Discover
    private ProxableService proxiedService;

    @Test
    public void shouldDiscoverServiceByField() throws InterruptedException {

        // when
        final int result = testService.incrementCounter();

        // then
        assertThat(result).isEqualTo(1);
    }

    @Test
    public void shouldDiscoverServiceByConstructor() throws InterruptedException {

        // given
        TestService testService1 = ratelServiceDiscoveredByConstructor.getTestService1();

        // when
        final int result = testService1.incrementCounter();

        // then
        assertThat(result).isEqualTo(1);
        assertThat(ratelServiceDiscoveredByConstructor.getEnvironment1()).isNotNull();
        assertThat(ratelServiceDiscoveredByConstructor.getEnvironment2()).isNotNull();
    }

    @Test
    public void shouldDiscoverServiceWithProxy() throws InterruptedException {

        //when
        int result = proxiedService.doInTransaction();

        //then
        assertThat(result).isEqualTo(4);

    }

}
