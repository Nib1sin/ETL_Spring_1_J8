package com.efsoft.engineContable.etl.steps;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.efsoft.engineContable.etl.listeners.CustomProcessorListener;
import com.efsoft.engineContable.etl.listeners.CustomReadListener;
import com.efsoft.engineContable.etl.listeners.CustomStepExecutionListenerSupport;
import com.efsoft.engineContable.etl.listeners.CustomWriteListener;
import com.efsoft.engineContable.etl.utils.Constants;

public abstract class StepContainer {

	@Autowired
	protected StepBuilderFactory stepBuilderFactory;

	@Autowired
	protected CustomStepExecutionListenerSupport customStepExecutionListenerSupport;

	protected Step step;

	public Step getStep() {
		return step;
	}

	public abstract void buildStep();

	public <I, O> void createStep(String name, ItemReader<I> reader, ItemProcessor<I, O> processor,
			ItemWriter<O> writer) {
		step = stepBuilderFactory.get(name).<I, O>chunk(Constants.CHUNK_SIZE)
				.reader(reader)
				.listener(new CustomReadListener<I>())
				.processor(processor)
				.listener(new CustomProcessorListener<I, O>())
				.writer(writer)
				.listener(new CustomWriteListener<O>())
				.faultTolerant().skip(Exception.class)
				.skipLimit(Integer.MAX_VALUE)
				// .listener(new CustomSkipListener<I>())
				.listener(customStepExecutionListenerSupport)
				.build();
	}

}
