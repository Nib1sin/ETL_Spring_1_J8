package com.efsoft.engineContable.etl.processors;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.efsoft.engineContable.etl.reject.Interceptor;

public abstract class AbstractProcessor<I, O> implements ItemProcessor<I, O> {

	//Clase Custom "Interceptor" para gestionar los posibles errores
	protected static final Logger rejectLogger = LoggerFactory.getLogger(Interceptor.class);
	protected final Calendar calendar = Calendar.getInstance();
	protected final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

}
