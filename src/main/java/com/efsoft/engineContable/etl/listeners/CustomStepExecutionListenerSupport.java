package com.efsoft.engineContable.etl.listeners;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CustomStepExecutionListenerSupport extends StepExecutionListenerSupport{

	private static final String START_SENTENCE = "Execution job step [{}]";
	private static final String END_SENTENCE = "End job step [{}] with status [{}]";

	private static final Logger appLogger = LoggerFactory.getLogger(CustomStepExecutionListenerSupport.class);
	private List<StepExecution> stepsExecution = new ArrayList<>();

	@Autowired
	@Qualifier("jdbcTemplateDataSource")
	protected JdbcTemplate jdbcTemplateDataSource;


	@Override
	public void beforeStep(StepExecution stepExecution) {

		appLogger.info(START_SENTENCE, stepExecution.getStepName());

		//Rename de los steps
		String HEAP_TABLE_NAME = "";
		switch (stepExecution.getStepName()) {
			case "":
				HEAP_TABLE_NAME = stepExecution.getStepName().replace("Step", "");
				break;
		}

		if (!HEAP_TABLE_NAME.isEmpty())
			jdbcTemplateDataSource.execute(String.format("truncate table %s", HEAP_TABLE_NAME));
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution){
		appLogger.info(END_SENTENCE, stepExecution.getStepName(), stepExecution.getStatus());
		return stepExecution.getExitStatus();
	}

	public List<StepExecution> getStepsExecution() {
		return stepsExecution;
	}

}
