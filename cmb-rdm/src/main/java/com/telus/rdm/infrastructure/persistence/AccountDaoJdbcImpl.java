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

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.telus.rdm.domain.account.Account;
import com.telus.rdm.domain.shared.ConsumerName;
import com.telus.rdm.domain.shared.Customer;

/**
 * @author x113300
 *
 */
public class AccountDaoJdbcImpl extends JdbcDaoSupport implements AccountDao {

	private AccountRowMapper accountRowMapper;
	
	private CustomerRowMapper customerRowMapper;
	
	private ConsumerNameRowMapper consumerNameRowMapper;

	public void setAccountRowMapper(AccountRowMapper accountRowMapper) {
		this.accountRowMapper = accountRowMapper;
	}
	
	public void setCustomerRowMapper(CustomerRowMapper customerRowMapper) {
		this.customerRowMapper = customerRowMapper;
	}
	
	public void setConsumerNameRowMapper(ConsumerNameRowMapper consumerNameRowMapper) {
		this.consumerNameRowMapper = consumerNameRowMapper;
	}
	
	@Override
	public Account getAccount(Integer accountId) {
		String sql = "select * from billing_account where ban = ?";
		return getJdbcTemplate().queryForObject(sql, accountRowMapper, accountId);
	}
	
	@Override
	public Customer getCustomer(Integer accountId) {
		String sql = "select * from customer c, billing_account ba where ba.ban = ? and ba.customer_id = c.customer_id";
		return getJdbcTemplate().queryForObject(sql, customerRowMapper, accountId);
	}
	
	@Override
	public ConsumerName getContactName(Integer accountId) {
		String sql = "select * from address_name_link an, name_data nd where an.ban = ? and an.link_type = 'C' and an.name_id = nd.name_id and an.expiration_date is null";
		return getJdbcTemplate().queryForObject(sql, consumerNameRowMapper, accountId);
	}
	
}
