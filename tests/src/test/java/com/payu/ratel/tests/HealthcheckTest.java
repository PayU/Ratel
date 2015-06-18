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

import com.payu.ratel.Discover;
import com.payu.ratel.config.beans.RegistryStrategiesProvider;
import com.payu.ratel.tests.service.TestService;
import com.payu.ratel.tests.service.TestServiceConfiguration;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@RatelTest(registerServices = TestServiceConfiguration.class)
public class HealthcheckTest {

    @Discover
    private TestService testService;

    @Autowired
    private RegistryStrategiesProvider strategiesProvider;

    @Test
    public void shouldReportServiceAviability() throws Exception {

        //when
        String serviceAddress = strategiesProvider.getFetchStrategy().fetchServiceAddress(TestService.class.getCanonicalName());

        HttpClient client = new DefaultHttpClient();
        HttpUriRequest request = new HttpHead(serviceAddress);

        HttpResponse response = client.execute(request);

        //then

        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpServletResponse.SC_OK);
    }

}
