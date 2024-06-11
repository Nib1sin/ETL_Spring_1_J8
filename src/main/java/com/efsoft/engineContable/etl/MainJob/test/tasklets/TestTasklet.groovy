package com.efsoft.engineContable.etl.MainJob.test.tasklets

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Component

import com.efsoft.engineContable.etl.tasklet.CommonTasklet

@Component
class TestTasklet extends CommonTasklet{

	private static final Logger appLogger = LoggerFactory.getLogger(TestTasklet.class);


	//Definir metodos aqui





	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		try{

			//metodos
			appLogger.info("Test")


		}catch(Exception e){

			throw e
		}

		return RepeatStatus.FINISHED;
	}

}
