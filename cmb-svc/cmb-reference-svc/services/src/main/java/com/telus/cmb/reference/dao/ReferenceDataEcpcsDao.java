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
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.telus.cmb.reference.utilities.AppConfiguration;
import com.telus.eas.utility.info.PrepaidAdjustmentReasonInfo;
import com.telus.eas.utility.info.PrepaidEventTypeInfo;

/**
 * @author Pavel Simonovsky
 *
 */
public class ReferenceDataEcpcsDao extends JdbcDaoSupport {

	public static final int  REASON_TYPE_ID_PREPAID_ADJUSTMENT =2;
	public static final int  REASON_TYPE_ID_PREPAID_TOP_UP_WAIVE =3;
	public static final int  REASON_TYPE_ID_PREPAID_FEATURE_ADD_WAIVE =4;
	public static final int  REASON_TYPE_ID_PREPAID_DEVICE_DIRECT_FULFILLMENT =8;
	
	private class PrepaidEventTypeRowMapper implements RowMapper<PrepaidEventTypeInfo> {
	
		/* (non-Javadoc)
		 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
		 */
		@Override
		public PrepaidEventTypeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {

			PrepaidEventTypeInfo info = new PrepaidEventTypeInfo();

			info.setCode(Integer.toString(rs.getInt(1)));
			info.setDescription(rs.getString(2));
			info.setDescriptionFrench(rs.getString(3) == null ? rs.getString(2) : rs.getString(3));
			info.setClientVisible((rs.getString(4) != null && rs.getString(4).equals("Y")));

			return info;
		}
	}
	
	public PrepaidEventTypeInfo[] retrievePrepaidEventTypes() throws DataAccessException {
		
		String sql = " select event_type_id, event_type_desc, event_type_desc_fr, client_view_ind " +
		" from event_type order by 1 ";
		
		return getJdbcTemplate().query(sql, new PrepaidEventTypeRowMapper()).toArray( new PrepaidEventTypeInfo[0]);
	}
	
	public PrepaidEventTypeInfo retrievePrepaidEventType(int eventTypeId) throws DataAccessException {
		
		String sql = "select event_type_id, event_type_desc, event_type_desc_fr, client_view_ind " +
        " from event_type where event_type_id = ? order by 1 " ;
		
		List<PrepaidEventTypeInfo> result = new ArrayList<PrepaidEventTypeInfo>();
		result = getJdbcTemplate().query(sql,  new Object [] {eventTypeId}, new PrepaidEventTypeRowMapper());
		
		return result.isEmpty() ? null : result.get(0);
	}
	
	public PrepaidAdjustmentReasonInfo[] retrievePrepaidAdjustmentReasons(int reasonTypeId, boolean onlyManual) throws DataAccessException {
		
		PrepaidAdjustmentReasonInfo[] result = null;
		//Check LDAP rollback flag
		if (!AppConfiguration.isPrepaidLargeBalanceRollback()) {
			StringBuffer sql = new StringBuffer();
			sql.append(
					"select rc.reason_id, rc.reason_desc, rc.reason_desc_fr " +
					",rcst.credit_debit_ind, rcst.req_trans_id_ind, nvl(rcst.default_amt,0) " +
					",rcst.equip_type_cd , rcst.manual_ind " +
					",rcst.min_balance_amt, rcst.min_adjustment_amt, rcst.max_adjustment_amt " +
					" FROM  reason_code rc, reason_code_sub_type rcst " +
					" where rc.reason_type_id = " + reasonTypeId +
					" and rcst.reason_type_id(+) = rc.reason_type_id " +
					" and rcst.reason_id(+) =rc.reason_id ");
			
			if (onlyManual) {
				sql.append(" and rcst.manual_ind='Y' and rcst.active_ind='Y' ");
			}
			
			sql.append(" order by 1, 2 ");
			
			result = getJdbcTemplate().query(sql.toString(), new RowMapper<PrepaidAdjustmentReasonInfo>() {
				
				@Override
				public PrepaidAdjustmentReasonInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
					
					PrepaidAdjustmentReasonInfo info = new PrepaidAdjustmentReasonInfo();
					info.setCode(rs.getString(1));
					info.setDescription(rs.getString(2));
					info.setDescriptionFrench(rs.getString(3) == null ? rs.getString(2) : rs.getString(3));
					info.setCreditAdjustment(rs.getString(4) == null ? true : rs.getString(4).equals("C") ? true : false);
					info.setDebitAdjustment(rs.getString(4) == null ? true : rs.getString(4).equals("D") ? true : false);
					info.setTransactionIdRequired(rs.getString(5) == null ? false : rs.getString(5).equals("N") ? false : true);
					info.setAmount(rs.getDouble(6));
					info.setAmountOverrideable(rs.getDouble(6)==0 ? true :false);
					info.setEquipmentType(rs.getString(7)==null ? "9" : rs.getString(7));
					info.setManualCharge(rs.getString(8)==null ? false: rs.getString(8).equals("Y") ? true : false);
					info.setMinimumBalance(rs.getDouble(9));
					info.setMinimumAdjustmentAmount(rs.getDouble(10));
					info.setMaximumAdjustmentAmount(rs.getDouble(11));
					
					return info;
				}
			}).toArray( new PrepaidAdjustmentReasonInfo[0]);
			
		} else {
			result = retrievePrepaidAdjustmentReasonsRollback(reasonTypeId, onlyManual);
		}
		
		return result;
		
	}
	
	private PrepaidAdjustmentReasonInfo[] retrievePrepaidAdjustmentReasonsRollback(int reasonTypeId, boolean onlyManual) throws DataAccessException {
		PrepaidAdjustmentReasonInfo[] result = null;
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select rc.reason_id, rc.reason_desc, rc.reason_desc_fr " +
				",rcst.credit_debit_ind, rcst.req_trans_id_ind, nvl(rcst.default_amt,0) " +
				",rcst.equip_type_cd , rcst.manual_ind" +
				" FROM  reason_code rc, reason_code_sub_type rcst " +
				" where rc.reason_type_id = " + reasonTypeId +
				" and rcst.reason_type_id(+) = rc.reason_type_id " +
				" and rcst.reason_id(+) =rc.reason_id ");
		
		if (onlyManual) {
			sql.append(" and rcst.manual_ind='Y' and rcst.active_ind='Y' ");
		}
		
		sql.append(" order by 1, 2 ");
		
		result = getJdbcTemplate().query(sql.toString(), new RowMapper<PrepaidAdjustmentReasonInfo>() {
			
			@Override
			public PrepaidAdjustmentReasonInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				PrepaidAdjustmentReasonInfo info = new PrepaidAdjustmentReasonInfo();
				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3) == null ? rs.getString(2) : rs.getString(3));
				info.setCreditAdjustment(rs.getString(4) == null ? true : rs.getString(4).equals("C") ? true : false);
				info.setDebitAdjustment(rs.getString(4) == null ? true : rs.getString(4).equals("D") ? true : false);
				info.setTransactionIdRequired(rs.getString(5) == null ? false : rs.getString(5).equals("N") ? false : true);
				info.setAmount(rs.getDouble(6));
				info.setAmountOverrideable(rs.getDouble(6)==0 ? true :false);
				info.setEquipmentType(rs.getString(7)==null ? "9" : rs.getString(7));
				info.setManualCharge(rs.getString(8)==null ? false: rs.getString(8).equals("Y") ? true : false);
				
				return info;
			}
		}).toArray( new PrepaidAdjustmentReasonInfo[0]);		
		
		return result;
	}
}
