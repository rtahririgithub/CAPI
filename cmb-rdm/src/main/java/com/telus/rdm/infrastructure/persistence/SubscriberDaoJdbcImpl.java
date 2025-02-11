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

import com.telus.rdm.domain.shared.ConsumerName;
import com.telus.rdm.domain.shared.Customer;
import com.telus.rdm.domain.subscriber.Subscriber;

/**
 * @author x113300
 *
 */
public class SubscriberDaoJdbcImpl extends JdbcDaoSupport implements SubscriberDao {

	private SubscriberRowMapper subscriberRowMapper;

	private CustomerRowMapper customerRowMapper;
	
	private ConsumerNameRowMapper consumerNameRowMapper;
	
	public void setSubscriberRowMapper(SubscriberRowMapper subscriberRowMapper) {
		this.subscriberRowMapper = subscriberRowMapper;
	}
	
	public void setConsumerNameRowMapper(ConsumerNameRowMapper consumerNameRowMapper) {
		this.consumerNameRowMapper = consumerNameRowMapper;
	}
	
	public void setCustomerRowMapper(CustomerRowMapper customerRowMapper) {
		this.customerRowMapper = customerRowMapper;
	}

	@Override
	public Subscriber getSubscriber(String subscriberId) {
		return getJdbcTemplate().queryForObject("select * from subscriber where subscriber_no = ?",  subscriberRowMapper, subscriberId);
	}

	@Override
	public ConsumerName getContactName(String subscriberId) {
		String sql = "select * from address_name_link an, name_data nd where an.subscriber_no = ? and an.name_id = nd.name_id and an.expiration_date is null";
		return getJdbcTemplate().queryForObject(sql, consumerNameRowMapper, subscriberId);
	}
	
	@Override
	public Customer getCustomer(String subscriberId) {
		String sql = "select * from customer c, subscriber s where s.subscriber_no = ? and s.customer_id = c.customer_id";
		return getJdbcTemplate().queryForObject(sql, customerRowMapper, subscriberId);
	}
}
