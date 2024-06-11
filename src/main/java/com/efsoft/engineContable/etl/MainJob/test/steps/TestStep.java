package com.efsoft.engineContable.etl.MainJob.test.steps;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.efsoft.engineContable.etl.MainJob.test.tasklets.TestTasklet;
import com.efsoft.engineContable.etl.steps.StepContainer;

@Component
public class TestStep extends StepContainer{

	@Autowired
	TestTasklet testTasklet;

	@Override
	@PostConstruct
	public void buildStep() {
		step = stepBuilderFactory
				.get("testTasklet")
				.tasklet(testTasklet)
				.listener(customStepExecutionListenerSupport)
				.build();
	}

}
