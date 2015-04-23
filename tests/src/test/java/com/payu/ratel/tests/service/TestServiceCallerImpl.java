package com.payu.ratel.tests.service;

import com.payu.ratel.Discover;
import com.payu.ratel.Publish;

@Publish
public class TestServiceCallerImpl implements TestServiceCallerService {
	
	@Discover
	private TestService testService;

	@Override
	public String callRemoteTestService() {
		return testService.hello();
	}

}
