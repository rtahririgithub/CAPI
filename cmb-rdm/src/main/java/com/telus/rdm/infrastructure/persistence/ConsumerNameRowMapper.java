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

import com.telus.rdm.domain.shared.ConsumerName;

/**
 * @author x113300
 *
 */
public class ConsumerNameRowMapper implements RowMapper<ConsumerName> {
	
	@Override
	public ConsumerName mapRow(ResultSet rs, int idx) throws SQLException {
		ConsumerName result = new ConsumerName();
		
		result.setFirstName(rs.getString("first_name"));
		result.setMiddleName(rs.getString("middle_initial"));
		result.setLastName(rs.getString("last_business_name"));
		result.setTitle(rs.getString("name_title"));
		
		return result;
	}
}
