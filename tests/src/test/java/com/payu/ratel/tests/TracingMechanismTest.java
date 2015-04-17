package com.payu.ratel.tests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.payu.ratel.Discover;
import com.payu.ratel.tests.service.TestServiceCallerConfiguration;
import com.payu.ratel.tests.service.TestServiceCallerService;
import com.payu.ratel.tests.service.TestServiceConfiguration;

//

@RunWith(SpringJUnit4ClassRunner.class)
@RatelTest(registerServices={TestServiceConfiguration.class, TestServiceCallerConfiguration.class})
@IntegrationTest
public class TracingMechanismTest {

    
    @Discover
    private TestServiceCallerService testServiceCaller;

    @Test
    public void callerShouldCallTestService() throws Exception {
        //when
        String testCallerResult = testServiceCaller.callRemoteTestService();

        //then
        assertThat(testCallerResult).isEqualTo("success");
    }
}
