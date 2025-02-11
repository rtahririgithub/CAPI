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

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.telus.rdm.domain.account.AccountFactory;
import com.telus.rdm.domain.subscriber.SubscriberFactory;

/**
 * @author x113300
 *
 */

@Configuration
public class PersistenceConfiguration {

	@Resource(name = "knowbilityCoreDataSource") 
	private DataSource knowbilityCoreDataSource;
	
	@Inject private AccountFactory accountFactory;
	
	@Inject private SubscriberFactory subscriberFactory;
	
	
	@Bean public AccountDao accountDao() {
		AccountDaoJdbcImpl dao = new AccountDaoJdbcImpl();
		dao.setDataSource(knowbilityCoreDataSource);
		dao.setAccountRowMapper( new AccountRowMapper(accountFactory));
		dao.setCustomerRowMapper( new CustomerRowMapper());
		dao.setConsumerNameRowMapper( new ConsumerNameRowMapper());
		return dao;
	}
	
	@Bean public SubscriberDao subscriberDao() {
		SubscriberDaoJdbcImpl dao = new SubscriberDaoJdbcImpl();
		dao.setDataSource(knowbilityCoreDataSource);
		dao.setSubscriberRowMapper( new SubscriberRowMapper(subscriberFactory));
		dao.setCustomerRowMapper( new CustomerRowMapper());
		dao.setConsumerNameRowMapper( new ConsumerNameRowMapper());
		return dao;
	}
}
