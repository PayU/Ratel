package com.payu.ratel.tests.service.tracing;

import com.payu.ratel.Publish;
import com.payu.ratel.context.ProcessContext;

@Publish
public class ProcessIdTargetServiceImpl implements ProcessIdTargetService {

	
	private String processId;

	@Override
	public void storeProcessId() {
		this.processId = ProcessContext.getInstance().getProcessIdentifier();

	}

	@Override
	public String getProcessId() {
		return processId;
	}

}
