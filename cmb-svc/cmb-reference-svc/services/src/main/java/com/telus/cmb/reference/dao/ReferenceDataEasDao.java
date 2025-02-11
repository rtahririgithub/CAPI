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

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import oracle.jdbc.OracleTypes;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.telus.eas.message.info.ApplicationMessageInfo;
import com.telus.eas.message.info.ApplicationMessageMappingInfo;
import com.telus.eas.utility.info.ApplicationSummaryInfo;
import com.telus.eas.utility.info.AudienceTypeInfo;
import com.telus.eas.utility.info.ConditionInfo;
import com.telus.eas.utility.info.ResultInfo;
import com.telus.eas.utility.info.RuleInfo;

/**
 * @author Pavel Simonovsky
 *
 */
public class ReferenceDataEasDao extends JdbcDaoSupport {

	public Collection<ApplicationSummaryInfo> retrieveApplicationSummaries() throws DataAccessException {
		
		String sql = "select application_id, application_cd, name from application";
		
		return getJdbcTemplate().query(sql, new RowMapper<ApplicationSummaryInfo>() {
			
			@Override
			public ApplicationSummaryInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new ApplicationSummaryInfo(rs.getInt(1), rs.getString(2), rs.getString(3));
			}
		});
	}

	public Collection<AudienceTypeInfo> retrieveAudienceTypes() throws DataAccessException {
		
		String sql = "select audience_type_id, audience_cd, description from audience_type";
		
		return getJdbcTemplate().query(sql, new RowMapper<AudienceTypeInfo>() {
			
			@Override
			public AudienceTypeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new AudienceTypeInfo(rs.getInt(1), rs.getString(2), rs.getString(3));
			}
		});
	}
	
	public Collection<ApplicationMessageInfo> retrieveApplicationMessages() throws DataAccessException {
		
		String sql = "select application_message_id, nvl(application_id, 0), nvl(audience_type_id, 0), nvl(brand_id, 255), message_type_id, language_cd, message_text " +
		" from adapted_message " +
		" order by application_message_id, application_id, audience_type_id, brand_id, language_cd";
		
		return getJdbcTemplate().query(sql, new ResultSetExtractor<Collection<ApplicationMessageInfo>>() {
			
			@Override
			public Collection<ApplicationMessageInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {

				List<ApplicationMessageInfo> result = new ArrayList<ApplicationMessageInfo>();
				
				long oldMessageId = 0;
				int oldApplicationId = 0;
				int oldAudienceTypeId = 0;
				int oldBrandId = 255;
				
				ApplicationMessageInfo info = null;

				while (rs.next()) {
					int newMessageId = rs.getInt(1);
					int newApplicationId = rs.getInt(2);
					int newAudienceTypeId = rs.getInt(3);
					int newBrandId = rs.getInt(4);

					if (newMessageId != oldMessageId || newApplicationId != oldApplicationId || newAudienceTypeId != oldAudienceTypeId || newBrandId != oldBrandId) {
						if (info != null)
							result.add(info);

						info = new ApplicationMessageInfo(newMessageId, newApplicationId, newAudienceTypeId, rs.getInt(5), newBrandId);

						oldMessageId = newMessageId;
						oldApplicationId = newApplicationId;
						oldAudienceTypeId = newAudienceTypeId;
						oldBrandId = newBrandId;
					}

					info.setText(rs.getString(6).toLowerCase(), rs.getString(7));
				}

				if (info != null)
					result.add(info);
				
				return result;
			}
		});
	}
	
	public Collection<ApplicationMessageMappingInfo> retrieveApplicationMessageMappings() throws DataAccessException {
		
		String sql = "select ap.application_cd, ae.application_error_cd, ae.application_message_id " +
		"  from application_error ae, " +
		"       application ap " +
		" where ap.application_id = ae.application_id " +
		" order by ap.application_cd, ae.application_error_cd";
		
		return getJdbcTemplate().query(sql, new RowMapper<ApplicationMessageMappingInfo>() {
			
			@Override
			public ApplicationMessageMappingInfo mapRow(ResultSet rs, int rowNum) throws SQLException {

				ApplicationMessageMappingInfo info = new ApplicationMessageMappingInfo();

		        info.setSourceApplicationCode(rs.getString(1));
		        info.setSourceMessageCode(rs.getString(2));
		        info.setTargetMessageId(rs.getLong(3));
		        
		        return info;
			}
		});
	}
	
	public Collection<RuleInfo> retrieveRules() throws DataAccessException {
		
		String call = "{call rule_utility_pkg.getRules(?)}";
		
		return getJdbcTemplate().execute(call, new CallableStatementCallback<Collection<RuleInfo>>() {
			
			@Override
			public Collection<RuleInfo> doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {

				cs.registerOutParameter(1, OracleTypes.CURSOR);
				cs.execute();
				ResultSet rs = (ResultSet) cs.getObject(1);
				
				try {
					int messageRuleIdOld = -1;
					RuleInfo rule = null;
					ArrayList<RuleInfo> rules = new ArrayList<RuleInfo>();
					ArrayList<ConditionInfo> conditions = null;

					while (rs.next()) {
						int ruleId = rs.getInt("MESSAGE_RULE_ID");
						if (ruleId != messageRuleIdOld) {
							if (rule != null && conditions != null) {
								rule.setConditions( (ConditionInfo[]) conditions.toArray(new
										ConditionInfo[conditions.size()]));
								rules.add(rule);
							}
							rule = new RuleInfo();
							conditions = new ArrayList<ConditionInfo>();
							rule.setCategory(rs.getInt("MESSAGE_RULE_CLASS_ID"));
							rule.setId(ruleId);
							rule.setName("");
							rule.setRole(rs.getInt("MESSAGE_ROLE_TYPE_ID"));
							rule.setType(rs.getInt("MESSAGE_RULE_TYPE_ID"));
							rule.setDescription(rs.getString("MESSAGE_RULE_TXT"));
							ResultInfo resultInfo = new ResultInfo();
							resultInfo.setName(rs.getString("RULE_RESULT_TYPE_NM"));
							resultInfo.setResultType(rs.getInt("RULE_RESULT_TYPE_ID"));
							resultInfo.setValue(rs.getString("MESSAGE_RESULT_ID"));
							// Follow the xml way, to
							// set the result code and descriptions using the same attributes of the encompassing rule
							resultInfo.setCode(rule.getCode());
							resultInfo.setDescription(rule.getDescription());
							resultInfo.setDescriptionFrench(rule.getDescriptionFrench());
							rule.setResultInfo(resultInfo);
							messageRuleIdOld = ruleId;
						}
						ConditionInfo condition = new ConditionInfo();
						condition.setId(rs.getInt("RULE_CONDITION_ID"));
						condition.setName(rs.getString("RULE_CONDITION_TXT"));
						condition.setConditionType(rs.getInt("RULE_CONDITION_TYPE_ID"));
						condition.setValue(rs.getString("RULE_COND_VALUE_STR"));
						condition.setFromAmount(rs.getDouble("RULE_COND_VALUE_FROM_QTY"));
						condition.setToAmount(rs.getDouble("RULE_COND_VALUE_TO_QTY"));
						condition.setFromDate(rs.getDate("RULE_COND_VALUE_FROM_DT"));
						condition.setToDate(rs.getDate("RULE_COND_VALUE_TO_DT"));
						conditions.add(condition);
					}
					// for the last set
					if (rule != null && conditions != null) {
						rule.setConditions( (ConditionInfo[]) conditions.toArray(new
								ConditionInfo[conditions.size()]));
						rules.add(rule);
					}				

					return rules;
					
				} finally {
					rs.close();
				}
			}
		});
	}
}
