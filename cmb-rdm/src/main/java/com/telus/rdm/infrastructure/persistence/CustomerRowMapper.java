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

import com.telus.rdm.domain.shared.Customer;

/**
 * @author x113300
 *
 */
public class CustomerRowMapper implements RowMapper<Customer> {

	@Override
	public Customer mapRow(ResultSet rs, int idx) throws SQLException {
		Customer customer = new Customer();
		
		customer.setCustomerId(rs.getInt("customer_id"));
		customer.setEmailAddress(rs.getString("email_address"));
		
		return customer;
	}
}
