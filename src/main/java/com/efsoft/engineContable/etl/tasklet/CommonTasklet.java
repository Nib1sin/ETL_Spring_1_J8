package com.efsoft.engineContable.etl.tasklet;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.efsoft.engineContable.etl.config.AppConfig;

public abstract class CommonTasklet implements Tasklet{

	private static final Logger appLogger = LoggerFactory.getLogger(CommonTasklet.class);

	@Autowired
	AppConfig appConfig;

	@Autowired
	@Qualifier("jdbcTemplateDataSource")
	protected JdbcTemplate jdbcTemplateDataSource;

	protected String schemaDb;
	protected int maxLineErrors;



	@PostConstruct
	private void init() {
		schemaDb = appConfig.getSchemaDb();
		maxLineErrors = AppConfig.MAX_LINE_ERRORS;
	}


	protected void runDbStatisticDb(String schemaName, String tableName, boolean forceStats) {
		if (appConfig.isForceStatistics() || forceStats) {
			appLogger.info("runDbStatistic LAWEB4 [" + schemaName + "." + tableName + "] ... ");
			Integer cnt = jdbcTemplateDataSource.update(
					"begin DBMS_STATS.GATHER_TABLE_STATS(OWNNAME => '" + schemaName + "', TABNAME => '" + tableName + "', CASCADE => TRUE, estimate_percent => 100, METHOD_OPT => 'FOR ALL COLUMNS size 1', no_invalidate => false); end;"
			);
			appLogger.info("runDbStatistic Db - OK (returnValue : " + cnt + ")");
		}
	}



}
