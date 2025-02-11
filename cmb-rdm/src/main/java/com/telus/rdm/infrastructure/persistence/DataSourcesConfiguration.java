/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.rdm.infrastructure.persistence;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * @author x113300
 *
 */
@Configuration
@PropertySource("classpath:datasources.properties")
public class DataSourcesConfiguration {

	@Inject private Environment environment;
	
	@Bean(destroyMethod = "close")
	public DataSource knowbilityCoreDataSource() {
		return basicDataSource("datasources/knowbility-core/url", "datasources/knowbility-core/username", "datasources/knowbility-core/password");
	}

	@Bean(destroyMethod = "close")
	public DataSource knowbilityReferenceDataSource() {
		return basicDataSource("datasources/knowbility-reference/url", "datasources/knowbility-reference/username", "datasources/knowbility-reference/password");
	}
		
	private DataSource basicDataSource(String url, String user, String password) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		dataSource.setUrl(environment.getProperty(url));
		dataSource.setUsername(environment.getProperty(user));
		dataSource.setPassword(environment.getProperty(password));
		return dataSource;
	}
}
