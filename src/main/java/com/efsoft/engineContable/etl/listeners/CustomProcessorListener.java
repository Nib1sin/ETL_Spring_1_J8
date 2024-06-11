package com.efsoft.engineContable.etl.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;

public class CustomProcessorListener<T, S> implements ItemProcessListener<T, S> {

	private static final Logger appLogger = LoggerFactory.getLogger(CustomProcessorListener.class);

	@Override
	public void beforeProcess(T item) {
		//appLogger.debug("Before process");
	}

	@Override
	public void afterProcess(T item, S result) {
		//appLogger.debug("After process");
	}

	@Override
	public void onProcessError(T item, Exception e) {
		appLogger.error("Error processing the line " + item, e);
	}
}
