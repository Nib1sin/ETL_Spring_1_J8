package com.efsoft.engineContable.etl.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;

public class CustomReadListener<T> implements ItemReadListener<T> {

	@SuppressWarnings("unused")
	private static final Logger appLogger = LoggerFactory.getLogger(CustomReadListener.class);

	@Override
	public void beforeRead() {
		//appLogger.debug("Before read");
	}

	@Override
	public void afterRead(T item) {
		//appLogger.debug("After read");

	}

	@Override
	public void onReadError(Exception ex) {
		//appLogger.error("Read error");
	}

}
