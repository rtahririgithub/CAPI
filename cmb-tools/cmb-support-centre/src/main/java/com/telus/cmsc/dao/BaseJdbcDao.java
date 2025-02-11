/*
 *  Copyright (c) 2015 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmsc.dao;

import javax.sql.DataSource;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * @author Pavel Simonovsky	
 *
 */
public class BaseJdbcDao extends JdbcDaoSupport {

	protected BaseJdbcDao(DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	protected Integer generateEntityId(String sequenceName) {
		return getJdbcTemplate().queryForObject("select " + sequenceName +  ".nextval from dual", Integer.class);
	}
}
