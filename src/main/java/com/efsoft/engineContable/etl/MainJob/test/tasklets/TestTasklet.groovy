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

	private void testQuery(String tableName){

		def query = """
		select * from `users`
		"""
		//appLogger.info("query ---- " + query);
		List<Map<String,Object>> items = jdbcTemplateDataSource.queryForList(query);
		appLogger.info("Test ---- " + items);
	}



	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		try{

			//metodos
			testQuery()

		}catch(Exception e){

			throw e
		}

		return RepeatStatus.FINISHED;
	}

}
