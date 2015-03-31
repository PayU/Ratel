package com.payu.ratel.tests.service.tracing;

import java.util.UUID;

import com.payu.ratel.context.ProcessContext;

public interface ProcessIdPassingService {

	String getProcessId();

	void passProcessId();

}
