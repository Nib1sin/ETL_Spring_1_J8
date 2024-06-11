package com.efsoft.engineContable.etl.listeners;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;


public class CustomWriteListener<S> implements ItemWriteListener<S> {

	private static final Logger appLogger = LoggerFactory.getLogger(CustomWriteListener.class);

	@Override
	public void beforeWrite(List<? extends S> items) {
		//appLogger.debug("Before write");
	}

	@Override
	public void afterWrite(List<? extends S> items) {
		//appLogger.debug("After write");
	}

	@Override
	public void onWriteError(Exception exception, List<? extends S> items) {
		appLogger.error("Error writing", exception);
	}

}
