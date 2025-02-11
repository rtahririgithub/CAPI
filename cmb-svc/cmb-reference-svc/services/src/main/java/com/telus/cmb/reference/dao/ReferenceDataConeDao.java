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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.telus.cmb.reference.utilities.AppConfiguration;
import com.telus.eas.utility.info.NotificationMessageTemplateInfo;
import com.telus.eas.utility.info.NotificationTypeInfo;
import com.telus.erm.referenceods.domain.ReferenceDecode;
import com.telus.erm.refpds.access.client.ReferencePdsAccess;

/**
 * @author Pavel Simonovsky
 *
 */
public class ReferenceDataConeDao extends JdbcDaoSupport {
	private static final Logger LOGGER = Logger.getLogger(ReferenceDataConeDao.class);
	private JdbcTemplate rollbackTemplate;
	
	public void setRollbackDataSource(DataSource rollbackJdbcDataSource) {
		this.rollbackTemplate = new JdbcTemplate(rollbackJdbcDataSource);
	}

	public NotificationTypeInfo[] retrieveNotificationTypeInfo() throws DataAccessException {
		// Keystone Rollback - begin
		if (AppConfiguration.isKeystoneRollback()) {
			LOGGER.debug("[Reference Cache] retrieveNotificationTypeInfo - KeystoneRollback!");
			return retrieveNotificationTypeInfoRollback();
		}
		// Keystone Rollback - end
			
		LOGGER.debug("[Reference Cache] retrieveNotificationTypeInfo - EMCM.CONE");
		String sql = "select CNTCT_CONTENT_TEMPLATE_CD, ORIG_ADDR_NUM, DELIV_RCPT_REQ_IND from CNTCT_CONTENT_DTL";
				
		NotificationTypeInfo[] notificationTypeList = getJdbcTemplate().query(sql, new RowMapper<NotificationTypeInfo>() {
			@Override
			public NotificationTypeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				NotificationTypeInfo info = new NotificationTypeInfo();
				info.setCode(rs.getString(1));
				info.setBillable("false");
				info.setOriginatingUser(String.valueOf(rs.getLong(2)));
				info.setDeliveryReceiptRequired(rs.getString(3));

				return info;
			}
		}).toArray( new NotificationTypeInfo[0]);
		
		if (notificationTypeList != null && notificationTypeList.length > 0) {
			Properties contentTypeDescEn = getContentTypeDescriptions(ReferencePdsAccess.LANG_EN);
			Properties contentTypeDescFr = getContentTypeDescriptions(ReferencePdsAccess.LANG_FR);
			for (NotificationTypeInfo info : notificationTypeList) {
				info.setDescription(contentTypeDescEn.getProperty(info.getCode()));
				info.setDescriptionFrench(contentTypeDescFr.getProperty(info.getCode()));
			}
		}
		return notificationTypeList;
	}
	
	private Properties getContentTypeDescriptions(String language) {
		Properties contentTypeDescs = new Properties();
		@SuppressWarnings("unchecked")
		Collection<ReferenceDecode> list = ReferencePdsAccess.getAllReferenceDecodes("CNTCT_CONTENT_TEMPLATE", language);
		Iterator<ReferenceDecode> iter = list.iterator();
		while (iter.hasNext()) {
			ReferenceDecode ref = iter.next();
			contentTypeDescs.setProperty(ref.getCode(), ref.getDecode());
		}
		return contentTypeDescs;
	}
	
	// Keystone Rollback method
	private NotificationTypeInfo[] retrieveNotificationTypeInfoRollback() throws DataAccessException {
		
		String sql = " select contact_content_type_id, description, " +
		" description_french, billable_indicator, originating_user_name, " +
		" delivery_receipt_req_ind from contact_content_type";
		
		return rollbackTemplate.query(sql, new RowMapper<NotificationTypeInfo>() {
			
			@Override
			public NotificationTypeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				NotificationTypeInfo info = new NotificationTypeInfo();

				info.setCode(String.valueOf(rs.getLong("contact_content_type_id")));
				info.setDescription(rs.getString("description"));
				info.setDescriptionFrench(rs.getString("description_french"));
				info.setBillable(rs.getString("billable_indicator"));
				info.setOriginatingUser(rs.getString("originating_user_name"));
				info.setDeliveryReceiptRequired(rs.getString("delivery_receipt_req_ind"));

				return info;
			}
		}).toArray( new NotificationTypeInfo[0]);
	}
	
	public NotificationMessageTemplateInfo[] retrieveNotificationMessageTemplateInfo() {
		// Keystone Rollback - begin
		if (AppConfiguration.isKeystoneRollback()) {
			LOGGER.debug("[Reference Cache] retrieveNotificationMessageTemplateInfo - KeystoneRollback!");
			return retrieveNotificationMessageTemplateInfoRollback();
		}
		// Keystone Rollback - end
		
		LOGGER.debug("[Reference Cache] retrieveNotificationMessageTemplateInfo - EMCM.CONE");
		
		String sql = "select ccd.CNTCT_CONTENT_TEMPLATE_CD, cct1.CNTCT_CONTENT_TXT, cct2.CNTCT_CONTENT_TXT from CNTCT_CONTENT_DTL ccd " +
				"left join CNTCT_CONTENT_TXT cct1 on cct1.CNTCT_CONTENT_DTL_ID = ccd.CNTCT_CONTENT_DTL_ID and cct1.LANGUAGE_CD = 'EN' " +
				"left join CNTCT_CONTENT_TXT cct2 on cct2.CNTCT_CONTENT_DTL_ID = ccd.CNTCT_CONTENT_DTL_ID and cct2.LANGUAGE_CD = 'FR' " +
				"order by ccd.CNTCT_CONTENT_TEMPLATE_CD"; 		

		return getJdbcTemplate().query(sql, new ResultSetExtractor<NotificationMessageTemplateInfo []>() {
			
			@Override
			public NotificationMessageTemplateInfo[] extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<NotificationMessageTemplateInfo> result = new ArrayList<NotificationMessageTemplateInfo>();
				NotificationMessageTemplateInfo lastRecord = null;
				
				while (rs.next()) {
					String code = rs.getString(1);
					if (lastRecord == null || !lastRecord.getCode().equals(code)) {
						lastRecord = new NotificationMessageTemplateInfo();
						lastRecord.setCode(code);
						lastRecord.setMessageTemplate(rs.getString(2));
						lastRecord.setMessageTemplateFrench(rs.getString(3));
						result.add(lastRecord);
					}
				}
				return result.toArray( new NotificationMessageTemplateInfo[0]);
			}
		}); 

	}
	
	// Keystone Rollback method
	private NotificationMessageTemplateInfo[] retrieveNotificationMessageTemplateInfoRollback() {

		// Set the ordering of the resultset - the contact_content_language table uses a
		// combined key.  The expected ordering should be two consecutive records
		// with the same contact_content_type_id, the first English and the second French. 
		
		String sql = "select contact_content_type_id, language_cd, template_text " +
		" from contact_content_language " +
		" order by contact_content_type_id, language_cd"; 
		
		return rollbackTemplate.query(sql, new ResultSetExtractor<NotificationMessageTemplateInfo []>() {
			
			@Override
			public NotificationMessageTemplateInfo[] extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<NotificationMessageTemplateInfo> result = new ArrayList<NotificationMessageTemplateInfo>();

				NotificationMessageTemplateInfo info = null;
				
				long typeId = 0;
				long currentId = 0;
				
				while (rs.next()) {

					// Only add a new NotificationMessageTemplateInfo object to the list if the
					// expected order is maintained.
					
					currentId = rs.getLong("contact_content_type_id");
					if (typeId != currentId) {

						// We've moved on to the next set of templates.
						
						if (info != null) {
							result.add(info);
						}
						typeId = currentId;
						info = new NotificationMessageTemplateInfo();
						info.setCode(String.valueOf(typeId));
					}
					if (rs.getString("language_cd").equals("EN")) {				
						info.setMessageTemplate(rs.getString("template_text"));
					}
					if (rs.getString("language_cd").equals("FR")) {
						info.setMessageTemplateFrench(rs.getString("template_text"));
					}
				}
				
				// After iterating through the resultset, add the final info object to the list.
				result.add(info);
				
				return result.toArray( new NotificationMessageTemplateInfo[0]);
			}
		}); 
	}
}
