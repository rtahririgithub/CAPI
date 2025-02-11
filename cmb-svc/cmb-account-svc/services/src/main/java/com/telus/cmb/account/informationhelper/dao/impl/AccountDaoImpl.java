package com.telus.cmb.account.informationhelper.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import com.telus.api.ApplicationException;
import com.telus.api.account.AccountManager;
import com.telus.api.account.AccountSummary;
import com.telus.api.reference.Brand;
import com.telus.cmb.account.informationhelper.dao.AccountDao;
import com.telus.cmb.account.informationhelper.mapping.AccountExtAttribMapper;
import com.telus.cmb.account.informationhelper.mapping.ContactNameMapper;
import com.telus.cmb.account.informationhelper.mapping.SubscriberCountMapper;
import com.telus.cmb.account.informationhelper.task.AccountRetrievalTask;
import com.telus.cmb.account.informationhelper.task.LightweightAccountRetrievalTask;
import com.telus.cmb.account.utilities.AppConfiguration;
import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.BillParametersInfo;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.ContactDetailInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.InvoicePropertiesInfo;
import com.telus.eas.account.info.MonthlyFinancialActivityInfo;
import com.telus.eas.account.info.PaymentHistoryInfo;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.eas.account.info.PostpaidBoxedConsumerAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessDealerAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessOfficialAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessPersonalAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessRegularAccountInfo;
import com.telus.eas.account.info.PostpaidConsumerAccountInfo;
import com.telus.eas.account.info.PostpaidCorporatePersonalAccountInfo;
import com.telus.eas.account.info.PostpaidCorporateRegularAccountInfo;
import com.telus.eas.account.info.PostpaidEmployeeAccountInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.account.info.SearchResultsInfo;
import com.telus.eas.account.info.StatusChangeHistoryInfo;
import com.telus.eas.account.info.WesternPrepaidConsumerAccountInfo;
import com.telus.eas.subscriber.info.SubscriberCountInfo;

public class AccountDaoImpl extends MultipleJdbcDaoTemplateSupport implements AccountDao {
	private String dateFormatSt = "MM/dd/yyyy";
	private SimpleDateFormat dateFormat   =  new SimpleDateFormat(dateFormatSt);
	// private static final int BRAND_ALL = 255;
	// private static final int BRAND_TELUS = 1;
	// private static final int MAX_QUERY_RESULT_COUNT = 1000;
	// private static final String NAME_TYPE_BUSINESS = "B";
	private final Logger LOGGER = Logger.getLogger(AccountDaoImpl.class);
	
	/**
	 * Refactored with the KB Capacity project in July 2013
	 */
	@Override
	public AccountInfo retrieveAccountByBan(final int ban) {
		LOGGER.debug("Begin accountDao.retrieveAccountByBan for ban [" + ban + "]...");

		if (AppConfiguration.isWRPPh3Rollback()) {
			String call = "{call ACC_RETRIEVAL_PKG.GetAccountInfoByBAN (?, ?, ?)}";

			return super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<AccountInfo>() {

				@Override
				public AccountInfo doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
					AccountInfo accountInfo = null;
					char accountType;
					char accountSubType;
					ResultSet result = null;
					Date logicalDate;

					try {
						long startTime = System.currentTimeMillis();
						callable.setInt(1, ban); // pi_ban    IN  number
						callable.registerOutParameter(2, OracleTypes.CURSOR);
						callable.registerOutParameter(3, OracleTypes.DATE);
						callable.execute();
						result = (ResultSet) callable.getObject(2);
						logicalDate = callable.getDate(3);
						if (result.next()) {
							accountType = result.getString("account_type").charAt(0);
							accountSubType = result.getString("account_sub_type").charAt(0);
							AccountRetrievalTask arTask = new AccountRetrievalTask(accountType, accountSubType, result);
							arTask.setLogicalDate(logicalDate);
							arTask.determineAccountType();
							accountInfo = arTask.createAccountInfoInstance();
							arTask.mapData();

							// Populate common Account Info
							accountInfo.setBanId(ban);
							accountInfo.setAccountType(accountType);
							accountInfo.setAccountSubType(accountSubType);

							arTask = null;
						}
						long endTime = System.currentTimeMillis();
						LOGGER.debug("ACC_RETRIEVAL_PKG.GetAccountInfoByBAN sql and mapping execution time for ban [" + ban + "] is [" + (endTime - startTime) + " ms].");
					} finally {
						if (result != null) {
							result.close();
						}
					}

					if (accountInfo != null) {
						long startTime = System.currentTimeMillis();
						retrieveAccountExtendProperties(callable.getConnection(), accountInfo);
						long endTime = System.currentTimeMillis();
						LOGGER.debug("Total execution time for retrieveAccountExtendProperties, for ban [" + ban + "] is [" + (endTime - startTime) + " ms].");
						retrieveSubscriberCounts(callable.getConnection(), accountInfo);
					}
					return accountInfo;
				}
			});
		} else {
			String sql = " SELECT DISTINCT ba.ban, ba.ban_status, ba.account_type, ba.account_sub_type, ba.status_last_date, ba.start_service_date, ba.col_delinq_status, "
                    + " ba.ar_balance, ba.auto_gen_pym_type, ba.hot_line_ind, ba.status_actv_code, ba.status_actv_rsn_code, ba.cs_ret_envlp_ind, "
                    + " NVL (TO_CHAR (ba.hierarchy_id), 'N') hierarchy_id, ba.cs_ca_rep_id, ba.inv_suppression_ind, ba.corporate_id, "
                    + " NVL (RTRIM (ba.ar_wo_ind), 'N') wo_ind, ba.home_province, DECODE (ba.national_account, 'R', 'R', 'N', 'N', NULL) national_account, "
                    + " ba.cs_handle_by_ctn_ind, ba.tax_gst_exmp_ind, ba.tax_pst_exmp_ind, ba.tax_hst_exmp_ind, ba.tax_gst_exmp_eff_dt, ba.tax_pst_exmp_eff_dt, "
                    + " ba.tax_hst_exmp_eff_dt, ba.tax_gst_exmp_exp_dt, ba.tax_pst_exmp_exp_dt, ba.tax_hst_exmp_exp_dt, ba.tax_gst_exmp_rf_no, ba.tax_pst_exmp_rf_no, "
                    + " ba.tax_hst_exmp_rf_no, ba.col_path_code, ba.col_next_step_no, ba.col_next_step_date, ba.col_agncy_code, ba.bl_man_hndl_req_opid, "
                    + " ba.bl_man_hndl_eff_date, ba.bl_man_hndl_exp_date, ba.bl_man_hndl_rsn, ba.bl_man_hndl_by_opid, ba.last_act_hotline, ba.col_delinq_sts_date, "
                    + " bc.cycle_code, bc.cycle_close_day, nbc.cycle_code next_bill_cycle, nbc.cycle_close_day next_bill_cycle_close_day, "
                    + " SUBSTR (c.dealer_code, 1, 10) dealer_code, SUBSTR (c.dealer_code, 11) sales_rep_code, c.customer_id, c.work_telno, c.work_tn_extno, "
                    + " c.home_telno, c.birth_date, c.contact_telno, c.contact_tn_extno, c.contact_faxno, c.acc_password, c.customer_ssn, c.lang_pref, c.email_address, "
                    + " c.drivr_licns_no, c.drivr_licns_state, c.drivr_licns_exp_dt, c.incorporation_no, c.incorporation_date, c.gur_cr_card_no, "
                    + " c.gur_pymt_card_first_six_str,c.gur_pymt_card_last_four_str, c.gur_cr_card_exp_dt, c.other_telno, c.other_extno, "
                    + " c.other_tn_type, c.verified_date, c.conv_run_no, ad.adr_primary_ln, ad.adr_city, ad.adr_province, ad.adr_postal, adr_type, adr_secondary_ln, "
                    + " adr_country, adr_zip_geo_code, adr_state_code, civic_no, civic_no_suffix, adr_st_direction, adr_street_name, adr_street_type, adr_designator, "
                    + " adr_identifier, adr_box, unit_designator, unit_identifier, adr_area_nm, adr_qualifier, adr_site, adr_compartment, adr_attention, "
                    + " adr_delivery_tp, adr_group, nd.first_name, nd.last_business_name, middle_initial, name_title, additional_title, name_format, bdd.credit_card_no, "
                    + " bdd.pymt_card_first_six_str, bdd.pymt_card_last_four_str, bdd.card_mem_hold_nm, bdd.expiration_date, bdd.bnk_code, bdd.bnk_acct_number, "
                    + " bdd.bnk_acct_type, bdd.bnk_branch_number, bdd.dd_status, bdd.status_reason, cs.effective_date cs_effective_date, cs.cu_past_due_amt, "
                    + " cs.cu_age_bucket_0, cs.cu_age_bucket_1_30, cs.cu_age_bucket_31_60, cs.cu_age_bucket_61_90, cs.cu_age_bucket_91_plus, nx_past_due_amt, "
                    + " nx_age_bucket_0, nx_age_bucket_1_30, nx_age_bucket_31_60, nx_age_bucket_61_90, nx_age_bucket_91_plus, fu_past_due_amt, fu_age_bucket_0, "
                    + " fu_age_bucket_1_30, fu_age_bucket_31_60, fu_age_bucket_61_90, fu_age_bucket_91_plus, name_suffix, NVL(ba.brand_id, 1) brand_ind, "
                    + " ba.gl_segment, ba.gl_subsegment "
                    + " FROM billing_account ba, customer c, address_name_link anl, address_data ad, name_data nd, ban_direct_debit bdd, CYCLE bc, collection_status cs, CYCLE nbc " 
                    + " WHERE ba.ban = :ban AND c.customer_id = ba.customer_id AND anl.customer_id = c.customer_id "
                    + " AND (TRUNC (anl.expiration_date) > TRUNC (TO_DATE(:logicalDate,'YYYYMMDD')) OR anl.expiration_date IS NULL) "
                    + " AND anl.link_type = 'B' AND ad.address_id = anl.address_id AND nd.name_id = anl.name_id AND bdd.ban(+) = ba.ban "
                    + " AND bc.cycle_code(+) = ba.bill_cycle AND nbc.cycle_code(+) = ba.bl_next_cycle AND cs.ban(+) = ba.ban ";
			
			return (AccountInfo) getKnowbilityJdbcTemplate().execute(sql, new PreparedStatementCallback<AccountInfo>() {	
	
				@Override
				public AccountInfo doInPreparedStatement(PreparedStatement pstmt) throws SQLException, DataAccessException {
					AccountInfo accountInfo = null;
					char accountType;
					char accountSubType;
					Date logicalDate = null;
					ResultSet rs = null;
					
					try {
						logicalDate = retrieveLogicalDate(pstmt.getConnection());
						
						pstmt.setInt(1, ban);
						pstmt.setString(2, new SimpleDateFormat("yyyyMMdd").format(logicalDate));
						
						long startTime = System.currentTimeMillis();
						rs = pstmt.executeQuery();
						
						if (rs.next()) {
							accountType = rs.getString("account_type").charAt(0);
							accountSubType = rs.getString("account_sub_type").charAt(0);
							AccountRetrievalTask arTask = new AccountRetrievalTask(accountType, accountSubType, rs);
							arTask.setLogicalDate(logicalDate);
							arTask.determineAccountType();
							accountInfo = arTask.createAccountInfoInstance();
							arTask.mapData();
							arTask = null;
							
							// Populate common Account Info
							accountInfo.setBanId(ban);
							accountInfo.setAccountType(accountType);
							accountInfo.setAccountSubType(accountSubType);
						}
						long endTime = System.currentTimeMillis();
						LOGGER.debug("ACC_RETRIEVAL_PKG.GetAccountInfoByBAN sql and mapping execution time for ban [" + ban + "] is [" + (endTime - startTime) + " ms].");
					} finally {
						if (rs != null) {
							rs.close();
						}
					}

					if (accountInfo != null) {
						long startTime = System.currentTimeMillis();
						retrieveAccountExtendProperties(pstmt.getConnection(), accountInfo);
						long endTime = System.currentTimeMillis();
						LOGGER.debug("Total execution time for retrieveAccountExtendProperties, for ban [" + ban + "] is [" + (endTime - startTime) + " ms].");
						retrieveSubscriberCounts(pstmt.getConnection(), accountInfo);
					}
					
					return accountInfo;
				}
			});
		}
	}
	
	@Override
	public void retrieveAccountExtendProperties(final AccountInfo accountInfo) {
		if (accountInfo == null || accountInfo.getBanId() == 0) {
			return;
		}
			
		super.getKnowbilityJdbcTemplate().execute(new ConnectionCallback<Object>() {

			@Override
			public Object doInConnection(Connection connection) throws SQLException, DataAccessException {
				retrieveAccountExtendProperties(connection, accountInfo);
				
				return null;
			}

			
		});
	}
	
	private void retrieveAccountExtendProperties(Connection connection, final AccountInfo accountInfo) {
		LOGGER.debug("Begin retrieveAccountExtendProperties for ban [" + accountInfo.getBanId() + "]...");
		String call = "{call ACC_RETRIEVAL_PKG.getAccountExtPropertiesByBan (?, ?, ?, ?, ?, ?, ?, ? ,? ,?, ?, ?, ?, ?, ?)}";
		
		executeCallableWithConnection(connection, call, new CallableStatementCallback<Object>() {

			@Override
			public Object doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {
				ResultSet consentIndicatorCursor = null;
				ResultSet collectionCursor = null;
				ResultSet dishonouredPaymentsCursor = null;
				ResultSet financeHistoryCursor = null;
				ResultSet contactNameDetailCursor = null;
				
				try {				
					callable.setInt(1, accountInfo.getBanId());
					callable.registerOutParameter(2, OracleTypes.CURSOR);
					callable.registerOutParameter(3, OracleTypes.CURSOR);
					callable.registerOutParameter(4, OracleTypes.CURSOR);
					callable.registerOutParameter(5, OracleTypes.CURSOR);
					callable.registerOutParameter(6, OracleTypes.CURSOR);
					callable.registerOutParameter(7, OracleTypes.DATE);
					callable.registerOutParameter(8, OracleTypes.NUMBER);
					callable.registerOutParameter(9, OracleTypes.VARCHAR);
					callable.registerOutParameter(10, OracleTypes.DATE);
					callable.registerOutParameter(11, OracleTypes.DATE);
					callable.registerOutParameter(12, OracleTypes.NUMBER);
					callable.registerOutParameter(13, OracleTypes.DATE);
					callable.registerOutParameter(14, OracleTypes.NUMBER);
					callable.registerOutParameter(15, OracleTypes.VARCHAR);
					callable.execute();
					consentIndicatorCursor = (ResultSet) callable.getObject(2);
					collectionCursor = (ResultSet) callable.getObject(3);
					dishonouredPaymentsCursor = (ResultSet) callable.getObject(4);
					financeHistoryCursor = (ResultSet) callable.getObject(5);
					contactNameDetailCursor = (ResultSet) callable.getObject(6);
					
					AccountExtAttribMapper.mapClientConsentIndicatorCodes(accountInfo, consentIndicatorCursor);
					ContactNameMapper.transform(accountInfo, contactNameDetailCursor);
					AccountExtAttribMapper.setMonthlyFinancialDetails(accountInfo, collectionCursor, dishonouredPaymentsCursor, financeHistoryCursor);
					
					int collectionStepCode = callable.getInt(8);
					String collectionActivityCode = callable.getString(9);
					Date collectionActivityDate = callable.getDate(10);
					accountInfo.getFinancialHistory0().getCollectionStep0().setStep(collectionStepCode);
					accountInfo.getFinancialHistory0().getCollectionStep0().setCollectionActivityCode(collectionActivityCode);
					accountInfo.getFinancialHistory0().getCollectionStep0().setTreatmentDate(collectionActivityDate);
					
					accountInfo.getFinancialHistory0().getDebtSummary0().setBillDueDate(callable.getTimestamp(7)); //bill due date
					
					accountInfo.getFinancialHistory0().setWrittenOffDate(callable.getDate(11)); //written off date
					
					//PCI - change:
					PaymentHistoryInfo lastPayment = new PaymentHistoryInfo();
					lastPayment.setOriginalAmount(callable.getDouble(12));
					//PROD00176130 fix: populate the deposit date  to reflect the actual table column we retrieve this data from.
					lastPayment.setDepositDate(callable.getTimestamp(13));
					lastPayment.setActualAmount(callable.getDouble(14) );
					lastPayment.setActivityCode(callable.getString(15) );
					accountInfo.getFinancialHistory0().setLastPayment( lastPayment );  

				}finally {
					if (consentIndicatorCursor != null) {
						consentIndicatorCursor.close();
					}
					
					if (collectionCursor != null) {
						collectionCursor.close();
					}
					
					if (dishonouredPaymentsCursor != null) {
						dishonouredPaymentsCursor.close();
					}
					
					if (financeHistoryCursor != null) {
						financeHistoryCursor.close();
					}
					
					if (contactNameDetailCursor != null) {
						contactNameDetailCursor.close();
					}
				}
				return null;
			}
		});
		
	}
	
	@Override
	public SubscriberCountInfo retrieveSubscriberCounts(final int ban, final char accountType, final char accountSubType) {
		
		return super.getKnowbilityJdbcTemplate().execute(new ConnectionCallback<SubscriberCountInfo>() {

			@Override
			public SubscriberCountInfo doInConnection(Connection connection) throws SQLException, DataAccessException {
				return retrieveSubscriberCounts (connection, ban, accountType, accountSubType);
			}
		});
	}
	
	private void retrieveSubscriberCounts(Connection connection, AccountInfo accountInfo) {
		LOGGER.debug("Begin retrieveSubscriberCounts for ban [" + accountInfo.getBanId() + "]...");
		long startTime = System.currentTimeMillis();
		if (connection != null && accountInfo != null) {
			if (accountInfo.getBanId() != 0) {
				SubscriberCountInfo subCountInfo = retrieveSubscriberCounts (connection, accountInfo.getBanId(), accountInfo.getAccountType(), accountInfo.getAccountSubType());
				SubscriberCountMapper.mapSubscriberCount(accountInfo, subCountInfo);
			}
		}
		long endTime = System.currentTimeMillis();
		LOGGER.debug("Total execution time for retrieveSubscriberCounts for ban [" + accountInfo.getBanId() + "] is [" + (endTime - startTime) + "ms]." );
	}
	
	private SubscriberCountInfo retrieveSubscriberCounts(Connection connection, final int ban, final char accountType, final char accountSubType) {

		String call = "{call SUBSCRIBER_COUNT_PKG.getSubscriberCounts(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
		
		return executeCallableWithConnection(connection, call, new CallableStatementCallback<SubscriberCountInfo>() {

			@Override
			public SubscriberCountInfo doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				SubscriberCountInfo subscriberCountInfo = new SubscriberCountInfo();
				
				callable.setInt(1, ban);
				callable.setString(2, String.valueOf(accountType));
				callable.setString(3, String.valueOf(accountSubType));
				callable.registerOutParameter(4, Types.NUMERIC);
				callable.registerOutParameter(5, Types.NUMERIC);
				callable.registerOutParameter(6, Types.NUMERIC);
				callable.registerOutParameter(7, Types.NUMERIC);
				callable.registerOutParameter(8, Types.NUMERIC);
				callable.registerOutParameter(9, Types.NUMERIC);
				callable.registerOutParameter(10, Types.NUMERIC);
				callable.registerOutParameter(11, Types.NUMERIC);
				
				callable.execute();
				
				subscriberCountInfo.setActiveSubscribersCount(callable.getInt(4));
				subscriberCountInfo.setReservedSubscribersCount(callable.getInt(5));
				subscriberCountInfo.setSuspendedSubscribersCount(callable.getInt(6));
				subscriberCountInfo.setCancelledSubscribersCount(callable.getInt(7));
				subscriberCountInfo.setAllActiveSubscribersCount(callable.getInt(8));
				subscriberCountInfo.setAllReservedSubscribersCount(callable.getInt(9));
				subscriberCountInfo.setAllSuspendedSubscribersCount(callable.getInt(10));
				subscriberCountInfo.setAllCancelledSubscribersCount(callable.getInt(11));
				return subscriberCountInfo;
			}
			
		});
	}



	@Override
	public AccountInfo retrieveAccountByBanRollback(final int ban) {				

		String call = "{call RA_Utility_pkg.GetAccountInfoByBAN " +
		"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, " +
		" ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, " +
		" ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, " +
		" ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, " +
		" ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, " +
		" ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, " +
		" ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, " +
		" ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, " +
		" ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		return super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<AccountInfo>() {

			@Override
			public AccountInfo doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {
				AccountInfo accountInfo = null;
				char accountType;
				char accountSubType;
				boolean postpaidConsumer = false;
				boolean postpaidBusinessRegular = false;
				boolean postpaidBusinessPersonal = false;
				boolean postpaidBusinessDealer = false;
				boolean postpaidBusinessOffical = false;
				boolean prepaidConsumer = false;
				boolean quebecTelPrepaidConsumer = false;
				boolean westernPrepaidConsumer = false;
				boolean IDENCorporateVPN = false;
				boolean IDENCorporate = false;
				boolean postpaidLikeBusinessRegular = false;
				boolean postpaidBoxedConsumer = false;
				boolean postpaidCorporateRegional = false;
				boolean postpaidConsumerEmployee = false;
				boolean postpaidConsumerEmployeeNew = false;
				boolean postpaidCorporateAutotel = false;
				boolean postpaidCorporatePersonal = false;
				boolean postpaidCorporateRegular = false;
				boolean delinqent;
				String currentDateString;
				String[] consentIndicatorsList = new String[0];
				int currentMonth;
				int currentYear;
				int i, m;

				callable.setInt(1, ban);// pi_ban    IN  number
				callable.registerOutParameter(2, Types.VARCHAR);//po_ban_status  OUT char
				callable.registerOutParameter(3, Types.CHAR);//po_account_type OUT char
				callable.registerOutParameter(4, Types.CHAR);//po_account_sub_type OUT char
				callable.registerOutParameter(5, Types.TIMESTAMP);//po_create_date OUT date
				callable.registerOutParameter(6, Types.TIMESTAMP);//po_start_service_date OUT date
				callable.registerOutParameter(7, Types.VARCHAR);//po_col_delinq_status  OUT varchar2
				callable.registerOutParameter(8, Types.NUMERIC);//po_ar_balance OUT number
				callable.registerOutParameter(9, Types.VARCHAR);//po_dealer_code    OUT varchar2
				callable.registerOutParameter(10, Types.VARCHAR);//po_sales_rep_code  OUT varchar2
				callable.registerOutParameter(11, Types.NUMERIC);//po_bill_cycle    OUT number
				callable.registerOutParameter(12, Types.CHAR);//po_payment_method OUT varchar2
				callable.registerOutParameter(13, Types.VARCHAR);//po_work_telno   OUT varchar2
				callable.registerOutParameter(14, Types.VARCHAR);//po_work_tn_extno  OUT varchar2
				callable.registerOutParameter(15, Types.VARCHAR);//po_home_telno   OUT varchar2
				callable.registerOutParameter(16, Types.DATE);//po_birth_date    OUT date
				callable.registerOutParameter(17, Types.VARCHAR);//po_contact_faxno  OUT varchar2
				callable.registerOutParameter(18, Types.VARCHAR);//po_acc_password OUT varchar2
				callable.registerOutParameter(19, Types.VARCHAR);//po_customer_ssn OUT varchar2
				callable.registerOutParameter(20, Types.VARCHAR);//po_adr_primary_ln OUT varchar2
				callable.registerOutParameter(21, Types.VARCHAR);//po_adr_city   OUT varchar2
				callable.registerOutParameter(22, Types.VARCHAR);//po_adr_province OUT varchar2
				callable.registerOutParameter(23, Types.VARCHAR);//po_adr_postal   OUT varchar2
				callable.registerOutParameter(24, Types.VARCHAR);//po_first_name   OUT varchar2
				callable.registerOutParameter(25, Types.VARCHAR);//po_last_business_name OUT varchar2
				callable.registerOutParameter(26, Types.VARCHAR);//po_special_instruction OUT  varchar2
				callable.registerOutParameter(27, Types.NUMERIC);//po_cu_age_bucket_0  OUT number
				callable.registerOutParameter(28, Types.NUMERIC);//po_cu_age_bucket_1_30 OUT number
				callable.registerOutParameter(29, Types.NUMERIC);//po_cu_age_bucket_31_60 OUT  number
				callable.registerOutParameter(30, Types.NUMERIC);//po_cu_age_bucket_61_90  OUT number
				callable.registerOutParameter(31, Types.NUMERIC);//po_cu_age_bucket_91_plus OUT  number
				callable.registerOutParameter(32, Types.NUMERIC);//po_cu_past_due_amt  OUT number
				callable.registerOutParameter(33, Types.NUMERIC);//po_active_subs  OUT number
				callable.registerOutParameter(34, Types.NUMERIC);//po_reserved_subs  OUT number
				callable.registerOutParameter(35, Types.NUMERIC);//po_suspended_subs OUT number
				callable.registerOutParameter(36, Types.NUMERIC);//po_cancelled_subs OUT number
				callable.registerOutParameter(37, Types.CHAR);//po_col_actv_JAN    OUT     char
				callable.registerOutParameter(38, Types.CHAR);//po_col_actv_FEB   OUT     char
				callable.registerOutParameter(39, Types.CHAR);//po_col_actv_MAR   OUT     char
				callable.registerOutParameter(40, Types.CHAR);//po_col_actv_APR   OUT     char
				callable.registerOutParameter(41, Types.CHAR);//,po_col_actv_MAY    OUT     char
				callable.registerOutParameter(42, Types.CHAR);//po_col_actv_JUN   OUT     char
				callable.registerOutParameter(43, Types.CHAR);//po_col_actv_JUL   OUT     char
				callable.registerOutParameter(44, Types.CHAR);//po_col_actv_AUG   OUT     char
				callable.registerOutParameter(45, Types.CHAR);//po_col_actv_SEP   OUT     char
				callable.registerOutParameter(46, Types.CHAR);//po_col_actv_OCT   OUT     char
				callable.registerOutParameter(47, Types.CHAR);//po_col_actv_NOV   OUT     char
				callable.registerOutParameter(48, Types.CHAR);//po_col_actv_DEC   OUT     char
				callable.registerOutParameter(49, Types.NUMERIC);//po_DCK_JAN   OUT     number
				callable.registerOutParameter(50, Types.NUMERIC);//po_DCK_FEB   OUT     number
				callable.registerOutParameter(51, Types.NUMERIC);//po_DCK_MAR   OUT     number
				callable.registerOutParameter(52, Types.NUMERIC);//po_DCK_APR   OUT     number
				callable.registerOutParameter(53, Types.NUMERIC);//po_DCK_MAY   OUT     number
				callable.registerOutParameter(54, Types.NUMERIC);//po_DCK_JUN   OUT     number
				callable.registerOutParameter(55, Types.NUMERIC);//po_DCK_JUL   OUT     number
				callable.registerOutParameter(56, Types.NUMERIC);//po_DCK_AUG   OUT     number
				callable.registerOutParameter(57, Types.NUMERIC);//po_DCK_SEP   OUT     number
				callable.registerOutParameter(58, Types.NUMERIC);//po_DCK_OCT   OUT     number
				callable.registerOutParameter(59, Types.NUMERIC);//po_DCK_NOV   OUT     number
				callable.registerOutParameter(60, Types.NUMERIC);//po_DCK_DEC   OUT     number
				callable.registerOutParameter(61, Types.NUMERIC);//po_last_payment_amnt OUT   number
				callable.registerOutParameter(62, Types.TIMESTAMP);//po_last_payment_date OUT date
				callable.registerOutParameter(63, Types.VARCHAR);//po_hotline_ind   OUT     char
				callable.registerOutParameter(64, Types.NUMERIC);//po_customer_id  OUT     number
				callable.registerOutParameter(65, Types.VARCHAR);//po_language   OUT varchar2
				callable.registerOutParameter(66, Types.VARCHAR);//po_email    OUT varchar2
				callable.registerOutParameter(67, Types.VARCHAR);//po_adr_type     OUT varchar2
				callable.registerOutParameter(68, Types.VARCHAR);//po_adr_secondary_ln  OUT varchar2
				callable.registerOutParameter(69, Types.VARCHAR);//po_adr_country    OUT varchar2
				callable.registerOutParameter(70, Types.VARCHAR);//po_adr_zip_geo_code  OUT varchar2
				callable.registerOutParameter(71, Types.VARCHAR);//po_adr_state_code   OUT varchar2
				callable.registerOutParameter(72, Types.VARCHAR);//po_civic_no    OUT varchar2
				callable.registerOutParameter(73, Types.VARCHAR);//po_civic_no_suffix  OUT varchar2
				callable.registerOutParameter(74, Types.VARCHAR); //po_adr_st_direction OUT varchar2
				callable.registerOutParameter(75, Types.VARCHAR);//po_adr_street_name  OUT varchar2
				callable.registerOutParameter(76, Types.VARCHAR);//po_adr_street_type   OUT varchar2
				callable.registerOutParameter(77, Types.VARCHAR);//po_adr_designator     OUT varchar2
				callable.registerOutParameter(78, Types.VARCHAR);//po_adr_identifier OUT varchar2
				callable.registerOutParameter(79, Types.VARCHAR);//po_adr_box    OUT varchar2
				callable.registerOutParameter(80, Types.VARCHAR);//po_unit_designator  OUT varchar2
				callable.registerOutParameter(81, Types.VARCHAR);//po_unit_identifier  OUT varchar2
				callable.registerOutParameter(82, Types.VARCHAR);//po_adr_area_nm    OUT varchar2
				callable.registerOutParameter(83, Types.VARCHAR);//po_adr_qualifier  OUT varchar2
				callable.registerOutParameter(84, Types.VARCHAR);//po_adr_site     OUT varchar2
				callable.registerOutParameter(85, Types.VARCHAR);//po_adr_compartment    OUT varchar2
				callable.registerOutParameter(86, Types.VARCHAR);//po_middle_initial OUT varchar2
				callable.registerOutParameter(87, Types.VARCHAR);//po_name_title         OUT varchar2
				callable.registerOutParameter(88, Types.VARCHAR);//po_additional_title OUT varchar2
				callable.registerOutParameter(89, Types.VARCHAR);//po_contact_last_name  OUT varchar2
				callable.registerOutParameter(90, Types.VARCHAR);//po_drivr_licns_no     OUT varchar2
				callable.registerOutParameter(91, Types.VARCHAR);//po_drivr_licns_state  OUT varchar2
				callable.registerOutParameter(92, Types.TIMESTAMP);//po_drivr_licns_exp_dt   OUT     date
				callable.registerOutParameter(93, Types.VARCHAR);//po_incorporation_no        OUT  varchar2
				callable.registerOutParameter(94, Types.TIMESTAMP);//po_incorporation_date   OUT  date
				callable.registerOutParameter(95, Types.VARCHAR);//po_gur_cr_card_no           OUT  varchar2
				callable.registerOutParameter(96, Types.NUMERIC);//po_gur_cr_card_exp_dt_mm  OUT  number
				callable.registerOutParameter(97, Types.NUMERIC);//po_gur_cr_card_exp_dt_yyyy OUT number
				callable.registerOutParameter(98, Types.VARCHAR);//po_credit_card_no      OUT varchar2
				callable.registerOutParameter(99, Types.VARCHAR);//po_card_mem_hold_nm    OUT varchar2
				callable.registerOutParameter(100, Types.NUMERIC); //po_expiration_date_mm   OUT  number
				callable.registerOutParameter(101, Types.NUMERIC);//po_expiration_date_yyyy   OUT number
				callable.registerOutParameter(102, Types.VARCHAR);//po_status_actv_code      OUT varchar2
				callable.registerOutParameter(103, Types.VARCHAR);//po_status_actv_rsn_code      OUT varchar2
				callable.registerOutParameter(104, Types.NUMERIC);//po_bill_cycle_close_day  OUT number
				callable.registerOutParameter(105, Types.VARCHAR);//po_return_envelope_ind   OUT varchar2
				callable.registerOutParameter(106, Types.TIMESTAMP); //po_bill_due_date    OUT date
				callable.registerOutParameter(107, Types.VARCHAR);//po_corp_hierarhy_ind          OUT varchar2
				callable.registerOutParameter(108, Types.VARCHAR);//po_corp_csr_id    OUT varchar2
				callable.registerOutParameter(109, Types.VARCHAR);//po_inv_supression_ind  OUT varchar2
				callable.registerOutParameter(110, Types.VARCHAR); //po_bankCode      OUT varchar2
				callable.registerOutParameter(111, Types.VARCHAR);//po_bankAccountNumber         OUT varchar2
				callable.registerOutParameter(112, Types.VARCHAR);//po_bankBranchNumber     OUT varchar2
				callable.registerOutParameter(113, Types.VARCHAR);//po_bankAccountType  OUT varchar2
				callable.registerOutParameter(114, Types.VARCHAR);//po_directDebitStatus    OUT varchar2
				callable.registerOutParameter(115, Types.VARCHAR);//po_directDebitStatusRsn    OUT varchar2
				callable.registerOutParameter(116, Types.VARCHAR);//po_other_phone    OUT     varchar2
				callable.registerOutParameter(117, Types.VARCHAR);//po_other_phone_ext    OUT     varchar2
				callable.registerOutParameter(118, Types.VARCHAR);//po_other_phone_type      OUT     varchar2
				callable.registerOutParameter(119, Types.VARCHAR);//po_tax_gst_exmp_ind		OUT	varchar2
				callable.registerOutParameter(120, Types.VARCHAR);//po_tax_pst_exmp_ind 		OUT	varchar2
				callable.registerOutParameter(121, Types.VARCHAR);//po_tax_hst_exmp_ind		OUT	varchar2
				callable.registerOutParameter(122, Types.TIMESTAMP);//po_tax_gst_exmp_exp_dt	OUT	date
				callable.registerOutParameter(123, Types.TIMESTAMP);//po_tax_pst_exmp_exp_dt	OUT	date
				callable.registerOutParameter(124, Types.TIMESTAMP);//po_tax_hst_exmp_exp_dt	OUT     date
				callable.registerOutParameter(125, Types.VARCHAR);//po_home_province 		OUT     varchar2
				callable.registerOutParameter(126, Types.VARCHAR);//po_category  			OUT     varchar2
				callable.registerOutParameter(127, Types.NUMERIC);//po_next_bill_cycle		OUT     number
				callable.registerOutParameter(128, Types.NUMERIC);//po_next_bill_cycle_close_day	OUT     number
				callable.registerOutParameter(129, Types.TIMESTAMP);//po_verified_date  		OUT     date
				callable.registerOutParameter(130, Types.VARCHAR);//po_handle_by_subscriber_ind 	OUT     varchar2
				callable.registerOutParameter(131, Types.VARCHAR);//po_corporate_id		OUT	varchar2
				callable.registerOutParameter(132, Types.VARCHAR);//po_write_off_ind		OUT	varchar2
				callable.registerOutParameter(133, Types.VARCHAR);//po_contact_first_name	OUT	varchar2
				callable.registerOutParameter(134, Types.VARCHAR);// po_contact_name_title      OUT	varchar2
				callable.registerOutParameter(135, Types.VARCHAR);// po_contact_middle_initial	OUT	varchar2
				callable.registerOutParameter(136, Types.VARCHAR);// po_contact_additional_title	OUT	varchar2
				callable.registerOutParameter(137, Types.VARCHAR);// po_contact_name_suffix	OUT	varchar2
				callable.registerOutParameter(138, Types.VARCHAR);// po_contact_phone_number	OUT	varchar2
				callable.registerOutParameter(139, Types.VARCHAR);// po_contact_phone_number_ext	OUT	varchar2
				callable.registerOutParameter(140, Types.VARCHAR);// po_legal_business_name	OUT	varchar2
				callable.registerOutParameter(141, Types.VARCHAR);// po_col_path_code		OUT	varchar2
				callable.registerOutParameter(142, Types.NUMERIC);// po_col_step			OUT     number
				callable.registerOutParameter(143, Types.VARCHAR);// po_col_actv_code		OUT	varchar2
				callable.registerOutParameter(144, Types.DATE);// po_col_actv_date		OUT	date
				callable.registerOutParameter(145, Types.NUMERIC);// po_col_next_step		OUT     number
				callable.registerOutParameter(146, Types.DATE);// po_col_next_actv_date	OUT	date
				callable.registerOutParameter(147, Types.VARCHAR);// po_col_agency		OUT	varchar2
				callable.registerOutParameter(148, Types.VARCHAR); //po_adr_attention		OUT	varchar2
				callable.registerOutParameter(149, Types.VARCHAR); //po_adr_delivery_type	OUT	varchar2
				callable.registerOutParameter(150, Types.VARCHAR);  //po_adr_group		OUT     varchar2
				callable.registerOutParameter(151, Types.VARCHAR);//po_tax_gst_exmp_rf_no		OUT	varchar2
				callable.registerOutParameter(152, Types.VARCHAR);//po_tax_pst_exmp_rf_no 	OUT	varchar2
				callable.registerOutParameter(153, Types.VARCHAR);//po_tax_hst_exmp_rf_no		OUT	varchar2
				callable.registerOutParameter(154, Types.DATE);//po_tax_gst_exmp_eff_dt	OUT	date
				callable.registerOutParameter(155, Types.DATE);//po_tax_pst_exmp_eff_dt	OUT	date
				callable.registerOutParameter(156, Types.DATE);//po_tax_hst_exmp_eff_dt	OUT     date
				callable.registerOutParameter(157, Types.DATE);//po_status_last_date		OUT     date
				callable.registerOutParameter(158, Types.NUMERIC);//po_conv_run_no		OUT     number
				callable.registerOutParameter(159, Types.VARCHAR);//po_client_cons_ind	OUT		OUT     varchar2)
				callable.registerOutParameter(160, Types.NUMERIC);//po_all_active_subs   OUT	    number
				callable.registerOutParameter(161, Types.NUMERIC);//po_all_reserved_subs OUT	    number
				callable.registerOutParameter(162, Types.NUMERIC);//po_all_suspended_subs OUT	number
				callable.registerOutParameter(163, Types.NUMERIC);//po_all_cancelled_subs OUT	number
				callable.registerOutParameter(164, Types.NUMERIC);//po_bl_man_hndl_req_opid OUT    number
				callable.registerOutParameter(165, Types.DATE);//po_bl_man_hndl_eff_date    OUT date
				callable.registerOutParameter(166, Types.DATE);//po_bl_man_hndl_exp_date    OUT date
				callable.registerOutParameter(167, Types.VARCHAR);//po_bl_man_hndl_rsn    OUT varchar2
				callable.registerOutParameter(168, Types.NUMERIC);//po_bl_man_hndl_by_opid     OUT number
				callable.registerOutParameter(169, Types.NUMERIC);//po_last_payment_actual_amt  OUT number
				callable.registerOutParameter(170, Types.VARCHAR);// po_name_suffix	OUT	varchar2
				callable.registerOutParameter(171, Types.DATE);// po_last_act_hotline	OUT	date
				callable.registerOutParameter(172, Types.DATE);// po_col_delinq_sts_date	OUT	date
				callable.registerOutParameter(173, Types.DATE);// po_col_written_off_date	OUT	date
				callable.registerOutParameter(174, Types.NUMERIC);// po_brand_ind	OUT	NUMBER
				callable.registerOutParameter(175, Types.VARCHAR);// po_gl_segment	OUT	varchar2
				callable.registerOutParameter(176, Types.VARCHAR);// po_gl_subsegment	OUT	varchar2

				//PCI changes
				callable.registerOutParameter(177, Types.VARCHAR);// po_gur_cr_card_first6 	OUT	varchar2
				callable.registerOutParameter(178, Types.VARCHAR);// po_gur_cr_card_last4 	OUT	varchar2
				callable.registerOutParameter(179, Types.VARCHAR);// po_payment_card_first6 	OUT	varchar2
				callable.registerOutParameter(180, Types.VARCHAR);// po_payment_card_last4 	OUT	varchar2
				callable.registerOutParameter(181, Types.VARCHAR);// po_last_payment_actv_code  OUT	varchar2

				callable.execute();

				accountType = callable.getString(3).charAt(0);
				accountSubType = callable.getString(4).charAt(0);
				if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER  && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PAGER_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_AUTOTEL_REGULAR)){
					postpaidConsumer = true;
				} else if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER  && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_TELUS_EMPLOYEE || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_TELUS_EMPLOYEE )) {
					postpaidConsumerEmployee = true;
				} else if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER  && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_TELUS_EMPLOYEE_NEW || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_PERSONAL )) {
					postpaidConsumerEmployeeNew = true;
				} else if (accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_REGULAR ||accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_REGULAR || accountSubType==AccountSummary.ACCOUNT_SUBTYPE_PAGER_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_AUTOTEL_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_CONNECT_REGULAR )) {
					postpaidBusinessRegular = true ;
				} else if (accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_OFFICAL)	{
					postpaidBusinessOffical = true ;
				} else if (accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS &&( accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_PERSONAL || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_PERSONAL|| accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_PERSONAL || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_CONNECT_PERSONAL )) {
					postpaidBusinessPersonal = true;
				} else if (accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_DEALER || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_DEALER)) {
					postpaidBusinessDealer = true;
				} else if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_PREPAID) {
					prepaidConsumer = true ;
				} else if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_QUEBECTEL) { 
					quebecTelPrepaidConsumer = true ;
				} else if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_WESTERN_PREPAID)	{
					westernPrepaidConsumer = true ;
				} else if (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_REGULAR) {
					IDENCorporate = true;
				} else if (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_PRIVATE_NETWORK_PLUS) {
					IDENCorporateVPN = true;
				} else if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PAGER_BOXED)	{
					postpaidBoxedConsumer = true;
				} else if (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE  && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_REGIONAL) {
					postpaidCorporateRegional = true;
				} else if (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE  && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_AUTOTEL_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_AUTOTEL_EARS)) {
					postpaidCorporateAutotel = true;
				} else if (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE  && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_INDIVIDUAL || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_EMPLOYEE)) {
					postpaidCorporatePersonal = true;
				} else if (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE  &&
						(accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_FEDERAL_GOVERNMENT ||
								accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_ENTERPRISE ||
								accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_CORPORATE ||
								accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_ABORIGINAL ||
								accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_FUSION_EAST_CONV ||
								accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_KEY ||
								accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_OFFICIAL ||
								accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_CNBS ||
								accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_GOVERNMENT ||
								accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_NATIONAL_STRATEGIC ||
								accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_REGIONAL_STRATEGIC ||
								accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_TMI_AFFILIATE ||
								accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_ANYWHERE ||
								accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_TMI_DIVISION)) {
					postpaidCorporateRegular = true;
				} else {
					postpaidLikeBusinessRegular = true;
				}

				// create AccountInfoHelper to recognize account sub type
				AccountInfo accountInfoHelper = new AccountInfo();
				accountInfoHelper.setAccountType(accountType);
				accountInfoHelper.setAccountSubType(accountSubType);

				// populate specific Account Info
				if (postpaidConsumer || postpaidBoxedConsumer || postpaidConsumerEmployee || postpaidConsumerEmployeeNew) {

					if (postpaidConsumerEmployee) {
						accountInfo = PostpaidEmployeeAccountInfo.newPCSInstance0();
					} else if (postpaidConsumerEmployeeNew && !accountInfoHelper.isIDEN()) {
						accountInfo = PostpaidEmployeeAccountInfo.newPCSInstance1();
					} else if (postpaidConsumerEmployeeNew && accountInfoHelper.isIDEN()) {
						accountInfo = PostpaidEmployeeAccountInfo.newIDENInstance1();
					} else if (accountInfoHelper.isIDEN()) {
						accountInfo = PostpaidConsumerAccountInfo.newIDENInstance();
					} else if (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PAGER_REGULAR) {
						accountInfo = PostpaidConsumerAccountInfo.newPagerInstance();
					} else if (postpaidBoxedConsumer) {
						accountInfo = PostpaidBoxedConsumerAccountInfo.newPagerInstance();
					} else if (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_AUTOTEL_REGULAR) {
						accountInfo = PostpaidConsumerAccountInfo.newAutotelInstance();
					} else {
						accountInfo = PostpaidConsumerAccountInfo.newPCSInstance();
					}

					// set Payment Method
					((PostpaidConsumerAccountInfo)accountInfo).getPaymentMethod0().setPaymentMethod(callable.getString(12));
					((PostpaidConsumerAccountInfo)accountInfo).getPaymentMethod0().setSuppressReturnEnvelope(callable.getString(105) == null ? false : callable.getString(105).equals("N") ? true : false);
					((PostpaidConsumerAccountInfo)accountInfo).getPaymentMethod0().setStatus(callable.getString(114));
					((PostpaidConsumerAccountInfo)accountInfo).getPaymentMethod0().setStatusReason(callable.getString(115));

					// set Credit Card for Payment
					//PCI changes: retrieve credit card token , leading and trailing digits 
					CreditCardInfo paymentCCInfo = ((PostpaidConsumerAccountInfo)accountInfo).getPaymentMethod0().getCreditCard0();
					paymentCCInfo.setToken(callable.getString(98));
					paymentCCInfo.setLeadingDisplayDigits(callable.getString(179));
					paymentCCInfo.setTrailingDisplayDigits(callable.getString(180));
					//((PostpaidConsumerAccountInfo)accountInfo).getPaymentMethod0().getCreditCard0().setNumber(callable.getString(98)); 
					((PostpaidConsumerAccountInfo)accountInfo).getPaymentMethod0().getCreditCard0().setExpiryMonth(callable.getInt(100));
					((PostpaidConsumerAccountInfo)accountInfo).getPaymentMethod0().getCreditCard0().setExpiryYear(callable.getInt(101));
					((PostpaidConsumerAccountInfo)accountInfo).getPaymentMethod0().getCreditCard0().setHolderName(callable.getString(99));

					// set bank Info
					((PostpaidConsumerAccountInfo)accountInfo).getPaymentMethod0().getCheque0().getBankAccount0().setBankCode(callable.getString(110));
					((PostpaidConsumerAccountInfo)accountInfo).getPaymentMethod0().getCheque0().getBankAccount0().setBankAccountNumber(callable.getString(111));
					((PostpaidConsumerAccountInfo)accountInfo).getPaymentMethod0().getCheque0().getBankAccount0().setBankBranchNumber(callable.getString(112));
					((PostpaidConsumerAccountInfo)accountInfo).getPaymentMethod0().getCheque0().getBankAccount0().setBankAccountType(callable.getString(113));

					// set Credit Info
					//if (accountSubType != AccountSummary.ACCOUNT_SUBTYPE_PAGER_REGULAR) {
					((PostpaidConsumerAccountInfo)accountInfo).getPersonalCreditInformation0().setBirthDate(callable.getDate(16));
					((PostpaidConsumerAccountInfo)accountInfo).getPersonalCreditInformation0().setSin(callable.getString(19));
					((PostpaidConsumerAccountInfo)accountInfo).getPersonalCreditInformation0().setDriversLicense(callable.getString(90));
					((PostpaidConsumerAccountInfo)accountInfo).getPersonalCreditInformation0().setDriversLicenseProvince(callable.getString(91));
					((PostpaidConsumerAccountInfo)accountInfo).getPersonalCreditInformation0().setDriversLicenseExpiry(callable.getTimestamp(92));

					// set  Credit Card
					//PCI changes: retrieve credit card token , leading and trailing digits 
					CreditCardInfo personalCreditCCInfo = ((PostpaidConsumerAccountInfo)accountInfo).getPersonalCreditInformation0().getCreditCard0();
					personalCreditCCInfo.setToken(callable.getString(95));
					personalCreditCCInfo.setLeadingDisplayDigits(callable.getString(177));
					personalCreditCCInfo.setTrailingDisplayDigits(callable.getString( 178 ));
					//((PostpaidConsumerAccountInfo)accountInfo).getPersonalCreditInformation0().getCreditCard0().setNumber(callable.getString(95));
					((PostpaidConsumerAccountInfo)accountInfo).getPersonalCreditInformation0().getCreditCard0().setExpiryMonth(callable.getInt(96));
					((PostpaidConsumerAccountInfo)accountInfo).getPersonalCreditInformation0().getCreditCard0().setExpiryYear(callable.getInt(97));
					//}

					// set Consumer Name Info
					((PostpaidConsumerAccountInfo)accountInfo).getName0().setFirstName(callable.getString(24));
					((PostpaidConsumerAccountInfo)accountInfo).getName0().setLastName(callable.getString(25));
					((PostpaidConsumerAccountInfo)accountInfo).getName0().setMiddleInitial(callable.getString(86));
					((PostpaidConsumerAccountInfo)accountInfo).getName0().setTitle(callable.getString(87));
					((PostpaidConsumerAccountInfo)accountInfo).getName0().setGeneration(callable.getString(170));
					((PostpaidConsumerAccountInfo)accountInfo).getName0().setAdditionalLine(callable.getString(88));

				} else if (postpaidBusinessPersonal || postpaidCorporatePersonal) {

					if (accountInfoHelper.isIDEN()) {
						accountInfo = PostpaidBusinessPersonalAccountInfo.newIDENInstance();
					} if (postpaidCorporatePersonal){
						accountInfo = PostpaidCorporatePersonalAccountInfo.newInstance0(accountSubType);
					}  else {
						accountInfo = PostpaidBusinessPersonalAccountInfo.newPCSInstance();
					}

					// set Billing Name
					// note that the additional line field here is used for legal business name
					((PostpaidBusinessPersonalAccountInfo)accountInfo).getName0().setFirstName(callable.getString(24));
					((PostpaidBusinessPersonalAccountInfo)accountInfo).getName0().setLastName(callable.getString(25));
					((PostpaidBusinessPersonalAccountInfo)accountInfo).getName0().setMiddleInitial(callable.getString(86));
					((PostpaidBusinessPersonalAccountInfo)accountInfo).getName0().setTitle(callable.getString(87));
					((PostpaidBusinessPersonalAccountInfo)accountInfo).getName0().setGeneration(callable.getString(170));
					((PostpaidBusinessPersonalAccountInfo)accountInfo).getName0().setAdditionalLine(callable.getString(88));

					// set Payment Method
					((PostpaidBusinessPersonalAccountInfo)accountInfo).getPaymentMethod0().setPaymentMethod(callable.getString(12));
					((PostpaidBusinessPersonalAccountInfo)accountInfo).getPaymentMethod0().setSuppressReturnEnvelope(callable.getString(105)==null ? false : callable.getString(105).equals("N") ? true : false);
					((PostpaidBusinessPersonalAccountInfo)accountInfo).getPaymentMethod0().setStatus(callable.getString(114));
					((PostpaidBusinessPersonalAccountInfo)accountInfo).getPaymentMethod0().setStatusReason(callable.getString(115));

					// set Credit Card for Payment
					//PCI changes: retrieve credit card token , leading and trailing digits 
					CreditCardInfo paymentCCInfo = ((PostpaidBusinessPersonalAccountInfo)accountInfo).getPaymentMethod0().getCreditCard0();
					paymentCCInfo.setToken(callable.getString(98));
					paymentCCInfo.setLeadingDisplayDigits(callable.getString(179));
					paymentCCInfo.setTrailingDisplayDigits(callable.getString(180));
					//((PostpaidBusinessPersonalAccountInfo)accountInfo).getPaymentMethod0().getCreditCard0().setNumber(callable.getString(98));
					((PostpaidBusinessPersonalAccountInfo)accountInfo).getPaymentMethod0().getCreditCard0().setExpiryMonth(callable.getInt(100));
					((PostpaidBusinessPersonalAccountInfo)accountInfo).getPaymentMethod0().getCreditCard0().setExpiryYear(callable.getInt(101));
					((PostpaidBusinessPersonalAccountInfo)accountInfo).getPaymentMethod0().getCreditCard0().setHolderName(callable.getString(99));

					// set bank Info
					((PostpaidBusinessPersonalAccountInfo)accountInfo).getPaymentMethod0().getCheque0().getBankAccount0().setBankCode(callable.getString(110));
					((PostpaidBusinessPersonalAccountInfo)accountInfo).getPaymentMethod0().getCheque0().getBankAccount0().setBankAccountNumber(callable.getString(111));
					((PostpaidBusinessPersonalAccountInfo)accountInfo).getPaymentMethod0().getCheque0().getBankAccount0().setBankBranchNumber(callable.getString(112));
					((PostpaidBusinessPersonalAccountInfo)accountInfo).getPaymentMethod0().getCheque0().getBankAccount0().setBankAccountType(callable.getString(113));

					// set Credit Info
					((PostpaidBusinessPersonalAccountInfo)accountInfo).getPersonalCreditInformation0().setBirthDate(callable.getDate(16));
					((PostpaidBusinessPersonalAccountInfo)accountInfo).getPersonalCreditInformation0().setSin(callable.getString(19));
					((PostpaidBusinessPersonalAccountInfo)accountInfo).getPersonalCreditInformation0().setDriversLicense(callable.getString(90));
					((PostpaidBusinessPersonalAccountInfo)accountInfo).getPersonalCreditInformation0().setDriversLicenseProvince(callable.getString(91));
					((PostpaidBusinessPersonalAccountInfo)accountInfo).getPersonalCreditInformation0().setDriversLicenseExpiry(callable.getTimestamp(92));

					// set  Credit Card
					//PCI changes: retrieve credit card token , leading and trailing digits 
					CreditCardInfo personalCreditCCInfo = ((PostpaidBusinessPersonalAccountInfo)accountInfo).getPersonalCreditInformation0().getCreditCard0();
					personalCreditCCInfo.setToken(callable.getString(95));
					personalCreditCCInfo.setLeadingDisplayDigits(callable.getString(177));
					personalCreditCCInfo.setTrailingDisplayDigits(callable.getString( 178 ));
					//((PostpaidBusinessPersonalAccountInfo)accountInfo).getPersonalCreditInformation0().getCreditCard0().setNumber(callable.getString(95));
					((PostpaidBusinessPersonalAccountInfo)accountInfo).getPersonalCreditInformation0().getCreditCard0().setExpiryMonth(callable.getInt(96));
					((PostpaidBusinessPersonalAccountInfo)accountInfo).getPersonalCreditInformation0().getCreditCard0().setExpiryYear(callable.getInt(97));

				} else if (prepaidConsumer || quebecTelPrepaidConsumer || westernPrepaidConsumer) {

					if (prepaidConsumer) {
						accountInfo = PrepaidConsumerAccountInfo.newPCSInstance(AccountSummary.ACCOUNT_SUBTYPE_PCS_PREPAID);
					} else if (quebecTelPrepaidConsumer) {
						accountInfo = PrepaidConsumerAccountInfo.newPCSInstance(AccountSummary.ACCOUNT_SUBTYPE_PCS_QUEBECTEL);
					} else {
						accountInfo = WesternPrepaidConsumerAccountInfo.newPCSInstance();
					}

					((PrepaidConsumerAccountInfo)accountInfo).setBirthDate(callable.getDate(16));

					// set Name Info
					((PrepaidConsumerAccountInfo)accountInfo).getName0().setFirstName(callable.getString(24));
					((PrepaidConsumerAccountInfo)accountInfo).getName0().setLastName(callable.getString(25));
					((PrepaidConsumerAccountInfo)accountInfo).getName0().setMiddleInitial(callable.getString(86));
					((PrepaidConsumerAccountInfo)accountInfo).getName0().setTitle(callable.getString(87));
					((PrepaidConsumerAccountInfo)accountInfo).getName0().setGeneration(callable.getString(170));
					((PrepaidConsumerAccountInfo)accountInfo).getName0().setAdditionalLine(callable.getString(88));

					// set Top Up Credit Card
					if (!((callable.getString(114) == null ? "C " : callable.getString(114)).equals("C "))) {
						//PCI changes: retrieve credit card token , leading and trailing digits 
						CreditCardInfo topupCCInfo = ((PrepaidConsumerAccountInfo)accountInfo).getTopUpCreditCard0();
						topupCCInfo.setToken(callable.getString(98));
						topupCCInfo.setLeadingDisplayDigits(callable.getString(179));
						topupCCInfo.setTrailingDisplayDigits(callable.getString(180));
						//((PrepaidConsumerAccountInfo)accountInfo).getTopUpCreditCard0().setNumber(callable.getString(98));
						((PrepaidConsumerAccountInfo)accountInfo).getTopUpCreditCard0().setExpiryMonth(callable.getInt(100));
						((PrepaidConsumerAccountInfo)accountInfo).getTopUpCreditCard0().setExpiryYear(callable.getInt(101));
						((PrepaidConsumerAccountInfo)accountInfo).getTopUpCreditCard0().setHolderName(callable.getString(99));
					}

				} else if (postpaidBusinessRegular || postpaidLikeBusinessRegular || IDENCorporateVPN || IDENCorporate || postpaidBusinessDealer
						|| postpaidCorporateRegional || postpaidBusinessOffical || postpaidCorporateAutotel || postpaidCorporateRegular) {

					if (accountInfoHelper.isIDEN()) {
						if (IDENCorporateVPN) {
							accountInfo = PostpaidCorporateRegularAccountInfo.newInstance(AccountSummary.ACCOUNT_SUBTYPE_IDEN_PRIVATE_NETWORK_PLUS);
						} else if (IDENCorporate) {
							accountInfo = PostpaidCorporateRegularAccountInfo.newInstance(accountSubType);
						} else if (postpaidBusinessRegular || postpaidLikeBusinessRegular) {
							accountInfo = PostpaidBusinessRegularAccountInfo.newIDENInstance();
						} else if (postpaidBusinessDealer) {
							accountInfo = PostpaidBusinessDealerAccountInfo.newIDENInstance0();
						}
					} else if (postpaidBusinessOffical) {
						accountInfo = PostpaidBusinessOfficialAccountInfo.newPCSInstance0();
					} else if (postpaidBusinessDealer) {
						accountInfo = PostpaidBusinessDealerAccountInfo.newPCSInstance0();
					} else if (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PAGER_REGULAR) {
						accountInfo = PostpaidBusinessRegularAccountInfo.newPagerInstance();
					} else if (postpaidCorporateRegional) {
						accountInfo = PostpaidCorporateRegularAccountInfo.newInstance(AccountSummary.ACCOUNT_SUBTYPE_PCS_REGIONAL);
					} else if (postpaidCorporateAutotel) {
						accountInfo = PostpaidCorporateRegularAccountInfo.newInstance(accountSubType);
					} else if (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_AUTOTEL_REGULAR) {
						accountInfo = PostpaidBusinessRegularAccountInfo.newAutotelInstance();
					} else if (postpaidCorporateRegular) {
						accountInfo = PostpaidCorporateRegularAccountInfo.newInstance0(accountSubType);
					} else {
						accountInfo = PostpaidBusinessRegularAccountInfo.newPCSInstance();
					}

					// set Name
					((PostpaidBusinessRegularAccountInfo)accountInfo).setLegalBusinessName(callable.getString(25));
					/*((PostpaidBusinessRegularAccountInfo)accountInfo).setFirstName(callable.getString(24));
					    ((PostpaidBusinessRegularAccountInfo)accountInfo).setLastName(callable.getString(89));*/
					//set Contact Phone
					/* ((PostpaidBusinessRegularAccountInfo)accountInfo).setContactPhone(callable.getString(13));
					    ((PostpaidBusinessRegularAccountInfo)accountInfo).setContactPhoneExtension(callable.getString(14)==null ? null : callable.getString(14).trim());*/

					// set Business Credit Info
					((PostpaidBusinessRegularAccountInfo)accountInfo).getCreditInformation0().setIncorporationNumber(callable.getString(93));
					((PostpaidBusinessRegularAccountInfo)accountInfo).getCreditInformation0().setIncorporationDate(callable.getTimestamp(94));

					// Payment Method
					((PostpaidBusinessRegularAccountInfo)accountInfo).getPaymentMethod0().setPaymentMethod(callable.getString(12));
					((PostpaidBusinessRegularAccountInfo)accountInfo).getPaymentMethod0().setSuppressReturnEnvelope(callable.getString(105)==null ? false : callable.getString(105).equals("N") ? true : false);
					((PostpaidBusinessRegularAccountInfo)accountInfo).getPaymentMethod0().setStatus(callable.getString(114));
					((PostpaidBusinessRegularAccountInfo)accountInfo).getPaymentMethod0().setStatusReason(callable.getString(115));

					// Credit Card for Payment Method
					//PCI changes: retrieve credit card token , leading and trailing digits 
					CreditCardInfo paymentCCInfo = ((PostpaidBusinessRegularAccountInfo)accountInfo).getPaymentMethod0().getCreditCard0();
					paymentCCInfo.setToken(callable.getString(98));
					paymentCCInfo.setLeadingDisplayDigits(callable.getString(179));
					paymentCCInfo.setTrailingDisplayDigits(callable.getString(180));
					//((PostpaidBusinessRegularAccountInfo)accountInfo).getPaymentMethod0().getCreditCard0().setNumber(callable.getString(98));
					((PostpaidBusinessRegularAccountInfo)accountInfo).getPaymentMethod0().getCreditCard0().setExpiryMonth(callable.getInt(100));
					((PostpaidBusinessRegularAccountInfo)accountInfo).getPaymentMethod0().getCreditCard0().setExpiryYear(callable.getInt(101));
					((PostpaidBusinessRegularAccountInfo)accountInfo).getPaymentMethod0().getCreditCard0().setHolderName(callable.getString(99));

					// bank Info
					((PostpaidBusinessRegularAccountInfo)accountInfo).getPaymentMethod0().getCheque0().getBankAccount0().setBankCode(callable.getString(110));
					((PostpaidBusinessRegularAccountInfo)accountInfo).getPaymentMethod0().getCheque0().getBankAccount0().setBankAccountNumber(callable.getString(111));
					((PostpaidBusinessRegularAccountInfo)accountInfo).getPaymentMethod0().getCheque0().getBankAccount0().setBankBranchNumber(callable.getString(112));
					((PostpaidBusinessRegularAccountInfo)accountInfo).getPaymentMethod0().getCheque0().getBankAccount0().setBankAccountType(callable.getString(113));

					// set Credit Info
					((PostpaidBusinessRegularAccountInfo)accountInfo).getPersonalCreditInformation0().setBirthDate(callable.getDate(16));
					((PostpaidBusinessRegularAccountInfo)accountInfo).getPersonalCreditInformation0().setSin(callable.getString(19));
					((PostpaidBusinessRegularAccountInfo)accountInfo).getPersonalCreditInformation0().setDriversLicense(callable.getString(90));
					((PostpaidBusinessRegularAccountInfo)accountInfo).getPersonalCreditInformation0().setDriversLicenseProvince(callable.getString(91));
					((PostpaidBusinessRegularAccountInfo)accountInfo).getPersonalCreditInformation0().setDriversLicenseExpiry(callable.getTimestamp(92));

					// set Credit Card
					//PCI changes: retrieve credit card token , leading and trailing digits 
					CreditCardInfo personalCreditCCInfo = ((PostpaidBusinessRegularAccountInfo)accountInfo).getPersonalCreditInformation0().getCreditCard0();
					personalCreditCCInfo.setToken(callable.getString(95));
					personalCreditCCInfo.setLeadingDisplayDigits(callable.getString(177));
					personalCreditCCInfo.setTrailingDisplayDigits(callable.getString( 178 ));
					//((PostpaidBusinessRegularAccountInfo)accountInfo).getPersonalCreditInformation0().getCreditCard0().setNumber(callable.getString(95));
					((PostpaidBusinessRegularAccountInfo)accountInfo).getPersonalCreditInformation0().getCreditCard0().setExpiryMonth(callable.getInt(96));
					((PostpaidBusinessRegularAccountInfo)accountInfo).getPersonalCreditInformation0().getCreditCard0().setExpiryYear(callable.getInt(97));
				}

				// Populate common Account Info
				accountInfo.setBanId(ban);
				accountInfo.setBrandId(callable.getInt(174));
				accountInfo.setStatus(callable.getString(2).charAt(0));
				accountInfo.setStatusActivityCode(callable.getString(102));
				accountInfo.setStatusActivityReasonCode(callable.getString(103));
				accountInfo.setAccountType(accountType);
				accountInfo.setAccountSubType(accountSubType);
				accountInfo.setCreateDate(callable.getTimestamp(5));
				accountInfo.setStartServiceDate(callable.getTimestamp(6));
				accountInfo.setDealerCode(callable.getString(9));
				accountInfo.setSalesRepCode(callable.getString(10));
				accountInfo.setBillCycle(callable.getInt(11));
				accountInfo.setBillCycleCloseDay(callable.getInt(104));
				accountInfo.setPin(callable.getString(18));
				accountInfo.setCustomerId(callable.getInt(64));
				accountInfo.setLanguage(callable.getString(65));
				accountInfo.setEmail(callable.getString(66));
				accountInfo.setHotlined(callable.getString(63) == null ? false : String.valueOf(callable.getString(63)).trim().equals("Y") ? true : false);
				accountInfo.setCorporateHierarchy(callable.getString(107) == null ? false : callable.getString(107).equals("N") ? false : true );

				if ( accountInfo.isCorporateHierarchy() ) {
					accountInfo.setHierarchyId( callable.getInt(107) );
				}
				accountInfo.setCorporateAccountRepCode(callable.getString(108) == null ? "" : callable.getString(108));
				accountInfo.setBanSegment(callable.getString(175));
				accountInfo.setBanSubSegment(callable.getString(176));

				// set subscriber counts	   
				accountInfo.setActiveSubscribersCount(callable.getInt(33));
				accountInfo.setReservedSubscribersCount(callable.getInt(34));
				accountInfo.setSuspendedSubscribersCount(callable.getInt(35));
				accountInfo.setCancelledSubscribersCount(callable.getInt(36));
				accountInfo.setAllActiveSubscribersCount(callable.getInt(160));
				accountInfo.setAllReservedSubscribersCount(callable.getInt(161));
				accountInfo.setAllSuspendedSubscribersCount(callable.getInt(162));
				accountInfo.setAllCancelledSubscribersCount(callable.getInt(163));

				// set invoice properties - start
				InvoicePropertiesInfo invoiceProperties = new InvoicePropertiesInfo();
				invoiceProperties.setBan(ban);
				invoiceProperties.setInvoiceSuppressionLevel(callable.getString(109));
				invoiceProperties.setHoldRedirectDestinationCode(String.valueOf(callable.getInt(164)));
				invoiceProperties.setHoldRedirectFromDate(callable.getTimestamp(165));
				invoiceProperties.setHoldRedirectToDate(callable.getTimestamp(166));
				accountInfo.setInvoiceProperties(invoiceProperties);
				// set invoice properties - end

				// note: accountInfo.setAdditionalLine should always be set to the same value as accountInfo.getName0().setAdditionalLine
				// for PostpaidConsumer, PostpaidBusinessPersonal and PrepaidConsumer accounts
				accountInfo.setAdditionalLine(callable.getString(88));
				accountInfo.setStatusDate(callable.getTimestamp(157));
				accountInfo.setFidoConversion(AccountInfo.ACCOUNT_CONV_RUN_NO_FIDO == callable.getShort(158) ? true : false);

				// set tax exempt
				if (callable.getString(119)!= null && !callable.getString(119).trim().equals(""))
					accountInfo.setGstExempt(callable.getString(119).trim().equals("Y") ? (byte)'Y' : (byte)'N');
				if (callable.getString(120) != null && !callable.getString(120).trim().equals(""))
					accountInfo.setPstExempt(callable.getString(120).trim().equals("Y") ? (byte)'Y' : (byte)'N');
				if (callable.getString(121) != null && !callable.getString(121).trim().equals(""))
					accountInfo.setHstExempt(callable.getString(121).trim().equals("Y") ? (byte)'Y' : (byte)'N');

				accountInfo.setGSTExemptExpiryDate(callable.getTimestamp(122));
				accountInfo.setPSTExemptExpiryDate(callable.getTimestamp(123));
				accountInfo.setHSTExemptExpiryDate(callable.getTimestamp(124));
				accountInfo.setGSTCertificateNumber(callable.getString(151));
				accountInfo.setPSTCertificateNumber(callable.getString(152));
				accountInfo.setHSTCertificateNumber(callable.getString(153));
				accountInfo.setGSTExemptEffectiveDate(callable.getTimestamp(154));
				accountInfo.setPSTExemptEffectiveDate(callable.getTimestamp(155));
				accountInfo.setHSTExemptEffectiveDate(callable.getTimestamp(156));

				accountInfo.setHomeProvince(callable.getString(125));
				accountInfo.setAccountCategory(callable.getString(126));
				accountInfo.setNextBillCycle(callable.getInt(127));
				accountInfo.setNextBillCycleCloseDay(callable.getInt(128));
				accountInfo.setVerifiedDate(callable.getTimestamp(129));
				accountInfo.setHandledBySubscriberOnly(callable.getString(130) == null ? false : String.valueOf(callable.getString(130)).trim().equals("Y") ? true : false);
				accountInfo.setCorporateId(callable.getString(131));

				// set Address
				accountInfo.getAddress0().setPrimaryLine(callable.getString(20));
				accountInfo.getAddress0().setCity(callable.getString(21));
				accountInfo.getAddress0().setProvince(callable.getString(22));
				accountInfo.getAddress0().setPostalCode(callable.getString(23));
				accountInfo.getAddress0().setSecondaryLine(callable.getString(68));
				accountInfo.getAddress0().setCountry(callable.getString(69));
				accountInfo.getAddress0().setZipGeoCode(callable.getString(70));
				accountInfo.getAddress0().setForeignState(callable.getString(71));
				accountInfo.getAddress0().setCivicNo(callable.getString(72));
				accountInfo.getAddress0().setCivicNoSuffix(callable.getString(73));
				accountInfo.getAddress0().setStreetDirection(callable.getString(74));
				accountInfo.getAddress0().setStreetName(callable.getString(75));
				accountInfo.getAddress0().setStreetType(callable.getString(76));
				accountInfo.getAddress0().setRrDesignator(callable.getString(77));
				accountInfo.getAddress0().setRrIdentifier(callable.getString(78));
				accountInfo.getAddress0().setPoBox(callable.getString(79));
				accountInfo.getAddress0().setUnitDesignator(callable.getString(80));
				accountInfo.getAddress0().setUnitIdentifier(callable.getString(81));
				accountInfo.getAddress0().setRrAreaNumber(callable.getString(82));
				accountInfo.getAddress0().setRrQualifier(callable.getString(83));
				accountInfo.getAddress0().setRrSite(callable.getString(84));
				accountInfo.getAddress0().setRrCompartment(callable.getString(85));
				accountInfo.getAddress0().setAttention(callable.getString(148));
				accountInfo.getAddress0().setRrDeliveryType(callable.getString(149));
				accountInfo.getAddress0().setRrGroup(callable.getString(150));
				accountInfo.getAddress0().setAddressType(callable.getString(67));

				//set Contact Name
				accountInfo.getContactName().setFirstName(callable.getString(133));
				accountInfo.getContactName().setLastName(callable.getString(89));
				accountInfo.getContactName().setTitle(callable.getString(134));
				accountInfo.getContactName().setMiddleInitial(callable.getString(135));
				accountInfo.getContactName().setAdditionalLine(callable.getString(136));
				accountInfo.getContactName().setGeneration(callable.getString(137));

				//set Contact Phone and Fax
				accountInfo.setContactPhone(callable.getString(138));
				accountInfo.setContactPhoneExtension(callable.getString(139) == null ? null : callable.getString(139).trim());
				accountInfo.setContactFax(callable.getString(17));

				// set Home and Business phones
				accountInfo.setHomePhone(callable.getString(15));
				accountInfo.setBusinessPhone(callable.getString(13));
				accountInfo.setBusinessPhoneExtension(callable.getString(14) == null ? null : callable.getString(14).trim());

				//set Other Phone
				accountInfo.setOtherPhone(callable.getString(116));
				accountInfo.setOtherPhoneExtension(callable.getString(117) == null ? null : callable.getString(117).trim());
				accountInfo.setOtherPhoneType(callable.getString(118));

				if (!(callable.getString(159) == null)) {
					StringTokenizer strToken = new StringTokenizer(callable.getString(159), "|");
					consentIndicatorsList = new String[strToken.countTokens()];
					i = 0;
					while (strToken.hasMoreTokens()) {
						consentIndicatorsList[i] = strToken.nextToken();
						i++;
					}
				}

				accountInfo.setClientConsentIndicatorCodes(consentIndicatorsList);

				//set Full name
				/*   if ((!postpaidBusinessRegular)&&(!postpaidBusinessDealer)&&(!postpaidConsumer)&&(!postpaidBusinessPersonal)&&(!prepaidConsumer)&&(!quebecTelPrepaidConsumer)&&(!IDENCorporate)&&(!IDENCorporateVPN))
				    {accountInfo.setFullName(AttributeTranslator.emptyFromNull(callable.getString(24)) + " " + AttributeTranslator.emptyFromNull(callable.getString(25)));
				    }*/

				// set Financial History
				delinqent =(callable.getString(7) == null ? false : callable.getString(7).equals("D") ? true : false);
				accountInfo.getFinancialHistory0().setDelinquent(delinqent);
				accountInfo.getFinancialHistory0().setWrittenOff(callable.getString(132).equals("W") ? true : false);

				//PCI - change:
				PaymentHistoryInfo lastPayment = new PaymentHistoryInfo();
				lastPayment.setOriginalAmount(callable.getDouble(61));
				//PROD00176130 fix: populate the deposit date  to reflect the actual table column we retrieve this data from.
				lastPayment.setDepositDate(callable.getTimestamp(62));
				lastPayment.setActualAmount(callable.getDouble(169) );
				lastPayment.setActivityCode(callable.getString(181) );
				accountInfo.getFinancialHistory0().setLastPayment( lastPayment );
				/*		   
				   accountInfo.getFinancialHistory0().setLastPaymentAmount(callable.getDouble(61));
				   accountInfo.getFinancialHistory0().setLastPaymentDate(callable.getTimestamp(62));
				   accountInfo.getFinancialHistory0().setLastPaymentRefunded(callable.getInt(169) == 1 ? true : false);
				 */		   

				accountInfo.getFinancialHistory0().setHotlined(callable.getString(63) == null ? false : String.valueOf(callable.getString(63)).trim().equals("Y") ? true : false);

				//set Collection Info
				accountInfo.getFinancialHistory0().setCollectionAgency(callable.getString(147));
				accountInfo.getFinancialHistory0().getCollectionStep0().setPath(callable.getString(141));
				accountInfo.getFinancialHistory0().getCollectionStep0().setStep(callable.getInt(142));
				accountInfo.getFinancialHistory0().getCollectionStep0().setCollectionActivityCode(callable.getString(143));
				accountInfo.getFinancialHistory0().getCollectionStep0().setTreatmentDate(callable.getDate(144));
				accountInfo.getFinancialHistory0().getNextCollectionStep0().setPath(callable.getString(141));
				accountInfo.getFinancialHistory0().getNextCollectionStep0().setStep(callable.getInt(145));
				accountInfo.getFinancialHistory0().getNextCollectionStep0().setTreatmentDate(callable.getDate(146));
				accountInfo.getFinancialHistory0().setHotlinedDate(callable.getDate(171));
				accountInfo.getFinancialHistory0().setDelinquentDate(callable.getDate(172));
				accountInfo.getFinancialHistory0().setWrittenOffDate(callable.getDate(173));

				//add Monthly Financial Activity
				currentDateString = dateFormat.format(new java.util.Date());
				currentMonth = Integer.parseInt(currentDateString.substring(0,2));
				currentYear = Integer.parseInt(currentDateString.substring(6));

				//for debugging
				String  act_01 =(callable.getString(37) == null ? "" : callable.getString(37).trim());
				String  act_02 =(callable.getString(38) == null ? "" : callable.getString(38).trim());
				String  act_03 =(callable.getString(39) == null ? "" : callable.getString(39).trim());
				String  act_04 =(callable.getString(40) == null ? "" : callable.getString(40).trim());
				String  act_05 =(callable.getString(41) == null ? "" : callable.getString(41).trim());
				String  act_06 =(callable.getString(42) == null ? "" : callable.getString(42).trim());
				String  act_07 =(callable.getString(43) == null ? "" : callable.getString(43).trim());
				String  act_08 =(callable.getString(44) == null ? "" : callable.getString(44).trim());
				String  act_09 =(callable.getString(45) == null ? "" : callable.getString(45).trim());
				String  act_10 =(callable.getString(46) == null ? "" : callable.getString(46).trim());
				String  act_11 =(callable.getString(47) == null ? "" : callable.getString(47).trim());
				String  act_12 =(callable.getString(48) == null ? "" : callable.getString(48).trim());

				for (i = 0; i < 12; i++) {
					// Financial history  month
					m = ((currentMonth + i + 1)%12 == 0 ? 12 : (currentMonth + i + 1)%12);
					MonthlyFinancialActivityInfo monthlyFinancialActivityInfo = new MonthlyFinancialActivityInfo();
					monthlyFinancialActivityInfo.setMonth(m);
					if (m <= currentMonth) {
						monthlyFinancialActivityInfo.setYear(currentYear);
					} else {
						monthlyFinancialActivityInfo.setYear(currentYear - 1);
					}
					monthlyFinancialActivityInfo.setActivity(m == 1 ? act_01 : m == 2 ? act_02 : m == 3 ? act_03 : m == 4 ? act_04 : m == 5 ? act_05 : m == 6 ? act_06 : m == 7 ? act_07 : m == 8 ? act_08 : m == 9 ? act_09 : m == 10 ? act_10 : m == 11 ? act_11 : m == 12 ? act_12 : "");
					monthlyFinancialActivityInfo.setDishonoredPaymentCount(m == 1 ? callable.getInt(49) : m == 2 ? callable.getInt(50) : m == 3 ? callable.getInt(51) : m == 4 ? callable.getInt(52) : m==5 ? callable.getInt(53) : m == 6 ? callable.getInt(54) : m == 7 ? callable.getInt(55) : m == 8 ? callable.getInt(56) : m == 9 ? callable.getInt(57) : m == 10 ? callable.getInt(58) : m == 11 ? callable.getInt(59) : m == 12 ? callable.getInt(60) : 0);
					accountInfo.getFinancialHistory0().addMonthlyFinancialActivity(monthlyFinancialActivityInfo);
				}

				// set Debt Summary
				// no Past Due Payments
				if ((callable.getDouble(27) == 0)&&(callable.getDouble(32) == 0)) {
					accountInfo.getFinancialHistory0().getDebtSummary0().setCurrentDue(callable.getDouble(8));
					accountInfo.getFinancialHistory0().getDebtSummary0().setPastDue(0);
				} else {
					accountInfo.getFinancialHistory0().getDebtSummary0().setCurrentDue(callable.getDouble(27));
					accountInfo.getFinancialHistory0().getDebtSummary0().setPastDue(callable.getDouble(32));
				}
				accountInfo.getFinancialHistory0().getDebtSummary0().setPastDue1to30Days(callable.getDouble(28));
				accountInfo.getFinancialHistory0().getDebtSummary0().setPastDue31to60Days(callable.getDouble(29));
				accountInfo.getFinancialHistory0().getDebtSummary0().setPastDue61to90Days(callable.getDouble(30));
				accountInfo.getFinancialHistory0().getDebtSummary0().setPastDueOver90Days(callable.getDouble(31));
				accountInfo.getFinancialHistory0().getDebtSummary0().setBillDueDate(callable.getTimestamp(106));			

				return accountInfo;
			}
		});
	}

	@Override
	public List<AccountInfo> retrieveAccountsByBan(final int[] banArray) {		

		return super.getKnowbilityJdbcTemplate().execute(new ConnectionCallback<List<AccountInfo>>() {
			@Override
			public List<AccountInfo> doInConnection(Connection connection)
			throws SQLException, DataAccessException {
				List<AccountInfo> accountList = new ArrayList<AccountInfo>();

				OracleCallableStatement callable = null;
				ResultSet result = null;
				try {
					// prepare callable statement
					callable = (OracleCallableStatement) connection.prepareCall("{? = call RA_UTILITY_PKG.GetAccountsByBANs(?, ?, ?)}");

					// create array descriptor
					ArrayDescriptor memoIdArrayDesc = ArrayDescriptor.createDescriptor("T_BAN_ARRAY", (OracleConnection)connection);

					// create Oracle array of BANs
					ARRAY bansArray = new ARRAY(memoIdArrayDesc, connection, banArray);

					// set/register input/output parameters
					callable.registerOutParameter(1, OracleTypes.NUMBER);
					callable.setARRAY(2, bansArray);
					callable.registerOutParameter(3, OracleTypes.CURSOR);
					callable.registerOutParameter(4, OracleTypes.VARCHAR);

					callable.execute();

					boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;

					if (success) {
						result = (ResultSet) callable.getObject(3);

						while (result.next()) {
							AccountInfo account = new AccountInfo();

							account.setBanId(result.getInt(1));
							account.setBrandId(result.getInt(42));
							account.setStatus(result.getString(2).charAt(0));
							account.setAccountType(result.getString(3).charAt(0));
							account.setAccountSubType(result.getString(4).charAt(0));
							account.setCreateDate(result.getTimestamp(5));
							account.setStartServiceDate(result.getTimestamp(6));
							account.setDealerCode(result.getString(7));
							account.setSalesRepCode(result.getString(8));
							account.setCustomerId(result.getInt(9));
							account.getAddress0().setPrimaryLine(result.getString(14));
							account.getAddress0().setCity(result.getString(15));
							account.getAddress0().setProvince(result.getString(16));
							account.getAddress0().setPostalCode(result.getString(17));
							account.getAddress0().setAddressType(result.getString(18));
							String addressType = account.getAddress0().getAddressType();
							account.getAddress0().setSecondaryLine(result.getString(19));
							account.getAddress0().setCountry(result.getString(20));
							account.getAddress0().setZipGeoCode(result.getString(21));
							account.getAddress0().setForeignState(result.getString(22));
							account.getAddress0().setCivicNo(result.getString(23));
							account.getAddress0().setCivicNoSuffix(result.getString(24));
							account.getAddress0().setStreetDirection(result.getString(25));
							account.getAddress0().setStreetName(result.getString(26));
							account.getAddress0().setStreetType(result.getString(27));
							account.getAddress0().setRrDesignator(result.getString(28));
							account.getAddress0().setRrIdentifier(result.getString(29));
							account.getAddress0().setPoBox(result.getString(30));
							account.getAddress0().setUnitDesignator(result.getString(31));
							account.getAddress0().setUnitIdentifier(result.getString(32));
							account.getAddress0().setRrAreaNumber(result.getString(33));
							account.getAddress0().setRrQualifier(result.getString(34));
							account.getAddress0().setRrSite(result.getString(35));
							account.getAddress0().setRrCompartment(result.getString(36));
							account.getAddress0().setAddressType(addressType);
							account.setFullName(AttributeTranslator.emptyFromNull(result.getString(10)) +
									AttributeTranslator.emptyFromNull(" ", result.getString(12)) +
									AttributeTranslator.emptyFromNull(" ", result.getString(11)));
							account.setPin(result.getString(37));
							account.setStatusActivityCode(result.getString(38));
							account.setStatusActivityReasonCode(result.getString(39));
							account.setAdditionalLine(result.getString(40));
							account.setStatusDate(result.getTimestamp(41));
							account.setBanSegment(result.getString(43));
							account.setBanSubSegment(result.getString(44));

							accountList.add(account);
						}
					} else {
						String errorMessage = callable.getString(4);
						LOGGER.debug("Stored procedure failed: " + errorMessage);
					}
				} finally {
					if (result != null ){
						result.close();
					}
					if (callable != null ){
						callable.close();
					}
				}

				return accountList;
			}
		}
		);
	}

	@Override
	public String retrieveAccountStatusByBan(final int ban) {
		String sql =  "SELECT ban_status FROM billing_account WHERE ban = ?";

		return super.getKnowbilityJdbcTemplate().execute(sql, new PreparedStatementCallback<String>() {
			@Override
			public String doInPreparedStatement(PreparedStatement pstmt)
			throws SQLException, DataAccessException {
				String accountStatus = null;
				ResultSet rset = null;

				try {
					pstmt.setInt(1, ban);
					rset = pstmt.executeQuery();
					if (rset.next()) {
						accountStatus = rset.getString(1);
					}
				} finally {
					if (rset != null ) {
						rset.close();
					}
				}

				return accountStatus;
			}
		});
	}

	@Override
	public List<AccountInfo> retrieveAccountsByPostalCode(final String lastName, final String postalCode, final int maximum) {		
		String call = "{call ACC_RETRIEVAL_PKG.getAccountsByPostalCode(?, ?, ?, ?)}";
		
		if (lastName == null || lastName.trim().isEmpty()) {
			LOGGER.error("==== PROBLEMATIC lastName passed in ["+lastName+"]"); //it shouldn't happen. This is for troubleshooting purpose only.
		}

		return super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<List<AccountInfo>>() {
			@Override
			public List<AccountInfo> doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				List<AccountInfo> accountList = new ArrayList<AccountInfo>();
				ResultSet result = null;

				try {
					callable.setString(1, lastName+"#%");
					callable.setString(2, postalCode);
					callable.setInt(3, maximum);
					callable.registerOutParameter(4, OracleTypes.CURSOR);
					callable.execute();
					
					result = (ResultSet) callable.getObject(4);

					while (result != null && result.next()){
						AccountInfo account = new AccountInfo();

						account.setBanId(result.getInt(1));
						account.setStatus(result.getString(2).charAt(0));
						account.setAccountType(result.getString(3).charAt(0));
						account.setAccountSubType(result.getString(4).charAt(0));
						account.setCreateDate(result.getTimestamp(5));
						account.setStartServiceDate(result.getTimestamp(6));
						account.setDealerCode(result.getString(7));
						account.setSalesRepCode(result.getString(8));
						account.setCustomerId(result.getInt(9));
						account.getAddress0().setPrimaryLine(result.getString(14));
						account.getAddress0().setCity(result.getString(15));
						account.getAddress0().setProvince(result.getString(16));
						account.getAddress0().setPostalCode(result.getString(17));
						account.getAddress0().setAddressType(result.getString(18));
						account.getAddress0().setSecondaryLine(result.getString(19));
						account.getAddress0().setCountry(result.getString(20));
						account.getAddress0().setZipGeoCode(result.getString(21));
						account.getAddress0().setForeignState(result.getString(22));
						account.getAddress0().setCivicNo(result.getString(23));
						account.getAddress0().setCivicNoSuffix(result.getString(24));
						account.getAddress0().setStreetDirection(result.getString(25));
						account.getAddress0().setStreetName(result.getString(26));
						account.getAddress0().setStreetType(result.getString(27));
						account.getAddress0().setRrDesignator(result.getString(28));
						account.getAddress0().setRrIdentifier(result.getString(29));
						account.getAddress0().setPoBox(result.getString(30));
						account.getAddress0().setUnitDesignator(result.getString(31));
						account.getAddress0().setUnitIdentifier(result.getString(32));
						account.getAddress0().setRrAreaNumber(result.getString(33));
						account.getAddress0().setRrQualifier(result.getString(34));
						account.getAddress0().setRrSite(result.getString(35));
						account.getAddress0().setRrCompartment(result.getString(36));
						account.getAddress0().setAddressType(result.getString(18)); //this must be after setRrAreaNumber, setRrBox, setRrIdentifier because of side-effects in those methods
						account.setFullName(AttributeTranslator.emptyFromNull(result.getString(10)) +
								AttributeTranslator.emptyFromNull(" ", result.getString(12)) +
								AttributeTranslator.emptyFromNull(" ", result.getString(11)));
						account.setPin(result.getString(37));
						account.setStatusActivityCode(result.getString(38));
						account.setStatusActivityReasonCode(result.getString(39));
						account.setAdditionalLine(result.getString(40));
						account.setStatusDate(result.getTimestamp(41));
						account.setBrandId(result.getInt(42));

						accountList.add(account);
					}
				} finally {
					if (result != null ) {
						result.close();
					}
				}

				return accountList;
			}


		});
	}

	@Override
	public List<AccountInfo> retrieveAccountsByPhoneNumber(
			final String phoneNumber, boolean onlyLastAccount) {
		String sqlCall = "";
		
		if (onlyLastAccount) {
			sqlCall = "{call RA_UTILITY_PKG.getLastAccountsByPhoneNumber(?, ?)}";
		}else {
			sqlCall = "{call RA_UTILITY_PKG.getAccountsByPhoneNumber(?, ?)}";
		}
		
		return super.getKnowbilityJdbcTemplate().execute(sqlCall, new CallableStatementCallback<List<AccountInfo>>() {

			@Override
			public List<AccountInfo> doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				List<AccountInfo> accountList = new ArrayList<AccountInfo>();

				ResultSet result = null;
				try {
					callable.setString(1, phoneNumber);
					callable.registerOutParameter(2, OracleTypes.CURSOR);
					callable.execute();
					result = (ResultSet) callable.getObject(2);
					while (result != null && result.next() ){
						AccountInfo account = new AccountInfo();

						account.setBanId(result.getInt(1));
						account.setStatus(result.getString(2).charAt(0));
						account.setAccountType(result.getString(3).charAt(0));
						account.setAccountSubType(result.getString(4).charAt(0));
						account.setCreateDate(result.getTimestamp(5));
						account.setStartServiceDate(result.getTimestamp(6));
						account.setDealerCode(result.getString(7));
						account.setSalesRepCode(result.getString(8));
						account.setCustomerId(result.getInt(9));
						account.getAddress0().setPrimaryLine(result.getString(14));
						account.getAddress0().setCity(result.getString(15));
						account.getAddress0().setProvince(result.getString(16));
						account.getAddress0().setPostalCode(result.getString(17));
						account.getAddress0().setAddressType(result.getString(18));
						String tempAddressType = account.getAddress0().getAddressType();
						account.getAddress0().setSecondaryLine(result.getString(19));
						account.getAddress0().setCountry(result.getString(20));
						account.getAddress0().setZipGeoCode(result.getString(21));
						account.getAddress0().setForeignState(result.getString(22));
						account.getAddress0().setCivicNo(result.getString(23));
						account.getAddress0().setCivicNoSuffix(result.getString(24));
						account.getAddress0().setStreetDirection(result.getString(25));
						account.getAddress0().setStreetName(result.getString(26));
						account.getAddress0().setStreetType(result.getString(27));
						account.getAddress0().setRrDesignator(result.getString(28));
						account.getAddress0().setRrIdentifier(result.getString(29));
						account.getAddress0().setPoBox(result.getString(30));
						account.getAddress0().setUnitDesignator(result.getString(31));
						account.getAddress0().setUnitIdentifier(result.getString(32));
						account.getAddress0().setRrAreaNumber(result.getString(33));
						account.getAddress0().setRrQualifier(result.getString(34));
						account.getAddress0().setRrSite(result.getString(35));
						account.getAddress0().setRrCompartment(result.getString(36));
						account.getAddress0().setAddressType(tempAddressType);
						account.setFullName(AttributeTranslator.emptyFromNull(result.getString(10)) +
								AttributeTranslator.emptyFromNull(" ", result.getString(12)) +
								AttributeTranslator.emptyFromNull(" ", result.getString(11)));
						account.setPin(result.getString(37));
						account.setStatusActivityCode(result.getString(38));
						account.setStatusActivityReasonCode(result.getString(39));
						account.setAdditionalLine(result.getString(40));
						account.setStatusDate(result.getTimestamp(41));
						account.setBrandId(result.getInt(42));


						accountList.add(account);
					}
				} finally {
					if (result != null ) {
						result.close();
					}
				}

				return accountList;
			}
		});

	}

	@Override
	public List<AccountInfo> retrieveLastAccountsBySeatNumber(
			final String phoneNumber) {
		
		return super.getKnowbilityJdbcTemplate().execute("{call RA_UTILITY_PKG.getLastAccountsBySeatNumber(?, ?)}", new CallableStatementCallback<List<AccountInfo>>() {

			@Override
			public List<AccountInfo> doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				List<AccountInfo> accountList = new ArrayList<AccountInfo>();

				ResultSet result = null;
				try {
					callable.setString(1, phoneNumber);
					callable.registerOutParameter(2, OracleTypes.CURSOR);
					callable.execute();
					result = (ResultSet) callable.getObject(2);
					while (result != null && result.next() ){
						AccountInfo account = new AccountInfo();

						account.setBanId(result.getInt(1));
						account.setStatus(result.getString(2).charAt(0));
						account.setAccountType(result.getString(3).charAt(0));
						account.setAccountSubType(result.getString(4).charAt(0));
						account.setCreateDate(result.getTimestamp(5));
						account.setStartServiceDate(result.getTimestamp(6));
						account.setDealerCode(result.getString(7));
						account.setSalesRepCode(result.getString(8));
						account.setCustomerId(result.getInt(9));
						account.getAddress0().setPrimaryLine(result.getString(14));
						account.getAddress0().setCity(result.getString(15));
						account.getAddress0().setProvince(result.getString(16));
						account.getAddress0().setPostalCode(result.getString(17));
						account.getAddress0().setAddressType(result.getString(18));
						String tempAddressType = account.getAddress0().getAddressType();
						account.getAddress0().setSecondaryLine(result.getString(19));
						account.getAddress0().setCountry(result.getString(20));
						account.getAddress0().setZipGeoCode(result.getString(21));
						account.getAddress0().setForeignState(result.getString(22));
						account.getAddress0().setCivicNo(result.getString(23));
						account.getAddress0().setCivicNoSuffix(result.getString(24));
						account.getAddress0().setStreetDirection(result.getString(25));
						account.getAddress0().setStreetName(result.getString(26));
						account.getAddress0().setStreetType(result.getString(27));
						account.getAddress0().setRrDesignator(result.getString(28));
						account.getAddress0().setRrIdentifier(result.getString(29));
						account.getAddress0().setPoBox(result.getString(30));
						account.getAddress0().setUnitDesignator(result.getString(31));
						account.getAddress0().setUnitIdentifier(result.getString(32));
						account.getAddress0().setRrAreaNumber(result.getString(33));
						account.getAddress0().setRrQualifier(result.getString(34));
						account.getAddress0().setRrSite(result.getString(35));
						account.getAddress0().setRrCompartment(result.getString(36));
						account.getAddress0().setAddressType(tempAddressType);
						account.setFullName(AttributeTranslator.emptyFromNull(result.getString(10)) +
								AttributeTranslator.emptyFromNull(" ", result.getString(12)) +
								AttributeTranslator.emptyFromNull(" ", result.getString(11)));
						account.setPin(result.getString(37));
						account.setStatusActivityCode(result.getString(38));
						account.setStatusActivityReasonCode(result.getString(39));
						account.setAdditionalLine(result.getString(40));
						account.setStatusDate(result.getTimestamp(41));
						account.setBrandId(result.getInt(42));


						accountList.add(account);
					}
				} finally {
					if (result != null ) {
						result.close();
					}
				}

				return accountList;
			}
		});

	}
	
	@Override
	public SearchResultsInfo retrieveAccountsByName(final String nameType,
			final String firstName, final boolean firstNameExactMatch, final String lastName,
			final boolean lastNameExactMatch, final char accountStatus, final char accountType,
			final String provinceCode, final int brandId, final int maximum) {
		
		String call = "{? = call ACC_RETRIEVAL_PKG.GetAccountsByName(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

		return super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<SearchResultsInfo>() {

			@Override
			public SearchResultsInfo doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				SearchResultsInfo searchResults = null;
				ResultSet result = null;

				try {
					// compose control name
					boolean controlNameExactMatch = false;
					String controlName = evaluateControlName(lastName);

					if (controlName.length() >= 12) {
						controlName = controlName.substring(0, 12) + "%#%";
						controlNameExactMatch = false;
					} else {
						if (lastNameExactMatch) {
							controlName += "#";

							if (firstName != null && firstName.length() > 0) {
								controlName += evaluateControlName(firstName);

								if (controlName.length() < 15) {
									if (firstNameExactMatch) {
										controlNameExactMatch = true;
									} else {
										controlName += "%";
										controlNameExactMatch = false;
									}
								} else {
									controlName = controlName.substring(0, 15);
									controlNameExactMatch = true;
								}
							} else {
								controlName += "%";
								controlNameExactMatch = false;
							}
						} else {
							controlName += "%#%";
							controlNameExactMatch = false;
						}
					}

					// initialize return object
					searchResults = new SearchResultsInfo();

					// set/register input/output parameters
					callable.registerOutParameter(1, OracleTypes.NUMBER);
					callable.setString(2, nameType);
					callable.setString(3, controlName);
					callable.setInt(4, controlNameExactMatch ? AccountManager.NUMERIC_TRUE : AccountManager.NUMERIC_FALSE);
					callable.setString(5, firstName);
					callable.setInt(6, firstNameExactMatch ? AccountManager.NUMERIC_TRUE : AccountManager.NUMERIC_FALSE);
					callable.setString(7, String.valueOf(accountStatus));
					callable.setString(8, String.valueOf(accountType));
					callable.setString(9, provinceCode);
					callable.setInt(10, maximum);
					callable.setInt(11, brandId);
					callable.registerOutParameter(12, OracleTypes.CURSOR);
					callable.registerOutParameter(13, OracleTypes.VARCHAR);

					callable.execute();

					boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;

					if (success) {
						Collection<AccountInfo> accountList = new ArrayList<AccountInfo>();

						result = (ResultSet) callable.getObject(12);

						int resultCount = 0;
						boolean hasMore = false;
						while (result.next()) {
							AccountInfo account = new AccountInfo();

							account.setBanId(result.getInt(1));
							account.setBrandId(result.getInt(42));
							account.setStatus(result.getString(2).charAt(0));
							account.setAccountType(result.getString(3).charAt(0));
							account.setAccountSubType(result.getString(4).charAt(0));
							account.setCreateDate(result.getTimestamp(5));
							account.setStartServiceDate(result.getTimestamp(6));
							account.setDealerCode(result.getString(7));
							account.setSalesRepCode(result.getString(8));
							account.setCustomerId(result.getInt(9));
							account.getAddress0().setPrimaryLine(result.getString(14));
							account.getAddress0().setCity(result.getString(15));
							account.getAddress0().setProvince(result.getString(16));
							account.getAddress0().setPostalCode(result.getString(17));
							account.getAddress0().setAddressType(result.getString(18));
							String addressType = account.getAddress0().getAddressType();
							account.getAddress0().setSecondaryLine(result.getString(19));
							account.getAddress0().setCountry(result.getString(20));
							account.getAddress0().setZipGeoCode(result.getString(21));
							account.getAddress0().setForeignState(result.getString(22));
							account.getAddress0().setCivicNo(result.getString(23));
							account.getAddress0().setCivicNoSuffix(result.getString(24));
							account.getAddress0().setStreetDirection(result.getString(25));
							account.getAddress0().setStreetName(result.getString(26));
							account.getAddress0().setStreetType(result.getString(27));
							account.getAddress0().setRrDesignator(result.getString(28));
							account.getAddress0().setRrIdentifier(result.getString(29));
							account.getAddress0().setPoBox(result.getString(30));
							account.getAddress0().setUnitDesignator(result.getString(31));
							account.getAddress0().setUnitIdentifier(result.getString(32));
							account.getAddress0().setRrAreaNumber(result.getString(33));
							account.getAddress0().setRrQualifier(result.getString(34));
							account.getAddress0().setRrSite(result.getString(35));
							account.getAddress0().setRrCompartment(result.getString(36));
							account.getAddress0().setAddressType(addressType);
							account.setFullName(AttributeTranslator.emptyFromNull(result.getString(10)) + AttributeTranslator.emptyFromNull(" ", result.getString(12)) + AttributeTranslator.emptyFromNull(" ", result.getString(11)));
							account.setPin(result.getString(37));
							account.setStatusActivityCode(result.getString(38));
							account.setStatusActivityReasonCode(result.getString(39));
							account.setAdditionalLine(result.getString(40));
							account.setStatusDate(result.getTimestamp(41));
							account.setBanSegment(result.getString(43));
							account.setBanSubSegment(result.getString(44));

							// fix defect PROD00074265
							// package query returns maximum + 1 results
							// do not return the (maximum + 1)th results
							resultCount++;
							if (resultCount <= maximum) {
								accountList.add(account);
							}
							hasMore = (result.getInt("has_more") == AccountManager.NUMERIC_TRUE);
						}

						AccountSummary[] accounts = new AccountSummary[accountList.size()];
						accounts = (AccountSummary[]) accountList.toArray(accounts);

						searchResults.setItems(accounts);
						searchResults.setHasMore(hasMore);
					} else {
						searchResults.setItems(new AccountSummary[0]);
						searchResults.setHasMore(false);
					}
				} finally {
					if (result != null) {
						result.close();
					}
				}
				return searchResults;
			}

		});
	}
	
	private String evaluateControlName(String nameString) {
		String controlName = "";
		String[] nameParts = nameString.split(" ");

		for (String namePart : nameParts) {
			String part = namePart.trim();

			for (int j = 0; j < part.length(); j++) {
				int charType = Character.getType(part.charAt(j));

				if (charType == Character.DECIMAL_DIGIT_NUMBER || charType == Character.UPPERCASE_LETTER || charType == Character.LOWERCASE_LETTER)
					controlName += part.substring(j, j + 1);
			}
		}
		
		return controlName;
	}
	
	@Override
	public AccountInfo retrieveLwAccountByBan(final int ban) {
		if (AppConfiguration.isWRPPh3Rollback()) {
			String call = "{call ACC_RETRIEVAL_PKG.getLWaccountInfoByBAN (?,?,?)}";

			return super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<AccountInfo>() {

				@Override
				public AccountInfo doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
					AccountInfo accountInfo = null;
					char accountType;
					char accountSubType;
					ResultSet result = null;
					Date logicalDate;

					try {
						callable.setInt(1, ban);
						callable.registerOutParameter(2, OracleTypes.CURSOR);
						callable.registerOutParameter(3, OracleTypes.DATE);
						callable.execute();
						result = (ResultSet) callable.getObject(2);
						logicalDate = callable.getDate(3);

						if (result.next()) {
							accountType = result.getString("account_type").charAt(0);
							accountSubType = result.getString("account_sub_type").charAt(0);
							LightweightAccountRetrievalTask arTask = new LightweightAccountRetrievalTask(accountType, accountSubType, result);
							arTask.setLogicalDate(logicalDate);
							arTask.determineAccountType();
							accountInfo = arTask.createAccountInfoInstance();
							arTask.mapData();

							// Populate common Account Info
							accountInfo.setBanId(ban);
							accountInfo.setAccountType(accountType);
							accountInfo.setAccountSubType(accountSubType);

							arTask = null;
						}
					} finally {
						if (result != null) {
							result.close();
						}
					}

					if (accountInfo != null) {
						retrieveSubscriberCounts(callable.getConnection(), accountInfo);
					}

					return accountInfo;
				}

			});
		} else {
			String sql = " SELECT ba.account_type, ba.account_sub_type, ba.gl_segment, ba.gl_subsegment, ba.brand_id, ba.col_path_code, ba.ar_balance, "
					+ " cs.effective_date cs_effective_date, cs.cu_age_bucket_0, cs.nx_age_bucket_0, cs.fu_age_bucket_0, cs.cu_past_due_amt, cs.nx_past_due_amt, "
					+ " cs.fu_past_due_amt, ba.status_actv_rsn_code, ba.ban_status, ba.home_province, ba.bill_cycle, bc.cycle_close_day, substr(c.dealer_code, 1,10) dealer_code, "
					+ " substr(c.dealer_code, 11) sales_rep_code, c.email_address, c.lang_pref " + " FROM billing_account ba, collection_status cs, customer c, cycle bc "
					+ " WHERE ba.ban = :ban AND cs.ban(+) = ba.ban AND ba.customer_id=c.customer_id AND ba.bill_cycle=bc.cycle_code(+)";
			
			return (AccountInfo) getKnowbilityJdbcTemplate().execute(sql, new PreparedStatementCallback<AccountInfo>() {
				@Override
				public AccountInfo doInPreparedStatement(PreparedStatement pstmt) throws SQLException, DataAccessException {
					AccountInfo accountInfo = null;
					char accountType;
					char accountSubType;
					ResultSet rs = null;
					
					try {
						pstmt.setInt(1, ban);
						rs = pstmt.executeQuery();
						
						if (rs.next()) {
							accountType = rs.getString("account_type").charAt(0);
							accountSubType = rs.getString("account_sub_type").charAt(0);
							LightweightAccountRetrievalTask arTask = new LightweightAccountRetrievalTask(accountType, accountSubType, rs);
							arTask.setLogicalDate(retrieveLogicalDate(pstmt.getConnection()));
							arTask.determineAccountType();
							accountInfo = arTask.createAccountInfoInstance();
							arTask.mapData();
							arTask = null;
							
							// Populate common Account Info
							accountInfo.setBanId(ban);
							accountInfo.setAccountType(accountType);
							accountInfo.setAccountSubType(accountSubType);
						}
					} finally {
						if (rs != null) {
							rs.close();
						}
					}

					if (accountInfo != null) {
						retrieveSubscriberCounts(pstmt.getConnection(), accountInfo);
					}
					return accountInfo;

				}
			});
		}
	}

	@Override
	public AccountInfo retrieveLwAccountByBanRollback(final int pBan){
		String call = "{call RA_Utility_pkg.getlwaccountinfobyban " +
		"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		return super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<AccountInfo>() {

			@Override
			public AccountInfo doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {
				AccountInfo accountInfo = null;
				char accountType;
				char accountSubType;
				boolean postpaidConsumer = false;
				boolean postpaidBusinessRegular = false;
				boolean postpaidBusinessPersonal = false;
				boolean postpaidBusinessDealer = false;
				boolean postpaidBusinessOffical = false;
				boolean prepaidConsumer = false;
				boolean quebecTelPrepaidConsumer = false;
				boolean westernPrepaidConsumer = false;
				boolean IDENCorporateVPN = false;
				boolean IDENCorporate = false;
				boolean postpaidLikeBusinessRegular = false;
				boolean postpaidBoxedConsumer = false;
				boolean postpaidCorporateRegional = false;
				boolean postpaidConsumerEmployee = false;
				boolean postpaidConsumerEmployeeNew = false;
				boolean postpaidCorporateAutotel = false;
				boolean postpaidCorporatePersonal = false;
				boolean postpaidCorporateRegular = false;

				callable.setInt(1, pBan);// pi_ban    IN  number
				callable.registerOutParameter(2, Types.CHAR);//po_account_type  OUT cha
				callable.registerOutParameter(3, Types.CHAR);//po_account_sub_type  OUT cha
				callable.registerOutParameter(4, Types.VARCHAR);//po_gl_segment  OUT cha
				callable.registerOutParameter(5, Types.VARCHAR);//po_gl_subsegment  OUT cha
				callable.registerOutParameter(6, Types.NUMERIC);//po_brand_ind  OUT cha
				callable.registerOutParameter(7, Types.VARCHAR);//po_col_path_code  OUT cha
				callable.registerOutParameter(8, Types.NUMERIC);//po_ar_balance  OUT
				callable.registerOutParameter(9, Types.NUMERIC);//po_cu_age_bucket_0  OUT
				callable.registerOutParameter(10, Types.NUMERIC);//po_cu_past_due_amt  OUT
				callable.registerOutParameter(11, Types.NUMERIC);//po_active_subs  OUT
				callable.registerOutParameter(12, Types.NUMERIC);//po_reserved_subs  OUT
				callable.registerOutParameter(13, Types.NUMERIC);//po_suspended_subs  OUT
				callable.registerOutParameter(14, Types.NUMERIC);//po_cancelled_subs  OUT 
				callable.registerOutParameter(15, Types.NUMERIC);//po_active_subs_all  OUT
				callable.registerOutParameter(16, Types.NUMERIC);//po_reserved_subs_all  OUT
				callable.registerOutParameter(17, Types.NUMERIC);//po_suspended_subs_all  OUT
				callable.registerOutParameter(18, Types.NUMERIC);//po_cancelled_subs_all  OUT
				callable.registerOutParameter(19, Types.VARCHAR);//po_status_actv_rsn_code  OUT
				callable.registerOutParameter(20, Types.VARCHAR);//po_ban_status  OUT
				callable.registerOutParameter(21, Types.VARCHAR);//po_home_province  OUT
				callable.registerOutParameter(22, Types.NUMERIC);//po_bill_cycle  OUT
				callable.registerOutParameter(23, Types.NUMERIC);//po_cycle_close_day  OUT
				callable.registerOutParameter(24, Types.VARCHAR);//po_dealer_code  OUT
				callable.registerOutParameter(25, Types.VARCHAR);//po_sales_rep_code  OUT
				callable.registerOutParameter(26, Types.VARCHAR);//po_email_address  OUT
				callable.registerOutParameter(27, Types.VARCHAR);//po_lang_pref  OUT

				callable.execute();

				accountType = callable.getString(2).charAt(0);
				accountSubType = callable.getString(3).charAt(0);
				if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER  && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PAGER_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_AUTOTEL_REGULAR))
				{
					postpaidConsumer = true;
				}
				else if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER  && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_TELUS_EMPLOYEE || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_TELUS_EMPLOYEE ))
				{
					postpaidConsumerEmployee = true;
				}
				else if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER  && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_TELUS_EMPLOYEE_NEW || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_PERSONAL ))
				{
					postpaidConsumerEmployeeNew = true;
				}
				else if (accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_REGULAR ||
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_REGULAR || accountSubType==AccountSummary.ACCOUNT_SUBTYPE_PAGER_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_AUTOTEL_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_CONNECT_REGULAR ))
				{
					postpaidBusinessRegular = true ;
				}
				else if (accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_OFFICAL)
				{
					postpaidBusinessOffical = true ;
				}
				else if (accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS &&(accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_PERSONAL ||
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_PERSONAL|| accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_PERSONAL || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_CONNECT_PERSONAL ))
				{
					postpaidBusinessPersonal = true;
				}
				else if (accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_DEALER || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_DEALER))
				{
					postpaidBusinessDealer = true;
				}
				else if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_PREPAID)
				{
					prepaidConsumer = true ;
				}
				else if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_QUEBECTEL)
				{
					quebecTelPrepaidConsumer = true ;
				}
				else if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_WESTERN_PREPAID)
				{
					westernPrepaidConsumer = true ;
				}
				else if (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_REGULAR)
				{
					IDENCorporate = true;
				}
				else if (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_PRIVATE_NETWORK_PLUS) {
					IDENCorporateVPN = true;
				}
				else if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PAGER_BOXED)
				{
					postpaidBoxedConsumer = true;
				}
				else if (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE  && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_REGIONAL)
				{
					postpaidCorporateRegional = true;
				}
				else if (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE  && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_AUTOTEL_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_AUTOTEL_EARS))
				{
					postpaidCorporateAutotel = true;
				}
				else if (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE  && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_INDIVIDUAL || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_EMPLOYEE))
				{
					postpaidCorporatePersonal = true;
				}
				else if (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE  &&
						(accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_FEDERAL_GOVERNMENT ||
								accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_ENTERPRISE ||
								accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_CORPORATE ||
								accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_ABORIGINAL ||
								accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_FUSION_EAST_CONV ||
								accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_KEY ||
								accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_OFFICIAL ||
								accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_CNBS ||
								accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_GOVERNMENT ||
								accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_NATIONAL_STRATEGIC ||
								accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_REGIONAL_STRATEGIC ||
								accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_TMI_AFFILIATE ||
								accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_ANYWHERE ||
								accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_TMI_DIVISION)) {
					postpaidCorporateRegular = true;
				}
				else
				{
					postpaidLikeBusinessRegular = true;
				}

				// create AccountInfoHelper to recognize account sub type
				AccountInfo accountInfoHelper = new AccountInfo();
				accountInfoHelper.setAccountType(accountType);
				accountInfoHelper.setAccountSubType(accountSubType);

				// populate specific Account Info
				if (postpaidConsumer || postpaidBoxedConsumer || postpaidConsumerEmployee || postpaidConsumerEmployeeNew) {

					if (postpaidConsumerEmployee) {
						accountInfo = PostpaidEmployeeAccountInfo.newPCSInstance0();
					} else if (postpaidConsumerEmployeeNew && !accountInfoHelper.isIDEN()) {
						accountInfo = PostpaidEmployeeAccountInfo.newPCSInstance1();
					} else if (postpaidConsumerEmployeeNew && accountInfoHelper.isIDEN()) {
						accountInfo = PostpaidEmployeeAccountInfo.newIDENInstance1();
					} else if (accountInfoHelper.isIDEN()) {
						accountInfo = PostpaidConsumerAccountInfo.newIDENInstance();
					} else if (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PAGER_REGULAR) {
						accountInfo = PostpaidConsumerAccountInfo.newPagerInstance();
					} else if (postpaidBoxedConsumer) {
						accountInfo = PostpaidBoxedConsumerAccountInfo.newPagerInstance();
					} else if (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_AUTOTEL_REGULAR) {
						accountInfo = PostpaidConsumerAccountInfo.newAutotelInstance();
					} else {
						accountInfo = PostpaidConsumerAccountInfo.newPCSInstance();
					}


				} else if (postpaidBusinessPersonal || postpaidCorporatePersonal) {

					if (accountInfoHelper.isIDEN()) {
						accountInfo = PostpaidBusinessPersonalAccountInfo.newIDENInstance();
					} 
					if (postpaidCorporatePersonal){
						accountInfo = PostpaidCorporatePersonalAccountInfo.newInstance0(accountSubType);
					}  else {
						accountInfo = PostpaidBusinessPersonalAccountInfo.newPCSInstance();
					}

				} else if (prepaidConsumer || quebecTelPrepaidConsumer || westernPrepaidConsumer) {

					if (prepaidConsumer) {
						accountInfo = PrepaidConsumerAccountInfo.newPCSInstance(AccountSummary.ACCOUNT_SUBTYPE_PCS_PREPAID);
					} else if (quebecTelPrepaidConsumer) {
						accountInfo = PrepaidConsumerAccountInfo.newPCSInstance(AccountSummary.ACCOUNT_SUBTYPE_PCS_QUEBECTEL);
					} else {
						accountInfo = WesternPrepaidConsumerAccountInfo.newPCSInstance();
					}

				} else if (postpaidBusinessRegular || postpaidLikeBusinessRegular || IDENCorporateVPN || IDENCorporate || postpaidBusinessDealer
						|| postpaidCorporateRegional || postpaidBusinessOffical || postpaidCorporateAutotel || postpaidCorporateRegular) {

					if (accountInfoHelper.isIDEN()) {
						if (IDENCorporateVPN) {
							accountInfo = PostpaidCorporateRegularAccountInfo.newInstance(AccountSummary.ACCOUNT_SUBTYPE_IDEN_PRIVATE_NETWORK_PLUS);
						} else if (IDENCorporate) {
							accountInfo = PostpaidCorporateRegularAccountInfo.newInstance(accountSubType);
						} else if (postpaidBusinessRegular || postpaidLikeBusinessRegular) {
							accountInfo = PostpaidBusinessRegularAccountInfo.newIDENInstance();
						} else if (postpaidBusinessDealer) {
							accountInfo = PostpaidBusinessDealerAccountInfo.newIDENInstance0();
						}
					} else if (postpaidBusinessOffical) {
						accountInfo = PostpaidBusinessOfficialAccountInfo.newPCSInstance0();
					} else if (postpaidBusinessDealer) {
						accountInfo = PostpaidBusinessDealerAccountInfo.newPCSInstance0();
					} else if (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PAGER_REGULAR) {
						accountInfo = PostpaidBusinessRegularAccountInfo.newPagerInstance();
					} else if (postpaidCorporateRegional) {
						accountInfo = PostpaidCorporateRegularAccountInfo.newInstance(AccountSummary.ACCOUNT_SUBTYPE_PCS_REGIONAL);
					} else if (postpaidCorporateAutotel) {
						accountInfo = PostpaidCorporateRegularAccountInfo.newInstance(accountSubType);
					} else if (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_AUTOTEL_REGULAR) {
						accountInfo = PostpaidBusinessRegularAccountInfo.newAutotelInstance();
					} else if (postpaidCorporateRegular) {
						accountInfo = PostpaidCorporateRegularAccountInfo.newInstance0(accountSubType);
					} else {
						accountInfo = PostpaidBusinessRegularAccountInfo.newPCSInstance();
					}
				}
				// Populate common Account Info
				accountInfo.setBanId(pBan);
				accountInfo.setAccountType(accountType);
				accountInfo.setAccountSubType(accountSubType);
				accountInfo.setBanSegment(callable.getString(4));
				accountInfo.setBanSubSegment(callable.getString(5));
				accountInfo.setBrandId(callable.getInt(6));
				accountInfo.getFinancialHistory0().getCollectionStep0().setPath(callable.getString(7));
				accountInfo.getFinancialHistory0().getNextCollectionStep0().setPath(callable.getString(7));
				accountInfo.setActiveSubscribersCount(callable.getInt(11));
				accountInfo.setReservedSubscribersCount(callable.getInt(12));
				accountInfo.setSuspendedSubscribersCount(callable.getInt(13));
				accountInfo.setCancelledSubscribersCount(callable.getInt(14));
				accountInfo.setAllActiveSubscribersCount(callable.getInt(15));
				accountInfo.setAllReservedSubscribersCount(callable.getInt(16));
				accountInfo.setAllSuspendedSubscribersCount(callable.getInt(17));
				accountInfo.setAllCancelledSubscribersCount(callable.getInt(18));
				accountInfo.setStatusActivityReasonCode(callable.getString(19));
				accountInfo.setStatus(callable.getString(20).charAt(0));
				accountInfo.setHomeProvince(callable.getString(21));
				accountInfo.setBillCycle(callable.getInt(22));
				accountInfo.setBillCycleCloseDay(callable.getInt(23));
				accountInfo.setDealerCode(callable.getString(24));
				accountInfo.setSalesRepCode(callable.getString(25));
				accountInfo.setEmail(callable.getString(26));
				accountInfo.setLanguage(callable.getString(27));


				// set Debt Summary
				// no Past Due Payments
				if ((callable.getDouble(9) == 0)&&(callable.getDouble(10) == 0)) {
					accountInfo.getFinancialHistory0().getDebtSummary0().setCurrentDue(callable.getDouble(8));
					accountInfo.getFinancialHistory0().getDebtSummary0().setPastDue(0);
				} else {
					accountInfo.getFinancialHistory0().getDebtSummary0().setCurrentDue(callable.getDouble(9));
					accountInfo.getFinancialHistory0().getDebtSummary0().setPastDue(callable.getDouble(10));
				}

				return accountInfo;
			}
		});
	}

	@Override
	public int retrieveBANByPhoneNumber(String pPhoneNumber) {

		String phoneNumber = AttributeTranslator.replaceString(pPhoneNumber.trim()," ","");
		phoneNumber = AttributeTranslator.replaceString(phoneNumber.trim(),"-","");
		final String phoneNumberParam = phoneNumber;

		String call = "{ ? = call ra_utility_pkg.getBanByPhoneNumber(?) } ";

		Integer billingAccountNumber = super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<Integer>() {

			@Override
			public Integer doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				Integer ban = null;
				
				callable.registerOutParameter(1, OracleTypes.NUMBER);
				callable.setString(2, phoneNumberParam);

				callable.execute();
				ban = Integer.valueOf(callable.getInt(1));
				
				return ban;
			}
		});
		
		return (billingAccountNumber != null ? billingAccountNumber.intValue() : 0);
	}

	@Override
	public List<AccountInfo> retrieveAccountsByDealership(
			char accountStatus, String dealerCode, Date startDate,
			Date endDate,final int maximum) {
		String queryStr = "select substr(c.dealer_code, 1,10), substr(c.dealer_code, 11) " +
		" , ltrim(nd.first_name||' '||nd.MIDDLE_INITIAL||' '||nd.last_business_name) " +
		" , ba.ban, ba.ban_status, ba.account_type, ba.account_sub_type, ch.credit_req_sts, " +
		" ch.credit_class, ch.credit_limit , cd.deposit_amt, " +
		" ch.credit_result_2, ch.beacon_score ,  " +
		" to_number(NVL(ba.brand_id,'"+Brand.BRAND_ID_TELUS+"')) AS brandId , " +
		" max(decode(sign(s.effective_date - ba.status_last_date), 1, s.effective_date, ba.status_last_date) ) eff_date " +
		"    from  customer c,  " +
		"   billing_account ba,  " +
		"  subscriber s,  " +
		"  address_name_link anl,  " +
		"  name_data nd,  " +
		" credit_history ch,  " +
		" crd_deposit cd  " +
		" where  ( c.dealer_code like " + "'" + dealerCode + "'||'%' or s.dealer_code like "  + "'" + dealerCode + "'||'%' )  " +
		" and ba.customer_id = c.customer_id  " +
		" and (ba.ban_status = " + "'" + accountStatus + "'"  + " and " + "'" + accountStatus + "'!='2'  "  +
		" or ba.ban_status in ('O','T') " + " and " + "'" + accountStatus + "'='2')  "  +
		" and ba.ban = s.customer_ban (+)  " +
		" and ( trunc(ba.status_last_date) >= to_date('" + dateFormat.format(startDate) + "','mm/dd/yyyy') or trunc(s.effective_date)  >= " + "to_date('" + dateFormat.format(startDate) + "','mm/dd/yyyy')) " +
		" and ( trunc(ba.status_last_date) <=  to_date('" + dateFormat.format(endDate) + "','mm/dd/yyyy') or trunc(s.effective_date)  <= " + "to_date('" + dateFormat.format(endDate) + "','mm/dd/yyyy')) " +
		" and ch.ban (+) = ba.ban  " +
		" and ( ch.crd_seq_no =  " +
		"            (select max(ch2.crd_seq_no)  " +
		"           from credit_history ch2  " +
		"           where ch2.ban = ch.ban ) " +
		"     or ch.crd_seq_no is null )  " +
		"  and cd.deposit_seq_no (+)  = ch.deposit_seq_no  " +
		"  and cd.product_type (+) = 'C'  " +
		" and anl.customer_id = ba.customer_id  " +
		" and anl.ban= ba.ban  " +
		"  and anl.link_type = 'B'  " +
		"  and anl.effective_date =  " +
		"      (select max(aanl.effective_date)  " +
		"    from address_name_link aanl  " +
		"    where aanl.customer_id = anl.customer_id  " +
		"   and aanl.ban = anl.ban  " +
		"  and aanl.link_type = 'B')  " +
		"  and (anl.expiration_date is null or anl.expiration_date > sysdate )  " +
		"  and nd.name_id = anl.name_id  " +
		" group by c.dealer_code, ltrim(nd.first_name||' '||nd.MIDDLE_INITIAL||' '||nd.last_business_name) , ba.ban  " +
		"    , ba.ban_status, ba.account_type, ba.account_sub_type,  " +
		"ch.credit_req_sts, ch.credit_class, ch.credit_limit , cd.deposit_amt,  " +
		" ch.credit_result_2, ch.beacon_score, to_number(NVL(ba.brand_id,'"+Brand.BRAND_ID_TELUS+"'))  " +
		" order by eff_date desc " ;

		return super.getKnowbilityJdbcTemplate().query(queryStr, new ResultSetExtractor<List<AccountInfo>>() {

			@Override
			public List<AccountInfo> extractData(ResultSet result)
			throws SQLException, DataAccessException {				
				List<AccountInfo> accountList = new ArrayList<AccountInfo>();
				int counter = 0;
				
					while ((result.next())&&(counter < maximum)){
						AccountInfo account = new AccountInfo();
						account.setDealerCode(result.getString(1));
						account.setSalesRepCode(result.getString(2));
						account.setFullName(result.getString(3));
						account.setBanId(result.getInt(4));
						account.setStatus(result.getString(5).charAt(0));
						account.setAccountType(result.getString(6).charAt(0));
						account.setAccountSubType(result.getString(7).charAt(0));
						account.getCreditCheckResult0().setCreditClass(result.getString(9));
						account.getCreditCheckResult0().setLimit(result.getDouble(10));
						account.getCreditCheckResult0().setDeposit(result.getDouble(11));
						account.getCreditCheckResult0().setMessage(result.getString(12));
						account.getCreditCheckResult0().setCreditScore(result.getInt(13));
						account.setBrandId(result.getInt(14));
						account.setLastChangesDate(result.getDate(15));
						accountList.add(account);
						counter++;
					}
				
				return accountList;
			}

		});
	}

	@Override
	public List<AccountInfo> retrieveAccountsBySalesRep(
			char accountStatus, String dealerCode, String salesRepCode,
			Date startDate, Date endDate, final int maximum) {

		String queryStr = " select substr(c.dealer_code, 1,10), substr(c.dealer_code, 11) , ltrim(nd.first_name||' '||nd.MIDDLE_INITIAL||' '||nd.last_business_name)  " +
		" , ba.ban, ba.ban_status, ba.account_type, ba.account_sub_type, ch.credit_req_sts,  " +
		" ch.credit_class, ch.credit_limit , cd.deposit_amt,  " +
		" ch.credit_result_2, ch.beacon_score ,  " +
		" to_number(NVL(ba.brand_id,'"+Brand.BRAND_ID_TELUS+"')) as brandId , " +
		" max(decode(sign(s.effective_date - ba.status_last_date), 1, s.effective_date, ba.status_last_date) ) eff_date  " +
		" from  customer c,  " +
		" billing_account ba,  " +
		" subscriber s,  " +
		" address_name_link anl,  " +
		" name_data nd,  " +
		" credit_history ch,  " +
		" crd_deposit cd  " +
		" where ( c.dealer_code = " + "'" + dealerCode + "'||'" + salesRepCode + "'" +  " or s.dealer_code = "  + "'" + dealerCode + "'||'" + salesRepCode + "' )  " +
		" and ba.customer_id = c.customer_id   " +
		" and (ba.ban_status = " + "'" + accountStatus + "'"  + " and " + "'" + accountStatus + "'!='2'  "  +
		" or ba.ban_status in ('O','T') " + " and " + "'" + accountStatus + "'='2')  "  +
		" and ba.ban = s.customer_ban (+)  " +
		" and ( trunc(ba.status_last_date) >= to_date('" + dateFormat.format(startDate) + "','mm/dd/yyyy') or trunc(s.effective_date)  >= " + "to_date('" + dateFormat.format(startDate) + "','mm/dd/yyyy')) " +
		" and ( trunc(ba.status_last_date) <=  to_date('" + dateFormat.format(endDate) + "','mm/dd/yyyy') or trunc(s.effective_date)  <= " + "to_date('" + dateFormat.format(endDate) + "','mm/dd/yyyy')) " +
		" and ch.ban (+) = ba.ban   " +
		" and ( ch.crd_seq_no =   " +
		" (select max(ch2.crd_seq_no)  " +
		" from credit_history ch2   " +
		" where ch2.ban = ch.ban )" +
		" or ch.crd_seq_no is null )  " +
		" and cd.deposit_seq_no (+)  = ch.deposit_seq_no   " +
		" and cd.product_type (+) = 'C'   " +
		" and anl.customer_id = ba.customer_id  " +
		" and anl.ban= ba.ban  " +
		" and anl.link_type = 'B'   " +
		" and anl.effective_date =   " +
		" (select max(aanl.effective_date)   " +
		" from address_name_link aanl   " +
		" where aanl.customer_id = anl.customer_id   " +
		" and aanl.ban = anl.ban   " +
		" and aanl.link_type = 'B')  " +
		" and (anl.expiration_date is null or anl.expiration_date > sysdate )  " +
		" and nd.name_id = anl.name_id  " +
		" group by c.dealer_code, ltrim(nd.first_name||' '||nd.MIDDLE_INITIAL||' '||nd.last_business_name) , ba.ban  " +
		" , ba.ban_status, ba.account_type, ba.account_sub_type,  " +
		" ch.credit_req_sts, ch.credit_class, ch.credit_limit , cd.deposit_amt,  " +
		" ch.credit_result_2, ch.beacon_score, to_number(NVL(ba.brand_id,'"+Brand.BRAND_ID_TELUS+"'))  " +
		" order by eff_date desc ";

		return super.getKnowbilityJdbcTemplate().query(queryStr, new ResultSetExtractor<List<AccountInfo>>() {

			@Override
			public List<AccountInfo> extractData(ResultSet result)
			throws SQLException, DataAccessException {
				List<AccountInfo> accountList = new ArrayList<AccountInfo>();
				int counter = 0;
				
					while ((result.next())&&(counter < maximum)){
						AccountInfo account = new AccountInfo();
						account.setDealerCode(result.getString(1));
						account.setSalesRepCode(result.getString(2));
						account.setFullName(result.getString(3));
						account.setBanId(result.getInt(4));
						account.setStatus(result.getString(5).charAt(0));
						account.setAccountType(result.getString(6).charAt(0));
						account.setAccountSubType(result.getString(7).charAt(0));
						account.getCreditCheckResult0().setCreditClass(result.getString(9));
						account.getCreditCheckResult0().setLimit(result.getDouble(10));
						account.getCreditCheckResult0().setDeposit(result.getDouble(11));
						account.getCreditCheckResult0().setMessage(result.getString(12));
						account.getCreditCheckResult0().setCreditScore(result.getInt(13));
						account.setBrandId(result.getInt(14));
						account.setLastChangesDate(result.getDate(15));
						accountList.add(account);
						counter++;
					}
				
				return accountList;
			}			
		});
	}

	@Override
	public int[] retrieveBanIds(final char accountType, final char accountSubType,
			final char banStatus, final int maximum) {
		String queryStr =
			"Select  " +
			"    BILLING_ACCOUNT.BAN " +
			"from  " +
			"    BILLING_ACCOUNT " +
			"where   " +
			"    BILLING_ACCOUNT.ACCOUNT_TYPE=? and  " +
			"    BILLING_ACCOUNT.ACCOUNT_SUB_TYPE=? and  " +
			"    BILLING_ACCOUNT.BAN_STATUS=? and " +
			"    ROWNUM <= ?";
		return super.getKnowbilityJdbcTemplate().query(queryStr, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstmt) throws SQLException {
				pstmt.setString(1, String.valueOf(accountType));
				pstmt.setString(2, String.valueOf(accountSubType));
				pstmt.setString(3, String.valueOf(banStatus));
				pstmt.setInt(4, maximum);
			}

		}, new RetrieveBanIdResultSetExtractor(maximum));
	}

	@Override
	public int[] retrieveBanIdByAddressType(final char accountType,
			final char accountSubType,final char banStatus,final char addressType,final int maximum) {
		String queryStr =
			"Select " +
			"  BILLING_ACCOUNT.BAN " +
			"from " +
			"  BILLING_ACCOUNT, ADDRESS_NAME_LINK, ADDRESS_DATA " +
			"where " +
			"   ROWNUM <= ? and  " +
			"   BILLING_ACCOUNT.ACCOUNT_TYPE = ? and " +
			"   BILLING_ACCOUNT.ACCOUNT_SUB_TYPE = ? and " +
			"   BILLING_ACCOUNT.BAN_STATUS = ? and " +
			"   ADDRESS_NAME_LINK.BAN = BILLING_ACCOUNT.BAN and " +
			"   ADDRESS_NAME_LINK.EXPIRATION_DATE is null and " +
			"   ADDRESS_NAME_LINK.LINK_TYPE = 'B' and " +
			"   ADDRESS_DATA.ADDRESS_ID = ADDRESS_NAME_LINK.ADDRESS_ID and  " +
			"   ADDRESS_DATA.ADR_TYPE = ? " +
			"";
		return super.getKnowbilityJdbcTemplate().query(queryStr, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstmt) throws SQLException {
				pstmt.setInt(1, maximum);
				pstmt.setString(2, String.valueOf(accountType));
				pstmt.setString(3, String.valueOf(accountSubType));
				pstmt.setString(4, String.valueOf(banStatus));
				pstmt.setString(5, String.valueOf(addressType));
			}

		}, new RetrieveBanIdResultSetExtractor(maximum));
	}

	private static class RetrieveBanIdResultSetExtractor implements ResultSetExtractor<int[]> {

		private int maximum;

		public RetrieveBanIdResultSetExtractor(int maximum) {
			this.maximum = maximum;
		}

		@Override
		public int[] extractData(ResultSet result) throws SQLException,
		DataAccessException {
			int[] list = new int[maximum];
			int count = 0;
			while (result.next()) {
				list[count] = result.getInt(1);
				count++;
			}

			if (count == maximum) {
				return list;
			} else {
				int[] newList = new int[count];
				System.arraycopy(list, 0, newList, 0, count);
				return newList;
			}
		}			
	}

	@Override
	public BillParametersInfo retrieveBillParamsInfo(final int banId) {
		String call = "{ call ra_utility_pkg.getBillingParamNoOfInvoice(?, ?, ?, ?) }";

		return super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<BillParametersInfo>() {

			@Override
			public BillParametersInfo doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {
				BillParametersInfo billParamsInfo = new BillParametersInfo();

				callable.setInt(1, banId );
				callable.registerOutParameter(2, OracleTypes.INTEGER);
				callable.registerOutParameter(3, OracleTypes.VARCHAR);
				callable.registerOutParameter(4, OracleTypes.VARCHAR);

				callable.execute();

				billParamsInfo.setNoOfInvoice(callable.getShort(2));
				billParamsInfo.setMediaCategory(callable.getString(3));
				billParamsInfo.setBillFormat(callable.getString(4));

				return billParamsInfo;
			}
		});
	}

	@Override
	public int retrieveBanByImsi(final String imsi) {
		String callString = "{call RA_UTILITY_PKG.GetAccountByIMSI(?,?)}";

		try {

			return super.getKnowbilityJdbcTemplate().execute(callString, new CallableStatementCallback<Integer>() {

				@Override
				public Integer doInCallableStatement(CallableStatement callable)
				throws SQLException, DataAccessException {
					callable.setString(1, imsi);
					callable.registerOutParameter(2, Types.INTEGER);

					callable. execute();

					return callable.getInt(2);
				}			
			});
		} catch (UncategorizedSQLException e) {
			SQLException sqe = e.getSQLException();
			if (sqe != null) {
				if (sqe.getErrorCode() == 20101 || sqe.getErrorCode() == 20102) {
					return 0;
				}
			}
			throw e;
		}
	}

	@Override
	public List<AccountInfo> retrieveAccountsBySerialNumber(
			String serialNumber) {
		String queryStr = " select distinct ba.ban, " +
		"        ba.ban_status, " +
		"        ba.account_type, " +
		"        ba.account_sub_type, " +
		"        ba.sys_creation_date, " +
		"        ba.start_service_date, " +
		"        substr(c.dealer_code, 1,10) dealer_code, " +
		"        substr(c.dealer_code, 11) sales_rep_code, " +
		"        c.customer_id, " +
		"        nd.first_name, " +
		"        nd.last_business_name, " +
		"        middle_initial, " +
		"        name_title, " +
		"        ad.adr_primary_ln, " +
		"        ad.adr_city, " +
		"        ad.adr_province, " +
		"        ad.adr_postal, " +
		"        adr_type , " +
		"        adr_secondary_ln , " +
		"        adr_country  , " +
		"        adr_zip_geo_code , " +
		"        adr_state_code , " +
		"        civic_no , " +
		"        civic_no_suffix , " +
		"        adr_st_direction , " +
		"        adr_street_name , " +
		"        adr_street_type , " +
		"        adr_designator , " +
		"        adr_identifier , " +
		"        adr_box , " +
		"        unit_designator, " +
		"        unit_identifier, " +
		"        adr_area_nm , " +
		"        adr_qualifier , " +
		"        adr_site , " +
		"        adr_compartment, " +
		"        c.acc_password, " +
		"        ba.status_actv_code, " +
		"        ba.status_actv_rsn_code, " +
		"        nd.additional_title, " +
		"        ba.status_last_date, " + //
		"        to_number(NVL(ba.brand_id,'"+Brand.BRAND_ID_TELUS+"')) as brandId " +
		" from   physical_device pd, " +
		"        billing_account ba, " +
		"        customer c, " +
		"        address_name_link anl, " +
		"        address_data ad, " +
		"        name_data nd " +
		" where   pd.unit_esn =" + "'" + serialNumber + "'" +
		" and pd.expiration_date is null    " +
		" and     ba.ban=pd.customer_id " +
		" and     c.customer_id = ba.customer_id " +
		" and     anl.customer_id= c.customer_id " +
		" and     anl.expiration_date  is null " +
		" and     anl.link_type = 'B' " +
		" and     nd.name_id = anl.name_id " +
		" and     ad.address_id=anl.address_id ";

		return super.getKnowbilityJdbcTemplate().query(queryStr, new ParameterizedRowMapper<AccountInfo>() {

			@Override
			public AccountInfo mapRow(ResultSet result, int row)
			throws SQLException {
				AccountInfo account = new AccountInfo();

				account.setBanId(result.getInt(1));
				account.setStatus(result.getString(2).charAt(0));
				account.setAccountType(result.getString(3).charAt(0));
				account.setAccountSubType(result.getString(4).charAt(0));
				account.setCreateDate(result.getTimestamp(5));
				account.setStartServiceDate(result.getTimestamp(6));
				account.setDealerCode(result.getString(7));
				account.setSalesRepCode(result.getString(8));
				account.setCustomerId(result.getInt(9));
				account.getAddress0().setPrimaryLine(result.getString(14));
				account.getAddress0().setCity(result.getString(15));
				account.getAddress0().setProvince(result.getString(16));
				account.getAddress0().setPostalCode(result.getString(17));
				account.getAddress0().setAddressType(result.getString(18));
				String tempAddressType = account.getAddress0().getAddressType();
				account.getAddress0().setSecondaryLine(result.getString(19));
				account.getAddress0().setCountry(result.getString(20));
				account.getAddress0().setZipGeoCode(result.getString(21));
				account.getAddress0().setForeignState(result.getString(22));
				account.getAddress0().setCivicNo(result.getString(23));
				account.getAddress0().setCivicNoSuffix(result.getString(24));
				account.getAddress0().setStreetDirection(result.getString(25));
				account.getAddress0().setStreetName(result.getString(26));
				account.getAddress0().setStreetType(result.getString(27));
				account.getAddress0().setRrDesignator(result.getString(28));
				account.getAddress0().setRrIdentifier(result.getString(29));
				account.getAddress0().setPoBox(result.getString(30));
				account.getAddress0().setUnitDesignator(result.getString(31));
				account.getAddress0().setUnitIdentifier(result.getString(32));
				account.getAddress0().setRrAreaNumber(result.getString(33));
				account.getAddress0().setRrQualifier(result.getString(34));
				account.getAddress0().setRrSite(result.getString(35));
				account.getAddress0().setRrCompartment(result.getString(36));
				account.getAddress0().setAddressType(tempAddressType);
				account.setFullName(AttributeTranslator.emptyFromNull(result.getString(10)) +
						AttributeTranslator.emptyFromNull(" ", result.getString(12)) +
						AttributeTranslator.emptyFromNull(" ", result.getString(11)));
				account.setPin(result.getString(37));
				account.setStatusActivityCode(result.getString(38));
				account.setStatusActivityReasonCode(result.getString(39));
				account.setAdditionalLine(result.getString(40));
				account.setStatusDate(result.getTimestamp(41));
				account.setBrandId(result.getInt(42));

				return account;
			}

		});
	}

	@Override
	public String retrieveCorporateName(final int id) {
		String call = "{ ? = call ra_utility_pkg.get_corporate_name(?) } ";

		return super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<String>() {

			@Override
			public String doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {
				String corpName = null;
				callable.registerOutParameter(1, OracleTypes.VARCHAR);
				callable.setInt(2, id);

				callable.execute();
				corpName = callable.getString(1);

				return corpName;
			}
		});
	}

	@Override
	public int getClientAccountId(int ban) {
		try{
			return super.getCodsJdbcTemplate().queryForInt("select client_account_id from client_account where ban ='"+ban+"'");
		}catch(EmptyResultDataAccessException e){
			return -1;
		}

	}

	@Override
	public String getPaperBillSupressionAtActivationInd(final int pBan) {
		String call = "{call portal_notification_pkg.getsuppratactivationind  (?,?) }";

		return super.getCodsJdbcTemplate().execute(call, new CallableStatementCallback<String>() {

			@Override
			public String doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {
				String supressionAtActivationInd = null;	
			
				callable.setInt(1, pBan);
				callable.registerOutParameter(2, OracleTypes.VARCHAR);
	
				callable.execute();
				String actInd = callable.getString(2);
	
				supressionAtActivationInd = actInd == null ? AccountSummary.BILL_SUPPRESSION_AT_ACTIVATION_UNKNOWN : actInd;
				
				return supressionAtActivationInd;
			}
		});
	}

	@Override
	public BusinessCreditIdentityInfo[] retrieveBusinessList(int ban) {
		String queryStr =
			"select 	cbl.company_name, cbl.market_account " +
			"from		credit_history ch, crd_business_list cbl " +
			"where 	ch.ban = ? " +
			"and 		ch.crd_seq_no = " +
			"(select max(crd_seq_no) from credit_history ch2 where ch2.ban=ch.ban and ch2.credit_req_sts = 'D' ) " +
			"and 		ch.business_l_seq_no = cbl.business_l_seq_no " +
			"order by cbl.crd_seq_no";

		List<BusinessCreditIdentityInfo> returnList = super.getKnowbilityJdbcTemplate().query(queryStr, new Object[]{ban}
		, new RowMapper<BusinessCreditIdentityInfo>() {

			@Override
			public BusinessCreditIdentityInfo mapRow(ResultSet result, int rownum)
			throws SQLException {
				BusinessCreditIdentityInfo info = new BusinessCreditIdentityInfo();
				info.setCompanyName(result.getString("company_name"));
				info.setMarketAccount(result.getDouble("market_account"));
				return info;
			}

		});

		if (returnList != null) {
			return returnList.toArray(new BusinessCreditIdentityInfo[returnList.size()]);
		} else {
			return new BusinessCreditIdentityInfo[0];
		}
	}

	@Override
	public ConsumerNameInfo[] retrieveAuthorizedNames(int ban) {
		String queryStr =   "select "
			+ "  b.name_title, "
			+ "  b.first_name, "
			+ "  b.middle_initial, "
			+ "  b.last_business_name, "
			+ "  b.name_suffix, "
			+ "  b.additional_title "
			+ "from  "
			+ "  address_name_link a,  "
			+ "  name_data b "
			+ "where a.ban = ? "
			+ "  and a.name_id = b.name_id "
			+ "  and a.link_type in ('A', 'S') "
			+ "  and a.expiration_date is null "
			+ "order by a.link_type ";

		List<ConsumerNameInfo> nameList = super.getKnowbilityJdbcTemplate().query(queryStr, new Object[]{ban}, new RowMapper<ConsumerNameInfo>() {

			@Override
			public ConsumerNameInfo mapRow(ResultSet result, int rownum)
			throws SQLException {
				ConsumerNameInfo info = new ConsumerNameInfo();

				info.setTitle(result.getString(1));
				info.setFirstName(result.getString(2));
				info.setMiddleInitial(result.getString(3));
				info.setLastName(result.getString(4));
				info.setGeneration(result.getString(5));
				info.setAdditionalLine(result.getString(6));

				return info;
			}

		});

		if (nameList != null) {
			return nameList.toArray(new ConsumerNameInfo[nameList.size()]);
		} else {
			return new ConsumerNameInfo[0];
		}
	}

	@Override
	public List<StatusChangeHistoryInfo> retrieveStatusChangeHistory(int ban,
			Date fromDate, Date toDate) {
		String formattedFromDate = dateFormat.format(fromDate) ;
		String formattedToDate = dateFormat.format(toDate) ;

		String queryStr = " SELECT  expiration_date, status_actv_code, status_actv_rsn_code, ban_status " +
		" FROM    ban_bill_history " +
		" WHERE   ban = " + ban +
		" AND   expiration_date between to_date(?,'mm/dd/yyyy') and " +
		"                                       to_date(?,'mm/dd/yyyy') " +
		" AND   status_actv_code in ('CAN', 'CBN', 'CCN', 'MCN', " +
		"                                  'NCL', 'RSP', 'SUS')" +
		" UNION " +
		" SELECT  status_last_date, status_actv_code, status_actv_rsn_code, ban_status " +
		" FROM    billing_account " +
		" WHERE   ban = " + ban +
		" AND   status_last_date between to_date(?,'mm/dd/yyyy') and " +
		"                                      to_date(?,'mm/dd/yyyy') " +
		" AND   status_actv_code in ('CAN', 'CBN', 'CCN', 'MCN', " +
		"                                  'NCL', 'RSP', 'SUS')";

		return super.getKnowbilityJdbcTemplate().query(queryStr, new Object[]{formattedFromDate, formattedToDate,
				formattedFromDate, formattedToDate}, new RowMapper<StatusChangeHistoryInfo>() {
			@Override
			public StatusChangeHistoryInfo mapRow(ResultSet result,
					int rowNum) throws SQLException {
				StatusChangeHistoryInfo statusChangeHistoryInfo = new StatusChangeHistoryInfo();
				statusChangeHistoryInfo.setDate(result.getDate(1));
				statusChangeHistoryInfo.setActivityTypeCode(result.getString(2));
				statusChangeHistoryInfo.setReasonCode(result.getString(3));
				statusChangeHistoryInfo.setBanStatus(result.getString(4));

				return statusChangeHistoryInfo;
			}
		});
	}

	@Override
	public ContactDetailInfo getCustomerContactInfo(int ban) {
		String sql = "SELECT contact_telno, contact_tn_extno, contact_faxno, work_telno, work_tn_extno, home_telno, " +
					"other_telno, other_extno, other_tn_type, email_address " +
					"FROM CUSTOMER WHERE customer_id = ?";
		
		return super.getKnowbilityJdbcTemplate().query(sql, new Object[] {ban}, new ResultSetExtractor<ContactDetailInfo>() {

			@Override
			public ContactDetailInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				ContactDetailInfo contactDetailInfo = null;
				
					if (rs.next()) {
						contactDetailInfo = new ContactDetailInfo();
						contactDetailInfo.setContactPhone(rs.getString("contact_telno"));
						contactDetailInfo.setContactPhoneExtension(rs.getString("contact_tn_extno"));
						contactDetailInfo.setContactFax(rs.getString("contact_faxno"));
						contactDetailInfo.setWorkPhone(rs.getString("work_telno"));
						contactDetailInfo.setWorkPhoneExtension(rs.getString("work_tn_extno"));
						contactDetailInfo.setHomePhone(rs.getString("home_telno"));
						contactDetailInfo.setOtherPhone(rs.getString("other_telno"));
						contactDetailInfo.setOtherPhoneExt(rs.getString("other_extno"));
						contactDetailInfo.setOtherPhoneType(rs.getString("other_tn_type"));
						contactDetailInfo.setEmailAddress(rs.getString("email_address"));
					}
				
				return contactDetailInfo;
			}
			
		});
	}

	@Override
	public PersonalCreditInfo retrievePersonalCreditInformation(int ban)
			throws ApplicationException {
		String sql = "SELECT birth_date, customer_ssn, drivr_licns_no, drivr_licns_state, drivr_licns_exp_dt, " +
				"gur_cr_card_no, gur_pymt_card_first_six_str, gur_pymt_card_last_four_str, gur_cr_card_exp_dt " +
				"FROM CUSTOMER " +
				"WHERE customer_id = ?";

		return super.getKnowbilityJdbcTemplate().query(sql, new Object[] {ban}, new ResultSetExtractor<PersonalCreditInfo>() {
		
		@Override
		public PersonalCreditInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
			PersonalCreditInfo personalCreditInfo = null;
			
				if (rs.next()) {
					personalCreditInfo = new PersonalCreditInfo();
					personalCreditInfo.setBirthDate(rs.getDate("birth_date"));
					personalCreditInfo.setDriversLicense(rs.getString("drivr_licns_no"));
					personalCreditInfo.setDriversLicenseExpiry(rs.getDate("drivr_licns_exp_dt"));
					personalCreditInfo.setDriversLicenseProvince(rs.getString("drivr_licns_state"));
					personalCreditInfo.setSin(rs.getString("customer_ssn"));
					CreditCardInfo creditCardInfo = personalCreditInfo.getCreditCard0();
					creditCardInfo.setLeadingDisplayDigits(rs.getString("gur_pymt_card_first_six_str"));
					creditCardInfo.setTrailingDisplayDigits(rs.getString("gur_pymt_card_last_four_str"));
					creditCardInfo.setToken(rs.getString("gur_cr_card_no"));
					Calendar expiryDate = Calendar.getInstance();
					if(rs.getDate("gur_cr_card_exp_dt")!=null){
						expiryDate.setTime(rs.getDate("gur_cr_card_exp_dt"));
						creditCardInfo.setExpiryMonth(expiryDate.get(Calendar.MONTH)+1);
						creditCardInfo.setExpiryYear(expiryDate.get(Calendar.YEAR));
					}
				}
			
			return personalCreditInfo;
		}
		
		});
	}

	@Override
	public BusinessCreditInfo retrieveBusinessCreditInformation(int ban)
			throws ApplicationException {
		String sql = "SELECT incorporation_no, incorporation_date FROM CUSTOMER WHERE customer_id = ?";

		return super.getKnowbilityJdbcTemplate().query(sql, new Object[] {ban}, new ResultSetExtractor<BusinessCreditInfo>() {
		
		@Override
		public BusinessCreditInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
			BusinessCreditInfo businessCreditInfo = null;
			
				if (rs.next()) {
					businessCreditInfo = new BusinessCreditInfo();
					businessCreditInfo.setIncorporationNumber(rs.getString("incorporation_no"));
					businessCreditInfo.setIncorporationDate(rs.getDate("incorporation_date"));
				}
			
			return businessCreditInfo;
		}
		
		});
	}
	
	@Override
	public int retrieveBANBySeatNumber(String seatNumber) {

		String seatNum = AttributeTranslator.replaceString(seatNumber.trim()," ","");
		seatNum = AttributeTranslator.replaceString(seatNum.trim(),"-","");
		final String phoneNumberParam = seatNum;

		String call = "{ ? = call ra_utility_pkg.getBanBySeatNumber(?) } ";

		Integer billingAccountNumber = super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<Integer>() {

			@Override
			public Integer doInCallableStatement(CallableStatement callable) throws SQLException, DataAccessException {
				Integer ban = null;
				
				callable.registerOutParameter(1, OracleTypes.NUMBER);
				callable.setString(2, phoneNumberParam);

				callable.execute();
				ban = Integer.valueOf(callable.getInt(1));
				
				return ban;
			}
		});
		
		return (billingAccountNumber != null ? billingAccountNumber.intValue() : 0);
	}
	
	
	public List<Integer> retrieveBanListByImsi(final String imsi) {
		String sql = "SELECT distinct sr1.ban FROM subscriber_rsource sr1 WHERE sr1.imsi_number = ? AND sr1.resource_type = 'Q' AND sr1.record_exp_date = TO_DATE ('12/31/4700', 'mm/dd/yyyy')";
		List<Integer> banIdList = null;
		banIdList = super.getKnowbilityJdbcTemplate().queryForList(sql,Integer.class, imsi);
		return banIdList;
	}
	
	private Date retrieveLogicalDate(Connection connection) {
		String sql = "SELECT logical_date FROM logical_date ld WHERE ld.logical_date_type = 'O' ";
		return (Date) executePreparedStatementWithConnection(connection, sql, null, new PreparedStatementCallback<Date>() {
			
			@Override
			public Date doInPreparedStatement(PreparedStatement pstmt) throws SQLException, DataAccessException {
				Date logicalDate = null;
				ResultSet rs = null;
				
				try {
					rs = pstmt.executeQuery();
					
					if (rs.next()) {
						logicalDate = rs.getDate("logical_date");
					}
				} finally {
					if (rs != null) {
						rs.close();
					}
				}
				return logicalDate;
			}
		});
	}
}