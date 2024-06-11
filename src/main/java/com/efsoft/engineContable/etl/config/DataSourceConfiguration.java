package com.efsoft.engineContable.etl.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataSourceConfiguration {

	@Autowired
    AppConfig appConfig;

    @Bean(name = "jdbcTemplateDataSource")
    public JdbcTemplate jdbcTemplateDataSource() {
    	return new JdbcTemplate(DataSourceBuilder
			.create()
			.type(BasicDataSource.class)
			.url(appConfig.getJdbcTemplateDataSourceUrl())
			.username(appConfig.getJdbcTemplateDataSourceUsername())
			.password(appConfig.getJdbcTemplateDataSourcePassword())
			.driverClassName(appConfig.getJdbcTemplateDataSourceDriverClassName())
			.build());
    }

}
