package com.telus.cmb.subscriber.lifecyclehelper.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.TelusAPIException;
import com.telus.api.account.AccountManager;
import com.telus.api.account.Subscriber;
import com.telus.api.reference.SeatType;
import com.telus.api.resource.Resource;
import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.cmb.common.util.StringUtil;
import com.telus.cmb.subscriber.lifecyclehelper.dao.RollbackSubscriberDao;
import com.telus.cmb.subscriber.utilities.AppConfiguration;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.account.info.SubscriberIdentifierInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.subscriber.info.CDPDSubscriberInfo;
import com.telus.eas.subscriber.info.CommitmentInfo;
import com.telus.eas.subscriber.info.HSPAResourcesInfo;
import com.telus.eas.subscriber.info.HSPASubscriberInfo;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.eas.subscriber.info.IdenResourcesInfo;
import com.telus.eas.subscriber.info.LightWeightSubscriberInfo;
import com.telus.eas.subscriber.info.PCSSubscriberInfo;
import com.telus.eas.subscriber.info.PagerSubscriberInfo;
import com.telus.eas.subscriber.info.ResourceChangeHistoryInfo;
import com.telus.eas.subscriber.info.ResourceInfo;
import com.telus.eas.subscriber.info.SeatDataInfo;
import com.telus.eas.subscriber.info.SubscriberHistoryInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.SubscriptionPreferenceInfo;
import com.telus.eas.subscriber.info.SubscriptionRoleInfo;
import com.telus.eas.subscriber.info.TangoSubscriberInfo;
import com.telus.eas.utility.info.NumberGroupInfo;
import com.telus.eas.utility.info.NumberRangeInfo;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
/**
 * Retrievals of subscriber information
 * 
 *
 */
public class RollbackSubscriberDaoImpl extends MultipleJdbcDaoTemplateSupport implements RollbackSubscriberDao{

	private String dateFormatSt = "MM/dd/yyyy";
	private SimpleDateFormat dateFormat   =  new SimpleDateFormat(dateFormatSt);

	private final Logger LOGGER = Logger.getLogger(RollbackSubscriberDaoImpl.class);

	private NumberRangeInfo[] retainNumberRangesByNpaNXX(NumberRangeInfo[] numberRanges, String npaNxx) {

		NumberRangeInfo[]nbrRanges = null;

		if (numberRanges != null) {
			List<NumberRangeInfo> list = new ArrayList<NumberRangeInfo>(numberRanges.length);

			for (int i = 0; i < numberRanges.length; i++) {

				if (numberRanges[i].getNPANXX().equals(npaNxx) ) {
					list.add(numberRanges[i]);
					break ;
				}
			}
			nbrRanges = list.toArray(new NumberRangeInfo[list.size()]);
		}
		return  nbrRanges;
	}

	private NumberRangeInfo[]  retainNumberRangesByNpa(NumberRangeInfo[] numberRanges, String npa) {

		NumberRangeInfo[]nbrRanges = null;

		if (numberRanges != null) {
			List<NumberRangeInfo> list = new ArrayList<NumberRangeInfo>(numberRanges.length);

			for (int i = 0; i < numberRanges.length; i++) {

				if (numberRanges[i].getNPA()== Integer.valueOf(npa).intValue() ) {
					list.add(numberRanges[i]);
				}
			}
			nbrRanges = list.toArray(new NumberRangeInfo[list.size()]);
		}
		return  nbrRanges;
	}

	
	/**
	 * This method retrieves the subscriber ID and subscription ID (EXTERNAL_ID in KB) with the given input. The underlying query may
	 * return more than 1 result but this method will execute the procedure that always returns the first record only, which is supposed
	 * to be the latest one.
	 * 
	 * The current logic indicates that if a BAN is passed in, include the cancelled subscriber.
	 * If the BAN is not set, do not include cancelled subscriber. 
	 * 
	 * @param ban  optional
	 * @param phoneNumber   required. Validation is done to make sure the phone number is not empty.
	 * @return SubscriberIdentifierInfo
	 * @throws ApplicationException
	 */
	@Override
	public SubscriberIdentifierInfo retrieveLatestSubscriberIdentifierInfoByBanAndPhoneNumber(final int ban, final String phoneNumber) throws ApplicationException {
		if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
			throw new ApplicationException(SystemCodes.CMB_SLH_DAO, "Invalid phoneNumber input [" + phoneNumber + "]", "");
		}
		final String callableStmt = "{call SUB_ATTRIB_RETRIEVAL_PKG.getIdentifierByBanAndPhone(?, ?, ?, ?)}";

		return super.getKnowbilityJdbcTemplate().execute(callableStmt, new CallableStatementCallback<SubscriberIdentifierInfo>() {
			@Override
			public SubscriberIdentifierInfo doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				SubscriberIdentifierInfo subscriberIdentifierInfo = new SubscriberIdentifierInfo();
				ResultSet rs = null;

				String formattedPhoneNumber = AttributeTranslator.replaceString(phoneNumber.trim(), " ", "");
				formattedPhoneNumber = AttributeTranslator.replaceString(formattedPhoneNumber.trim(), "-", "");

				// set/register input/output parameters
				callable.setInt(1, ban);
				callable.setString(2, formattedPhoneNumber);
				callable.setInt(3, (ban == 0) ? AccountManager.NUMERIC_FALSE : AccountManager.NUMERIC_TRUE);
				callable.registerOutParameter(4, OracleTypes.CURSOR);

				try {
					callable.execute();

					rs = (ResultSet) callable.getObject(4);

					if (rs.next()) {
						subscriberIdentifierInfo.setSubscriberId(rs.getString("subscriber_no"));
						subscriberIdentifierInfo.setSubscriptionId(rs.getLong("external_id"));
						subscriberIdentifierInfo.setSeatType(rs.getString("seat_type"));
						subscriberIdentifierInfo.setSeatGroup(rs.getString("seat_group"));
						//subscriberIdentifierInfo.setBrandId(rs.getInt("brand_id"));
					}
				} finally {
					if (rs != null) {
						rs.close();
					}
					subscriberIdentifierInfo.setBan(ban);
					subscriberIdentifierInfo.setPhoneNumber(formattedPhoneNumber);
				}

				return subscriberIdentifierInfo;
			}
		});
	}

	
	/**
	 * Retrieves subscriber list from KB data source by BAN, maximum subscriber count to be returned and flag indicating if 
	 * cancelled subscribers should be included in the list.
	 * 
	 * @param ban
	 * @param maximumCount 0 means ALL, but it has to be within the limit. There's consumer passing 0 today.
	 * @param includeCancelled
	 * @return
	 */
	@Override
	public Collection<SubscriberInfo> retrieveSubscriberListByBAN(final int ban, int maximumCount, final boolean includeCancelled) {
		LOGGER.debug("retrieveSubscriberListByBAN ban=["+ban+"] maximumCount=["+maximumCount+"] includeCancelled=["+includeCancelled+"]");
		if (ban <= 0) {
			return new ArrayList<SubscriberInfo>() ;
		}
		String callableStmt = "{call SUB_RETRIEVAL_PKG.getSubListByBAN(?, ?, ?, ?, ?)}";
		final int resultSetMaxCount = getActualMaxSubLimit(maximumCount);

		return super.getKnowbilityJdbcTemplate().execute(callableStmt, new CallableStatementCallback<Collection<SubscriberInfo>>() {
			@Override
			public Collection<SubscriberInfo> doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {

				Collection<SubscriberInfo> subscriberList = null;
				ResultSet result = null;

				try {
					// set/register input/output parameters
					callable.setInt(1, ban);
					callable.setInt(2, includeCancelled ? AccountManager.NUMERIC_TRUE : AccountManager.NUMERIC_FALSE);
					callable.setInt(3, resultSetMaxCount);
					callable.registerOutParameter(4, OracleTypes.CURSOR);
					callable.registerOutParameter(5, OracleTypes.VARCHAR);

					callable.execute();

					result = (ResultSet) callable.getObject(4);
					if (result != null) {
						subscriberList = convertSubscribersResultSet(callable.getConnection(), result, resultSetMaxCount,includeCancelled);
					}
				} finally {
					if (result != null) {
						result.close();
					}
				}
				return subscriberList;
			}
		});
	}

	/**
	 * Retrieves subscriber list from KB data source by BAN, subscriber ID, 
	 * maximum subscriber count to be returned and flag indicating if 
	 * cancelled subscribers should be included in the list.
	 * 
	 * @param ban required
	 * @param subscriberId required
	 * @param includeCancelled
	 * @param subId
	 * @return Collection<SubscriberInfo>  Collection of subscriber info object.  The list size will be based on the maximumCount parameter
	 */
	@Override
	public Collection<SubscriberInfo> retrieveSubscriberListByBANAndSubscriberId(final int ban, final String subscriberId, final boolean includeCancelled, int maximumCount) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("retrieveSubscriberListByBANAndSubscriberId ban=["+ban+"] subscriberId=["+subscriberId+"] maximumCount=["+maximumCount+"] includeCancelled=["+includeCancelled+"]");
		}
		
		if (ban <= 0 || subscriberId == null || subscriberId.trim().isEmpty()) {
			return new ArrayList<SubscriberInfo>() ;
		}
		
		if (AppConfiguration.isWRPPh3Rollback()) {
			String callableStmt = "{call SUB_RETRIEVAL_PKG.getSubListByBANAndSubID(?, ?, ?, ?, ?, ?)}";
			final int resultSetMaxCount = getActualMaxSubLimit(maximumCount);

			return super.getKnowbilityJdbcTemplate().execute(callableStmt, new CallableStatementCallback<Collection<SubscriberInfo>>() {
				@Override
				public Collection<SubscriberInfo> doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {

					Collection<SubscriberInfo> subscriberList = null;
					ResultSet result = null;

					try {
						// set/register input/output parameters
						callable.setInt(1, ban);
						callable.setString(2, subscriberId);
						callable.setInt(3, includeCancelled ? AccountManager.NUMERIC_TRUE : AccountManager.NUMERIC_FALSE);
						callable.setInt(4, resultSetMaxCount);
						callable.registerOutParameter(5, OracleTypes.CURSOR);
						callable.registerOutParameter(6, OracleTypes.VARCHAR);

						callable.execute();

						result = (ResultSet) callable.getObject(5);
						subscriberList = convertSubscribersResultSet(callable.getConnection(), result, resultSetMaxCount, includeCancelled);
					
					} finally {
						if (result != null) {
							result.close();
						}
					}
					return subscriberList;
				}
			});
		} else {
			String sql = null;
			
			if (includeCancelled) {
				sql = " SELECT * FROM (SELECT DISTINCT s.customer_id, s.subscriber_no, s.sub_status, nd.first_name, nd.middle_initial, nd.last_business_name, pd.unit_esn, "
                    + " s.product_type, SUBSTR (s.dealer_code, 1, 10), sa.soc, s.email_address, NVL (s.init_activation_date, s.effective_date), s.init_activation_date, "
                    + " SUBSTR (s.dealer_code, 11), s.sub_status_rsn_code, s.sub_status_last_act, s.sub_alias, DECODE (INSTR (user_seg, '@'), 0, '', SUBSTR (user_seg, INSTR (user_seg, '@') + 1, 1)), "
                    + " sub_lang_pref, s.next_ctn, s.next_ctn_chg_date, s.prv_ctn, s.prv_ctn_chg_date, DECODE (s.sub_status, 'S', 'B', s.sub_status), s.tax_gst_exmp_ind, s.tax_pst_exmp_ind, "
                    + " s.tax_hst_exmp_ind, s.tax_gst_exmp_eff_dt, s.tax_pst_exmp_eff_dt, s.tax_hst_exmp_eff_dt, s.tax_gst_exmp_exp_dt, s.tax_pst_exmp_exp_dt, s.tax_hst_exmp_exp_dt, "
                    + " s.tax_gst_exmp_rf_no, s.tax_pst_exmp_rf_no, s.tax_hst_exmp_rf_no, s.sub_status_date, s.calls_sort_order, s.commit_reason_code, s.commit_orig_no_month, s.commit_start_date, "
                    + " s.commit_end_date, nd.name_suffix, nd.additional_title, nd.name_title, s.hot_line_ind, nd.name_format, s.migration_type, s.migration_date, s.tenure_date, s.req_deposit_amt, "
                    + " s.port_type, s.port_date, s.brand_id, NVL (s.external_id, 0) AS subscription_id, s.seat_type, s.seat_group "
                    + " FROM subscriber s, address_name_link anl, name_data nd, physical_device pd, service_agreement sa "
                    + " WHERE s.customer_id = :ban AND (s.subscriber_no IN (:subscriberIdList)) AND s.sub_status = 'C' AND anl.ban(+) = s.customer_id AND anl.subscriber_no(+) = s.subscriber_no "
                    + " AND anl.expiration_date IS NULL AND nd.name_id(+) = anl.name_id AND sa.ban = s.customer_id AND sa.subscriber_no = s.subscriber_no AND sa.product_type = s.product_type "
                    + " AND sa.service_type = 'P' AND sa.soc_seq_no = (SELECT MAX (sa1.soc_seq_no) FROM service_agreement sa1 WHERE sa1.ban = sa.ban AND sa1.subscriber_no = sa.subscriber_no "
                    + " AND sa1.product_type = sa.product_type AND sa1.service_type = 'P') AND pd.customer_id = s.customer_id AND pd.subscriber_no = s.subscriber_no AND pd.product_type = s.product_type "
                    + " AND pd.esn_seq_no = (SELECT MAX (esn_seq_no) FROM physical_device pd1 WHERE pd1.customer_id = pd.customer_id AND pd1.subscriber_no = pd.subscriber_no AND pd1.product_type = "
                    + " pd.product_type AND NVL (pd1.esn_level, 1) = 1) "
                    + " UNION ALL "
                    + " SELECT DISTINCT s.customer_id, s.subscriber_no, s.sub_status, nd.first_name, nd.middle_initial, nd.last_business_name, pd.unit_esn, s.product_type, SUBSTR (s.dealer_code, 1, 10), "
                    + " sa.soc, s.email_address, NVL (s.init_activation_date, s.effective_date), s.init_activation_date, SUBSTR (s.dealer_code, 11), s.sub_status_rsn_code, s.sub_status_last_act, "
                    + " s.sub_alias, DECODE (INSTR (user_seg, '@'), 0, '', SUBSTR (user_seg, INSTR (user_seg, '@') + 1, 1)), sub_lang_pref, s.next_ctn, s.next_ctn_chg_date, s.prv_ctn, s.prv_ctn_chg_date, "
                    + " DECODE (s.sub_status, 'S', 'B', s.sub_status), s.tax_gst_exmp_ind, s.tax_pst_exmp_ind, s.tax_hst_exmp_ind, s.tax_gst_exmp_eff_dt, s.tax_pst_exmp_eff_dt, s.tax_hst_exmp_eff_dt, "
                    + " s.tax_gst_exmp_exp_dt, s.tax_pst_exmp_exp_dt, s.tax_hst_exmp_exp_dt, s.tax_gst_exmp_rf_no, s.tax_pst_exmp_rf_no, s.tax_hst_exmp_rf_no, s.sub_status_date, s.calls_sort_order, "
                    + " s.commit_reason_code, s.commit_orig_no_month, s.commit_start_date, s.commit_end_date, nd.name_suffix, nd.additional_title, nd.name_title, s.hot_line_ind, nd.name_format, "
                    + " s.migration_type, s.migration_date, s.tenure_date, s.req_deposit_amt, s.port_type, s.port_date, s.brand_id, NVL (s.external_id, 0) AS subscription_id, s.seat_type, s.seat_group "
                    + " FROM subscriber s, address_name_link anl, name_data nd, physical_device pd, service_agreement sa, logical_date ld "
                    + " WHERE  s.customer_id = :ban AND (s.subscriber_no IN (:subscriberIdList)) AND s.sub_status != 'C' AND anl.ban(+) = s.customer_id AND anl.subscriber_no(+) = s.subscriber_no "
                    + " AND anl.expiration_date IS NULL AND nd.name_id(+) = anl.name_id AND sa.ban = s.customer_id AND sa.subscriber_no = s.subscriber_no AND sa.product_type = s.product_type "
                    + " AND sa.service_type = 'P' AND (TRUNC(sa.expiration_date) > TRUNC(ld.logical_date) OR sa.expiration_date IS NULL) AND ld.logical_date_type = 'O' AND sa.effective_date = "
                    + " (SELECT MIN (sa1.effective_date) FROM service_agreement sa1, logical_date ld WHERE sa1.ban = sa.ban AND sa1.subscriber_no = sa.subscriber_no AND sa1.product_type = sa.product_type "
                    + " AND sa1.service_type = 'P' AND (TRUNC (sa1.expiration_date) > TRUNC (ld.logical_date) OR sa1.expiration_date IS NULL) AND ld.logical_date_type = 'O') AND pd.customer_id = s.customer_id "
                    + " AND pd.subscriber_no = s.subscriber_no AND pd.product_type = s.product_type AND pd.esn_level = 1 AND pd.expiration_date IS NULL ORDER BY 24, 12 DESC) WHERE ROWNUM <= :maxCount";
			} else {
				sql = " SELECT * FROM (SELECT DISTINCT s.customer_id, s.subscriber_no, s.sub_status, nd.first_name, nd.middle_initial, nd.last_business_name, pd.unit_esn, "
	                    + " s.product_type, SUBSTR (s.dealer_code, 1, 10), sa.soc, s.email_address, NVL (s.init_activation_date, s.effective_date), s.init_activation_date, "
	                    + " SUBSTR (s.dealer_code, 11), s.sub_status_rsn_code, s.sub_status_last_act, s.sub_alias, DECODE (INSTR (user_seg, '@'), 0, '', SUBSTR (user_seg, INSTR (user_seg, '@') + 1, 1)), "
	                    + " sub_lang_pref, s.next_ctn, s.next_ctn_chg_date, s.prv_ctn, s.prv_ctn_chg_date, DECODE (s.sub_status, 'S', 'B', s.sub_status), s.tax_gst_exmp_ind, s.tax_pst_exmp_ind, "
	                    + " s.tax_hst_exmp_ind, s.tax_gst_exmp_eff_dt, s.tax_pst_exmp_eff_dt, s.tax_hst_exmp_eff_dt, s.tax_gst_exmp_exp_dt, s.tax_pst_exmp_exp_dt, s.tax_hst_exmp_exp_dt, "
	                    + " s.tax_gst_exmp_rf_no, s.tax_pst_exmp_rf_no, s.tax_hst_exmp_rf_no, s.sub_status_date, s.calls_sort_order, s.commit_reason_code, s.commit_orig_no_month, s.commit_start_date, "
	                    + " s.commit_end_date, nd.name_suffix, nd.additional_title, nd.name_title, s.hot_line_ind, nd.name_format, s.migration_type, s.migration_date, s.tenure_date, s.req_deposit_amt, "
	                    + " s.port_type, s.port_date, s.brand_id, NVL (s.external_id, 0) AS subscription_id, s.seat_type, s.seat_group "
	                    + " FROM subscriber s, address_name_link anl, name_data nd, physical_device pd, service_agreement sa, logical_date ld "
	                    + " WHERE s.customer_id = :ban AND (s.subscriber_no IN(:subscriberIdList)) AND s.sub_status != 'C' AND anl.ban(+) = s.customer_id AND anl.subscriber_no(+) = s.subscriber_no "
                        + " AND anl.expiration_date IS NULL AND nd.name_id(+) = anl.name_id AND sa.ban = s.customer_id AND sa.subscriber_no = s.subscriber_no AND sa.product_type = s.product_type "
                        + " AND sa.service_type = 'P' AND (TRUNC (sa.expiration_date) > TRUNC (ld.logical_date) OR sa.expiration_date IS NULL) AND ld.logical_date_type = 'O' AND sa.effective_date = "
                        + " (SELECT MIN (sa1.effective_date) FROM service_agreement sa1, logical_date ld WHERE sa1.ban = sa.ban AND sa1.subscriber_no = sa.subscriber_no AND sa1.product_type = sa.product_type "
                        + " AND sa1.service_type = 'P' AND (TRUNC (sa1.expiration_date) > TRUNC (ld.logical_date) OR sa1.expiration_date IS NULL) AND ld.logical_date_type = 'O') AND pd.customer_id = s.customer_id "
                        + " AND pd.subscriber_no = s.subscriber_no AND pd.product_type = s.product_type AND pd.esn_level = 1 AND pd.expiration_date IS NULL ORDER BY 24, 12 DESC) WHERE ROWNUM <= :maxCount";          
			}
			
			final int resultSetMaxCount = getActualMaxSubLimit(maximumCount);
			List<String> subscriberIdList = new ArrayList<String>();
			subscriberIdList.add(subscriberId);
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("ban", ban);
			namedParameters.addValue("subscriberIdList", subscriberIdList);
			namedParameters.addValue("maxCount", resultSetMaxCount);
				
			return (Collection<SubscriberInfo>) getKnowbilityNamedParameterJdbcTemplate().execute(sql, namedParameters, new PreparedStatementCallback<Collection<SubscriberInfo>>() {
				@Override
				public Collection<SubscriberInfo> doInPreparedStatement(PreparedStatement pstmt) throws SQLException, DataAccessException {
					Collection<SubscriberInfo> subscriberList = null;
					ResultSet rs = null;
					
					try {
						rs = pstmt.executeQuery();
						subscriberList = convertSubscribersResultSet(pstmt.getConnection(), rs, resultSetMaxCount, includeCancelled);
					} finally {
						if (rs != null) {
							rs.close();
						}
					}
					return subscriberList;
				};
			});
		}
	}
	

	@Override
	public Collection<SubscriberInfo> retrieveSubscriberListBySubscriberID(final String subscriberId, final boolean includeCancelled, int maximumCount) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("retrieveSubscriberListBySubscriberID subscriberId=["+subscriberId+"] maximumCount=["+maximumCount+"] includeCancelled=["+includeCancelled+"]");
		}
		if (subscriberId == null || subscriberId.trim().isEmpty()) {
			return new ArrayList<SubscriberInfo>() ;
		}
		
		String callableStmt = "{call SUB_RETRIEVAL_PKG.getSubListBySubID(?, ?, ?, ?, ?)}";
		final int resultSetMaxCount = getActualMaxSubLimit(maximumCount);

		return super.getKnowbilityJdbcTemplate().execute(callableStmt, new CallableStatementCallback<Collection<SubscriberInfo>>() {
			@Override
			public Collection<SubscriberInfo> doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {

				Collection<SubscriberInfo> subscriberList = null;
				ResultSet result = null;

				try {
					// set/register input/output parameters
					callable.setString(1, subscriberId);
					callable.setInt(2, includeCancelled ? AccountManager.NUMERIC_TRUE : AccountManager.NUMERIC_FALSE);
					callable.setInt(3, resultSetMaxCount);
					callable.registerOutParameter(4, OracleTypes.CURSOR);
					callable.registerOutParameter(5, OracleTypes.VARCHAR);

					callable.execute();
					
					result = (ResultSet) callable.getObject(4);
					subscriberList = convertSubscribersResultSet(callable.getConnection(), result, resultSetMaxCount, includeCancelled);
					
				} finally {
					if (result != null) {
						result.close();
					}
				}
				return subscriberList;
			}
		});
	}
	
	

	private Collection<SubscriberInfo> convertSubscribersResultSet(final ResultSet result, final int maximum) throws SQLException {
		return super.getKnowbilityJdbcTemplate().execute(new ConnectionCallback<Collection<SubscriberInfo>> () {

			@Override
			public Collection<SubscriberInfo> doInConnection(Connection connection) throws SQLException, DataAccessException {
				return convertSubscribersResultSet(connection, result, maximum,false);
			}
			
		});
	}
	

	/**
	 * Converts subscriber result set into Collection of SubscriberInfo objects
	 * 
	 * @param kbConnection - connection to knowbility database
	 * @param result
	 * @param maximum
	 * @return Collection<SubscriberInfo> collection of subscriber info objects
	 * @throws SQLException
	 */
	private Collection<SubscriberInfo> convertSubscribersResultSet(Connection kbConnection, ResultSet result, int maximum,boolean includeCanceledSeatResources) throws SQLException {

		// return object
		Collection<SubscriberInfo> subscriberList = new ArrayList<SubscriberInfo>();
		Hashtable<String,SubscriberInfo> subscriberMap = new Hashtable<String, SubscriberInfo>();
		ArrayList<String> phoneNumberList = new ArrayList<String>();
		ArrayList<String> secSubscriberIdList = new ArrayList<String>();
		ArrayList<String> secBanIdList = new ArrayList<String>();
		ArrayList<String> idenSubscriberIdList = new ArrayList<String>();
		ArrayList<String> idenBanIdList = new ArrayList<String>();
		ArrayList<String> hspaSubscriberIdList = new ArrayList<String>();
		ArrayList<String> hspaBanIdList = new ArrayList<String>();

		int counter = 0;
		
		while (result.next()) {		
			if (maximum > 0 && counter++ >= maximum) break;

			SubscriberInfo subscriberInfo = null;

			final int banId = result.getInt(1);
			final String subscriberId = result.getString(2);
			final String productType = result.getString(8);
			final String serialNumber = result.getString(7);

			if (Subscriber.PRODUCT_TYPE_IDEN.equals(productType)) {
				subscriberInfo = new IDENSubscriberInfo();
				((IDENSubscriberInfo) subscriberInfo).setSubscriberAlias(result.getString(17) == null ? "" : result.getString(17));

				idenSubscriberIdList.add(subscriberId);
				idenBanIdList.add(String.valueOf(banId));
			}
			else if (Subscriber.PRODUCT_TYPE_PAGER.equals(productType)) {
				subscriberInfo = new PagerSubscriberInfo();
				subscriberInfo.setPhoneNumber(subscriberId);

				PagerSubscriberInfo pagerSubscriberInfo = (PagerSubscriberInfo) subscriberInfo;
				pagerSubscriberInfo.setNextPhoneNumber(result.getString(20));
				pagerSubscriberInfo.setNextPhoneNumberChangeDate(result.getDate(21));

				secSubscriberIdList.add(subscriberId);
				secBanIdList.add(String.valueOf(banId));
				phoneNumberList.add(subscriberId);
			}
			else if (Subscriber.PRODUCT_TYPE_PCS.equals(productType)) {

				if (serialNumber.equals(EquipmentInfo.DUMMY_ESN_FOR_USIM)) {
					subscriberInfo = new HSPASubscriberInfo();  
					subscriberInfo.setHasDummyESN(true);  
					hspaSubscriberIdList.add(subscriberId);
					hspaBanIdList.add(String.valueOf(banId)); 	  
				} 
				else {
					subscriberInfo = new PCSSubscriberInfo(); 
				}

				subscriberInfo.setPhoneNumber(subscriberId);
				if (result.getString("seat_type") != null) {
					SeatDataInfo seatInfo = new SeatDataInfo();
					seatInfo.setSeatGroup(result.getString("seat_group"));
					seatInfo.setSeatType(result.getString("seat_type"));
					if (SeatType.SEAT_TYPE_MOBILE.equals(result.getString("seat_type")) == false) {
						try {
							List<ResourceInfo> seatResourceList= retrieveSeatResourceInfoByBanAndPhoneNumber(banId, subscriberId,includeCanceledSeatResources);
							for (ResourceInfo resourceInfo : seatResourceList) {
								seatInfo.addSeatResource(resourceInfo);
							}
							
						} catch (ApplicationException e) {
							throw new SQLException(e);
						}
					}
					
					subscriberInfo.setSeatData(seatInfo);
					
				}
				PCSSubscriberInfo pcsSubscriberInfo = (PCSSubscriberInfo) subscriberInfo;
				pcsSubscriberInfo.setNextPhoneNumber(result.getString(20));
				pcsSubscriberInfo.setNextPhoneNumberChangeDate(result.getDate(21));
				pcsSubscriberInfo.setPreviousPhoneNumber(result.getString(22));
				pcsSubscriberInfo.setPreviousPhoneNumberChangeDate(result.getDate(23));
				pcsSubscriberInfo.setFidoConversion(result.getString(17) != null && result.getString(17).startsWith("FIDO"));

				secSubscriberIdList.add(subscriberId);
				secBanIdList.add(String.valueOf(banId));
				phoneNumberList.add(subscriberId);
			}
			else if (Subscriber.PRODUCT_TYPE_TANGO.equals(productType)) {
				subscriberInfo = new TangoSubscriberInfo();
				subscriberInfo.setPhoneNumber(subscriberId);

				TangoSubscriberInfo tangoSubscriberInfo = (TangoSubscriberInfo) subscriberInfo;
				tangoSubscriberInfo.setNextPhoneNumber(result.getString(20));
				tangoSubscriberInfo.setNextPhoneNumberChangeDate(result.getDate(21));

				phoneNumberList.add(subscriberId);
			}
			else if (Subscriber.PRODUCT_TYPE_CDPD.equals(productType)) {
				subscriberInfo = new CDPDSubscriberInfo();
				subscriberInfo.setPhoneNumber("");
			}else {
				//TODO: throw ApplicationException
				LOGGER.error("Unsupported product type ["+productType+"] for subscriber ["+banId+"/"+subscriberId+"].");
				continue; //skip this subscriber if the product type is not supported. If not, NullPointerException will follow since subscriberInfo is not initialized
			}

			subscriberInfo.setBanId(banId);
			subscriberInfo.setSubscriberId(subscriberId);
			subscriberInfo.setStatus(result.getString(3).toCharArray()[0]);
			subscriberInfo.getConsumerName().setFirstName(result.getString(4));
			subscriberInfo.getConsumerName().setMiddleInitial(result.getString(5));
			subscriberInfo.getConsumerName().setLastName(result.getString(6));
			subscriberInfo.getConsumerName().setTitle(result.getString("name_title"));
			subscriberInfo.getConsumerName().setAdditionalLine(result.getString("additional_title"));
			subscriberInfo.getConsumerName().setGeneration(result.getString("name_suffix"));
			subscriberInfo.getConsumerName().setNameFormat(result.getString("name_format"));
			subscriberInfo.setSerialNumber(serialNumber);

			subscriberInfo.setProductType(result.getString(8));
			subscriberInfo.setDealerCode(result.getString(9));
			subscriberInfo.setPricePlan(result.getString(10));
			subscriberInfo.setEmailAddress(result.getString(11));
			subscriberInfo.setCreateDate(result.getTimestamp(12));
			if (result.getDate("tenure_date") != null) {
				subscriberInfo.setStartServiceDate(result.getDate("tenure_date"));
			}  else {
				subscriberInfo.setStartServiceDate(result.getDate("init_activation_date"));
			}
			subscriberInfo.setSalesRepId(result.getString(14));
			subscriberInfo.setActivityReasonCode(result.getString(15));
			subscriberInfo.setActivityCode(result.getString(16));
			subscriberInfo.setUserValueRating(result.getString(18) == null ? "" : result.getString(18));
			subscriberInfo.setLanguage(result.getString(19));
			subscriberInfo.isGSTExempt("Y".equals(result.getString("tax_gst_exmp_ind")));
			subscriberInfo.isPSTExempt("Y".equals(result.getString("tax_pst_exmp_ind")));
			subscriberInfo.isHSTExempt("Y".equals(result.getString("tax_hst_exmp_ind")));
			subscriberInfo.setGSTExemptEffectiveDate(result.getDate("tax_gst_exmp_eff_dt"));
			subscriberInfo.setPSTExemptEffectiveDate(result.getDate("tax_pst_exmp_eff_dt"));
			subscriberInfo.setHSTExemptEffectiveDate(result.getDate("tax_hst_exmp_eff_dt"));
			subscriberInfo.setGSTExemptionExpiryDate(result.getDate("tax_gst_exmp_exp_dt"));
			subscriberInfo.setPSTExemptionExpiryDate(result.getDate("tax_pst_exmp_exp_dt"));
			subscriberInfo.setHSTExemptionExpiryDate(result.getDate("tax_hst_exmp_exp_dt"));
			subscriberInfo.setGSTCertificateNumber(result.getString("tax_gst_exmp_rf_no"));
			subscriberInfo.setPSTCertificateNumber(result.getString("tax_pst_exmp_rf_no"));
			subscriberInfo.setHSTCertificateNumber(result.getString("tax_hst_exmp_rf_no"));
			subscriberInfo.setStatusDate(result.getDate("sub_status_date"));
			subscriberInfo.setInvoiceCallSortOrderCode(result.getString("calls_sort_order"));
			subscriberInfo.setHotlined(result.getString("hot_line_ind"));
			subscriberInfo.setMigrationTypeCode(result.getString("migration_type") == null ? "" : result.getString("migration_type").trim());
			subscriberInfo.setMigrationDate(result.getDate("migration_date"));

			CommitmentInfo commitment = new CommitmentInfo();
			commitment.setReasonCode(result.getString("commit_reason_code"));
			commitment.setMonths(result.getInt("commit_orig_no_month"));
			commitment.setStartDate(result.getDate("commit_start_date"));
			commitment.setEndDate(result.getDate("commit_end_date"));
			subscriberInfo.setCommitment(commitment);

			subscriberInfo.setSecurityDeposit(result.getDouble("req_deposit_amt"));
			subscriberInfo.setPortType(result.getString("port_type"));
			subscriberInfo.setPortDate(result.getDate("port_date"));

			subscriberInfo.setBrandId((result.getInt("brand_id")));
			subscriberInfo.setSubscriptionId(result.getLong("subscription_id"));

			subscriberList.add(subscriberInfo);
			subscriberMap.put(subscriberId + "_" + String.valueOf(banId), subscriberInfo);
		}

		if (secSubscriberIdList.size() > 0) {
			String[] subscriberIdArray = new String[secSubscriberIdList.size()];
			subscriberIdArray = secSubscriberIdList.toArray(subscriberIdArray);

			int[] banIdArray = new int[secBanIdList.size()];
			for (int i = 0; i < banIdArray.length; i++)
				banIdArray[i] = Integer.parseInt(secBanIdList.get(i));

			Hashtable<String,String[]> secondarySerialNumbers = retrieveSecondarySerialNumbers(kbConnection, banIdArray, subscriberIdArray);

			Enumeration<String> subscriberIds = secondarySerialNumbers.keys();

			while (subscriberIds.hasMoreElements()) {
				String subscriberId = subscriberIds.nextElement();
				subscriberMap.get(subscriberId).setSecondarySerialNumbers(secondarySerialNumbers.get(subscriberId));
			}
		}

		if (idenSubscriberIdList.size() > 0) {
			String[] subscriberIdArray = new String[idenSubscriberIdList.size()];
			subscriberIdArray = idenSubscriberIdList.toArray(subscriberIdArray);

			int[] banIdArray = new int[idenBanIdList.size()];
			for (int i = 0; i < banIdArray.length; i++)
				banIdArray[i] = Integer.parseInt(idenBanIdList.get(i));

			Hashtable<String,IdenResourcesInfo> idenResources = retrieveIdenResources(kbConnection, banIdArray, subscriberIdArray);

			Enumeration<String> subscriberIds = idenResources.keys();

			while (subscriberIds.hasMoreElements()) {
				String subscriberId = subscriberIds.nextElement();
				IDENSubscriberInfo subscriberInfo = (IDENSubscriberInfo) subscriberMap.get(subscriberId);

				IdenResourcesInfo idenResourcesInfo = idenResources.get(subscriberId);
				subscriberInfo.getFleetIdentity().setUrbanId(idenResourcesInfo.getUrbanId());
				subscriberInfo.getFleetIdentity().setFleetId(idenResourcesInfo.getFleetId());
				subscriberInfo.getMemberIdentity0().getFleetIdentity0().setUrbanId(idenResourcesInfo.getUrbanId());
				subscriberInfo.getMemberIdentity0().getFleetIdentity0().setFleetId(idenResourcesInfo.getFleetId());
				subscriberInfo.getMemberIdentity0().setMemberId(idenResourcesInfo.getMemberId());
				subscriberInfo.getMemberIdentity0().setResourceStatus(idenResourcesInfo.getResourceStatus());
				subscriberInfo.setIPAddress(idenResourcesInfo.getIp());
				subscriberInfo.setPhoneNumber(idenResourcesInfo.getPhoneNumber());
				subscriberInfo.setIMSI(idenResourcesInfo.getImsi());

				if (idenResourcesInfo.getPhoneNumber() != null) { //to prevent bad subscriber data with no phone number (resource type ='N') {
					phoneNumberList.add(idenResourcesInfo.getPhoneNumber());
				}
			}
		}

		// HSPA

		if (hspaSubscriberIdList.size() > 0) {
			String[] subscriberIdArray = new String[hspaSubscriberIdList.size()];
			subscriberIdArray = hspaSubscriberIdList.toArray(subscriberIdArray);

			int[] banIdArray = new int[hspaBanIdList.size()];
			for (int i = 0; i < banIdArray.length; i++) {
				banIdArray[i] = Integer.parseInt(hspaBanIdList.get(i));
			}

			if (banIdArray.length > 10 || subscriberIdArray.length > 10) {
				LOGGER.info("Large ban or subscriberId size for retrieveHSPAResources ["+banIdArray.length +"/"+ subscriberIdArray.length+"]");
			}
			Hashtable<String, HSPAResourcesInfo> hspaResources = retrieveHSPAResources(kbConnection, banIdArray, subscriberIdArray);
						
			Enumeration<String> subscriberIds = hspaResources.keys();

			while (subscriberIds.hasMoreElements()) {
				String subscriberId = subscriberIds.nextElement();			
				SubscriberInfo subscriberInfo = subscriberMap.get(subscriberId);
				
				if (subscriberInfo instanceof HSPASubscriberInfo) {
					HSPASubscriberInfo hspaSubscriberInfo = (HSPASubscriberInfo) subscriberInfo;
					HSPAResourcesInfo hspaResourcesInfo = (HSPAResourcesInfo) hspaResources.get(subscriberId);
					hspaSubscriberInfo.setHspaImsi(hspaResourcesInfo.getHspaImsi());
					hspaSubscriberInfo.setPhoneNumber(hspaResourcesInfo.getPhoneNumber());
				}else {
					LOGGER.error("SubscriberInfo is instanceof " + subscriberInfo.getClass() + " while expecting HSPASubscriberInfo for key="+subscriberId);
				}

			}
		}

		if (phoneNumberList.size() > 0) {
			String[] phoneNumberArray = new String[phoneNumberList.size()];
			phoneNumberArray = phoneNumberList.toArray(phoneNumberArray);

			Hashtable<String,String> marketProvinces = retrieveMarketProvincesByPhoneNumbers(kbConnection, phoneNumberArray);

			for (Iterator<SubscriberInfo> iter=subscriberList.iterator(); iter.hasNext();) {
				SubscriberInfo subscriberInfo = iter.next();
				if (subscriberInfo.getPhoneNumber() != null) { //to prevent bad subscriber data with no phone number (resource type ='N')
					subscriberInfo.setMarketProvince(marketProvinces.get(subscriberInfo.getPhoneNumber()));
				}
			}
		}

		return subscriberList;
	}

	/**
	 * Retrieves IDEN resources for given subscribers.
	 *
	 * @param subscriberIds Array of subscribers.
	 * @return Hashtable where instances of <tt>IdenResourcesInfo</tt> are accessed by subscriber IDs.
	 * @see IdenResourcesInfo
	 */
	private Hashtable<String,IdenResourcesInfo> retrieveIdenResources(Connection kbConnection, final int[] bans, final String[] subscriberIds) {
		return super.executeCallbackWithConnection(kbConnection, new ConnectionCallback<Hashtable<String,IdenResourcesInfo>>() {
			@Override
			public Hashtable<String,IdenResourcesInfo> doInConnection(Connection connection)
			throws SQLException, DataAccessException {

				Hashtable<String,IdenResourcesInfo> idenResources = null;
				OracleCallableStatement callable = null;
				ResultSet result = null;
				idenResources = new Hashtable<String,IdenResourcesInfo>();

				try {
					if (bans != null && bans.length > 0 && subscriberIds != null && subscriberIds.length > 0) {
						// prepare callable statement
						callable = (OracleCallableStatement) connection.prepareCall("{? = call SUBSCRIBER_PKG.GetIdenResources(?, ?, ?, ?)}");

						// create array descriptors
						ArrayDescriptor banArrayDesc = ArrayDescriptor.createDescriptor("T_BAN_ARRAY", connection);
						ArrayDescriptor subscriberIdArrayDesc = ArrayDescriptor.createDescriptor("T_SUBSCRIBER_ARRAY", connection);

						// create Oracle array of BANs
						ARRAY banArray = new ARRAY(banArrayDesc, connection, bans);

						// create Oracle array of subscriber IDs
						ARRAY subscriberIdsArray = new ARRAY(subscriberIdArrayDesc, connection, subscriberIds);

						// set/register input/output parameters
						callable.registerOutParameter(1, OracleTypes.NUMBER);
						callable.setARRAY(2, banArray);
						callable.setARRAY(3, subscriberIdsArray);
						callable.registerOutParameter(4, OracleTypes.CURSOR);
						callable.registerOutParameter(5, OracleTypes.VARCHAR);

						callable.execute();

						boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;

						if (success) {
							String curSubscriberId = null;
							int curBanId = 0;
							IdenResourcesInfo idenResource = null;

							result = (ResultSet) callable.getObject(4);

							while (result.next()) {
								String newSubscriberId = result.getString(1);
								int newBanId = result.getInt(2);

								if (newSubscriberId != null && !(newSubscriberId.equals(curSubscriberId) && newBanId == curBanId)) {
									if (curSubscriberId != null)
										idenResources.put(curSubscriberId + "_" + String.valueOf(curBanId), idenResource);

									curSubscriberId = newSubscriberId;
									curBanId = newBanId;
									idenResource = new IdenResourcesInfo();
								}

								String resourceType = result.getString(8);

								if (resourceType != null) {
									if (resourceType.equals("H")) {
										idenResource.setUrbanId(result.getInt(3));
										idenResource.setFleetId(result.getInt(4));
										idenResource.setMemberId(result.getString(5));
										idenResource.setResourceStatus(result.getString(9));
									}
									else if (resourceType.equals("N")) {
										idenResource.setPhoneNumber(result.getString(7));
									}
									else {
										idenResource.setIp(result.getString(7));
										idenResource.setIpType(result.getString(8));
									}

									// IMSI
									idenResource.setImsi(result.getString("imsi_number"));
								}
							}

							if (curSubscriberId != null)
								idenResources.put(curSubscriberId + "_" + curBanId, idenResource);
						}
						else {
							LOGGER.error("Failed to retrieve IDEN resources: " + callable.getString(5));
						}
					}
					else {
						LOGGER.info("Array of subscriber IDs is either null or empty.");
					}
				}
				finally {
					if (callable!=null)
						callable.close();
					if (result != null)
						result.close();
				}
				return idenResources;
			}
		}
		);
	}

	/**
	 * Retrieves provinces corresponding to the given phone numbers.
	 *
	 * @param phoneNumbers Array of phone numbers.
	 * @return Hashtable where province codes are accessed by phone numbers.
	 */
	private Hashtable<String,String> retrieveMarketProvincesByPhoneNumbers(Connection kbConnection, final String[] phoneNumbers) {
		return super.executeCallbackWithConnection(kbConnection, new ConnectionCallback<Hashtable<String,String>>() {
			@Override
			public Hashtable<String,String> doInConnection(Connection connection)
			throws SQLException, DataAccessException {

				Hashtable<String,String> marketProvinces = null;
				OracleCallableStatement callable = null;

				try {
					marketProvinces = new Hashtable<String,String>();

					if (phoneNumbers != null && phoneNumbers.length > 0) {
						// prepare callable statement
						callable = (OracleCallableStatement) connection.prepareCall("{? = call SUBSCRIBER_PKG.GetMarketProvinces(?, ?, ?)}");

						// create array descriptor
						ArrayDescriptor phoneNumbersArrayDesc = ArrayDescriptor.createDescriptor("T_PHONE_NUM_ARRAY", connection);

						// create Oracle array of phone numbers
						ARRAY phoneNumbersArray = new ARRAY(phoneNumbersArrayDesc, connection, phoneNumbers);

						// set/register input/output parameters
						callable.registerOutParameter(1, OracleTypes.NUMBER);
						callable.setARRAY(2, phoneNumbersArray);
						callable.registerOutParameter(3, OracleTypes.VARCHAR);
						callable.registerOutParameter(4, OracleTypes.VARCHAR);

						callable.execute();

						boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;

						if (success) {
							String result = callable.getString(3);

							int resultSz = result != null ? result.length() : 0;

							if (resultSz > 0 && resultSz % 12 == 0) {
								int count = resultSz / 12;
								int index = 0;

								for (int i = 0; i < count; i++) {
									String phoneNumber = result.substring(index, index + 10);
									String province = result.substring(index + 10, index + 12);

									marketProvinces.put(phoneNumber, province);

									index += 12;
								}
							}
						}
						else {
							LOGGER.info("Failed to retrieve market provinces: " + callable.getString(4));
						}
					}
					else {
						LOGGER.info("Array of phone numbers is either null or empty.");
					}
				}
				finally {
					if (callable!=null)
						callable.close();
				}

				return marketProvinces;
			}
		}
		);
	}

	/**
	 * Retrieves HSPA resources for given subscribers.
	 *
	 * @param kbConnection - database connection to knowbility
	 * @param bans   Array of bans
	 * @param subscriberIds Array of subscribers.
	 * @return Hashtable where instances of <tt>HSPAResourcesInfo</tt> are accessed by subscriber IDs.
	 * @see HSPAResourcesInfo
	 */

	private Hashtable<String,HSPAResourcesInfo> retrieveHSPAResources(Connection kbConnection, final int[] bans, final String[] subscriberIds) {
		return super.executeCallbackWithConnection(kbConnection, new ConnectionCallback<Hashtable<String,HSPAResourcesInfo>>() {
			@Override
			public Hashtable<String,HSPAResourcesInfo> doInConnection(Connection connection)
			throws SQLException, DataAccessException {
				// return object
				Hashtable<String,HSPAResourcesInfo> hspaResources = new Hashtable<String,HSPAResourcesInfo>();
				OracleCallableStatement callable = null;
				ResultSet result = null;

				try {
					hspaResources = new Hashtable<String,HSPAResourcesInfo>();

					if (bans != null && bans.length > 0 && subscriberIds != null && subscriberIds.length > 0) {
						// prepare callable statement
						callable = (OracleCallableStatement) connection.prepareCall("{? = call SUBSCRIBER_PKG.GetHSPAResources(?, ?, ?, ?)}");

						// create array descriptors
						ArrayDescriptor banArrayDesc = ArrayDescriptor.createDescriptor("T_BAN_ARRAY", connection);
						ArrayDescriptor subscriberIdArrayDesc = ArrayDescriptor.createDescriptor("T_SUBSCRIBER_ARRAY", connection);

						// create Oracle array of BANs
						ARRAY banArray = new ARRAY(banArrayDesc, connection, bans);

						// create Oracle array of subscriber IDs
						ARRAY subscriberIdsArray = new ARRAY(subscriberIdArrayDesc, connection, subscriberIds);

						// set/register input/output parameters
						callable.registerOutParameter(1, OracleTypes.NUMBER);
						callable.setARRAY(2, banArray);
						callable.setARRAY(3, subscriberIdsArray);
						callable.registerOutParameter(4, OracleTypes.CURSOR);
						callable.registerOutParameter(5, OracleTypes.VARCHAR);

						callable.execute();

						boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;

						if (success) {
							String curSubscriberId = null;
							int curBanId = 0;
							HSPAResourcesInfo hspaResource = null;

							result = (ResultSet) callable.getObject(4);

							while (result.next()) {
								String newSubscriberId = result.getString(1);
								int newBanId = result.getInt(2);

								if (newSubscriberId != null && !(newSubscriberId.equals(curSubscriberId) && newBanId == curBanId)) {
									if (curSubscriberId != null)
										hspaResources.put(curSubscriberId + "_" + String.valueOf(curBanId), hspaResource);

									curSubscriberId = newSubscriberId;
									curBanId = newBanId;
									hspaResource = new HSPAResourcesInfo();
								}

								hspaResource.setResourceStatus(result.getString("resource_status"));
								hspaResource.setPhoneNumber(result.getString("resource_number"));
								hspaResource.setHspaImsi(result.getString("imsi_number"));
							}

							if (curSubscriberId != null)
								hspaResources.put(curSubscriberId + "_" + curBanId, hspaResource);
						}
						else {
							LOGGER.info("Failed to retrieve HSPA resources: " + callable.getString(5));
						}
					}
					else {
						LOGGER.info("Array of subscriber IDs is either null or empty.");
					}
				}
				finally
				{
					if (callable!=null) {
						callable.close();
					}
					if (result!=null) {
						result.close();
					}
				}
				return hspaResources;
			}
		}
		);
	}

	/**
	 * Retrieves secondary serial numbers for given subscribers.
	 *
	 * @param kbConnection - database connection to knowbility
	 * @param bans
	 * @param subscriberIds
	 * @return Hashtable containing arrays of secondary serial numbers for each given subscriber.
	 */
	private Hashtable<String,String[]> retrieveSecondarySerialNumbers(Connection kbConnection, final int[] bans, final String[] subscriberIds) {
		return super.executeCallbackWithConnection(kbConnection, new ConnectionCallback<Hashtable<String,String[]>>() {
			@Override
			public Hashtable<String,String[]> doInConnection(Connection connection)
			throws SQLException, DataAccessException {
				Hashtable<String, String[]> secondarySerialNumbers = null;
				OracleCallableStatement callable = null;
				ResultSet result = null;

				try {
					secondarySerialNumbers = new Hashtable<String, String[]>();

					if (bans != null && bans.length > 0 && subscriberIds != null && subscriberIds.length == bans.length) {
						// prepare callable statement
						callable = (OracleCallableStatement) connection.prepareCall("{? = call SUBSCRIBER_PKG.GetSecondaryESNs(?, ?, ?, ?)}");

						// create array descriptors
						ArrayDescriptor banArrayDesc = ArrayDescriptor.createDescriptor("T_BAN_ARRAY", connection);
						ArrayDescriptor subscriberIdArrayDesc = ArrayDescriptor.createDescriptor("T_SUBSCRIBER_ARRAY", connection);

						// create Oracle array of BANs
						ARRAY banArray = new ARRAY(banArrayDesc, connection, bans);

						// create Oracle array of subscriber IDs
						ARRAY subscriberIdArray = new ARRAY(subscriberIdArrayDesc, connection, subscriberIds);

						// set/register input/output parameters
						callable.registerOutParameter(1, OracleTypes.NUMBER);
						callable.setARRAY(2, banArray);
						callable.setARRAY(3, subscriberIdArray);
						callable.registerOutParameter(4, OracleTypes.CURSOR);
						callable.registerOutParameter(5, OracleTypes.VARCHAR);

						callable.execute();

						boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;

						if (success) {
							String curSubscriberId = null;
							int curBanId = 0;
							ArrayList<String> secondarySerialNumberList = null;

							result = (ResultSet) callable.getObject(4);

							while (result.next()) {
								String newSubscriberId = result.getString(1);
								int newBanId = result.getInt(2);

								if (newSubscriberId != null && !(newSubscriberId.equals(curSubscriberId) && newBanId == curBanId)) {
									if (curSubscriberId != null) {
										String[] secondarySerialNumbersArray = new String[secondarySerialNumberList.size()];
										secondarySerialNumbersArray = secondarySerialNumberList.toArray(secondarySerialNumbersArray);

										secondarySerialNumbers.put(curSubscriberId + "_" + String.valueOf(curBanId), secondarySerialNumbersArray);
									}

									curSubscriberId = newSubscriberId;
									curBanId = newBanId;
									secondarySerialNumberList = new ArrayList<String>();
								}				
								String secondarySerialNumber = result.getString(3);

								if (secondarySerialNumber != null && secondarySerialNumber.trim().length() > 0)
									secondarySerialNumberList.add(secondarySerialNumber);
							}

							if (curSubscriberId != null) {
								String[] secondarySerialNumbersArray = new String[secondarySerialNumberList.size()];
								secondarySerialNumbersArray = secondarySerialNumberList.toArray(secondarySerialNumbersArray);

								secondarySerialNumbers.put(curSubscriberId + "_" + String.valueOf(curBanId), secondarySerialNumbersArray);
							}
						}
						else {
							LOGGER.info("Failed to retrieve secondary serial numbers: " + callable.getString(5));
						}
					}
					else {
						LOGGER.info("Array of subscriber IDs is either null or empty.");
					}
				}
				finally {
					if (callable!=null)
						callable.close();
					if (result!=null)
						result.close();
				}

				return secondarySerialNumbers;
			}
		}
		);
	}

	@Override
	public List<String> retrievePartiallyReservedSubscriberListByBan(final int ban,
			final int maximum) {

		String sql = "{? = call SUBSCRIBER_PKG.getreservedsubscriberlistbyBAN(?,?,?)}";

		return super.getKnowbilityJdbcTemplate().execute(sql, new CallableStatementCallback<List<String>>() {

			@Override
			public List<String> doInCallableStatement(CallableStatement callstmt)
			throws SQLException, DataAccessException {
				ResultSet result = null;
				List<String> subscriberList = new ArrayList<String>();
				String[] subscriberIdArray = new String[1];
				int[] banIdArray = new int[1];

				try{
					// set/register input/output parameters
					callstmt.registerOutParameter(1, OracleTypes.NUMBER);
					callstmt.setInt(2, ban);
					callstmt.registerOutParameter(3, OracleTypes.CURSOR);
					callstmt.registerOutParameter(4, OracleTypes.VARCHAR);

					callstmt.execute();

					boolean success = callstmt.getInt(1) == AccountManager.NUMERIC_TRUE;

					if (success) {
						result = (ResultSet) callstmt.getObject(3);

						int counter = 0;
						String phoneNumber = "";
						while (result.next()) {
							if (maximum > 0 && counter++ >= maximum) break;

							// Use the correct SubscriberInfo type.
							String subscriberId = result.getString(1);
							String productType = result.getString(2);
							if (productType.equals(Subscriber.PRODUCT_TYPE_IDEN)) {
								subscriberIdArray[0] = subscriberId;
								banIdArray[0] = ban;
								Hashtable<String,IdenResourcesInfo> idenResources = retrieveIdenResources(callstmt.getConnection(), banIdArray, subscriberIdArray);
								IdenResourcesInfo idenResourcesInfo = idenResources.get(subscriberId);
								phoneNumber =idenResourcesInfo.getPhoneNumber();
							} else if (productType.equals(Subscriber.PRODUCT_TYPE_PAGER)) {
								phoneNumber = subscriberId;
							} else if (productType.equals(Subscriber.PRODUCT_TYPE_PCS)) {
								phoneNumber = subscriberId;
							} else if (productType.equals(Subscriber.PRODUCT_TYPE_TANGO)) {
								phoneNumber = subscriberId;
							} else if (productType.equals(Subscriber.PRODUCT_TYPE_CDPD)) {
								phoneNumber = "";
							}

							subscriberList.add(phoneNumber);
						}
					}
					else {
						String errorMessage = callstmt.getString(4);
						LOGGER.error("Stored procedure failed: " + errorMessage);

					}
				}finally {
					if (result != null)
						result.close();
				}
				return subscriberList;
			}
		});
	}

	@Override
	public Collection<SubscriberInfo> retrievePortedSubscriberListByBAN(int ban, final int listLength)	 {

		String sql = "SELECT DISTINCT s.customer_id, s.subscriber_no, s.sub_status, nd.first_name, " +
		"nd.middle_initial, nd.last_business_name, pd.unit_esn, s.product_type, " +
		"SUBSTR(s.dealer_code, 1, 10), sa.soc, s.email_address, "+
		"NVL(s.init_activation_date, s.effective_date), s.init_activation_date, " +
		"SUBSTR(s.dealer_code, 11), s.sub_status_rsn_code, s.sub_status_last_act, " +
		"s.sub_alias, DECODE(INSTR(user_seg, '@'), 0, '', SUBSTR(user_seg, INSTR(user_seg, '@') + 1, 1)), " +
		"sub_lang_pref, s.next_ctn, s.next_ctn_chg_date, s.prv_ctn, s.prv_ctn_chg_date, " +
		"DECODE(s.sub_status, 'S', 'B', s.sub_status), " +
		"s.tax_gst_exmp_ind, s.tax_pst_exmp_ind, s.tax_hst_exmp_ind, " +
		"s.tax_gst_exmp_eff_dt, s.tax_pst_exmp_eff_dt, s.tax_hst_exmp_eff_dt, " +
		"s.tax_gst_exmp_exp_dt, s.tax_pst_exmp_exp_dt, s.tax_hst_exmp_exp_dt, " +
		"s.tax_gst_exmp_rf_no, s.tax_pst_exmp_rf_no, s.tax_hst_exmp_rf_no, " +
		"s.sub_status_date, s.calls_sort_order, " +
		"s.commit_reason_code, s.commit_orig_no_month, s.commit_start_date, s.commit_end_date, " +
		"nd.name_suffix, nd.additional_title, nd.name_title, s.hot_line_ind, nd.name_format, " +
		"s.migration_type, s.migration_date, s.tenure_date, s.req_deposit_amt, s.port_type, s.port_date, " +
		"s.brand_id, s.seat_type, s.seat_group, s.external_id as subscription_id " +
		"FROM subscriber s, " +
		"	 address_name_link anl, " +
		"     name_data nd, " +
		"     physical_device pd, " +
		"     service_agreement sa " +
		"WHERE s.customer_id = ? " +
		"	  AND s.sub_status = 'C' " +
		"      AND anl.ban(+) = s.customer_id " +
		"      AND anl.subscriber_no(+) = s.subscriber_no " +
		"      AND anl.expiration_date IS NULL " +
		"      AND nd.name_id(+) = anl.name_id " +
		"      AND sa.ban = s.customer_id " + 
		"      AND sa.subscriber_no = s.subscriber_no " +
		"      AND sa.product_type = s.product_type " +
		"      AND sa.service_type = 'P' " +
		"      AND sa.soc_seq_no = (SELECT MAX(sa1.soc_seq_no) " +
		"                           FROM service_agreement sa1 " +
		"                           WHERE sa1.ban = sa.ban " +
		"                                 AND sa1.subscriber_no = sa.subscriber_no " +
		"                                 AND sa1.product_type = sa.product_type " +
		"                                 AND sa1.service_type = 'P') " +
		"      AND pd.customer_id = s.customer_id " +
		"	  AND pd.subscriber_no = s.subscriber_no " +
		"      AND pd.product_type = s.product_type " +
		"      AND pd.esn_seq_no = (SELECT MAX(esn_seq_no) " +
		"                           FROM physical_device pd1 " +
		"                           WHERE pd1.customer_id = pd.customer_id " +
		"                                 AND pd1.subscriber_no = pd.subscriber_no " +
		"                                 AND pd1.product_type = pd.product_type " +
		"                                 AND NVL(pd1.esn_level, 1) = 1) " +
		"      AND s.port_type = 'O' " +								 
		"UNION " +
		"SELECT DISTINCT s.customer_id, s.subscriber_no, s.sub_status, nd.first_name, " +
		"	   nd.middle_initial, nd.last_business_name, pd.unit_esn, s.product_type, " +
		"       SUBSTR(s.dealer_code, 1, 10), sa.soc, s.email_address, " +
		"       NVL(s.init_activation_date, s.effective_date), s.init_activation_date, " +
		"       SUBSTR(s.dealer_code, 11), s.sub_status_rsn_code, s.sub_status_last_act, " +
		"       s.sub_alias, DECODE(INSTR(user_seg, '@'), 0, '', SUBSTR(user_seg, INSTR(user_seg, '@') + 1, 1)), " +
		"       sub_lang_pref, s.next_ctn, s.next_ctn_chg_date, s.prv_ctn, s.prv_ctn_chg_date, " +
		"       DECODE(s.sub_status, 'S', 'B', s.sub_status), " +
		"       s.tax_gst_exmp_ind, s.tax_pst_exmp_ind, s.tax_hst_exmp_ind, " +
		"       s.tax_gst_exmp_eff_dt, s.tax_pst_exmp_eff_dt, s.tax_hst_exmp_eff_dt, " +
		"       s.tax_gst_exmp_exp_dt, s.tax_pst_exmp_exp_dt, s.tax_hst_exmp_exp_dt, " +
		"       s.tax_gst_exmp_rf_no, s.tax_pst_exmp_rf_no, s.tax_hst_exmp_rf_no, " +
		"       s.sub_status_date, s.calls_sort_order, " +
		"       s.commit_reason_code, s.commit_orig_no_month, s.commit_start_date, s.commit_end_date, " +
		"       nd.name_suffix, nd.additional_title, nd.name_title, s.hot_line_ind, nd.name_format, " +
		"       s.migration_type, s.migration_date, s.tenure_date, s.req_deposit_amt, s.port_type, s.port_date, " +
		"	   s.brand_id, s.seat_type, s.seat_group, s.external_id as subscription_id " +
		"FROM subscriber s,  " +
		"     address_name_link anl, " +
		"	 name_data nd, " +
		"	 physical_device pd, " +
		"	 service_agreement sa, " +
		"	 logical_date ld " +	   
		"WHERE s.customer_id = ? " +
		"	  AND s.sub_status = 'S' " +
		"      AND anl.ban(+) = s.customer_id " +
		"      AND anl.subscriber_no(+) = s.subscriber_no " +
		"      AND anl.expiration_date IS NULL " +
		"      AND nd.name_id(+) = anl.name_id " +
		"      AND sa.ban = s.customer_id " +
		"      AND sa.subscriber_no = s.subscriber_no " +
		"      AND sa.product_type = s.product_type " +
		"      AND sa.service_type = 'P' " +
		"      AND (TRUNC(sa.expiration_date) > TRUNC(ld.logical_date) OR sa.expiration_date IS NULL) " +
		"      AND ld.logical_date_type = 'O' " +
		"      AND sa.effective_date = (SELECT MIN(sa1.effective_date) " +
		"      	  					   FROM service_agreement sa1, " +
		"							        logical_date ld " +
		"							   WHERE sa1.ban = sa.ban " +
		"							         AND sa1.subscriber_no = sa.subscriber_no " +
		"									 AND sa1.product_type = sa.product_type " +
		"									 AND sa1.service_type = 'P' " +
		"									 AND (TRUNC(sa1.expiration_date) > TRUNC(ld.logical_date) OR sa1.expiration_date IS NULL) " +
		"									 AND ld.logical_date_type = 'O') " +
		"      AND pd.customer_id = s.customer_id " +
		"	  AND pd.subscriber_no = s.subscriber_no " +
		"	  AND pd.product_type = s.product_type " +
		"	  AND pd.esn_level = 1 " +
		"      AND pd.expiration_date IS NULL " +
		"      AND s.port_type = 'O' " +
		"ORDER BY 24, 12 DESC ";

		return super.getKnowbilityJdbcTemplate().query(sql,new Object[]{ban,ban},new ResultSetExtractor<Collection<SubscriberInfo>>(){

			@Override
			public Collection<SubscriberInfo> extractData(ResultSet result)
			throws SQLException, DataAccessException {

				return convertSubscribersResultSet(result, listLength);
			}
			
			

		});
	}

	@Override
	public List<ResourceChangeHistoryInfo> retrieveResourceChangeHistory(final int ban,
			final String subscriberID, final String type, Date from, Date to) {

		final String fromDate = dateFormat.format(from) ;
		final String toDate =   dateFormat.format(to) ;
		String sql ="{call subscriber_pkg.retrieveResourceChangeHistory(?, ?, ?, ?, ?, ?)}";
		
		return super.getKnowbilityJdbcTemplate().execute(sql, new CallableStatementCallback<List<ResourceChangeHistoryInfo>>() {

			@Override
			public List<ResourceChangeHistoryInfo> doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				callable.setInt(1, ban);
				callable.setString(2, subscriberID);
				callable.setString(3, type);
				callable.setString(4, fromDate);
				callable.setString(5, toDate);
				callable.registerOutParameter(6, OracleTypes.CURSOR);
				
				callable.execute();
				List<ResourceChangeHistoryInfo> resourceChangeHistoryList = new ArrayList<ResourceChangeHistoryInfo>();
				ResultSet rs = (ResultSet) callable.getObject(6);

				if (rs != null) {
					try {
						while (rs.next()) {
							ResourceChangeHistoryInfo rChangeHistoryInfo = new ResourceChangeHistoryInfo();
							rChangeHistoryInfo.setType(rs.getString(1));
							rChangeHistoryInfo.setValue(rs.getString(2));
							rChangeHistoryInfo.setStatus(rs.getString(3));
							rChangeHistoryInfo.setStatusDate(rs.getTimestamp(4));
							rChangeHistoryInfo.setKnowbilityOperatorID(rs.getString(5) == null ? "" : rs.getString(5));
							rChangeHistoryInfo.setApplicationID(rs.getString(6) == null ? "" : rs.getString(6));
							resourceChangeHistoryList.add(rChangeHistoryInfo);
						}
					}finally {
						rs.close();
					}
				}
				return resourceChangeHistoryList;
			}
		});

	}


	@Override
	public List<LightWeightSubscriberInfo> retrieveLightWeightSubscriberListByBAN(final int banId,
			final boolean isIDEN, final int listLength, final boolean includeCancelled) {
		
		String sql = "{? = call SUB_RETRIEVAL_PKG.getLwSubListByBan(?, ?, ?, ?, ?, ?)}";

		return super.getKnowbilityJdbcTemplate().execute(sql, new CallableStatementCallback<List<LightWeightSubscriberInfo>>() {

			@Override
			public List<LightWeightSubscriberInfo> doInCallableStatement(CallableStatement callstmt)
			throws SQLException, DataAccessException {
				ResultSet result = null;
				List<LightWeightSubscriberInfo> lwsiList = null;

				try{
					// set/register input/output parameters
					callstmt.registerOutParameter(1, OracleTypes.NUMBER);
					callstmt.setInt(2, banId);
					callstmt.setInt(3, isIDEN ? AccountManager.NUMERIC_TRUE : AccountManager.NUMERIC_FALSE);
					callstmt.setInt(4, includeCancelled ? AccountManager.NUMERIC_TRUE : AccountManager.NUMERIC_FALSE);
					callstmt.setInt(5, listLength);
					callstmt.registerOutParameter(6, OracleTypes.CURSOR);
					callstmt.registerOutParameter(7, OracleTypes.VARCHAR);

					callstmt.execute();

					boolean success = callstmt.getInt(1) == AccountManager.NUMERIC_TRUE;

					if (success) {

						result = (ResultSet) callstmt.getObject(6);
						lwsiList = new ArrayList<LightWeightSubscriberInfo>();

						while (result.next()) {
							LightWeightSubscriberInfo lwsi = new LightWeightSubscriberInfo();
							lwsi.setBanId( result.getInt("customer_id") );
							lwsi.setSubscriberId( result.getString("subscriber_no") );
							lwsi.setProductType(result.getString("product_type") );
							lwsi.setStatus( result.getString("sub_status").charAt(0) );
							lwsi.setFirstName( result.getString("first_name") );
							lwsi.setLastName( result.getString("last_business_name") );
							lwsi.setNetworkType( result.getString("network_type") );
							lwsi.setSubscriptionId(result.getLong("external_id"));

							if (Subscriber.PRODUCT_TYPE_IDEN.equals(lwsi.getProductType()) ) { 
								if (isIDEN) { //isIDEN is required since phone_number column is only returned with the isIDEN=true query
									lwsi.setPhoneNumber( result.getString("phone_number") );
								}else {
									LOGGER.error("getLwSubListByBan not setting phone number on unexpected non-IDEN BAN with IDEN subscriber. [" + banId+"/"+lwsi.getSubscriberId()+"]" );
								}
							} else {
								lwsi.setPhoneNumber( lwsi.getSubscriberId() );
								lwsi.setSeatType(result.getString("seat_type"));
								lwsi.setSeatGroup(result.getString("seat_group"));
							}
							lwsiList.add( lwsi );
						}
					}else {
						String errorMessage = callstmt.getString(7);
						LOGGER.debug("Stored procedure failed: " + errorMessage);
					}
				}finally {
					if (result != null) {
						result.close();
					}
				}
				return lwsiList;
			}
		});
	}
	
	

	@Override
	public String retrieveLastAssociatedSubscriptionId(final String imsi)	 {

		String sql = "{? = call SUBSCRIBER_PKG.getlastassocsubscriptionid(?)}";

		return super.getKnowbilityJdbcTemplate().execute(sql, new CallableStatementCallback<String>() {

			@Override
			public String doInCallableStatement(CallableStatement callstmt)
			throws SQLException, DataAccessException {

				String subId = null;
				// set/register input/output parameters
				callstmt.registerOutParameter(1, OracleTypes.VARCHAR);
				callstmt.setString(2, imsi);
				callstmt.execute();
				subId = callstmt.getString(1);
				return subId;
			}
		});
	}

	@Override
	public boolean retrieveHotlineIndicator(final String subscriberId)	 {

		String sql = "{? = call SUBSCRIBER_PKG.getHotlineIndicator(?)}";
		int ishotlined=0;
		ishotlined=super.getKnowbilityJdbcTemplate().execute(sql, new CallableStatementCallback<Integer>() {

			@Override
			public Integer doInCallableStatement(CallableStatement callstmt)
			throws SQLException, DataAccessException {

				int hotlined = 0;
				// set/register input/output parameters
				callstmt.registerOutParameter(1, OracleTypes.NUMBER);
				callstmt.setString(2, subscriberId);
				callstmt.execute();
				hotlined = callstmt.getInt(1);
				return hotlined;
			}
		});
		return (ishotlined != 0);
	}

	@Override
	public String getPortProtectionIndicator(final int ban, final String subscriberId,
			final String phoneNumber, final String status) {

		String sql = "{ call eas_utility_pkg.GetPortProtection(?,?,?,?,?)}";

		return super.getCodsJdbcTemplate().execute(sql, new CallableStatementCallback<String>() {

			@Override
			public String doInCallableStatement(CallableStatement callstmt)
			throws SQLException, DataAccessException {
				String portRestrictedInd=null;

				// set/register input/output parameters
				callstmt.setInt(1, ban);
				callstmt.setString(2, phoneNumber);
				callstmt.setString(3, subscriberId);
				callstmt.setString(4,status);
				callstmt.registerOutParameter(5, OracleTypes.VARCHAR);
				callstmt.execute();

				portRestrictedInd = callstmt.getString(5) ;
				return portRestrictedInd;
			}
		});
	}

	@Override
	public SubscriptionPreferenceInfo retrieveSubscriptionPreference(
			final long subscriptionId, final int preferenceTopicId) {

		String sql = "{?=call subscriber_pref_pkg.getsubscriberpreference (?,?,?)}";

		return super.getCodsJdbcTemplate().execute(sql, new CallableStatementCallback<SubscriptionPreferenceInfo>() {

			@Override
			public SubscriptionPreferenceInfo doInCallableStatement(CallableStatement callstmt)
			throws SQLException, DataAccessException {

				ResultSet result=null;
				SubscriptionPreferenceInfo spi=null;
				try{
					callstmt.setLong(2, subscriptionId);
					callstmt.setInt(3, preferenceTopicId );
	
					callstmt.registerOutParameter(1, OracleTypes.NUMBER);
					callstmt.registerOutParameter(4, OracleTypes.CURSOR);
	
					callstmt. execute();
					boolean success = callstmt.getInt(1) == 0;
	
	
					if (success) {
						result = (ResultSet) callstmt.getObject(4);
					}
					else {
						LOGGER.error("APP_CODS_SUBPREF_001 Cannont find subscription for subscriptionId[" + subscriptionId +"]" );
					}
					if(result != null && result.next()) { //result may be null regardless of success value
						spi = new SubscriptionPreferenceInfo();
						spi.setSubscriberPreferenceId(result.getInt("subscriber_preference_id"));
						spi.setPreferenceTopicId(result.getInt("preference_topic_id"));
						spi.setPreferenceValueTxt(result.getString("preference_value_txt"));
						spi.setSubscrPrefChoiceSeqNum(result.getInt("subscr_pref_choice_seq_num"));
						spi.setSubscriptionId( subscriptionId );
					}
				}finally{
					if(result!=null){
						result.close();
					}
				}
				return spi;
			}
		});
	}

	@Override
	public List<String> retrieveAvailableCellularPhoneNumbersByRanges(
			PhoneNumberReservationInfo phoneNumberReservation,
			String startFromPhoneNumber, String searchPattern, boolean asian,
			int maxNumber) throws ApplicationException {

		String translatedSearchPattern = "" ;
		String asianInd = (asian ? "Y" : "N") ;
		String  startFromPhoneNumberString = (startFromPhoneNumber==null ? "0" : startFromPhoneNumber);
		NumberGroupInfo  ngInfo = (NumberGroupInfo)phoneNumberReservation.getNumberGroup();
		int i, j ;
		List<String> phoneNumbersList = new ArrayList<String>();
		boolean npaSpecifield = false;
		boolean nxxSpecifield = false ;

		String  sql = " select   ctn " +
		"  from    ctn_inv c " +
		"  ,market_npa_nxx_lr lr " +
		"  , logical_date ld " +
		"  where  c.nl = ? " +
		" and    c.ngp = ? " +
		" and    c.ctn_status = 'AA' " +
		" and    c.product_type = 'C' " +
		" and    c.ctn like ? "   +
		" and     c.ctn > '" + startFromPhoneNumberString + "'" +
		" and     lr.npa=substr(c.ctn,1,3) " +
		" and     lr.nxx=substr(c.ctn,4,3) " +
		" and     substr(c.ctn,7,4) between lr.begin_line_range " +
		" and   lr.end_line_range " +
		" and     c.product_type = lr.product_type " +
		" and    ( trunc(lr.expiration_date) > trunc(ld.logical_date)  or lr.expiration_date is null) " +
		" and    ld.logical_date_type='O' " +
		" and     substr(c.ctn,7,4) between ? and ? " +
		" and  ( ( ( '" + asianInd + "'= 'Y' ) " +
		"       and substr ( c.ctn,7,4) not like '%4%' " +
		"       and substr ( c.ctn,7,4) not like '%5%' " +
		"       and substr ( c.ctn,7,4) not like '%7%') " +
		"    or " +
		"     ( ( '" + asianInd + "'= 'N' ) " +
		"       and  ( substr ( c.ctn,7,4) like '%4%' " +
		"            or substr ( c.ctn,7,4) like '%5%' " +
		"            or substr ( c.ctn,7,4) like '%7%' " +
		"            ) " +
		"     ) " +
		"   ) " +
		" and rownum <=  " + maxNumber  +
		" order by c.ctn asc ";

		// search pattern may have % , translate it, so that, it is consistent in searching for '*'
		searchPattern = searchPattern.replace('%','*');
		int len = searchPattern.length();
		if (len < 10) {
			searchPattern = searchPattern + "***********".substring(0, 10 - len) ;
		}


		if (!searchPattern.substring(0,3).equals("***"))
		{
			npaSpecifield = true;
		}
		if (!searchPattern.substring(3,6).equals("***"))

		{  
			nxxSpecifield = true;
		} 

		NumberRangeInfo[] numberRanges;
		try {
			numberRanges = ngInfo.getNumberRanges();

		} catch (TelusAPIException e) {
			throw new ApplicationException(SystemCodes.CMB_SLH_DAO,e.getMessage(),"");
		}
		if ((npaSpecifield) && (nxxSpecifield)){
			numberRanges = retainNumberRangesByNpaNXX(numberRanges,searchPattern.substring(0,6));
		}
		else if ((npaSpecifield) && (!nxxSpecifield)){
			numberRanges = retainNumberRangesByNpa(numberRanges,searchPattern.substring(0,3));
		}

		List<Object> inputArgs=new ArrayList<Object>();

		for (i=0; i<numberRanges.length; i++)
		{ 
			for (j=0; j< numberRanges[i].getLineRanges().length; j++)
			{
				translatedSearchPattern = "";
				inputArgs.add(ngInfo.getNumberLocation());
				inputArgs.add(ngInfo.getCode());
				translatedSearchPattern = numberRanges[i].getNPANXX() + searchPattern.substring(6);  
				translatedSearchPattern = translatedSearchPattern.replace('*','_');
				inputArgs.add(translatedSearchPattern);
				inputArgs.add(StringUtil.rangeIntToString(numberRanges[i].getLineRanges()[j].getStart()));
				inputArgs.add(StringUtil.rangeIntToString(numberRanges[i].getLineRanges()[j].getEnd()));

				try{
					phoneNumbersList.addAll(super.getKnowbilityJdbcTemplate().queryForList(sql,inputArgs.toArray(), String.class));
				}
				catch(EmptyResultDataAccessException e){
					LOGGER.error("EmptyResultDataAccessException : ", e);
					return phoneNumbersList;
				}
				if ( phoneNumbersList.size() == maxNumber) {
					return phoneNumbersList;
				}
				//defect PROD00192836, to reuse the inputArgs, we shall to clear the list for each iteration. - May 25,2011. M.Liao 
				inputArgs.clear();
			}
		}
		return phoneNumbersList;
	}
	@Override
	public boolean isPortRestricted(final int ban, final String subscriberId,
			final String phoneNumber,final String status) {
		String callString="{ call eas_utility_pkg.IsPortRestricted(?,?,?,?,?)}";
		return super.getCodsJdbcTemplate().execute(callString,new CallableStatementCallback<Boolean>() {

			@Override
			public Boolean doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {
				boolean portRestricted = false;
				callable.setInt(1, ban);
				callable.setString(2, phoneNumber);
				callable.setString(3, subscriberId);
				callable.setString(4,status);

				callable.registerOutParameter(5, OracleTypes.VARCHAR);

				callable.execute();

				portRestricted = callable.getString(5).equals("Y") ? true:false ;
				return portRestricted;
			}

		});

	}
	
	@Override
	public SubscriberInfo retrieveSubscriberByPhoneNumber(String phoneNumber) throws ApplicationException {
		SubscriberInfo subscriber = null;
		Collection<SubscriberInfo> subscribers ;
		subscribers = retrieveSubscriberListByPhoneNumbers(new String[]{phoneNumber}, 10, false);
		Iterator<SubscriberInfo> iterator = subscribers.iterator();

		if (iterator.hasNext()) {
			subscriber = iterator.next();
		}

		return subscriber;
	}

	

	@Override
	public List<SubscriberHistoryInfo> retrieveSubscriberHistory(final int ban,
			final String subscriberID, final Date from, final Date to) {
		String callString="{? = call HISTORY_UTILITY_PKG.GetSubscriberHistory(?, ?, ?, ?, ?, ?)}";
		return super.getKnowbilityJdbcTemplate().execute(callString,new CallableStatementCallback<List<SubscriberHistoryInfo>>() {

			@Override
			public List<SubscriberHistoryInfo> doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {
				List<SubscriberHistoryInfo> list = new ArrayList<SubscriberHistoryInfo>();
				ResultSet result = null;
				try{
					callable.registerOutParameter(1, OracleTypes.NUMBER);
					callable.setInt(2, ban);
					callable.setString(3, subscriberID);
					callable.setString(4, dateFormat.format(from));
					callable.setString(5, dateFormat.format(to));
					callable.registerOutParameter(6, OracleTypes.CURSOR);
					callable.registerOutParameter(7, OracleTypes.VARCHAR);
	
					callable.execute();
	
					boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;
	
					if (success) {
						result = (ResultSet) callable.getObject(6);
	
						while (result.next()) {
							SubscriberHistoryInfo info = new SubscriberHistoryInfo();
							info.setDate(result.getTimestamp(1));
							info.setStatus(result.getString(2).toCharArray()[0]);
							info.setActivityCode(result.getString(3));
							info.setActivityReasonCode(result.getString(4));
							info.setPreviousBanId(result.getInt(5));
							info.setNextBanId(result.getInt(6));
							info.setBrandId(result.getInt(7));
	
							list.add(info);
						}
					}
					else {
						LOGGER.debug("Stored Procedure failed"+callable.getString(7));
					}
				}finally{
					if(result!=null)result.close();		
				}
				return list;
			}
		});
	}

	@Override
	public Collection<SubscriberInfo> retrieveSubscriberListByBanAndFleet(final int ban,final int urbanId,
			final int fleetId, final int listLength) {
		String callString="{? = call SUBSCRIBER_PKG.GetSubscriberListByBanAndFleet(?, ?, ?, ?, ?)}"	;
		return super.getKnowbilityJdbcTemplate().execute(callString, new CallableStatementCallback<Collection<SubscriberInfo>>() {

			@Override
			public Collection<SubscriberInfo> doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {
				Collection<SubscriberInfo> subscriberList = new ArrayList<SubscriberInfo>();
				ResultSet result = null;
				try {
					// prepare callable statement

					// set/register input/output parameters
					callable.registerOutParameter(1, OracleTypes.NUMBER);
					callable.setInt(2, ban);
					callable.setInt(3, urbanId);
					callable.setInt(4, fleetId);
					callable.registerOutParameter(5, OracleTypes.CURSOR);
					callable.registerOutParameter(6, OracleTypes.VARCHAR);

					callable.execute();

					boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;

					if (success) {
						result = (ResultSet) callable.getObject(5);

						subscriberList = convertSubscribersResultSet(callable.getConnection(), result, listLength,false);
					}
					else {
						String errorMessage = callable.getString(6);
						LOGGER.debug("Stored procedure failed: " + errorMessage) ;

					}
				}finally{
					if(result!= null)
						result.close();
				}
				return subscriberList;
			}
		});
	}

	@Override
	public Collection<SubscriberInfo> retrieveSubscriberListByBanAndTalkGroup(final int ban,
			final int urbanId, final int fleetId, final int talkGroupId, final int listLength) {
		String callString="{? = call SUBSCRIBER_PKG.GetSubscriberListByBanAndTG(?, ?, ?, ?, ?, ?)}"	;
		return super.getKnowbilityJdbcTemplate().execute(callString, new CallableStatementCallback<Collection<SubscriberInfo>>() {

			@Override
			public Collection<SubscriberInfo> doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {
				Collection<SubscriberInfo> subscriberList = new ArrayList<SubscriberInfo>();
				ResultSet result = null;
				try {
					callable.registerOutParameter(1, OracleTypes.NUMBER);
					callable.setInt(2, ban);
					callable.setInt(3, urbanId);
					callable.setInt(4, fleetId);
					callable.setInt(5, talkGroupId);
					callable.registerOutParameter(6, OracleTypes.CURSOR);
					callable.registerOutParameter(7, OracleTypes.VARCHAR);

					callable.execute();

					boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;

					if (success) {
						result = (ResultSet) callable.getObject(6);

						subscriberList = convertSubscribersResultSet(callable.getConnection(), result, listLength,false);
					}
					else {
						String errorMessage = callable.getString(7);
						LOGGER.debug("Stored procedure failed: " + errorMessage) ;
					}
				}finally{
					if(result!= null)
						result.close();
				}
				return subscriberList;
			}
		});

	}

	@Override
	public SubscriptionRoleInfo retrieveSubscriptionRole(String phoneNumber) {

		String sql="select sr.subscription_role_cd " +
		",sr.assigned_at_outlet_code " +
		",sr.assigned_by_sales_rep_id " +
		",sr.assigned_by_csr_id " +
		"from subscription_role sr " +
		"where sr.subscription_id = " +
		"  ( select max(subscription_id) from subscription where " +
		"    cell_phone_no = ? and current_status_cd in ('A','S') )";
		return super.getCodsJdbcTemplate().query(sql,new Object[]{phoneNumber}, new ResultSetExtractor<SubscriptionRoleInfo>(){

			@Override
			public SubscriptionRoleInfo extractData(ResultSet rset)
			throws SQLException, DataAccessException {
				SubscriptionRoleInfo mySubscriptionRoleInfo = null;
				if(rset.next())
				{
					//Read only the first row from the result set. Database should enforce constraint.
					mySubscriptionRoleInfo = new SubscriptionRoleInfo();
					mySubscriptionRoleInfo.setCode(rset.getString(1));
					mySubscriptionRoleInfo.setDealerCode(rset.getString(2));
					mySubscriptionRoleInfo.setSalesRepCode(rset.getString(3));
					mySubscriptionRoleInfo.setCsrId(rset.getString(4));
				}
				return mySubscriptionRoleInfo;
			}

		});
	}

	@Override
	public List<String> retrieveSubscriberPhonenumbers(char subscriberStatus,
			char accountType, char accountSubType, char banStatus, int maximum) {
		String queryStr =
			"select " +
			"    distinct SUBSCRIBER.SUBSCRIBER_NO " +
			"from " +
			"    SUBSCRIBER, BILLING_ACCOUNT " +
			"where " +
			"    SUBSCRIBER.SUB_STATUS = ? and " +
			"    BILLING_ACCOUNT.BAN = SUBSCRIBER.CUSTOMER_BAN and " +
			"    BILLING_ACCOUNT.ACCOUNT_TYPE=? and  " +
			"    BILLING_ACCOUNT.ACCOUNT_SUB_TYPE=? and  " +
			"    BILLING_ACCOUNT.BAN_STATUS=? and " +
			"    ROWNUM <= ?";

		return super.getKnowbilityJdbcTemplate().query(queryStr, new Object[]{String.valueOf(subscriberStatus),String.valueOf(accountType),
				String.valueOf(accountSubType),String.valueOf(banStatus),maximum},new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet result, int arg1) throws SQLException {
				return result.getString(1);
			}
		});
	}

	@Override
	public List<String> retrieveSubscriberPhonenumbers(char subscriberStatus,
			char accountType, char accountSubType, char banStatus,
			String addressType, int maximum) {

		String queryStr =
			"select " +
			"    distinct SUBSCRIBER.SUBSCRIBER_NO " +
			"from " +
			"    SUBSCRIBER, BILLING_ACCOUNT, ADDRESS_NAME_LINK, ADDRESS_DATA " +
			"where " +
			"    SUBSCRIBER.SUB_STATUS = ? and " +
			"    BILLING_ACCOUNT.BAN = SUBSCRIBER.CUSTOMER_BAN and " +
			"    BILLING_ACCOUNT.ACCOUNT_TYPE=? and  " +
			"    BILLING_ACCOUNT.ACCOUNT_SUB_TYPE=? and  " +
			"    BILLING_ACCOUNT.BAN_STATUS=? and " +
			"    ADDRESS_NAME_LINK.BAN = BILLING_ACCOUNT.BAN and " +
			"    ADDRESS_NAME_LINK.EXPIRATION_DATE is null and " +
			"    ADDRESS_DATA.ADDRESS_ID = ADDRESS_NAME_LINK.ADDRESS_ID and " +
			"    ADDRESS_DATA.ADR_TYPE = ? and " +
			"    ROWNUM <= ?  ";
		return super.getKnowbilityJdbcTemplate().query(queryStr, new Object[]{String.valueOf(subscriberStatus),String.valueOf(accountType),
				String.valueOf(accountSubType),String.valueOf(banStatus),addressType,maximum},new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet result, int arg1) throws SQLException {
				return result.getString(1);
			}
		});

	}

	@Override
	public Collection<SubscriberInfo> retrieveSubscriberListBySerialNumber(
			final String serialNumber, final boolean includeCancelled) {
		String callString="{? = call SUBSCRIBER_PKG.GetSubscriberListByESN(?, ?, ?, ?)}"	;
		return super.getKnowbilityJdbcTemplate().execute(callString, new CallableStatementCallback<Collection<SubscriberInfo>>() {

			@Override
			public Collection<SubscriberInfo> doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {
				Collection<SubscriberInfo> subscriberList = new ArrayList<SubscriberInfo>();
				ResultSet result = null;
				try {
					callable.registerOutParameter(1, OracleTypes.NUMBER);
					callable.setString(2, serialNumber);
					callable.setInt(3, includeCancelled ? AccountManager.NUMERIC_TRUE : AccountManager.NUMERIC_FALSE);
					callable.registerOutParameter(4, OracleTypes.CURSOR);
					callable.registerOutParameter(5, OracleTypes.VARCHAR);

					callable.execute();

					boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;

					if (success) {
						result = (ResultSet) callable.getObject(4);

						subscriberList = convertSubscribersResultSet(callable.getConnection(), result, 0,includeCancelled);
					}
					else {
						String errorMessage = callable.getString(5);
						LOGGER.debug("Stored procedure failed: " + errorMessage) ;
					}
				}finally{
					if(result!= null)
						result.close();  
				}
				return subscriberList;
			}
		});
	}
	
	

	@Override
	public Collection<SubscriberInfo> retrieveSubscriberListByPhoneNumbers(final String[] phoneNumbers, int maximum, final boolean includeCancelled)  {
		if (phoneNumbers == null || phoneNumbers.length == 0) {
			return new ArrayList<SubscriberInfo>();
		}
		
		String callString= "{? = call SUB_RETRIEVAL_PKG.getSubListByPhoneNumbers(?, ?, ?, ?, ?)}";
		final int resultSetMaxCount = getActualMaxSubLimit(maximum);
		
		return super.getKnowbilityJdbcTemplate().execute(callString, new CallableStatementCallback<Collection<SubscriberInfo>>() {

			@Override
			public Collection<SubscriberInfo> doInCallableStatement(
					CallableStatement callable) throws SQLException,
					DataAccessException {
				Collection<SubscriberInfo> subscriberList = null;
				ResultSet result = null;
				try{
					// create array descriptor - note that vendor service IDs are category codes 
					ArrayDescriptor phoneNumbersArrayDescriptor = ArrayDescriptor.createDescriptor("T_PHONE_NUM_ARRAY", callable.getConnection());

					// create Oracle array of vendor category codes
					ARRAY phoneNumbersArray = new ARRAY(phoneNumbersArrayDescriptor, callable.getConnection(), phoneNumbers);


					// set/register input/output parameters
					callable.registerOutParameter(1, OracleTypes.NUMBER);    
					callable.setArray(2, phoneNumbersArray);
					callable.setInt(3, includeCancelled ? AccountManager.NUMERIC_TRUE : AccountManager.NUMERIC_FALSE);
					callable.setInt(4, resultSetMaxCount);
					callable.registerOutParameter(5, OracleTypes.CURSOR);
					callable.registerOutParameter(6, OracleTypes.VARCHAR);

					callable.execute();

					boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;

					if (success) {
						result = (ResultSet) callable.getObject(5);
						subscriberList = convertSubscribersResultSet(callable.getConnection(), result, resultSetMaxCount,includeCancelled);
					} else {
						subscriberList = new ArrayList<SubscriberInfo>();
						String errorMessage = callable.getString(6);
						LOGGER.debug("Stored procedure failed: " + errorMessage);
					}
				}finally{
					if(result!=null) {
						result.close();
					}
				}
				return subscriberList;
			}
		});
	}
	
	@Override
	public Collection<SubscriberInfo> retrieveSubscriberListByPhoneNumbers (final String[] phoneNumbers, final boolean includeCancelled) {
		return retrieveSubscriberListByPhoneNumbers(phoneNumbers, getActualMaxSubLimit(0), includeCancelled);
	}
	
	@Override
	public Collection<SubscriberInfo> retrieveHSPASubscriberListByIMSI(
			final String IMSI, final boolean includeCancelled) throws ApplicationException{

		String sql="{? = call SUBSCRIBER_PKG.GetSubListByIMSI(?, ?, ?, ?)}";
		Collection<SubscriberInfo> subscriberInfoList = new ArrayList<SubscriberInfo>();

		try{
			subscriberInfoList= super.getKnowbilityJdbcTemplate().execute(sql, new CallableStatementCallback<Collection<SubscriberInfo>>() {

				@Override
				public Collection<SubscriberInfo> doInCallableStatement(CallableStatement callstmt)
				throws SQLException, DataAccessException{


					Collection<SubscriberInfo> subscriberList = new ArrayList<SubscriberInfo>();
					ResultSet result = null;

					try {
						// set/register input/output parameters
						callstmt.registerOutParameter(1, OracleTypes.NUMBER);
						callstmt.setString(2, IMSI);
						callstmt.setInt(3, includeCancelled ? AccountManager.NUMERIC_TRUE : AccountManager.NUMERIC_FALSE);
						callstmt.registerOutParameter(4, OracleTypes.CURSOR);
						callstmt.registerOutParameter(5, OracleTypes.VARCHAR);
						callstmt.execute();

						boolean success = callstmt.getInt(1) == AccountManager.NUMERIC_TRUE;

						if (success) {
							result = (ResultSet) callstmt.getObject(4);
							subscriberList = convertSubscribersResultSet(callstmt.getConnection(), result, 0,includeCancelled);
						}
						else {
							String errorMessage = callstmt.getString(5);
							LOGGER.error("Stored procedure failed: " + errorMessage);
							throw new SQLException("APP20002 Subscribers not found for IMSI " + IMSI);
						}
					}finally {
						if(result!=null)
							result.close();
					}
					return subscriberList;
				}
			});

		}catch(Throwable t){
			throw new ApplicationException(SystemCodes.CMB_SLH_DAO,ErrorCodes.SUBSCRIBER_NOT_FOUND, "Subscribers not found for IMSI " + IMSI,"",t);

		}

		return subscriberInfoList;
	}

	@Override
	public long retrieveSubscriptionId(final int banId, final String phoneNumber,
			final String status) throws ApplicationException {
		String sql = "{? = call SUBSCRIBER_INFO_PKG.selectsubscriptionid(?,?,?)}";
		
		return super.getCodsJdbcTemplate().execute(sql, new CallableStatementCallback<Long>() {

			@Override
			public Long doInCallableStatement(CallableStatement callable)
					throws SQLException, DataAccessException {
				  // set/register input/output parameters
		         
		          callable.registerOutParameter(1, OracleTypes.NUMBER);
		          callable.setInt(2, banId);
		          callable.setString(3, phoneNumber);
		          callable.setString(4, status);
		          callable.execute();

		          long subscriptionId = callable.getInt(1);
		          
		          return subscriptionId;		       		        
			}
		});
	}

	@Override
	public String retrieveEmailAddress(int ban, String subscriberNumber) throws ApplicationException {
		String sql = "select email_address from subscriber where sub_status='A' and subscriber_no=? and customer_ban=?";
		return super.getKnowbilityJdbcTemplate().queryForObject(
			sql, 
			new Object[]{subscriberNumber, ban},
			new RowMapper<String>() {
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getString(1);
				}
			}
		);
	}
	
	@Override
	public SubscriberIdentifierInfo retrieveSubscriberIdentifierBySubscriptionId(final long subscriptionId) throws ApplicationException {
		String sql = "SELECT ca.ban, s.cell_phone_no, s.iden_subscriber_id " + 
					 "FROM CLIENT_ACCOUNT ca, ACCOUNT_SUBSCRIPTION_ASSOC asa, SUBSCRIPTION s "+
					 "WHERE asa.CLIENT_ACCOUNT_ID = ca.CLIENT_ACCOUNT_ID "+
					 "and s.SUBSCRIPTION_ID = asa.SUBSCRIPTION_ID "+
					 "and s.subscription_id = ? " +
					 "order by asa.ACCT_SUBS_ASSOC_SEQ_NO desc";
		
		return getCodsJdbcTemplate().query(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setLong(1, subscriptionId);
			}
		}, new ResultSetExtractor<SubscriberIdentifierInfo>() {

			@Override
			public SubscriberIdentifierInfo extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				if (resultSet.next()) {
					String idenSubscriberId = resultSet.getString("iden_subscriber_id");
					String phoneNumber = resultSet.getString("cell_phone_no");
					
					SubscriberIdentifierInfo subIdentifier = new SubscriberIdentifierInfo();
					subIdentifier.setBan(resultSet.getInt("ban"));
					subIdentifier.setPhoneNumber(phoneNumber);
					subIdentifier.setSubscriberId(idenSubscriberId != null ? idenSubscriberId : phoneNumber);			
					subIdentifier.setSubscriptionId(subscriptionId);
					return subIdentifier;
				}
				return null;
			}
		});
	}

	@Override
	public SubscriberInfo retrieveSubscriberByBANAndPhoneNumber(final int ban, final String phoneNumber) throws ApplicationException {

		String callableStmt = "{? = call SUB_RETRIEVAL_PKG.getSubByBANandPhoneNumber(?, ?, ?, ?, ?)}";

		SubscriberInfo subscriberInfo = super.getKnowbilityJdbcTemplate().execute(callableStmt, new CallableStatementCallback<SubscriberInfo>() {
			@Override
			public SubscriberInfo doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				ResultSet result = null;
				SubscriberInfo subscriberInfo = null;
				int includeCancelled = AccountManager.NUMERIC_TRUE;
				String formattedPhoneNumber = AttributeTranslator.replaceString(phoneNumber.trim(), " ", "");
				formattedPhoneNumber = AttributeTranslator.replaceString(formattedPhoneNumber.trim(), "-", "");
				try {
					// set/register input/output parameters
					callable.registerOutParameter(1, OracleTypes.NUMBER);
					callable.setInt(2, ban);
					callable.setString(3, formattedPhoneNumber);
					callable.setInt(4, includeCancelled);
					callable.registerOutParameter(5, OracleTypes.CURSOR);
					callable.registerOutParameter(6, OracleTypes.VARCHAR);
					callable.setFetchSize(1);
					
					callable.execute();

					boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;

					if (success) {
						result = (ResultSet) callable.getObject(5);
						Collection<SubscriberInfo> subscriberInfos = convertSubscribersResultSet(callable.getConnection(), result, 1,false);
						if (!subscriberInfos.isEmpty()) {
							subscriberInfo = subscriberInfos.iterator().next();// max count is always 1 as we need only one subscriber
						}
					} else {
						String errorMessage = callable.getString(6);
						LOGGER.error("Callable Statement resulted in error [" + errorMessage + "]");
					}
				} finally {
					if (result != null) {
						result.close();
					}
				}
				return subscriberInfo;
			}
		});
		return subscriberInfo;

	}
	
	@Override
	public String retrieveChangedSubscriber(final int ban,final String subscriberId,final String productType,final Date searchFromDate, final Date searchToDate) throws ApplicationException {

		String sql = "select customer_id, subscriber_no from subscriber s join "+
				"(select prv_ctn from subscriber where customer_id = ? and subscriber_no = ? and product_type =? ) s0 "+
				" on s.subscriber_no=s0.prv_ctn "+
				" where customer_id = ? and product_type = ? and sys_creation_date between ? and ? ";


		final java.sql.Timestamp fromDate = new Timestamp(searchFromDate.getTime());
		final java.sql.Timestamp toDate = new Timestamp(searchToDate.getTime());
		return super.getKnowbilityJdbcTemplate().execute(sql, new PreparedStatementCallback<String>() {
			@Override
			public String doInPreparedStatement(PreparedStatement pstmt)
					throws SQLException, DataAccessException {

				ResultSet rset = null;
				try {
					pstmt.setInt(1, ban);
					pstmt.setString(2, subscriberId);
					pstmt.setString(3, productType);
					pstmt.setInt(4, ban);
					pstmt.setString(5, productType);
					pstmt.setTimestamp(6, fromDate);
					pstmt.setTimestamp(7, toDate);
					rset = pstmt.executeQuery();
					if(rset.next()) {
						return rset.getString("subscriber_no");
					}
				} finally {
					if (rset != null ) {
						rset.close();
					}
				}
				return null;
			}

		});
	}
	
	/**
	 * 
	 * @param maximumFromUser 0 means ALL,  but we should still put a limit for stability. It is arbitrarily set to ldap max * 10
	 * @return
	 */
	private int getActualMaxSubLimit(int maximumFromUser) {
		int maxSubLimit = AppConfiguration.getMaxSubLimit();
		if (maximumFromUser > 0 && maximumFromUser <= maxSubLimit) {
			return maximumFromUser;
		} else if (maximumFromUser == 0) {
			return (maxSubLimit * 10);  //virtually ALL with a very high limit.
		}else {
			return maxSubLimit;
		}
	}
	
	@Override
	public SubscriberInfo retrieveSubscriberBySeatResourceNumber(final String seatResourceNumber) throws ApplicationException {
		SubscriberInfo subscriber = null;
		Collection<SubscriberInfo> subscribers;
		subscribers = retrieveSubscriberListBySeatResourceNumber(0,seatResourceNumber, 10, false);

		Iterator<SubscriberInfo> iterator = subscribers.iterator();

		if (iterator.hasNext()) {
			subscriber = iterator.next();
		}
		return subscriber;
	}

	@Override
	public Collection<SubscriberInfo> retrieveSubscriberListBySeatResourceNumber(String seatResourceNumber, int listLength, boolean includeCancelled) throws ApplicationException {
		return retrieveSubscriberListBySeatResourceNumber(0, seatResourceNumber, 10, includeCancelled);
	}
	
	@Override
	public Collection<SubscriberInfo> retrieveSubscriberListBySeatResourceNumber( final int ban,final String seatResourceNumber, final int maximum, final boolean includeCancelled) throws ApplicationException 	
	{
		if (seatResourceNumber == null ) {
				return new ArrayList<SubscriberInfo>();
		}
			
			String callString= "{? = call SUB_RETRIEVAL_PKG.getSubListByBanAndSeatNumber(?, ?, ?, ?, ?,?)}";
			final int resultSetMaxCount = getActualMaxSubLimit(maximum);
			
			return super.getKnowbilityJdbcTemplate().execute(callString, new CallableStatementCallback<Collection<SubscriberInfo>>() {

				@Override
				public Collection<SubscriberInfo> doInCallableStatement(
						CallableStatement callable) throws SQLException,
						DataAccessException {
					Collection<SubscriberInfo> subscriberList = null;
					ResultSet result = null;
					try{
						callable.registerOutParameter(1, OracleTypes.NUMBER);
						callable.setInt(2, ban);    
						callable.setString(3, seatResourceNumber);
						callable.setInt(4, includeCancelled ? AccountManager.NUMERIC_TRUE : AccountManager.NUMERIC_FALSE);
						callable.setInt(5, resultSetMaxCount);
						callable.registerOutParameter(6, OracleTypes.CURSOR);
						callable.registerOutParameter(7, OracleTypes.VARCHAR);

						callable.execute();

						boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;

						if (success) {
							result = (ResultSet) callable.getObject(6);
							Collection<SubscriberInfo> subscriberLists = convertSubscribersResultSet(callable.getConnection(), result, resultSetMaxCount,includeCancelled);
							subscriberList = filterVOIPAssociatedSubscriberList(subscriberLists,seatResourceNumber);
						} else {
							subscriberList = new ArrayList<SubscriberInfo>();
							String errorMessage = callable.getString(7);
							LOGGER.debug("Stored procedure failed: " + errorMessage);
						}
					}finally{
						if(result!=null) {
							result.close();
						}
					}
					return subscriberList;
				}
			});
	}
	
	private Collection<SubscriberInfo>  filterVOIPAssociatedSubscriberList(Collection<SubscriberInfo> subscriberLists,String seatResourceNumber) {
		Collection<SubscriberInfo> VOIPAssociatedSubscriberList = new ArrayList<SubscriberInfo>();

		for (SubscriberInfo info : subscriberLists) {
			if (info.getSeatData() != null) {
				Resource[] seatResources = info.getSeatData().getResources();
				for (Resource resource : seatResources) {
					if (resource.getResourceNumber().equals(seatResourceNumber)) {
						VOIPAssociatedSubscriberList.add(info);
					}
				}

			}
		}
		return VOIPAssociatedSubscriberList;
	}
	
	
	@Override
	public SubscriberInfo retrieveSubscriberByBanAndSeatResourceNumber(final int ban, final String seatResourceNumber) throws ApplicationException {
		SubscriberInfo subscriber = null;
		Collection<SubscriberInfo> subscribers;
		subscribers = retrieveSubscriberListBySeatResourceNumber(ban,seatResourceNumber, 10, false);

		Iterator<SubscriberInfo> iterator = subscribers.iterator();

		if (iterator.hasNext()) {
			subscriber = iterator.next();
		}
		return subscriber;
	}

	@Override
	public List<ResourceInfo> retrieveSeatResourceInfoByBanAndPhoneNumber(final int ban, final String phoneNumber,final boolean includeCancelled) throws ApplicationException {

		String sql = "{call SUB_RETRIEVAL_PKG.getResourceByBanAndOrPhoneNum(?,?,?,?)}";

		return super.getKnowbilityJdbcTemplate().execute(sql,new CallableStatementCallback<List<ResourceInfo>>() {
			@Override
			public List<ResourceInfo> doInCallableStatement(CallableStatement callstmt) throws SQLException,DataAccessException {
				List<ResourceInfo> seatResourceList = null;
				ResultSet result = null;
				try {
					callstmt.setInt(1, ban);
					callstmt.setString(2, phoneNumber);
					callstmt.setInt(3,includeCancelled ? AccountManager.NUMERIC_TRUE: AccountManager.NUMERIC_FALSE);
					callstmt.registerOutParameter(4, OracleTypes.CURSOR);
					callstmt.execute();
					result = (ResultSet) callstmt.getObject(4);
					seatResourceList = new ArrayList<ResourceInfo>();
					while (result.next()) {
						ResourceInfo resourceInfo = new ResourceInfo();
						resourceInfo.setResourceType(result.getString(1));
						resourceInfo.setResourceNumber(result.getString(2));
						resourceInfo.setResourceStatus(result.getString(3));
						seatResourceList.add(resourceInfo);
					}
				} finally {
					if (result != null)
						result.close();
				}
				return seatResourceList;
			}

		});
	}

	@Override
	public SubscriberIdentifierInfo retrieveLatestSubscriberIdentifierInfoBySeatResourceNumber(final int ban, final String seatResourceNumber) throws ApplicationException {
		if (seatResourceNumber == null || seatResourceNumber.trim().isEmpty()) {
			throw new ApplicationException(SystemCodes.CMB_SLH_DAO, "Invalid seatResourceNumber input [" + seatResourceNumber + "]", "");
		}
		final String callableStmt = "{call SUB_ATTRIB_RETRIEVAL_PKG.getIdentifierByBanAndSeatNum(?, ?, ?, ?)}";

		return super.getKnowbilityJdbcTemplate().execute(callableStmt, new CallableStatementCallback<SubscriberIdentifierInfo>() {
			@Override
			public SubscriberIdentifierInfo doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				SubscriberIdentifierInfo subscriberIdentifierInfo = new SubscriberIdentifierInfo();
				ResultSet rs = null;

				String formattedseatResourceNumber = AttributeTranslator.replaceString(seatResourceNumber.trim(), " ", "");
				formattedseatResourceNumber = AttributeTranslator.replaceString(formattedseatResourceNumber.trim(), "-", "");

				// set/register input/output parameters
				callable.setInt(1, ban);
				callable.setString(2, formattedseatResourceNumber);
				callable.setInt(3, (ban == 0) ? AccountManager.NUMERIC_FALSE : AccountManager.NUMERIC_TRUE);
				callable.registerOutParameter(4, OracleTypes.CURSOR);

				try {
					callable.execute();

					rs = (ResultSet) callable.getObject(4);

					if (rs.next()) {
						subscriberIdentifierInfo.setSubscriberId(rs.getString("subscriber_no"));
						subscriberIdentifierInfo.setSubscriptionId(rs.getLong("external_id"));
						subscriberIdentifierInfo.setSeatType(rs.getString("seat_type"));
						subscriberIdentifierInfo.setSeatGroup(rs.getString("seat_group"));
						subscriberIdentifierInfo.setBrandId(rs.getInt("brand_id"));
					}
				} finally {
					if (rs != null) {
						rs.close();
					}
					subscriberIdentifierInfo.setBan(ban);
					subscriberIdentifierInfo.setPhoneNumber(formattedseatResourceNumber);
				}

				return subscriberIdentifierInfo;
			}
		});
	}

	@Override
	public int updateSubscriptionRole(String phoneNumber,String subscriptionRoleCd) throws ApplicationException {
		String sql = "update subscription_role sr "
				+ "set sr.subscription_role_cd = ? "
				+ "where sr.subscription_id = ( select max(subscription_id) from subscription where cell_phone_no = ? and current_status_cd in ('A','S') )";
		int updateStatus = 0;
		try {
			updateStatus = getCodsJdbcTemplate().update(sql,new Object[] { subscriptionRoleCd, phoneNumber });
			LOGGER.debug("Cods DB updateStatus :" + updateStatus);

		} catch (Exception ex) {
			throw new ApplicationException(SystemCodes.CMB_SLH_DAO,
					ex.getMessage(), "");
		}
		return updateStatus;
	}
	

	public List<LightWeightSubscriberInfo> retrieveLightWeightSubscriberListInSameBan(int ban, String[] subscriberNumList) {

		String sql = "SELECT subscriber_no, customer_id, sub_status, product_type, brand_id, external_id, network_type, seat_type, seat_group FROM subscriber "
				+ "WHERE customer_id = :ban and subscriber_no in (:subscriberNumList)";

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("subscriberNumList", Arrays.asList(subscriberNumList));
		namedParameters.addValue("ban", ban);

		return (List<LightWeightSubscriberInfo>) getKnowbilityNamedParameterJdbcTemplate().query(sql, namedParameters, new ResultSetExtractor<List<LightWeightSubscriberInfo>>() {
			public List<LightWeightSubscriberInfo> extractData(ResultSet result) throws SQLException {
				List<LightWeightSubscriberInfo> lwsiList = new ArrayList<LightWeightSubscriberInfo>();
				while (result.next()) {
					LightWeightSubscriberInfo lwsi = new LightWeightSubscriberInfo();
					lwsi.setBanId(result.getInt("customer_id"));
					lwsi.setSubscriberId(result.getString("subscriber_no"));
					lwsi.setProductType(result.getString("product_type"));
					lwsi.setStatus(result.getString("sub_status").charAt(0));
					lwsi.setNetworkType(result.getString("network_type"));
					lwsi.setSubscriptionId(result.getLong("external_id"));
					lwsi.setPhoneNumber(lwsi.getSubscriberId());
					lwsi.setSeatType(result.getString("seat_type"));
					lwsi.setSeatGroup(result.getString("seat_group"));
					lwsiList.add(lwsi);
				}
				return lwsiList;
			}
		});
	}
	
	@Override
	public Collection<SubscriberInfo> retrieveSubscriberListByPhoneNumbersNew(final List<String> phoneNumberList, final int maximum,final boolean includeCancelled) throws ApplicationException {
		final long startTime = System.currentTimeMillis();
		if (phoneNumberList == null || phoneNumberList.isEmpty()) {
			return new ArrayList<SubscriberInfo>();
		}
		LOGGER.debug("Begin retrieveSubscriberListByPhoneNumbersNew method , phoneNumberList  "+ phoneNumberList + " with includeCancelled flag [ "+ includeCancelled + " ]");
		final int resultSetMaxCount = getActualMaxSubLimit(maximum);

		return super.getKnowbilityJdbcTemplate().execute(new ConnectionCallback<Collection<SubscriberInfo>> () {
			@Override
			public Collection<SubscriberInfo> doInConnection(Connection connection) throws SQLException, DataAccessException {
				List<String> subscriberIdList = retrieveSubscriberIdListByPhoneNumbers(connection, phoneNumberList, resultSetMaxCount, includeCancelled);
				 Collection<SubscriberInfo> subscriberInfoList = null;
				if(subscriberIdList.isEmpty()== false){
					subscriberInfoList = retrieveSubscriberListBySubscriberIds(connection, subscriberIdList, includeCancelled,resultSetMaxCount);
				} 
				LOGGER.debug("retrieveSubscriberListByPhoneNumbersNew method total execution time is [ "+(System.currentTimeMillis()-startTime)+"ms ]");
				return subscriberInfoList != null ? subscriberInfoList : new ArrayList<SubscriberInfo>();
			}
		});
	}
	
	private Collection<SubscriberInfo> retrieveSubscriberListBySubscriberIds(final Connection kbConnection, final List<String> subscriberIdList,final boolean includeCancelled,final int maximum) {
		LOGGER.debug("Begin using the retrieveSubscriberListByPhoneNumbers (Inline) method for subscriberIdList "+ subscriberIdList+" with includeCancelled flag [ "+includeCancelled+ " ]");
		final long startTime = System.currentTimeMillis();
		final String includeCancelSubSql = "SELECT DISTINCT s.customer_id, s.subscriber_no, s.sub_status,"
				+ "                            nd.first_name, nd.middle_initial,"
				+ "                            nd.last_business_name, pd.unit_esn,"
				+ "                            s.product_type, SUBSTR (s.dealer_code, 1, 10),"
				+ "                            sa.soc, s.email_address,"
				+ "                            NVL (s.init_activation_date, s.effective_date),"
				+ "                            s.init_activation_date,"
				+ "                            SUBSTR (s.dealer_code, 11),"
				+ "                            s.sub_status_rsn_code, s.sub_status_last_act,"
				+ "                            s.sub_alias,"
				+ "                            DECODE (INSTR (user_seg, '@'),0, '',SUBSTR (user_seg,INSTR (user_seg, '@') + 1,1)),"
				+ "                            sub_lang_pref, s.next_ctn, s.next_ctn_chg_date,"
				+ "                            s.prv_ctn, s.prv_ctn_chg_date,"
				+ "                            DECODE (s.sub_status, 'S', 'B', s.sub_status),"
				+ "                            s.tax_gst_exmp_ind, s.tax_pst_exmp_ind,"
				+ "                            s.tax_hst_exmp_ind, s.tax_gst_exmp_eff_dt,"
				+ "                            s.tax_pst_exmp_eff_dt, s.tax_hst_exmp_eff_dt,"
				+ "                            s.tax_gst_exmp_exp_dt, s.tax_pst_exmp_exp_dt,"
				+ "                            s.tax_hst_exmp_exp_dt, s.tax_gst_exmp_rf_no,"
				+ "                            s.tax_pst_exmp_rf_no, s.tax_hst_exmp_rf_no,"
				+ "                            s.sub_status_date, s.calls_sort_order,"
				+ "                            s.commit_reason_code, s.commit_orig_no_month,"
				+ "                            s.commit_start_date, s.commit_end_date,"
				+ "                            nd.name_suffix, nd.additional_title,"
				+ "                            nd.name_title, s.hot_line_ind, nd.name_format,"
				+ "                            s.migration_type, s.migration_date, s.tenure_date,"
				+ "                            s.req_deposit_amt, s.port_type, s.port_date,"
				+ "                            s.brand_id, NVL(s.external_id, 0) as subscription_id, s.seat_type, s.seat_group "
				+ "                       FROM subscriber s,"
				+ "                            address_name_link anl,"
				+ "                            name_data nd,"
				+ "                            physical_device pd,"
				+ "                            service_agreement sa"
				+ "                       WHERE  s.subscriber_no  IN ("+ buildInClauseString(subscriberIdList)
				+ "                        AND s.sub_status = 'C'"
				+ "                        AND anl.ban(+) = s.customer_id"
				+ "                        AND anl.subscriber_no(+) = s.subscriber_no"
				+ "                        AND anl.expiration_date IS NULL"
				+ "                        AND nd.name_id(+) = anl.name_id"
				+ "                        AND sa.ban = s.customer_id"
				+ "                        AND sa.subscriber_no = s.subscriber_no"
				+ "                        AND sa.product_type = s.product_type"
				+ "                        AND sa.service_type = 'P'"
				+ "                        AND sa.soc_seq_no ="
				+ "                               (SELECT MAX (sa1.soc_seq_no)"
				+ "                                  FROM service_agreement sa1"
				+ "                                 WHERE sa1.ban = sa.ban"
				+ "                                   AND sa1.subscriber_no = sa.subscriber_no"
				+ "                                   AND sa1.product_type = sa.product_type"
				+ "                                   AND sa1.service_type = 'P')"
				+ "                        AND pd.customer_id = s.customer_id"
				+ "                        AND pd.subscriber_no = s.subscriber_no"
				+ "                        AND pd.product_type = s.product_type"
				+ "                        AND pd.esn_seq_no ="
				+ "                               (SELECT MAX (esn_seq_no) FROM physical_device pd1 WHERE pd1.customer_id = pd.customer_id"
				+ "							   AND pd1.subscriber_no = pd.subscriber_no AND pd1.product_type = pd.product_type AND NVL (pd1.esn_level, 1) = 1)"
				+ "                   UNION ALL"
				+ "                   SELECT  DISTINCT s.customer_id, s.subscriber_no, s.sub_status,"
				+ "                            nd.first_name, nd.middle_initial,"
				+ "                            nd.last_business_name, pd.unit_esn,"
				+ "                            s.product_type, SUBSTR (s.dealer_code, 1, 10),"
				+ "                            sa.soc, s.email_address,"
				+ "                            NVL (s.init_activation_date, s.effective_date),"
				+ "                            s.init_activation_date,"
				+ "                            SUBSTR (s.dealer_code, 11), s.sub_status_rsn_code,"
				+ "                            s.sub_status_last_act, s.sub_alias,"
				+ "                            DECODE (INSTR (user_seg, '@'),0, '',SUBSTR (user_seg,INSTR (user_seg, '@') + 1,1)),"
				+ "                            sub_lang_pref, s.next_ctn, s.next_ctn_chg_date,"
				+ "                            s.prv_ctn, s.prv_ctn_chg_date,"
				+ "                            DECODE (s.sub_status, 'S', 'B', s.sub_status),"
				+ "                            s.tax_gst_exmp_ind, s.tax_pst_exmp_ind,"
				+ "                            s.tax_hst_exmp_ind, s.tax_gst_exmp_eff_dt,"
				+ "                            s.tax_pst_exmp_eff_dt, s.tax_hst_exmp_eff_dt,"
				+ "                            s.tax_gst_exmp_exp_dt, s.tax_pst_exmp_exp_dt,"
				+ "                            s.tax_hst_exmp_exp_dt, s.tax_gst_exmp_rf_no,"
				+ "                            s.tax_pst_exmp_rf_no, s.tax_hst_exmp_rf_no,"
				+ "                            s.sub_status_date, s.calls_sort_order,"
				+ "                            s.commit_reason_code, s.commit_orig_no_month,"
				+ "                            s.commit_start_date, s.commit_end_date,"
				+ "                            nd.name_suffix, nd.additional_title,"
				+ "                            nd.name_title, s.hot_line_ind, nd.name_format,"
				+ "                            s.migration_type, s.migration_date, s.tenure_date,"
				+ "                            s.req_deposit_amt, s.port_type, s.port_date,"
				+ "                            s.brand_id, NVL(s.external_id, 0) as subscription_id, s.seat_type,s.seat_group "
				+ "                       FROM subscriber s,"
				+ "                            address_name_link anl,"
				+ "                            name_data nd,"
				+ "                            physical_device pd,"
				+ "                            service_agreement sa,"
				+ "                            logical_date ld"
				+ "                      WHERE  s.subscriber_no IN ("+ buildInClauseString(subscriberIdList)
				+ "                        AND s.sub_status != 'C'"
				+ "                        AND anl.ban(+) = s.customer_id"
				+ "                        AND anl.subscriber_no(+) = s.subscriber_no"
				+ "                        AND anl.expiration_date IS NULL"
				+ "                        AND nd.name_id(+) = anl.name_id"
				+ "                        AND sa.ban = s.customer_id"
				+ "                        AND sa.subscriber_no = s.subscriber_no"
				+ "                        AND sa.product_type = s.product_type"
				+ "                        AND sa.service_type = 'P'"
				+ "                        AND (TRUNC (sa.expiration_date) > TRUNC (ld.logical_date) OR sa.expiration_date IS NULL)"
				+ "                        AND ld.logical_date_type = 'O'"
				+ "                        AND sa.effective_date ="
				+ "                               (SELECT MIN (sa1.effective_date)"
				+ "                                  FROM service_agreement sa1, logical_date ld"
				+ "                                 WHERE sa1.ban = sa.ban"
				+ "                                   AND sa1.subscriber_no = sa.subscriber_no"
				+ "                                   AND sa1.product_type = sa.product_type"
				+ "                                   AND sa1.service_type = 'P'"
				+ "                                   AND (TRUNC (sa1.expiration_date) > TRUNC (ld.logical_date) OR sa1.expiration_date IS NULL)"
				+ "                                   AND ld.logical_date_type = 'O')"
				+ "                        AND pd.customer_id = s.customer_id"
				+ "                        AND pd.subscriber_no = s.subscriber_no"
				+ "                        AND pd.product_type = s.product_type"
				+ "                        AND pd.esn_level = 1"
				+ "                        AND pd.expiration_date IS NULL"
				+ "                   ORDER BY 24, 12 DESC";
		
		
		String excludeCancelSubSql = "SELECT  DISTINCT s.customer_id, s.subscriber_no, s.sub_status,"
				+ "                            nd.first_name, nd.middle_initial,"
				+ "                            nd.last_business_name, pd.unit_esn,"
				+ "                            s.product_type, SUBSTR (s.dealer_code, 1, 10),"
				+ "                            sa.soc, s.email_address,"
				+ "                            NVL (s.init_activation_date, s.effective_date),"
				+ "                            s.init_activation_date,"
				+ "                            SUBSTR (s.dealer_code, 11), s.sub_status_rsn_code,"
				+ "                            s.sub_status_last_act, s.sub_alias,"
				+ "                            DECODE (INSTR (user_seg, '@'),0, '',SUBSTR (user_seg,INSTR (user_seg, '@') + 1,1)),"
				+ "                            sub_lang_pref, s.next_ctn, s.next_ctn_chg_date,"
				+ "                            s.prv_ctn, s.prv_ctn_chg_date,"
				+ "                            DECODE (s.sub_status, 'S', 'B', s.sub_status),"
				+ "                            s.tax_gst_exmp_ind, s.tax_pst_exmp_ind,"
				+ "                            s.tax_hst_exmp_ind, s.tax_gst_exmp_eff_dt,"
				+ "                            s.tax_pst_exmp_eff_dt, s.tax_hst_exmp_eff_dt,"
				+ "                            s.tax_gst_exmp_exp_dt, s.tax_pst_exmp_exp_dt,"
				+ "                            s.tax_hst_exmp_exp_dt, s.tax_gst_exmp_rf_no,"
				+ "                            s.tax_pst_exmp_rf_no, s.tax_hst_exmp_rf_no,"
				+ "                            s.sub_status_date, s.calls_sort_order,"
				+ "                            s.commit_reason_code, s.commit_orig_no_month,"
				+ "                            s.commit_start_date, s.commit_end_date,"
				+ "                            nd.name_suffix, nd.additional_title,"
				+ "                            nd.name_title, s.hot_line_ind, nd.name_format,"
				+ "                            s.migration_type, s.migration_date, s.tenure_date,"
				+ "                            s.req_deposit_amt, s.port_type, s.port_date,"
				+ "                            s.brand_id, NVL(s.external_id, 0) as subscription_id, s.seat_type,s.seat_group "
				+ "                       FROM subscriber s,"
				+ "                            address_name_link anl,"
				+ "                            name_data nd,"
				+ "                            physical_device pd,"
				+ "                            service_agreement sa,"
				+ "                            logical_date ld"
				+ "                      WHERE  s.subscriber_no  IN ("+ buildInClauseString(subscriberIdList)
				+ "                        AND s.sub_status != 'C'"
				+ "                        AND anl.ban(+) = s.customer_id"
				+ "                        AND anl.subscriber_no(+) = s.subscriber_no"
				+ "                        AND anl.expiration_date IS NULL"
				+ "                        AND nd.name_id(+) = anl.name_id"
				+ "                        AND sa.ban = s.customer_id"
				+ "                        AND sa.subscriber_no = s.subscriber_no"
				+ "                        AND sa.product_type = s.product_type"
				+ "                        AND sa.service_type = 'P'"
				+ "                        AND (TRUNC (sa.expiration_date) > TRUNC (ld.logical_date) OR sa.expiration_date IS NULL)"
				+ "                        AND ld.logical_date_type = 'O'"
				+ "                        AND sa.effective_date = (SELECT MIN (sa1.effective_date)"
				+ "                                  FROM service_agreement sa1, logical_date ld"
				+ "                        			  WHERE sa1.ban = sa.ban"
				+ "                                   AND sa1.subscriber_no = sa.subscriber_no"
				+ "                                   AND sa1.product_type = sa.product_type"
				+ "                                   AND sa1.service_type = 'P'"
				+ "                                   AND (TRUNC (sa1.expiration_date) > TRUNC (ld.logical_date) OR sa1.expiration_date IS NULL)"
				+ "                                   AND ld.logical_date_type = 'O')"
				+ "                        AND pd.customer_id = s.customer_id"
				+ "                        AND pd.subscriber_no = s.subscriber_no"
				+ "                        AND pd.product_type = s.product_type"
				+ "                        AND pd.esn_level = 1"
				+ "                        AND pd.expiration_date IS NULL"
				+ "                   ORDER BY 24, 12 DESC";
		
		
		
		final String sql = includeCancelled ? includeCancelSubSql: excludeCancelSubSql;
		return super.executeCallbackWithConnection(kbConnection, new ConnectionCallback<Collection<SubscriberInfo>>() {
					@Override
					public Collection<SubscriberInfo> doInConnection(Connection connection) throws SQLException,DataAccessException {
						ResultSet resultSet = connection.createStatement().executeQuery(sql);
						LOGGER.info("Step 2:  retrieveSubscriberListBySubscriberIds (Inline sql) method execution time is [  "+ (System.currentTimeMillis() - startTime) +"ms ]");
						Collection<SubscriberInfo> subscriberInfoList = convertSubscribersResultSet(connection,resultSet, maximum,includeCancelled);
						LOGGER.info("Step 3:  convertSubscribersResultSet method execution time is [  "+ (System.currentTimeMillis() - startTime) +"ms ]");
						return subscriberInfoList;
					}
				});
	}
	
	private List<String> retrieveSubscriberIdListByPhoneNumbers(final Connection kbConnection, final List<String> phoneNumberList,final int maximum,boolean includeCancelled) {
		LOGGER.debug("Begin using the retrieveSubscriberIdListByPhoneNumbers (Inline) method for phoneNumberList  "+ phoneNumberList + " with includeCancelled flag [ "+ includeCancelled + " ]");
		final long startTime = System.currentTimeMillis();

		String includeCancelSubIdListSql = "SELECT s1.subscriber_no FROM subscriber s1 WHERE  s1.subscriber_no IN ("+buildInClauseString(phoneNumberList)+" AND s1.product_type <> 'I'"
				+ "UNION"
				+ " SELECT s2.subscriber_no FROM subscriber s2, subscriber_rsource sr WHERE  sr.resource_number IN ("+buildInClauseString(phoneNumberList)+ "AND sr.resource_type = 'N' "
				+ "AND sr.resource_seq = (SELECT MAX (sr2.resource_seq)FROM subscriber_rsource sr2 WHERE  sr2.subscriber_no = sr.subscriber_no "
				+ "AND sr2.ban = sr.ban AND sr2.resource_type = 'N') AND s2.subscriber_no = sr.subscriber_no ";

		String excludeCancelSubIdListSql  = "SELECT s1.subscriber_no FROM subscriber s1 WHERE  s1.subscriber_no IN ("+buildInClauseString(phoneNumberList)+" AND s1.product_type <> 'I' AND (s1.sub_status != 'C')"
				+ "UNION"
				+ " SELECT s2.subscriber_no FROM subscriber s2, subscriber_rsource sr WHERE  sr.resource_number IN ("+buildInClauseString(phoneNumberList)+" AND sr.resource_status != 'C'"
				+ " AND sr.resource_type = 'N' AND sr.resource_seq = (SELECT MAX (sr2.resource_seq)FROM subscriber_rsource sr2 WHERE  sr2.subscriber_no = sr.subscriber_no "
				+ "AND sr2.ban = sr.ban AND sr2.resource_type = 'N') AND s2.subscriber_no = sr.subscriber_no AND (s2.sub_status != 'C')";
				
		final String sql = includeCancelled ? includeCancelSubIdListSql : excludeCancelSubIdListSql;
		return super.executeCallbackWithConnection(kbConnection,new ConnectionCallback<List<String>>() {
					@Override
					public List<String> doInConnection(Connection connection) throws SQLException, DataAccessException {
						ResultSet resultSet = connection.createStatement().executeQuery(sql);
						List<String> subscriberIdList = new ArrayList<String>();
						while (resultSet.next()) {
							subscriberIdList.add(resultSet.getString(1));
						}
						LOGGER.info("Step 1:  retrieveSubscriberIdListByPhoneNumbers (Inline sql) method execution time is [  "+ (System.currentTimeMillis() - startTime) +"ms ]");
						return subscriberIdList;
					}
				});
	}
	
	private StringBuilder buildInClauseString(List<String> values) {
		StringBuilder parameterBuilder = new StringBuilder();
		for (String value : values) {
			parameterBuilder.append("'");
			parameterBuilder.append(value);
			parameterBuilder.append("'");
			parameterBuilder.append(",");
		}
		parameterBuilder.deleteCharAt(parameterBuilder.lastIndexOf(","));
		return parameterBuilder.append(")");
	}

}
