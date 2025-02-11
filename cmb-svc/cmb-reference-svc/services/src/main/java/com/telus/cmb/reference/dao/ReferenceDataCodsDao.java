/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.reference.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.telus.eas.utility.info.ClientStateReasonInfo;
import com.telus.eas.utility.info.SubscriptionRoleTypeInfo;

/**
 * @author Pavel Simonovsky
 *
 */
public class ReferenceDataCodsDao extends JdbcDaoSupport {

	public SubscriptionRoleTypeInfo[] retrieveSubscriptionRoleTypes() throws DataAccessException {

		String sql = " select distinct srt.subscription_role_cd, " +
		" srt.description, srt.description_french, srt.load_dt, srt.update_dt, " +
		" srt.user_last_modify, srt.subscription_role_rank " +
		" from subscription_role_type srt order by subscription_role_rank";

		return getJdbcTemplate().query(sql, new RowMapper<SubscriptionRoleTypeInfo>() {
			
			@Override
			public SubscriptionRoleTypeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				SubscriptionRoleTypeInfo info = new SubscriptionRoleTypeInfo();

				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3));
				info.setLoadDate(rs.getDate(4));
				info.setUpdateDate(rs.getDate(5));
				info.setUserLastModify(rs.getString(6));
				info.setRank(rs.getInt(7));

				return info;
			}
		}).toArray( new SubscriptionRoleTypeInfo[0]);
	}
	
	public Collection<ClientStateReasonInfo> retrieveClientStateReasons() throws DataAccessException {
		
		String sql = " select  kb_activity_cd, kb_activity_reason_cd, des, client_state_reason_id "   +
		" from client_state_reason "   +
		" where kb_activity_cd is not null and kb_activity_reason_cd is not null "   +
		" order by kb_activity_cd, kb_activity_reason_cd "  ;
		
		return getJdbcTemplate().query(sql, new RowMapper<ClientStateReasonInfo>() {
			
			@Override
			public ClientStateReasonInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				ClientStateReasonInfo info = new ClientStateReasonInfo();

				info.setCode(rs.getString(1) + "_" + rs.getString(2));
				info.setDescription(rs.getString(3));
				info.setDescriptionFrench(rs.getString(3));
				info.setReasonId(rs.getLong(4));
				
				return info;
			}
		});
	}
}
