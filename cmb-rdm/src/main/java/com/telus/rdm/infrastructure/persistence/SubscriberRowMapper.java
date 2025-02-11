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

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.telus.rdm.domain.subscriber.Subscriber;
import com.telus.rdm.domain.subscriber.SubscriberFactory;

/**
 * @author x113300
 *
 */
public class SubscriberRowMapper implements RowMapper<Subscriber> {

	private SubscriberFactory subscriberFactory;

	public SubscriberRowMapper(SubscriberFactory subscriberFactory) {
		this.subscriberFactory = subscriberFactory;
	}

	@Override
	public Subscriber mapRow(ResultSet rs, int idx) throws SQLException {
		Subscriber subscriber = subscriberFactory.createSubscriber();

		subscriber.setSubscriberId(rs.getString("subscriber_no"));
		subscriber.setAccountId(rs.getInt("customer_ban"));
		
		return subscriber;
	}
	
}
