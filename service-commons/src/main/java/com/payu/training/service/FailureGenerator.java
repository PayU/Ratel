package com.payu.training.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class FailureGenerator {

	private static final String EXCEPTION = "exception";

	private static final String TIMEOUT = "timeout";

	private int counter;

	private boolean enabled = true;

	@Autowired
	private Environment env;
	
	private Random random = new Random();

	public void forceFailureIfNeeded() {
		if (shouldFailOn(TIMEOUT)) {
			doTimeout();
		}
		if (shouldFailOn(EXCEPTION)) {
			doThrowException();
		}
	}

	private void doThrowException() {
		throw new NiespodziewanyException();
	}

	private void doTimeout() {
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
		}
	}

	private boolean shouldFailOn(String timeout) {
		int exceptionPeriod = env.getProperty("forced." + timeout + ".period", 
				Integer.class, 0);
		double exceptionProbability = env.getProperty("forced." + timeout + ".probability",
				Double.class, 1.0);
		boolean shouldFail = enabled && exceptionPeriod > 0 && ((++counter) % exceptionPeriod != 0) && random.nextDouble() <= exceptionProbability;
		return shouldFail;
	}

	public void reset() {
		counter = 0;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void enable() {
		setEnabled(true);

	}

	public void disable() {
		setEnabled(false);

	}

}

class NiespodziewanyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
