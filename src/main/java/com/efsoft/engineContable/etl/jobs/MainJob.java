package com.efsoft.engineContable.etl.jobs;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class MainJob {

	@Autowired private JobBuilderFactory jobs;


	@Bean(name = "Main")
	public Job job() throws Exception {

		//
		Flow testFlow = new FlowBuilder<SimpleFlow>("sTATIFlow").start(sTATIStep.getStep())
				.on(ExitStatus.COMPLETED.getExitCode()).to(statiSygesStep.getStep()).from(sTATIStep.getStep())
				.on(ExitStatus.FAILED.getExitCode()).end().from(statiSygesStep.getStep()).on("*").end().end();



		return jobs.get("Main")
				.start(testFlow)
				//.next(NuevoFlow)
				.end()
				.build();

	}

}
