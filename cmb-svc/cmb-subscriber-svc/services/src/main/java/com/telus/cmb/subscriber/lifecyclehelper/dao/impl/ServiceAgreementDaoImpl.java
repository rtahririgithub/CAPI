package com.telus.cmb.subscriber.lifecyclehelper.dao.impl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.telus.api.account.AccountManager;
import com.telus.cmb.common.dao.jdbctemplate.MultipleJdbcDaoTemplateSupport;
import com.telus.cmb.common.util.StringUtil;
import com.telus.cmb.subscriber.lifecyclehelper.dao.ServiceAgreementDao;
import com.telus.eas.subscriber.info.CallingCirclePhoneListInfo;
import com.telus.eas.subscriber.info.ContractChangeHistoryInfo;
import com.telus.eas.subscriber.info.FeatureParameterHistoryInfo;
import com.telus.eas.subscriber.info.PricePlanChangeHistoryInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.ServiceChangeHistoryInfo;
import com.telus.eas.subscriber.info.ServiceFeatureInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.VendorServiceChangeHistoryInfo;

public class ServiceAgreementDaoImpl extends MultipleJdbcDaoTemplateSupport implements ServiceAgreementDao {

	private static final Logger LOGGER = Logger.getLogger(ServiceAgreementDaoImpl.class);
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

	@Override
	public List<CallingCirclePhoneListInfo> retrieveCallingCirclePhoneNumberListHistory(
			final int banId, final String subscriberNo, final String productType, final Date from, final Date to) {


		String sql = "{call HISTORY_UTILITY_PKG.getCallingCirclePhoneHistory(?,?,?,?,?,?)}";

		return super.getKnowbilityJdbcTemplate().execute(sql, new CallableStatementCallback<List<CallingCirclePhoneListInfo>>() {

			@Override
			public List<CallingCirclePhoneListInfo> doInCallableStatement(CallableStatement callstmt)
			throws SQLException, DataAccessException {

				ResultSet result = null;
				List<CallingCirclePhoneListInfo> ccpliList = new ArrayList<CallingCirclePhoneListInfo>();
				try{
					
					String startDate = dateFormat.format(from);
					String endDate = dateFormat.format(to);
	
					callstmt.registerOutParameter(1, OracleTypes.CURSOR);
					callstmt.setInt(2, banId);
					callstmt.setString(3, subscriberNo);
					callstmt.setString(4, productType);
					callstmt.setString(5, startDate);
					callstmt.setString(6, endDate);
	
					callstmt.execute();
	
					result = (ResultSet) callstmt.getObject(1);
	
					while (result.next()) {
						CallingCirclePhoneListInfo info = new CallingCirclePhoneListInfo();
						info.setPhoneNumberList(StringUtil.stringTokensToArray(result.getString(1)));
						info.setEffectiveDate(result.getDate(2));
						info.setExpiryDate(result.getDate(3));
						ccpliList.add(info);
					}
				}finally{
					if(result!=null){
						result.close();
					}
				}
				return ccpliList;
			}
		});
	}

	@Override
	public List<ContractChangeHistoryInfo> retrieveContractChangeHistory(int ban,
			String subscriberID, Date from, Date to) {

		String fromDate = dateFormat.format(from) ;
		String toDate =   dateFormat.format(to) ;
		String sql =	" SELECT        effective_date, commit_start_date, commit_end_date " +
		",commit_reason_cd, substr(dealer_code, 1,10), commit_orig_no_month, operator_id, application_id, " +
		"substr(dealer_code, 11)" +
		" FROM     contract  " +
		" WHERE    ban = ?  " +
		" AND          subscriber_no = ?  " +
		" AND      trunc(effective_date) >= to_date(?,'mm/dd/yyyy') " +
		" AND          trunc(effective_date) <= to_date(?,'mm/dd/yyyy') " +
		" ORDER BY CNT_SEQ_NO ASC ";

		return super.getKnowbilityJdbcTemplate().query(
				sql,new Object[]{ban,subscriberID,fromDate,toDate}, 
				new RowMapper<ContractChangeHistoryInfo>(){

					@Override
					public ContractChangeHistoryInfo mapRow(ResultSet result,
							int rowNum) throws SQLException {

						ContractChangeHistoryInfo cchInfo = new ContractChangeHistoryInfo();
						cchInfo.setDate(result.getTimestamp(1));
						cchInfo.setNewCommitmentStartDate(result.getTimestamp(2));
						cchInfo.setNewCommitmentEndDate(result.getTimestamp(3));
						cchInfo.setReasonCode(result.getString(4));
						cchInfo.setDealerCode(result.getString(5)==null ? "" :result.getString(5));
						cchInfo.setNewCommitmentMonths(result.getInt(6));
						cchInfo.setKnowbilityOperatorID(result.getString(7)==null ? "" :result.getString(7));
						cchInfo.setApplicationID(result.getString(8)==null ? "" :result.getString(8));
						cchInfo.setSalesRepId(result.getString(9)==null ? "" :result.getString(9));
						return cchInfo;
					}
				});

	}

	@Override
	public List<FeatureParameterHistoryInfo> retrieveFeatureParameterHistory(
			final int banId, final String subscriberNo, final String productType,
			final String[] parameterNames, final Date from, final Date to) {
		
		String sql =  "{call HISTORY_UTILITY_PKG.getFtrParamByParamNames(?,?,?,?,?,?,?)}";

		return super.getKnowbilityJdbcTemplate().execute(sql, new CallableStatementCallback<List<FeatureParameterHistoryInfo>>() {

			@Override
			public List<FeatureParameterHistoryInfo> doInCallableStatement(CallableStatement callstmt)
			throws SQLException, DataAccessException {

				ResultSet result = null;
				List<FeatureParameterHistoryInfo> fphiList = new ArrayList<FeatureParameterHistoryInfo>();
				try{
					String startDate = dateFormat.format(from);
					String endDate = dateFormat.format(to);
	
					ArrayDescriptor subscriberIdArrayDesc = ArrayDescriptor.createDescriptor("T_PARAMETER_NAME_ARRAY", callstmt.getConnection());
					ARRAY nameArrays = new ARRAY(subscriberIdArrayDesc, callstmt.getConnection(), parameterNames);
	
					// set/register input/output parameters
					callstmt.registerOutParameter(1, OracleTypes.CURSOR);
					callstmt.setInt(2, banId);
					callstmt.setString(3, subscriberNo);
					callstmt.setString(4, productType);
					callstmt.setArray(5, nameArrays);
					callstmt.setString(6, startDate);
					callstmt.setString(7, endDate);
	
					callstmt.execute();
	
					result = (ResultSet) callstmt.getObject(1);
					while (result.next()) {
						FeatureParameterHistoryInfo info = mapToFeatureParameterHistoryInfo(result);
						fphiList.add(info);
					}
				
				}finally{
					if(result!=null){
						result.close();
					}
				}
				return fphiList;
			}
		});
	}

	@Override
	public List<String> retrieveMultiRingPhoneNumbers(final String subscriberId) {

		String sql = "{ call subscriber_pkg.retrieve_multiring (?,?) }";

		return super.getKnowbilityJdbcTemplate().execute(sql, new CallableStatementCallback<List<String>>() {

			@Override
			public List<String> doInCallableStatement(CallableStatement callstmt)
			throws SQLException, DataAccessException {
				ResultSet result = null;
				List<String> phoneNumbers = new ArrayList<String>();

				try{
					callstmt.setString(1, subscriberId);
					callstmt.registerOutParameter(2, OracleTypes.CURSOR);
					callstmt.execute();
					result = (ResultSet)callstmt.getObject(2);

					while (result.next()) {
						phoneNumbers.add(result.getString(1).trim());
					}
				}finally {
					if (result != null)
						result.close();
				}
				return phoneNumbers;
			}
		});
	}

	@Override
	public List<PricePlanChangeHistoryInfo> retrievePricePlanChangeHistory(int ban,
			String subscriberID, Date from, Date to) {

		String fromDate = dateFormat.format(from) ;
		String toDate =   dateFormat.format(to) ;
		String sql = "SELECT    effective_date, soc, expiration_date, operator_id, application_id, " +
		" substr(sa.dealer_code, 1,10), substr(sa.dealer_code, 11) " +
		" FROM    service_agreement sa " +
		" WHERE    ban = ?  " +
		" AND          subscriber_no = ?  " +
		" AND     ( trunc(effective_date) >= to_date(?,'mm/dd/yyyy') " +
		" AND          trunc(effective_date) <= to_date(?,'mm/dd/yyyy') " +
		" OR     trunc(expiration_date) >= to_date(?,'mm/dd/yyyy') " +
		" AND          trunc(expiration_date) <= to_date(?,'mm/dd/yyyy'))" +
		" AND      service_type = 'P'"  +
		" order by 1 desc " ;

		return super.getKnowbilityJdbcTemplate().query(
				sql,new Object[]{ban, subscriberID, fromDate, toDate,fromDate, toDate},	
				new RowMapper<PricePlanChangeHistoryInfo>(){

					@Override
					public PricePlanChangeHistoryInfo mapRow(ResultSet result,
							int rowNum) throws SQLException {

						PricePlanChangeHistoryInfo ppchiList=new PricePlanChangeHistoryInfo();
						ppchiList.setDate(result.getTimestamp(1));
						ppchiList.setNewPricePlanCode(result.getString(2));
						ppchiList.setNewExpirationDate(result.getTimestamp(3));
						ppchiList.setKnowbilityOperatorID(result.getString(4)==null ? "" :result.getString(4));
						ppchiList.setApplicationID(result.getString(5)==null ? "" :result.getString(5));
						ppchiList.setDealerCode(result.getString(6)==null ? "" :result.getString(6));
						ppchiList.setSalesRepId(result.getString(7)==null ? "" :result.getString( 7));
						return ppchiList;

					}
				});
	}

	@Override
	public SubscriberContractInfo retrieveServiceAgreementBySubscriberID(String subscriberID) {

		String sql = " select  sa.soc, sa.service_type , sa.effective_date,sa.expiration_date, sf.feature_code, sf.ftr_effective_date,sf.ftr_expiration_date, sf.ftr_add_sw_prm  "
				+ " ,sf.msisdn, substr(sa.dealer_code, 1,10), substr(sa.dealer_code, 11), sa.soc_seq_no, sa.soc_ver_no, sf.service_ftr_seq_no from subscriber s "
				+ " ,service_agreement sa, service_feature sf ,logical_date ld  where  s.subscriber_no = ? and  s.sub_status! ='C' and   sa.ban=s.customer_ban "
				+ " and     sa.subscriber_no=s.subscriber_no  and   sa.product_type=s.product_type and   sa.service_type  in ('P','O','T') "
				+ " and   (trunc(sa.expiration_date) > trunc(ld.logical_date) or sa.expiration_date is null)  and  ld.logical_date_type='O' and  sa.effective_date = "
				+ " (select min(sa1.effective_date) from service_agreement sa1  where sa1.ban=sa.ban   and   sa1.subscriber_no=sa.subscriber_no "
				+ " and     sa1.product_type=sa.product_type and   sa1.service_type='P' and   (trunc(sa1.expiration_date) > trunc(ld.logical_date) or sa1.expiration_date is null)  "
				+ " and   ld.logical_date_type='O'  ) and sf.ban=sa.ban and sf.subscriber_no=sa.subscriber_no  and sf.soc=sa.soc and sf.soc_seq_no=sa.soc_seq_no "
				+ " and   sf.ftr_soc_ver_no= sa.soc_ver_no and   (trunc(sf.ftr_expiration_date) > trunc(ld.logical_date) or sf.ftr_expiration_date is null)  union "
				+ " select  sa.soc, sa.service_type ,  sa.effective_date,sa.expiration_date, sf.feature_code,  sf.ftr_effective_date,sf.ftr_expiration_date,  sf.ftr_add_sw_prm   "
				+ " ,sf.msisdn, substr(sa.dealer_code, 1,10), substr(sa.dealer_code, 11) ,sa.soc_seq_no, sa.soc_ver_no ,sf.service_ftr_seq_no  from  subscriber s "
				+ " ,service_agreement sa, service_feature sf, logical_date ld where  s.subscriber_no = ? and  s.sub_status! ='C'  and sa.ban=s.customer_ban "
				+ " and     sa.subscriber_no=s.subscriber_no  and  sa.product_type=s.product_type and sa.service_type in ('O','T') "
				+ " and   (trunc(sa.expiration_date) > trunc(ld.logical_date) or sa.expiration_date is null) and   ld.logical_date_type='O' "
				+ " and    trunc(sa.effective_date) <= trunc(ld.logical_date) and sf.ban=sa.ban and sf.subscriber_no=sa.subscriber_no  and sf.soc=sa.soc "
				+ " and   sf.soc_seq_no=sa.soc_seq_no and sf.ftr_soc_ver_no= sa.soc_ver_no "
				+ " and   (trunc(sf.ftr_expiration_date) > trunc(ld.logical_date) or sf.ftr_expiration_date is null) union select  sa.soc, sa.service_type , "
				+ " sa.effective_date,sa.expiration_date, sf.feature_code,  sf.ftr_effective_date,sf.ftr_expiration_date, sf.ftr_add_sw_prm   "
				+ " ,sf.msisdn, substr(sa.dealer_code, 1,10), substr(sa.dealer_code, 11) ,sa.soc_seq_no, sa.soc_ver_no ,sf.service_ftr_seq_no  from  subscriber s "
				+ " ,service_agreement sa "
				+ " ,service_feature   sf "
				+ " ,logical_date ld "
				+ " where   s.subscriber_no = ? "
				+ " and     s.sub_status! ='C' "
				+ " and     sa.ban=s.customer_ban "
				+ " and     sa.subscriber_no=s.subscriber_no "
				+ " and     sa.product_type=s.product_type"
				+ " and     sa.service_type not in ('O','P','T') "
				+ " and   (trunc(sa.expiration_date) > trunc(ld.logical_date) or sa.expiration_date is null) "
				+ " and   ld.logical_date_type='O' "
				+ " and sf.ban=sa.ban and sf.subscriber_no=sa.subscriber_no and sf.soc=sa.soc and sf.soc_seq_no=sa.soc_seq_no and sf.ftr_soc_ver_no= sa.soc_ver_no "
				+ " and   (trunc(sf.ftr_expiration_date) > trunc(ld.logical_date) or sf.ftr_expiration_date is null) order by 2, 1, 3, 5";

		return super.getKnowbilityJdbcTemplate().query(sql, new Object[] { subscriberID, subscriberID, subscriberID }, new ResultSetExtractor<SubscriberContractInfo>() {

			@Override
			public SubscriberContractInfo extractData(ResultSet result) throws SQLException, DataAccessException {
				SubscriberContractInfo subscriberContractInfo = new SubscriberContractInfo();
				ServiceAgreementInfo serviceAgreementSoc = null;
				ServiceFeatureInfo serviceFeature = null;
				HashMap<String, ServiceFeatureInfo> pricePlanFeatures = new HashMap<String, ServiceFeatureInfo>();
				HashMap<String, ServiceAgreementInfo> pricePlanServices = new HashMap<String, ServiceAgreementInfo>();
				HashMap<String, ServiceFeatureInfo> serviceFeatures = new HashMap<String, ServiceFeatureInfo>();
				String socCode = null;

				while (result.next()) {
					if (result.getString(2).equals("P")) {
						// Price Plan
						subscriberContractInfo.setPricePlan(result.getString(1));
						subscriberContractInfo.setPricePlanServiceType(result.getString(2));
						subscriberContractInfo.setEffectiveDate(result.getTimestamp(3));
						subscriberContractInfo.setExpiryDate(result.getTimestamp(4));
						subscriberContractInfo.setPricePlanDealerCode(result.getString(10));
						subscriberContractInfo.setPricePlanSalesRepId(result.getString(11));
						subscriberContractInfo.setServiceSequenceNo(result.getString("soc_seq_no"));
						subscriberContractInfo.setServiceVersionNo(result.getString("soc_ver_no"));
						subscriberContractInfo.setTransaction(SubscriberContractInfo.NO_CHG);

						// get and set featues
						serviceFeature = new ServiceFeatureInfo();
						serviceFeature.setFeatureCode(result.getString(5));
						serviceFeature.setEffectiveDate(result.getTimestamp(6));
						serviceFeature.setExpiryDate(result.getTimestamp(7));
						serviceFeature.setParameter(StringUtils.trimToEmpty(result.getString(8)));
						serviceFeature.setAdditionalNumber(result.getString(9));
						serviceFeature.setTransaction(ServiceFeatureInfo.NO_CHG);
						serviceFeature.setServiceSequenceNo(result.getString("soc_seq_no"));
						serviceFeature.setServiceVersionNo(result.getString("soc_ver_no"));
						serviceFeature.setFeatureSequenceNo(result.getString("service_ftr_seq_no"));
						pricePlanFeatures.put(serviceFeature.getFeatureCode(), serviceFeature);

					} else {
						// save existing SA, if doesn't yet exist, or if already has a feature.
						// These conditions indicate that a new Agreement has started.
						if (socCode == null || !socCode.equals(result.getString(1))) {
							// must be socs
							if (serviceFeatures.size() > 0) {
								serviceAgreementSoc.setFeatures(serviceFeatures);
								pricePlanServices.put(serviceAgreementSoc.getServiceMappingCode(), serviceAgreementSoc);
								serviceFeatures = new HashMap<String, ServiceFeatureInfo>();
							}
							serviceAgreementSoc = new ServiceAgreementInfo();
							serviceAgreementSoc.setServiceCode(result.getString(1));
							serviceAgreementSoc.setServiceType(result.getString(2));
							serviceAgreementSoc.setEffectiveDate(result.getTimestamp(3));
							serviceAgreementSoc.setExpiryDate(result.getTimestamp(4));
							serviceAgreementSoc.setDealerCode(result.getString(10));
							serviceAgreementSoc.setSalesRepId(result.getString(11));
							serviceAgreementSoc.setTransaction(ServiceFeatureInfo.NO_CHG);
							serviceAgreementSoc.setServiceSequenceNo(result.getString("soc_seq_no"));
							serviceAgreementSoc.setServiceVersionNo(result.getString("soc_ver_no"));
							socCode = serviceAgreementSoc.getServiceCode();
						}

						// get soc features
						serviceFeature = new ServiceFeatureInfo();
						serviceFeature.setFeatureCode(result.getString(5));
						serviceFeature.setEffectiveDate(result.getTimestamp(6));
						serviceFeature.setExpiryDate(result.getTimestamp(7));
						serviceFeature.setParameter(StringUtils.trimToEmpty(result.getString(8)));
						serviceFeature.setAdditionalNumber(result.getString(9));
						serviceFeature.setTransaction(ServiceFeatureInfo.NO_CHG);
						serviceFeature.setServiceSequenceNo(result.getString("soc_seq_no"));
						serviceFeature.setServiceVersionNo(result.getString("soc_ver_no"));
						serviceFeature.setFeatureSequenceNo(result.getString("service_ftr_seq_no"));
						serviceFeatures.put(serviceFeature.getFeatureCode(), serviceFeature);

					}
				}
				if (serviceAgreementSoc != null) {
					serviceAgreementSoc.setFeatures(serviceFeatures);
					pricePlanServices.put(serviceAgreementSoc.getServiceMappingCode(), serviceAgreementSoc);
					subscriberContractInfo.setServices(pricePlanServices);
				}
				subscriberContractInfo.setFeatures(pricePlanFeatures);

				return subscriberContractInfo;
			}

		});
	}

	@Override
	public List<ServiceChangeHistoryInfo> retrieveServiceChangeHistory(int ban,
			String subscriberID, Date from, Date to, boolean includeAllServices) {

		String fromDate = dateFormat.format(from) ;
		String toDate =   dateFormat.format(to) ;
		String sql = "SELECT     effective_date, soc, expiration_date, operator_id, application_id, " +
		" substr(sa.dealer_code, 1,10), substr(sa.dealer_code, 11) " +
		" FROM    service_agreement sa " +
		" WHERE    ban = ?  " +
		" AND          subscriber_no = ?  " +
		" AND    ( trunc(effective_date) >= to_date(?,'mm/dd/yyyy') " +
		" AND          trunc(effective_date) <= to_date(?,'mm/dd/yyyy') " +
		" OR     trunc(expiration_date) >= to_date(?,'mm/dd/yyyy') " +
		" AND          trunc(expiration_date) <= to_date(?,'mm/dd/yyyy'))" +
		( includeAllServices ? " AND service_type <> 'P' " :  " AND service_type not in ('P','O','T')  " ) +
		" order by 1 desc " ;

		return super.getKnowbilityJdbcTemplate().query(
				sql,new Object[]{ban, subscriberID, fromDate, toDate,fromDate, toDate}, 
				new RowMapper<ServiceChangeHistoryInfo>(){

					@Override
					public ServiceChangeHistoryInfo mapRow(ResultSet result,
							int rowNum) throws SQLException {

						ServiceChangeHistoryInfo schi = new ServiceChangeHistoryInfo();
						schi.setDate(result.getTimestamp(1));
						schi.setServiceCode(result.getString(2));
						schi.setNewExpirationDate(result.getTimestamp(3));
						schi.setKnowbilityOperatorID(result.getString(4)==null ? "" :result.getString(4));
						schi.setApplicationID(result.getString(5)==null ? "" :result.getString(5));
						schi.setDealerCode(result.getString(6)==null ? "" :result.getString(6));
						schi.setSalesRepId(result.getString(7)==null ? "" :result.getString(7));
						return schi;

					}
				});

	}

	@Override
	public List<VendorServiceChangeHistoryInfo> retrieveVendorServiceChangeHistory(
			final int ban, final String subscriberId, final String[] categoryCodes) {

		String sql = "{ ? = call SUBSCRIBER_PKG.getpromohistory(?, ?, ?, ?, ?) }";

		return super.getKnowbilityJdbcTemplate().execute(sql, new CallableStatementCallback<List<VendorServiceChangeHistoryInfo>>() {

			@Override
			public List<VendorServiceChangeHistoryInfo> doInCallableStatement(CallableStatement callstmt)
			throws SQLException, DataAccessException {

				ResultSet result = null;
				List<VendorServiceChangeHistoryInfo> vschiList = new ArrayList<VendorServiceChangeHistoryInfo>();
				String vendorServiceCode = "^";
				VendorServiceChangeHistoryInfo vendorInfo = null;
				List<ServiceChangeHistoryInfo> serviceList=new ArrayList<ServiceChangeHistoryInfo>();

				try{

					// create array descriptor - note that vendor service IDs are category codes 
					ArrayDescriptor serviceArrayDesc = ArrayDescriptor.createDescriptor("T_CATEGORY_CODE_ARRAY", callstmt.getConnection());

					// create Oracle array of vendor category codes
					ARRAY serviceArray = new ARRAY(serviceArrayDesc, callstmt.getConnection(), categoryCodes);

					// set / register input / output parameters
					callstmt.registerOutParameter(1, OracleTypes.NUMBER);
					callstmt.setInt(2, ban);
					callstmt.setString(3, subscriberId);
					callstmt.setArray(4, serviceArray);
					callstmt.registerOutParameter(5, OracleTypes.CURSOR);
					callstmt.registerOutParameter(6, OracleTypes.VARCHAR);
					callstmt.execute();

					boolean success = callstmt.getInt(1) == AccountManager.NUMERIC_TRUE;	      
					if (success) {	    		
						result = (ResultSet)callstmt.getObject(5);
						while (result.next()) {

							// check if this is a different vendor service code from the previous iteration
							if (!vendorServiceCode.equals(result.getString(1))) {

								// add the last vendorInfo to the vendorServiceList before we switch to a new vendorInfo
								// and reset the serviceList (except for the first iteration)
								if (!vendorServiceCode.equals("^")) {							
									vendorInfo.setPromoSOCs((ServiceChangeHistoryInfo[])serviceList.toArray(
											new ServiceChangeHistoryInfo[serviceList.size()]));  			
									vschiList.add(vendorInfo);
									serviceList = new ArrayList<ServiceChangeHistoryInfo>();
								}

								// map the new vendor service info and service change history info 
								vendorInfo = new VendorServiceChangeHistoryInfo();
								vendorInfo.setVendorServiceCode(result.getString(1));	
								ServiceChangeHistoryInfo historyInfo = new ServiceChangeHistoryInfo();
								historyInfo.setServiceCode(result.getString(2));
								historyInfo.setDate(result.getTimestamp(3));	    			
								historyInfo.setNewExpirationDate(result.getTimestamp(4));
								historyInfo.setKnowbilityOperatorID(result.getString(5) == null ? "" : result.getString(5));
								historyInfo.setApplicationID(result.getString(6) == null ? "" : result.getString(6));
								historyInfo.setDealerCode(result.getString(7) == null ? "" : result.getString(7));
								historyInfo.setSalesRepId(result.getString(8) == null ? "" : result.getString(8));
								serviceList.add(historyInfo);

							} else {
								// we're still on the same vendor service code - only map the service change history info
								ServiceChangeHistoryInfo historyInfo = new ServiceChangeHistoryInfo();
								historyInfo.setServiceCode(result.getString(2));
								historyInfo.setDate(result.getTimestamp(3));	    			
								historyInfo.setNewExpirationDate(result.getTimestamp(4));
								historyInfo.setKnowbilityOperatorID(result.getString(5) == null ? "" : result.getString(5));
								historyInfo.setApplicationID(result.getString(6) == null ? "" : result.getString(6));
								historyInfo.setDealerCode(result.getString(7) == null ? "" : result.getString(7));
								historyInfo.setSalesRepId(result.getString(8) == null ? "" : result.getString(8));
								serviceList.add(historyInfo);
							}

							// set this prior to the next iteration through the result set
							vendorServiceCode = result.getString(1);
						}

						// add the very last vendorInfo in the result set to the vendorServiceList
						if (vendorInfo != null) { 
							vendorInfo.setPromoSOCs((ServiceChangeHistoryInfo[])serviceList.toArray(
									new ServiceChangeHistoryInfo[serviceList.size()]));  			
							vschiList.add(vendorInfo); 
						}

					} 

				}catch(Exception e){
					LOGGER.error("Error: ", e);
				}

				finally {
					if (result != null)
						result.close();
				}
				return vschiList;
			}
		});

	}

	@Override
	public List<String> retrieveVoiceMailFeatureByPhoneNumber(String phoneNumber,
			String productType) {

		String producttype = (productType==null ? "C" : productType.trim());
		String sql ="select  sa.soc,  sf.feature_code, sf.ftr_add_sw_prm " +
		" , sf.service_type" +
		"  from  subscriber s " +
		" ,service_agreement sa " +
		" ,service_feature   sf " +
		" ,feature f " +
		" ,logical_date ld " +
		" where   s.subscriber_no = ? " +
		" and     s.product_type= ? " +
		" and     s.sub_status! ='C' " +
		" and     sa.ban=s.customer_ban " +
		" and     sa.subscriber_no=s.subscriber_no " +
		" and     sa.product_type=s.product_type" +
		" and     (trunc(sa.expiration_date) > trunc(ld.logical_date) or sa.expiration_date is null) " +
		" and     ld.logical_date_type='O' " +
		" and     sa.effective_date <= trunc(ld.logical_date) " +
		" and     sf.ban=sa.ban " +
		" and     sf.subscriber_no=sa.subscriber_no " +
		" and     sf.product_type = ? " +
		" and     sf.soc=sa.soc " +
		" and     sf.soc_seq_no=sa.soc_seq_no " +
		" and     sf.ftr_soc_ver_no= sa.soc_ver_no " +
		" and     f.feature_code=sf.feature_code " +
		" and     f.switch_code='VM'  " +
		" order by decode (sf.service_type,'R',1,'S',1,'O',2,'P',2,3) " ;

		return super.getKnowbilityJdbcTemplate().query(
				sql,new Object[]{phoneNumber,producttype,producttype}, 
				new ResultSetExtractor<List<String>>(){

					@Override
					public List<String> extractData(ResultSet result) throws SQLException  {

						List<String> vmfString=new ArrayList<String>();
						if (result.next()){
							vmfString.add(result.getString(1));
							vmfString.add(result.getString(2));
							vmfString.add(result.getString(3));
						}
						return vmfString;

					}
				});

	}
	
	private FeatureParameterHistoryInfo mapToFeatureParameterHistoryInfo(ResultSet result) throws SQLException {
		FeatureParameterHistoryInfo info = new FeatureParameterHistoryInfo();
		info.setServiceCode(result.getString("soc"));
		info.setFeatureCode(result.getString("feature_code"));
		info.setParameterName( result.getString("parameter_name"));
		String value = result.getString("parameter_value");
		info.setParameterValue( (value!=null)? value.trim(): value );
		info.setCreationDate(result.getTimestamp("sys_creation_date"));
		info.setEffectiveDate(result.getDate("effective_date"));
		info.setExpirationDate(result.getDate("expiration_date"));
		info.setApplicationID( result.getString("application_id"));
		info.setKnowbilityOperatorID( result.getString("operator_ID") );
		info.setUpdateDate(result.getTimestamp( "sys_update_date"));
		
		//TODO, remove try.. catch after history_utility_pkg get deployed 
		try {
			info.setServiceSequenceNo(result.getString("soc_seq_no"));
			info.setServiceVersionNo(result.getString("FTR_SOC_VER_NO"));
			info.setFeatureSequenceNo(result.getString("SERVICE_FTR_SEQ_NO"));
		} catch (Exception e ) {
			System.err.println( e );
		}
		return info;
	}

	@Override
	public FeatureParameterHistoryInfo retrieveLastEffectiveFeatureParameter(
			int banId, String subscriberId, String productType,
			String serviceCode, String featureCode) {
		
		String sql = 
			" select * from("   +
			" SELECT sfp.soc, sfp.feature_code, sfp.parameter_name, sfp.parameter_value, sfp.sys_creation_date, sfp.effective_date, sfp.expiration_date, sfp.operator_id, sfp.application_id"   +
			" 	, sfp.sys_update_date ,sfp.SOC_SEQ_NO, sfp.FTR_SOC_VER_NO, sfp.SERVICE_FTR_SEQ_NO"   +
			"   FROM service_feature_parameters sfp"   +
			"  WHERE sfp.ban = ?"   +
			"    AND sfp.subscriber_no = ?"   +
			"    AND sfp.product_type = ?"   +
			"    AND sfp.soc=?"   +
			"    and sfp.feature_code=?"   +
			"    AND (sfp.expiration_date is null OR sfp.expiration_date >= sfp.effective_date)"   +
			"    ORDER BY sfp.effective_date desc, sfp.sys_creation_date desc"   +
			" ) "   +
			" where rownum<=1"  ;

		List<FeatureParameterHistoryInfo> result = super.getKnowbilityJdbcTemplate().query(
				sql,
				new Object[]{banId, subscriberId, productType, serviceCode, featureCode }, 
				new RowMapper<FeatureParameterHistoryInfo>(){

					@Override
					public FeatureParameterHistoryInfo mapRow(ResultSet result,	int rowNum) throws SQLException {

						return mapToFeatureParameterHistoryInfo( result );

					}
				});
		
		if ( result.isEmpty()==false ) {
			return result.get(0);
		}
		
		return null;
	}

	@Override
	public List<FeatureParameterHistoryInfo> retrieveCallingCircleParametersByDate(
			int banId, String subscriberId, String productType, Date fromDate ) {
		
		String sql = 
			" SELECT sfp.soc, sfp.feature_code, sfp.parameter_name, sfp.parameter_value, sfp.sys_creation_date, sfp.effective_date, sfp.expiration_date, sfp.operator_id, sfp.application_id, sfp.sys_update_date"   +
			"   ,sfp.SOC_SEQ_NO, sfp.FTR_SOC_VER_NO, sfp.SERVICE_FTR_SEQ_NO" + 
			"    FROM service_feature_parameters sfp"   +
			"   WHERE sfp.ban = ?"   +
			"     AND sfp.subscriber_no = ?"   +
			"     AND sfp.product_type = ?"   +
			"     AND sfp.parameter_name in ( 'CALLING-CIRCLE' )"   +
			"     AND (sfp.expiration_date is null "   +
			" 	  OR (sfp.expiration_date >=sfp.effective_date "   +
			" 	  and sfp.expiration_date>=trunc(?)"   +
			" 	  )"   +
			" 	  )"   +
			"     ORDER BY sfp.effective_date desc, sfp.expiration_date desc, sfp.sys_creation_date";
		List<FeatureParameterHistoryInfo> result = super.getKnowbilityJdbcTemplate().query(
				sql,
				new Object[]{banId, subscriberId, productType, fromDate }, 
				new RowMapper<FeatureParameterHistoryInfo>(){

					@Override
					public FeatureParameterHistoryInfo mapRow(ResultSet result,	int rowNum) throws SQLException {

						return mapToFeatureParameterHistoryInfo( result );

					}
				});
		return result;
	}
	
	@Override
	public List<String> retrieveCSCFeatureByPhoneNumber(String phoneNumber,String productType)
	{
		String prodType = (productType==null ? "C" : productType.trim());
		
		String sql ="select  sa.soc,  sf.feature_code, sf.ftr_add_sw_prm, " +
				" sf.service_type" +
				"  from  subscriber s " +
				" ,service_agreement sa " +
				" ,service_feature   sf " +
				" ,feature f " +
				" ,logical_date ld " +
				" where   s.subscriber_no = ? " +
				" and     s.product_type= ? " +
				" and     s.sub_status! ='C' " +
				" and     sa.ban=s.customer_ban " +
				" and     sa.subscriber_no=s.subscriber_no " +
				" and     sa.product_type=s.product_type" +
				" and     (trunc(sa.expiration_date) > trunc(ld.logical_date) or sa.expiration_date is null) " +
				" and     ld.logical_date_type='O' " +
				" and     sa.effective_date <= trunc(ld.logical_date) " +
				" and     sf.ban=sa.ban " +
				" and     sf.subscriber_no=sa.subscriber_no " +
				" and     sf.product_type = ? " +
				" and     sf.soc=sa.soc " +
				" and     sf.soc_seq_no=sa.soc_seq_no " +
				" and     sf.ftr_soc_ver_no= sa.soc_ver_no " +
				" and     f.feature_code=sf.feature_code " +
				" and     f.switch_code='CSC'  " +
				" order by decode (sf.service_type,'R',1,'S',1,'O',2,'P',2,3) " ;
			
		return super.getKnowbilityJdbcTemplate().query(sql,
				new Object[] { phoneNumber, prodType, prodType },
				new ResultSetExtractor<List<String>>() {

					@Override
					public List<String> extractData(ResultSet result)
							throws SQLException {
						List<String> vmfString = new ArrayList<String>();
						if (result.next()) {
							vmfString.add(result.getString(1));
							vmfString.add(result.getString(2));
							vmfString.add(result.getString(3));
						}
						return vmfString;
					}
				});
	}
}
