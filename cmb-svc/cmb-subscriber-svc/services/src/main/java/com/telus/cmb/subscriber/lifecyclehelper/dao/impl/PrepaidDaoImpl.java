package com.telus.cmb.subscriber.lifecyclehelper.dao.impl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;

import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.reference.FundSource;
import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.cmb.subscriber.lifecyclehelper.dao.PrepaidDao;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.subscriber.info.PrepaidCallHistoryInfo;
import com.telus.eas.subscriber.info.PrepaidEventHistoryInfo;
import com.telus.eas.subscriber.info.PrepaidPromotionDetailInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.ServiceFeatureInfo;
import com.telus.eas.utility.info.PrepaidEventTypeInfo;

public class PrepaidDaoImpl extends MultipleJdbcDaoTemplateSupport implements PrepaidDao {

	public static SimpleDateFormat dateFormat   =  new SimpleDateFormat("MM/dd/yyyy");

	@Override
	public List<PrepaidCallHistoryInfo> retrievePrepaidCallHistory(
			final String phoneNumber, final Date from, final Date to) {
		String callString="{call prepaid_transaction_pkg.getPrepaidCallHistory (?,?,?,?)}";
		return super.getEcpcsJdbcTemplate().execute(callString,new CallableStatementCallback<List<PrepaidCallHistoryInfo>>(){
			@Override
			public List<PrepaidCallHistoryInfo> doInCallableStatement(
					CallableStatement callable) throws SQLException,DataAccessException{
				
			    PrepaidCallHistoryInfo prepaidCallHistoryInfo = null;
			    List<PrepaidCallHistoryInfo> list = new ArrayList<PrepaidCallHistoryInfo>();
			    String fromDate = dateFormat.format(from) ;
			    String toDate =   dateFormat.format(to) ;
				ResultSet rset = null;
				try{
		        callable.setString(1, phoneNumber);
		        callable.setString(2, fromDate);
		        callable.setString(3, toDate);
		        callable.registerOutParameter(4, ORACLE_REF_CURSOR);
		        callable.execute();

		        rset = (ResultSet) callable.getObject(4);
		        
		        while (rset.next()) {
		            prepaidCallHistoryInfo = new PrepaidCallHistoryInfo();
		            prepaidCallHistoryInfo.setStartDate(rset.getTimestamp(1));
		            prepaidCallHistoryInfo.setEndDate(rset.getTimestamp(2));
		            prepaidCallHistoryInfo.setLocalLongDistanceIndicator(rset.getInt(3));
		            prepaidCallHistoryInfo.setInternationalDomesticIndicator(rset.getInt(4));
		            prepaidCallHistoryInfo.setOrigin_cd(rset.getString(5));
		            prepaidCallHistoryInfo.setChargeDuration(rset.getInt(6));
		            prepaidCallHistoryInfo.setCallingPhoneNumber(rset.getString(7));
		            prepaidCallHistoryInfo.setCalledPhoneNumber(rset.getString(8));
		            prepaidCallHistoryInfo.setChargedAmount(rset.getDouble(9));
		            prepaidCallHistoryInfo.setStartBalance(rset.getDouble(10));
		            prepaidCallHistoryInfo.setEndBalance(rset.getDouble(11));
		            prepaidCallHistoryInfo.setVoiceMailIndicator(rset.getString(12));
		            prepaidCallHistoryInfo.setRateId(rset.getString(13));
		            prepaidCallHistoryInfo.setServingSID(rset.getString(14));
		            prepaidCallHistoryInfo.setReasonTypeId(rset.getInt(15));
		            prepaidCallHistoryInfo.setReasonId(rset.getString(16));
		            prepaidCallHistoryInfo.setWPSServiceCode(rset.getString(17));
		            prepaidCallHistoryInfo.setLongDistanceCallCost(rset.getDouble(18));
		            prepaidCallHistoryInfo.setLocalCallCost(rset.getDouble(19));
		            
		            prepaidCallHistoryInfo.setRoamingCallCost(rset.getDouble(20));
		            prepaidCallHistoryInfo.setSwitchId(rset.getString(21));
		            prepaidCallHistoryInfo.setRouteId(rset.getString(22));
		            prepaidCallHistoryInfo.setCalledMarketCode(rset.getString(23));
		            prepaidCallHistoryInfo.setCallingMarketCode(rset.getString(24));
		            /* Commented the Prepaid 5.1 rel changes
		            String [] discountIds = {rset.getString(23),rset.getString(24),rset.getString(25)};
		            prepaidCallHistoryInfo.setDiscountIds(discountIds);
		            */
		            list.add(prepaidCallHistoryInfo);
		          }

				}finally{
					if(rset != null)
						rset.close();
				}
				return list;
			}
		});	
	}

	@Override
	public List<PrepaidEventHistoryInfo> retrievePrepaidEventHistory(
			final String phoneNumber, final Date from, final Date to) {
		String callString="{call prepaid_transaction_pkg.getPrepaidEventHistory (?,?,?,?)}";
		return super.getEcpcsJdbcTemplate().execute(callString,new CallableStatementCallback<List<PrepaidEventHistoryInfo>>(){
			@Override
			public List<PrepaidEventHistoryInfo> doInCallableStatement(
					CallableStatement callable) throws SQLException,DataAccessException{

				PrepaidEventHistoryInfo prepaidEventHistoryInfo = null;
			    List<PrepaidEventHistoryInfo> list = new ArrayList<PrepaidEventHistoryInfo>();
			    ResultSet rset = null;
			    String fromDate = dateFormat.format(from) ;
			    String toDate =   dateFormat.format(to) ;
			
				try{
			        callable.setString(1, phoneNumber);
			        callable.setString(2, fromDate);
			        callable.setString(3, toDate);
			        callable.registerOutParameter(4, ORACLE_REF_CURSOR);
			        callable.execute();

			        rset = (ResultSet) callable.getObject(4);
			        while (rset.next()) {
			            prepaidEventHistoryInfo = mapResultSetToPrepaidEventHistoryInfo(rset);
			            list.add(prepaidEventHistoryInfo);
			          }
				}finally{
					if(rset != null)
						rset.close();
				}
				return list;
			}
		});	
	}
	  private PrepaidEventHistoryInfo mapResultSetToPrepaidEventHistoryInfo(ResultSet rset) throws SQLException {
			PrepaidEventHistoryInfo prepaidEventHistoryInfo;
			prepaidEventHistoryInfo = new PrepaidEventHistoryInfo();
			prepaidEventHistoryInfo.setEventDate(rset.getTimestamp(1));
			prepaidEventHistoryInfo.setPrepaidEventTypeCode(Integer.toString(rset.getInt(2)));
			prepaidEventHistoryInfo.setDebitCreditFlag(rset.getString(3));
			prepaidEventHistoryInfo.setAmount(rset.getDouble(4));
			prepaidEventHistoryInfo.setCardId(rset.getString(5));
			prepaidEventHistoryInfo.setCreditCardNumber(rset.getString(6));
			prepaidEventHistoryInfo.setReferenceCode(rset.getString(7));
			prepaidEventHistoryInfo.setStartBalance(rset.getDouble(8));
			prepaidEventHistoryInfo.setEndBalance(rset.getDouble(9));
			prepaidEventHistoryInfo.setUserID(rset.getString(10));
			prepaidEventHistoryInfo.setSourceID(rset.getString(11));
			prepaidEventHistoryInfo.setTransactionID(rset.getString(12));
			prepaidEventHistoryInfo.setRelatedTransactionID(rset.getString(13));
			prepaidEventHistoryInfo.setPrepaidAdjustmentReasonCode(rset.getString(14));
			prepaidEventHistoryInfo.setOutstandingCharge(rset.getDouble(15));
			prepaidEventHistoryInfo.setTransactionDate(rset.getTimestamp(16));
			prepaidEventHistoryInfo.setConfiscatedBalance(rset.getDouble(17));
			prepaidEventHistoryInfo.setGMTOffset(rset.getString(18));
			prepaidEventHistoryInfo.setPreEventStatus(rset.getString(19));
			prepaidEventHistoryInfo.setPostEventStatus(rset.getString(20));
			prepaidEventHistoryInfo.setPreEventAmount(rset.getDouble(21));
			prepaidEventHistoryInfo.setPostEventAmount(rset.getDouble(22));
			prepaidEventHistoryInfo.setUnitType(rset.getString(23));
			// Commented the Prepaid 5.1 rel changes -- prepaidEventHistoryInfo.setDiscountPercentage(rset.getString(24));
			return prepaidEventHistoryInfo;
		  }


	@Override
	public List<PrepaidEventHistoryInfo> retrievePrepaidEventHistory(
			final String phoneNumber, final Date from, final Date to,
			final PrepaidEventTypeInfo[] prepaidEventTypes) {
		String callString="{call prepaid_transaction_pkg.getPrepaidEventHistory (?,?,?,?,?)}";
		return super.getEcpcsJdbcTemplate().execute(callString,new CallableStatementCallback<List<PrepaidEventHistoryInfo>>(){
			@Override
			public List<PrepaidEventHistoryInfo> doInCallableStatement(
					CallableStatement callable) throws SQLException,DataAccessException{

				PrepaidEventHistoryInfo prepaidEventHistoryInfo = null;
			    List<PrepaidEventHistoryInfo> list = new ArrayList<PrepaidEventHistoryInfo>();
			    ResultSet rset = null;
			    String fromDate = dateFormat.format(from) ;
			    String toDate =   dateFormat.format(to) ;
			
				try{
					int[] eventIDs = new int[prepaidEventTypes.length];
					if ( (prepaidEventTypes != null) && (prepaidEventTypes.length != 0)) {
						for (int i = 0; i < prepaidEventTypes.length; i++) {
							eventIDs[i] = Integer.parseInt(prepaidEventTypes[i].getCode());
						}
					}
			          ArrayDescriptor eventIdArrayDesc = ArrayDescriptor.createDescriptor("PDRADM.T_EVENT_TYPE_ARRAY", callable.getConnection());

			          // create Oracle array of event IDs
			          ARRAY eventIdsArray = new ARRAY(eventIdArrayDesc, callable.getConnection(), eventIDs);
			          callable.setString(1, phoneNumber);
			          callable.setString(2, fromDate);
			          callable.setString(3, toDate);
			          callable.setArray(4, eventIdsArray);
			          callable.registerOutParameter(5, ORACLE_REF_CURSOR);
			          callable.execute();

			          rset = (ResultSet) callable.getObject(5);
			        while (rset.next()) {
			            prepaidEventHistoryInfo = mapResultSetToPrepaidEventHistoryInfo(rset);
			            list.add(prepaidEventHistoryInfo);
			          }
				}finally{
					if(rset != null)
						rset.close();
				}
				return list;
			}
		});
	}

}
