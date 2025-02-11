package com.telus.cmb.account.informationhelper.dao.impl;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.rsa.cryptoj.c.ps;
import com.telus.api.account.AccountManager;
import com.telus.api.account.BillNotificationContact;
import com.telus.api.account.BillNotificationHistoryRecord;
import com.telus.cmb.account.informationhelper.dao.InvoiceDao;
import com.telus.cmb.account.utilities.AppConfiguration;
import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.eas.account.info.BillNotificationContactInfo;
import com.telus.eas.account.info.BillNotificationHistoryRecordInfo;
import com.telus.eas.account.info.EBillRegistrationReminderInfo;
import com.telus.eas.account.info.InvoiceHistoryInfo;
import com.telus.eas.account.info.InvoicePropertiesInfo;
import com.telus.eas.account.info.SMSEBillRegistrationReminderInfo;
import com.telus.eas.account.info.SubscriberInvoiceDetailInfo;
import com.telus.eas.framework.info.ChargeInfo;

@SuppressWarnings("deprecation")
public class InvoiceDaoImpl extends MultipleJdbcDaoTemplateSupport implements InvoiceDao {

	private static final int ORACLE_REF_CURSOR = -10;
	private static final int CONTACT_TYPE_EMAIL = 5;
	public static String dateFormatSt = "MM/dd/yyyy";
	public static SimpleDateFormat dateFormat   =  new SimpleDateFormat(dateFormatSt);
	private final Logger LOGGER = Logger.getLogger(InvoiceDaoImpl.class);

	@Override
	public BillNotificationContactInfo getLastEBillNotificationSent(final int ban) {
		// TODO Auto-generated method stub

		String callString="{call portal_notification_pkg.getlastebillnotificationsent (?,?) }";
		return super.getCodsJdbcTemplate().execute(callString,new CallableStatementCallback<BillNotificationContactInfo>(){
			@Override
			public BillNotificationContactInfo doInCallableStatement(
					CallableStatement callable) throws SQLException,DataAccessException{

				BillNotificationContactInfo notificationContactInfo = null;
				ResultSet result=null;
				try{
					callable.setInt(1, ban);
					callable.registerOutParameter(2, ORACLE_REF_CURSOR);
					callable.execute();
					result = (ResultSet) callable.getObject(2);

					while (result.next()) {
						notificationContactInfo = new BillNotificationContactInfo();
						notificationContactInfo.setContactType(result.getInt("contact_mechanism_type_id") == CONTACT_TYPE_EMAIL ? BillNotificationContact.CONTACT_TYPE_EMAIL
								: BillNotificationContact.CONTACT_TYPE_SMS);
						notificationContactInfo.setNotificationAddress(result.getString("contact_address_txt"));
						String validationStatus = result.getString("contact_status_cd");
						notificationContactInfo.setEMailValidationStatus(validationStatus == null ? BillNotificationContact.E_MAIL_VALIDATION_STATUS_VALID_NEW :validationStatus);
						notificationContactInfo.setLastNotificationDate(result.getDate("last_notification_date"));
						return notificationContactInfo;
					}
				}finally{
					if (result != null ) {
						result.close();
					}
				}
				return notificationContactInfo;
			}
		});

	}

	@Override
	public List<BillNotificationHistoryRecord> getBillNotificationHistory(final int ban,
			String subscriptionType) {
		String callString="{call portal_notification_pkg.getBillNotifiHistoryForEPost(?,?)}";
		return super.getCodsJdbcTemplate().execute(callString,new CallableStatementCallback<List<BillNotificationHistoryRecord>>(){
			@Override
			public List<BillNotificationHistoryRecord> doInCallableStatement(
					CallableStatement callable) throws SQLException,DataAccessException{

				ResultSet result = null;
				List<BillNotificationHistoryRecord> list= new ArrayList<BillNotificationHistoryRecord>();
				try{

					callable.setInt(1, ban);
					callable.registerOutParameter(2, ORACLE_REF_CURSOR);
					callable.execute();
					result = (ResultSet) callable.getObject(2);

					while (result.next()) {
						BillNotificationHistoryRecordInfo billNotificationHistoryRecordInfo = new BillNotificationHistoryRecordInfo();
						billNotificationHistoryRecordInfo.setSrcReferenceId(result.getString(1));
						billNotificationHistoryRecordInfo.setMostRecentInd(result.getString(2).equalsIgnoreCase("Y")?true:false);
						billNotificationHistoryRecordInfo.setEmailAddress(result.getString(3));
						billNotificationHistoryRecordInfo.setEffectiveStartDate(result.getDate(4));
						billNotificationHistoryRecordInfo.setEffectiveEndDate(result.getDate(5));
						billNotificationHistoryRecordInfo.setActivityType(result.getString(6));
						billNotificationHistoryRecordInfo.setActivityTypeFr(result.getString(7));
						billNotificationHistoryRecordInfo.setActivityReason(result.getString(8));
						billNotificationHistoryRecordInfo.setActivityReasonFr(result.getString(9));
						billNotificationHistoryRecordInfo.setActionType(result.getString(10));
						billNotificationHistoryRecordInfo.setActionTypeFr(result.getString(11));
						list.add((BillNotificationHistoryRecord)billNotificationHistoryRecordInfo);
					}
				}finally{
					if (result != null) {
						result.close();
					}
				}
				return list;
			}
		});	
	}

	@Override
	public void expireBillNotificationDetails(final int ban) {
		String callString="{call portal_notification_pkg.expireBillNotificationDetails(?)}";
		super.getCodsJdbcTemplate().execute(callString,new CallableStatementCallback<String>(){
			@Override
			public String doInCallableStatement(
					CallableStatement callable) throws SQLException,DataAccessException{

				callable.setInt(1, ban);
				callable.execute();
				return"";
			}
		});	
	}

	@Override
	public List<SubscriberInvoiceDetailInfo> retrieveSubscriberInvoiceDetails(final int banId, final int billSeqNo){

		String callString=("{ call history_utility_pkg.retrieve_sub_charge (?,?,?) }");

		return super.getKnowbilityJdbcTemplate().execute(callString,new CallableStatementCallback<List<SubscriberInvoiceDetailInfo>>(){
			@Override
			public List<SubscriberInvoiceDetailInfo> doInCallableStatement(
					CallableStatement callable) throws SQLException,DataAccessException{

				ResultSet results = null;
				SubscriberInvoiceDetailInfo info = null;
				List<SubscriberInvoiceDetailInfo> invoiceList = new ArrayList<SubscriberInvoiceDetailInfo>();

				try {
					callable.setInt(1, banId);
					callable.setInt(2, billSeqNo);
					callable.registerOutParameter(3,  ORACLE_REF_CURSOR);

					callable.execute();

					results = (ResultSet)callable.getObject(3);

					while (results.next()) {
						info = new SubscriberInvoiceDetailInfo();
						info.setProductType(results.getString(1));
						info.setSubscriberId(results.getString(2));
						info.setPricePlan(results.getString(3));
						info.setBilledName(results.getString(4));
						info.setCurrentCharges(results.getDouble(5));
						info.setCurrentCredits(results.getDouble(6));
						info.setGSTTaxAmount(results.getDouble(7));
						info.setPSTTaxAmount(results.getDouble(8));
						info.setHSTTaxAmount(results.getDouble(9));
						info.setRoamingTaxAmount(results.getDouble(10));
						info.setTotalCharge(results.getDouble(11));

						invoiceList.add(info);
					}

				}finally {
					if (results != null ) {
						results.close();
					}
				}
				return invoiceList; 
			}
		});	
	}
	@Override
	public List<ChargeInfo> retrieveBilledCharges(final int ban, final int pBillSeqNo,
			final String pPhoneNumber, final Date from, final Date to) {

		String call = "{? = call HISTORY_UTILITY_PKG.GetBilledCharges(?, ?, ?, ?, ?, ?, ?)}";

		return super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<List<ChargeInfo>>() {

			@Override
			public List<ChargeInfo> doInCallableStatement(CallableStatement callable)
			throws SQLException, DataAccessException {
				List<ChargeInfo> list = null;
				ResultSet result = null;

				java.sql.Date fromDate = null;
				java.sql.Date toDate = null;
				if (from != null && to != null) {
					fromDate = new java.sql.Date(from.getTime());
					toDate = new java.sql.Date(to.getTime());
				}
				try{
					list = new ArrayList<ChargeInfo>();
					callable.registerOutParameter(1, OracleTypes.NUMBER);
					callable.setInt(2, ban);
					callable.setInt(3, pBillSeqNo);
					callable.setString(4, pPhoneNumber);
					callable.setDate(5, fromDate);
					callable.setDate(6, toDate);
					callable.registerOutParameter(7, OracleTypes.CURSOR);
					callable.registerOutParameter(8, OracleTypes.VARCHAR);

					callable.execute();
					boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;

					if (success) {
						result = (ResultSet) callable.getObject(7);
						while (result.next()) {
							ChargeInfo info = new ChargeInfo();
							info.setBan(ban);
							info.setId(result.getDouble("ent_seq_no"));
							info.setCreationDate(result.getDate("chg_creation_date"));
							info.setEffectiveDate(result.getDate("effective_date"));
							info.setChargeCode(result.getString("actv_code"));
							info.setReasonCode(result.getString("actv_reason_code"));
							info.setFeatureCode(result.getString("feature_code"));
							info.setFeatureRevenueCode(result.getString("ftr_revenue_code"));
							info.setBalanceImpactFlag(result.getString("balance_impact_code"));
							info.setSubscriberId(result.getString("subscriber_no"));
							info.setProductType(result.getString("product_type"));
							info.setOperatorId(result.getInt("operator_id"));
							info.setAmount(result.getDouble("actv_amt"));
							info.setGSTAmount(result.getDouble("tax_gst_amt"));
							info.setPSTAmount(result.getDouble("tax_pst_amt"));
							info.setHSTAmount(result.getDouble("tax_hst_amt"));
							info.setApprovalStatus("Y");
							info.setBalanceIgnoreFlag(result.getString("bl_ignore_ind"));
							info.setServiceCode(result.getString("soc"));
							info.setBilled(true);
							info.setBillSequenceNo(pBillSeqNo);
							info.setPeriodCoverageStartDate(result.getDate("priod_cvrg_st_date"));
							info.setPeriodCoverageEndDate(result.getDate("priod_cvrg_nd_date"));
							info.setRoamingTaxAmount(result.getDouble("tax_roaming_amt"));
							info.setGSTExempt(result.getString("tax_gst_exmp_src")!=null?(result.getString("tax_gst_exmp_src").equals("T")):false);
							info.setPSTExempt(result.getString("tax_pst_exmp_src")!=null?(result.getString("tax_pst_exmp_src").equals("T")):false);
							info.setHSTExempt(result.getString("tax_hst_exmp_src")!=null?(result.getString("tax_hst_exmp_src").equals("T")):false);
							info.setRoamingTaxExempt(result.getString("tax_roam_exmp_src")!=null?(result.getString("tax_roam_exmp_src").equals("T")):false);

							list.add(info);
						}
					}
				}finally{
					if (result != null ) {
						result.close();
					}
				}

				return list;
			}
		});

	}
	@Override
	public boolean hasEPostSubscription(int ban){
		String queryStr = " select count(*)" +
		" from client_account_activity caa, client_account ca " +
		" where ca.client_account_id = caa.client_account_id " +
		" and ca.ban = '" +ban+
		"' and caa.activity_type_id = " + BillNotificationHistoryRecord.ACTIVITY_TYPE_EPOST +
		" and caa.activity_action_id = " + BillNotificationHistoryRecord.ACTIVITY_ACTION_REGISTRATION +
		" and caa.activity_reason_id = " + BillNotificationHistoryRecord.ACTIVITY_REASON_CUSTOMER_REQ +
		" and most_recent_ind = 'Y'"+
		" and caa.effective_end_ts = TO_TIMESTAMP ('9999-12-31 00:00:00', 'YYYY-MM-DD HH24:MI:SS')" ;
		int result=0;
		try{
			result= super.getCodsJdbcTemplate().queryForInt(queryStr);
		}catch(EmptyResultDataAccessException e){
			return false;
		}
		if (result>0)
			return true;
		return false;
	}

	@Override
	public InvoicePropertiesInfo getInvoiceProperties(final int ban) {
		String sql= "SELECT inv_suppression_ind, bl_man_hndl_req_opid, bl_man_hndl_eff_date, bl_man_hndl_exp_date " +
		"FROM BILLING_ACCOUNT WHERE ban =?";
		return super.getKnowbilityJdbcTemplate().query(sql, new Object[]{ban}, new ResultSetExtractor<InvoicePropertiesInfo>(){

			@Override
			public InvoicePropertiesInfo extractData(ResultSet result)
			throws SQLException {
				InvoicePropertiesInfo invoicePropertiesInfo = null;
				if (result.next()) {	
					invoicePropertiesInfo = new InvoicePropertiesInfo();
					invoicePropertiesInfo.setBan(ban);
					invoicePropertiesInfo.setHoldRedirectDestinationCode(String.valueOf(result.getInt("bl_man_hndl_req_opid")));
					invoicePropertiesInfo.setHoldRedirectFromDate(result.getTimestamp("bl_man_hndl_eff_date"));
					invoicePropertiesInfo.setHoldRedirectToDate(result.getTimestamp("bl_man_hndl_exp_date"));
					invoicePropertiesInfo.setInvoiceSuppressionLevel(result.getString("inv_suppression_ind"));
				}
				return invoicePropertiesInfo;
			}

		});
	}


	@Override
	public List<InvoiceHistoryInfo> retrieveInvoiceHistory(final int ban,final Date fromDate, final Date toDate) {
		
		if(AppConfiguration.isWRPPh3GetInvoiceHistoryRollback()){
			
			String call = "{? = call HISTORY_UTILITY_PKG.GetInvoiceHistory(?, ?, ?, ?, ?)} ";
			return super.getKnowbilityJdbcTemplate().execute(call, new CallableStatementCallback<List<InvoiceHistoryInfo>>() {
	
				@Override
				public List<InvoiceHistoryInfo> doInCallableStatement(CallableStatement callable)
				throws SQLException, DataAccessException {
					List<InvoiceHistoryInfo> list = new ArrayList<InvoiceHistoryInfo>();
					ResultSet result = null;
					try{
						callable.registerOutParameter(1, OracleTypes.NUMBER);
						callable.setInt(2, ban);
						callable.setString(3, dateFormat.format(fromDate));
						callable.setString(4, dateFormat.format(toDate));
						callable.registerOutParameter(5, OracleTypes.CURSOR);
						callable.registerOutParameter(6, OracleTypes.VARCHAR);
						callable.execute();
	
						boolean success = callable.getInt(1) == AccountManager.NUMERIC_TRUE;
	
						if (success) {
							result = (ResultSet) callable.getObject(5);
	
							while (result.next()) {
								InvoiceHistoryInfo invoiceHistoryInfo = new InvoiceHistoryInfo();
								invoiceHistoryInfo.setDate(result.getDate(1));
								invoiceHistoryInfo.setDueDate(result.getDate(2));
								invoiceHistoryInfo.setMailedIndicator(result.getString(3) == null);
								invoiceHistoryInfo.setPreviousBalance(result.getDouble(4));
								invoiceHistoryInfo.setInvoiceAmount(result.getDouble(5));
								invoiceHistoryInfo.setAmountDue(result.getDouble(6));
								invoiceHistoryInfo.setPaymentReceivedAmount(result.getDouble(7));
								invoiceHistoryInfo.setAdjustmentAmount(result.getDouble(8));
								invoiceHistoryInfo.setPastDue(result.getDouble(9));
								invoiceHistoryInfo.setLatePaymentCharge(result.getDouble(10));
								invoiceHistoryInfo.setCurrentCharges(result.getDouble(11));
								invoiceHistoryInfo.setTotalTax(result.getDouble(12));
								invoiceHistoryInfo.setBillSeqNo(result.getInt(13));
								invoiceHistoryInfo.setCycleRunYear(result.getInt(14));
								invoiceHistoryInfo.setCycleRunMonth(result.getInt(15));
								invoiceHistoryInfo.setCycleCode(result.getInt(16));
								invoiceHistoryInfo.setStatus(result.getString(17));
								invoiceHistoryInfo.setHomeCallCount(result.getInt(18));
								invoiceHistoryInfo.setRoamingCallCount(result.getInt(19));
								invoiceHistoryInfo.setHomeCallMinutes(result.getDouble(20));
								invoiceHistoryInfo.setRoamingCallMinutes(result.getDouble(21));
								invoiceHistoryInfo.setMonthlyRecurringCharge(result.getDouble(22));
								invoiceHistoryInfo.setLocalCallingCharges(result.getDouble(23));
								invoiceHistoryInfo.setOtherCharges(result.getDouble(24));
								invoiceHistoryInfo.setZoneUsageCharges(result.getDouble(25));
								invoiceHistoryInfo.setEHAUsageCharges(result.getDouble(26));
								invoiceHistoryInfo.setBanId(ban);
	
								list.add(invoiceHistoryInfo);
							}
						}
					}finally{
						if (result != null ) {
							result.close();
						}
					}
	
					return list;
				}
	
			});
			
		}else{
			
			if (ban > 0 && fromDate != null && toDate != null) {
				
				String startDate = dateFormat.format(fromDate);
				String endDate = dateFormat.format(toDate);
				
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT   cycle_close_date, bill_due_date, bill_delivery_ind, ");
				sql.append("         prev_balance_amt, total_due_amt, actual_balance_amt, ");
				sql.append("         pym_received_amt, ");
				sql.append("         NVL (total_billed_adjust, 0)+ NVL (curr_credit_amt, 0), ");
				sql.append("         past_due_amt, late_pym_chrg_amt, curr_charge_amt, ");
				sql.append("         NVL (tax_gst_amt, 0)+ NVL (tax_pst_amt, 0)+ NVL (tax_hst_amt, 0) + NVL (tax_rm_amt, 0),  ");
				sql.append("         bill_seq_no, cycle_run_year, cycle_run_month, ");
				sql.append("         cycle_code, bill_conf_status, ctns_num_home_calls, ");
				sql.append("         ctns_num_rm_calls, ctns_num_home_mins, ");
				sql.append("         ctns_num_rm_mins, curr_rc_chrg_amt, ");
				sql.append("         NVL (local_toll_amt, 0)+ NVL (local_at_amt, 0)+ NVL (local_ac_amt, 0), ");
				sql.append("         curr_oc_chrg_amt, ");
				sql.append("         NVL (zone_toll_amt, 0)+ NVL (zone_at_amt, 0)+ NVL (zone_ac_amt, 0), ");
				sql.append("         NVL (eha_toll_amt, 0)+ NVL (eha_at_amt, 0)+ NVL (eha_ac_amt, 0) ");
				sql.append("    FROM bill ");
				sql.append("   WHERE ban = :ban ");
				sql.append("     AND cycle_close_date BETWEEN TO_DATE ('"+startDate+"','mm/dd/yyyy') AND TO_DATE ('"+endDate+"','mm/dd/yyyy')");
				sql.append(" ORDER BY cycle_run_year DESC, cycle_run_month DESC");
				
				MapSqlParameterSource namedParameters = new MapSqlParameterSource();
				namedParameters.addValue("ban", ban);
				
				LOGGER.debug("retrieveInvoiceHistory (BAN:"+ban+") SQL:"+sql.toString());
				
				return (List<InvoiceHistoryInfo>) super.getKnowbilityNamedParameterJdbcTemplate().query(sql.toString(), namedParameters, new ResultSetExtractor<Collection<InvoiceHistoryInfo>>() {
	
					@Override
					public Collection<InvoiceHistoryInfo> extractData(ResultSet result)	throws SQLException, DataAccessException {
						
						Collection<InvoiceHistoryInfo> list = new ArrayList<InvoiceHistoryInfo>();
						try{		
							if(result.isBeforeFirst()) {
								while (result.next()) {
									InvoiceHistoryInfo invoiceHistoryInfo = new InvoiceHistoryInfo();
									invoiceHistoryInfo.setDate(result.getDate(1));
									invoiceHistoryInfo.setDueDate(result.getDate(2));
									invoiceHistoryInfo.setMailedIndicator(result.getString(3) == null);
									invoiceHistoryInfo.setPreviousBalance(result.getDouble(4));
									invoiceHistoryInfo.setInvoiceAmount(result.getDouble(5));
									invoiceHistoryInfo.setAmountDue(result.getDouble(6));
									invoiceHistoryInfo.setPaymentReceivedAmount(result.getDouble(7));
									invoiceHistoryInfo.setAdjustmentAmount(result.getDouble(8));
									invoiceHistoryInfo.setPastDue(result.getDouble(9));
									invoiceHistoryInfo.setLatePaymentCharge(result.getDouble(10));
									invoiceHistoryInfo.setCurrentCharges(result.getDouble(11));
									invoiceHistoryInfo.setTotalTax(result.getDouble(12));
									invoiceHistoryInfo.setBillSeqNo(result.getInt(13));
									invoiceHistoryInfo.setCycleRunYear(result.getInt(14));
									invoiceHistoryInfo.setCycleRunMonth(result.getInt(15));
									invoiceHistoryInfo.setCycleCode(result.getInt(16));
									invoiceHistoryInfo.setStatus(result.getString(17));
									invoiceHistoryInfo.setHomeCallCount(result.getInt(18));
									invoiceHistoryInfo.setRoamingCallCount(result.getInt(19));
									invoiceHistoryInfo.setHomeCallMinutes(result.getDouble(20));
									invoiceHistoryInfo.setRoamingCallMinutes(result.getDouble(21));
									invoiceHistoryInfo.setMonthlyRecurringCharge(result.getDouble(22));
									invoiceHistoryInfo.setLocalCallingCharges(result.getDouble(23));
									invoiceHistoryInfo.setOtherCharges(result.getDouble(24));
									invoiceHistoryInfo.setZoneUsageCharges(result.getDouble(25));
									invoiceHistoryInfo.setEHAUsageCharges(result.getDouble(26));
									invoiceHistoryInfo.setBanId(ban);
		
									list.add(invoiceHistoryInfo);
								}
							} else {
								if (LOGGER.isDebugEnabled()) {
									LOGGER.debug("The supplied ban, startDate and endDate return no record for ban ["+ban+"]");
								}
								return new ArrayList<InvoiceHistoryInfo>();
							}								
							
						}finally{
							if (result != null ) {
								result.close();
							}
						}
		
						return list;
					}
					
				});
	
			} else {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Ban, startDate and endDate should not be null. ban="+ban+", startDate="+fromDate+", endDate="+toDate);
				}
				return new ArrayList<InvoiceHistoryInfo>();
			}
			
		}

	}

	@Override
	public List<BillNotificationContactInfo> retrieveBillNotificationContactsHasEPost(                                                                                                                                                                                                                                                                                                                                                                                                                                                    
			final int ban,final int clientID) {                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
		String call="{call portal_notification_pkg.getBillNotifiConDetForEPost(?,?) }";
		return super.getCodsJdbcTemplate().execute(call, new CallableStatementCallback<List<BillNotificationContactInfo>>() {                                                                                                                                                                                                                                                                                                                                                                                                       

			@Override                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       
			public List<BillNotificationContactInfo> doInCallableStatement(CallableStatement callable)                                                                                                                                                                                                                                                                                                                                                                                                                                
			throws SQLException, DataAccessException {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
				List<BillNotificationContactInfo> notificationContactInfoList = new ArrayList<BillNotificationContactInfo>();                                                                                                                                                                                                                                                                                                                                                                                                                 
				ResultSet result = null;   
				try{
					callable.setInt(1, ban);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
					callable.registerOutParameter(2, ORACLE_REF_CURSOR);                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
					callable.execute();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
					result = (ResultSet) callable.getObject(2);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
					while (result.next()) {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
						BillNotificationContactInfo notificationContactInfo = new BillNotificationContactInfo();
						
						notificationContactInfo.setNotificationAddress(result.getString(1));
						notificationContactInfo.setBillNotificationEnabled(result.getDate(2) != null && result.getDate(2).before(new Date()) ? false:true);
						notificationContactInfo.setBillNotificationRegistrationId(result.getString(3));
						notificationContactInfo.setPortalUserId(result.getInt(4));
						notificationContactInfo.setEMailValidationStatus(result.getString(5));
						notificationContactInfo.setContactType(BillNotificationContact.CONTACT_TYPE_EMAIL);
						notificationContactInfo.setBillNotificationType(BillNotificationContact.NOTIFICATION_TYPE_EPOST);
						notificationContactInfo.setClientAccountId(clientID);                                                                                                                                                                                                                                                                                                                                                                                                                       

						notificationContactInfoList.add(notificationContactInfo);                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
					}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
				}finally{
					if (result != null ) {
						result.close();
					}
				}
				return notificationContactInfoList;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
			}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               

		});                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
	} 
	@Override
	public List<BillNotificationContactInfo> retrieveBillNotificationContactsHasEPostFalse(                                                                                                                                                                                                                                                                                                                                                                                                                                                    
			final int ban,final int clientID) {                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
		 String call="{call portal_notification_pkg.getBillNotificationContacts (?,?) } ";
		return super.getCodsJdbcTemplate().execute(call, new CallableStatementCallback<List<BillNotificationContactInfo>>() {                                                                                                                                                                                                                                                                                                                                                                                                       

			@Override                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       
			public List<BillNotificationContactInfo> doInCallableStatement(CallableStatement callable)                                                                                                                                                                                                                                                                                                                                                                                                                                
			throws SQLException, DataAccessException {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
				List<BillNotificationContactInfo> notificationContactInfoList = new ArrayList<BillNotificationContactInfo>();                                                                                                                                                                                                                                                                                                                                                                                                                 
				ResultSet result = null;   
				try{
					callable.setInt(1, ban);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
					callable.registerOutParameter(2, ORACLE_REF_CURSOR);                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
					callable.execute();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
					result = (ResultSet) callable.getObject(2);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
					while (result.next()) {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
						BillNotificationContactInfo notificationContactInfo = new BillNotificationContactInfo();
						notificationContactInfo.setContactType(result.getInt("contact_mechanism_type_id") == CONTACT_TYPE_EMAIL ? BillNotificationContact.CONTACT_TYPE_EMAIL
								: BillNotificationContact.CONTACT_TYPE_SMS);
						notificationContactInfo.setNotificationAddress(result.getString("contact_address_txt"));
						notificationContactInfo.setEMailValidationStatus(result.getString("contact_status_cd"));
						notificationContactInfo.setClientAccountId(clientID);
						notificationContactInfo.setBillNotificationType(BillNotificationContact.NOTIFICATION_TYPE_EBILL);                                                                                                                                                                                                                                                                                                                                                                                                                         

						notificationContactInfoList.add(notificationContactInfo);                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
					}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
				}finally{
					if (result != null) {
						result.close();
					}
				}
				return notificationContactInfoList;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
			}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
		});                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
	}  

}
