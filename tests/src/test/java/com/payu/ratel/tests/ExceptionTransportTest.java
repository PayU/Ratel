package com.payu.ratel.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.payu.ratel.Discover;
import com.payu.ratel.tests.service.MyCheckedException;
import com.payu.ratel.tests.service.TestService;
import com.payu.ratel.tests.service.TestServiceConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@RatelTest(registerServices=TestServiceConfiguration.class)
public class ExceptionTransportTest {



    @Discover
    private TestService testService;


    @Test(expected=MyCheckedException.class)
    public void shouldThrowCheckedExceptionFromRemoteService() throws Exception {

      //when
      testService.alwaysThrowsCheckedException();

      //then
      //nothing
    }
    
    @Test(expected=RuntimeException.class)
    public void shouldThrowRuntimeExceptionFromRemoteService() throws Exception {

      //when
    	testService.alwaysThrowsRuntimeException();
    	
    	//then
    	//nothing
    }

}
