package com.payu.ratel.tests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.payu.ratel.Discover;
import com.payu.ratel.tests.service.TestServiceCallerConfiguration;
import com.payu.ratel.tests.service.TestServiceCallerService;
import com.payu.ratel.tests.service.TestServiceConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@RatelIntegrationTest
public class TracingMechanismTest {

	@Autowired
	private TestContext testContext;
    
    @Discover
    private TestServiceCallerService testServiceCaller;

    @Before
    public void before() throws InterruptedException {
    	testContext.startService(TestServiceConfiguration.class);
    	testContext.startService(TestServiceCallerConfiguration.class);
    	
        testContext.waitForServicesRegistration();
    }

    @After
    public void close() {
        testContext.close();
    }
    
    @Test
    public void callerShouldCallTestService() throws Exception {
        //when
        String testCallerResult = testServiceCaller.callRemoteTestService();

        //then
        assertThat(testCallerResult).isEqualTo("success");
    }
}
