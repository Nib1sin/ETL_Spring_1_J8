package com.efsoft.engineContable.etl;


import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


@SpringBootApplication
public class Main {

	private static final Logger appLogger = LoggerFactory.getLogger(Main.class);
	public final static String keyParamJob = "job";
	public final static String keyParamStep = "step";


	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	@Qualifier("Main")
	private Job etlMain;



	private JobParameters parseArgs(String[] args) throws Exception {
		final List<String> paramKeys = Arrays.asList(keyParamJob, keyParamStep);
		final String ERR_PARAMETRO_NON_VALIDO = "Parametro [%s] non valid";

		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		for (String item : args) {
			String[] arg = item.split("=");
			if (arg.length != 2)
				throw new Exception(String.format(ERR_PARAMETRO_NON_VALIDO, item));
			if (!paramKeys.contains(arg[0].trim().toLowerCase()))
				throw new Exception(String.format(ERR_PARAMETRO_NON_VALIDO, item));
			jobParametersBuilder.addString(arg[0].trim().toLowerCase(), arg[1].trim().toUpperCase());
		}

		JobParameters result = jobParametersBuilder.toJobParameters();
		if (result.getString(keyParamJob) == null
				|| !Arrays.asList(
						"MAIN"
						).contains(result.getString(keyParamJob)))
			throw new Exception(String.format(ERR_PARAMETRO_NON_VALIDO, keyParamJob));

		return result;
	}


	private void executeJob(Job job, JobParameters jobParameters) {
		appLogger.info("executeJob({},{})", job.getName(), jobParameters.getParameters().toString());
		try {
			jobLauncher.run(job, jobParameters);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			e.printStackTrace();
		}
	}


	private void exec(String[] args) throws Exception {
		try {
			appLogger.info("*** START UP ***");
			appLogger.info(String.format("Parameters commands: %s", Arrays.toString(args)));

			JobParameters jobParameters = parseArgs(args);

			switch (jobParameters.getString(keyParamJob)) {
			case "MAIN":
				executeJob(etlMain, jobParameters);
				break;
			default:
				throw new Exception(String.format("Parameter [%s] non valid", args[0]));
			}

		} catch (Exception e) {
			appLogger.error(e.getMessage());
			throw e;
		}

	}














	public static void main(String[] args) throws Exception {
		appLogger.info("************************************* START JOB *************************************");
		ApplicationContext ac = new AnnotationConfigApplicationContext(Main.class);
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(ac);
		try {
			Main main = ac.getBean(Main.class);
			main.exec(args);
		} finally {
			ctx.close();
		}
		appLogger.info("************************************** END JOB **************************************");
		System.exit(0);
	}

}
