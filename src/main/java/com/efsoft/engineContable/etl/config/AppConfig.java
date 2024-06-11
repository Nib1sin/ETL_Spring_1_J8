package com.efsoft.engineContable.etl.config;

import java.io.FileInputStream;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.w3c.dom.Document;

import com.efsoft.engineContable.etl.io.IoManager;
import com.efsoft.engineContable.etl.io.JavaIoManager;

@Configuration
@PropertySource({ "file:${app.home}/application.properties" })
public class AppConfig {

	@Autowired
	private Environment env;

	@Value("#{systemProperties['app.home']}")
	private String appHome;

	private JobParameters jobParameters;
	public static final int MAX_LINE_ERRORS = 100;
	private static final Logger appLogger = LoggerFactory.getLogger(AppConfig.class);

	@Autowired
	private JavaIoManager javaIoManager;


	//Gestion de Archivos
	private IoManager ioManager;
	private String ioInputPath;
	private String ioOutputPath;

	//Conexion del db
	private String jdbcTemplateDataSourceUrl;
	private String jdbcTemplateDataSourceUsername;
	private String jdbcTemplateDataSourcePassword;
	private String jdbcTemplateDataSourceDriverClassName;

	//Xml Configuracion del db
	private String inputConfigurationXml;
	private Document inputConfigurationXmlDoc;

	//Schema del Db
	private String schemaDb;
	private boolean forceStatistics = false;


	//Archivos in output
	private String ioOutputIncassiFilename;




	@PostConstruct
	private void postConstruct() throws Exception {

		appLogger.info("Init de la configuracion al db");

		//
		if(env.getProperty("sql.force.statistics") != null)
			setForceStatistics(Boolean.getBoolean(env.getProperty("sql.force.statistics")));

		//
		setSchemaDb(env.getProperty("sql.schema.db"));

		//Init de la Configuracion de la conexion al db
		setJdbcTemplateDataSourceUrl(env.getProperty("jdbcTemplate.datasource.laweb4.url"));
		setJdbcTemplateDataSourceUsername(env.getProperty("jdbcTemplate.datasource.laweb4.username"));
		setJdbcTemplateDataSourcePassword(env.getProperty("jdbcTemplate.datasource.laweb4.password"));
		setJdbcTemplateDataSourceDriverClassName(env.getProperty("jdbcTemplate.datasource.laweb4.driver-class-name"));

		//Init del ioManager && Path de input && Path de output
		setIoManager(javaIoManager);
		setIoInputPath(env.getProperty("io.input.path"));
		setIoOutputPath(env.getProperty("io.output.path"));


		//Archivo que se creara
		ioOutputIncassiFilename = env.getProperty("io.output.incassi.filename");


		//Init del inputConfig xml
		setInputConfigurationXmlDoc(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
				new FileInputStream(appHome + "/inputConfiguration.xml")));
		//Configuracion del file xml del db
		setInputConfigurationXml(env.getProperty("query.inputConfiguration.xml"));


	}




	//Setters && Getters

	public IoManager getIoManager() {
		return ioManager;
	}


	public void setIoManager(IoManager ioManager) {
		this.ioManager = ioManager;
	}


	public String getIoInputPath() {
		return ioInputPath;
	}


	public void setIoInputPath(String ioInputPath) {
		this.ioInputPath = ioInputPath;
	}


	public String getIoOutputPath() {
		return ioOutputPath;
	}


	public void setIoOutputPath(String ioOutputPath) {
		this.ioOutputPath = ioOutputPath;
	}


	public String getJdbcTemplateDataSourceUrl() {
		return jdbcTemplateDataSourceUrl;
	}


	public void setJdbcTemplateDataSourceUrl(String jdbcTemplateDataSourceUrl) {
		this.jdbcTemplateDataSourceUrl = jdbcTemplateDataSourceUrl;
	}


	public String getJdbcTemplateDataSourceUsername() {
		return jdbcTemplateDataSourceUsername;
	}


	public void setJdbcTemplateDataSourceUsername(String jdbcTemplateDataSourceUsername) {
		this.jdbcTemplateDataSourceUsername = jdbcTemplateDataSourceUsername;
	}


	public String getJdbcTemplateDataSourcePassword() {
		return jdbcTemplateDataSourcePassword;
	}


	public void setJdbcTemplateDataSourcePassword(String jdbcTemplateDataSourcePassword) {
		this.jdbcTemplateDataSourcePassword = jdbcTemplateDataSourcePassword;
	}


	public String getJdbcTemplateDataSourceDriverClassName() {
		return jdbcTemplateDataSourceDriverClassName;
	}


	public void setJdbcTemplateDataSourceDriverClassName(String jdbcTemplateDataSourceDriverClassName) {
		this.jdbcTemplateDataSourceDriverClassName = jdbcTemplateDataSourceDriverClassName;
	}


	public Document getInputConfigurationXmlDoc() {
		return inputConfigurationXmlDoc;
	}


	public void setInputConfigurationXmlDoc(Document inputConfigurationXmlDoc) {
		this.inputConfigurationXmlDoc = inputConfigurationXmlDoc;
	}


	public String getSchemaDb() {
		return schemaDb;
	}


	public void setSchemaDb(String schemaDb) {
		this.schemaDb = schemaDb;
	}


	public boolean isForceStatistics() {
		return forceStatistics;
	}


	public void setForceStatistics(boolean forceStatistics) {
		this.forceStatistics = forceStatistics;
	}


	public JobParameters getJobParameters() {
		return jobParameters;
	}


	public void setJobParameters(JobParameters jobParameters) {
		this.jobParameters = jobParameters;
	}


	public String getInputConfigurationXml() {
		return inputConfigurationXml;
	}


	public void setInputConfigurationXml(String inputConfigurationXml) {
		this.inputConfigurationXml = inputConfigurationXml;
	}










}
