package com.payu.ratel.context;

import java.util.UUID;
import java.util.function.Supplier;

public class ProcessContext {

	private static final ThreadLocal<ProcessContext> instance = ThreadLocal.withInitial(new Supplier<ProcessContext>() {

		@Override
		public ProcessContext get() {
			return new ProcessContext();
		}
		
	});
	

	private String processIdentifier;


	public static final String RATEL_HEADER_PROCESS_ID = "ratel-process-id";

	public static ProcessContext getInstance() {
		return instance.get();
	}

	public String getProcessIdentifier() {
		return processIdentifier;
	}

	public String getOrCreateProcessIdentifier() {
		if (this.processIdentifier == null) {
			generateProcessIdentifier();
		}
		return processIdentifier;
	}
	
	public void setProcessIdentifier(String processId) {
		this.processIdentifier = processId;
	}
	
	public void generateProcessIdentifier() {
		if (getProcessIdentifier() != null) {
			throw new IllegalStateException("Cannot generate new process identifier when present is not cleared");
		}
		setProcessIdentifier(UUID.randomUUID().toString());
	}

	public void clearProcessIdentifier() {
		setProcessIdentifier(null);
	}
	

}
