package com.telus.cmb.utility.configurationmanager.dao.impl;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.interaction.Interaction;
import com.telus.api.interaction.InteractionDetail;
import com.telus.api.interaction.InteractionManager;
import com.telus.cmb.common.util.DatabaseUtil;
import com.telus.cmb.common.util.EJBUtil;
import com.telus.cmb.productequipment.helper.svc.ProductEquipmentHelper;
import com.telus.cmb.utility.configurationmanager.dao.ConfigurationManagerDao;
import com.telus.cmb.utility.dealermanager.dao.impl.ChannelPartnerDaoImpl;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.config.info.AccountStatusChangeInfo;
import com.telus.eas.config.info.AddressChangeInfo;
import com.telus.eas.config.info.BillPaymentInfo;
import com.telus.eas.config.info.ConfigurationInfo;
import com.telus.eas.config.info.EquipmentChangeInfo;
import com.telus.eas.config.info.InteractionInfo;
import com.telus.eas.config.info.LogInfo;
import com.telus.eas.config.info.PaymentMethodChangeInfo;
import com.telus.eas.config.info.PhoneNumberChangeInfo;
import com.telus.eas.config.info.PrepaidTopupInfo;
import com.telus.eas.config.info.PricePlanChangeInfo;
import com.telus.eas.config.info.RoleChangeInfo;
import com.telus.eas.config.info.ServiceChangeInfo;
import com.telus.eas.config.info.SubscriberChangeInfo;
import com.telus.eas.config.info.SubscriberChargeInfo;
import com.telus.eas.config1.info.ActivationLogInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.equipment.info.WarrantyInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;


public class ConfigurationManagerDaoImpl implements ConfigurationManagerDao {
	private JdbcTemplate easJdbcTemplate;
	private JdbcTemplate servJdbcTemplate;
	private static final Logger LOGGER = Logger.getLogger(ChannelPartnerDaoImpl.class);

	private static final String TYPES_FIELD = "TYPE";
	private static final String GET_EXISTING_ADDRESS_DETAILS_SQL = "select '" + InteractionManager.TYPE_ADDRESS_CHANGE + "' " + TYPES_FIELD + " from TMI_ADDRESS_TRANSACTION where TRANSACTION_ID = ?";
	private static final String GET_EXISTING_CHARGE_DETAILS_SQL = "select '" + InteractionManager.TYPE_SUBSCRIBER_CHARGE + "' " + TYPES_FIELD + " from TMI_CHARGE where TRANSACTION_ID = ?";
	private static final String GET_EXISTING_EQUIPMENT_CHANGE_DETAILS_SQL = "select '" + InteractionManager.TYPE_EQUIPMENT_CHANGE + "' " + TYPES_FIELD + " from TMI_EQUIPMENT_CHANGE where TRANSACTION_ID = ?";
	private static final String GET_EXISTING_PAYMENT_METHOD_DETAILS_SQL = "select '" + InteractionManager.TYPE_PAYMENT_METHOD_CHANGE + "' " + TYPES_FIELD + " from TMI_PAYMENT_METHOD_TRANSACTION where TRANSACTION_ID = ?";
	private static final String GET_EXISTING_PAYMENT_DETAILS_SQL = "select '" + InteractionManager.TYPE_BILL_PAYMENT + "' " + TYPES_FIELD + " from TMI_PAYMENT_TRANSACTION where TRANSACTION_ID = ?";
	private static final String GET_EXISTING_PHONE_NUMBER_DETAILS_SQL = "select '" + InteractionManager.TYPE_PHONE_NUMBER_CHANGE + "' " + TYPES_FIELD + " from TMI_PHONE_NUMBER_TRANSACTION where TRANSACTION_ID = ?";
	private static final String GET_EXISTING_PRICE_PLAN_DETAILS_SQL = "select '" + InteractionManager.TYPE_PRICE_PLAN_CHANGE + "' " + TYPES_FIELD + " from TMI_PRICE_PLAN_TRANSACTION where TRANSACTION_ID = ?";
	private static final String GET_EXISTING_SERVICE_DETAILS_SQL = "select '" + InteractionManager.TYPE_SERVICE_CHANGE + "' " + TYPES_FIELD + " from TMI_SERVICE_TRANSACTION where TRANSACTION_ID = ?";
	private static final String GET_EXISTING_STATUS_CHANGE_DETAILS_SQL = "select '" + InteractionManager.TYPE_ACCOUNT_STATUS_CHANGE + "' " + TYPES_FIELD + " from TMI_STATUS_CHANGE where TRANSACTION_ID = ?";
	private static final String GET_EXISTING_SUBS_INFO_DETAILS_SQL = "select '" + InteractionManager.TYPE_SUBSCRIBER_CHANGE + "' " + TYPES_FIELD + " from TMI_SUBS_INFO_TRANSACTION where TRANSACTION_ID = ?";
	private static final String GET_EXISTING_TOP_UP_DETAILS_SQL = "select '" + InteractionManager.TYPE_PREPAID_TOPUP + "' " + TYPES_FIELD + " from TMI_TOP_UP_TRANSACTION where TRANSACTION_ID = ?";
	private static final String GET_EXISTING_ROLE_CHANGE_DETAILS_SQL = "select '" + InteractionManager.TYPE_ROLE_CHANGE + "' " + TYPES_FIELD + " from TMI_ROLE_CHANGE where TRANSACTION_ID = ?";

	private static final String GET_EXISTING_DETAILS_SQL =
		    GET_EXISTING_ADDRESS_DETAILS_SQL + " union " +
		    GET_EXISTING_CHARGE_DETAILS_SQL + " union " +
		    GET_EXISTING_EQUIPMENT_CHANGE_DETAILS_SQL + " union " +
		    GET_EXISTING_PAYMENT_METHOD_DETAILS_SQL + " union " +
		    GET_EXISTING_PAYMENT_DETAILS_SQL + " union " +
		    GET_EXISTING_PHONE_NUMBER_DETAILS_SQL + " union " +
		    GET_EXISTING_PRICE_PLAN_DETAILS_SQL + " union " +
		    GET_EXISTING_SERVICE_DETAILS_SQL + " union " +
		    GET_EXISTING_STATUS_CHANGE_DETAILS_SQL + " union " +
		    GET_EXISTING_SUBS_INFO_DETAILS_SQL + " union " +
		    GET_EXISTING_TOP_UP_DETAILS_SQL + " union " +
		    GET_EXISTING_ROLE_CHANGE_DETAILS_SQL
		  ;

		  // detail SQLS
	private static final String GET_ADDRESS_DETAILS_SQL = "select TRANSACTION_ID, TRANSACTION_DATETIME, OLD_ADDRESS_LINE, OLD_CITY, OLD_PROVINCE, OLD_COUNTRY, OLD_POSTAL, NEW_ADDRESS_LINE, NEW_CITY, NEW_PROVINCE, NEW_COUNTRY, NEW_POSTAL from TMI_ADDRESS_TRANSACTION where TRANSACTION_ID = ?";
	private static final String GET_CHARGE_DETAILS_SQL = "select TRANSACTION_ID, TRANSACTION_DATETIME, CHARGE_CODE, WAIVER_CODE from TMI_CHARGE where TRANSACTION_ID = ?";
	private static final String GET_EQUIPMENT_CHANGE_DETAILS_SQL = "select TRANSACTION_ID, TRANSACTION_DATETIME, SWAP_REQUEST_ID, CHNL_ORG_CD, USER_CODE, REQUESTOR_ID, SWAP_TYPE, BAN, SUBSCRIBER_NO, REPAIR_ID, OLD_SM_NUM, OLD_TECHNOLOGY_TYPE, OLD_PRODUCT_CD, OLD_PRODUCT_STATUS_CD, OLD_PRODUCT_CLASS_CD, OLD_PRODUCT_GP_TYPE_CD, OLD_WARRANTY_DATE, OLD_DOA_DATE, OLD_INITIAL_MFG_DATE, OLD_ASSOC_MULE_IMEI, OLD_PEND_DATE, OLD_PEND_PROD_GP_TYPE_CD, NEW_SM_NUM, NEW_TECHNOLOGY_TYPE, NEW_PRODUCT_CD, NEW_PRODUCT_STATUS_CD, NEW_PRODUCT_CLASS_CD, NEW_PRODUCT_GP_TYPE_CD, NEW_WARRANTY_DATE, LEASE_FLG, SAP_ORDER_NUM, CREATE_DATE, CREATE_USER, MODIFY_DATE, MODIFY_USER from TMI_EQUIPMENT_CHANGE where TRANSACTION_ID = ?";
	private static final String GET_EQUIPMENT_CHANGE_DETAILS_BY_ESNS_SQL = "select TRANSACTION_ID, TRANSACTION_DATETIME, SWAP_REQUEST_ID, CHNL_ORG_CD, USER_CODE, REQUESTOR_ID, SWAP_TYPE, BAN, SUBSCRIBER_NO, REPAIR_ID, OLD_SM_NUM, OLD_TECHNOLOGY_TYPE, OLD_PRODUCT_CD, OLD_PRODUCT_STATUS_CD, OLD_PRODUCT_CLASS_CD, OLD_PRODUCT_GP_TYPE_CD, OLD_WARRANTY_DATE, OLD_DOA_DATE, OLD_INITIAL_MFG_DATE, OLD_ASSOC_MULE_IMEI, OLD_PEND_DATE, OLD_PEND_PROD_GP_TYPE_CD, NEW_SM_NUM, NEW_TECHNOLOGY_TYPE, NEW_PRODUCT_CD, NEW_PRODUCT_STATUS_CD, NEW_PRODUCT_CLASS_CD, NEW_PRODUCT_GP_TYPE_CD, NEW_WARRANTY_DATE, LEASE_FLG, SAP_ORDER_NUM, CREATE_DATE, CREATE_USER, MODIFY_DATE, MODIFY_USER from TMI_EQUIPMENT_CHANGE where OLD_SM_NUM = ? and NEW_SM_NUM = ?";
	private static final String GET_PAYMENT_METHOD_CHANGE_DETAILS_SQL = "select TRANSACTION_ID, TRANSACTION_DATETIME, OLD_PAYMENT_METHOD, NEW_PAYMENT_METHOD from TMI_PAYMENT_METHOD_TRANSACTION where TRANSACTION_ID = ?";
	private static final String GET_PAYMENT_DETAILS_SQL = "select TRANSACTION_ID, TRANSACTION_DATETIME,PAYMENT_METHOD, PAYMENT_AMOUNT from TMI_PAYMENT_TRANSACTION where TRANSACTION_ID = ?";
	private static final String GET_PHONE_CHANGE_DETAILS_SQL = "select TRANSACTION_ID, TRANSACTION_DATETIME, OLD_PHONE_NUMBER, NEW_PHONE_NUMBER from TMI_PHONE_NUMBER_TRANSACTION where TRANSACTION_ID = ?";
	private static final String GET_PP_CHANGE_DETAILS_SQL = "select TRANSACTION_ID, TRANSACTION_DATETIME, KB_SOC_NEW, KB_SOC_OLD, SERVICE_ID_NEW_PRICE_PLAN, SERVICE_ID_OLD_PRICE_PLAN from TMI_PRICE_PLAN_TRANSACTION where TRANSACTION_ID = ?";
	private static final String GET_SERVICE_CHANGE_DETAILS_SQL = "select TRANSACTION_ID, TRANSACTION_DATETIME, KB_SOC, ST_SERVICE_ID, ACTION from TMI_SERVICE_TRANSACTION where TRANSACTION_ID = ?";
	private static final String GET_STATUS_CHANGE_DETAILS_SQL = "select TRANSACTION_ID, TRANSACTION_DATETIME, STATUS_FLAG, OLD_STATUS, OLD_HOTLINED_IND, NEW_STATUS, NEW_HOTLINED_IND from TMI_STATUS_CHANGE where TRANSACTION_ID = ?";
	private static final String GET_SUB_CHANGE_DETAILS_SQL = "select TRANSACTION_ID, TRANSACTION_DATETIME, NEW_LAST_NAME, NEW_MIDDLE_INITIAL, NEW_FIRST_NAME, NEW_EMAIL_ADDRESS, OLD_LAST_NAME, OLD_MIDDLE_INITIAL, OLD_FIRST_NAME, OLD_EMAIL_ADDRESS from TMI_SUBS_INFO_TRANSACTION where TRANSACTION_ID = ?";
	private static final String GET_TOP_UP_DETAILS_SQL = "select TRANSACTION_ID, TRANSACTION_DATETIME, AMOUNT, CARD_TYPE, TOP_UP_TYPE from TMI_TOP_UP_TRANSACTION where TRANSACTION_ID = ?";
	private static final String GET_ROLE_CHANGE_DETAILS_SQL = "select TRANSACTION_ID, TRANSACTION_DATETIME, NEW_ACCESS_ROLE_CD, OLD_ACCESS_ROLE_CD from TMI_ROLE_CHANGE where TRANSACTION_ID = ?";

	/**
	 * Sql for selecting from the transaction_header table by ban.
	 */
	private static final String GET_HEADER_BY_BAN_SQL = "select TRANSACTION_ID, TRANSACTION_DATETIME, TRANSACTION_TYPE_CD, PATRON_ID, PATRON_ID_TYPE_CD, BAN, PROFILE_EFFECTIVE_DATE, SALEABLE_ITEM_ID, INSTANCE_ID, APPLICATION_ID, LOCATION_ID, OPERATOR_ID, DEALER_CODE, SALESREP_CODE, PICTURE_ID, CLIENT_STATE_REASON_ID from TRANSACTION_HEADER where ban = ? and TRANSACTION_DATETIME > ? and TRANSACTION_DATETIME < ?";
	/**
	 * Same as GET_HEADER_BY_BAN_SQL except the transactions are further
	 * filtered by the type.
	 */
	private static final String GET_HEADER_BY_BAN_WITH_TYPE_SQL = GET_HEADER_BY_BAN_SQL + " and TRANSACTION_TYPE_CD = ?";

	/**
	 * SQL for selecting from the transaction_header table by subscriber id
	 * (patron id).
	 */
	private static final String GET_HEADER_BY_SUB_SQL = "select TRANSACTION_ID, TRANSACTION_DATETIME, TRANSACTION_TYPE_CD, PATRON_ID, PATRON_ID_TYPE_CD, BAN, PROFILE_EFFECTIVE_DATE, SALEABLE_ITEM_ID, INSTANCE_ID, APPLICATION_ID, LOCATION_ID, OPERATOR_ID, DEALER_CODE, SALESREP_CODE, PICTURE_ID, CLIENT_STATE_REASON_ID from TRANSACTION_HEADER where PATRON_ID = ? and TRANSACTION_DATETIME > ? and TRANSACTION_DATETIME < ?";

	/**
	 * Same as GET_HEADER_BY_SUB_SQL except the transactions are further
	 * filtered by the type.
	 */
	private static final String GET_HEADER_BY_SUB_WITH_TYPE_SQL = GET_HEADER_BY_SUB_SQL + " and TRANSACTION_TYPE_CD = ?";
	  
	private ProductEquipmentHelper productEquipmentHelperSvc = null;
	
	public JdbcTemplate getEasJdbcTemplate() {
		return easJdbcTemplate;
	}

	public JdbcTemplate getServJdbcTemplate() {
		return servJdbcTemplate;
	}

	public void setEasJdbcTemplate(JdbcTemplate easJdbcTemplate) {
		this.easJdbcTemplate = easJdbcTemplate;
	}

	public void setServJdbcTemplate(JdbcTemplate servJdbcTemplate) {
		this.servJdbcTemplate = servJdbcTemplate;
	}

	@Override
	public void addProperties(final int configurationId, final String[] nameArray, final String[] valueArray)
			throws ApplicationException {
		String sqlDelete = "DELETE FROM PROPERTY WHERE CONFIGURATION_ID=? AND NAME=?";
		String sqlInsert = "INSERT INTO PROPERTY (CONFIGURATION_ID , NAME, VALUE) VALUES ( ?, ?, ?)";
		getEasJdbcTemplate().batchUpdate(sqlDelete, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				DatabaseUtil.setNullable(ps, 1, java.sql.Types.DECIMAL, configurationId);
				DatabaseUtil.setNullable(ps, 2, java.sql.Types.VARCHAR, nameArray[i]);
			}
			
			@Override
			public int getBatchSize() {
				return nameArray.length;
			}
		});
		
		getEasJdbcTemplate().batchUpdate(sqlInsert, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				DatabaseUtil.setNullable(ps, 1, java.sql.Types.DECIMAL, configurationId);
				DatabaseUtil.setNullable(ps, 2, java.sql.Types.VARCHAR, nameArray[i]);
				DatabaseUtil.setNullable(ps, 3, java.sql.Types.VARCHAR, valueArray[i]);
			}
			
			@Override
			public int getBatchSize() {
				return nameArray.length;
			}
		});
		
	}

	@Override
	public long getActivationLogID() throws ApplicationException {
		String sql = "SELECT act_log_seq.nextval FROM dual";

		return getEasJdbcTemplate().query(sql, new ResultSetExtractor<Long>() {

			@Override
			public Long extractData(ResultSet result) throws SQLException, DataAccessException {
				long actLogID = 0;
				while (result.next()) {
					actLogID = result.getLong(1);
				}
				return actLogID;
			}

		});
	}

	@Override
	public ConfigurationInfo[] getChildConfigurations(final int parentConfigurationId)
			throws ApplicationException {
		String sql = "SELECT CONFIGURATION.CONFIGURATION_ID, " + 
							"CONFIGURATION.NAME, " +
							"CONFIGURATION.PARENT_ID " + 
					" FROM CONFIGURATION " +
					" WHERE PARENT_ID = ?";
		
		ConfigurationInfo[] configInfoArray = getEasJdbcTemplate().query(sql, new Object[] { parentConfigurationId }, new ResultSetExtractor<ConfigurationInfo[]>() {

			@Override
			public ConfigurationInfo[] extractData(ResultSet result) throws SQLException, DataAccessException {
				int counter = 0;
				Collection<ConfigurationInfo> configurationList = new ArrayList<ConfigurationInfo>();

				while (result.next() && counter < 1000) {
					int configurationId = result.getInt("CONFIGURATION_ID");
					String name = result.getString("NAME");

					ConfigurationInfo info = new ConfigurationInfo(configurationId, name);
					configurationList.add(info);
					counter++;
				}

				return configurationList.toArray(new ConfigurationInfo[configurationList.size()]);
			}

		});
		
		for (ConfigurationInfo configInfo : configInfoArray) {
			loadProperties(configInfo);
		}
		
		return configInfoArray;
	}

	@Override
	public ConfigurationInfo[] getConfiguration(final String[] path) throws ApplicationException {
		String sql = "SELECT CONFIGURATION.CONFIGURATION_ID, " + 
							"CONFIGURATION.NAME, " +
							"CONFIGURATION.PARENT_ID " + 
					" FROM CONFIGURATION " +
					" WHERE PARENT_ID = ? AND NAME = ?";
		
		ConfigurationInfo lastInfo = null;
		Collection<ConfigurationInfo> configurationList = new ArrayList<ConfigurationInfo>();
		int parentId = 0 ;
		for (int i = 0; i < path.length; i++) {
			Object[] args = new Object[] {parentId, path[i]};
			ConfigurationInfo info = getEasJdbcTemplate().query(sql, args, new ResultSetExtractor<ConfigurationInfo>() {

				@Override
				public ConfigurationInfo extractData(ResultSet result) throws SQLException, DataAccessException {
					if (result.next()) {
						int configurationId = result.getInt("CONFIGURATION_ID");
						String name = result.getString("NAME");
						ConfigurationInfo info = new ConfigurationInfo(configurationId, name);
						
						return info;
					}
					
					return null;
				}
				
			});
			
			if (info != null) {
				info.setParent0(lastInfo);
				configurationList.add(info);
				parentId = info.getId();
				lastInfo = info;
				loadProperties(info);
			} else {
				throw new ApplicationException(SystemCodes.CMB_CFGMGR_DAO, "Unknown Object Exception " + path[i], "");
			}
		}
		
		return configurationList.toArray(new ConfigurationInfo[configurationList.size()]);
	}

	@Override
	public EquipmentChangeInfo getEquipmentChangeDetailsByESNs(String oldESN, String newESN) throws ApplicationException {

		String sql = "select TRANSACTION_ID, TRANSACTION_DATETIME, SWAP_REQUEST_ID, CHNL_ORG_CD, USER_CODE, " +
							"REQUESTOR_ID, SWAP_TYPE, BAN, SUBSCRIBER_NO, REPAIR_ID, OLD_SM_NUM, OLD_TECHNOLOGY_TYPE, OLD_PRODUCT_CD, " +
							"OLD_PRODUCT_STATUS_CD, OLD_PRODUCT_CLASS_CD, OLD_PRODUCT_GP_TYPE_CD, OLD_WARRANTY_DATE, OLD_DOA_DATE, " +
							"OLD_INITIAL_MFG_DATE, OLD_ASSOC_MULE_IMEI, OLD_PEND_DATE, OLD_PEND_PROD_GP_TYPE_CD, NEW_SM_NUM, NEW_TECHNOLOGY_TYPE, " +
							"NEW_PRODUCT_CD, NEW_PRODUCT_STATUS_CD, NEW_PRODUCT_CLASS_CD, NEW_PRODUCT_GP_TYPE_CD, NEW_WARRANTY_DATE, LEASE_FLG, " +
							"SAP_ORDER_NUM, CREATE_DATE, CREATE_USER, MODIFY_DATE, MODIFY_USER " +
					"from TMI_EQUIPMENT_CHANGE " +
					"where OLD_SM_NUM = ? and NEW_SM_NUM = ?";
		
		Object[] args = new Object[] {oldESN, newESN};
		return getServJdbcTemplate().query(sql, args, new ResultSetExtractor<EquipmentChangeInfo>() {

			@Override
			public EquipmentChangeInfo extractData(ResultSet result) throws SQLException, DataAccessException {
				EquipmentChangeInfo info = new EquipmentChangeInfo();
				if (result.next()) {
					mapToEquipmentChangeInfo(result, info);
				}
				return info;
			}
			
		});
	}

	@Override
	public InteractionDetail[] getInteractionDetails(long id) throws ApplicationException {
		List<InteractionDetail> details = new ArrayList<InteractionDetail>();
		List<String> detailTypes = getDetailTypes(id);
		for (String type : detailTypes) {
			/**
			 * It would be better to move the getXXXDetails method to there own
			 * classes which all implement a common interface, however, since
			 * this is relatively simple implementation for now, i'm only going
			 * to use methods instead of classes.
			 */

			if (InteractionManager.TYPE_ADDRESS_CHANGE.equals(type)) {
				List<InteractionDetail> addressDetails = getDetails(id, new AddressHelper());
				details.addAll(addressDetails);
			} else if (type.equals(InteractionManager.TYPE_SUBSCRIBER_CHARGE)) {
				List<InteractionDetail> chargeDetails = getDetails(id, new ChargeHelper());
				details.addAll(chargeDetails);
			} else if (type.equals(InteractionManager.TYPE_EQUIPMENT_CHANGE)) {
				List<InteractionDetail> equipDetails = getDetails(id, new EquipmentHelper());
				details.addAll(equipDetails);
			} else if (type.equals(InteractionManager.TYPE_PAYMENT_METHOD_CHANGE)) {
				List<InteractionDetail> payMethDetails = getDetails(id, new PayMethodChangeHelper());
				details.addAll(payMethDetails);
			} else if (type.equals(InteractionManager.TYPE_BILL_PAYMENT)) {
				List<InteractionDetail> paymentDetails = getDetails(id, new PaymentHelper());
				details.addAll(paymentDetails);
			} else if (type.equals(InteractionManager.TYPE_PHONE_NUMBER_CHANGE)) {
				List<InteractionDetail> phoneChangeDetails = getDetails(id, new PhoneChangeHelper());
				details.addAll(phoneChangeDetails);
			} else if (type.equals(InteractionManager.TYPE_PRICE_PLAN_CHANGE)) {
				List<InteractionDetail> ppChangeDetails = getDetails(id, new PricePlanChangeHelper());
				details.addAll(ppChangeDetails);
			} else if (type.equals(InteractionManager.TYPE_SERVICE_CHANGE)) {
				List<InteractionDetail> srvChangeDetails = getDetails(id, new ServiceChangeHelper());
				details.addAll(srvChangeDetails);
			} else if (type.equals(InteractionManager.TYPE_ACCOUNT_STATUS_CHANGE)) {
				List<InteractionDetail> statusChangeDetails = getDetails(id, new StatusChangeHelper());
				details.addAll(statusChangeDetails);
			} else if (type.equals(InteractionManager.TYPE_SUBSCRIBER_CHANGE)) {
				List<InteractionDetail> subChangeDetails = getDetails(id, new SubChangeHelper());
				details.addAll(subChangeDetails);
			} else if (type.equals(InteractionManager.TYPE_PREPAID_TOPUP)) {
				List<InteractionDetail> topUpDetails = getDetails(id, new TopUpDetails());
				details.addAll(topUpDetails);
			} else if (type.equals(InteractionManager.TYPE_ROLE_CHANGE)) {
				List<InteractionDetail> roleChangeDetails = getDetails(id, new RoleChangeHelper());
				details.addAll(roleChangeDetails);
			} else {
				LOGGER.error("Unknown type " + type);
			}
		}

		InteractionDetail[] returnArray = new InteractionDetail[details.size()];
		details.toArray(returnArray);

		return returnArray;
	}

	@Override
	public Interaction[] getInteractionsByBan(int ban, Date from, Date to, String type) throws ApplicationException {
	    String sql = "";
	    Object[] sqlParams = null;
	    if(type == null || type.length() == 0) {
	      sql = GET_HEADER_BY_BAN_SQL;
	      Object[] params = {new Integer(ban), new java.sql.Date(from.getTime()), new java.sql.Date(to.getTime())};
	      sqlParams = params;
	    }
	    else {
	      sql = GET_HEADER_BY_BAN_WITH_TYPE_SQL;
	      Object[] params = {new Integer(ban), new java.sql.Date(from.getTime()), new java.sql.Date(to.getTime()), type};
	      sqlParams = params;
	    }

	    return getTransHeader(sql, sqlParams);
	}

	@Override
	public Interaction[] getInteractionsBySubscriber(String subscriberId,
			Date from, Date to, String type) throws ApplicationException {
		String sql = "";
	    Object[] sqlParams = null;
	    if(type == null || type.length() == 0) {
	      sql = GET_HEADER_BY_SUB_SQL;
	      Object[] params = {subscriberId, new java.sql.Date(from.getTime()), new java.sql.Date(to.getTime())};
	      sqlParams = params;
	    }
	    else {
	      sql = GET_HEADER_BY_SUB_WITH_TYPE_SQL;
	      Object[] params = {subscriberId, new java.sql.Date(from.getTime()), new java.sql.Date(to.getTime()), type};
	      sqlParams = params;
	    }

	    return getTransHeader(sql, sqlParams);
	}

	@Override
	public void loadProperties(final ConfigurationInfo info) throws ApplicationException {
		String sql = "SELECT CONFIGURATION_ID, NAME, VALUE FROM property WHERE CONFIGURATION_ID = ? ";
		Object[] args = new Object[] {info.getId()};

		getEasJdbcTemplate().query(sql, args, new ResultSetExtractor<Object>() {
			@Override
			public Object extractData(ResultSet result) throws SQLException, DataAccessException {
				while (result.next()) {
					String name = result.getString("NAME");
					String value = result.getString("VALUE");

					info.put(name, value);
				}
				return null;
			}

		});

	}

	@Override
	public void logActivation(final ActivationLogInfo pActivationLogInfo,
			final long pActLogID) throws ApplicationException {
		String call = "{call Log_Utility.LogActivation(?,?,?,?,?,?,?)}";
		
		getEasJdbcTemplate().execute(call, new CallableStatementCallback<Object>() {

			@Override
			public Object doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				callable.setLong(1, pActLogID);
				callable.setString(2, pActivationLogInfo.getStatusCD().trim());
				callable.setLong(3, pActivationLogInfo.getClientID());
				callable.setLong(4, pActivationLogInfo.getDealerID());
				callable.setLong(5, pActivationLogInfo.getCustomerID());
				callable.setString(6, pActivationLogInfo.getActivationData().trim());
				callable.setString(7, pActivationLogInfo.getUserID().trim());

				callable.execute();
				return null;
			}
			
		});
		
	}

	@Override
	public void logActivationSummary(final ActivationLogInfo pActivationLogInfo)
			throws ApplicationException {
		String sql = " INSERT INTO activation_log " +
				      "(act_log_id, status_cd, client_id, dealer_id, customer_id, activation_data, create_date, create_userid ) " +
				      " VALUES " +
				      " (act_log_seq.nextval, ?, ?, ?, ?, ?, SYSDATE, ?)";
		
		getEasJdbcTemplate().execute(sql, new PreparedStatementCallback<Object>() {

			@Override
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ps.setString(1, pActivationLogInfo.getStatusCD().trim());
				ps.setLong(2, pActivationLogInfo.getClientID());
				ps.setLong(3, pActivationLogInfo.getDealerID());
				ps.setLong(4, pActivationLogInfo.getCustomerID());
				ps.setString(5, pActivationLogInfo.getActivationData().trim());
				ps.setString(6, pActivationLogInfo.getUserID().trim());
				
				int resultCount = ps.executeUpdate();
				if (resultCount != 1) {
			        throw new SQLException("Error in Web Activation Logging Info Insert");
			      }
				return null;
			}
		});
		
	}

	@Override
	public void logApplication(final LogInfo plogInfo) throws ApplicationException {
		String sql = "INSERT INTO log " +
				      "(log_id, transaction_id , class_name , " +
				      " operation, customer_id , alternate_id , " +
				      " application_message , trans_type_cd , " +
				      " create_date , create_userid ) " +
				      " VALUES " +
				      " (log_seq.nextval , ?, ?, ? ,? ,? ,? ,? " +
				      " , sysdate , ? ) ";
		
		getEasJdbcTemplate().execute(sql, new PreparedStatementCallback<Object>() {

			@Override
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ps.setLong(1, plogInfo.getTransactionID());
				ps.setString(2, plogInfo.getClassName().trim());
				ps.setString(3, plogInfo.getOperation().trim());
				ps.setLong(4, plogInfo.getCustomerID());
				ps.setLong(5, plogInfo.getAlternateID());
				ps.setString(6, plogInfo.getAppMessage());
				ps.setString(7, plogInfo.getTransactionTypeCD().trim());
				ps.setString(8, plogInfo.getUserID());

				int resultCount = ps.executeUpdate();
				if (resultCount != 1) {
					throw new SQLException("Error in Logging Info Insert");
				}
				return null;
			}
		});
	}

	@Override
	public ConfigurationInfo newConfiguration(final int parentConfigurationId,
			final String name) throws ApplicationException {
		String sql = "INSERT INTO CONFIGURATION"
            + "( CONFIGURATION_ID"
            + ", NAME"
            + ", PARENT_ID"
            + ") VALUES"
            + "( ?"
            + ", ?"
            + ", ?"
            + ")";
		
		return getEasJdbcTemplate().execute(sql, new PreparedStatementCallback<ConfigurationInfo>() {

			@Override
			public ConfigurationInfo doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				int configurationId = (int)(System.currentTimeMillis()/10L & 0x0FFFFFFFL);
				DatabaseUtil.setNullable(ps, 1, java.sql.Types.DECIMAL, configurationId);
				DatabaseUtil.setNullable(ps, 2, java.sql.Types.VARCHAR, name);
				DatabaseUtil.setNullable(ps, 3, java.sql.Types.DECIMAL, parentConfigurationId);
				ps.executeUpdate();
				return new ConfigurationInfo(configurationId, name);
			}
			
		});
	}

	@Override
	public InteractionInfo newReport(final String transactionType,
			final String applicationId, final int operatorId, final String dealerCode,
			final String salesRepCode, final int banId, final String subscriberId, final long reasonId)
			throws ApplicationException {
		String sql = "INSERT INTO TRANSACTION_HEADER"
            + "( TRANSACTION_ID"
            + ", TRANSACTION_DATETIME"
            + ", PATRON_ID"
            + ", TRANSACTION_TYPE_CD"
            + ", PATRON_ID_TYPE_CD"
            + ", BAN"
            + ", PROFILE_EFFECTIVE_DATE"
            + ", SALEABLE_ITEM_ID"
            + ", INSTANCE_ID"
            + ", APPLICATION_ID"
            + ", LOCATION_ID"
            + ", OPERATOR_ID"
            + ", DEALER_CODE"
            + ", SALESREP_CODE"
            + ", PICTURE_ID"
            + ", CLIENT_STATE_REASON_ID"
            + ") VALUES"
            + "( ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ")";
		
		final InteractionInfo info = new InteractionInfo();
		info.setId(getNextTransactionId());      
		return getServJdbcTemplate().execute(sql, new PreparedStatementCallback<InteractionInfo>() {

			@Override
			public InteractionInfo doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
		        info.setType(transactionType);
		        if (applicationId != null && applicationId.length() > 12) {
		        	info.setApplicationId(applicationId.substring(0, 12));
		        }else {
		        	info.setApplicationId(applicationId);
		        }
		        info.setOperatorId(operatorId);
		        info.setDealerCode(dealerCode);
		        info.setSalesRepCode(salesRepCode);
		        info.setBan(banId);
		        info.setSubscriberId(subscriberId);
		        info.setDate(new java.util.Date());   // TODO: use logical date
		        info.setReasonId(reasonId);
		        
		        ps.setLong(1, info.getId());
		        
				if (info.getDatetime() == null) {
					ps.setNull(2, java.sql.Types.TIMESTAMP);
				} else {
					ps.setTimestamp(2, new java.sql.Timestamp(info.getDatetime().getTime()));
				}                               
	        
		        //--------------------------------------------------------
		        // PatronId should be set to subscriberId, or Ban, if
		        // subscriberId is unavailable
		        //--------------------------------------------------------
				if (subscriberId != null) {
					ps.setString(3, subscriberId);
					ps.setString(5, InteractionInfo.PATRON_ID_TYPE_SUB);
				}else {
					ps.setString(3, String.valueOf(banId));
					ps.setString(5, InteractionInfo.PATRON_ID_TYPE_BAN);
				}
				
				if (transactionType == null) {
					ps.setNull(4, java.sql.Types.VARCHAR);
				} else {
					ps.setString(4, transactionType);
				}
	        
				ps.setInt(6, banId);
				ps.setNull(7, java.sql.Types.TIMESTAMP);
				ps.setNull(8, java.sql.Types.DECIMAL);
				ps.setNull(9, java.sql.Types.DECIMAL);
				if (applicationId != null && applicationId.length() > 12) {
					DatabaseUtil.setNullable(ps, 10, java.sql.Types.CHAR, applicationId.substring(0, 12));
				}else {
					DatabaseUtil.setNullable(ps, 10, java.sql.Types.CHAR, applicationId);
				}
				ps.setNull(11, java.sql.Types.DECIMAL);
				ps.setInt(12, operatorId);
				if (dealerCode != null && dealerCode.length() > 10) { //column size is 10 CHARs max
					DatabaseUtil.setNullable(ps, 13, java.sql.Types.CHAR, dealerCode.substring(0, 10));
				}else {
					DatabaseUtil.setNullable(ps, 13, java.sql.Types.CHAR, dealerCode);
				}
				if (salesRepCode != null && salesRepCode.length() > 4) { //column size is 4 CHARs max
					DatabaseUtil.setNullable(ps, 14, java.sql.Types.CHAR, salesRepCode.substring(0, 4));
				}else {
					DatabaseUtil.setNullable(ps, 14, java.sql.Types.CHAR, salesRepCode);
				}

				ps.setNull(15, java.sql.Types.CHAR);

				if (reasonId == 0) {
					ps.setNull(16, java.sql.Types.NUMERIC);
				} else {
					ps.setLong(16, reasonId);
				}
		        
				ps.executeUpdate();
				
				return info;
			}
	        
			
		});
	}

	@Override
	public int removeConfiguration(final int configurationId) throws ApplicationException {
		String sql = "DELETE FROM CONFIGURATION"
	        + "  WHERE CONFIGURATION_ID in"
	        + "    (SELECT CONFIGURATION_ID"
	        + "     FROM CONFIGURATION"
	        + "     START WITH CONFIGURATION_ID = ?"
	        + "     CONNECT BY PRIOR CONFIGURATION_ID = PARENT_ID)";
		return getEasJdbcTemplate().execute(sql, new PreparedStatementCallback<Integer>() {

			@Override
			public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ps.setInt(1, configurationId);

				return ps.executeUpdate();
			}
			
		});
	}

	@Override
	public int removeProperties(final int configurationId, final String[] name)
			throws ApplicationException {
		String sql = "DELETE FROM PROPERTY WHERE PROPERTY.CONFIGURATION_ID = ? AND PROPERTY.NAME = ?";
		return getEasJdbcTemplate().execute(sql, new PreparedStatementCallback<Integer>() {

			@Override
			public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				int recoerdsAffected = 0;

				for (String strName : name) {
					ps.setInt(1, configurationId);
					ps.setString(2, strName);
					recoerdsAffected += ps.executeUpdate();
				}

				return recoerdsAffected;
			}

		});
	}

	@Override
	public int removeProperties(final int configurationId) throws ApplicationException {
		String sql = "DELETE FROM PROPERTY WHERE PROPERTY.CONFIGURATION_ID = ?";
		return getEasJdbcTemplate().execute(sql, new PreparedStatementCallback<Integer>() {

			@Override
			public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ps.setInt(1, configurationId);

				return ps.executeUpdate();
			}
			
		});
	}

	@Override
	public int removePropertiesRecursively(final int configurationId) throws ApplicationException {
		String sql = "DELETE FROM PROPERTY"
	        + "  WHERE PROPERTY.CONFIGURATION_ID in"
	        + "    (SELECT CONFIGURATION_ID"
	        + "     FROM CONFIGURATION"
	        + "     START WITH CONFIGURATION_ID = ?"
	        + "     CONNECT BY PRIOR CONFIGURATION_ID = PARENT_ID)";
		return getEasJdbcTemplate().execute(sql, new PreparedStatementCallback<Integer>() {

			@Override
			public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ps.setInt(1, configurationId);

				return ps.executeUpdate();
			}
			
		});
	}

	@Override
	public void report_accountStatusChange(final long transactionId,
			final Date transactionDate, final char oldHotlinedInd, final char newHotlinedInd,
			final char oldStatus, final char newStatus, final char statusFlag)
			throws ApplicationException {
		String sql = "INSERT INTO TMI_STATUS_CHANGE"
            + "( TRANSACTION_ID"
            + ", TRANSACTION_DATETIME"
            + ", STATUS_FLAG"
            + ", OLD_STATUS"
            + ", OLD_HOTLINED_IND"
            + ", NEW_STATUS"
            + ", NEW_HOTLINED_IND"
            + ") VALUES"
            + "( ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ")";
		
		getServJdbcTemplate().execute(sql, new PreparedStatementCallback<Object>() {

			@Override
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ps.setLong(1, transactionId);
		        
				if (transactionDate == null) {
					ps.setNull(2, java.sql.Types.TIMESTAMP);
				} else {
					ps.setTimestamp(2, new java.sql.Timestamp(transactionDate.getTime()));
				}                             
	        
	            ps.setString(3, String.valueOf(statusFlag));
	            ps.setString(4, String.valueOf(oldStatus));
	            ps.setString(5, String.valueOf(oldHotlinedInd));
	            ps.setString(6, String.valueOf(newStatus));
	            ps.setString(7, String.valueOf(newHotlinedInd));
				ps.executeUpdate();
				return null;
			}
		});
		
	}

	@Override
	public void report_changeAddress(final long transactionId, final Date transactionDate,
			final AddressInfo oldAddr, final AddressInfo newAddr) throws ApplicationException {
		String sql = "INSERT INTO TMI_ADDRESS_TRANSACTION"
            + "( TRANSACTION_DATETIME"
            + ", TRANSACTION_ID"
            + ", OLD_ADDRESS_LINE"
            + ", OLD_CITY"
            + ", OLD_PROVINCE"
            + ", OLD_COUNTRY"
            + ", OLD_POSTAL"
            + ", NEW_ADDRESS_LINE"
            + ", NEW_CITY"
            + ", NEW_PROVINCE"
            + ", NEW_COUNTRY"
            + ", NEW_POSTAL"
            + ") VALUES"
            + "( ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ")";
		
		getServJdbcTemplate().execute(sql, new PreparedStatementCallback<Object>() {

			@Override
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				if (transactionDate == null) {
					ps.setNull(1, java.sql.Types.TIMESTAMP);
				} else {
					ps.setTimestamp(1, new java.sql.Timestamp(transactionDate.getTime()));
				}

				ps.setLong(2, transactionId);
				DatabaseUtil.setNullable(ps, 3, java.sql.Types.VARCHAR, oldAddr.getPrimaryLine());
				
				if (oldAddr.getCity() != null && oldAddr.getCity().length() > 30 ){
					DatabaseUtil.setNullable(ps, 4, java.sql.Types.VARCHAR, oldAddr.getCity().substring(0, 30));
				}else {
					DatabaseUtil.setNullable(ps, 4, java.sql.Types.VARCHAR, oldAddr.getCity());
				}
				DatabaseUtil.setNullable(ps, 5, java.sql.Types.VARCHAR, oldAddr.getProvince());
				DatabaseUtil.setNullable(ps, 6, java.sql.Types.VARCHAR, oldAddr.getCountry());
				DatabaseUtil.setNullable(ps, 7, java.sql.Types.VARCHAR, oldAddr.getPostalCode());
				DatabaseUtil.setNullable(ps, 8, java.sql.Types.VARCHAR, newAddr.getFullAddress()[0]);
				if (newAddr.getCity() != null && newAddr.getCity().length() > 30 ){
					DatabaseUtil.setNullable(ps, 9, java.sql.Types.VARCHAR, newAddr.getCity().substring(0, 30));
				}else {
					DatabaseUtil.setNullable(ps, 9, java.sql.Types.VARCHAR, newAddr.getCity());
				}
				DatabaseUtil.setNullable(ps, 10, java.sql.Types.VARCHAR, newAddr.getProvince());
				DatabaseUtil.setNullable(ps, 11, java.sql.Types.VARCHAR, newAddr.getCountry());
				DatabaseUtil.setNullable(ps, 12, java.sql.Types.VARCHAR, newAddr.getPostalCode());

				ps.executeUpdate();
				return null;
			}
		});
		
	}

	@Override
	public void report_changePaymentMethod(final long transactionId,
			final Date transactionDate, final char oldPaymentMethod, final char newPaymentMethod)
			throws ApplicationException {
		String sql = "INSERT INTO TMI_PAYMENT_METHOD_TRANSACTION"
            + "( TRANSACTION_DATETIME"
            + ", TRANSACTION_ID"
            + ", OLD_PAYMENT_METHOD"
            + ", NEW_PAYMENT_METHOD"
            + ") VALUES"
            + "( ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ")";
		
		getServJdbcTemplate().execute(sql, new PreparedStatementCallback<Object>() {

			@Override
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				if (transactionDate == null) {
					ps.setNull(1, java.sql.Types.TIMESTAMP);
				} else {
					ps.setTimestamp(1, new java.sql.Timestamp(transactionDate.getTime()));
				}                               
	        
				ps.setLong(2, transactionId);
				ps.setString(3, String.valueOf(oldPaymentMethod));
	            ps.setString(4, String.valueOf(newPaymentMethod));
	            ps.executeUpdate();
	            
				return null;
			}
			
		});
	}

	@Override
	public void report_changePhoneNumber(final long transactionId,
			final Date transactionDate, final String oldPhoneNumber, final String newPhoneNumber)
			throws ApplicationException {
		String sql = "INSERT INTO TMI_PHONE_NUMBER_TRANSACTION"
            + "( TRANSACTION_DATETIME"
            + ", TRANSACTION_ID"
            + ", NEW_PHONE_NUMBER"
            + ", OLD_PHONE_NUMBER"
            + ") VALUES"
            + "( ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ")";
		
		getServJdbcTemplate().execute(sql, new PreparedStatementCallback<Object>() {

			@Override
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				if (transactionDate == null) {
					ps.setNull(1, java.sql.Types.TIMESTAMP);
				} else {
					ps.setTimestamp(1, new java.sql.Timestamp(transactionDate.getTime()));
				}                              
	        
				ps.setLong(2, transactionId);
				DatabaseUtil.setNullable(ps, 3, java.sql.Types.VARCHAR, newPhoneNumber);
				DatabaseUtil.setNullable(ps, 4, java.sql.Types.VARCHAR, oldPhoneNumber);
	                
	            ps.executeUpdate();
	            
				return null;
			}
			
		});
		
	}

	@Override
	public void report_changePricePlan(final long transactionId,
			final Date transactionDate, final String oldPlan, final String newPlan,
			final ServiceAgreementInfo[] services) throws ApplicationException {

		String sql = "INSERT INTO TMI_PRICE_PLAN_TRANSACTION"
            + "( TRANSACTION_DATETIME"
            + ", TRANSACTION_ID"
            + ", KB_SOC_NEW"
            + ", KB_SOC_OLD"
            + ", SERVICE_ID_NEW_PRICE_PLAN"
            + ", SERVICE_ID_OLD_PRICE_PLAN"
            + ") VALUES"
            + "( ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ")";
		
		getServJdbcTemplate().execute(sql, new PreparedStatementCallback<Object>() {

			@Override
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				if (transactionDate == null) {
					ps.setNull(1, java.sql.Types.TIMESTAMP);
				} else {
					ps.setTimestamp(1, new java.sql.Timestamp (transactionDate.getTime()));
				}                             
	        
				ps.setLong(2, transactionId);
				DatabaseUtil.setNullable(ps, 3, java.sql.Types.VARCHAR, newPlan);
				DatabaseUtil.setNullable(ps, 4, java.sql.Types.VARCHAR, oldPlan);
				ps.setNull(5, java.sql.Types.DECIMAL);  
				ps.setNull(6, java.sql.Types.DECIMAL);                
	            ps.executeUpdate();
	            
				return null;
			}
		});
		report_changeService(transactionId, transactionDate, services);
	}

	@Override
	public void report_changeRole(final long transactionId, final Date transactionDate,
			final String oldRole, final String newRole) throws ApplicationException {
		String sql = "INSERT INTO TMI_ROLE_CHANGE"
            + "( TRANSACTION_DATETIME"
            + ", TRANSACTION_ID"
            + ", NEW_ACCESS_ROLE_CD"
            + ", OLD_ACCESS_ROLE_CD"
            + ") VALUES"
            + "( ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ")";
		
		getServJdbcTemplate().execute(sql, new PreparedStatementCallback<Object>() {

			@Override
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				if (transactionDate == null) {
					ps.setNull(1, java.sql.Types.TIMESTAMP);
				} else {
					ps.setTimestamp(1, new java.sql.Timestamp (transactionDate.getTime()));
				}                             

				ps.setLong(2, transactionId);
				DatabaseUtil.setNullable(ps, 3, java.sql.Types.VARCHAR, newRole);
				DatabaseUtil.setNullable(ps, 4, java.sql.Types.VARCHAR, oldRole);
	            ps.executeUpdate();
	            
				return null;
			}
		});
		
	}

	@Override
	public void report_changeService(final long transactionId, final Date transactionDate,
			final ServiceAgreementInfo[] services) throws ApplicationException {
		String sql = "INSERT INTO TMI_SERVICE_TRANSACTION"
            + "( TRANSACTION_DATETIME"
            + ", TRANSACTION_ID"
            + ", KB_SOC"
            + ", ST_SERVICE_ID"
            + ", ACTION"
            + ") VALUES"
            + "( ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ")";
		
		getServJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				if (transactionDate == null) {
					ps.setNull(1, java.sql.Types.TIMESTAMP);
				} else {
					ps.setTimestamp(1, new java.sql.Timestamp (transactionDate.getTime()));
				}                             

				ps.setLong(2, transactionId);
				DatabaseUtil.setNullable(ps, 3, java.sql.Types.VARCHAR, services[i].getServiceCode());
				ps.setNull(4, java.sql.Types.DECIMAL); 
				ps.setString(5, String.valueOf((char) services[i].getTransaction()));
			}
			
			@Override
			public int getBatchSize() {
				return services.length;
			}
		});
		
	}

	@Override
	public void report_changeSubscriber(final long transactionId,
			final Date transactionDate, final SubscriberInfo oldSubscriber,
			final SubscriberInfo newSubscriber) throws ApplicationException {
		String sql = "INSERT INTO TMI_SUBS_INFO_TRANSACTION"
            + "( TRANSACTION_DATETIME"
            + ", TRANSACTION_ID"
            + ", NEW_LAST_NAME"
            + ", NEW_MIDDLE_INITIAL"
            + ", NEW_FIRST_NAME"
            + ", NEW_EMAIL_ADDRESS"
            + ", OLD_LAST_NAME"
            + ", OLD_MIDDLE_INITIAL"
            + ", OLD_FIRST_NAME"
            + ", OLD_EMAIL_ADDRESS"
            + ") VALUES"
            + "( ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ")";
		
		getServJdbcTemplate().execute(sql, new PreparedStatementCallback<Object>() {

			@Override
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				if (transactionDate == null) {
					ps.setNull(1, java.sql.Types.TIMESTAMP);
				} else {
					ps.setTimestamp(1, new java.sql.Timestamp (transactionDate.getTime()));
				}                             
				ps.setLong(2, transactionId);
				DatabaseUtil.setNullable(ps, 3, java.sql.Types.VARCHAR, newSubscriber.getConsumerName().getLastName());
				DatabaseUtil.setNullable(ps, 4, java.sql.Types.VARCHAR, newSubscriber.getConsumerName().getMiddleInitial());
				DatabaseUtil.setNullable(ps, 5, java.sql.Types.VARCHAR, newSubscriber.getConsumerName().getFirstName());
				DatabaseUtil.setNullable(ps, 6, java.sql.Types.VARCHAR, newSubscriber.getEmailAddress());
				DatabaseUtil.setNullable(ps, 7, java.sql.Types.VARCHAR, oldSubscriber.getConsumerName().getLastName());
				DatabaseUtil.setNullable(ps, 8, java.sql.Types.VARCHAR, oldSubscriber.getConsumerName().getMiddleInitial());
				DatabaseUtil.setNullable(ps, 9, java.sql.Types.VARCHAR, oldSubscriber.getConsumerName().getFirstName());
				DatabaseUtil.setNullable(ps, 10, java.sql.Types.VARCHAR, oldSubscriber.getEmailAddress());
	            ps.executeUpdate();
	            
				return null;
			}
		});
		
	}

	@Override
	public void report_makePayment(final long transactionId, final Date transactionDate,
			final char paymentMethod, final double paymentAmount)
			throws ApplicationException {
		String sql = "INSERT INTO TMI_PAYMENT_TRANSACTION"
            + "( TRANSACTION_DATETIME"
            + ", TRANSACTION_ID"
            + ", PAYMENT_METHOD"
            + ", PAYMENT_AMOUNT"
            + ") VALUES"
            + "( ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ")";
		
		getServJdbcTemplate().execute(sql, new PreparedStatementCallback<Object>() {

			@Override
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				if (transactionDate == null) {
					ps.setNull(1, java.sql.Types.TIMESTAMP);
				} else {
					ps.setTimestamp(1, new java.sql.Timestamp (transactionDate.getTime()));
				}                             
				ps.setLong(2, transactionId);
				ps.setString(3, String.valueOf(paymentMethod));
				ps.setDouble(4, paymentAmount);
	            ps.executeUpdate();
	            
				return null;
			}
		});
		
	}

	@Override
	public void report_prepaidAccountTopUp(final long transactionId,
			final Date transactionDate, final Double amount, final char cardType, final char topUpType)
			throws ApplicationException {
		String sql = "INSERT INTO TMI_TOP_UP_TRANSACTION"
            + "( TRANSACTION_DATETIME"
            + ", TRANSACTION_ID"
            + ", AMOUNT"
            + ", CARD_TYPE"
            + ", TOP_UP_TYPE"
            + ") VALUES"
            + "( ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ")";
		
		getServJdbcTemplate().execute(sql, new PreparedStatementCallback<Object>() {

			@Override
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				if (transactionDate == null) {
					ps.setNull(1, java.sql.Types.TIMESTAMP);
				} else {
					ps.setTimestamp(1, new java.sql.Timestamp(transactionDate.getTime()));
				}
				ps.setLong(2, transactionId);
				DatabaseUtil.setNullable(ps, 3, java.sql.Types.DECIMAL, amount);

				ps.setString(4, String.valueOf(cardType));
				ps.setString(5, String.valueOf(topUpType));
				ps.executeUpdate();

				return null;
			}
		});
		
	}

	@Override
	public void report_subscriberChangeEquipment(final long transactionId,
			final Date transactionDate, final SubscriberInfo subscriberInfo,
			final EquipmentInfo oldEquipmentInfo, final EquipmentInfo newEquipmentInfo,
			final String dealerCode, final String salesRepCode, final String requestorId,
			final String repairId, final String swapType,
			final EquipmentInfo associatedMuleEquipmentInfo, final String applicationName)
			throws ApplicationException {
		String sql = "INSERT INTO TMI_EQUIPMENT_CHANGE"
            + "( TRANSACTION_ID"
            + ", TRANSACTION_DATETIME"
            + ", SWAP_REQUEST_ID"
            + ", CHNL_ORG_CD"
            + ", USER_CODE"
            + ", REQUESTOR_ID"
            + ", SWAP_TYPE"
            + ", BAN"
            + ", SUBSCRIBER_NO"
            + ", REPAIR_ID"
            + ", OLD_SM_NUM"
            + ", OLD_TECHNOLOGY_TYPE"
            + ", OLD_PRODUCT_CD"
            + ", OLD_PRODUCT_STATUS_CD"
            + ", OLD_PRODUCT_CLASS_CD"
            + ", OLD_PRODUCT_GP_TYPE_CD"
            + ", OLD_WARRANTY_DATE"
            + ", OLD_DOA_DATE"
            + ", OLD_INITIAL_MFG_DATE"
            + ", OLD_ASSOC_MULE_IMEI"
            + ", OLD_PEND_DATE"
            + ", OLD_PEND_PROD_GP_TYPE_CD"
            + ", NEW_SM_NUM"
            + ", NEW_TECHNOLOGY_TYPE"
            + ", NEW_PRODUCT_CD"
            + ", NEW_PRODUCT_STATUS_CD"
            + ", NEW_PRODUCT_CLASS_CD"
            + ", NEW_PRODUCT_GP_TYPE_CD"
            + ", NEW_WARRANTY_DATE"
            + ", LEASE_FLG"
            + ", SAP_ORDER_NUM"
            + ", CREATE_DATE"
            + ", CREATE_USER"
            + ", MODIFY_DATE"
            + ", MODIFY_USER"
            + ") VALUES"
            + "( ?"
            + ", ?"
//            + ", swap_request_seq.nextval"
            + ", TMI_EQUIPMENT_CHANGE_SEQ.nextval"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ")";
		
		final WarrantyInfo oldWarrantyInfo = getProductEquipmentHelper().getWarrantyInfo(oldEquipmentInfo.getSerialNumber());
		final WarrantyInfo newWarrantyInfo = getProductEquipmentHelper().getWarrantyInfo(newEquipmentInfo.getSerialNumber());
		
		getServJdbcTemplate().execute(sql, new PreparedStatementCallback<Object>() {

			@Override
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ps.setLong(1, transactionId);
				if (transactionDate == null) {
					ps.setNull(2, java.sql.Types.TIMESTAMP);
				} else {
					ps.setTimestamp(2, new java.sql.Timestamp(transactionDate.getTime()));
				}
				DatabaseUtil.setNullable(ps, 3, java.sql.Types.VARCHAR, dealerCode);
				DatabaseUtil.setNullable(ps, 4, java.sql.Types.VARCHAR, salesRepCode);
				
				if (requestorId != null && requestorId.length() > 16) {
					DatabaseUtil.setNullable(ps, 5, java.sql.Types.VARCHAR, requestorId.substring(0, 16));
				}else {
					DatabaseUtil.setNullable(ps, 5, java.sql.Types.VARCHAR, requestorId);
				}
				DatabaseUtil.setNullable(ps, 6, java.sql.Types.VARCHAR, swapType != null ? swapType.trim().toUpperCase() : "");
				ps.setInt(7, subscriberInfo.getBanId());
				ps.setString(8, subscriberInfo.getSubscriberId());
				ps.setString(9, repairId);
				DatabaseUtil.setNullable(ps, 10, java.sql.Types.VARCHAR, oldEquipmentInfo.getSerialNumber());
				DatabaseUtil.setNullable(ps, 11, java.sql.Types.VARCHAR, oldEquipmentInfo.getTechType());
				DatabaseUtil.setNullable(ps, 12, java.sql.Types.VARCHAR, oldEquipmentInfo.getProductCode());
				DatabaseUtil.setNullable(ps, 13, java.sql.Types.VARCHAR, oldEquipmentInfo.getProductStatusCode());
				DatabaseUtil.setNullable(ps, 14, java.sql.Types.VARCHAR, oldEquipmentInfo.getProductClassCode());
				DatabaseUtil.setNullable(ps, 15, java.sql.Types.VARCHAR, oldEquipmentInfo.getProductGroupTypeCode());
				DatabaseUtil.setNullable(ps, 16, java.sql.Types.TIMESTAMP, oldWarrantyInfo.getWarrantyExpiryDate() != null ? new java.sql.Timestamp(oldWarrantyInfo.getWarrantyExpiryDate().getTime()): null);
				DatabaseUtil.setNullable(ps, 17, java.sql.Types.TIMESTAMP, oldWarrantyInfo.getDOAExpiryDate() != null ? new java.sql.Timestamp(oldWarrantyInfo.getDOAExpiryDate().getTime()): null);
				DatabaseUtil.setNullable(ps, 18, java.sql.Types.TIMESTAMP, oldWarrantyInfo.getInitialManufactureDate() != null ? new java.sql.Timestamp(oldWarrantyInfo.getInitialManufactureDate().getTime()) : null);
				DatabaseUtil.setNullable(ps, 19, java.sql.Types.VARCHAR, associatedMuleEquipmentInfo != null ? associatedMuleEquipmentInfo.getSerialNumber() : null);
				DatabaseUtil.setNullable(ps, 20, java.sql.Types.TIMESTAMP, oldWarrantyInfo.getLatestPendingDate() != null ? new java.sql.Timestamp(oldWarrantyInfo.getLatestPendingDate().getTime()): null);
				ps.setString(21, "");
				DatabaseUtil.setNullable(ps, 22, java.sql.Types.VARCHAR, newEquipmentInfo.getSerialNumber());
				DatabaseUtil.setNullable(ps, 23, java.sql.Types.VARCHAR, newEquipmentInfo.getTechType());
				DatabaseUtil.setNullable(ps, 24, java.sql.Types.VARCHAR, newEquipmentInfo.getProductCode());
				DatabaseUtil.setNullable(ps, 25, java.sql.Types.VARCHAR, newEquipmentInfo.getProductStatusCode());
				DatabaseUtil.setNullable(ps, 26, java.sql.Types.VARCHAR, newEquipmentInfo.getProductClassCode());
				DatabaseUtil.setNullable(ps, 27, java.sql.Types.VARCHAR, newEquipmentInfo.getProductGroupTypeCode());
				DatabaseUtil.setNullable(ps, 28, java.sql.Types.TIMESTAMP, newWarrantyInfo.getWarrantyExpiryDate() != null ? new java.sql.Timestamp(newWarrantyInfo.getWarrantyExpiryDate().getTime()) : null);
				ps.setString(29, "N");
				ps.setString(30, "");
				ps.setTimestamp(31, new java.sql.Timestamp(new java.util.Date().getTime()));

				String createUser = applicationName;
				if (createUser == null || createUser.trim().length() == 0) {
					createUser = requestorId;
				}

				ps.setString(32, createUser);
				ps.setNull(33, java.sql.Types.TIMESTAMP);
				ps.setNull(34, java.sql.Types.VARCHAR);
				ps.executeUpdate();

				return null;
			}
		});
		
	}

	@Override
	public void report_subscriberNewCharge(final long transactionId,
			final Date transactionDate, final String chargeCode, final String waiverCode)
			throws ApplicationException {
		String sql = "INSERT INTO TMI_CHARGE"
            + "( TRANSACTION_DATETIME"
            + ", TRANSACTION_ID"
            + ", CHARGE_CODE"
            + ", WAIVER_CODE"
            + ") VALUES"
            + "( ?"
            + ", ?"
            + ", ?"
            + ", ?"
            + ")";
		getServJdbcTemplate().execute(sql, new PreparedStatementCallback<Object>() {

			@Override
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				if (transactionDate == null) {
					ps.setNull(1, java.sql.Types.TIMESTAMP);
				} else {
					ps.setTimestamp(1, new java.sql.Timestamp(transactionDate.getTime()));
				}
				ps.setLong(2, transactionId);
				DatabaseUtil.setNullable(ps, 3, java.sql.Types.VARCHAR, chargeCode);
				DatabaseUtil.setNullable(ps, 4, java.sql.Types.VARCHAR, waiverCode);
				ps.executeUpdate();

				return null;
			}
		});
		
	}

	private void mapToEquipmentChangeInfo(ResultSet result, EquipmentChangeInfo info) throws SQLException {
		info.setSwapRequestId(result.getObject("SWAP_REQUEST_ID") == null ? null : Long.valueOf(result.getLong("SWAP_REQUEST_ID")));
		info.setChnlOrgCd(result.getString("CHNL_ORG_CD"));
		info.setUserCode(result.getString("USER_CODE"));
		info.setRequestorId(result.getString("REQUESTOR_ID"));
		info.setSwapType(result.getString("SWAP_TYPE"));
		info.setBan(result.getObject("BAN") == null ? null : Integer.valueOf(result.getInt("BAN")));
		info.setSubscriberNo(result.getString("SUBSCRIBER_NO"));
		info.setRepairId(result.getString("REPAIR_ID"));
		info.setOldSmNum(result.getString("OLD_SM_NUM"));
		info.setOldTechnologyType(result.getString("OLD_TECHNOLOGY_TYPE"));
		info.setOldProductCd(result.getString("OLD_PRODUCT_CD"));
		info.setOldProductStatusCd(result.getString("OLD_PRODUCT_STATUS_CD"));
		info.setOldProductClassCd(result.getString("OLD_PRODUCT_CLASS_CD"));
		info.setOldProductGpTypeCd(result.getString("OLD_PRODUCT_GP_TYPE_CD"));
		info.setOldWarrantyDate(result.getTimestamp("OLD_WARRANTY_DATE"));
		info.setOldDoaDate(result.getTimestamp("OLD_DOA_DATE"));
		info.setOldInitialMfgDate(result.getTimestamp("OLD_INITIAL_MFG_DATE"));
		info.setOldAssocMuleImei(result.getString("OLD_ASSOC_MULE_IMEI"));
		info.setOldPendDate(result.getTimestamp("OLD_PEND_DATE"));
		info.setOldPendProdGpTypeCd(result.getString("OLD_PEND_PROD_GP_TYPE_CD"));
		info.setNewSmNum(result.getString("NEW_SM_NUM"));
		info.setNewTechnologyType(result.getString("NEW_TECHNOLOGY_TYPE"));
		info.setNewProductCd(result.getString("NEW_PRODUCT_CD"));
		info.setNewProductStatusCd(result.getString("NEW_PRODUCT_STATUS_CD"));
		info.setNewProductClassCd(result.getString("NEW_PRODUCT_CLASS_CD"));
		info.setNewProductGpTypeCd(result.getString("NEW_PRODUCT_GP_TYPE_CD"));
		info.setNewWarrantyDate(result.getTimestamp("NEW_WARRANTY_DATE"));
		info.setLeaseFlg(result.getString("LEASE_FLG"));
		info.setSapOrderNum(result.getString("SAP_ORDER_NUM"));
		info.setCreateDate(result.getTimestamp("CREATE_DATE"));
		info.setCreateUser(result.getString("CREATE_USER"));
		info.setModifyDate(result.getTimestamp("MODIFY_DATE"));
		info.setModifyUser(result.getString("MODIFY_USER"));
	}

	  /**
	    * Encapsulates the differences between the details, especially concerning retrieval.
	    */
	private interface DetailHelper {
		public List<InteractionDetail> read(ResultSet rs) throws SQLException;
		public String getSql();
	}

	/**
	 * Retrieves the top up transaction details
	 */
	private class TopUpDetails implements DetailHelper {
		
		public String getSql() {
			return GET_TOP_UP_DETAILS_SQL;
		}
	    
		public List<InteractionDetail> read(ResultSet resultSet) throws SQLException {
			List<InteractionDetail> details = new ArrayList<InteractionDetail>();

			while (resultSet.next()) {
				PrepaidTopupInfo info = new PrepaidTopupInfo();
				info.setAmount(resultSet.getDouble("AMOUNT"));
				info.setCardType(resultSet.getString("CARD_TYPE").charAt(0));
				info.setTopUpType(resultSet.getString("TOP_UP_TYPE").charAt(0));
				details.add(info);
			}

			return details;
		}
	}

	/**
	 * Retrieves the subscriber change transaction details
	 */
	private class SubChangeHelper implements DetailHelper {
		public String getSql() {
			return GET_SUB_CHANGE_DETAILS_SQL;
		}

		public List<InteractionDetail> read(ResultSet resultSet) throws SQLException {
			List<InteractionDetail> details = new ArrayList<InteractionDetail>();

			while (resultSet.next()) {
				SubscriberChangeInfo info = new SubscriberChangeInfo();
				info.setOldLastName(resultSet.getString("OLD_LAST_NAME"));
				info.setOldMiddleInitial(resultSet.getString("OLD_MIDDLE_INITIAL"));
				info.setOldFirstName(resultSet.getString("OLD_FIRST_NAME"));
				info.setOldEmailAddress(resultSet.getString("OLD_EMAIL_ADDRESS"));

				info.setNewLastName(resultSet.getString("NEW_LAST_NAME"));
				info.setNewMiddleInitial(resultSet.getString("NEW_MIDDLE_INITIAL"));
				info.setNewFirstName(resultSet.getString("NEW_FIRST_NAME"));
				info.setNewEmailAddress(resultSet.getString("NEW_EMAIL_ADDRESS"));
				details.add(info);
			}

			return details;
		}
	}

	/**
	 * Retrieves the status change transaction details
	 */
	private class StatusChangeHelper implements DetailHelper {
		
		public String getSql() {
			return GET_STATUS_CHANGE_DETAILS_SQL;
		}

		public List<InteractionDetail> read(ResultSet resultSet) throws SQLException {
			List<InteractionDetail> details = new ArrayList<InteractionDetail>();

			while (resultSet.next()) {
				AccountStatusChangeInfo info = new AccountStatusChangeInfo();
				info.setStatusFlag(resultSet.getString("STATUS_FLAG").charAt(0));
				info.setOldStatus(resultSet.getString("OLD_STATUS").charAt(0));
				info.setOldHotlinedInd(resultSet.getString("OLD_HOTLINED_IND").charAt(0));
				info.setNewStatus(resultSet.getString("NEW_STATUS").charAt(0));
				info.setNewHotlinedInd(resultSet.getString("NEW_HOTLINED_IND").charAt(0));

				details.add(info);
			}

			return details;
		}
	}


	/**
	 * Retrieves the service change transaction details
	 */
	private class ServiceChangeHelper implements DetailHelper {
		public String getSql() {
			return GET_SERVICE_CHANGE_DETAILS_SQL;
		}

		public List<InteractionDetail> read(ResultSet resultSet) throws SQLException {
			List<InteractionDetail> details = new ArrayList<InteractionDetail>();

			while (resultSet.next()) {
				ServiceChangeInfo info = new ServiceChangeInfo();
				info.setServiceCode(resultSet.getString("KB_SOC"));
				info.setAction(resultSet.getString("ACTION").charAt(0));

				details.add(info);
			}

			return details;
		}
	}

	/**
	 * Retrieves the price plan change transaction details
	 */
	private class PricePlanChangeHelper implements DetailHelper {

		public String getSql() {
			return GET_PP_CHANGE_DETAILS_SQL;
		}

		public List<InteractionDetail> read(ResultSet resultSet) throws SQLException {
			List<InteractionDetail> details = new ArrayList<InteractionDetail>();

			while (resultSet.next()) {
				PricePlanChangeInfo info = new PricePlanChangeInfo();
				info.setOldPricePlanCode(resultSet.getString("KB_SOC_OLD"));
				info.setNewPricePlanCode(resultSet.getString("KB_SOC_NEW"));

				details.add(info);
			}

			return details;
		}
	}

	/**
	 * Retrieves the phone number change transaction details
	 */
	private class PhoneChangeHelper implements DetailHelper {
		
		public String getSql() {
			return GET_PHONE_CHANGE_DETAILS_SQL;
		}

		public List<InteractionDetail> read(ResultSet resultSet) throws SQLException {
			List<InteractionDetail> details = new ArrayList<InteractionDetail>();

			while (resultSet.next()) {
				PhoneNumberChangeInfo info = new PhoneNumberChangeInfo();
				info.setOldPhoneNumber(resultSet.getString("OLD_PHONE_NUMBER"));
				info.setNewPhoneNumber(resultSet.getString("NEW_PHONE_NUMBER"));

				details.add(info);
			}

			return details;
		}
	}


	/**
	 * Retrieves the payment transaction details
	 */
	private class PaymentHelper implements DetailHelper {

		public String getSql() {
			return GET_PAYMENT_DETAILS_SQL;
		}

		public List<InteractionDetail> read(ResultSet resultSet) throws SQLException {
			List<InteractionDetail> details = new ArrayList<InteractionDetail>();

			while (resultSet.next()) {
				BillPaymentInfo info = new BillPaymentInfo();
				info.setPaymentMethod(resultSet.getString("PAYMENT_METHOD").charAt(0));
				info.setPaymentAmount(resultSet.getDouble("PAYMENT_AMOUNT"));

				details.add(info);
			}

			return details;
		}
	}


	/**
	 * Retrieves payment method change transaction details
	 */
	private class PayMethodChangeHelper implements DetailHelper {

		public String getSql() {
			return GET_PAYMENT_METHOD_CHANGE_DETAILS_SQL;
		}

		public List<InteractionDetail> read(ResultSet resultSet) throws SQLException {
			List<InteractionDetail> details = new ArrayList<InteractionDetail>();

			while (resultSet.next()) {
				PaymentMethodChangeInfo info = new PaymentMethodChangeInfo();
				info.setOldPaymentMethod(resultSet.getString("OLD_PAYMENT_METHOD").charAt(0));
				info.setNewPaymentMethod(resultSet.getString("NEW_PAYMENT_METHOD").charAt(0));

				details.add(info);
			}

			return details;
		}
	}

	/**
	 * Retrieves equipment change transaction details
	 */
	private class EquipmentHelper implements DetailHelper {

		public String getSql() {
			return GET_EQUIPMENT_CHANGE_DETAILS_SQL;
		}

		public List<InteractionDetail> read(ResultSet rs) throws SQLException {
			List<InteractionDetail> details = new ArrayList<InteractionDetail>();

			while (rs.next()) {
				EquipmentChangeInfo info = new EquipmentChangeInfo();
				mapToEquipmentChangeInfo(rs, info);
				details.add(info);
			}

			return details;
		}
	}

	/**
	 * Retrieves charge transaction details
	 */
	private class ChargeHelper implements DetailHelper {
		
		public String getSql() {
			return GET_CHARGE_DETAILS_SQL;
		}

		public List<InteractionDetail> read(ResultSet resultSet) throws SQLException {
			List<InteractionDetail> details = new ArrayList<InteractionDetail>();
			while (resultSet.next()) {
				SubscriberChargeInfo info = new SubscriberChargeInfo();
				info.setChargeCode(resultSet.getString("CHARGE_CODE"));
				info.setWaiverCode(resultSet.getString("WAIVER_CODE"));

				details.add(info);
			}

			return details;
		}
	}

	private class AddressHelper implements DetailHelper {
		public String getSql() {
			return GET_ADDRESS_DETAILS_SQL;
		}

		public List<InteractionDetail> read(ResultSet resultSet) throws SQLException {
			List<InteractionDetail> details = new ArrayList<InteractionDetail>();

			while (resultSet.next()) {
				AddressChangeInfo info = new AddressChangeInfo();
				info.setOldAddressLine(resultSet.getString("OLD_ADDRESS_LINE"));
				info.setOldCity(resultSet.getString("OLD_CITY"));
				info.setOldProvince(resultSet.getString("OLD_PROVINCE"));
				info.setOldCountry(resultSet.getString("OLD_COUNTRY"));
				info.setOldPostal(resultSet.getString("OLD_POSTAL"));

				info.setNewAddressLine(resultSet.getString("NEW_ADDRESS_LINE"));
				info.setNewCity(resultSet.getString("NEW_CITY"));
				info.setNewProvince(resultSet.getString("NEW_PROVINCE"));
				info.setNewCountry(resultSet.getString("NEW_COUNTRY"));
				info.setNewPostal(resultSet.getString("NEW_POSTAL"));
				details.add(info);
			}

			return details;
		}
	}

	/**
	 * Retrieves role change details
	 */
	private class RoleChangeHelper implements DetailHelper {

		public String getSql() {
			return GET_ROLE_CHANGE_DETAILS_SQL;
		}

		public List<InteractionDetail> read(ResultSet resultSet) throws SQLException {
			List<InteractionDetail> details = new ArrayList<InteractionDetail>();
			while (resultSet.next()) {
				RoleChangeInfo info = new RoleChangeInfo();
				info.setOldRole(resultSet.getString("OLD_ACCESS_ROLE_CD"));
				info.setNewRole(resultSet.getString("NEW_ACCESS_ROLE_CD"));

				details.add(info);
			}

			return details;
		}
	}
	
	/**
	 * Return the details for an address change interaction
	 * 
	 * @param id
	 *            -- The transaction id.
	 * @return List -- Instances of AddressChangeInfo
	 */
	private List<InteractionDetail> getDetails(long id, final DetailHelper helper)  throws ApplicationException {
		String sql = helper.getSql();
		Object[] sqlParams = { Long.valueOf(id) };
		return getServJdbcTemplate().query(sql, sqlParams, new ResultSetExtractor<List<InteractionDetail>>() {

			@Override
			public List<InteractionDetail> extractData(ResultSet result) throws SQLException, DataAccessException {
				List<InteractionDetail> resultList = helper.read(result);
				return resultList;
			}

		});
	}
	
	/**
	 * Returns a list of types for all the interaction detail associated with a
	 * given id.
	 * 
	 * @param long id
	 * @return List -- Instances of String
	 */
	private List<String> getDetailTypes(long id) throws ApplicationException  {

		String sql = GET_EXISTING_DETAILS_SQL;
		Long idObj = Long.valueOf(id);
		Object[] sqlParams = { idObj, idObj, idObj, idObj, idObj, idObj, idObj, idObj, idObj, idObj, idObj, idObj };

		return getServJdbcTemplate().query(sql, sqlParams, new ResultSetExtractor<List<String>>() {

			@Override
			public List<String> extractData(ResultSet result) throws SQLException, DataAccessException {
				List<String> resultList = new ArrayList<String>();
				while (result.next()) {
					resultList.add(result.getString(TYPES_FIELD));
				}
				return resultList;
			}

		});
	}
	
	/**
	 * Returns the list of Interaction objects selected by the given sql and sql
	 * parameters (for the prepared statement).
	 * 
	 * @param String sql            
	 * @param sqlParams
	 * @return Interaction[]
	 */
	private Interaction[] getTransHeader(String sql, Object[] sqlParams) throws ApplicationException {

		return getServJdbcTemplate().query(sql, sqlParams, new ResultSetExtractor<Interaction[]>() {

			@Override
			public Interaction[] extractData(ResultSet result) throws SQLException, DataAccessException {
				List<InteractionInfo> interactions = new ArrayList<InteractionInfo>();
				while (result.next()) {
					InteractionInfo info = new InteractionInfo();
					mapToInteractionInfo(result, info);
					interactions.add(info);
				}

				return interactions.toArray(new Interaction[interactions.size()]);
			}

		});
	}
	  
	private void mapToInteractionInfo(ResultSet resultSet, InteractionInfo interactionInfo) throws SQLException {
		interactionInfo.setId(resultSet.getLong("TRANSACTION_ID"));
		interactionInfo.setType(resultSet.getString("TRANSACTION_TYPE_CD"));
		interactionInfo.setDate(resultSet.getTimestamp("TRANSACTION_DATETIME"));

		String patronIdType = resultSet.getString("PATRON_ID_TYPE_CD");
		if (com.telus.eas.config.info.InteractionInfo.PATRON_ID_TYPE_SUB.equals(patronIdType)) {
			interactionInfo.setSubscriberId(resultSet.getString("PATRON_ID"));
		} else {
			interactionInfo.setSubscriberId(null);
		}

		interactionInfo.setOperatorId(resultSet.getObject("OPERATOR_ID") == null ? null : Integer.valueOf(resultSet.getInt("OPERATOR_ID")));
		interactionInfo.setApplicationId(resultSet.getString("APPLICATION_ID"));
		interactionInfo.setDealerCode(resultSet.getString("DEALER_CODE"));
		interactionInfo.setSalesRepCode(resultSet.getString("SALESREP_CODE"));
		interactionInfo.setReasonId(resultSet.getLong("CLIENT_STATE_REASON_ID"));
	}
	
	// ==================================================================
	// Reporting Methods.
	// ==================================================================
	private int getNextTransactionId() throws ApplicationException {
		String sql = "SELECT transaction_seq.NEXTVAL FROM DUAL";
		int txId = 0;
		
		try {
			txId = getServJdbcTemplate().query(sql, new ResultSetExtractor<Integer>() {

				@Override
				public Integer extractData(ResultSet result) throws SQLException, DataAccessException {
					if (result.next()) {
						return result.getInt(1);
					}
					throw new SQLException("getNextTransactionId() is unable to retrieve sequence value");
				}
			});
		} catch (Throwable t) {
			throw new ApplicationException(SystemCodes.CMB_CFGMGR_DAO, t.getMessage(), "", t);
		}
		
		return txId;
	}
	
	private ProductEquipmentHelper getProductEquipmentHelper() {
		if (productEquipmentHelperSvc == null) {
			productEquipmentHelperSvc = EJBUtil.getHelperProxy(ProductEquipmentHelper.class, EJBUtil.TELUS_CMBSERVICE_PRODUCT_EQUIPMENT_HELPER);
		}
		
		return productEquipmentHelperSvc;
	}
}
