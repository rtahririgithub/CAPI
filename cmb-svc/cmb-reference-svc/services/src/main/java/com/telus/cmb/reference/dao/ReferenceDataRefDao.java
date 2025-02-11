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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.util.StringUtils;

import com.telus.api.account.AccountManager;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.Brand;
import com.telus.api.reference.BusinessRole;
import com.telus.api.reference.NetworkType;
import com.telus.api.reference.PricePlanSummary;
import com.telus.api.reference.ReasonType;
import com.telus.api.reference.ReferenceDataManager;
import com.telus.api.reference.ServiceSummary;
import com.telus.api.reference.UsageUnit;
import com.telus.cmb.common.aspects.ExpectedSLA;
import com.telus.cmb.common.util.ArrayUtil;
import com.telus.cmb.common.util.ExceptionUtil;
import com.telus.cmb.common.util.StringUtil;
import com.telus.cmb.reference.dto.FeeRuleDto;
import com.telus.cmb.reference.dto.ServiceTermDto;
import com.telus.cmb.reference.utilities.AppConfiguration;
import com.telus.eas.account.info.FleetClassInfo;
import com.telus.eas.contactevent.info.RoamingServiceNotificationInfo;
import com.telus.eas.equipment.info.EquipmentPossessionInfo;
import com.telus.eas.framework.info.Info;
import com.telus.eas.utility.info.AccountTypeInfo;
import com.telus.eas.utility.info.ActivityTypeInfo;
import com.telus.eas.utility.info.AdjustmentReasonInfo;
import com.telus.eas.utility.info.BillHoldRedirectDestinationInfo;
import com.telus.eas.utility.info.BrandInfo;
import com.telus.eas.utility.info.BusinessRoleInfo;
import com.telus.eas.utility.info.ChargeTypeInfo;
import com.telus.eas.utility.info.CollectionActivityInfo;
import com.telus.eas.utility.info.CollectionAgencyInfo;
import com.telus.eas.utility.info.CollectionPathDetailsInfo;
import com.telus.eas.utility.info.CollectionStateInfo;
import com.telus.eas.utility.info.CollectionStepApprovalInfo;
import com.telus.eas.utility.info.CountryInfo;
import com.telus.eas.utility.info.CreditCardPaymentTypeInfo;
import com.telus.eas.utility.info.CreditCheckDepositChangeReasonInfo;
import com.telus.eas.utility.info.CreditClassInfo;
import com.telus.eas.utility.info.CreditMessageInfo;
import com.telus.eas.utility.info.DepartmentInfo;
import com.telus.eas.utility.info.DiscountPlanInfo;
import com.telus.eas.utility.info.ExceptionReasonInfo;
import com.telus.eas.utility.info.FeatureInfo;
import com.telus.eas.utility.info.FeeWaiverReasonInfo;
import com.telus.eas.utility.info.FeeWaiverTypeInfo;
import com.telus.eas.utility.info.FollowUpCloseReasonInfo;
import com.telus.eas.utility.info.FollowUpTypeInfo;
import com.telus.eas.utility.info.GenerationInfo;
//import com.telus.eas.utility.info.LetterInfo;
//import com.telus.eas.utility.info.LetterSubCategoryInfo;
//import com.telus.eas.utility.info.LetterVariableInfo;
import com.telus.eas.utility.info.MemoTypeCategoryInfo;
import com.telus.eas.utility.info.MemoTypeInfo;
import com.telus.eas.utility.info.MigrationTypeInfo;
import com.telus.eas.utility.info.NetworkInfo;
import com.telus.eas.utility.info.NetworkTypeInfo;
import com.telus.eas.utility.info.PaymentMethodTypeInfo;
import com.telus.eas.utility.info.PaymentSourceTypeInfo;
import com.telus.eas.utility.info.PaymentTransferReasonInfo;
import com.telus.eas.utility.info.PoolingGroupInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.PricePlanTermInfo;
import com.telus.eas.utility.info.ProductTypeInfo;
import com.telus.eas.utility.info.PromoTermInfo;
import com.telus.eas.utility.info.ProvinceInfo;
import com.telus.eas.utility.info.ProvisioningTransactionStatusInfo;
import com.telus.eas.utility.info.ProvisioningTransactionTypeInfo;
import com.telus.eas.utility.info.RatedFeatureInfo;
import com.telus.eas.utility.info.ReasonTypeInfo;
import com.telus.eas.utility.info.ReferenceInfo;
import com.telus.eas.utility.info.RouteInfo;
import com.telus.eas.utility.info.SIDInfo;
import com.telus.eas.utility.info.SeatTypeInfo;
import com.telus.eas.utility.info.SegmentationInfo;
import com.telus.eas.utility.info.ServiceCodeTypeInfo;
import com.telus.eas.utility.info.ServiceDataSharingGroupInfo;
import com.telus.eas.utility.info.ServiceEquipmentTypeInfo;
import com.telus.eas.utility.info.ServiceExclusionGroupsInfo;
import com.telus.eas.utility.info.ServiceExtendedInfo;
import com.telus.eas.utility.info.ServiceFamilyTypeInfo;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.eas.utility.info.ServicePeriodHoursInfo;
import com.telus.eas.utility.info.ServicePeriodIncludedMinutesInfo;
import com.telus.eas.utility.info.ServicePeriodInfo;
import com.telus.eas.utility.info.ServicePeriodUsageRatesInfo;
import com.telus.eas.utility.info.ServicePolicyInfo;
import com.telus.eas.utility.info.ServiceRelationInfo;
import com.telus.eas.utility.info.ServiceUsageInfo;
import com.telus.eas.utility.info.SpecialNumberInfo;
import com.telus.eas.utility.info.SpecialNumberRangeInfo;
import com.telus.eas.utility.info.StateInfo;
import com.telus.eas.utility.info.StreetTypeInfo;
import com.telus.eas.utility.info.TalkGroupPriorityInfo;
import com.telus.eas.utility.info.TaxationPolicyInfo;
import com.telus.eas.utility.info.TitleInfo;
import com.telus.eas.utility.info.UnitTypeInfo;
import com.telus.eas.utility.info.UsageRateInfo;
import com.telus.eas.utility.info.VendorServiceInfo;
import com.telus.eas.utility.info.WorkFunctionInfo;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

/**
 * @author Pavel Simonovsky
 *
 */
public class ReferenceDataRefDao extends JdbcDaoSupport {
	
	private static final Log logger = LogFactory.getLog(ReferenceDataRefDao.class);

	private static final int ORACLE_REF_CURSOR = -10;
	
	private static final int FETCH_SIZE = 400;
	
	private static final String[] ROAM_LIKE_HOME_VALUES = { "U", "I" };
	
	private Set<String> interBrandPortActivityReasonCodes = null;
	
	
	private String arrayToDelimitedString(Object[] arr, String delim, String prefix, String suffix) {
		if (arr != null) {
			return StringUtils.collectionToDelimitedString( Arrays.asList(arr), delim, prefix, suffix);
		}
		return "";
	}
	
	public Collection<String> filterServiceListByPricePlan(String[] serviceList, String pricePlan) {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select distinct sg_src.soc " +
				" from  soc_group sg_src " +
				",soc_relation sr " +
				",soc_group sg_dst " +
				" where rtrim(sg_dst.soc)= ? " +
				" and   sr.soc_dest=sg_dst.gp_soc " +
				" and   sr.relation_type='G' " +
				" and   sg_src.gp_soc= sr.soc_src " +
				" and   sg_src.soc in ( ");
		sql.append(arrayToDelimitedString(serviceList, ",", "'", "'"));
		sql.append(")");
		
		return getJdbcTemplate().queryForList(sql.toString(), new Object[] {pricePlan}, String.class);
	}
	
	public Collection<String> filterServiceListByProvince(String[] serviceList, String province) {
		String sql = " select distinct ssr.soc  " +
		" from  soc_submkt_relation ssr, market m " +
		" where ssr.soc in ( " + 
		arrayToDelimitedString(serviceList, ",", "'", "'") + 
		") and ssr.sub_market=m.market_code and m.province= ? ";
		
		return getJdbcTemplate().queryForList(sql, new Object[] {province}, String.class);
	}
	
	public Set<String> getInterBrandPortActivityReasonCodes() {
		if (interBrandPortActivityReasonCodes==null) interBrandPortActivityReasonCodes=new HashSet<String>();
		return interBrandPortActivityReasonCodes;
	}

	private <T> T getReferenceData(final String refType, final String param, final ResultSetExtractor<T> rse) {
		
		String call = "{? = call REFERENCE_PKG.GetReferenceData(?, ?, ?, ?)}";
		
		return getJdbcTemplate().execute(call, new CallableStatementCallback<T>() {
			
			@Override
			public T doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {

				logger.debug("Retrieving reference data for type: [" + refType + "], and param: [" + param + "]");
				
				cs.registerOutParameter(1, OracleTypes.NUMBER);
				cs.setString(2, refType);
				cs.setString(3, param);
				cs.registerOutParameter(4, OracleTypes.CURSOR);
				cs.registerOutParameter(5, OracleTypes.VARCHAR);
				
				cs.execute();

				boolean success = cs.getInt(1) == AccountManager.NUMERIC_TRUE;

				if (success) {
				
					ResultSet rs = (ResultSet) cs.getObject(4);
					
					try {
						return rse.extractData(rs);
					} finally {
						if (rs != null) {
							rs.close();
						}
					}
				}
				else {
					throw new SQLException("Strored procedure failed: " + cs.getString(5));
				}
			}
		});
	}

	private <T> Collection<T> getReferenceDataCollection(final String refType, final String param, final RowMapper<T> rowMapper) {
		ResultSetExtractor<Collection<T>> extractor = new ResultSetExtractor<Collection<T>>() {
			
			@Override
			public Collection<T> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Collection<T> result = new ArrayList<T>();
				
				int rowNum = 1;
				
				while (rs.next()) {
					result.add(rowMapper.mapRow(rs, rowNum++));
				}
				
				return result;
			}
		};
		
		return getReferenceData(refType, param, extractor);
	}
	
	public boolean isAssociatedIncludedPromotion(String pricePlanCode, int term, String serviceCode){
		
		String termString = "";
		if (!(term == PricePlanSummary.CONTRACT_TERM_ALL)) {
			termString = " and uc.minimum_no_months =  " + term;
		}
		String sql = " select 'Y'  " +
		" from soc p, uc_soc_promo_terms uc " +
		" where p.soc= ? " +
		" and uc.soc = ? " +
		termString +
		" and exists " +
		"(select 1 " +
		" from soc_relation sr,soc_group sg_s, soc_group sg_p  " +
		" where sg_s.soc=uc.soc  " +
		" and sg_p.soc= p.soc " +
		" and sr.soc_src=sg_s.gp_soc  " +
		" and sr.soc_dest =sg_p.gp_soc)  ";
		
		return getJdbcTemplate().query(sql, new Object[] {Info.padTo(pricePlanCode, ' ', 9), Info.padTo(serviceCode, ' ', 9)}, 
				new ResultSetExtractor<Boolean>() {

			@Override
			public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
				return rs.next();
			}
		}) ;
	}

	public boolean isDefaultDealer(String dealerCode){

		String sql = " select default_dealer   " +
		" from account_sub_type_defaults "  +
		" where  default_dealer   = ? " ;

		return getJdbcTemplate().query(sql, new Object[] {dealerCode},new ResultSetExtractor<Boolean>() {

			@Override
			public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
				return rs.next();
			}
		});
	}

	public boolean isPortOutAllowed(final String status, final String activityCode, final String activityReasonCode) {
		
		String call = "{ ?=call REFERENCE_PKG.IsPortOutAllowed (?,?,?) }";
		
		return getJdbcTemplate().execute(call, new CallableStatementCallback<Boolean>() {
			
			@Override
			public Boolean doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				cs.registerOutParameter(1, Types.NUMERIC );
				cs.setString(2, status);
				cs.setString(3, activityCode);
				cs.setString(4, activityReasonCode);

				cs.execute();

				return cs.getInt(1) == 1;
			}
		});
	}

	public boolean isPrivilegeAvailable(final String businessRoleCode, String privilegeCode, String serviceCode) {	
		String sql = " select availability_ind from soc_policy " +
		" where soc=? and (business_role_code= ? or business_role_code='ALL') and privilege_code= ? ";
		
		String privilegeAvailable = getJdbcTemplate().query(sql, 
				new Object[] {Info.padTo(serviceCode, ' ', 9), businessRoleCode, privilegeCode}, new ResultSetExtractor<String>() {

			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				String privilege = "A";
				if (rs.next()) {
					privilege = rs.getString(1);
				}
				return privilege;
			}
		});

		if (privilegeAvailable.equals("A")) {
			if ((businessRoleCode.equals(BusinessRole.BUSINESS_ROLE_AGENT))||
					(privilegeCode.equals(ServiceSummary.PRIVILEGE_SERVICE_DISPLAY))||
					(privilegeCode.equals(ServiceSummary.PRIVILEGE_SERVICE_REMOVE))||
					(privilegeCode.equals(ServiceSummary.PRIVILEGE_SERVICE_RETAIN_ON_PRICE_PLAN_CHANGE))) {
				return true;
			} else {
				sql = "select nvl(eserve_ind,'N'), nvl(rda_ind,'N')from soc where soc=? ";
				
				privilegeAvailable = getJdbcTemplate().query(sql, 
						new Object[] {Info.padTo(serviceCode, ' ', 9)}, new ResultSetExtractor<String>() {
					
					@Override
					public String extractData(ResultSet rs) throws SQLException, DataAccessException {
						String privilege = "A";
						if (rs.next()) {
							if (businessRoleCode.equals(BusinessRole.BUSINESS_ROLE_CLIENT)) {
								privilege = rs.getString(1);
							} else if (businessRoleCode.equals(BusinessRole.BUSINESS_ROLE_DEALER)) {
								privilege = rs.getString(2);
							}
						}
						return privilege;
					}
				});
			}
		}
		
		return !privilegeAvailable.equals("N");
	}

	public boolean isServiceAssociatedToPricePlan(String pricePlanCode, String serviceCode) {
		String sql = " select sr.soc_src " +
		" from soc_relation sr,soc_group sg_s, soc_group sg_p " +
		" where sg_s.soc= ? " +
		" and sg_p.soc=  ? " +
		" and sr.soc_src=sg_s.gp_soc " +
		" and   sr.soc_dest =sg_p.gp_soc ";
		
		return getJdbcTemplate().query(sql, new Object[] {serviceCode, pricePlanCode}, new ResultSetExtractor<Boolean>() {
			
			@Override
			public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
				return rs.next();
			}
		});
	}

	private void mapReasonType( ResultSet result, List<ReasonTypeInfo> reasonTypeList ) throws SQLException {
		//we need to filter out Inter-Brand Port Activity Reason Codes, so that it wouldn't show up in any interface applications
		if ( !getInterBrandPortActivityReasonCodes().contains(result.getString(3)) ) {
			ReasonTypeInfo reasonType = new ReasonTypeInfo();
			reasonType.setCode((result.getString(3) == null ? "" : result.getString(3)));
			reasonType.setDescription((result.getString(6) == null ? "" : result.getString(6)));
			reasonType.setDescriptionFrench(result.getString(7) == null ? reasonType.getDescription() : result.getString(7).equals("") ? reasonType.getDescription() : result.getString(7));
			reasonType.setFeatureCode((result.getString(8) == null ? "" : result.getString(8)));
			reasonType.setDirection((result.getString(9) == null ? "" : result.getString(9)));
			reasonType.setProcessCode(result.getString(10) == null ? "" : result.getString(10));
			reasonType.setPricePlanChangeRequired(result.getString(11).equals("N") ? false : true);
			reasonTypeList.add(reasonType);
		}
	}

	public Collection<AccountTypeInfo> retrieveAccountTypes(){
		return getReferenceDataCollection("AccountTypes", "", new RowMapper<AccountTypeInfo>() {
			
			@Override
			public AccountTypeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				AccountTypeInfo info = new AccountTypeInfo();
				
				info.setAccountType(rs.getString(1).charAt(0));
				info.setAccountSubType(rs.getString(2).charAt(0));
				info.setDescription(rs.getString(3));
				info.setDescriptionFrench(rs.getString(4) == null ? rs.getString(3) : rs.getString(4).equals("") ? rs.getString(3) : rs.getString(4));
				info.setNumberLocation(rs.getString(6) == null ? "" : rs.getString(6));
				info.setDefaultDealer(rs.getString(7) == null ? "" : rs.getString(7));
				info.setDefaultSalesCode(rs.getString(8) == null ? "" : rs.getString(8));
				info.setMinimumSubscribersForFleet(rs.getInt(9));
				info.setBillingNameFormat(rs.getString(10).charAt(0));
				info.setCreditCheckRequired("Y".equals(rs.getString(11)) ? true : false);
				info.setDuplicateBANCheck("Y".equals(rs.getString(5)) ? true : false);
				
				return info;
			}
		});
	}

	public Collection<ActivityTypeInfo> retrieveActivityTypes(){
		return getReferenceData("ActivityTypes", "", new ResultSetExtractor<Collection<ActivityTypeInfo>>() {
			
			@Override
			public Collection<ActivityTypeInfo> extractData(ResultSet rs) throws SQLException,DataAccessException {
				
				List<ActivityTypeInfo> result = new ArrayList<ActivityTypeInfo>();
				String code = "^";
				ActivityTypeInfo info = null;
				List<ReasonTypeInfo> reasonTypeList = new ArrayList<ReasonTypeInfo>();
				
				while (rs.next()) {
					if (!code.equals(rs.getString(1) + rs.getString(2))) {
						if (!code.equals("^")) {
							info.setReasonTypes(reasonTypeList.toArray(new ReasonType[reasonTypeList.size()]));
							result.add(info);
							reasonTypeList = new ArrayList<ReasonTypeInfo>();
						}
						info = new ActivityTypeInfo();
						info.setActivityType(rs.getString(1));
						info.setCode(rs.getString(2));
						info.setDescription(rs.getString(4) == null ? "" : rs.getString(4));
						info.setDescriptionFrench(rs.getString(5) == null ? info.getDescription() : rs.getString(5).equals("") ? info.getDescription() : rs.getString(5));

						mapReasonType(rs, reasonTypeList);
					}
					else {
						mapReasonType(rs, reasonTypeList);
					}

					code = rs.getString(1) + rs.getString(2);
				}

				info.setReasonTypes(reasonTypeList.toArray(new ReasonType[reasonTypeList.size()]));
				result.add(info);
				
				return result;
			}
		});
	}
	
	public Collection<AdjustmentReasonInfo> retrieveAdjustmentReasons() {
		return getReferenceDataCollection("AdjustmentReasons", "", new RowMapper<AdjustmentReasonInfo>() {
			
			@Override
			public AdjustmentReasonInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				AdjustmentReasonInfo info = new AdjustmentReasonInfo();
				
				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3));
				info.setManualCharge(toBoolean(rs.getString(4)));
				info.setAmount(rs.getDouble(5));
				info.setAmountOverrideable(toBoolean(rs.getString(6)));
				info.setAdjustmentLevelCode(rs.getString(7));
				info.setAdjustmentActivityCode(rs.getString(8));
				info.setAdjustmentTaxIndicator(rs.getString(9));
				info.setAdjustmentCategory(rs.getString(10));
				info.setFrequency(rs.getInt(11));
				info.setMaxNumberOfRecurringCredits(rs.getInt(12));
				info.setExpiryDate(rs.getDate(13));
				info.setTypeCode(rs.getString(14));
				
				return info;
			}
		});
	}
	
	public Collection<ProvinceInfo> retrieveAllProvinces() {
		return getReferenceDataCollection("AllProvinces", "", new RowMapper<ProvinceInfo>() {
			
			@Override
			public ProvinceInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				ProvinceInfo info = new ProvinceInfo();

				info.setCountryCode(rs.getString(1));
				info.setCode(rs.getString(2));
				info.setDescription(rs.getString(3));
				info.setDescriptionFrench(rs.getString(4));
				
				return info;
			}
		});
	}

	public Collection<TitleInfo> retrieveAllTitles(){
		return getReferenceDataCollection("AllTitles", "", new RowMapper<TitleInfo>() {
			
			@Override
			public TitleInfo mapRow(ResultSet rs, int rowNum) throws SQLException, DataAccessException {
				TitleInfo info = new TitleInfo();

				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(1));
				info.setDescriptionFrench(rs.getString(3) == null ? rs.getString(1) : rs.getString(3).equals("") ? rs.getString(1) : rs.getString(3));

				return info;
			}
		});
	}

	public Date retrieveAlternateRCContractStartDate(String province) {
		
		String sql = " select effective_date " +
		" from batch_pp_rc_rate " +
		" where soc = 'CONTRDATE' " +
		" and province = '" + province.trim().toUpperCase() + "' ";
		
		return getJdbcTemplate().query(sql, new ResultSetExtractor<Date>() {
			
			@Override
			public Date extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					return rs.getTimestamp(1);
				}
				return null;
			}
		});
	}
	
	public Collection<BusinessRoleInfo> retrieveBusinessRoles()	{
		return getReferenceDataCollection("BusinessRoles", "", new RowMapper<BusinessRoleInfo>() {
			
			@Override
			public BusinessRoleInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				BusinessRoleInfo info = new BusinessRoleInfo();

				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(2));
				
				return info;
			}
		});
	}
	
	public Collection<String> retrieveCoverageServiceCodes(String coverageRegionCode){
		String sql = "select soc from soc_coverage_region where coverage_region_code = ?";
		return getJdbcTemplate().queryForList(sql, new Object[] {coverageRegionCode}, String.class);
		
	}
	
	public Collection<CreditMessageInfo> retrieveCreditMessages(){
		return getReferenceDataCollection("CreditMessages", "", new RowMapper<CreditMessageInfo>() {
			
			@Override
			public CreditMessageInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				CreditMessageInfo info = new CreditMessageInfo();

				info.setCode(rs.getString(1));
				info.setReferToCreditAnalyst(rs.getString(2).equals("Y"));
				info.setMessage(rs.getString(3));
				info.setMessageFrench(rs.getString(4) == null ? rs.getString(3) : rs.getString(4).equals("") ? rs.getString(3) : rs.getString(4));
				
				return info;
			}
		});	
	}

	public Collection<DepartmentInfo> retrieveDepartments(){
		return getReferenceDataCollection("Departments", "", new RowMapper<DepartmentInfo>() {

			@Override
			public DepartmentInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				DepartmentInfo info = new DepartmentInfo();

				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3) == null ? rs.getString(2) : rs.getString(3).equals("") ? rs.getString(2) : rs.getString(3));
				info.setDefaultWorkPosition(rs.getString(4));

				return info;
			}
		});	
	}
	
	public Collection<NetworkInfo> retrieveNetworks(){
		return getReferenceDataCollection("Networks", "", new RowMapper<NetworkInfo>() {
			
			@Override
			public NetworkInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				NetworkInfo info = new NetworkInfo();

				info.setNetworkId(rs.getInt(1));
				info.setAlias(rs.getString(2));
				info.setDescription(rs.getString(3));
				info.setDescriptionFrench(rs.getString(3));

				return info;
			}
		});
	}
	
	public Collection<ProductTypeInfo> retrieveProductTypes(){
		return getReferenceDataCollection("ProductTypes", "", new RowMapper<ProductTypeInfo>() {
			
			@Override
			public ProductTypeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				ProductTypeInfo info = new ProductTypeInfo();

				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3));
				
				return info;
			}
		});
	}
		
	public Collection<StateInfo> retrieveStates(){
		return getReferenceDataCollection("States", "", new RowMapper<StateInfo>() {
			
			@Override
			public StateInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				StateInfo info = new StateInfo();
				
				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3));
				
				return info;
			}
		});
	}

	public Collection<StreetTypeInfo> retrieveStreetTypes() {
		return getReferenceDataCollection("StreetTypes", "", new RowMapper<StreetTypeInfo>() {
			
			@Override
			public StreetTypeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				StreetTypeInfo info = new StreetTypeInfo();

				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3) == null ? rs.getString(2) : rs.getString(3).equals("") ? rs.getString(2) : rs.getString(3));
				
				return info;
			}
		});
	}
	
	public Collection<TitleInfo> retrieveTitles(){
		return getReferenceDataCollection("Titles", "", new RowMapper<TitleInfo>() {

			@Override
			public TitleInfo mapRow(ResultSet rs, int rowNum) throws SQLException, DataAccessException {
				TitleInfo info = new TitleInfo();

				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(1));
				info.setDescriptionFrench(rs.getString(4) == null ? rs.getString(1) : rs.getString(4).equals("") ? rs.getString(1) : rs.getString(4));

				return info;
			}
		});
	}
	
	public Collection<UnitTypeInfo> retrieveUnitTypes(){
		return getReferenceDataCollection("UnitTypes", "", new RowMapper<UnitTypeInfo>() {
			
			@Override
			public UnitTypeInfo mapRow(ResultSet rs, int rowNum) throws SQLException, DataAccessException {
				UnitTypeInfo info = new UnitTypeInfo();
				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3) == null ? rs.getString(2) : rs.getString(3).equals("") ? rs.getString(2) : rs.getString(3));

				return info;
			}
		});
	}
	
	public void setInterBrandPortActivityReasonCodes(Set<String> interBrandPortActivityReasonCodes) {
		this.interBrandPortActivityReasonCodes = interBrandPortActivityReasonCodes;
	}
	
	private boolean toBoolean(String str) {
		if (str == null || str.length() == 0)
			return false;
		else if (str.equalsIgnoreCase("Y") || str.equals("1"))
			return true;
		else
			return false;
	}

	private char toChar(String charStr) {
		if (charStr == null || charStr.length() == 0)
			return '\0';
		else
			return charStr.charAt(0);
	}
	
	public Collection<BillHoldRedirectDestinationInfo> retrieveBillHoldRedirectDestinations() {
		return getReferenceDataCollection("BillHoldRedirectDestinations", "", new RowMapper<BillHoldRedirectDestinationInfo>() {
			
			@Override
			public BillHoldRedirectDestinationInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				BillHoldRedirectDestinationInfo info = new BillHoldRedirectDestinationInfo();

				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3));
				
				return info;
			}
		});
	}
	
	public Collection<BrandInfo> retrieveBrands() {
		
		String sql = "select brand_id, brand_short_desc, brand_short_desc_f, brand_desc, brand_desc_f from brand";
		
		return getJdbcTemplate().query(sql, new RowMapper<BrandInfo>() {
			
			@Override
			public BrandInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				BrandInfo info = new BrandInfo();

				info.setBrandId(rs.getInt(1));
				info.setShortDescription(rs.getString(2));
				info.setShortDescriptionFrench(rs.getString(3));
				info.setDescription(rs.getString(4));
				info.setDescriptionFrench(rs.getString(5));

				return info;
			}
		});
	}
	
	public Collection<CollectionActivityInfo> retrieveCollectionActivities() {
		return getReferenceDataCollection("CollectionActivities", "", new RowMapper<CollectionActivityInfo>() {
			
			@Override
			public CollectionActivityInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				CollectionActivityInfo info = new CollectionActivityInfo();

				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3));
				
				return info;
			}
		});
	}
	
	public Collection<CollectionAgencyInfo> retrieveCollectionAgencies() {
		return getReferenceDataCollection("CollectionAgencies", "", new RowMapper<CollectionAgencyInfo>() {
			
			@Override
			public CollectionAgencyInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				CollectionAgencyInfo info = new CollectionAgencyInfo();
				
				String code = rs.getString(1);
				info.setCode(code == null ? "" : code.trim());
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3));
				
				return info;
			}
		});
	}
	
	public Collection<CollectionPathDetailsInfo> retrieveCollectionPathDetails() {
		return getReferenceDataCollection("CollectionPathDetails", "", new RowMapper<CollectionPathDetailsInfo>() {
			
			@Override
			public CollectionPathDetailsInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				CollectionPathDetailsInfo info = new CollectionPathDetailsInfo();

				info.setPathCode(rs.getString("col_path_code"));
				info.setStepNumber(rs.getInt("col_step_num"));
				info.setActivityCode(rs.getString("col_activity_code"));
				info.setDaysFromPreviousStep(rs.getInt("days_from_prev_step"));
				info.setPointOfDaysCount(rs.getString("point_of_days_count"));
				info.setApprovalFollowUpTypeCode(rs.getString("approval_fu_type"));
				info.setFollowUpTypeCode(rs.getString("follow_up_type"));
				info.setLetterTitle(rs.getString("letter_title"));
				info.setLetterTitleFrench(rs.getString("letter_title_f"));
				info.setBillMessageActivityCode(rs.getString("bill_msg_actv_code"));
				info.setBillMessageActivityReasonCode(rs.getString("bill_msg_actv_rsn_cd"));
				
				return info;
			}
		});
	}
	
	public Collection<CollectionStateInfo> retrieveCollectionStates() {
		return getReferenceDataCollection("CollectionStates", "", new RowMapper<CollectionStateInfo>() {
			
			@Override
			public CollectionStateInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				CollectionStateInfo info = new CollectionStateInfo();

				info.setPath(rs.getString(1));
				info.setStep(rs.getInt(2));
				info.setCollectionActivityCode(rs.getString(3));

				return info;
			}
		});
	}
	
	public Collection<CollectionStepApprovalInfo> retrieveCollectionStepApprovals() {
		return getReferenceDataCollection("CollectionStepApproval", "", new RowMapper<CollectionStepApprovalInfo>() {
			
			@Override
			public CollectionStepApprovalInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				CollectionStepApprovalInfo info = new CollectionStepApprovalInfo();

				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3));

				return info;
			}
		});
	}
	
	public <T extends ReferenceInfo> Collection<T> retrieveGenericCodesByType(String type, final Class<T> javaType) {
		
		String sql;
		
		if ( type.equalsIgnoreCase("COMM_REASON") ) {
			sql = "select generic_code, description, description_f from generic_codes_ext where trim(generic_type) = ?";
		} else {
			sql = "select gen_code, gen_desc, gen_desc_f from generic_codes where trim(gen_type) = ?";
		}
		
		return getJdbcTemplate().query(sql, new Object[] {type}, new RowMapper<T>() {
			
			@Override
			public T mapRow(ResultSet rs, int rowNum) throws SQLException {

				T info;
				
				try {
					info = javaType.newInstance();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}

				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3));

				return info;
			}
		});
	}
	
	public Collection<CountryInfo> retrieveCountries() {
		return getReferenceDataCollection("Countries", "", new RowMapper<CountryInfo>() {
			
			@Override
			public CountryInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new CountryInfo(rs.getString(1), rs.getString(2), rs.getString(3));
			}
		});
	}
	
	public Collection<CreditCardPaymentTypeInfo> retrieveCreditCardPaymentTypes() {
		return getReferenceDataCollection("CreditCardPaymentTypes", "", new RowMapper<CreditCardPaymentTypeInfo>() {
			
			@Override
			public CreditCardPaymentTypeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				CreditCardPaymentTypeInfo info = new CreditCardPaymentTypeInfo();

				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3));
				
				return info;
			}
		});
	}
	
	public Collection<CreditCheckDepositChangeReasonInfo> retrieveCreditCheckDepositChangeReasons() {
		return getReferenceDataCollection("CreditCheckDepositChangeReasons", "", new RowMapper<CreditCheckDepositChangeReasonInfo>() {
			
			@Override
			public CreditCheckDepositChangeReasonInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				CreditCheckDepositChangeReasonInfo info = new CreditCheckDepositChangeReasonInfo();
				
				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3));
				
				return info;
			}
		});
	}
	
	public Collection<CreditClassInfo> retrieveCreditClasses() {
		return getReferenceDataCollection("CreditClasses", "", new RowMapper<CreditClassInfo>() {
			
			@Override
			public CreditClassInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				CreditClassInfo info = new CreditClassInfo();
				
				info.setCode(rs.getString(1));
				info.setAccountEligibilityForLatePaymentInd(rs.getString(2));
				info.setShortDescription(rs.getString(3));
				info.setShortDescriptionFrench(rs.getString(4));
				info.setDescription(rs.getString(5));
				info.setDescriptionFrench(rs.getString(6));
				info.setDepositReqInd(rs.getString(7));
				info.setOnlineSelectInd(rs.getString(8));
				
				return info;
			}
		});
	}
	
	public CreditMessageInfo retrieveCreditMessageByCode(String code) {
		
		String sql = " select credit_message_cd, credit_referral_flg, english_message, french_message " +
		" from credit_class_decision where credit_message_cd = ? ";
		
		return getJdbcTemplate().query(sql, new Object[] {code}, new ResultSetExtractor<CreditMessageInfo>() {
			
			@Override
			public CreditMessageInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				CreditMessageInfo info = new CreditMessageInfo();
				if (rs.next()) {
					info.setCode(rs.getString(1));
					info.setReferToCreditAnalyst(rs.getString(2).equals("Y") ? true : false);
					info.setMessage(rs.getString(3));
					info.setMessageFrench(rs.getString(4)==null ? rs.getString(3) : rs.getString(4).equals("") ? rs.getString(3) : rs.getString(4));
				}
				return info;
			}
		});
	}
	
	public String retrieveDefaultLDC() {
		String sql = " select prm_value from param_values where param_code = 'DEFAULT_LDC'";
		return getJdbcTemplate().queryForObject(sql, String.class);
	}
	
	public Collection<EquipmentPossessionInfo> retrieveEquipmentPossessions() {
		return getReferenceDataCollection("EquipmentPossessions", "", new RowMapper<EquipmentPossessionInfo>() {
			
			@Override
			public EquipmentPossessionInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				EquipmentPossessionInfo info = new EquipmentPossessionInfo();

				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3));
				
				return info;
			}
		});
	}
	
	public Collection<ExceptionReasonInfo> retrieveExceptionReasons() {
		return getReferenceDataCollection("ExceptionReasons", "", new RowMapper<ExceptionReasonInfo>() {
			
			@Override
			public ExceptionReasonInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				ExceptionReasonInfo info = new ExceptionReasonInfo();
				
				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3));
				
				return info;
			}
		});
	}
	
	public Collection<FeatureInfo> retrieveFeatureCategories() {
		
		String sql = " select fcr.feature_code, fcr.category_code " +
		" from  feature_category_relation fcr order by fcr.category_code";
		
		return getJdbcTemplate().query(sql, new RowMapper<FeatureInfo>() {
			
			@Override
			public FeatureInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				FeatureInfo info = new FeatureInfo();

				info.setCode(rs.getString(1));
				info.setCategoryCode(rs.getString(2));
				
				return info;
			}
		});
	}
	
	public Collection<FeatureInfo> retrieveFeatures() {

		String sql = " select f.feature_code, f.feature_desc, f.feature_desc_f  " +
		" ,f.dup_ftr_allow_ind, f.csm_param_req_ind " +
		" ,f.msisdn_ind , nvl(f.ftr_service_type,' ')  " +
		" ,f.switch_code, f.product_type    " +
		" , category_code " +
		" ,f.feature_type " +
		" ,f.pool_group_id " +
		"  ,f.def_sw_params " +
		" from feature f , feature_category_relation fcr " +
		" where fcr.feature_code(+)=f.feature_code " +
		" order by f.feature_desc ";

		return getJdbcTemplate().query(sql, new RowMapper<FeatureInfo>() {
			
			@Override
			public FeatureInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				FeatureInfo info = new FeatureInfo();

				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3));
				info.setDuplFeatureAllowed(rs.getString(4) == null ? true :rs.getString(4).equals("N") ? false : true);
				info.setParameterRequired(rs.getString(5) == null ? false :rs.getString(5).equals("Y") ? true : false);
				info.setAdditionalNumberRequired(rs.getString(6) == null ? false :rs.getString(6).equals("Y") ? true : false);
				info.setTelephony(rs.getString(7).trim().equals("") || rs.getString(7).equals("C") ? true : false);
				info.setDispatch(rs.getString(7).equals("D") || rs.getString(9).equals("H") ? true : false);
				info.setWirelessWeb(rs.getString(7).equals("P") ? true : false);
				info.setSwitchCode(rs.getString(8));
				info.setCategoryCode(rs.getString(10));
				info.setFeatureType(rs.getString(11) == null ? "" : rs.getString(11));
				info.setPoolGroupId(rs.getString(12));
				info.setParameterDefault(rs.getString(13));
				
				return info;
			}
		});
	}
	
	public Collection<FeeWaiverReasonInfo> retrieveFeeWaiverReasons() {
		
		String  sql = "SELECT ADJUSTMENT_REASON.REASON_CODE,ADJUSTMENT_REASON.ADJ_DSC, ADJUSTMENT_REASON.ADJ_DSC_F " +
		" FROM ADJUSTMENT_REASON   " +
		"WHERE ( ADJUSTMENT_REASON.ADJ_ACTV_CODE = 'FEW' ) AND ( ADJUSTMENT_REASON.MAN_ADJ_IND = 'Y' )";
		
		return getJdbcTemplate().query(sql, new RowMapper<FeeWaiverReasonInfo>() {
			
			@Override
			public FeeWaiverReasonInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				FeeWaiverReasonInfo info = new FeeWaiverReasonInfo();
				
				info.setCode(rs.getString(1).trim());
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3));
				
				return info;
			}
		});
	}
	
	public Collection<FeeWaiverTypeInfo> retrieveFeeWaiverTypes() {

		String sql = "SELECT FEATURE.FEATURE_CODE, FEATURE.FEATURE_DESC, FEATURE.FEATURE_DESC_F " +
		"FROM FEATURE   WHERE ((FEATURE.FEE_WAIVE_CODE = 'N' )  OR  ( FEATURE.FEE_WAIVE_CODE = 'B' ))";
		
		return getJdbcTemplate().query(sql, new RowMapper<FeeWaiverTypeInfo>() {
			
			@Override
			public FeeWaiverTypeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				FeeWaiverTypeInfo info = new FeeWaiverTypeInfo();
				
				info.setCode(rs.getString(1).trim());
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3));
				
				return info;
			}
		});
	}

	public Collection<FleetClassInfo> retrieveFleetClasses() {
		
		String sql = " select fleet_class, max_ufmi_in_range from fleet_class " ;
		
		return getJdbcTemplate().query(sql, new RowMapper<FleetClassInfo>() {
			
			@Override
			public FleetClassInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				FleetClassInfo info = new FleetClassInfo() ;
				
				info.setCode(rs.getString(1));
				info.setMaximumMemberIdInRange(rs.getInt(2));
				
				return info;
			}
		});
	}	

	public Collection<FollowUpCloseReasonInfo> retrieveFollowUpCloseReasons() {
		
		String sql = "select close_fup_code, close_fup_desc, close_fup_desc_f from close_fup_code";
		
		return getJdbcTemplate().query(sql, new RowMapper<FollowUpCloseReasonInfo>() {
			
			@Override
			public FollowUpCloseReasonInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				FollowUpCloseReasonInfo info = new FollowUpCloseReasonInfo();

				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3));
	
				return info;
			}
		});
	}
	
	public FollowUpCloseReasonInfo retrieveFollowUpCloseReason(String reasonCode) {
		
		String sql = "select close_fup_code, close_fup_desc, close_fup_desc_f from close_fup_code where close_fup_code = ?";
		
		return getJdbcTemplate().query(sql, new Object[] {reasonCode}, new ResultSetExtractor<FollowUpCloseReasonInfo>() {
			
			@Override
			public FollowUpCloseReasonInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					FollowUpCloseReasonInfo info = new FollowUpCloseReasonInfo();
					
					info.setCode(rs.getString(1));
					info.setDescription(rs.getString(2));
					info.setDescriptionFrench(rs.getString(3));
					
					return info;
				}
				return null;
			}
		});
	}
	
	public Collection<FollowUpTypeInfo> retrieveFollowUpTypes() {
		
		String sql = "select futp.futp_fu_type, futp.futp_fu_description, "
			+ "       futp.futp_fu_description_f, futp.futp_manual_open_ind, "
			+ "       futp.futp_fu_category, futp.futp_system_txt, futp.futp_system_txt_f, "
			+ "       futp.futp_def_no_of_days, futp.futp_priority, "
			+ "       fudp.department_code, fuwp.work_position_code "
			+ "  from follow_up_type futp, "
			+ "       follow_up_type_department fudp, "
			+ "       follow_up_type_assigned_wp fuwp "
			+ " where futp.futp_fu_type = fudp.follow_up_type(+) "
			+ "   and fudp.effective_date(+) <= sysdate "
			+ "   and NVL(fudp.expiration_date, TO_DATE('2100/12/31', 'YYYY/MM/DD')) > sysdate "
			+ "   and futp.futp_fu_type = fuwp.follow_up_type(+) "
			+ "   and fuwp.effective_date(+) <= sysdate "
			+ "   and NVL(fuwp.expiration_date, TO_DATE('2100/12/31', 'YYYY/MM/DD')) > sysdate "
			+ " order by futp.futp_fu_type";
		
		return getJdbcTemplate().query(sql, new ResultSetExtractor<Collection<FollowUpTypeInfo>>() {
			
			@Override
			public Collection<FollowUpTypeInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {

				ArrayList<FollowUpTypeInfo> result = new ArrayList<FollowUpTypeInfo>();
				ArrayList<String> departmentCodes = new ArrayList<String>();
				ArrayList<String> workPositionIds = new ArrayList<String>();
				
				FollowUpTypeInfo followUpType = null;

				String currentFollowUpType = "";
				
				while (rs.next()) {
					String newFollowUpType = rs.getString(1);

					if (!newFollowUpType.equals(currentFollowUpType)) {
						if (followUpType != null) {
							followUpType.setDefaultDepartmentCodes(departmentCodes.toArray( new String[departmentCodes.size()]));
							followUpType.setDefaultWorkPositionIds(workPositionIds.toArray( new String[workPositionIds.size()]));
							result.add(followUpType);
						}

						followUpType = new FollowUpTypeInfo();

						followUpType.setCode(newFollowUpType);
						followUpType.setDescription(rs.getString(2));
						followUpType.setDescriptionFrench(rs.getString(3) == null ? rs.getString(2) : rs.getString(3).equals("") ? rs.getString(2) : rs.getString(3));
						followUpType.setManualOpenInd(rs.getString(4));
						followUpType.setCategory(rs.getString(5));
						followUpType.setSystemText(rs.getString(6));
						followUpType.setSystemTextFrench(rs.getString(7) == null ? rs.getString(6) : rs.getString(7).equals("") ? rs.getString(6) : rs.getString(7));
						followUpType.setDefaultNoOfDays(rs.getInt(8));
						followUpType.setPriority(rs.getInt(9));

						departmentCodes = new ArrayList<String>();
						workPositionIds = new ArrayList<String>();

						currentFollowUpType = newFollowUpType;
					}

					String defaultDepartmentCode = rs.getString(10);
					if (defaultDepartmentCode != null && !departmentCodes.contains(defaultDepartmentCode))
						departmentCodes.add(defaultDepartmentCode);

					String defaultWorkPositionId = rs.getString(11);
					if (defaultWorkPositionId != null && !workPositionIds.contains(defaultWorkPositionId))
						workPositionIds.add(defaultWorkPositionId);
				}

				if (followUpType != null) {
					followUpType.setDefaultDepartmentCodes(departmentCodes.toArray( new String[departmentCodes.size()]));
					followUpType.setDefaultWorkPositionIds(workPositionIds.toArray( new String[workPositionIds.size()]));
					result.add(followUpType);
				}
				
				return result;
			}
		});
	}
	
	public Collection<GenerationInfo> retrieveGenerations() {
		return getReferenceDataCollection("Generations", "", new RowMapper<GenerationInfo>() {
			
			@Override
			public GenerationInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new GenerationInfo(rs.getString(1).trim(), rs.getString(2), rs.getString(3));
			}
		});
	}
	
	public List<ServiceInfo> retrieveIncludedPromotions(String pricePlanCode, final String equipmentType, final String networkType, final String provinceCode, final int term) {
		
		long startTime = Calendar.getInstance().getTimeInMillis();
		final String ppSoc = Info.padTo(pricePlanCode, ' ', 9);
		String call = "{ call Price_Plan_utility_pkg.GetIncludedPromotions(?, ?, ?, ?, ?, ?) }";
		List<ServiceInfo> services = getJdbcTemplate().execute(call, new CallableStatementCallback<List<ServiceInfo>>() {
			
			@Override
			public List<ServiceInfo> doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				
				cs.setString(1, ppSoc);
				cs.setString(2, equipmentType);
				cs.setString(3, networkType);
				cs.setString(4, provinceCode);
				cs.setInt(5, term);
				cs.registerOutParameter(6, ORACLE_REF_CURSOR);
				
				cs.execute();
				
				ResultSet result = (ResultSet) cs.getObject(6);

				String serviceCode = "^";
				String featureCode = "^";
				ArrayList<ServiceInfo> optionalServices = new ArrayList<ServiceInfo>();
				ArrayList<RatedFeatureInfo> optionalFeatures = new ArrayList<RatedFeatureInfo>();
				ServiceInfo optionalService = new ServiceInfo();
				double optionalServiceRC = 0;
				HashSet<String> optionalServiceCategoryCodes = new HashSet<String>();
				
				try {
					
					while (result.next()) {

						if (!serviceCode.equals(result.getString(2))) {					
							
							// initialize these values for each soc in the result set
							if (!serviceCode.equals("^")) {
								optionalService.setFeatures(optionalFeatures.toArray( new RatedFeatureInfo[optionalFeatures.size()]));
								optionalService.setRecurringCharge(optionalServiceRC);
								optionalService.setCategoryCodes(optionalServiceCategoryCodes.toArray(new String[optionalServiceCategoryCodes.size()]));
								optionalServices.add(optionalService);
								optionalService = new ServiceInfo();
								optionalFeatures = new ArrayList<RatedFeatureInfo>();
								optionalServiceRC = 0;
								optionalServiceCategoryCodes = new HashSet<String>();
							}

							//sEquipTypes = new HashSet();
							
							// service
							optionalService.setServiceType(result.getString("service_type"));
							optionalService.setCode(result.getString("soc"));
							optionalService. setBillCycleTreatmentCode(result.getString("bill_cycle_treatment_cd"));
							optionalService.setBrandId(result.getInt("brand_ind"));
							optionalService.setDescription(result.getString("soc_description") == null ? null :
								result.getString("soc_description").trim());
							optionalService.setDescriptionFrench(result.getString("soc_description_f") == null ? 
									optionalService.getDescription() : result.getString("soc_description_f").equals("") ?
											optionalService.getDescription() : result.getString("soc_description_f"));
							optionalService.setTermMonths(result.getString("promo").equals("Y") ? result.getInt("duration") : 
								result.getInt("minimum_no_months"));
							optionalService.setDealerActivation(result.getString("promo").equals("Y") && 
									result.getString("dealer_available_ind").equals("Y") ? true : result.getString("promo").equals("N") &&
											result.getString("rda_ind").equals("Y") ? true : false);
							optionalService.setClientActivation(result.getString("promo").equals("Y") &&
									result.getString("client_available_ind").equals("Y") ? true : result.getString("promo").equals("N") && 
											result.getString("eserve_ind").equals("Y") ? true : false);
							optionalService.setCoverageType(result.getString("coverage_type"));
							optionalService.setProductType(result.getString("s_product_type"));
							optionalService.setIncludedPromotion(result.getString("promo").equals("Y") &&
									result.getString("included_promo_ind").equals("Y") ? true : false);
							optionalService.setActive(result.getString("soc_status") == null ? false : 
								result.getString("soc_status").equals("A") ? true : false);
							optionalService.setBillingZeroChrgSuppress(result.getString("bl_zero_chrg_suppress_ind") == null ?
									false : result.getString("bl_zero_chrg_suppress_ind").equals("Y") ? true : false );
							optionalService.setForSale(result.getString("for_sale_ind") == null ? false :
								result.getString("for_sale_ind").equals("Y") ? true : false);
							optionalService.setEffectiveDate(result.getTimestamp("sale_eff_date"));
							optionalService.setExpiryDate(result.getTimestamp("sale_exp_date"));
							optionalService.setCurrent(result.getString("current_ind") == null ? false :
								result.getString("current_ind").equals("Y") ? true : false);
							optionalService.setTelephonyFeaturesIncluded(result.getString("telephony_features_inc") == null ?
									false : result.getString("telephony_features_inc").equals("Y") ? true : false);
							optionalService.setDispatchFeaturesIncluded(result.getString("dispatch_features_inc") == null ?
									false : result.getString("dispatch_features_inc").equals("Y") ? true : false);
							optionalService.setWirelessWebFeaturesIncluded(result.getString("wireless_web_features_inc") == null ?
									false : result.getString("wireless_web_features_inc").equals("A") ? true : false);							
							optionalService.setBoundServiceAttached(StringUtil.emptyFromNull(
									result.getString("has_bound")).equals("Y") ? true : false);
							optionalService.setPromotionAttached(StringUtil.emptyFromNull(
									result.getString("has_promotion")).equals("Y") ? true : false);
							optionalService.setBoundService(StringUtil.emptyFromNull(
									result.getString("is_bound")).equals("Y") ? true : false);
							optionalService.setSequentiallyBoundServiceAttached(StringUtil.emptyFromNull(
									result.getString("has_seq_bound")).equals("Y") ? true : false);
							optionalService.setSequentiallyBoundService(StringUtil.emptyFromNull(
									result.getString("is_seq_bound")).equals("Y") ? true : false);
							optionalService.setRecurringChargeFrequency(result.getInt("rc_freq_of_pym"));
							optionalService.setPeriodCode(result.getString("period_set_code"));
							optionalService.setHasAlternateRecurringCharge(result.getString("brc_soc") != null);
							optionalService.setLevelCode(result.getString("soc_level_code"));
							optionalService.setPDAMandatoryGroup(StringUtil.emptyFromNull(
									result.getString("pda_mandatory_ind")).equals("Y") ? true : false);
							optionalService.setRIMMandatoryGroup(StringUtil.emptyFromNull(
									result.getString("rim_mandatory_ind")).equals("Y") ? true : false);	
							optionalService.setSocServiceType(result.getString("soc_service_type"));
							optionalService.setDurationServiceHours(result.getInt("soc_duration_hours"));
							optionalService.setRLH(isRoamLikeHome(result.getString("rlh_ind")));
							
							// feature
							RatedFeatureInfo optionalFeature = mapFeature(result);
							optionalServiceRC += optionalFeature.getRecurringCharge();
							if (optionalFeature.getCategoryCode() != null) {
								optionalServiceCategoryCodes.add(optionalFeature.getCategoryCode());
							}							
							optionalFeatures.add(optionalFeature);
							
						} else if (!featureCode.equals(result.getString("feature_code"))) {
							
							RatedFeatureInfo optionalFeature = mapFeature(result);
							optionalServiceRC += optionalFeature.getRecurringCharge();
							if (optionalFeature.getCategoryCode() != null) {
								optionalServiceCategoryCodes.add(optionalFeature.getCategoryCode());
							}
							optionalFeatures.add(optionalFeature);
						}  

						// set these values prior to the next iteration through the result set
						featureCode = result.getString("feature_code");
						serviceCode = result.getString("soc");
						
					}
					
					if (!(optionalService.getCode()).equals("")) {
						optionalService.setFeatures(optionalFeatures.toArray( new RatedFeatureInfo[optionalFeatures.size()]));
						optionalService.setRecurringCharge(optionalServiceRC);
						optionalService.setCategoryCodes(optionalServiceCategoryCodes.toArray( new String[optionalServiceCategoryCodes.size()]));
						//optionalService.setEquipmentTypes((String[])sEquipTypes.toArray(new String[sEquipTypes.size()]));
						optionalServices.add(optionalService);
					}
				} finally {
					result.close();
				}
				populateServiceExtraProperties(optionalServices, cs.getConnection());
				
				return optionalServices;					
			}
			
			private RatedFeatureInfo mapFeature( ResultSet result ) throws SQLException {
				
				RatedFeatureInfo optionalFeature = new RatedFeatureInfo();
				optionalFeature.setCode(result.getString("feature_code"));
				optionalFeature.setDescription(result.getString("feature_desc") == null ? 
						null : result.getString("feature_desc").trim());
				optionalFeature.setDescriptionFrench(result.getString("feature_desc_f") == null ? 
						optionalFeature.getDescription() : result.getString("feature_desc_f").equals("") ?
								optionalFeature.getDescription() : result.getString("feature_desc_f").trim());
				optionalFeature.setDuplFeatureAllowed(result.getString("dup_ftr_allow_ind") == null ?
						true :result.getString("dup_ftr_allow_ind").equals("N") ? false : true);
				optionalFeature.setParameterRequired(result.getString("csm_param_req_ind") == null ?
						false :result.getString("csm_param_req_ind").equals("Y") ? true : false);
				optionalFeature.setRecurringChargeFrequency(result.getInt("rc_freq_of_pym"));
				// set recurring charge
				optionalFeature.setRecurringCharge(result.getDouble("rc_rate"));
				optionalFeature.setUsageCharge(result.getDouble("uc_rate"));
				// set category
				optionalFeature.setCategoryCode(result.getString("category_code"));
				
				optionalFeature.setAdditionalNumberRequired(result.getString("msisdn_ind") == null ? 
						false : result.getString("msisdn_ind").equals("Y") ? true : false);
				optionalFeature.setTelephony(result.getString("ftr_service_type").trim().equals("") ||
						result.getString("ftr_service_type").equals("C") ? true : false);
				optionalFeature.setDispatch(result.getString("ftr_service_type").equals("D") || 
						result.getString("f_product_type").equals("H") ? true : false);
				optionalFeature.setWirelessWeb(result.getString("ftr_service_type").equals("P") ? true : false);
				optionalFeature.setSwitchCode(result.getString("switch_code") == null ? "" : result.getString("switch_code"));
				optionalFeature.setMinutePoolingContributor(result.getString("mpc_ind") == null ? false :
					result.getString("mpc_ind").equals("Y") ? true : false);
				optionalFeature.setCallingCircleSize(result.getInt("calling_circle_size"));
				optionalFeature.setFeatureType(result.getString("feature_type") == null ? "" : result.getString("feature_type"));
				optionalFeature.setPoolGroupId(result.getString("pool_group_id"));
				optionalFeature.setParameterDefault(result.getString("def_sw_params"));
				
				return optionalFeature;
				
			}
		});
			
		long endTime = Calendar.getInstance().getTimeInMillis();
		int resultSize = -1;
		if (services != null) {
			resultSize = services.size();
		}
		logger.info("getIncludedPromotions Time elapsed=" + (endTime - startTime) + " with pricePlanCode=[" + pricePlanCode + "],equipmentType=[" + equipmentType + "], networkType=[" + networkType
				+ "], provinceCode=[" + provinceCode + "],term=[" + term + "]. resultSize=["+resultSize+"]");
		
		return services;
	}
/*	
	public <T extends ServiceInfo> void retrieveServiceEquipmentTypeInfo(List<T> services) {
		if (services == null || services.size() == 0) {
			return;
		}
		
		for (int i = 0; i < (int) Math.ceil((double) services.size() / FETCH_SIZE); i++) {
			int fromIndex = i * FETCH_SIZE;
			int toIndex = ((i + 1) * FETCH_SIZE <= services.size()) ? (i + 1) * FETCH_SIZE : services.size();
			final List<T> socs = services.subList(fromIndex, toIndex);
			final HashMap<String, ServiceInfo> map = new HashMap<String, ServiceInfo>(socs.size());

			for (ServiceInfo service : socs) {
				map.put(service.getCode().trim(), service);
			}

			String call = "{call price_plan_utility_pkg.getSocEquipRelations(?,?)}";
			
			getJdbcTemplate().execute(call, new CallableStatementCallback<Object>() {
				
				@Override
				public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {

					ArrayDescriptor socsArrayDesc = ArrayDescriptor.createDescriptor("T_SOCS", cs.getConnection());
					ARRAY socsArray = new ARRAY(socsArrayDesc, cs.getConnection(), (String[]) map.keySet().toArray(new String[socs.size()]));

					((OracleCallableStatement)cs).setARRAY(1, socsArray);
					cs.registerOutParameter(2, OracleTypes.CURSOR);
					cs.setFetchSize(FETCH_SIZE);
					cs.execute();
					ResultSet result = (ResultSet) cs.getObject(2);
					result.setFetchSize(FETCH_SIZE);

					while (result.next()) {
						ServiceInfo service = (ServiceInfo) map.get(result.getString("SOC").trim());
						populateServiceEquipRelation(service, result.getString("equipment_type"), result.getString("network_type"));
					}
					
					return null;
				}
			});
		}		
	}
*/	
	@SuppressWarnings("unchecked")
	private void populateServiceEquipRelation(ServiceInfo service, String equipmentType, String networkType) {
		String [] networkList;

		if (NetworkType.NETWORK_TYPE_ALL.equals(networkType)) {
			networkList = NetworkType.NETWORK_TYPE_ALL_LIST;
		}else {
			networkList = new String[1];
			networkList[0] = networkType;
		}

		for (int i = 0; i < networkList.length; i++) {
			ServiceEquipmentTypeInfo serviceEquipmentInfo = service.getServiceEquipmentTypeInfo(networkList[i]);
			if (serviceEquipmentInfo == null) {
				serviceEquipmentInfo = new ServiceEquipmentTypeInfo();
			}
			serviceEquipmentInfo.setCode(service.getCode());
			serviceEquipmentInfo.setNetworkType(networkList[i]);
			serviceEquipmentInfo.getEquipmentTypes().add(equipmentType);
			service.addServiecEquipmentTypeInfo(serviceEquipmentInfo);
		}
	}
	
//	public LetterInfo retrieveLetter(String letterCategory, String letterCode) {
//		
//		String sql = "SELECT " +
//		"  LETTER_CODE, LETTER_DESC, LETTER_DESC_F,  " +
//		"  LETTER_CAT, EFFECTIVE_DATE, EXPIRATION_DATE,  " +
//		"  LETTER_TITLE, LETTER_TITLE_F, LETTER_VER,  " +
//		"  LETTER_SUBCAT, LETTER_LEVEL " +
//		"FROM " +
//		"  LMS_LETTERS L1 " +
//		"WHERE " +
//		"  LETTER_CAT = ? and " +
//		"  LETTER_CODE = ? and " +
//		"  MANUAL_REQUEST_IND = 'Y' and " +
//		"  EFFECTIVE_DATE <= SYSDATE and " +
//		"  (EXPIRATION_DATE is null or " +
//		"  EXPIRATION_DATE >= SYSDATE) and " +
//		"  LETTER_VER =  " +
//		"  (SELECT  " +
//		"    MAX(LETTER_VER) " +
//		"  FROM " +
//		"    LMS_LETTERS " +
//		"   WHERE " +
//		"     LETTER_CODE = L1.LETTER_CODE and " +
//		"     LETTER_CAT = L1.LETTER_CAT " +
//		")";
//
//		return getJdbcTemplate().query(sql, new Object[] {letterCategory, letterCode}, new ResultSetExtractor<LetterInfo>() {
//			
//			@Override
//			public LetterInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
//				if (rs.next()){
//					LetterInfo info = new LetterInfo();
//
//					info.setCode(rs.getString(1));
//					info.setDescription(rs.getString(2));
//					info.setDescriptionFrench(rs.getString(3));
//					info.setLetterCategory(rs.getString(4));
//					info.setEffectiveDate(rs.getTimestamp(5));
//					info.setExpirationDate(rs.getTimestamp(6));
//					info.setTitle(rs.getString(7));
//					info.setTitleFrench(rs.getString(8));
//					info.setLetterVersion(rs.getByte(9));
//					info.setLetterSubCategory(rs.getString(10));
//					info.setLetterLeveL(rs.getString(11));
//
//					return info;
//				}
//				return null;
//			}
//		});
//	}
//	
//	public LetterInfo retrieveLetter(String letterCategory, String letterCode, int version) {
//		
//		String sql = "SELECT " +
//		"  LETTER_CODE, LETTER_DESC, LETTER_DESC_F,  " +
//		"  LETTER_CAT, EFFECTIVE_DATE, EXPIRATION_DATE,  " +
//		"  LETTER_TITLE, LETTER_TITLE_F, LETTER_VER,  " +
//		"  LETTER_SUBCAT, LETTER_LEVEL " +
//		"FROM " +
//		"  LMS_LETTERS  " +
//		"WHERE " +
//		"  LETTER_CAT = ? and " +
//		"  LETTER_CODE = ? and " +
//		"  LETTER_VER = ?";
//
//		return getJdbcTemplate().query(sql, new Object[] {letterCategory, letterCode, version}, new ResultSetExtractor<LetterInfo>() {
//			
//			@Override
//			public LetterInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
//				if (rs.next()){
//					LetterInfo info = new LetterInfo();
//
//					info.setCode(rs.getString(1));
//					info.setDescription(rs.getString(2));
//					info.setDescriptionFrench(rs.getString(3));
//					info.setLetterCategory(rs.getString(4));
//					info.setEffectiveDate(rs.getTimestamp(5));
//					info.setExpirationDate(rs.getTimestamp(6));
//					info.setTitle(rs.getString(7));
//					info.setTitleFrench(rs.getString(8));
//					info.setLetterVersion(rs.getByte(9));
//					info.setLetterSubCategory(rs.getString(10));
//					info.setLetterLeveL(rs.getString(11));
//
//					return info;
//				}
//				return null;
//			}
//		});
//	}
//	
//	public Collection<LetterInfo> retrieveLettersByCategory(String letterCategory) {
//		return getReferenceDataCollection("LettersByCategory", letterCategory, new RowMapper<LetterInfo>() {
//			
//			@Override
//			public LetterInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
//				LetterInfo info = new LetterInfo();
//
//				info.setCode(rs.getString(1));
//				info.setDescription(rs.getString(2));
//				info.setDescriptionFrench(rs.getString(3));
//				info.setLetterCategory(rs.getString(4));
//				info.setEffectiveDate(rs.getTimestamp(5));
//				info.setExpirationDate(rs.getTimestamp(6));
//				info.setTitle(rs.getString(7));
//				info.setTitleFrench(rs.getString(8));
//				info.setLetterVersion(rs.getByte(9));
//				info.setLetterSubCategory(rs.getString(10));
//				info.setLetterLeveL(rs.getString(11));
//				
//				return info;
//			}
//		});
//	}
//	
//	public Collection<LetterInfo> retrieveLettersByTitleKeyword(String titleKeyword) {
//		return getReferenceDataCollection("LettersByTitleKeyword", titleKeyword, new RowMapper<LetterInfo>() {
//			
//			@Override
//			public LetterInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
//				LetterInfo info = new LetterInfo();
//
//				info.setCode(rs.getString(1));
//				info.setDescription(rs.getString(2));
//				info.setDescriptionFrench(rs.getString(3));
//				info.setLetterCategory(rs.getString(4));
//				info.setEffectiveDate(rs.getTimestamp(5));
//				info.setExpirationDate(rs.getTimestamp(6));
//				info.setTitle(rs.getString(7));
//				info.setTitleFrench(rs.getString(8));
//				info.setLetterVersion(rs.getByte(9));
//				info.setLetterSubCategory(rs.getString(10));
//				info.setLetterLeveL(rs.getString(11));
//				
//				return info;
//			}
//		});
//	}
//
//	public Collection<LetterSubCategoryInfo> retrieveLetterSubCategories() {
//		
//		String sql = "select LETTER_SUBCAT, SUBCAT_DESC, SUBCAT_DESC_F, LETTER_CAT from LMS_LETTER_SUB_CAT";
//		
//		return getJdbcTemplate().query(sql, new RowMapper<LetterSubCategoryInfo>() {
//			
//			@Override
//			public LetterSubCategoryInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
//				LetterSubCategoryInfo info = new LetterSubCategoryInfo();
//				
//				info.setLetterSubCategory(rs.getString(1));
//				info.setDescription(rs.getString(2));
//				info.setDescriptionFrench(rs.getString(3));
//				info.setLetterCategory(rs.getString(4));
//				info.setCode(info.getLetterCategory()+"_"+info.getLetterSubCategory());
//				return info;
//			}
//		});
//	}
//	
//	public Collection<LetterVariableInfo> retrieveLetterVariables(String letterCategory, String letterCode, int letterVersion) {
//		
//		String sql = "SELECT " +
//		"  LMS_VARIABLES.VAR_NAME, LMS_VARIABLES.VAR_DESC, LMS_VARIABLES.VAR_DESC_F, LMS_VARIABLES.VAR_CONTROL_TYPE " +
//		"FROM " +
//		"  LMS_LETTER_VAR_LNK, LMS_VARIABLES " +
//		"WHERE " +
//		"  LMS_LETTER_VAR_LNK.LETTER_CAT = ? and " +
//		"  LMS_LETTER_VAR_LNK.LETTER_CODE = ? and " +
//		"  LMS_LETTER_VAR_LNK.LETTER_VER <= ? and " +
//		"  LMS_VARIABLES.VAR_NAME = LMS_LETTER_VAR_LNK.VAR_NAME and  " +
//		"  (LMS_VARIABLES.VAR_CONTROL_TYPE='M' or LMS_VARIABLES.VAR_CONTROL_TYPE='O')";
//		
//		Object[] args = {letterCategory, letterCode.trim(), letterVersion};
//		
//		return getJdbcTemplate().query(sql, args, new RowMapper<LetterVariableInfo>() {
//			
//			@Override
//			public LetterVariableInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
//				LetterVariableInfo info = new LetterVariableInfo();
//
//				info.setCode(rs.getString(1));
//				info.setDescription(rs.getString(2));
//				info.setDescriptionFrench(rs.getString(3));
//				info.setMandatory("M".equals(rs.getString(4)));
//				
//				return info;
//			}
//		});
//	}
	
	public Collection<ChargeTypeInfo> retrieveManualChargeTypes() {
		return getReferenceDataCollection("ManualChargeTypes", "", new RowMapper<ChargeTypeInfo>() {
			
			@Override
			public ChargeTypeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				ChargeTypeInfo info = new ChargeTypeInfo(
						rs.getString(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						true,
						rs.getDouble(8),
						toBoolean(rs.getString(6)),
						toChar(rs.getString(5)),
						toChar(rs.getString(7))
				);
				return info;
			}
		});
	}
	
	public Collection<String> retrieveMarketProvinces(String serviceCode) {
		return getReferenceDataCollection("MarketProvinces", serviceCode != null ? serviceCode.trim() : "", new RowMapper<String>() {
			
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1).equals("ALL") ? rs.getString(1) : rs.getString(2);
			}
		});
	}
	
	public Collection<MemoTypeCategoryInfo> retrieveMemoTypeCategories() {
		return getReferenceDataCollection("MemoTypeCategories", "", new RowMapper<MemoTypeCategoryInfo>() {
			
			@Override
			public MemoTypeCategoryInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				MemoTypeCategoryInfo info = new MemoTypeCategoryInfo();
				
				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(1));
				info.setDescriptionFrench(rs.getString(1));
				
				return info;
			}
		});
	}
	
	public Collection<MemoTypeInfo> retrieveMemoTypes() {
		return getReferenceDataCollection("MemoTypes", "", new RowMapper<MemoTypeInfo>() {
			
			@Override
			public MemoTypeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				MemoTypeInfo info = new MemoTypeInfo();

				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3) == null ? rs.getString(2) : rs.getString(3).equals("") ? rs.getString(2) : rs.getString(3));
				info.setCategory(rs.getString(4));
				info.setSystemText(rs.getString(5));
				info.setSystemTextFrench(rs.getString(6));
				info.setNumberOfParameters(rs.getInt(7));
				info.setManualInd(rs.getString(8));

				return info;
			}
		});
	}
	
	public Collection<MigrationTypeInfo> retrieveMigrationTypes() {
		return getReferenceDataCollection("MigrationTypes", "", new RowMapper<MigrationTypeInfo>() {
			
			@Override
			public MigrationTypeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				MigrationTypeInfo info = new MigrationTypeInfo();
				
				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3));
				
				return info;
			}
		});
	}
	
	public Collection<String> retrieveMinutePoolingContributorServiceCodes(String filterServices) {

		StringBuffer sql = new StringBuffer();
		sql.append(" select soc " +
		" from inclus_by_period " +
		" where soc in (select soc " +
		"  from soc " + 
		"  where service_type != 'P' " + 
		"  and soc in (select soc " +
		"   from uc_rated_feature " +
		"   where mpc_ind = 'Y')) ");

		if (filterServices.equalsIgnoreCase(ReferenceDataManager.ZERO_MINUTE_POOLING_CONTRIBUTOR_SERVICES)) {
			sql.append(" and inclusive_mou = 0 ");
		} else if (filterServices.equalsIgnoreCase(ReferenceDataManager.MINUTE_POOLING_CONTRIBUTOR_SERVICES)) {
			sql.append(" and inclusive_mou > 0 ");			
		}
		
		return getJdbcTemplate().query(sql.toString(), new RowMapper<String>() {
			
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
		});
	}

	public Collection<NetworkTypeInfo> retrieveNetworkTypes() {
		return getReferenceDataCollection("NetworkTypes", "", new RowMapper<NetworkTypeInfo>() {
			
			@Override
			public NetworkTypeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				NetworkTypeInfo info = new NetworkTypeInfo();

				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3) == null ? rs.getString(2) : rs.getString(3).equals("") ? rs.getString(2) : rs.getString(3));

				return info;
			}
		});
	}
	
	public Collection<String> retrieveNpaNxxForMsisdnReservation(final String phoneNumber, boolean isPortedInNumber) {
		
		String call;
		
		if (isPortedInNumber) {
			call = "{call REFERENCE_APP_PKG.getnpanxxforportedinmsisdn(?, ?, ?, ?)}";
		} else {
			call = "{call REFERENCE_APP_PKG.getnpanxxfortelusmsisdn(?, ?, ?, ?)}";
		}
		
		return getJdbcTemplate().execute(call, new CallableStatementCallback< Collection<String>>() {
			
			@Override
			public Collection<String> doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				
				List<String> result = new ArrayList<String>();
				
				cs.setString(1, phoneNumber.substring(0,3));
				cs.setString(2, phoneNumber.substring(3,6));
				cs.setString(3, phoneNumber.substring(6,10));
				cs.registerOutParameter(4, OracleTypes.CURSOR);
				
				cs.execute();
				
				ResultSet rs = (ResultSet) cs.getObject(4);

				try {
					while (rs.next()) {
						String npaNxx = rs.getString(1);
						if (npaNxx != null && !npaNxx.equals("")) {
							result.add(npaNxx);
						}
					}
					return result;
				} finally {
					rs.close();
				}
			}
		});
	}
	
	public Collection<PaymentMethodTypeInfo> retrievePaymentMethodTypes() {
		return getReferenceDataCollection("PaymentMethodTypes", "", new RowMapper<PaymentMethodTypeInfo>() {
			
			@Override
			public PaymentMethodTypeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				PaymentMethodTypeInfo info = new PaymentMethodTypeInfo();

				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3));
				
				return info;
			}
		});
	}
	
	public Collection<PaymentSourceTypeInfo> retrievePaymentSourceTypes() {
		return getReferenceDataCollection("PaymentSourceTypes", "", new RowMapper<PaymentSourceTypeInfo>() {
			
			@Override
			public PaymentSourceTypeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				PaymentSourceTypeInfo info = new PaymentSourceTypeInfo();

				info.setSourceID(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3));
				info.setSourceType(rs.getString(4));
				
				return info;
			}
		});
	}

	public Collection<PaymentTransferReasonInfo> retrievePaymentTransferReasons() {
		return getReferenceDataCollection("PaymentTransferReasons", "", new RowMapper<PaymentTransferReasonInfo>() {
			
			@Override
			public PaymentTransferReasonInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new PaymentTransferReasonInfo(rs.getString(1), rs.getString(2), rs.getString(3));
			}
		});
	}
	
	public Collection<PoolingGroupInfo> retrievePoolingGroups() {
		return getReferenceDataCollection("PoolingGroups", "", new RowMapper<PoolingGroupInfo>() {
			
			@Override
			public PoolingGroupInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				PoolingGroupInfo info = new PoolingGroupInfo();

				info.setPoolingGroupId(rs.getInt(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3) == null ? rs.getString(2) : rs.getString(3).equals("") ? rs.getString(2) : rs.getString(3));
				info.setCoverageType(rs.getString(4));
				
				return info;
			}
		});
	}

	public Collection<PricePlanTermInfo> retrievePricePlanTerms() {
		
		String sql = "select s.soc, to_char(term_months ) " +
				"from soc s, soc_group sg, price_plan_group_term ppgt " +
				"where sg.soc = s.soc " +
				"and ppgt.soc_group = sg.gp_soc " +
				"and s.service_type = 'P' " +
				"order by 1, 2 ";

		return getJdbcTemplate().query(sql, new ResultSetExtractor<Collection<PricePlanTermInfo>>() {
			
			@Override
			public Collection<PricePlanTermInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {

				List<PricePlanTermInfo> result = new ArrayList<PricePlanTermInfo>();
				Map<String, List<Integer>> ppTermMap = new HashMap<String, List<Integer>> ();
				
				while (rs.next()) {
					String pricePlanCode = rs.getString(1);
					int term = rs.getInt(2);
					
					List<Integer> ppTerms = ppTermMap.get(pricePlanCode);
					
					if (ppTerms == null) {
						ppTerms = new ArrayList<Integer>();
					}
					
					ppTerms.add(term);
					ppTermMap.put(pricePlanCode, ppTerms);
				}
				
				for (String ppCode : ppTermMap.keySet()) {
					PricePlanTermInfo termInfo = new PricePlanTermInfo();
					termInfo.setCode(ppCode);
					termInfo.setTermsInMonths(ArrayUtil.unboxInteger(ppTermMap.get(ppCode)));
					result.add(termInfo);
				}
				return result;
			}
		});
	}
	
	public PromoTermInfo retrievePromoTerm(String promoServiceCode) {
		
		String sql = " select price_plan_soc, " +
		"       duration, " +
		"       act_avail_ind, " +
		"       chng_avail_ind " +
		" from promo_price_plan_terms pt, " +
		" logical_date ld " +
		" where promo_price_plan_soc = rpad(?,9) " +
		" and  trunc(effective_date) <= trunc(ld.logical_date) " +
		" and (trunc(expiry_date) >  trunc(ld.logical_date) or expiry_date is null) " +
		"and    ld.logical_date_type='O' ";
		
		return getJdbcTemplate().query(sql, new Object[] {promoServiceCode}, new ResultSetExtractor<PromoTermInfo>() {
			
			@Override
			public PromoTermInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					PromoTermInfo info = new PromoTermInfo();
					
					info.setRegularServiceCode(rs.getString(1));
					info.setTermMonth(rs.getInt(2));
					info.setAvailableForActivation(rs.getString(3).equals("Y") ? true : false);
					info.setAvailableForChange(rs.getString(4).equals("Y") ? true : false);
					
					return info;
				}
				return null;
			}
		});
	}
	
	public Collection<String> retrievePromotionalDiscounts(String pricePlanCode, long[] productPromoTypes, boolean initialActivation) {

		String productPromoTypeString = "";

		if (productPromoTypes == null || productPromoTypes.length == 0) {
			productPromoTypeString = "99999999";
		} else {
			for (int i = 0 ; i < productPromoTypes.length; i++) {
				productPromoTypeString =  productPromoTypeString + ((i == 0) ? "99999999, " : "," ) + String.valueOf(productPromoTypes[i] );
			}
		}
		
		String sql = "select dps.discount_plan_cd " +
		" from discount_plan_soc dps " +
		"     ,discount_plan dp " +
		"     ,logical_date  ld " +
		" where rtrim(dps.soc) =   ? " +
		" and   dps.product_type_id in (" + productPromoTypeString + ")" +
		" and   dps.initial_activation_ind = ? " +
		" and  trunc(dps.effective_date)<=trunc(ld.logical_date) " +
		" and  (trunc(dps.expiry_date)>trunc(ld.logical_date) " +
		" or   dps.expiry_date is null) " +
		" and  dps.discount_plan_cd=dp.discount_plan_cd " +
		" and  trunc(dp.avail_eff_date)<=trunc(ld.logical_date) " +
		" and  (trunc(dp.avail_exp_date)>trunc(ld.logical_date) " +
		" or   dp.avail_exp_date is null) " +
		" and  ld.logical_date_type='O' " ;
		
		Object[] args = { pricePlanCode.trim(), initialActivation ? "Y" : "N" };
		
		return getJdbcTemplate().query(sql, args, new RowMapper<String>() {
			
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
		}); 
	}
	
	public Collection<ProvinceInfo> retrieveProvinces() {
		return getReferenceDataCollection("Provinces", "", new RowMapper<ProvinceInfo>() {
			
			@Override
			public ProvinceInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				ProvinceInfo info = new ProvinceInfo();

				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3) == null ? rs.getString(2) : rs.getString(3).equals("") ? rs.getString(2) : rs.getString(3));
				info.setCanadaPostCode(rs.getString(4));

				return info;
			}
		});
	}
	
	public Collection<ProvinceInfo> retrieveProvinces(String countryCode) {
		return getReferenceDataCollection("CountryProvinces", countryCode, new RowMapper<ProvinceInfo>() {
			
			@Override
			public ProvinceInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				ProvinceInfo info = new ProvinceInfo();

				info.setCountryCode(rs.getString(1));
				info.setCode(rs.getString(2));
				info.setDescription(rs.getString(3));
				info.setDescriptionFrench(rs.getString(4));

				return info;
			}
		});
	}
	
	public Collection<ProvisioningTransactionStatusInfo> retrieveProvisioningTransactionStatuses() {
		return getReferenceDataCollection("ProvisioningTransactionStatuses", "", new RowMapper<ProvisioningTransactionStatusInfo>() {
			
			@Override
			public ProvisioningTransactionStatusInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				ProvisioningTransactionStatusInfo info = new ProvisioningTransactionStatusInfo();

				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				
				return info;
			}
		});
	}
	
	public Collection<ProvisioningTransactionTypeInfo> retrieveProvisioningTransactionTypes() {
		return getReferenceDataCollection("ProvisioningTransactionTypes", "", new RowMapper<ProvisioningTransactionTypeInfo>() {
			
			@Override
			public ProvisioningTransactionTypeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				ProvisioningTransactionTypeInfo info = new ProvisioningTransactionTypeInfo();

				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3));
				
				return info;
			}
		});
	}
	
	@ExpectedSLA(value=1500)
	public Collection<ServiceInfo> retrieveRegularServices(String featureCategory, String productType, boolean available, boolean current) {

		String featureCategoryClause = "";
		String productTypeClause = "";
		String currentClause = "";
		String availableClause = "";

		if (featureCategory == null || featureCategory == "") {
			featureCategoryClause = " and fcr.feature_code(+) = f.feature_code " ;
		} else {
			featureCategoryClause = " and fcr.feature_code = f.feature_code " + 
			" and category_code = '" + featureCategory + "'" ;
		}

		if (productType != null && !productType.equals("")) {
			productTypeClause = " and s.product_type = '" + productType + "'";
		}

		if (available) {
			availableClause = "and trunc(s.sale_eff_date) <= trunc(sysdate) " +
			" and (trunc(s.sale_exp_date) > trunc(sysdate) " +
			" or s.sale_exp_date is null) " +
			" and s.soc_status = 'A' " +
			" and s.for_sale_ind = 'Y' ";
		}
		
		if (current) {
			currentClause = " and s.current_ind = 'Y'";
		}

		final String sql = "select distinct s.soc " + //1
		" , s.soc_description " + //2
		" , s.soc_description_f " + //3
		" , s.service_type " + //4
		" , nvl(s.minimum_no_months, 0) " + //5
		" , '' " + //6
		" , s.rda_ind " + //7
		" , s.bl_zero_chrg_suppress_ind " + //8
		" , f.feature_code " + //9
		" , f.feature_desc " + //10
		" , f.feature_desc_f " + //11
		" , f.dup_ftr_allow_ind " + //12
		",  f.csm_param_req_ind " + //13
		" , rf.rc_freq_of_pym " + //14
		" , nvl(rc.rate, 0) " + //15
		" , nvl(uc.rate, 0) " + //16
		" , s.product_type " + //17
		" , s.soc_status " + //18
		" , s.for_sale_ind " + //19
		" , s.sale_eff_date " + //20
		" , s.sale_exp_date " + //21
		" , s.current_ind " + //22
		" , decode(s.product_type, 'C', 'Y', s.inc_cel_ftr_ind) " + //23
		" , s.inc_dc_ftr_ind " + //24
		" , s.inc_pds_ftr_ind " + //25
		" , f.msisdn_ind " + //26
		" , nvl(f.ftr_service_type, ' ') " + //27
		" , soc_rel.relation_type " + //28
		" , '' " + //29
		" , decode(uspt.soc,null, 'N', 'Y') promo " + //30
		" , uspt.act_avail_ind " + // 31
		" , uspt.chng_avail_ind " + //32
		" , uspt.included_promo_ind " + //33
		" , s.period_set_code " + //34
		" , fcr.category_code " + //35
		" , brc.soc " + //36
		" , f.switch_code " + //37
		" , ' ' " + //38
		" , s.soc_level_code " + //39
		" , f.product_type " + //40
		" , ucrf.mpc_ind " + //41
		" , ucrf.calling_circle_size " + //42
		" , soc_rel_1.relation_type relation_type_sm" + //43
		" , f.feature_type " + //44
		" , f.pool_group_id " + //45
		" , s.coverage_type " + //46
		" , f.def_sw_params " +
		" , s.bill_cycle_treatment_cd " +
		" , nvl(s.brand_id, 1) brand_id " +	//TBS change
		" , s.soc_service_type " +	//BF 2.0
		" , s.soc_duration_hours " + // RLH support
		" , s.rlh_ind " + // RLH support
		" , s.soc_discount_group " +
		" , nvl(ac.rate, 0) additionalCharge " + // ac rate
		" from pp_rc_rate rc " +
		" , batch_pp_rc_rate brc " +
		" , pp_uc_rate uc " +
		" , pp_ac_rate ac " +
		" , feature f " +
		" , rated_feature rf " +
		" , uc_rated_feature ucrf " +
		" , feature_category_relation fcr " +
		" , soc s " +
		" , (select soc_src, soc_dest, relation_type " +
		"   from soc_relation " +
		"   where relation_type = 'F' " +
		"   union " +
		"   select soc_src, soc_dest, 'W' " +
		"   from soc_relation_ext " +
		"   where relation_type = 'M' " +
		"   union " +
		"   select soc_src, soc_dest, 'X' " +
		"   from soc_relation_ext " +
		"   where relation_type = 'S') soc_rel " +
		" , (select soc_dest, soc_src, relation_type " +
		"   from soc_relation_ext " +
		"   where relation_type in ('M','S')) soc_rel_1 " + 
		" , uc_soc_promo_terms uspt " +
		" where rc.soc(+) = rf.soc  " +
		" and rc.effective_date(+) = rf.effective_date " +
		" and rc.feature_code(+) = rf.feature_code " +
		" and ac.soc(+) = rf.soc  " +
		" and ac.effective_date(+) = rf.effective_date " +
		" and ac.feature_code(+) = rf.feature_code " +
		" and uc.soc(+) = rf.soc " +
		" and uc.effective_date(+) = rf.effective_date " +
		" and uc.feature_code(+) = rf.feature_code  " +
		" and uc.rate_version_num(+) = 0 " +
		" and f.feature_code = rf.feature_code " +
		" and f.feature_group = 'SF' " +
		featureCategoryClause +
		" and rf.soc = s.soc " +
		" and exists (select 1 from soc_equip_relation ser where ser.soc = s.soc)" +		
		" and ucrf.soc(+) = rf.soc " +
		" and ucrf.feature_code(+) = rf.feature_code " +
		" and ucrf.effective_date(+) = rf.effective_date " +
		" and (ucrf.action = (select min(action) " +
		"     from uc_rated_feature ucrf1 " +
		"     where ucrf1.soc(+) = ucrf.soc " +
		"     and ucrf1.effective_date(+) = ucrf.effective_date " +
		"     and ucrf1.feature_code(+) = ucrf.feature_code) " +
		"     or ucrf.action is null) " +
		" and s.service_type != 'P' " +
		productTypeClause +
		availableClause +
		currentClause +
		" and soc_rel.soc_dest(+) = s.soc " +
		" and soc_rel_1.soc_src(+) = s.soc " +  // new  
		" and uspt.soc(+) = s.soc " +
		" and rc.soc = brc.soc(+) " +
		" and rc.feature_code = brc.feature_code(+) " +
		" order by s.soc ";

		List<ServiceInfo> result = getJdbcTemplate().execute(new ConnectionCallback<List<ServiceInfo>>() {
			
			@Override
			public List<ServiceInfo> doInConnection(Connection conn) throws SQLException, DataAccessException {

				PreparedStatement stmt = null;
				ResultSet result = null;
				List<ServiceInfo> services = null;
				try {
					stmt = conn.prepareStatement(sql);
					result = stmt.executeQuery();
					services = extractData(result);
				} finally {
					close(result);
					close(stmt);
				}

				populateServiceExtraProperties(services, conn, new CallableStatementServicePopulator[] { new ServicePdaRimMandatoryGroupPopulator(), new ServiceEquipmentTypePopulator(),
						new ServiceFamilyTypePopulator(), new ServiceDataSharingGroupPopulator() });
				return services;
			}
			
			private List<ServiceInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {

				ArrayList<ServiceInfo> services = new ArrayList<ServiceInfo>();
				ServiceInfo service = new ServiceInfo();
				ArrayList<RatedFeatureInfo> optionalFeatureList = new ArrayList<RatedFeatureInfo>();
				RatedFeatureInfo[] optionalFeatureArray;
				String code = "^";
				HashSet<String> optionalServiceCategoryCodes = new HashSet<String>();

				rs.setFetchSize(FETCH_SIZE);

				while (rs.next()) {
					if (!code.equals(rs.getString(1))) {

						// initialize these values for each soc in the result set
						if (!code.equals("^")) {
							optionalFeatureArray = (RatedFeatureInfo[]) optionalFeatureList.toArray(new RatedFeatureInfo[optionalFeatureList.size()]);
							service.setFeatures(optionalFeatureArray);
							service.setRecurringCharge(calculateServiceRecurringCharge(service.getFeatures0()));
							service.setAdditionalCharge(calculateServiceAdditionalCharge(service.getFeatures0()));
							service.setCategoryCodes((String[]) optionalServiceCategoryCodes.toArray(new String[optionalServiceCategoryCodes.size()]));
							services.add(service);
							service = new ServiceInfo();
							optionalFeatureList = new ArrayList<RatedFeatureInfo>();
							optionalServiceCategoryCodes = new HashSet<String>();
						}

						// service
						service = new ServiceInfo();
						service.setCode(rs.getString(1));
						service.setBillCycleTreatmentCode(rs.getString("bill_cycle_treatment_cd"));
						service.setBrandId(rs.getInt("brand_id"));
						service.setDescription(rs.getString(2));
						service.setDescriptionFrench(rs.getString(3) == null ? rs.getString(2) : rs.getString(3).equals("") ? rs.getString(2) : rs.getString(3));
						service.setServiceType(rs.getString(4));
						service.setTermMonths(rs.getInt(5));
						service.setProductType(rs.getString(17));
						service.setDealerActivation(rs.getString(7) == null ? false : rs.getString(7).equals("Y") ? true : false);
						service.setClientActivation(rs.getString(7) == null ? false : rs.getString(7).equals("Y") ? true : false);
						service.setBillingZeroChrgSuppress(rs.getString(8) == null ? false : rs.getString(8).equals("Y") ? true : false);
						service.setActive(rs.getString(18) == null ? false : rs.getString(18).equals("A") ? true : false);
						service.setForSale(rs.getString(19) == null ? false : rs.getString(19).equals("Y") ? true : false);
						service.setEffectiveDate(rs.getTimestamp(20));
						service.setExpiryDate(rs.getTimestamp(21));
						service.setCurrent(rs.getString(22) == null ? false : rs.getString(22).equals("Y") ? true : false);
						service.setTelephonyFeaturesIncluded(rs.getString(23) == null ? false : rs.getString(23).equals("Y") ? true : false);
						service.setDispatchFeaturesIncluded(rs.getString(24) == null ? false : rs.getString(24).equals("Y") ? true : false);
						service.setWirelessWebFeaturesIncluded(rs.getString(25) == null ? false : rs.getString(25).equals("A") ? true : false);
						service.setBoundServiceAttached("M".equals(rs.getString("relation_type_sm")) ? true : false);
						service.setPromotionAttached(rs.getString(28) == null ? false : rs.getString(28).equals("F") ? true : false);
						service.setBoundService(rs.getString(28) == null ? false : rs.getString(28).equals("W") ? true : false);
						service.setSequentiallyBoundServiceAttached("S".equals(rs.getString("relation_type_sm")) ? true : false);
						service.setSequentiallyBoundService(rs.getString(28) == null ? false : rs.getString(28).equals("X") ? true : false);
						service.setIncludedPromotion(rs.getString(30).equals("Y") && rs.getString(33).equals("Y") ? true : false);
						service.setRecurringChargeFrequency(rs.getInt(14));
						service.setPeriodCode(rs.getString(34));
						service.setHasAlternateRecurringCharge(rs.getString(36) != null);
						service.setLevelCode(rs.getString(39));
						service.setCoverageType(rs.getString("coverage_type"));
						service.setSocServiceType(rs.getString("soc_service_type"));
						service.setDurationServiceHours(rs.getInt("soc_duration_hours"));
						service.setRLH(isRoamLikeHome(rs.getString("rlh_ind")));

						// Fixed a bug
						service.setDiscountAvailable("RG".equalsIgnoreCase(rs.getString("soc_discount_group").trim()) ? true : false);
						
						// feature
						RatedFeatureInfo optionalFeature = mapFeature(rs);
						if (optionalFeature.getCategoryCode() != null) {
							optionalServiceCategoryCodes.add(optionalFeature.getCategoryCode());
						}
						optionalFeatureList.add(optionalFeature);

					} else {

						RatedFeatureInfo optionalFeature = mapFeature(rs);
						if (optionalFeature.getCategoryCode() != null) {
							optionalServiceCategoryCodes.add(optionalFeature.getCategoryCode());
						}
						optionalFeatureList.add(optionalFeature);
					}

					// set these values prior to the next iteration through the result set
					code = rs.getString(1);
				}

				optionalFeatureArray = (RatedFeatureInfo[]) optionalFeatureList.toArray(new RatedFeatureInfo[optionalFeatureList.size()]);
				service.setFeatures(optionalFeatureArray);
				service.setRecurringCharge(calculateServiceRecurringCharge(service.getFeatures0()));
				service.setAdditionalCharge(calculateServiceAdditionalCharge(service.getFeatures0()));
				service.setCategoryCodes((String[]) optionalServiceCategoryCodes.toArray(new String[optionalServiceCategoryCodes.size()]));
				services.add(service);

				return services;
			}
			
			private RatedFeatureInfo mapFeature(ResultSet rs) throws SQLException {

				RatedFeatureInfo optionalFeature = new RatedFeatureInfo();

				optionalFeature.setCode(rs.getString(9));
				optionalFeature.setDescription(rs.getString(10) == null ? null : rs.getString(10).trim());
				optionalFeature.setDescriptionFrench(rs.getString(11) == null ? optionalFeature.getDescription() : rs.getString(11).equals("") ? optionalFeature.getDescription() : rs.getString(11).trim());
				optionalFeature.setDuplFeatureAllowed(rs.getString(12) == null ? true : rs.getString(12).equals("N") ? false : true);
				optionalFeature.setParameterRequired(rs.getString(13) == null ? false : rs.getString(13).equals("Y") ? true : false);
				optionalFeature.setRecurringChargeFrequency(rs.getInt(14));
				optionalFeature.setRecurringCharge(rs.getDouble(15));
				optionalFeature.setUsageCharge(rs.getDouble(16));
				optionalFeature.setAdditionalCharge(rs.getDouble("additionalCharge"));
				optionalFeature.setAdditionalNumberRequired(rs.getString(26) == null ? false : rs.getString(26).equals("Y") ? true : false);
				optionalFeature.setTelephony(rs.getString(27).trim().equals("") || rs.getString(27).equals("C") ? true : false);
				optionalFeature.setDispatch(rs.getString(27).equals("D") || rs.getString(40).equals("H") ? true : false);
				optionalFeature.setWirelessWeb(rs.getString(27).equals("P") ? true : false);
				optionalFeature.setCategoryCode(rs.getString(35));

				optionalFeature.setSwitchCode(rs.getString(37) == null ? "" : rs.getString(37));
				optionalFeature.setMinutePoolingContributor(rs.getString(41) == null ? false : rs.getString(41).equals("Y") ? true : false);
				optionalFeature.setCallingCircleSize(rs.getInt("calling_circle_size"));
				optionalFeature.setFeatureType(rs.getString("feature_type") == null ? "" : rs.getString("feature_type"));
				optionalFeature.setPoolGroupId(rs.getString("pool_group_id"));
				// defect 155131 fix: use column name instead of index due to exclusion of soc_equip_relation.network_type
				optionalFeature.setParameterDefault(rs.getString("def_sw_params"));

				return optionalFeature;
			}

		});
		
		return result;
	}
	
	public Collection<ServiceRelationInfo> retrieveRelations(String serviceCode) {
		
		String sql = " select soc_src , relation_type , sr.expiration_date " +
		" from soc_relation sr , logical_date ld  " +
		" where soc_dest= ?  " +
		" and relation_type='F'  " +
		" AND (   TRUNC (sr.expiration_date) > TRUNC (ld.logical_date) " +
		" OR sr.expiration_date IS NULL ) " +
		" AND ld.logical_date_type = 'O'  " +
		" union  " +
		" select soc_src, DECODE (relation_type,'M','W','S', 'X', null )as relation_type , sr.expiration_date " +
		" from soc_relation_ext sr , logical_date ld " +
		" where soc_dest= ? " +
		" and relation_type in ('M','S') " +
		" AND (   TRUNC (sr.expiration_date) > TRUNC (ld.logical_date) " +
		" OR sr.expiration_date IS NULL ) " +
		" AND ld.logical_date_type = 'O'  " +
		" union " +
		" select soc_dest, relation_type , sr.expiration_date " +
		" from soc_relation_ext sr , logical_date ld " +
		" where soc_src= ? " +
		" and relation_type in ('M','C','S')" +
		" AND (   TRUNC (sr.expiration_date) > TRUNC (ld.logical_date) " +
		" OR sr.expiration_date IS NULL ) " +
		" AND ld.logical_date_type = 'O'  " ;

		Object[] args = {Info.padTo(serviceCode, ' ', 9), Info.padTo(serviceCode, ' ', 9), Info.padTo(serviceCode, ' ', 9)};
		
		return getJdbcTemplate().query(sql, args, new RowMapper<ServiceRelationInfo>() {
			
			@Override
			public ServiceRelationInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				ServiceRelationInfo info = new ServiceRelationInfo();
				
				info.setServiceCode(rs.getString(1));
				info.setType(rs.getString(2));
				info.setExpirationDate(rs.getDate(3));
				
				return info;
			}
		});
	}
	
	public Collection<RouteInfo> retrieveRoutes() {
		return getReferenceDataCollection("Routes", "", new RowMapper<RouteInfo>() {
			
			@Override
			public RouteInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				RouteInfo info = new RouteInfo();
				
				info.setSwitchId(rs.getString(1).trim());
				info.setRouteId(rs.getString(2).trim());
				info.setServeSID(rs.getString(3));
				info.setCity(rs.getString(4));
				info.setCountry(rs.getString(5));
				info.setState(rs.getString(6));
				info.setCode(info.getSwitchId()+"_"+info.getRouteId());
				
				return info;
			}
		});
	}
	
	public Collection<SegmentationInfo> retrieveSegmentations() {
		return getReferenceDataCollection("BanSegments", "", new RowMapper<SegmentationInfo>() {
			
			@Override
			public SegmentationInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				SegmentationInfo info = new SegmentationInfo();
				
				info.setBrandId(rs.getInt(1));
				info.setAccType(rs.getString(2));
				info.setProvince(rs.getString(3));
				info.setSegment(rs.getString(4));
				info.setSubSegment(rs.getString(5));
				
				return info;
			}
		});
	}
	
	public Collection<ServiceExclusionGroupsInfo> retrieveServiceExclusionGroups() {

		String sql = " select sg.soc, sg.gp_soc " +
		"   from soc_group_ext sg, soc_family_group sfg " +
		"   where family_type ='X' " +
		"   and sg.gp_soc=sfg.soc_group " +
		"   order by sg.soc " ;
		
		return getJdbcTemplate().query(sql, new ResultSetExtractor<Collection<ServiceExclusionGroupsInfo>>() {
			
			@Override
			public Collection<ServiceExclusionGroupsInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {
				
				String newCode = "";
				String oldCode = null;

				List<ServiceExclusionGroupsInfo> serviceGroupsInfoList = new ArrayList<ServiceExclusionGroupsInfo>();
				List<String> serviceGroupsList = new ArrayList<String>();

				while (rs.next()) {

					newCode = rs.getString(1);

					if (!newCode.equals(oldCode)) {
						if (oldCode != null) {
							serviceGroupsInfoList.add( populateServiceGroupsInfo(oldCode,serviceGroupsList));
							serviceGroupsList.clear();
						}
						oldCode = newCode;
					}
					serviceGroupsList.add(rs.getString(2));
				}

				serviceGroupsInfoList.add( populateServiceGroupsInfo(oldCode,serviceGroupsList));
				serviceGroupsList.clear();

				return serviceGroupsInfoList;
			}
		});
	}
	
	private ServiceExclusionGroupsInfo populateServiceGroupsInfo(String serviceCode, List<String> serviceGroupsList ) {

		ServiceExclusionGroupsInfo serviceExclusionGroupsInfo = new ServiceExclusionGroupsInfo();
		serviceExclusionGroupsInfo.setCode(serviceCode);
		serviceExclusionGroupsInfo.setExclusionGroups(serviceGroupsList.toArray(new String[serviceGroupsList.size()]));

		return serviceExclusionGroupsInfo;
	}

	public Collection<String> retrieveServiceFamily(String serviceCode, String familyType, String provinceCode, String equipmentType , String networkType, boolean currentServicesOnly, int termInMonths) {
		
		String currentInd = (currentServicesOnly ? "Y" : "N");
		String ignoreEquipment = (equipmentType == null ? "Y" : equipmentType.equals(Equipment.EQUIPMENT_TYPE_ALL) ? "Y" : "N");
		String ignoreProvince = (provinceCode == null ? "Y" : provinceCode.equals(ServiceSummary.PROVINCE_CODE_ALL) ? "Y" : "N");
		String termTables = "";
		String termClause = "";

		if (!(termInMonths == PricePlanSummary.CONTRACT_TERM_ALL)) {
			termTables = ", soc_group sg_term , price_plan_group_term ppgt";
			termClause = "  and sg_term.soc=s_2.soc " + " and ppgt.soc_group(+)=sg_term.gp_soc " + 
			" and (ppgt.term_months = " + termInMonths + "   or  ppgt.term_months is null) " + 
			" and  nvl(ppgt.term_months,s_2.minimum_no_months) = " + termInMonths;
		}

		String sql = " select  distinct sg_2.soc " +
		" from soc s_1, soc_family_group sf " +
		" ,soc_group sg_1,soc_group sg_2 , soc s_2 " +
		" ,soc_submkt_relation ssr, market m " +
		" ,soc_equip_relation ser, logical_date ld " +
		termTables +
		" where s_1.soc= ? " +
		" and  sg_1.soc= s_1.soc " +
		" and  sf.soc_group= sg_1.gp_soc " +
		" and  sf.family_type= ? " +
		" and  sg_2.gp_soc=sf.soc_group " +
		" and  sg_2.soc!=sg_1.soc " +
		" and  s_2.soc=sg_2.soc " +
		" and  s_2.product_type=s_1.product_type " +
		" and  s_2.soc_status='A' " +
		" and   s_2.for_sale_ind='Y' " +
		" and    trunc(s_2.sale_eff_date)<=trunc(ld.logical_date) " +
		" and    (trunc(s_2.sale_exp_date)>trunc(ld.logical_date) " +
		" or      s_2.sale_exp_date is null) " +
		" and    ld.logical_date_type='O' " +
		" and ((s_2.current_ind='Y' and '" + currentInd + "'='Y') " +
		"    or '" + currentInd + "'='N' )" +
		" and ssr.soc=s_2.soc " +
		" and ((   m.province= ? " +
		" and    ssr.sub_market=m.market_code  " +
		" and '" + ignoreProvince + "' = 'N') " +
		" or (   m.province= 'AB' " +
		"  and '" + ignoreProvince + "' = 'Y')) " +
		" and ser.soc=ssr.soc " +
		" and ser.effective_date=ssr.effective_date  " +
		" and ((ser.equipment_type= ? " +
		" or ser.equipment_type ='9') " +
		"  AND (? = '9' OR ser.network_type = ? OR ser.network_type='9')" +
		" and '" + ignoreEquipment + "'='N'   " +
		" or '" + ignoreEquipment + "'='Y' ) " +
		termClause;
		
		Object[] args = {Info.padTo(serviceCode, ' ', 9), familyType, provinceCode, equipmentType, networkType, networkType};
		
		return getJdbcTemplate().queryForList(sql, args, String.class);
	}
	
	public Collection<ServicePolicyInfo> retrieveServicePolicyExceptions() {
		
		String sql = " select soc " +
		"        ,business_role_code" +
		"        ,privilege_code   " +
		"        ,availability_ind " +
		" from   soc_policy " +
		" where effective_date <= sysdate " +
		" and (expiry_date is null or expiry_date > sysdate) ";
		
		return getJdbcTemplate().query(sql, new RowMapper<ServicePolicyInfo>() {
			
			@Override
			public ServicePolicyInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				ServicePolicyInfo info = new ServicePolicyInfo();
				
				info.setServiceCode(rs.getString(1));
				info.setBusinessRoleCode(rs.getString(2));
				info.setPrivilegeCode(rs.getString(3));
				info.setAvailable(rs.getString(4).equals("N") ? false : true);

				return info;
			}
		});
	}
	
	public ServiceUsageInfo retrieveServiceUsageInfo(final String serviceCode) {
		return getJdbcTemplate().execute( new ConnectionCallback<ServiceUsageInfo>() {
			
			@Override
			public ServiceUsageInfo doInConnection(Connection con) throws SQLException, DataAccessException {
				
				ServicePeriodInfo servicePeriod = null ;
				HashMap<String, ServicePeriodInfo> servicePeriodList= new HashMap<String, ServicePeriodInfo>();
				ServicePeriodIncludedMinutesInfo spIncludedMinutes = null;
				ArrayList<ServicePeriodIncludedMinutesInfo> spIncludedMinutesList = new ArrayList<ServicePeriodIncludedMinutesInfo>();
				PreparedStatement stmt = null;
				ResultSet result = null;
				ServicePeriodHoursInfo spHours = null;
				ArrayList<ServicePeriodHoursInfo> spHoursList = new ArrayList<ServicePeriodHoursInfo>();
				ServicePeriodHoursInfo[] spHoursArray =null;
				PreparedStatement stmt2 = null;
				ResultSet result2 = null;
				ServicePeriodUsageRatesInfo spURsInfo = null;
				ArrayList<ServicePeriodUsageRatesInfo> spUsageRatesList = new ArrayList<ServicePeriodUsageRatesInfo>();
				ServicePeriodUsageRatesInfo[] spUsageRatesArray =null;
				UsageRateInfo urInfo = null;
				ArrayList<UsageRateInfo> usageRatesList = new ArrayList<UsageRateInfo>();
				PreparedStatement stmt3 = null;
				ResultSet result3 = null;

				String servicePeriodCode = "^";
				String directionCode = "^";

				ServiceUsageInfo serviceUsageInfo = new ServiceUsageInfo();
				serviceUsageInfo.setServiceCode(serviceCode);

				try {

					String queryStr = " select distinct p.period_value_code, '0', 0 " +
					" from soc s, period p " +
					" where  s.soc = ? " +
					" and s.service_type = 'P' " +
					" and p.period_set_code  = s.period_set_code " +
					" and p.effective_date <= sysdate " +
					" and (p.expiration_date is null or p.expiration_date > sysdate) " +
					" and not exists " +
					"  (select   1  " +
					"  from inclus_by_period ibp " +
					"  where ibp.soc=s.soc " +
					"  and   ibp.period_value_code=p.period_value_code) " +
					" UNION " +
					" select ibp.period_value_code, ibp.action, ibp.inclusive_mou " +
					" from soc s, inclus_by_period ibp " +
					" where  s.soc = ? " +
					" and s.service_type = 'P' " +
					" and   ibp.soc = s.soc  " +
					" and ibp.feature_code='STD' " +
					"    UNION  " +
					" select distinct pur.period_value_code,'0',0 " +
					" from pp_uc_rate pur " +
					" where pur.soc= ?  " +
					" and not exists " +
					" (select 1 " +
					"     from inclus_by_period ibp   " +
					"     where ibp.soc=pur.soc  " +
					"     and   ibp.period_value_code=pur.period_value_code " +
					"     and ibp.feature_code='STD'    ) " +
					" order by 1, 2 ";


					stmt = con.prepareStatement(queryStr);
					stmt.setString(1,Info.padTo(serviceCode, ' ', 9));
					stmt.setString(2,Info.padTo(serviceCode, ' ', 9));
					stmt.setString(3,Info.padTo(serviceCode, ' ', 9));

					result = stmt.executeQuery();

					while (result.next()){
						if ((!servicePeriodCode.equals(result.getString(1))) || servicePeriodCode.equals("^")) {
							if (spIncludedMinutesList.size() > 0) {
								servicePeriod.setServicePeriodIncludedMinutes((ServicePeriodIncludedMinutesInfo[])spIncludedMinutesList.toArray(new ServicePeriodIncludedMinutesInfo[spIncludedMinutesList.size()]));
								servicePeriodList.put(servicePeriod.getCode(), servicePeriod);
								spIncludedMinutesList = new ArrayList<ServicePeriodIncludedMinutesInfo>();
							}
							servicePeriod = new ServicePeriodInfo();
							servicePeriod.setCode(result.getString((1)));
						}
						spIncludedMinutes = new ServicePeriodIncludedMinutesInfo();
						spIncludedMinutes.setDirection(result.getString(2));
						spIncludedMinutes.setIncludedMinutes(result.getInt(3));
						spIncludedMinutesList.add(spIncludedMinutes);

						servicePeriodCode = result.getString(1);
					}
					if (servicePeriod!=null)
					{servicePeriod.setServicePeriodIncludedMinutes((ServicePeriodIncludedMinutesInfo[])spIncludedMinutesList.toArray(new ServicePeriodIncludedMinutesInfo[spIncludedMinutesList.size()]));
					servicePeriodList.put(servicePeriod.getCode(), servicePeriod );
					}
					// Retrieve Period  Days and Hours
					servicePeriodCode = "^";

					String queryStr2 = " select p.period_value_code, p.day_number " +
					" ,to_date(p.from_hour,'hh24miss') " +
					" ,to_date(p.to_hour,'hh24miss') " +
					" from soc s, period p " +
					" where s.period_set_code  = p.period_set_code " +
					" and p.effective_date <= sysdate " +
					" and (p.expiration_date is null or p.expiration_date > sysdate) " +
					" and s.service_type = 'P' " +
					" and s.soc = ? " +
					" order by 1, 2, 3 ";


					stmt2=con.prepareStatement(queryStr2);
					stmt2.setString(1,Info.padTo(serviceCode, ' ', 9));
					result2 = stmt2.executeQuery();

					while (result2.next()){
						// if ((!servicePeriodCode.equals(result2.getString(1)))||servicePeriodCode.equals("^"))
						if (!servicePeriodCode.equals(result2.getString(1)))
						{
							if (spHoursList.size() >0)
							{
								spHoursArray = (ServicePeriodHoursInfo[])(spHoursList.toArray(new ServicePeriodHoursInfo[spHoursList.size()]));
								((ServicePeriodInfo)(servicePeriodList.get(servicePeriodCode))).setServicePeriodHours(spHoursArray);
								spHoursList = new ArrayList<ServicePeriodHoursInfo>();
							}

						}
						spHours  = new ServicePeriodHoursInfo ();
						spHours.setDay(result2.getInt(2));
						spHours.setFrom(result2.getTime(3));
						spHours.setTo(result2.getTime(4));
						spHoursList.add(spHours);

						servicePeriodCode = result2.getString(1);
					}
					spHoursArray = (ServicePeriodHoursInfo[])(spHoursList.toArray(new ServicePeriodHoursInfo[spHoursList.size()]));
					((ServicePeriodInfo)(servicePeriodList.get(servicePeriodCode))).setServicePeriodHours(spHoursArray );

					// Retrieve Period Usage Rates

					servicePeriodCode = "^";

					String queryStr3 = " SELECT distinct PP_UC_RATE.PERIOD_VALUE_CODE  " + //1
					", PP_UC_RATE.ACTION  " + //
					",UC_RATED_FEATURE.UC_USG_DEPEND_CODE " + //3
					",UC_RATED_FEATURE.UC_ROUNDING_FACTOR  " +//4
					",TIER.FROM_QUANTITY  " +//5
					",TIER.TO_QUANTITY  " + // 6
					",PP_UC_RATE.RATE  " + //7
					" FROM    PERIOD_NAME PRDNM1  " +
					",PERIOD_NAME PRDNM2  " +
					",PP_UC_RATE  " +
					",SOC  " +
					",UC_RATED_FEATURE  " +
					",FEATURE  " +
					",TIER  " +
					" WHERE  FEATURE.FEATURE_CODE = UC_RATED_FEATURE.FEATURE_CODE  " +
					" AND	( (  UC_RATED_FEATURE.UC_PERIOD_IND = 'Y'  AND  PP_UC_RATE.PERIOD_VALUE_CODE = PRDNM1.PERIOD_VALUE_CODE )  " +
					"            OR  " +
					"    (  UC_RATED_FEATURE.UC_PERIOD_IND= 'N'  AND  PRDNM1.PERIOD_VALUE_CODE = (SELECT MIN(PERIOD_VALUE_CODE) " +
					"								      FROM PERIOD_NAME) ) ) " +
					" AND 	( (  UC_RATED_FEATURE.UC_COMB_TIER_PRD <> 'C'  AND  UC_RATED_FEATURE.UC_USG_DEPEND_CODE = 'C'  AND  " +
					"  UC_RATED_FEATURE.UC_COMB_TIER_PRD = PRDNM2.PERIOD_VALUE_CODE  ) " +
					"    OR  " +
					"  ( UC_RATED_FEATURE.UC_COMB_TIER_PRD = 'C'  AND UC_RATED_FEATURE.UC_USG_DEPEND_CODE = 'C' " +
					" AND  PRDNM2.PERIOD_VALUE_CODE = (SELECT MIN(PERIOD_VALUE_CODE) FROM PERIOD_NAME ) ) " +
					"    OR " +
					"  (  UC_RATED_FEATURE.UC_USG_DEPEND_CODE <> 'C'  AND " +
					"  PRDNM2.PERIOD_VALUE_CODE = (SELECT MIN(PERIOD_VALUE_CODE) FROM PERIOD_NAME) )  ) " +
					" AND   PP_UC_RATE.SOC = TIER.SOC(+) " +
					" AND   PP_UC_RATE.FEATURE_CODE = TIER.FEATURE_CODE(+) " +
					" AND   PP_UC_RATE.EFFECTIVE_DATE = TIER.EFFECTIVE_DATE(+) " +
					" AND   PP_UC_RATE.ACTION = TIER.ACTION(+) " +
					" AND   PP_UC_RATE.TIER_LEVEL_CODE = TIER.TIER_LEVEL_CODE(+) " +
					" AND   PP_UC_RATE.SOC = UC_RATED_FEATURE.SOC " +
					" AND   PP_UC_RATE.FEATURE_CODE = UC_RATED_FEATURE.FEATURE_CODE  " +
					" AND   PP_UC_RATE.EFFECTIVE_DATE = UC_RATED_FEATURE.EFFECTIVE_DATE " +
					" AND   PP_UC_RATE.ACTION = UC_RATED_FEATURE.ACTION " +
					" AND   PP_UC_RATE.RATE_VERSION_NUM=0 " +
					" AND   SOC.SOC= UC_RATED_FEATURE.SOC " +
					" AND   SOC.EFFECTIVE_DATE = UC_RATED_FEATURE.EFFECTIVE_DATE " +
					" AND   SOC.PERIOD_SET_CODE = PRDNM1.PERIOD_SET_CODE  " +
					" AND   SOC.PERIOD_SET_CODE = PRDNM2.PERIOD_SET_CODE  " +
					" AND   UC_RATED_FEATURE.SOC = ?  " +
					" AND   NVL(TO_CHAR(TRUNC(PRDNM1.EXPIRATION_DATE),'YYYYMMDD'),'99999999') >  " +
					"   TO_CHAR( TRUNC( UC_RATED_FEATURE.EFFECTIVE_DATE),'YYYYMMDD')  " +
					" AND   TRUNC(PRDNM1.EFFECTIVE_DATE) <= TRUNC(UC_RATED_FEATURE.EFFECTIVE_DATE) " +
					" AND  NVL(TO_CHAR(TRUNC(PRDNM2.EXPIRATION_DATE),'YYYYMMDD'),'99999999')> TO_CHAR( TRUNC( UC_RATED_FEATURE.EFFECTIVE_DATE),'YYYYMMDD')  " +
					" AND  TRUNC(PRDNM2.EFFECTIVE_DATE) <= TRUNC(UC_RATED_FEATURE.EFFECTIVE_DATE) " +
					" AND  FEATURE.FEATURE_GROUP = 'SF' " +
					" AND  UC_RATED_FEATURE.FEATURE_CODE = 'STD'  ORDER BY 1,2,5 ";

					stmt3=con.prepareStatement(queryStr3);
					stmt3.setString(1,Info.padTo(serviceCode, ' ', 9));

					result3 = stmt3.executeQuery();

					while (result3.next()){
						// new Service Period
						if (!servicePeriodCode.equals(result3.getString(1)))
						{   if (!servicePeriodCode.equals("^"))
						{spURsInfo.setUsageRates(usageRatesList.toArray(new UsageRateInfo[usageRatesList.size()]));
						spUsageRatesList.add(spURsInfo);
						spUsageRatesArray = spUsageRatesList.toArray(new ServicePeriodUsageRatesInfo[spUsageRatesList.size()]);
						((ServicePeriodInfo)(servicePeriodList.get(servicePeriodCode))).setServicePeriodUsageRates(spUsageRatesArray);
						spUsageRatesList = new ArrayList<ServicePeriodUsageRatesInfo>();
						usageRatesList = new ArrayList<UsageRateInfo>();
						}
						spURsInfo  = new ServicePeriodUsageRatesInfo();
						spURsInfo.setDirection(result3.getString(2));
						spURsInfo.setMethod(result3.getString(3));
						spURsInfo.setUsageRatingFrequency(result3.getInt(4));

						urInfo = new UsageRateInfo();
						urInfo.setFrom(result3.getInt(5));
						urInfo.setTo(result3.getInt(6));
						urInfo.setRate(result3.getDouble(7));
						urInfo.setUsageUnitCode(result3.getString(3)==null ? UsageUnit.VOICE_MINUTE : result3.getString(3).equals("M") ? UsageUnit.VOICE_DOLLAR : UsageUnit.VOICE_MINUTE);
						usageRatesList.add(urInfo);
						}
						//new Direction
						else if (!directionCode.equals(result3.getString(4)))
						{   if (spURsInfo!=null)
						{
							spURsInfo.setUsageRates(usageRatesList.toArray(new UsageRateInfo[usageRatesList.size()]));
							spUsageRatesList.add(spURsInfo);
						}
						spURsInfo  = new ServicePeriodUsageRatesInfo();
						usageRatesList = new ArrayList<UsageRateInfo>();
						spURsInfo.setDirection(result3.getString(2));
						spURsInfo.setMethod(result3.getString(3));
						spURsInfo.setUsageRatingFrequency(result3.getInt(4));

						urInfo = new UsageRateInfo();
						urInfo.setFrom(result3.getInt(5));
						urInfo.setTo(result3.getInt(6));
						urInfo.setRate(result3.getDouble(7));
						urInfo.setUsageUnitCode(result3.getString(3)==null ? UsageUnit.VOICE_MINUTE : result3.getString(3).equals("M") ? UsageUnit.VOICE_DOLLAR : UsageUnit.VOICE_MINUTE);
						usageRatesList.add(urInfo);
						}
						// new Usage Rate
						else
						{
							urInfo = new UsageRateInfo();
							urInfo.setFrom(result3.getInt(5));
							urInfo.setTo(result3.getInt(6));
							urInfo.setRate(result3.getDouble(7));
							urInfo.setUsageUnitCode(result3.getString(3)==null ? UsageUnit.VOICE_MINUTE : result3.getString(3).equals("M") ? UsageUnit.VOICE_DOLLAR : UsageUnit.VOICE_MINUTE);
							usageRatesList.add(urInfo);
						}


						servicePeriodCode = result3.getString(1);
						directionCode = result3.getString(3);
					}
					//
					if (spURsInfo!=null)
					{
						spURsInfo.setUsageRates((UsageRateInfo[])usageRatesList.toArray(new UsageRateInfo[usageRatesList.size()]));
						spUsageRatesList.add(spURsInfo);
						spUsageRatesArray = (ServicePeriodUsageRatesInfo[])(spUsageRatesList.toArray(new ServicePeriodUsageRatesInfo[spUsageRatesList.size()]));
						((ServicePeriodInfo)(servicePeriodList.get(servicePeriodCode))).setServicePeriodUsageRates(spUsageRatesArray);
					}
					serviceUsageInfo.setServicePeriods((ServicePeriodInfo[])servicePeriodList.values().toArray(new ServicePeriodInfo[servicePeriodList.size()]));
				} finally {
					if (result != null)
						result.close();
					if (stmt != null)
						stmt.close();
					if (result2 != null)
						result2.close();
					if (stmt2 != null)
						stmt2.close();
					if (result3 != null)
						result3.close();
					if (stmt3 != null)
						stmt3.close();
				}
				return serviceUsageInfo;
			}
		});
	}
	
	public Collection<SIDInfo> retrieveSIDs() {
		return getReferenceDataCollection("SIDs", "", new RowMapper<SIDInfo>() {
			
			@Override
			public SIDInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				SIDInfo info = new SIDInfo();
				
				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(2));
				info.setState(rs.getString(3));
				info.setCity(rs.getString(4));
				info.setCountry(rs.getString(5));

				return info;
			}
		});
	}
	
	public Collection<SpecialNumberRangeInfo> retrieveSpecialNumberRanges() {
		return getReferenceDataCollection("SpecialNumberRanges", "", new RowMapper<SpecialNumberRangeInfo>() {
			
			@Override
			public SpecialNumberRangeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				SpecialNumberRangeInfo info = new SpecialNumberRangeInfo();
				
				info.setFirstNumberInRange(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3));
				info.setLastNumberInRange(rs.getString(4));
				
				return info;
			}
		});
	}
	
	public Collection<String> retrieveServiceFamilyGroupCodes(String serviceCode, String familyType) {
		
		String sql = " select  soc_group " +
		" from  soc_family_group sf " +
		" , soc_group sg " +
		" where sg.soc= ? " +
		" and  sf.soc_group= sg.gp_soc " +
		" and  sf.family_type= ? " +
		" order by sg.sys_creation_date desc " ;

		return getJdbcTemplate().queryForList(sql, new Object[] {Info.padTo(serviceCode, ' ', 9), familyType}, String.class);
	}
	
	public String retrieveServiceGroupBySocCode(String serviceCode,String familyType) {
		String sql = "SELECT gp_soc FROM soc_group WHERE  soc ='" + serviceCode+ "' and gp_soc = '" + familyType + "'";
		return getJdbcTemplate().query(sql, new ResultSetExtractor<String>() {

			@Override
			public String extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				String gp_soc = null;
				if (rs.next()) {
					gp_soc = rs.getString("gp_soc");
				}
				return gp_soc;
			}
		});
	}
	
	public Collection<SpecialNumberInfo> retrieveSpecialNumbers() {
		return getReferenceDataCollection("SpecialNumbers", "", new RowMapper<SpecialNumberInfo>() {
			
			@Override
			public SpecialNumberInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				SpecialNumberInfo info = new SpecialNumberInfo();
				info.setSpecialNumber(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3));
				
				return info;
			}
		});
	}
	
	public Collection<TalkGroupPriorityInfo> retrieveTalkGroupPriorities() {
		
		String sql = "SELECT PRIORITY_ID, PRIORITY_DESC FROM PRIORITY_SETTINGS WHERE  DISPLAY_IND = 'Y' ";
		
		return getJdbcTemplate().query(sql, new RowMapper<TalkGroupPriorityInfo>() {
			
			@Override
			public TalkGroupPriorityInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				TalkGroupPriorityInfo info = new TalkGroupPriorityInfo();

				info.setCode(String.valueOf(rs.getInt(1)));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(2));
				
				return info;
			}
		});
	}

	public Collection<TaxationPolicyInfo> retrieveTaxPolicies() {
		return getReferenceDataCollection("TaxationPolicy", "", new RowMapper<TaxationPolicyInfo>() {
			
			@Override
			public TaxationPolicyInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				TaxationPolicyInfo info = new TaxationPolicyInfo();
				info.setProvince(rs.getString(1));				
				info.setMethod(rs.getString(4).charAt(0));
				info.setMinimumPSTTaxableAmount(rs.getDouble(5));
				
				String taxType = rs.getString(2);
				
				// GST rate
				if (taxType.equals("G") || taxType.equals("P"))
					info.setGSTRate(rs.getDouble(7));
				else
					info.setGSTRate(0.00);

				// PST rate
				if (taxType.equals("P"))
					info.setPSTRate(rs.getDouble(3));
				else
					info.setPSTRate(0.00);
				
				// HST rate
				if (taxType.equals("H"))
					info.setHSTRate(rs.getDouble(6));
				else
					info.setHSTRate(0.00);		
								
				return info;
			}
		});	
	}	
	
	public Collection<VendorServiceInfo> retrieveVendorServices() {
		return getReferenceDataCollection("VendorServices", "", new RowMapper<VendorServiceInfo>() {
			
			@Override
			public VendorServiceInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				VendorServiceInfo info = new VendorServiceInfo();
				
				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3));
				info.setHistoryUnitOfMeasure(rs.getString(4));
				info.setHistoryCheckPeriod(rs.getInt(5));
				info.setVendorName(rs.getString(6));
				info.isRestrictionRequired(rs.getString(7).toUpperCase().equals("Y") ? true : false);
				info.isNotificationRequired(rs.getString(8).toUpperCase().equals("Y") ? true : false);

				return info;
			}
		});
	}
	
	public Collection<WorkFunctionInfo> retrieveWorkFunctions() {
		
		String sql = "SELECT FUNC_FUNCTION_CODE, FUNC_FUNCTION_DESC, FUNC_FUNCTION_DESC_F, FUNC_DEPARTMENT_CODE FROM FUNCTION";
		
		return getJdbcTemplate().query(sql, new RowMapper<WorkFunctionInfo>() {
			
			@Override
			public WorkFunctionInfo mapRow(ResultSet rs, int rowNum) throws SQLException {

				WorkFunctionInfo info = new WorkFunctionInfo();

				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3));
				info.setDepartmentCode(rs.getString(4));
				
				return info;
			}
		});
	}

	public Collection<WorkFunctionInfo> retrieveWorkFunctions(String departmentCode) {

		String sql = "SELECT FUNC_FUNCTION_CODE, FUNC_FUNCTION_DESC, FUNC_FUNCTION_DESC_F, FUNC_DEPARTMENT_CODE " +
		"FROM FUNCTION WHERE FUNC_DEPARTMENT_CODE = ?";

		return getJdbcTemplate().query(sql, new Object[] {departmentCode}, new RowMapper<WorkFunctionInfo>() {

			@Override
			public WorkFunctionInfo mapRow(ResultSet rs, int rowNum) throws SQLException {

				WorkFunctionInfo info = new WorkFunctionInfo();

				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3));
				info.setDepartmentCode(rs.getString(4));

				return info;
			}
		});
	}

	public DiscountPlanInfo[] retrieveDiscountPlans(boolean current) {
		return getReferenceData("DiscountPlans", current ? "Y" : "N", new ResultSetExtractor<DiscountPlanInfo[]>() {
			
			@Override
			public DiscountPlanInfo[] extractData(ResultSet rs) throws SQLException, DataAccessException {

				List<DiscountPlanInfo> discountPlans = new ArrayList<DiscountPlanInfo>();
				DiscountPlanInfo discountPlanInfo = new DiscountPlanInfo();
				String discountPlanCode = "^";
				int brandId = 0;
				String groupCode = "^";
				List<String> discountGroupList = new ArrayList<String>();
				ArrayList<String> discountBrandIDList = new ArrayList<String>();
				int i;

				while (rs.next()) {
					if (!(rs.getString(1).equals(discountPlanCode))) {
						if (!(discountPlanCode.equals("^"))) {
							discountPlanInfo.setGroupCodes(discountGroupList.toArray( new String[discountGroupList.size()]));
							int [] discountBrandIDs = new int [discountBrandIDList.size()];
							for (i = 0; i < discountBrandIDList.size(); i++) {
								discountBrandIDs[i] = Integer.parseInt((String)discountBrandIDList.get(i));
							}
							discountPlanInfo.setDiscountBrandIDs(discountBrandIDs);
							discountPlans.add(discountPlanInfo);
							discountPlanInfo = new DiscountPlanInfo();
							discountGroupList = new ArrayList<String>();
							discountBrandIDList = new ArrayList<String>();
							brandId=0;
							groupCode = "^";
						}
						discountPlanInfo.setCode(rs.getString(1));
						discountPlanInfo.setDescription(rs.getString(2));
						discountPlanInfo.setDescriptionFrench(rs.getString(3));
						discountPlanInfo.setEffectiveDate(rs.getDate(4));
						discountPlanInfo.setExpiration(rs.getDate(5));
						if ((rs.getString(7)).equals("P")) {
							discountPlanInfo.setPercent(rs.getDouble(6));
						} else if ((rs.getString(7)).equals("D")) {
							discountPlanInfo.setAmount(rs.getDouble(6));
						}
						discountPlanInfo.setMonths(rs.getInt(8));
						discountGroupList.add(rs.getString(9));
						discountPlanInfo.setProductType(rs.getString(10));
						discountPlanInfo.setLevel(rs.getString(11));
						discountBrandIDList.add(String.valueOf(rs.getInt(12)));
					} else {
						if (!groupCode.equals("^") && !discountGroupList.contains(rs.getString(9)))
							discountGroupList.add(rs.getString(9));
						if (brandId != rs.getInt(12) && !discountBrandIDList.contains(String.valueOf(rs.getInt(12))))
							discountBrandIDList.add(String.valueOf(rs.getInt(12)));
					}
					discountPlanCode = rs.getString(1);
					groupCode = rs.getString(9);
					brandId = rs.getInt(12);
				}
				discountPlanInfo.setGroupCodes(discountGroupList.toArray( new String[discountGroupList.size()]));
				int [] discountBrandIDs = new int [discountBrandIDList.size()];
				for (i = 0; i < discountBrandIDList.size(); i++) {
					discountBrandIDs[i] = Integer.parseInt((String)discountBrandIDList.get(i));
				}
				discountPlanInfo.setDiscountBrandIDs(discountBrandIDs);
				discountPlans.add(discountPlanInfo);
				
				return discountPlans.toArray(new DiscountPlanInfo[discountPlans.size()]);
			}
		});
	}
	
	public DiscountPlanInfo[] retrieveDiscountPlans(final boolean current, final String pricePlanCode, final String provinceCode, final long[] productPromoTypes, final boolean initialActivation,
			final int term) {
		String call = "{ ?=call REFERENCE_PKG.RetrievePromoDiscountPlansEnha(?,?,?,?,?,?,?,?,?,?) }";

		return getJdbcTemplate().execute(call, new CallableStatementCallback<DiscountPlanInfo[]>() {

			@Override
			public DiscountPlanInfo[] doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				long startTime = System.currentTimeMillis();

				DiscountPlanInfo discountPlanInfo = new DiscountPlanInfo();
				List<DiscountPlanInfo> discountsList = new ArrayList<DiscountPlanInfo>();
				String initialAct = (initialActivation ? "Y" : "N");
				String ignoreEquipment = "N";
				String ignoreProvince = (provinceCode == null ? "Y" : provinceCode.equals("ALL") ? "Y" : "N");
				String currentInd = (current ? "Y" : "N");
				String termString = "";
				if (!(term == PricePlanSummary.CONTRACT_TERM_ALL)) {
					termString = String.valueOf(term);
				}
				ArrayList<String> discountBrandIDList = new ArrayList<String>();
				String discountPlanCode = "^";
				int brandId = 0;
				int i = 0;

				long type = 99999999;
				long[] productPromoTypeList = productPromoTypes;

				if ((productPromoTypeList == null) || (productPromoTypeList.length == 0)) {
					productPromoTypeList = new long[1];
					productPromoTypeList[0] = type;
				} else if (productPromoTypeList[0] == 0) {
					ignoreEquipment = "Y";
					productPromoTypeList = new long[1];
					productPromoTypeList[0] = type;
				} else {
					ArrayList<String> promoTypes = new ArrayList<String>();
					promoTypes.add(String.valueOf(type));
					for (i = 0; i < productPromoTypeList.length; i++) {
						promoTypes.add(String.valueOf(productPromoTypeList[i]));
					}
					productPromoTypeList = new long[promoTypes.size()];
					for (i = 0; i < productPromoTypeList.length; i++) {
						productPromoTypeList[i] = Long.parseLong((String) promoTypes.get(i));
					}
				}

				ResultSet result = null;

				ArrayDescriptor promoTypesArrayDesc = ArrayDescriptor.createDescriptor("T_PRODUCT_PROMO_TYPE", cs.getConnection());
				ARRAY promoTypesArray = new ARRAY(promoTypesArrayDesc, cs.getConnection(), productPromoTypeList);

				cs.registerOutParameter(1, OracleTypes.NUMBER);
				cs.setString(2, pricePlanCode.trim());
				cs.setString(3, provinceCode != null ? provinceCode.trim() : "");
				cs.setString(4, ignoreProvince);
				cs.setString(5, termString);
				((OracleCallableStatement) cs).setARRAY(6, promoTypesArray);
				cs.setString(7, initialAct);
				cs.setString(8, ignoreEquipment);
				cs.setString(9, currentInd);
				cs.registerOutParameter(10, OracleTypes.CURSOR);
				cs.registerOutParameter(11, OracleTypes.VARCHAR);
				cs.executeQuery();
				long endTime = System.currentTimeMillis();
				logger.debug("REFERENCE_PKG.RetrievePromoDiscountPlansEnha sql execution time [ " + (endTime - startTime) + " ms ] ");

				boolean success = cs.getInt(1) == AccountManager.NUMERIC_TRUE;

				if (success) {
					result = (ResultSet) cs.getObject(10);
				} else {
					throw new SQLException("Strored procedure failed: " + cs.getString(11));
				}
				try {
					while (result.next()) {
						if (!(result.getString(1).equals(discountPlanCode))) {
							if (!(discountPlanCode.equals("^"))) {
								int[] discountBrandIDs = new int[discountBrandIDList.size()];
								for (i = 0; i < discountBrandIDList.size(); i++) {
									discountBrandIDs[i] = Integer.parseInt((String) discountBrandIDList.get(i));
								}
								discountPlanInfo.setDiscountBrandIDs(discountBrandIDs);
								discountsList.add(discountPlanInfo);
								discountPlanInfo = new DiscountPlanInfo();
								discountBrandIDList = new ArrayList<String>();
								brandId = 0;
							}
							discountPlanInfo.setCode(result.getString(1));
							discountPlanInfo.setDescription(result.getString(2));
							discountPlanInfo.setDescriptionFrench(result.getString(3));
							discountPlanInfo.setEffectiveDate(result.getDate(4));
							discountPlanInfo.setExpiration(result.getDate(5));
							if ((result.getString(7)).equals("P")) {
								discountPlanInfo.setPercent(result.getDouble(6));
							} else if ((result.getString(7)).equals("D")) {
								discountPlanInfo.setAmount(result.getDouble(6));
							}
							discountPlanInfo.setMonths(result.getInt(8));
							discountPlanInfo.setOfferExpirationDate(result.getDate(9));
							discountPlanInfo.setPricePlanDiscount(result.getString(10).equals("N") ? false : true);
							discountPlanInfo.setProductType(result.getString(11));
							discountPlanInfo.setLevel(result.getString(12));
							discountBrandIDList.add(String.valueOf(result.getInt(13)));
						} else {
							if (brandId != result.getInt(13) && !discountBrandIDList.contains(String.valueOf(result.getInt(13)))) {
								discountBrandIDList.add(String.valueOf(result.getInt(13)));
							}
						}
						discountPlanCode = result.getString(1);
						brandId = result.getInt(13);
					}
				} finally {
					result.close();
				}

				logger.debug("retrieveDiscountPlans resultset mapping execution time [ " + (System.currentTimeMillis() - endTime) + " ms ] ");

				int[] discountBrandIDs = new int[discountBrandIDList.size()];
				for (i = 0; i < discountBrandIDList.size(); i++) {
					discountBrandIDs[i] = Integer.parseInt((String) discountBrandIDList.get(i));
				}
				if (discountBrandIDList.size() > 0) {
					discountPlanInfo.setDiscountBrandIDs(discountBrandIDs);
					discountsList.add(discountPlanInfo);
				}

				return discountsList.toArray(new DiscountPlanInfo[discountsList.size()]);
			}
		});
	}
	
	public PricePlanInfo retrievePricePlan(String pricePlanCode) {
		
		if ( !AppConfiguration.isTallboyComboPlanRollback() ) {
			logger.debug("CALLING retrievePricePlanByInlineSql(pricePlanCode)"); 
			return retrievePricePlanByInlineSql(pricePlanCode);
		} 	
		String call = "{ call Price_Plan_utility_pkg.GetPricePlan (?,?) }";
		
		final String code = Info.padTo(pricePlanCode, ' ', 9);
		
		PricePlanInfo pricePlan = null;
		
		try {
			pricePlan = getJdbcTemplate().execute(call, new CallableStatementCallback<PricePlanInfo>() {
				
				@Override
				public PricePlanInfo doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
	
					PricePlanInfo pricePlan = new PricePlanInfo();
					String serviceTypeIndicator = "^";
					ArrayList<RatedFeatureInfo> pricePlanFeatureList = new ArrayList<RatedFeatureInfo>();
					RatedFeatureInfo[] pricePlanFeatureArray;
					double pricePlanRC = 0;
					double minimumCommitmentAmout = 0;
					HashSet<String> pricePlanCategoryCodes = new HashSet<String>();
					ServiceInfo[] optionalServicesArray = new ServiceInfo[0];
	
					cs.setString(1, code);
					cs.registerOutParameter(2, ORACLE_REF_CURSOR);
					cs.execute();
					
					ResultSet result = (ResultSet) cs.getObject(2);
	
					try {
						
						while (result.next()) {
							// pricePlan
							if (result.getString("service_type_ind").equals("P")) {
	
								if (!serviceTypeIndicator.equals("P")) {
	
									pricePlan.setServiceType(result.getString("service_type"));
									pricePlan.setCode(result.getString("soc"));
									pricePlan.setBillCycleTreatmentCode(result.getString("bill_cycle_treatment_cd"));
									pricePlan.setDescription(result.getString("soc_description") == null ? null : result.getString("soc_description").trim());
									pricePlan.setDescriptionFrench(result.getString("soc_description_f") == null ? pricePlan.getDescription() : result.getString("soc_description_f").equals("") ? pricePlan.getDescription() : result.getString("soc_description_f"));
									pricePlan.setTermMonths(result.getInt("minimum_no_months"));
									pricePlan.setDealerActivation( result.getString("dealer_available_ind").equals("Y"));
									pricePlan.setClientActivation( result.getString("client_available_ind").equals("Y"));
									pricePlan.setAvailableForActivation(result.getString("act_avail_ind").equals("Y"));
									pricePlan.setAvailableForChange(result.getString("chng_avail_ind").equals("Y"));
									pricePlan.setBillingZeroChrgSuppress(result.getString("bl_zero_chrg_suppress_ind") == null ? false : result.getString("bl_zero_chrg_suppress_ind").equals("Y"));
									pricePlan.setProductType(result.getString("s_product_type"));
									pricePlan.setActive(result.getString("soc_status") == null ? false : result.getString("soc_status").equals("A"));
									pricePlan.setForSale(result.getString("for_sale_ind") == null ? false : result.getString("for_sale_ind").equals("Y"));
									pricePlan.setEffectiveDate(result.getTimestamp("sale_eff_date"));
									pricePlan.setExpiryDate(result.getTimestamp("sale_exp_date"));
									pricePlan.setCurrent(result.getString("current_ind") == null ? false :result.getString("current_ind").equals("Y"));
									pricePlan.setTelephonyFeaturesIncluded(result.getString("telephony_features_inc") == null ? false : result.getString("telephony_features_inc").equals("Y"));
									pricePlan.setDispatchFeaturesIncluded(result.getString("dispatch_features_inc") == null ? false : result.getString("dispatch_features_inc").equals("Y"));
									pricePlan.setWirelessWebFeaturesIncluded(result.getString("wireless_web_features_inc") == null ? false : result.getString("wireless_web_features_inc").equals("A"));
									pricePlan.setDiscountAvailable(result.getString("discount_available") == null ? false : result.getString("discount_available").equals("Y"));
									pricePlan.setLevelCode(result.getString("soc_level_code") == null ? "" : result.getString("soc_level_code"));
									pricePlan.setAvailableForChangeByClient(result.getString("client_chng_avail_ind") == null ? false : result.getString("client_chng_avail_ind").equals("Y"));
									pricePlan.setAvailableForChangeByDealer(result.getString("dealer_chng_avail_ind") == null ? false : result.getString("dealer_chng_avail_ind").equals("Y"));
									pricePlan.setAvailableToModifyByClient(result.getString("client_modify_avail_ind") == null ? false : result.getString("client_modify_avail_ind").equals("Y"));
									pricePlan.setAvailableToModifyByDealer(result.getString("dealer_modify_avail_ind") == null ? false : result.getString("dealer_modify_avail_ind").equals("Y"));
									pricePlan.setAvailableForCorporateRenewal(result.getString("corporate_renewal_ind") == null ? false : result.getString("corporate_renewal_ind").equals("Y"));
									pricePlan.setAvailableForNonCorporateRenewal(result.getString("non_corporate_renewal_ind") == null ? false : result.getString("non_corporate_renewal_ind").equals("Y"));
									pricePlan.setAvailableForRetailStoreActivation(result.getString("ares_avail_ind") == null ? false : result.getString("ares_avail_ind").equals("Y"));
									pricePlan.setAvailableForCorporateStoreActivation(result.getString("csa_avail_ind") == null ? false : result.getString("csa_avail_ind").equals("Y"));
									pricePlan.setPeriodCode(result.getString("period_set_code"));
									pricePlan.setUserSegment(result.getString("soc_def_user_seg"));
									pricePlan.setMinimumUsageCharge(result.getDouble("min_req_usg_chrg"));
									pricePlan.setSuspensionPricePlan(result.getString("suspension_price_plan").equals("N")? false : true);
									pricePlan.setBrandId(result.getInt("brand_ind"));
									pricePlan.setCoverageType(result.getString("coverage_type"));
									pricePlan.setSeatType(result.getString("seat_type"));
									pricePlan.setSocServiceType(result.getString("soc_service_type"));
									pricePlan.setDurationServiceHours(result.getInt("soc_duration_hours"));
									pricePlan.setRLH(isRoamLikeHome(result.getString("rlh_ind")));
									pricePlan.setComboGroupId(result.getString("combo_group_id"));
									
									if (result.getString("aom_avail_ind") == null) {
										pricePlan.setAOMPricePlan(false);
									} else {
										pricePlan.setAOMPricePlan(result.getString("aom_avail_ind").equals("Y"));
									}
									
									String minPooling = result.getString("mp_ind");
									if (minPooling != null) {
										pricePlan.setMinutePoolingCapable(minPooling.equals("Y"));
									} else {
										pricePlan.setMinutePoolingCapable(false);
									}
	
									minimumCommitmentAmout = result.getDouble("min_commit_amt");
	
									String dollarPooling = result.getString("mci_ind");
									if (dollarPooling != null && minimumCommitmentAmout > 0) {
										if (pricePlan.getProductType().equals(Subscriber.PRODUCT_TYPE_PCS)) {
											pricePlan.setDollarPoolingCapable(dollarPooling.equals("I"));
										} else if (pricePlan.getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN)) {
											pricePlan.setDollarPoolingCapable(dollarPooling.equals("I") || dollarPooling.equals("C"));
										} else {
											pricePlan.setDollarPoolingCapable(false);
										}
	
									} else {
										pricePlan.setDollarPoolingCapable(false);
									}	
	
									RatedFeatureInfo pricePlanFeature = mapFeature(result);
									
									if (pricePlanFeature.getCategoryCode() != null) {
										pricePlanCategoryCodes.add(pricePlanFeature.getCategoryCode());
									}
									pricePlanFeatureList.add(pricePlanFeature);
	
									if (pricePlanFeature.getCode().equals("STD   ")) {
										pricePlan.setUsageRatingFrequency(result.getInt("uc_rounding_factor"));
										pricePlan.setRecurringChargeFrequency(result.getInt("rc_freq_of_pym"));
									}
	
								} else {
	
									RatedFeatureInfo pricePlanFeature = mapFeature(result);
									
									if (pricePlanFeature.getCategoryCode() != null) {
										pricePlanCategoryCodes.add(pricePlanFeature.getCategoryCode());
									}
									pricePlanFeatureList.add(pricePlanFeature);
									
									if (pricePlanFeature.getCode().equals("STD   ")) {
										pricePlan.setUsageRatingFrequency(result.getInt("uc_rounding_factor"));
										pricePlan.setRecurringChargeFrequency(result.getInt("rc_freq_of_pym"));
									}
								}
	
								pricePlanRC += result.getDouble("rc_rate");
							}
	
							serviceTypeIndicator = result.getString("service_type_ind");
							pricePlanFeatureArray = (RatedFeatureInfo[]) pricePlanFeatureList.toArray(new RatedFeatureInfo[pricePlanFeatureList.size()]);
							pricePlan.setFeatures(pricePlanFeatureArray);
							pricePlan.setRecurringCharge(pricePlanRC + minimumCommitmentAmout);
							pricePlan.setCategoryCodes((String[])pricePlanCategoryCodes.toArray(new String[pricePlanCategoryCodes.size()]));
	
							pricePlan.setOptionalServices(optionalServicesArray);
	
							if ((pricePlan.getCode() == null) || pricePlan.getCode().equals("")) {
								return null;
							}
	
							if (pricePlan.isSharable()) {
								pricePlan.setMaximumSubscriberCount(result.getInt("max_subscribers_number"));
								pricePlan.setSecondarySubscriberService(result.getString("secondary_soc"));
							}	
						}
	
					} finally {
						result.close();
					}
					
					populateServiceExtraProperties(Arrays.asList( new ServiceInfo[] {pricePlan } ), cs.getConnection() );
					
					return pricePlan;
				}
				
				private RatedFeatureInfo mapFeature( ResultSet result ) throws SQLException {
					
					RatedFeatureInfo pricePlanFeature = new RatedFeatureInfo();
					pricePlanFeature.setCode(result.getString("feature_code"));
					pricePlanFeature.setDescription(result.getString("feature_desc") == null ? null : result.getString("feature_desc").trim());
					pricePlanFeature.setDescriptionFrench(result.getString("feature_desc_f") == null ? pricePlanFeature.getDescription() : result.getString("feature_desc_f").equals("") ? pricePlanFeature.getDescription() : result.getString("feature_desc_f").trim());
					pricePlanFeature.setDuplFeatureAllowed(result.getString("dup_ftr_allow_ind") == null ? true : result.getString("dup_ftr_allow_ind").equals("N") ? false : true);
					pricePlanFeature.setParameterRequired(result.getString("csm_param_req_ind") == null ? false : result.getString("csm_param_req_ind").equals("Y") ? true : false);
					pricePlanFeature.setRecurringChargeFrequency(result.getInt("rc_freq_of_pym"));
					pricePlanFeature.setRecurringCharge(result.getDouble("rc_rate"));
					pricePlanFeature.setUsageCharge(result.getDouble("uc_rate"));
					pricePlanFeature.setAdditionalNumberRequired(result.getString("msisdn_ind") == null ? false : result.getString("msisdn_ind").equals("Y") ? true : false);
					pricePlanFeature.setTelephony(result.getString("ftr_service_type").trim().equals("") || result.getString("ftr_service_type").equals("C") ? true : false);
					pricePlanFeature.setDispatch(result.getString("ftr_service_type").equals("D") || result.getString("f_product_type").equals("H") ? true : false);
					pricePlanFeature.setWirelessWeb(result.getString("ftr_service_type").equals("P") ? true : false);
					pricePlanFeature.setSwitchCode(result.getString("switch_code") == null ? "" : result.getString("switch_code") );
					pricePlanFeature.setCategoryCode(result.getString("category_code"));
					pricePlanFeature.setMinutePoolingContributor(result.getString("mpc_ind") == null ? false :	result.getString("mpc_ind").equals("Y") ? true : false);
					pricePlanFeature.setCallingCircleSize(result.getInt("calling_circle_size") );
					pricePlanFeature.setFeatureType(result.getString("feature_type") == null ? "" : result.getString("feature_type"));
					pricePlanFeature.setPoolGroupId(result.getString("pool_group_id"));
					pricePlanFeature.setParameterDefault(result.getString("def_sw_params"));
					
					return pricePlanFeature;
				}
			});
			
		} catch (DataAccessException dae) {
			//for logging purpose only. we re-throw the error until a better design is in-place
			logger.error("DataAccessException occured in retrievePricePlan with code=[" + code + "]. " + dae.getMessage());
			throw dae;
		}
		
		if (pricePlan != null) {
			pricePlan.setIncludedServices(retrieveIncludedServices(code));
		}
		
		return pricePlan;
		
	}
	
	/**
	 * Alternative way to retrieve PricePlan via In-line sql 
	 * replacing Price_Plan_utility_pkg.GetPricePlan.
	 * Note : new SOC column "combo_group_id" has been added for June 2019 Tallboy project. 
	 */
	private PricePlanInfo retrievePricePlanByInlineSql(String pricePlanCode) {

		final String code = Info.padTo(pricePlanCode, ' ', 9);

		List<PricePlanInfo> pricePlanResults = null;
		PricePlanInfo pricePlanInfo = null;
		
		final String getPricePlanSql = "SELECT DISTINCT s.service_type, "
                + "s.soc, "
                + "s.soc_description, "
                + "s.soc_description_f, "
                + "Nvl (s.minimum_no_months, 0) AS minimum_no_months, "
                + "s.rda_ind, "
                + "s.bl_zero_chrg_suppress_ind, "
                + "psg.client_available_ind, "
                + "psg.dealer_available_ind, "
                + "psg.act_avail_ind, "
                + "psg.chng_avail_ind, "
                + "s.soc_status, "
                + "s.for_sale_ind, "
                + "s.sale_eff_date, "
                + "s.sale_exp_date, "
                + "s.current_ind,"
                + "Decode (s.product_type, 'C', 'Y', s.inc_cel_ftr_ind) AS telephony_features_inc, "
                + "s.inc_dc_ftr_ind AS dispatch_features_inc, "
                + "s.inc_pds_ftr_ind AS wireless_web_features_inc, "
                + "s.product_type, "
                + "Nvl (s.min_commit_amt, 0) min_commit_amt, "
                + "s.soc_level_code, "
                + "psg.client_chng_avail_ind, "
                + "psg.dealer_chng_avail_ind, "
                + "psg.client_modify_avail_ind, "
                + "psg.dealer_modify_avail_ind, "
                + "psg.corporate_renewal_ind, "
                + "psg.non_corporate_renewal_ind, "
                + "s.period_set_code, "
                + "Nvl (s.soc_def_user_seg, 'PERSONAL  ') soc_def_user_seg, "
                + "psg.ares_avail_ind, "
                + "psg.csa_avail_ind, "
                + "s.min_req_usg_chrg, "
                + "s.mp_ind, "
                + "s.brand_id, "
                + "psg.aom_avail_ind, "
                + "s.mci_ind, "
                + "s.coverage_type, "
                + "s.bill_cycle_treatment_cd, "
                + "s.seat_type, "
                + "s.soc_service_type, "
                + "s.soc_duration_hours, "
                + "s.rlh_ind, "
                + "s.combo_group_id, "
                + "Decode (dps.soc, null, 'N', 'Y') AS discount_available_ind, "
                + "Decode (sarc.soc, null, 'N', 'Y') AS suspension_priceplan_ind, "
                + "NVL (spp.max_subscribers_number, 0) AS shareable_pp_max_subs_number, "
                + "spp.secondary_soc AS shareable_pp_secondary_soc "
                + "FROM   soc s LEFT JOIN soc_discount_plan dps ON dps.soc = s.soc "
                + "LEFT JOIN soc_activity_reason_code sarc ON sarc.soc = s.soc, "
                + "soc_group sg LEFT JOIN shareable_price_plan spp ON spp.price_plan_group = sg.gp_soc, "
                + "priceplan_soc_groups psg "
                + "WHERE  s.soc = ? "
                + "AND sg.soc = s.soc "
                + "AND psg.price_plan_group = sg.gp_soc "
                + "AND EXISTS (SELECT 1 "
                             + "FROM soc_group sg1, priceplan_soc_groups psg1 "
                             + "WHERE sg1.soc = ? AND psg1.price_plan_group = sg1.gp_soc)";
		
		final String getPricePlanSqlWhenFirstSqlReturnNone = "SELECT DISTINCT s.service_type, "
                + "s.soc, "
                + "s.soc_description, "
                + "s.soc_description_f, "
                + "NVL (s.minimum_no_months, 0) minimum_no_months, "
                + "s.rda_ind, "
                + "s.bl_zero_chrg_suppress_ind, "
                + "'' client_available_ind, "
                + "'' dealer_available_ind, "
                + "'' act_avail_ind, "
                + "'' chng_avail_ind, "
                + "s.soc_status, "
                + "s.for_sale_ind, "
                + "s.sale_eff_date, "
                + "s.sale_exp_date, "
                + "s.current_ind, "
                + "Decode (s.product_type, 'C', 'Y', s.inc_cel_ftr_ind) telephony_features_inc, "
                + "s.inc_dc_ftr_ind AS dispatch_features_inc, "
                + "s.inc_pds_ftr_ind AS wireless_web_features_inc, "
                + "s.product_type, "
                + "NVL (s.min_commit_amt, 0) min_commit_amt, "
                + "s.soc_level_code, "
                + "'' client_chng_avail_ind, "
                + "'' dealer_chng_avail_ind, "
                + "'' client_modify_avail_ind, "
                + "'' dealer_modify_avail_ind, "
                + "'' corporate_renewal_ind, "
                + "'' non_corporate_renewal_ind, "
                + "s.period_set_code, "
                + "NVL (s.soc_def_user_seg, 'PERSONAL  ') soc_def_user_seg, "
                + "'' ares_avail_ind, "
                + "'' csa_avail_ind, "
                + "s.min_req_usg_chrg, "
                + "s.mp_ind, "
                + "s.brand_id, "
                + "'' aom_avail_ind, "
                + "s.mci_ind, "
                + "s.coverage_type, "
                + "s.bill_cycle_treatment_cd, "
                + "s.seat_type, "
                + "s.soc_service_type, "
                + "s.soc_duration_hours, "
                + "s.rlh_ind, "
                + "s.combo_group_id, "
                + "Decode (dps.soc, null, 'N', 'Y') AS discount_available_ind, "
                + "Decode (sarc.soc, null, 'N', 'Y') AS suspension_priceplan_ind, "
                + "Nvl (spp.max_subscribers_number, 0) AS shareable_pp_max_subs_number, "
                + "spp.secondary_soc AS shareable_pp_secondary_soc "
                + "FROM   soc s  LEFT JOIN soc_discount_plan dps ON dps.soc = s.soc "
                + "LEFT JOIN soc_activity_reason_code sarc ON sarc.soc = s.soc, "
                + "soc_group sg LEFT JOIN shareable_price_plan spp "
                + "ON spp.price_plan_group = sg.gp_soc "
                + "WHERE  s.soc = ?  "
                + "AND NOT EXISTS (SELECT 1 "
                                 + "FROM   soc_group sg1, priceplan_soc_groups psg1 "
                                 + "WHERE  sg1.soc = ?  AND psg1.price_plan_group = sg1.gp_soc)";
		
		final String getFeatureSql = "SELECT DISTINCT f.feature_code, "
                + "f.feature_desc, "
                + "f.feature_desc_f, "
                + "f.dup_ftr_allow_ind, "
                + "f.csm_param_req_ind, "
                + "rf.rc_freq_of_pym, "
                + "NVL (rc.rate, 0) recurring_charge, "
                + "Decode (uc.rate, uc.rate, 0) usage_charge, "
                + "fcr.category_code, "
                + "f.msisdn_ind, "
                + "NVL (f.ftr_service_type, ' ') AS ftr_service_type, "
                + "f.switch_code, "
                + "f.product_type, "
                + "urf.uc_rounding_factor, "
                + "urf.mpc_ind, "
                + "urf.calling_circle_size, "
                + "f.feature_type, "
                + "f.pool_group_id, "
                + "f.def_sw_params "
                + "FROM   rated_feature rf, "
                + "feature f, "
                + "pp_rc_rate rc, "
                + "uc_rated_feature urf, "
                + "pp_uc_rate uc, "
                + "feature_category_relation fcr "
                + "WHERE  rf.soc = ? "
                + "AND f.feature_code = rf.feature_code "
                + "AND f.feature_group = 'SF' "
                + "AND fcr.feature_code(+) = rf.feature_code "
                + "AND rc.soc(+) = rf.soc "
                + "AND rc.effective_date(+) = rf.effective_date "
                + "AND rc.feature_code(+) = rf.feature_code "
                + "AND uc.soc(+) = rf.soc "
                + "AND uc.effective_date(+) = rf.effective_date "
                + "AND uc.feature_code(+) = rf.feature_code "
                + "AND uc.rate_version_num(+) = 0 "
                + "AND urf.soc(+) = rf.soc "
                + "AND urf.effective_date(+) = rf.effective_date "
                + "AND urf.feature_code(+) = rf.feature_code "
                + "AND ( urf.action IS NULL  "
                + "OR urf.action = (SELECT Min (action) "
                                  + "FROM   uc_rated_feature urf1 "
                                  + "WHERE  urf1.soc(+) = urf.soc "
                                         + "AND urf1.effective_date(+) = urf.effective_date "
                                         + "AND urf1.feature_code(+) = urf.feature_code) ) "
                + "ORDER  BY 1";  

		pricePlanResults = getJdbcTemplate().execute(new ConnectionCallback<List<PricePlanInfo>>() {
			@Override
			public List<PricePlanInfo> doInConnection(Connection conn) throws SQLException, DataAccessException {
				List<PricePlanInfo> pricePlans = null;
				List<RatedFeatureInfo> features = null;
				PreparedStatement stmtForPricePlan1 = null;
				ResultSet resultForPricePlan1 = null;
				PreparedStatement stmtForPricePlan2 = null;
				ResultSet resultForPricePlan2 = null;
				PreparedStatement stmtForFeatures = null;
				ResultSet resultForFeatures = null;
				final PricePlanFeatureCrossInfo pricePlanFeatureCrossInfo = new PricePlanFeatureCrossInfo();
				try {
					stmtForPricePlan1 = conn.prepareStatement(getPricePlanSql);
					stmtForPricePlan1.setString(1, code);
					stmtForPricePlan1.setString(2, code);
					resultForPricePlan1 = stmtForPricePlan1.executeQuery();
					pricePlans = fetchPriceplan(resultForPricePlan1);
					if (pricePlans.isEmpty()) {
						stmtForPricePlan2 = conn.prepareStatement(getPricePlanSqlWhenFirstSqlReturnNone);
						stmtForPricePlan2.setString(1, code);
						stmtForPricePlan2.setString(2, code);
						resultForPricePlan2 = stmtForPricePlan2.executeQuery();
						pricePlans = fetchPriceplan(resultForPricePlan2);
					}
					if ( !pricePlans.isEmpty() ) {
						stmtForFeatures = conn.prepareStatement(getFeatureSql);
						stmtForFeatures.setString(1, code);
						resultForFeatures = stmtForFeatures.executeQuery();
						features = fetchFeatures(pricePlanFeatureCrossInfo, resultForFeatures);
					}
				} finally {
					close(resultForPricePlan1);
					close(resultForPricePlan2);
					close(resultForFeatures);
					close(stmtForPricePlan1);
					close(stmtForPricePlan2);
					close(stmtForFeatures);
				}
				if ( !pricePlans.isEmpty() ) {
					pricePlans.get(0).setRecurringCharge(pricePlans.get(0).getRecurringCharge() +  pricePlanFeatureCrossInfo.getRecurringCharge());
					pricePlans.get(0).setUsageRatingFrequency(pricePlanFeatureCrossInfo.getUsageRatingFrequency());
					pricePlans.get(0).setRecurringChargeFrequency(pricePlanFeatureCrossInfo.getRecurringChargeFrequency());
					if ( !features.isEmpty() ) {
						pricePlans.get(0).setFeatures((RatedFeatureInfo[]) features.toArray(new RatedFeatureInfo[features.size()]));
					}
					populateServiceExtraProperties(Arrays.asList( new ServiceInfo[] {pricePlans.get(0)} ), conn);
				}
				return pricePlans;
			}

			private List<PricePlanInfo> fetchPriceplan(ResultSet rs) throws SQLException {
				List<PricePlanInfo> pricePlanInfos = new ArrayList<PricePlanInfo>();
				while (rs.next()) {
					PricePlanInfo pricePlan = getPricePlanRow(rs);
					if (pricePlan != null) {
						pricePlanInfos.add(pricePlan);
					}
				}
				return pricePlanInfos;
			}

			private List<RatedFeatureInfo> fetchFeatures(final PricePlanFeatureCrossInfo pricePlanFeatureCrossInfo, ResultSet rs) throws SQLException {
				List<RatedFeatureInfo> featureInfos = new ArrayList<RatedFeatureInfo>();
				while (rs.next()) {
					featureInfos.add(getFeatureForPriceplanRow(pricePlanFeatureCrossInfo, rs));
				}
				return featureInfos;
			}

		});

		if ( !pricePlanResults.isEmpty() ) {
			pricePlanInfo = pricePlanResults.get(0);
			pricePlanInfo.setIncludedServices(retrieveIncludedServices(code));
		}
		
		return pricePlanInfo;
	}	

	/**
	 * map each row of priceplan data in the ResultSet and return the corresponding PricePlanInfo
	 * @throws SQLException
	 */
	private PricePlanInfo getPricePlanRow(ResultSet result) throws SQLException {
		String code = result.getString("soc");
		if ( code == null || code.equals("") ) {
			return null;
		}
		PricePlanInfo pricePlan = new PricePlanInfo();
		pricePlan.setServiceType(result.getString("service_type"));
		pricePlan.setCode(code);
		pricePlan.setBillCycleTreatmentCode(result.getString("bill_cycle_treatment_cd"));
		pricePlan.setDescription(result.getString("soc_description") == null ? null : result.getString("soc_description").trim());
		pricePlan.setDescriptionFrench(result.getString("soc_description_f") == null ? pricePlan.getDescription() : result.getString("soc_description_f").equals("") ? pricePlan.getDescription() : result.getString("soc_description_f"));
		pricePlan.setTermMonths(result.getInt("minimum_no_months"));
		pricePlan.setDealerActivation( result.getString("dealer_available_ind") == null ? false : result.getString("dealer_available_ind").equals("Y"));
		pricePlan.setClientActivation( result.getString("client_available_ind") == null ? false : result.getString("client_available_ind").equals("Y"));
		pricePlan.setAvailableForActivation(result.getString("act_avail_ind") == null ? false : result.getString("act_avail_ind").equals("Y"));
		pricePlan.setAvailableForChange(result.getString("chng_avail_ind") == null ? false : result.getString("chng_avail_ind").equals("Y"));
		pricePlan.setBillingZeroChrgSuppress(result.getString("bl_zero_chrg_suppress_ind") == null ? false : result.getString("bl_zero_chrg_suppress_ind").equals("Y"));
		pricePlan.setProductType(result.getString("product_type"));
		pricePlan.setActive(result.getString("soc_status") == null ? false : result.getString("soc_status").equals("A"));
		pricePlan.setForSale(result.getString("for_sale_ind") == null ? false : result.getString("for_sale_ind").equals("Y"));
		pricePlan.setEffectiveDate(result.getTimestamp("sale_eff_date"));
		pricePlan.setExpiryDate(result.getTimestamp("sale_exp_date"));
		pricePlan.setCurrent(result.getString("current_ind") == null ? false :result.getString("current_ind").equals("Y"));
		pricePlan.setTelephonyFeaturesIncluded(result.getString("telephony_features_inc") == null ? false : result.getString("telephony_features_inc").equals("Y"));
		pricePlan.setDispatchFeaturesIncluded(result.getString("dispatch_features_inc") == null ? false : result.getString("dispatch_features_inc").equals("Y"));
		pricePlan.setWirelessWebFeaturesIncluded(result.getString("wireless_web_features_inc") == null ? false : result.getString("wireless_web_features_inc").equals("A"));
		pricePlan.setDiscountAvailable(result.getString("discount_available_ind").equals("Y") ? true : false);
		pricePlan.setLevelCode(result.getString("soc_level_code") == null ? "" : result.getString("soc_level_code"));
		pricePlan.setAvailableForChangeByClient(result.getString("client_chng_avail_ind") == null ? false : result.getString("client_chng_avail_ind").equals("Y"));
		pricePlan.setAvailableForChangeByDealer(result.getString("dealer_chng_avail_ind") == null ? false : result.getString("dealer_chng_avail_ind").equals("Y"));
		pricePlan.setAvailableToModifyByClient(result.getString("client_modify_avail_ind") == null ? false : result.getString("client_modify_avail_ind").equals("Y"));
		pricePlan.setAvailableToModifyByDealer(result.getString("dealer_modify_avail_ind") == null ? false : result.getString("dealer_modify_avail_ind").equals("Y"));
		pricePlan.setAvailableForCorporateRenewal(result.getString("corporate_renewal_ind") == null ? false : result.getString("corporate_renewal_ind").equals("Y"));
		pricePlan.setAvailableForNonCorporateRenewal(result.getString("non_corporate_renewal_ind") == null ? false : result.getString("non_corporate_renewal_ind").equals("Y"));
		pricePlan.setAvailableForRetailStoreActivation(result.getString("ares_avail_ind") == null ? false : result.getString("ares_avail_ind").equals("Y"));
		pricePlan.setAvailableForCorporateStoreActivation(result.getString("csa_avail_ind") == null ? false : result.getString("csa_avail_ind").equals("Y"));
		pricePlan.setPeriodCode(result.getString("period_set_code"));
		pricePlan.setUserSegment(result.getString("soc_def_user_seg"));
		pricePlan.setMinimumUsageCharge(result.getDouble("min_req_usg_chrg"));
		pricePlan.setSuspensionPricePlan(result.getString("suspension_priceplan_ind").equals("N") ? false : true);
		pricePlan.setBrandId(result.getInt("brand_id"));
		pricePlan.setCoverageType(result.getString("coverage_type"));
		pricePlan.setSeatType(result.getString("seat_type"));
		pricePlan.setSocServiceType(result.getString("soc_service_type"));
		pricePlan.setDurationServiceHours(result.getInt("soc_duration_hours"));
		pricePlan.setRLH(isRoamLikeHome(result.getString("rlh_ind")));
		pricePlan.setComboGroupId(result.getString("combo_group_id"));
		
		if (result.getString("aom_avail_ind") == null) {
			pricePlan.setAOMPricePlan(false);
		} else {
			pricePlan.setAOMPricePlan(result.getString("aom_avail_ind").equals("Y"));
		}
		String minPooling = result.getString("mp_ind");
		if (minPooling != null) {
			pricePlan.setMinutePoolingCapable(minPooling.equals("Y"));
		} else {
			pricePlan.setMinutePoolingCapable(false);
		}
		
		double minimumCommitmentAmout = result.getDouble("min_commit_amt");

		String dollarPooling = result.getString("mci_ind");
		if (dollarPooling != null && minimumCommitmentAmout > 0) {
			if (pricePlan.getProductType().equals(Subscriber.PRODUCT_TYPE_PCS)) {
				pricePlan.setDollarPoolingCapable(dollarPooling.equals("I"));
			} else if (pricePlan.getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN)) {
				pricePlan.setDollarPoolingCapable(dollarPooling.equals("I") || dollarPooling.equals("C"));
			} else {
				pricePlan.setDollarPoolingCapable(false);
			}
		} else {
			pricePlan.setDollarPoolingCapable(false);
		}			

		pricePlan.setRecurringCharge(minimumCommitmentAmout);//Will be added/summed-up by Feature.getRecurringCharge()
		
		if (pricePlan.isSharable()) {
			pricePlan.setMaximumSubscriberCount(result.getInt("shareable_pp_max_subs_number"));
			pricePlan.setSecondarySubscriberService(result.getString("shareable_pp_secondary_soc"));
		}	
		
		return pricePlan;
	}

	/**
	 * map each row of feature data in the ResultSet and return the corresponding RatedFeatureInfo.
	 * @throws SQLException
	 */
	private RatedFeatureInfo getFeatureForPriceplanRow(final PricePlanFeatureCrossInfo pricePlanFeatureCrossInfo,
			ResultSet result) throws SQLException {
		RatedFeatureInfo pricePlanFeature = new RatedFeatureInfo();
		pricePlanFeature.setCode(result.getString("feature_code"));
		pricePlanFeature.setDescription(result.getString("feature_desc") == null ? null : result.getString("feature_desc").trim());
		pricePlanFeature.setDescriptionFrench(result.getString("feature_desc_f") == null ? pricePlanFeature.getDescription() : result.getString("feature_desc_f").equals("") ? pricePlanFeature.getDescription() : result.getString("feature_desc_f").trim());
		pricePlanFeature.setDuplFeatureAllowed(result.getString("dup_ftr_allow_ind") == null ? true : result.getString("dup_ftr_allow_ind").equals("N") ? false : true);
		pricePlanFeature.setParameterRequired(result.getString("csm_param_req_ind") == null ? false : result.getString("csm_param_req_ind").equals("Y"));
		pricePlanFeature.setRecurringChargeFrequency(result.getInt("rc_freq_of_pym"));
		pricePlanFeature.setRecurringCharge(result.getDouble("recurring_charge"));
		pricePlanFeature.setUsageCharge(result.getDouble("usage_charge"));
		pricePlanFeature.setAdditionalNumberRequired(result.getString("msisdn_ind") == null ? false : result.getString("msisdn_ind").equals("Y"));
		pricePlanFeature.setTelephony(result.getString("ftr_service_type").trim().equals("") || result.getString("ftr_service_type").equals("C"));
		pricePlanFeature.setDispatch(result.getString("ftr_service_type").equals("D") || result.getString("product_type").equals("H"));
		pricePlanFeature.setWirelessWeb(result.getString("ftr_service_type").equals("P"));
		pricePlanFeature.setSwitchCode(result.getString("switch_code") == null ? "" : result.getString("switch_code") );
		pricePlanFeature.setCategoryCode(result.getString("category_code"));
		pricePlanFeature.setMinutePoolingContributor(result.getString("mpc_ind") == null ? false :	result.getString("mpc_ind").equals("Y"));
		pricePlanFeature.setCallingCircleSize(result.getInt("calling_circle_size") );
		pricePlanFeature.setFeatureType(result.getString("feature_type") == null ? "" : result.getString("feature_type"));
		pricePlanFeature.setPoolGroupId(result.getString("pool_group_id"));
		pricePlanFeature.setParameterDefault(result.getString("def_sw_params"));						
		if (pricePlanFeature.getCode().equals("STD   ")) {
			pricePlanFeatureCrossInfo.setUsageRatingFrequency(result.getInt("uc_rounding_factor"));
			pricePlanFeatureCrossInfo.setRecurringChargeFrequency(result.getInt("rc_freq_of_pym"));
		}
		if (pricePlanFeature.getCategoryCode() != null) {
			pricePlanFeatureCrossInfo.add(pricePlanFeature.getCategoryCode());
		}
		pricePlanFeatureCrossInfo.addRecurringCharge(pricePlanFeature.getRecurringCharge());
		
		return pricePlanFeature;
	}

	/**
	 * Responsible for holding cross-reference info between PricePlanInfo and RatedFeatureInfo. 
	 */
	private class PricePlanFeatureCrossInfo {
		private int usageRatingFrequency;
		private int recurringChargeFrequency;
		private double recurringCharge;
		private HashSet<String> pricePlanCategoryCodes = new HashSet<String>();

		public void setUsageRatingFrequency(int frequency) {
			usageRatingFrequency = frequency;			
		}
		public void setRecurringChargeFrequency(int frequency) {
			recurringChargeFrequency = frequency;			
		}
		public int getUsageRatingFrequency() {
			return usageRatingFrequency;
		}
		public int getRecurringChargeFrequency() {
			return recurringChargeFrequency;
		}
		public void add(String categoryCode) {
			pricePlanCategoryCodes.add(categoryCode);			
		}
		public void addRecurringCharge(double recurringCharge) {
			this.recurringCharge += recurringCharge;			
		}
		public double getRecurringCharge() {
			return recurringCharge;
		}
	}

	public ServiceInfo[] retrieveIncludedServices(String pricePlanCode) {
		
		final String code = Info.padTo(pricePlanCode, ' ', 9);
		
		String call = "{ call Price_Plan_utility_pkg.GetIncludedServices (?,?) }";
		
		return getJdbcTemplate().execute(call, new CallableStatementCallback<ServiceInfo[]>() {
			
			@Override
			public ServiceInfo[] doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {

				String serviceCode = "^";
				double includedServiceRC = 0;

				ArrayList<ServiceInfo> includedServicesList = new ArrayList<ServiceInfo>();
				ArrayList<RatedFeatureInfo> includedFeatureList = new ArrayList<RatedFeatureInfo>();
				RatedFeatureInfo[] includedFeatureArray;
				ServiceInfo includedService = new ServiceInfo();
				HashSet<String> includedServiceCategoryCodes = new HashSet<String>();

				cs.setString(1, code);
				cs.registerOutParameter(2, ORACLE_REF_CURSOR);
				cs.setFetchSize(FETCH_SIZE);
				cs.execute();
				
				ResultSet result = (ResultSet) cs.getObject(2);
				
				try {
					while (result.next()) {

						if (!serviceCode.equals(result.getString("soc"))) {
							
							// initialize these values for each soc in the result set
							if (!serviceCode.equals("^")) {
								includedFeatureArray = (RatedFeatureInfo[])includedFeatureList.toArray(new RatedFeatureInfo[includedFeatureList.size()]);
								includedService.setFeatures(includedFeatureArray);
								includedService.setRecurringCharge(includedServiceRC);
								includedService.setCategoryCodes((String[])includedServiceCategoryCodes.toArray(new String[includedServiceCategoryCodes.size()]));
								includedServicesList.add(includedService);
								includedService = new ServiceInfo();
								includedFeatureList = new ArrayList<RatedFeatureInfo>();
								includedServiceRC = 0;
								includedServiceCategoryCodes = new HashSet<String>();
							}
							
							// service
							includedService.setServiceType(result.getString("service_type"));
							includedService.setCode(result.getString("soc"));
							includedService.setBillCycleTreatmentCode (result.getString("bill_cycle_treatment_cd"));
							includedService.setBrandId(result.getInt("brand_ind"));
							includedService.setDescription(result.getString("soc_description") == null ? null : result.getString("soc_description").trim());
							includedService.setDescriptionFrench(result.getString("soc_description_f") == null ? includedService.getDescription() : result.getString("soc_description_f").equals("") ? includedService.getDescription() : result.getString("soc_description_f").trim());
							includedService.setTermMonths(result.getString("promo").equals("Y") ? result.getInt("duration") : result.getInt("minimum_no_months"));
							includedService.setDealerActivation(true);
							includedService.setClientActivation(true);
							includedService.setRecurringChargeFrequency(result.getInt("rc_freq_of_pym"));
							includedService.setBillingZeroChrgSuppress(result.getString("bl_zero_chrg_suppress_ind") == null ? false : result.getString("bl_zero_chrg_suppress_ind").equals("Y") ? true : false );
							includedService.setProductType(result.getString("s_product_type"));
							includedService.setActive(result.getString("soc_status") == null ? false : result.getString("soc_status").equals("A") ? true :false);
							includedService.setForSale(result.getString("for_sale_ind") == null ? false : result.getString("for_sale_ind").equals("Y") ? true :false);
							includedService.setEffectiveDate(result.getTimestamp("sale_eff_date"));
							includedService.setExpiryDate(result.getTimestamp("sale_exp_date"));
							includedService.setCurrent(result.getString("current_ind") == null ? false : result.getString("current_ind").equals("Y") ? true :false);
							includedService.setTelephonyFeaturesIncluded(result.getString("telephony_features_inc") == null ? false : result.getString("telephony_features_inc").equals("Y") ? true :false);
							includedService.setDispatchFeaturesIncluded(result.getString("dispatch_features_inc") == null ? false : result.getString("dispatch_features_inc").equals("Y") ? true :false);
							includedService.setWirelessWebFeaturesIncluded(result.getString("wireless_web_features_inc") == null ? false : result.getString("wireless_web_features_inc").equals("A") ? true :false);
							includedService.setBoundServiceAttached( StringUtil.emptyFromNull(result.getString("has_bound")).equals("Y") ? true : false);
							includedService.setPromotionAttached( StringUtil.emptyFromNull(result.getString("has_promotion")).equals("Y") ? true : false);
							includedService.setBoundService( StringUtil.emptyFromNull(result.getString("is_bound")).equals("Y") ? true : false);
							includedService.setSequentiallyBoundServiceAttached( StringUtil.emptyFromNull(result.getString("has_seq_bound")).equals("Y") ? true : false);
							includedService.setSequentiallyBoundService( StringUtil.emptyFromNull(result.getString("is_seq_bound")).equals("Y") ? true : false);
							includedService.setPeriodCode(result.getString("period_set_code"));
							includedService.setHasAlternateRecurringCharge(result.getString("brc_soc") != null);
							includedService.setCoverageType(result.getString("coverage_type"));
							includedService.setPDAMandatoryGroup(StringUtil.emptyFromNull(result.getString("pda_mandatory_ind")).equals("Y") ? true : false);
							includedService.setRIMMandatoryGroup(StringUtil.emptyFromNull(result.getString("rim_mandatory_ind")).equals("Y") ? true : false);
							includedService.setSocServiceType(result.getString("soc_service_type"));
							includedService.setDurationServiceHours(result.getInt("soc_duration_hours"));
							includedService.setRLH(isRoamLikeHome(result.getString("rlh_ind")));
							
							// feature
							RatedFeatureInfo includedFeature = mapFeature( result ); 
							includedServiceRC += includedFeature.getRecurringCharge();
							if (includedFeature.getCategoryCode()!=null) { 
								includedServiceCategoryCodes.add(includedFeature.getCategoryCode());
							}
							includedFeatureList.add(includedFeature);
							
						} else {
							RatedFeatureInfo includedFeature = mapFeature( result ); 
							includedServiceRC += includedFeature.getRecurringCharge();
							if (includedFeature.getCategoryCode()!=null) { 
								includedServiceCategoryCodes.add(includedFeature.getCategoryCode());
							}
							
							includedFeatureList.add(includedFeature);
						}
						// set this value prior to the next iteration through the result set
						serviceCode = result.getString("soc");
					}
				} finally {
					result.close();
				}
				
				if (!(includedService.getCode()).equals("")) {
					includedFeatureArray = (RatedFeatureInfo[])includedFeatureList.toArray(new RatedFeatureInfo[includedFeatureList.size()]);
					includedService.setFeatures(includedFeatureArray);
					includedService.setRecurringCharge(includedServiceRC);
					includedService.setCategoryCodes((String[])includedServiceCategoryCodes.toArray(new String[includedServiceCategoryCodes.size()]));
					includedServicesList.add(includedService);
				}

				populateServiceExtraProperties(includedServicesList, cs.getConnection() );
				
				return includedServicesList.toArray(new ServiceInfo[includedServicesList.size()]);
			}
			
			private RatedFeatureInfo mapFeature(ResultSet result) throws SQLException {
				
				RatedFeatureInfo includedFeature = new RatedFeatureInfo();
				includedFeature.setCode(result.getString("feature_code"));
				includedFeature.setDescription(result.getString("feature_desc") == null ? null : result.getString("feature_desc").trim());
				includedFeature.setDescriptionFrench(result.getString("feature_desc_f") == null ? includedFeature.getDescription() : result.getString("feature_desc_f").equals("") ? includedFeature.getDescription() : result.getString("feature_desc_f").trim());
				includedFeature.setDuplFeatureAllowed(result.getString("dup_ftr_allow_ind") == null ? true : result.getString("dup_ftr_allow_ind").equals("N") ? false : true);
				includedFeature.setParameterRequired(result.getString("csm_param_req_ind") == null ? false : result.getString("csm_param_req_ind").equals("Y") ? true : false);
				includedFeature.setRecurringChargeFrequency(result.getInt("rc_freq_of_pym"));
				includedFeature.setRecurringCharge(result.getDouble("rc_rate"));
				includedFeature.setUsageCharge(result.getDouble("uc_rate"));
				includedFeature.setAdditionalNumberRequired(result.getString("msisdn_ind") == null ? false :result.getString("msisdn_ind").equals("Y") ? true : false);
				includedFeature.setTelephony(result.getString("ftr_service_type").trim().equals("") || result.getString("ftr_service_type").equals("C") ? true : false);
				includedFeature.setDispatch(result.getString("ftr_service_type").equals("D") || result.getString("f_product_type").equals("H") ? true : false);
				includedFeature.setWirelessWeb(result.getString("ftr_service_type").equals("P") ? true : false);
				includedFeature.setSwitchCode(result.getString("switch_code") == null ? "" : result.getString("switch_code"));
				// set category
				includedFeature.setCategoryCode(result.getString("category_code"));
				includedFeature.setMinutePoolingContributor(result.getString("mpc_ind") == null ? false : result.getString("mpc_ind").equals("Y") ? true : false);
				includedFeature.setCallingCircleSize(result.getInt("calling_circle_size"));
				includedFeature.setFeatureType(result.getString("feature_type") == null ? "" : result.getString("feature_type"));
				includedFeature.setPoolGroupId(result.getString("pool_group_id"));
				includedFeature.setParameterDefault(result.getString("def_sw_params"));
				
				return includedFeature;
			}
		});
	}

	public ServiceInfo[] retrieveOptionalServices( String pricePlanCode, final String equipmentType, 
			final String provinceCode, final char accountType, final char accountSubType, final String networkType) {
		
		final String ppSoc = Info.padTo(pricePlanCode, ' ', 9);

		String call = "{ call Price_Plan_utility_pkg.GetOptionalServices(?, ?, ?, ?, ?, ?, ?) }";
		
		return getJdbcTemplate().execute(call, new CallableStatementCallback<ServiceInfo[]>() {
			
			@Override
			public ServiceInfo[] doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
			
				String serviceCode = "^";
				String featureCode = "^";
				ArrayList<ServiceInfo> optionalServicesList = new ArrayList<ServiceInfo>();
				ArrayList<RatedFeatureInfo> optionalFeatureList = new ArrayList<RatedFeatureInfo>();
				RatedFeatureInfo[] optionalFeatureArray;
				ServiceInfo optionalService = new ServiceInfo();
				double optionalServiceRC=0;
				HashSet<String> optionalServiceCategoryCodes = new HashSet<String>();

				cs.setString(1, ppSoc);
				cs.setString(2, equipmentType);
				cs.setString(3, networkType);
				cs.setString(4, provinceCode);
				cs.setString(5, String.valueOf(accountType));
				cs.setString(6, String.valueOf(accountSubType));
				cs.registerOutParameter(7, ORACLE_REF_CURSOR);
				
				cs.execute();
				ResultSet result = (ResultSet) cs.getObject(7);
				
				try {
					
					while (result.next()) {
						if (!serviceCode.equals(result.getString("soc"))) {
							
							// initialize these values for each soc in the result set
							if (!serviceCode.equals("^")) {
								optionalFeatureArray = (RatedFeatureInfo[])optionalFeatureList.toArray(
										new RatedFeatureInfo[optionalFeatureList.size()]);
								optionalService.setFeatures(optionalFeatureArray);
								optionalService.setRecurringCharge(optionalServiceRC);
								optionalService.setCategoryCodes((String[])optionalServiceCategoryCodes.toArray(
										new String[optionalServiceCategoryCodes.size()]));
								optionalServicesList.add(optionalService);
								optionalService = new ServiceInfo();
								optionalFeatureList = new ArrayList<RatedFeatureInfo>();
								optionalServiceRC = 0;
								optionalServiceCategoryCodes = new HashSet<String>();
							}
							
							// service
							optionalService.setServiceType(result.getString("service_type"));
							optionalService.setCode(result.getString("soc"));
							optionalService.setBillCycleTreatmentCode (result.getString("bill_cycle_treatment_cd"));
							optionalService.setBrandId(result.getInt("brand_ind"));
							optionalService.setDescription(result.getString("soc_description") == null ? null : result.getString("soc_description").trim());
							optionalService.setDescriptionFrench(result.getString("soc_description_f") == null ?
									optionalService.getDescription() : result.getString("soc_description_f").equals("") ? 
											optionalService.getDescription() : result.getString("soc_description_f"));
							optionalService.setTermMonths(result.getString("promo").equals("Y") ? 0 : result.getInt("minimum_no_months"));
							optionalService.setDealerActivation(result.getString("promo").equals("Y") ? true :
								result.getString("promo").equals("N") && result.getString("rda_ind").equals("Y") ? true : false);
							optionalService.setClientActivation(result.getString("promo").equals("Y") ? true :
								result.getString("promo").equals("N") && result.getString("eserve_ind").equals("Y") ? true : false);
							optionalService.setCoverageType(result.getString("coverage_type"));
							optionalService.setProductType(result.getString("s_product_type"));
							optionalService.setIncludedPromotion(false);
							optionalService.setActive(result.getString("soc_status") == null ? false : result.getString("soc_status").equals("A") ? true : false);
							optionalService.setBillingZeroChrgSuppress(result.getString("bl_zero_chrg_suppress_ind") == null ?
									false : result.getString("bl_zero_chrg_suppress_ind").equals("Y") ? true : false );
							optionalService.setForSale(result.getString("for_sale_ind") == null ? false : result.getString("for_sale_ind").equals("Y") ? true : false);
							optionalService.setEffectiveDate(result.getTimestamp("sale_eff_date"));
							optionalService.setExpiryDate(result.getTimestamp("sale_exp_date"));
							optionalService.setCurrent(result.getString("current_ind") == null ? false : result.getString("current_ind").equals("Y") ? true : false);
							optionalService.setTelephonyFeaturesIncluded(result.getString("telephony_features_inc") == null ?
									false :	result.getString("telephony_features_inc").equals("Y") ? true : false);
							optionalService.setDispatchFeaturesIncluded(result.getString("dispatch_features_inc") == null ? 
									false : result.getString("dispatch_features_inc").equals("Y") ? true : false);
							optionalService.setWirelessWebFeaturesIncluded(result.getString("wireless_web_features_inc") == null ?
									false : result.getString("wireless_web_features_inc").equals("A") ? true : false);							
							optionalService.setBoundServiceAttached(StringUtil.emptyFromNull(result.getString("has_bound")).equals("Y") ? true : false);
							optionalService.setPromotionAttached(StringUtil.emptyFromNull(result.getString("has_promotion")).equals("Y") ? true : false);
							optionalService.setBoundService(StringUtil.emptyFromNull(result.getString("is_bound")).equals("Y") ? true : false);
							optionalService.setSequentiallyBoundServiceAttached(StringUtil.emptyFromNull(result.getString("has_seq_bound")).equals("Y") ? true : false);
							optionalService.setSequentiallyBoundService(StringUtil.emptyFromNull(result.getString("is_seq_bound")).equals("Y") ? true : false);
							optionalService.setRecurringChargeFrequency(result.getInt("rc_freq_of_pym"));
							optionalService.setPeriodCode(result.getString("period_set_code"));
							optionalService.setHasAlternateRecurringCharge(result.getString("brc_soc") != null);
							optionalService.setLevelCode(result.getString("soc_level_code"));
							optionalService.setMandatory(StringUtil.emptyFromNull(result.getString("mandatory")).equals("Y") ? true : false);
							optionalService.setPDAMandatoryGroup(StringUtil.emptyFromNull(result.getString("pda_mandatory_ind")).equals("Y") ? true : false);
							optionalService.setRIMMandatoryGroup(StringUtil.emptyFromNull(result.getString("rim_mandatory_ind")).equals("Y") ? true : false);
							optionalService.setSocServiceType(result.getString("soc_service_type"));
							optionalService.setDurationServiceHours(result.getInt("soc_duration_hours")); // sets '0' for all null values
							optionalService.setRLH(isRoamLikeHome(result.getString("rlh_ind")));
							
							// feature
							RatedFeatureInfo optionalFeature = mapFeature( result);
							optionalServiceRC += optionalFeature.getRecurringCharge();
							if (optionalFeature.getCategoryCode() != null) {
								optionalServiceCategoryCodes.add(optionalFeature.getCategoryCode());
							}
							optionalFeatureList.add(optionalFeature);
							
						} else if (!featureCode.equals(result.getString("feature_code"))) {
							
							RatedFeatureInfo optionalFeature = mapFeature( result);
							optionalServiceRC += optionalFeature.getRecurringCharge();
							if (optionalFeature.getCategoryCode() != null) {
								optionalServiceCategoryCodes.add(optionalFeature.getCategoryCode());
							}
							optionalFeatureList.add(optionalFeature);
						}

						// set these values prior to the next iteration through the result set
						featureCode = result.getString("feature_code");
						serviceCode = result.getString("soc");
					}
					
					if (!(optionalService.getCode()).equals("")) {
						optionalFeatureArray = (RatedFeatureInfo[])optionalFeatureList.toArray(new RatedFeatureInfo[optionalFeatureList.size()]);
						optionalService.setFeatures(optionalFeatureArray);
						optionalService.setRecurringCharge(optionalServiceRC);
						optionalService.setCategoryCodes((String[])optionalServiceCategoryCodes.toArray(new String[optionalServiceCategoryCodes.size()]));
						optionalServicesList.add(optionalService);
					}
					
				} finally {
					result.close();
				}
				
				populateServiceExtraProperties( optionalServicesList, cs.getConnection() );
				
				return optionalServicesList.toArray(new ServiceInfo[optionalServicesList.size()]);
			}
			
			private RatedFeatureInfo mapFeature(ResultSet result) throws SQLException {
				
				RatedFeatureInfo optionalFeature = new RatedFeatureInfo();
				optionalFeature.setCode(result.getString("feature_code"));
				optionalFeature.setDescription(result.getString("feature_desc") == null ? null : result.getString("feature_desc").trim());
				optionalFeature.setDescriptionFrench(result.getString("feature_desc_f") == null ? optionalFeature.getDescription() : result.getString("feature_desc_f").equals("") 
						? optionalFeature.getDescription() : result.getString("feature_desc_f").trim());
				optionalFeature.setDuplFeatureAllowed(result.getString("dup_ftr_allow_ind") == null ? true : result.getString("dup_ftr_allow_ind").equals("N") ? false : true);
				optionalFeature.setParameterRequired(result.getString("csm_param_req_ind") == null ? false : result.getString("csm_param_req_ind").equals("Y") ? true : false);
				optionalFeature.setRecurringChargeFrequency(result.getInt("rc_freq_of_pym"));
				// set recurring charge
				optionalFeature.setRecurringCharge(result.getDouble("rc_rate"));
				optionalFeature.setUsageCharge(result.getDouble("uc_rate"));
				// set category
				optionalFeature.setCategoryCode(result.getString("category_code"));
				optionalFeature.setAdditionalNumberRequired(result.getString("msisdn_ind") == null ? false : result.getString("msisdn_ind").equals("Y") ? true : false);
				optionalFeature.setTelephony(result.getString("ftr_service_type").trim().equals("") || result.getString("ftr_service_type").equals("C") ? true : false);
				optionalFeature.setDispatch(result.getString("ftr_service_type").equals("D") || result.getString("f_product_type").equals("H") ? true : false);
				optionalFeature.setWirelessWeb(result.getString("ftr_service_type").equals("P") ? true : false);
				optionalFeature.setSwitchCode(result.getString("switch_code") == null ? "" : result.getString("switch_code"));
				optionalFeature.setMinutePoolingContributor(result.getString("mpc_ind") == null ? false : result.getString("mpc_ind").equals("Y") ? true : false);
				optionalFeature.setCallingCircleSize(result.getInt("calling_circle_size"));
				optionalFeature.setFeatureType(result.getString("feature_type") == null ? "" : result.getString("feature_type"));
				optionalFeature.setPoolGroupId(result.getString("pool_group_id"));
				optionalFeature.setParameterDefault(result.getString("def_sw_params"));
				
				return optionalFeature;				
			}
		});
	}
	
	public PricePlanInfo retrievePricePlan(String pricePlanCode, String equipmentType, 
			String provinceCode, char accountType, char accountSubType, int brandId) {
	
		PricePlanInfo pricePlan = retrievePricePlan(pricePlanCode);
		
		if (pricePlan != null) {
			pricePlan.setOptionalServices(retrieveOptionalServices(pricePlanCode, 
					equipmentType, provinceCode, accountType, accountSubType, NetworkType.NETWORK_TYPE_ALL));
		}
		
		return pricePlan;
	}
	
	public Collection<PricePlanInfo> retrievePricePlanList(String productType, String equipmentType, String provinceCode, 
			char accountType, char accountSubType, int brandId, boolean currentPricePlansOnly, boolean availableForActivationOnly, 
			int term, String activityCode, String activityReasonCode, String networkType, String seatTypeCode) {
	
		return retrievePricePlanList(productType, equipmentType, provinceCode, accountType, accountSubType, 
				brandId, new long[0], true, currentPricePlansOnly, availableForActivationOnly, 
				term, activityCode, activityReasonCode, networkType, seatTypeCode, null, null);
	}
	
	/**
	 * Note : new SOC column "combo_group_id" has been added for June 2019 Tallboy project.
	 *  
	 */
	public Collection<PricePlanInfo> retrievePricePlanList(final String productType, String equipmentType, String provinceCode,  
			char accountType, char accountSubType, int pBrandId, long[] pProductPromoTypeList, 
			boolean initialActivation, boolean currentPricePlansOnly, boolean availableForActivationOnly, 
			int term, String activityCode, String activityReasonCode, String networkType, String seatTypeCode, 
			String[] socGroups, String[] offerGroupCodes) {
		
		String initialAct = (initialActivation ? "Y" : "N");
		String psgptTable = "";
		String psgptClause = "";
		String exceptionNotExist = "";
		String termTables = "";
		String termClause = "";
		String activityReasonCodeClause = "";
		String brandIdClause = "";
		String networkTypeClause = "";
		String seatTypeCodeClause = "";
		String socGroupClause = getSocGroupFilterSQLClauseForGetPricePlanList(socGroups);
		String offerSelectClause = getOfferGroupSelectSQLClauseForGetPricePlanList(offerGroupCodes);
		String availableForActivationIndClause = "";
		String currentIndClause = "";

		if (activityReasonCode == null) {
			activityReasonCode = "";
		}

		if (activityCode == null) {
			activityCode = "";
		}
		
		if (!activityReasonCode.trim().equals("") && !activityCode.trim().equals("")) {
			activityReasonCodeClause = " and sarc.soc = s.soc " +
			" and sarc.activity_code = '" + Info.padTo(activityCode, ' ', 3) + "'" +
			" and sarc.activity_reason_code = '" + Info.padTo(activityReasonCode, ' ', 4) + "'";
		} else {
			activityReasonCodeClause =  " and sarc.soc(+) = s.soc ";
		}
		
	
		int i = 0;
		String productPromoTypeList = "";
		
		if (pProductPromoTypeList == null || pProductPromoTypeList.length == 0) {
			exceptionNotExist = " and not exists " +
			" (select 1 " +
			" from soc_group sg1 " +
			" , priceplan_soc_groups psg1 " +
			" , priceplan_soc_gp_product_type psgpt " +
			" where sg1.soc = s.soc " +
			" and psg1.price_plan_group = sg1.gp_soc " +
			" and psgpt.priceplan_soc_group_id = psg1.priceplan_soc_group_id " +
			" and psgpt.include_ind = 'Y') " ;

		} else {
			psgptTable = " , priceplan_soc_gp_product_type psgpt ";

			for (i = 0; i < pProductPromoTypeList.length; i++) {
				productPromoTypeList = productPromoTypeList + ((i == 0) ? "" :"," ) + "'" + String.valueOf(pProductPromoTypeList[i] + "'");
			}
			psgptClause = " and psgpt.priceplan_soc_group_id(+) = psg.priceplan_soc_group_id " +
			" and (psgpt.priceplan_soc_group_id is null " +
			" or (to_char(psgpt.product_type_id) not in (" + productPromoTypeList + ")" +
			" and psgpt.include_ind = 'N' " +
			" and not exists (select 1 " +
			"    from priceplan_soc_gp_product_type psgpt1 " +
			"    where psgpt1.priceplan_soc_group_id = psg.priceplan_soc_group_id " +
			"    and (to_char(psgpt1.product_type_id) in (" + productPromoTypeList + ") or psgpt1.include_ind = 'Y'))) " +
			"    or (to_char(psgpt.product_type_id) in (" + productPromoTypeList + ")" + " and psgpt.include_ind = 'Y' " +
			"    and (psgpt.initial_activation_ind = 'Y' and 'Y' = '" + initialAct + "' or psgpt.initial_activation_ind = 'N')) " +
			"    or (to_char(psgpt.product_type_id) in (" + productPromoTypeList + ")" + " and psgpt.include_ind = 'N' " +
			"    and psgpt.initial_activation_ind = 'Y' and 'N' = '" + initialAct + "') " +
			" ) ";
		}

		if (!(term == PricePlanSummary.CONTRACT_TERM_ALL)) {
			termTables = ", soc_group sg_term, price_plan_group_term ppgt ";
			termClause = " and sg_term.soc = s.soc " +
			" and ppgt.soc_group(+) = sg_term.gp_soc " +
			" and (ppgt.term_months = " + term +
			" or ppgt.term_months is null) " +
			" and NVL(ppgt.term_months, s.minimum_no_months) = " + term;			}

		if (pBrandId != Brand.BRAND_ID_ALL) {
			brandIdClause = " and nvl(s.brand_id, 1) = " + pBrandId;
		}

		if (!NetworkType.NETWORK_TYPE_ALL.equalsIgnoreCase(networkType))
			networkTypeClause = " and (ser.network_type = '" + networkType + "' OR ser.network_type = '" + NetworkType.NETWORK_TYPE_ALL + "')";
		
		if (StringUtils.hasText(seatTypeCode)) {
			seatTypeCodeClause = " and s.seat_type = '" + seatTypeCode + "'";
		}
		
		if (availableForActivationOnly == true) {
			availableForActivationIndClause = " and psg.act_avail_ind = 'Y'";
		}
		
		if (currentPricePlansOnly == true) {
			currentIndClause = " and s.current_ind = 'Y'";
		}
		
		final String sql = "select distinct s.soc, s.soc_description, s.soc_description_f " +
		" , s.service_type, NVL(s.minimum_no_months, 0) " +
		" , s.rda_ind, s.bl_zero_chrg_suppress_ind " +
		" , psg.client_available_ind, psg.dealer_available_ind " +
		" , psg.act_avail_ind, psg.chng_avail_ind " +
		" , '' "  + // 12
		" , s.soc_status " +
		" , s.for_sale_ind " +
		" , s.sale_eff_date " +
		" , s.sale_exp_date " +
		" , s.current_ind " +
		" , DECODE(s.product_type, 'C', 'Y', s.inc_cel_ftr_ind) " +
		" , s.inc_dc_ftr_ind " +
		" , s.inc_pds_ftr_ind " +
		" , DECODE(dps.soc, null, 'N', 'Y') " +
		" , NVL(rc.rate, 0) " +
		" , urf.uc_rounding_factor " +
		" , 0 " +
		" , NVL(s.min_commit_amt, 0) " + //25
		" , s.soc_level_code " +
		" , psg.client_chng_avail_ind " + //27
		" , psg.dealer_chng_avail_ind " +
		" , psg.client_modify_avail_ind " + //29
		" , psg.dealer_modify_avail_ind " +
		" , psg.corporate_renewal_ind " + //31
		" , psg.non_corporate_renewal_ind " + //32
		" , s.period_set_code " + //33
		" , NVL(rf.rc_freq_of_pym, 0) " + //34
		" , NVL(s.soc_def_user_seg, 'PERSONAL  ') " + //35
		" , psg.ares_avail_ind " + //36
		" , psg.csa_avail_ind " + //37
		" , s.min_req_usg_chrg " + //39
		" , DECODE(sarc.soc, null, 'N', 'Y') srac_soc" +
		" , s.mp_ind " + //41
		" , NVL(s.brand_id, " + Brand.BRAND_ID_ALL + ") brand_id " + //42, to be updated with real column
		" , psg.aom_avail_ind " + //43
		" , s.mci_ind " + //44
		" , ser.network_type " + //45
		" , s.bill_cycle_treatment_cd " + //46
		" , s.seat_type " + //47?
		" , s.soc_service_type " + //48?
		" , s.soc_duration_hours " + //49?
		" , s.rlh_ind " +
		" , s.combo_group_id " +
		offerSelectClause + //50 
		" from acct_type_priceplan_soc_groups atpsg " +
		" , priceplan_soc_groups psg  " +
		psgptTable  +
		" , soc_group sg " +
		" , soc_submkt_relation ssr " +
		" , soc_equip_relation ser " +
		" , market m " +
		" , logical_date ld " +
		" , soc_discount_plan dps " +
		" , pp_rc_rate rc " +
		" , rated_feature rf " +
		" , uc_rated_feature urf " +
		termTables +
		" , soc_activity_reason_code sarc " +
		" , soc s " +
		" where atpsg.acct_type = " + "'" + accountType + "'" +
		" and atpsg.acct_sub_type = " + "'" + accountSubType + "'" +
		" and psg.priceplan_soc_group_id = atpsg.priceplan_soc_group_id  " +
		availableForActivationIndClause + 
		psgptClause +
		" and sg.gp_soc = psg.price_plan_group " +
		" and s.soc = sg.soc " +
		" and s.effective_date = ssr.effective_date " +
		" and s.service_type = 'P' " +
		" and s.product_type = " + "'" + productType + "'" +
		" and s.soc_status = 'A' " +
		" and s.for_sale_ind = 'Y' " +
		" and trunc(s.sale_eff_date) <= trunc(ld.logical_date) " +
		" and (trunc(s.sale_exp_date) > trunc(ld.logical_date) " +
		" or s.sale_exp_date is null) " +
		" and ld.logical_date_type = 'O' " +
		currentIndClause +
		" and m.province=" + "'" + provinceCode + "'" +
		" and (ssr.sub_market = 'ALL' or (ssr.sub_market = m.market_code)) " +
		" and ssr.soc = s.soc " +
		" and ser.soc = ssr.soc " +
		" and ser.effective_date = ssr.effective_date " +
		" and (ser.equipment_type = " + "'" + equipmentType + "' " +
		"     or ser.equipment_type = '9') " +
		" and dps.soc(+) = s.soc " +
		" and rc.soc(+) = s.soc  " +
		" and rc.effective_date(+) = s.effective_date " +
		" and (rc.feature_code = 'STD' or rc.feature_code is null) " +
		" and rf.soc(+) = s.soc " +
		" and rf.effective_date(+) = s.effective_date " +
		" and (rf.feature_code = 'STD' or rf.feature_code is null) " +
		" and urf.soc(+) = s.soc  " +
		" and urf.effective_date(+) = s.effective_date " +
		" and (urf.feature_code = 'STD' or urf.feature_code is null) " +
		" and (urf.action = (select min(action) " +
		"     from uc_rated_feature urf1 " +
		"     where urf1.soc(+) = urf.soc " +
		"     and urf1.effective_date(+) = urf.effective_date " +
		"     and urf1.feature_code(+) = urf.feature_code) " +
		"     or urf.action is null) " +
		termClause +
		activityReasonCodeClause +
		brandIdClause +
		networkTypeClause +
		seatTypeCodeClause +
		socGroupClause +
		exceptionNotExist +
		" order by s.soc_description ";
		
		if (logger.isDebugEnabled()) {
			logger.debug(sql);
		}
						
		List<PricePlanInfo> result = getJdbcTemplate().execute(new StatementCallback<List<PricePlanInfo>>() {
			
			@Override
			public List<PricePlanInfo> doInStatement(Statement statement) throws SQLException, DataAccessException {
				List<PricePlanInfo> services = new ArrayList<PricePlanInfo>();
				ResultSet rs  = null;
				try {
					statement.setFetchSize(FETCH_SIZE);
					rs = statement.executeQuery(sql);
					while(rs.next()) {
						services.add( mapRow( rs, rs.getRow() ) );
					}
					
				} finally {
					close (rs );
				}				
				populateServiceExtraProperties( services, statement.getConnection() );
				return services;
			}
						
			public PricePlanInfo mapRow(ResultSet rs, int rowNum) throws SQLException {				
				PricePlanInfo service = new PricePlanInfo();
				
				service.setCode(rs.getString(1));
				service.setDescription(rs.getString(2));
				service.setDescriptionFrench(rs.getString(3) == null ? rs.getString(2) : rs.getString(3).equals("") ? rs.getString(2) : rs.getString(3));
				service.setServiceType(rs.getString(4));
				service.setTermMonths(rs.getInt(5));
				service.setProductType(productType);
				service.setClientActivation(rs.getString(8) == null ? false : rs.getString(8).equals("Y") ? true : false);
				service.setDealerActivation(rs.getString(9) == null ? false : rs.getString(9).equals("Y") ? true : false);
				service.setAvailableForChangeByClient(rs.getString(27) == null ? false : rs.getString(27).equals("Y") ? true : false);
				service.setAvailableForChangeByDealer(rs.getString(28) == null ? false : rs.getString(28).equals("Y") ? true : false);
				service.setAvailableToModifyByClient(rs.getString(29) == null ? false : rs.getString(29).equals("Y") ? true : false);
				service.setAvailableToModifyByDealer(rs.getString(30) == null ? false : rs.getString(30).equals("Y") ? true : false);
				service.setAvailableForCorporateRenewal(rs.getString(31) == null ? false : rs.getString(31).equals("Y") ? true : false);
				service.setAvailableForNonCorporateRenewal(rs.getString(32) == null ? false : rs.getString(32).equals("Y") ? true : false);
				service.setAvailableForRetailStoreActivation(rs.getString(36) == null ? false : rs.getString(36).equals("Y") ? true : false);
				service.setAvailableForCorporateStoreActivation(rs.getString(37) == null ? false : rs.getString(37).equals("Y") ? true : false);
				service.setAvailableForActivation(rs.getString(10) == null ? false : rs.getString(10).equals("Y") ? true : false);
				service.setAvailableForChange(rs.getString(11) == null ? false : rs.getString(11).equals("Y") ? true : false);
				service.setBillingZeroChrgSuppress(rs.getString(7) == null ? false : rs.getString(7).equals("Y") ? true : false);
				service.setActive(rs.getString(13) == null ? false : rs.getString(13).equals("A") ? true : false);
				service.setForSale(rs.getString(14) == null ? false : rs.getString(14).equals("Y") ? true : false);
				service.setEffectiveDate(rs.getTimestamp(15));
				service.setExpiryDate(rs.getTimestamp(16));
				service.setCurrent(rs.getString(17) == null ? false : rs.getString(17).equals("Y") ? true : false);
				service.setTelephonyFeaturesIncluded(rs.getString(18) == null ? false : rs.getString(18).equals("Y") ? true : false);
				service.setDispatchFeaturesIncluded(rs.getString(19) == null ? false : rs.getString(19).equals("Y") ? true : false);
				service.setWirelessWebFeaturesIncluded(rs.getString(20) == null ? false : rs.getString(20).equals("A") ? true : false);
				service.setDiscountAvailable(rs.getString(21) == null ? false : rs.getString(21).equals("Y") ? true : false);
				
				double minimumCommitmentAmout = rs.getDouble(25);
				service.setRecurringCharge((rs.getDouble(22) + minimumCommitmentAmout));
				service.setUsageRatingFrequency(rs.getInt(23));
				service.setIncludedMinutesCount(rs.getInt(24));
				service.setLevelCode(rs.getString(26) == null ? "" : rs.getString(26));
				service.setPeriodCode(rs.getString(33));
				service.setRecurringChargeFrequency(rs.getInt(34));
				service.setUserSegment(rs.getString(35));
				service.setMinimumUsageCharge(rs.getDouble("min_req_usg_chrg"));
				service.setSuspensionPricePlan(rs.getString("srac_soc").equals("N")? false : true);
				service.setMinutePoolingCapable("Y".equals(rs.getString("mp_ind")));
				service.setBrandId(rs.getInt("brand_id"));
				service.setAOMPricePlan("Y".equals(rs.getString("aom_avail_ind")));
				
				String dollarPooling = rs.getString("mci_ind");
				if (dollarPooling != null && minimumCommitmentAmout > 0) {
					if (service.getProductType().equals(Subscriber.PRODUCT_TYPE_PCS))
						service.setDollarPoolingCapable(dollarPooling.equals("I") ? true : false);
					else if (service.getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN))
						service.setDollarPoolingCapable(dollarPooling.equals("I") || dollarPooling.equals("C") ? true : false);
					else
						service.setDollarPoolingCapable(false);
					
				} else {
					service.setDollarPoolingCapable(false);
				}
				service.setBillCycleTreatmentCode(rs.getString("bill_cycle_treatment_cd"));
				service.setSeatType(rs.getString("seat_type"));
				service.setSocServiceType(rs.getString("soc_service_type"));
				service.setDurationServiceHours(rs.getInt("soc_duration_hours"));
				service.setRLH(isRoamLikeHome(rs.getString("rlh_ind")));
				service.setComboGroupId(rs.getString("combo_group_id"));
				
				//Handle offer price plan groups (rate plan/soc groups)
				String selectedOffer = rs.getString("offer_grp_ind");
				if ("Y".equals(selectedOffer)) {
					service.setSelectedOffer(true);
				}
				
				return service;
			}
		});		
				
		return result;
	}
	
	/*
	 * This method will return the soc group filter 'where query' clause for getPricePlanList
	 */
	private String getSocGroupFilterSQLClauseForGetPricePlanList(String[] socGroups) {
		if (socGroups != null) {
			//Let's get rid of all the empty and null entries
			List<String> socGroupList = new ArrayList<String>(Arrays.asList(socGroups));
			socGroupList.removeAll(Collections.singleton(null));
			socGroupList.removeAll(Collections.singleton(""));
			//Only add the socGroup clause if we have non empty entries
			if (socGroupList.size() > 0) {
				return " and exists (select 1 from soc_group sg2 where sg2.gp_soc in (" + 
						arrayToDelimitedString(socGroupList.toArray(new String[0]), ",", "'", "'") + ")" + 
						" and sg2.soc = s.soc) ";
			}
		}
		return "";
	}
	
	/*
	 * This method will return the offer group (soc group) 'select query' clause for getPricePlanList
	 */
	private String getOfferGroupSelectSQLClauseForGetPricePlanList(String[] offerGroups) {		
		if (offerGroups != null) { 
			if (offerGroups.length > 0) {
				String offerGroupCodes = arrayToDelimitedString(offerGroups, ",", "'", "'");
				return ", case when exists"
						+ " (select 1 from soc_group sg3 where sg3.soc = s.soc and sg3.gp_soc in (" + offerGroupCodes +"))" 
						+ " then 'Y' else 'N'" 
						+ " end as offer_grp_ind";
			} else {
				return ", 'N' as offer_grp_ind";
			}			
		}
		return ", ' ' as offer_grp_ind";
	}
	
	
	/**
	 * Not in use?
	 * 
	 * @param productType
	 * @param provinceCode
	 * @param accountType
	 * @param equipmentType
	 * @param brandId
	 * @param currentPlansOnly
	 * @param availableForActivationOnly
	 * @param networkType
	 * @return
	 */
	@Deprecated
	public Collection<PricePlanInfo> retrievePricePlanList(final String productType, final String provinceCode, final char accountType,
			final String equipmentType, final int brandId, final boolean currentPlansOnly, final boolean availableForActivationOnly, final String networkType) {
		if (logger.isDebugEnabled()) {
			logger.debug("Deprecated method retrievePricePlanList in use..");
		}
		
		String call ="{? = call price_plan_utility_pkg.GetPricePlanList(?,?,?,?,?,?,?,?,?,?)}";
		
		List<PricePlanInfo> result = getJdbcTemplate().execute(call, new CallableStatementCallback<List<PricePlanInfo>>() {
			
			@Override
			public List<PricePlanInfo> doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
			
				List<PricePlanInfo> result = new ArrayList<PricePlanInfo>();
				
				cs.registerOutParameter(1, OracleTypes.NUMBER);
				cs.setString(2, productType);
				cs.setString(3, provinceCode);
				cs.setString(4, String.valueOf(accountType));
				cs.setString(5, equipmentType);
				cs.setInt(6, brandId);
				cs.setString(7,currentPlansOnly ? "Y" : "N" );
				cs.setString(8,availableForActivationOnly ? "Y" : "N" );
				cs.setString(9, networkType);
				cs.registerOutParameter(10, OracleTypes.CURSOR);
				cs.registerOutParameter(11, OracleTypes.VARCHAR);

				cs.execute();

				boolean success = cs.getInt(1) == AccountManager.NUMERIC_TRUE;
				
				if (success) {
					ResultSet rs = (ResultSet) cs.getObject(10);
					
					try {
						while (rs.next()) {
							PricePlanInfo info = new PricePlanInfo();
							info.setCode(rs.getString(1));
							info.setDescription(rs.getString(2));
							info.setDescriptionFrench(rs.getString(3) == null ? rs.getString(2) : rs.getString(3).equals("") ? rs.getString(2) : rs.getString(3));
							info.setServiceType(rs.getString(4));
							info.setTermMonths(rs.getInt(5));
							info.setProductType(productType);
							info.setClientActivation(rs.getString(8) == null ? false : rs.getString(8).equals("Y") ? true : false);
							info.setDealerActivation(rs.getString(9) == null ? false : rs.getString(9).equals("Y") ? true : false);
							info.setAvailableForChangeByClient(rs.getString(27) == null ? false : rs.getString(27).equals("Y") ? true : false);
							info.setAvailableForChangeByDealer(rs.getString(28) == null ? false : rs.getString(28).equals("Y") ? true : false);
							info.setAvailableToModifyByClient(rs.getString(29) == null ? false : rs.getString(29).equals("Y") ? true : false);
							info.setAvailableToModifyByDealer(rs.getString(30) == null ? false : rs.getString(30).equals("Y") ? true : false);
							info.setAvailableForCorporateRenewal(rs.getString(31) == null ? false : rs.getString(31).equals("Y") ? true : false);
							info.setAvailableForNonCorporateRenewal(rs.getString(32) == null ? false : rs.getString(32).equals("Y") ? true : false);
							info.setAvailableForRetailStoreActivation(rs.getString(36) == null ? false : rs.getString(36).equals("Y") ? true : false);
							info.setAvailableForCorporateStoreActivation(rs.getString(37) == null ? false : rs.getString(37).equals("Y") ? true : false);
							info.setAvailableForActivation(rs.getString(10) == null ? false : rs.getString(10).equals("Y") ? true : false);
							info.setAvailableForChange(rs.getString(11) == null ? false : rs.getString(11).equals("Y") ? true : false);
							info.setBillingZeroChrgSuppress(rs.getString(7) == null ? false : rs.getString(7).equals("Y") ? true : false);
							info.setActive(rs.getString(13) == null ? false : rs.getString(13).equals("A") ? true : false);
							info.setForSale(rs.getString(14) == null ? false : rs.getString(14).equals("Y") ? true : false);
							info.setEffectiveDate(rs.getTimestamp(15));
							info.setExpiryDate(rs.getTimestamp(16));
							info.setCurrent(rs.getString(17) == null ? false : rs.getString(17).equals("Y") ? true : false);
							info.setTelephonyFeaturesIncluded(rs.getString(18) == null ? false : rs.getString(18).equals("Y") ? true : false);
							info.setDispatchFeaturesIncluded(rs.getString(19) == null ? false : rs.getString(19).equals("Y") ? true : false);
							info.setWirelessWebFeaturesIncluded(rs.getString(20) == null ? false : rs.getString(20).equals("A") ? true : false);
							info.setDiscountAvailable(rs.getString(21) == null ? false : rs.getString(21).equals("Y") ? true : false);
							
							double minimumCommitmentAmout = rs.getDouble(25);
							info.setRecurringCharge((rs.getDouble(22) + minimumCommitmentAmout));
							
							info.setUsageRatingFrequency(rs.getInt(23));
							info.setIncludedMinutesCount(rs.getInt(24));
							info.setLevelCode(rs.getString(26) == null ? "" : rs.getString(26));
							info.setPeriodCode(rs.getString(33));
							info.setRecurringChargeFrequency(rs.getInt(34));
							info.setUserSegment(rs.getString(35));
							info.setMinimumUsageCharge(rs.getDouble("min_req_usg_chrg"));
							info.setSuspensionPricePlan(rs.getString("sarc_soc").equals("N") ? false : true);							
							info.setMinutePoolingCapable("Y".equals(rs.getString("mp_ind")) ? true : false);
							info.setBrandId(rs.getInt("brand_id"));
							info.setAOMPricePlan("Y".equals(rs.getString("aom_avail_ind")) ? true : false);							
							String dollarPooling = rs.getString("mci_ind");
							if (dollarPooling != null && minimumCommitmentAmout > 0) {
								if (info.getProductType().equals(Subscriber.PRODUCT_TYPE_PCS))
									info.setDollarPoolingCapable(dollarPooling.equals("I") ? true : false);
								else if (info.getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN))
									info.setDollarPoolingCapable(dollarPooling.equals("I") || dollarPooling.equals("C") ? true : false);
								else
									info.setDollarPoolingCapable(false);								
							} else {
								info.setDollarPoolingCapable(false);
							}
							info.setBillCycleTreatmentCode(rs.getString("bill_cycle_treatment_cd")); // bill_cycle_treatment_cd
							info.setSeatType(rs.getString("seat_type"));
							info.setSocServiceType(rs.getString("soc_service_type"));
							info.setDurationServiceHours(rs.getInt("soc_duration_hours"));
							info.setRLH(isRoamLikeHome(rs.getString("rlh_ind")));
							
							result.add(info);
						}						
					} finally {
						rs.close();
					}
				} else {
					throw new SQLException("Strored procedure failed: " + cs.getString(10));
				}				
				populateServiceExtraProperties(result, cs.getConnection());
				
				return result;
			}
		});		
		
		return result;
	}
	
	public double retrieveAlternateRecurringCharge(final ServiceInfo serviceInfo, final String provinceCode, final String npa, final String nxx, final String corporateId) {

		return getJdbcTemplate().execute( new ConnectionCallback<Double>() {
			
			@Override
			public Double doInConnection(Connection con) throws SQLException, DataAccessException {

				double recurringChargeForService = 0;

				boolean noAlternateRowsFound = true;
				boolean corporateIdExcemptionRowsFound = false;
				boolean npaNxxExcemptionRowsFound = false;

				HashMap<String, Double> alternateRateRows = new HashMap<String, Double>();
				Set<String> npaNaxxExcemptionRows = new HashSet<String>();
				Set<String> corporateIdExcemptionRows = new HashSet<String>();

				String queryStr = "";

				PreparedStatement stmt1 = null;
				PreparedStatement stmt2 = null;

				ResultSet result1 = null;
				ResultSet result2 = null;

				try {

					logger.debug("Processing service=[" + serviceInfo.getCode() + "], province=[" +
							provinceCode + "], npa=[" + npa + "], nxx=[" + nxx + "], corporateId=[" + corporateId + "] ...");

					// Step I: retrieve all 'Alternate Rate' rows for this service/province
					//         and store in HashMap (key=feature code, value=rate)
					//         - if no rows found, return 'regular' recurring charge
					
					queryStr = " select soc " +
					"        ,feature_code " +
					"        ,province " +
					"        ,rate " +
					" from batch_pp_rc_rate " +
					" where soc = ? " +
					" and   province = ? " +
					" and   effective_date <= sysdate " +
					" and   (expiration_date is null or expiration_date >= sysdate) " +
					" and   rate is not null " ;

					stmt1 = con.prepareStatement(queryStr);
					stmt1.setString(1, serviceInfo.getCode());
					stmt1.setString(2, provinceCode);

					logger.debug("Looking for alternate rate rows for service=[" + serviceInfo.getCode() + "] and province=[" +
							provinceCode + "] effective today...");

					result1 = stmt1.executeQuery();

					while (result1.next()) {
						noAlternateRowsFound = false;
						String featureCode = result1.getString("feature_code").trim();
						Double rate = new Double(result1.getDouble("rate"));
						
						logger.debug("Found alternate rate=[" + rate + "] for feature=[" + featureCode + "]");
						
						alternateRateRows.put(featureCode,rate);
					}
					if (noAlternateRowsFound) {
						logger.debug("No alternate rates found - returning regular recurring charge=[" + serviceInfo.getRecurringCharge() + "] - Bye Bye!");
						return serviceInfo.getRecurringCharge();
					}

					// Step II: retrieve all 'Excemption' rows for this service/province
					//          and store in two HashMaps:
					//            1. corporate Id: key=feature code, value=null)
					//               corporate Id: key=feature code + corporateId, value=null)
					//            2. npa/nxx:      key=feature code, value=null)
					//               npa/nxx:      key=feature code + npa + nxx, value=null)
					
					queryStr = " select soc " +
					"        ,feature_code " +
					"        ,province " +
					"        ,npa " +
					"        ,nxx " +
					"        ,corporate_id " +
					" from batch_pp_rc_rate " +
					" where soc = ? " +
					" and   (province = ?  or province = '99') " +
					" and   effective_date <= sysdate " +
					" and   (expiration_date is null or expiration_date >= sysdate) " +
					" and   rate is null " +
					" order by 4,5,6 ";

					stmt2 = con.prepareStatement(queryStr);
					stmt2.setString(1, serviceInfo.getCode());
					stmt2.setString(2, provinceCode);

					logger.debug("Looking for excemption rows for service=[" + serviceInfo.getCode() + "] and province=[" + provinceCode + "]...");

					result2 = stmt2.executeQuery();

					while (result2.next()) {
						String featureCode = result2.getString("feature_code").trim();
						String npa = result2.getString("npa");
						String nxx = result2.getString("nxx");
						String corporateId = result2.getString("corporate_id");
						
						logger.debug("Found excemption row for feature=[" +	featureCode + "] with excemption parmameters as follows: npa=[" +
								npa + "], nxx=[" + nxx + "], corporateId=[" + corporateId + "]");
						
						if (npa == null) {
							corporateIdExcemptionRowsFound = true;
							corporateIdExcemptionRows.add(featureCode);
							corporateIdExcemptionRows.add(featureCode+corporateId.trim());
						} else {
							npaNxxExcemptionRowsFound = true;
							String npaNxx = nxx == null ? npa : npa + nxx;
							npaNaxxExcemptionRows.add(featureCode);
							npaNaxxExcemptionRows.add(featureCode+npaNxx);
						}
					}

					// Step III: loop through the services features and determine whether
					//           alternate rc exists and is applicable
					
					recurringChargeForService = serviceInfo.getRecurringCharge();
					logger.debug("Inital recurring charge=[" + recurringChargeForService + "]");

					for (int i = 0; i < serviceInfo.getFeatures0().length; i++) {

						String featureCode = serviceInfo.getFeatures0()[i].getCode().trim();
						logger.debug("Processing feature=[" + featureCode + "]");

						// check if alternate rate exists for this feature
						if (alternateRateRows.containsKey(featureCode)) {

							logger.debug("... alternate rate found");
							boolean excemptDueToCorporateId = false;
							boolean excemptDueToNpaNxx = false;

							// check if corporateId excemption row exists for this feature
							if (corporateIdExcemptionRowsFound && corporateId != null && !corporateId.trim().equals("") && corporateIdExcemptionRows.contains(featureCode)) {
								excemptDueToCorporateId = corporateIdExcemptionRows.contains(featureCode+corporateId);
							}

							// check if npa/nxx excemption row exists for this feature
							if (!excemptDueToCorporateId && npaNxxExcemptionRowsFound && npaNaxxExcemptionRows.contains(featureCode)) {
								excemptDueToNpaNxx = npaNaxxExcemptionRows.contains(featureCode+npa+nxx) ||
								npaNaxxExcemptionRows.contains(featureCode+npa);
							}

							// if not excempt, 'correct' recurring charge for service by subtracting the 'regular'
							// recurring charge and 'adding' the alternate recurring charge
							if (!excemptDueToNpaNxx && !excemptDueToCorporateId) {
								double alternateRc = ( (Double) alternateRateRows.get(featureCode)).doubleValue();
								logger.debug("... not excempt - regular rate=[" + serviceInfo.getFeatures0()[i].
										getRecurringCharge() + "] alternate rate=[" + alternateRc + "]");

								recurringChargeForService = recurringChargeForService -
								serviceInfo.getFeatures0()[i].getRecurringCharge() +
								alternateRc;

							} else {
								if (excemptDueToCorporateId) logger.debug("... excempt due to corporate id - no correction.");
								if (excemptDueToNpaNxx) logger.debug("... excempt due to npa/nxx - no correction.");
							}
						}
					}

				} finally {
					if (result1 != null) result1.close();
					if (result2 != null) result2.close();
					if (stmt1 != null) stmt1.close();
					if (stmt2 != null) stmt2.close();
				} 

				logger.debug("New recurring charge=[" + recurringChargeForService + "]");
				
				return recurringChargeForService;			
			}
		});
	}
	
	public Collection<ServiceEquipmentTypeInfo> retrieveServiceEquipmentNetworkInfo(final String serviceCode) {
		
		String sql = " select equipment_type, network_type from soc_equip_relation where soc= ? " ;
		
		return getJdbcTemplate().query(sql, new Object[] {Info.padTo(serviceCode, ' ', 9)}, new ResultSetExtractor<Collection<ServiceEquipmentTypeInfo>>() {
			
			@SuppressWarnings("unchecked")
			@Override
			public Collection<ServiceEquipmentTypeInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {
				
				ServiceInfo service = new ServiceInfo();
				service.setCode(serviceCode);
				
				while (rs.next()){					
					populateServiceEquipRelation(service, rs.getString(1), rs.getString(2));
				}

				return (Collection<ServiceEquipmentTypeInfo>) service.getServiceEquipmentTypeInfo().values();
			}
		});
	}
	
//Added for Charge PaperBill - start - Anitha Duraisamy
	
	public Collection<FeeRuleDto> retrievePaperBillChargeType(int brandId, String provinceCode, char accountType, 
			char accountSubType, String segment,String invoiceSuppressionLevel, Date logicalDate) {
	    // use a Sql query to lookup one-time charge information from the FEE_RULE, CHARGE_INFO, FEATURE and PP_RC_RATE reference tables
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT ci.feature_code," +
				" f.feature_desc," +
				" f.feature_desc_f," +
				" f.product_type," +
				" ci.mnl_oc_chrg_level," +
				" ci.mnl_oc_amt_ovrd_ind," +
				" ci.def_bal_impact_cd," +
				" prr.rate, " +
				" fr.brand_id, " +
				" fr.province_cd," +
				" fr.account_type_cd, " +
				" fr.acc_sub_type_cd, " +
				" fr.gl_segment_cd," +
				" ci.manual_oc_create_ind "+
				" FROM fee_rule fr," +
				" charge_info ci," +
				" feature f," +
				" pp_rc_rate prr" +
				" WHERE fr.effective_start_dt <= ?"+
				" AND fr.fee_group_cd = 'BAN_LVL_INV_SUPPRESSION'"+
				" AND fr.fee_type_value_cd = ? "+
				" AND (fr.effective_stop_dt IS NULL OR fr.effective_stop_dt >= ?)" +
				" AND ci.feature_code = fr.feature_cd" +
				" AND ci.feature_code = f.feature_code" +
				" AND ci.feature_code = prr.feature_code" +
				" AND prr.soc = fr.soc_cd" +
				" AND ci.ftr_revenue_code = 'O'");

		List<Object> params = new ArrayList<Object>();
		params.add(logicalDate);
		params.add(invoiceSuppressionLevel);
		params.add(logicalDate);
		
		if (brandId !=0) {
			sql.append(" AND (fr.brand_id = ? or fr.brand_id is null)");
			params.add(brandId);
		} else {
			sql.append(" AND fr.brand_id is null");
		}

		if (provinceCode!=null && !provinceCode.equals("")) {
			sql.append(" AND (fr.province_cd = ? or fr.province_cd is null)");
			params.add(provinceCode);
		} else {
			sql.append(" AND fr.province_cd is null");
		}
		
		if (accountType !='\u0000') {
			sql.append(" AND (fr.account_type_cd = ? or fr.account_type_cd is null)");
			params.add( Character.toString(accountType));
		} else {
			sql.append(" AND fr.account_type_cd is null");
		}
		
		if (accountSubType !='\u0000') {
			sql.append(" AND (fr.acc_sub_type_cd = ? or fr.acc_sub_type_cd is null)");
			params.add( Character.toString(accountSubType));
		} else {
			sql.append(" AND fr.acc_sub_type_cd is null");
		}
		
		if (segment != null && !segment.equals("")) {
			sql.append(" AND (fr.gl_segment_cd = ? or fr.gl_segment_cd is null)");
			params.add(segment);
		} else {
			sql.append(" AND fr.gl_segment_cd is null");
		}

	    return  getJdbcTemplate().query(sql.toString(), params.toArray(), new RowMapper<FeeRuleDto>() {
			 public FeeRuleDto mapRow(ResultSet rs, int rowNum) throws SQLException {
				 FeeRuleDto feeRuleDto= new FeeRuleDto(
						 rs.getString(1),						    //feature_code
							rs.getString(2),						//feature_desc
							rs.getString(3),						//feature_desc_f
							rs.getString(4),						//product_type
							toBoolean(rs.getString(14)),			//isManual
							rs.getDouble(8),						//amount
							toBoolean(rs.getString(6)),				//mnl_oc_amt_ovrd_ind ->isAmountOverrideable
							toChar(rs.getString(5)),				//mnl_oc_chrg_level -> level
							toChar(rs.getString(7)),				//def_bal_impact_cd -> balanceImpact
							rs.getInt(9),							//brandId
							rs.getString(10),						//provinceCode
							toChar(rs.getString(11)),				//accountType
							toChar(rs.getString(12)), 				//accountSubType
							rs.getString(13)						//segment
				 	);
				 return feeRuleDto;
			 }
		 });
	}
	
	//Added for Charge PaperBill - End - Anitha Duraisamy	
	
	
	public List<String> retrieveServiceGroupRelation(final String socGroupCode) {
		String sql = "select soc from soc_group_ext where gp_soc= ? union SELECT soc FROM soc_group WHERE gp_soc = ?";

		return getJdbcTemplate().execute(sql, new PreparedStatementCallback<List<String>>() {

			@Override
			public List<String> doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ResultSet rs = null;
				List<String> socList = new ArrayList<String>();

				try {
					ps.setFetchSize(FETCH_SIZE);
					ps.setString(1, socGroupCode);
					ps.setString(2, socGroupCode);
					rs = ps.executeQuery();

					while (rs.next()) {
						socList.add(rs.getString("soc"));
					}
				} finally {
					if (rs != null) {
						rs.close();
					}
				}
				return socList;
			}

		});

	}
	
	public ServiceTermDto retrieveServiceTerm(final String serviceCode) {

		String sql = "SELECT DURATION, duration_ind" + " FROM promotion_terms p, logical_date ld" + " WHERE p.soc = ?" + " AND TRUNC (p.effective_date) <= TRUNC (ld.logical_date)"
				+ " AND ( p.expiration_date IS NULL OR (p.expiration_date > TRUNC (ld.logical_date)))" + " AND ld.logical_date_type = 'O'";
		return getJdbcTemplate().query(sql, new Object[] { serviceCode }, new ResultSetExtractor<ServiceTermDto>() {

			@Override
			public ServiceTermDto extractData(ResultSet rs) throws SQLException, DataAccessException {
				ServiceTermDto serviceTermDto = null;
				while (rs.next()) {
					serviceTermDto = new ServiceTermDto(serviceCode, rs.getInt(1), rs.getString(2));
				}
				return serviceTermDto;
			}
		});
	}
	
	//==============TBS changes ====================
	
	/**
	 * This methods is used by all methods that retrieve service (included, optional, include promotions) and pricePlan list, to 
	 * populate ServiceInfo's properties:
	 * 		equipmentTypes / faimliyTypes / serivceDataSharingGroup
	 *  
	 * @param <T>
	 * @param services
	 * @param conn
	 * @throws SQLException
	 */
	private <T extends ServiceInfo> void populateServiceExtraProperties(List<T> services, Connection conn ) throws SQLException {
		populateServiceExtraProperties( services, conn,
				new CallableStatementServicePopulator[] {
					new ServiceEquipmentTypePopulator(),
					new ServiceFamilyTypePopulator(),
					new ServiceDataSharingGroupPopulator()
				}
			);
	}	
	
	//This method uses same connection to execute multiple CallableStatement multiple times
	private <T extends ServiceInfo> void populateServiceExtraProperties(List<T> services, Connection conn,  CallableStatementServicePopulator[] populators) throws SQLException {

		if (services == null || services.size() == 0) {
			return;
		}
		
		try {
			//create all callable statements up-front for reuse in the batch loop.
			for( CallableStatementServicePopulator p : populators ){
				p.setConnection(conn);
			}
			
			int fetchSize = 400; //services.size();
			int batchCount = (int) Math.ceil((double) services.size() / fetchSize);
			for (int i = 0; i < batchCount; i++) {
				//calculate the subset range
				int fromIndex = i * fetchSize;
				int toIndex = fromIndex+ fetchSize;
				if (toIndex>services.size() ) toIndex = services.size();
				
				//prepare the subset
				List<T> socs = services.subList(fromIndex, toIndex);
				HashMap<String, ServiceInfo> map = new HashMap<String, ServiceInfo>(socs.size());
				for (ServiceInfo service : socs) {
					map.put(service.getCode().trim(), service);
				}
				Connection realConn = populators[0].getCallableStatment().getConnection();
				ArrayDescriptor socsArrayDesc = ArrayDescriptor.createDescriptor("T_SOCS", realConn);
				ARRAY socArray = new ARRAY(socsArrayDesc, realConn, (String[]) map.keySet().toArray(new String[socs.size()]));
				
				for( CallableStatementServicePopulator p : populators ){
					ResultSet rs = null;
					try {
						int size = map.size();
						OracleCallableStatement cs = p.getCallableStatment();
						cs.setARRAY(1, socArray);
						cs.registerOutParameter(2, OracleTypes.CURSOR);
						cs.setFetchSize( size );
						try {
							cs.execute();
						} catch (SQLException t0) {
							if ( ExceptionUtil.isRootCauseAsORA04068(t0)) {
								//in case of ORA-04068 error, we retry the transaction one more time.
								try { 
									logger.info("Encoutered ORA-04068 failure,  retry CallableStatment.execute in : " + p.getClass().getName() );
									cs.execute();
								} catch (SQLException t1) {
									throw  t1 ;
								}
							} else { 
								//if not ORA-04068, let it error out
								throw  t0 ;
							}
						}
						
						rs = (ResultSet) cs.getObject(2);
						rs.setFetchSize( size );
						
						p.populuateSerivces( rs, map );
						
					} finally {
						close ( rs );
					}
				}
			}
		} finally {
			//make sure we close statement
			for( CallableStatementServicePopulator p : populators ){
				p.dispose();
			}
		}
	}
	
	/**
	 * The subclass of this populator must comply the followings:
	 * 1) invoke a procedure which take to two parameters
	 * 2) The first parameter is input parameter of ARRAY of soc codes
	 * 3) The second parameter is output parameter which can be cast to ResultSet
	 * 4) The ResultSet must contain a column named "SOC"
	 */
	private abstract class CallableStatementServicePopulator {
		
		private OracleCallableStatement cs = null;
		
		abstract String getCallString();
		
		abstract void populateService (ResultSet rs, ServiceInfo serviceInfo )throws SQLException ;
		
		/**
		 * For override purposes.
		 */
		void populuateSerivces( ResultSet rs, HashMap<String, ServiceInfo> services) throws SQLException {
			while (rs.next()) {
				ServiceInfo service = (ServiceInfo) services.get(rs.getString("SOC").trim());
				populateService( rs, service );
			}
		}
		
		void setConnection(Connection conn) throws SQLException {
			cs = (OracleCallableStatement) conn.prepareCall(getCallString());
		}
		OracleCallableStatement getCallableStatment() {
			return cs;
		}
		void dispose() {
			close( cs );
			cs = null;
		}
	}
	
	private class ServicePdaRimMandatoryGroupPopulator extends CallableStatementServicePopulator {

		@Override
		String getCallString() {
			return "{call price_plan_utility_pkg.checkMandatorySOCs(?,?)}";
		}

		@Override
		void populateService(ResultSet rs, ServiceInfo serviceInfo) throws SQLException {
			serviceInfo.setPDAMandatoryGroup(rs.getInt("pda") > 0 ? true : false);
			serviceInfo.setRIMMandatoryGroup(rs.getInt("rim") > 0 ? true : false);
		}
	}
	
	private class ServiceEquipmentTypePopulator extends CallableStatementServicePopulator {

		@Override
		String getCallString() {
			return "{call price_plan_utility_pkg.getSocEquipRelations(?,?)}";
		}

		@Override
		void populateService(ResultSet rs, ServiceInfo serviceInfo) throws SQLException {
			populateServiceEquipRelation(serviceInfo, rs.getString("equipment_type"), rs.getString("network_type"));
		}
	}
	
	private class ServiceFamilyTypePopulator extends CallableStatementServicePopulator {

		@Override
		String getCallString() {
			return "{call price_plan_utility_pkg.getSocFamilyTypes(?,?)}";
		}

		@Override
		void populateService(ResultSet rs, ServiceInfo serviceInfo) throws SQLException {
			serviceInfo.addFamilyType( rs.getString("family_type"));
		}
	}
	
	private class ServiceDataSharingGroupPopulator extends CallableStatementServicePopulator {

		@Override
		String getCallString() {
			return "{call price_plan_utility_pkg.getSocAllowSharingGroups(?,?)}";
		}

		@Override
		void populateService(ResultSet rs, ServiceInfo serviceInfo) throws SQLException {
			serviceInfo.addDataSharingGroup( mapServiceDataSharingGroup(rs));
		}
	}

	private void close (ResultSet rs) {
		if (rs != null) try { 
			rs.close();
			
		} catch (SQLException e) {		
		}
	}
	
	private void close (Statement stmt) {
		if (stmt != null) try {
			stmt.close();
			
		} catch (SQLException e) {
		}
	}
	
	private ServiceDataSharingGroupInfo mapServiceDataSharingGroup(ResultSet rs) throws SQLException {
		ServiceDataSharingGroupInfo info = new ServiceDataSharingGroupInfo();
		info.setDataSharingGroupCode(rs.getString("ALLOW_SHARING_GROUP_CD"));
		info.setContributing("C".equals(rs.getString("ALLOW_SHARING_ACCESS_TYPE_CD")));
		return info;
	}
	
	public List<ServicePeriodInfo> retrieveServicePeriodInfo (final String serviceCode) {
		
		String sql =" SELECT P.period_value_code, P.day_number, to_date(p.from_hour,'hh24miss') from_hour, to_date(p.to_hour,'hh24miss') to_hour, N.period_name"   +
		" FROM SOC S, PERIOD P, PERIOD_NAME N,logical_date ld" +
		" WHERE S.soc=? and S.period_set_code = P.period_set_code and P.period_set_code=N.period_set_code  " +
		" AND P.period_value_code = N.period_value_code" +
		" and ld.logical_date_type='O'  " +
		" AND trunc(P.effective_date) <= trunc( ld.logical_date) " +
		" AND (P.expiration_date is null or trunc(P.expiration_date) > trunc(ld.logical_date) )";

	    return getJdbcTemplate().query(sql, new Object[] {Info.padService(serviceCode)}, new ResultSetExtractor<List<ServicePeriodInfo>>() {

			@Override
			public List<ServicePeriodInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<String, ServicePeriodInfo> periods = new HashMap<String, ServicePeriodInfo>();
				while (rs.next()) {
					String periodValue = rs.getString("period_value_code");
					ServicePeriodInfo periodInfo = periods.get(periodValue);

					if (periodInfo == null) {
						periodInfo = new ServicePeriodInfo();
						periodInfo.setCode(periodValue);
						periodInfo.setPeriodName(rs.getString("period_name"));
						periods.put(periodValue, periodInfo);

					}
					
					ServicePeriodHoursInfo periodHour = new ServicePeriodHoursInfo();
					periodHour.setDay(rs.getInt("day_number"));
					periodHour.setFrom(rs.getTime("from_hour"));
					periodHour.setTo(rs.getTime("to_hour"));
					periodInfo.addServicePeriodHour(periodHour);
				}
				
				return new ArrayList<ServicePeriodInfo>(periods.values());
			}
	    });
	}
	
	public Map<String, Map<String, ServicePeriodInfo>> retrieveServicePeriodInfos() {
		
		String sql =" SELECT P.period_set_code, P.period_value_code, P.day_number, to_date(p.from_hour,'hh24miss') from_hour, to_date(p.to_hour,'hh24miss') to_hour, N.period_name"   +
		" FROM PERIOD P, PERIOD_NAME N,logical_date ld"   +
		" WHERE P.period_set_code=N.period_set_code  "   +
		" AND P.period_value_code = N.period_value_code"   +
		" and ld.logical_date_type='O'  "   +
		" AND trunc(P.effective_date) <= trunc( ld.logical_date) "   +
		" AND (P.expiration_date is null or trunc(P.expiration_date) > trunc(ld.logical_date) )" + 
		" order by p.period_set_code";

	    return getJdbcTemplate().query( sql, new ResultSetExtractor<Map<String, Map<String, ServicePeriodInfo>>>() {

			@Override
			public Map<String, Map<String, ServicePeriodInfo>> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<String, Map<String, ServicePeriodInfo>> periodSets = new HashMap<String, Map<String, ServicePeriodInfo>>();

				while (rs.next()) {
					String periodSetCode = rs.getString("period_set_code");
					Map<String, ServicePeriodInfo> periodSet = periodSets.get(periodSetCode);
					if (periodSet == null) {
						periodSet = new HashMap<String, ServicePeriodInfo>();
						periodSets.put(periodSetCode, periodSet);
					}
					
					String periodValue = rs.getString("period_value_code");
					ServicePeriodInfo periodInfo = periodSet.get(periodValue);

					if (periodInfo == null) {
						periodInfo = new ServicePeriodInfo();
						periodInfo.setCode(periodValue);
						periodInfo.setPeriodName(rs.getString("period_name"));
						periodSet.put(periodValue, periodInfo);
					}
					
					ServicePeriodHoursInfo periodHour = new ServicePeriodHoursInfo();
					periodHour.setDay(rs.getInt("day_number"));
					periodHour.setFrom(rs.getTime("from_hour"));
					periodHour.setTo(rs.getTime("to_hour"));
					periodInfo.addServicePeriodHour(periodHour);
				}
				
				return  periodSets;
			}
	    });
	}
	
	public Map<String,ServiceCodeTypeInfo> retrieveServiceCodes() {
		String sql = "SELECT SOC , SERVICE_TYPE FROM SOC";
		
		return  getJdbcTemplate().query(sql,new ResultSetExtractor<Map<String,ServiceCodeTypeInfo>>() {

			@Override
			public Map<String,ServiceCodeTypeInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<String,ServiceCodeTypeInfo> map = new LinkedHashMap<String,ServiceCodeTypeInfo>();
				while (rs.next()) {
					ServiceCodeTypeInfo info = new ServiceCodeTypeInfo();
					String code = rs.getString("SOC");
					info.setServiceCode(code);
					info.setServiceType(rs.getString("SERVICE_TYPE"));
					map.put(code.trim(), info);
				}
				
				return map;
			}
		});
	}
	
	public Collection<SeatTypeInfo> retrieveSeatTypes() {
		return getReferenceDataCollection("SeatTypes", "", new RowMapper<SeatTypeInfo>() {
			
			@Override
			public SeatTypeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				SeatTypeInfo info = new SeatTypeInfo();

				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3) == null ? rs.getString(2) : rs.getString(3).equals("") ? rs.getString(2) : rs.getString(3));

				return info;
			}
		});
	}
	
	/**
	 * This method is created for defect 20700 in MQC. The method is applied to retrieveRegularServices only as of July 2014. The same mechanism should be used
	 * for other methods such as retrieveIncludedPromotions, retrieveIncludedServices, retrieveOptionalServices, retrievePricePlan etc. for consistency.
	 * But it's applied to retrieveRegularServices just to limit the testing scope due to time constraint. - Chung
	 * @param ratedFeatureArray
	 * @return
	 */
	private double calculateServiceRecurringCharge(RatedFeatureInfo[] ratedFeatureArray) {
		double rcSum = 0;
		if (ratedFeatureArray != null) {
			for (RatedFeatureInfo ratedFeature : ratedFeatureArray) {
				rcSum = rcSum + ratedFeature.getRecurringCharge();
			}
		}
		
		return rcSum;
	}
	
	/**
	 * Naresh.A - added below method to calculate the additionalCharge (ac_rate) for roaming soc's
	 * @param ratedFeatureArray
	 * @return
	 */
	private double calculateServiceAdditionalCharge(RatedFeatureInfo[] ratedFeatureArray) {
		double acSum = 0;
		if (ratedFeatureArray != null) {
			for (RatedFeatureInfo ratedFeature : ratedFeatureArray) {
				acSum = acSum + ratedFeature.getAdditionalCharge();
			}
		}
		return acSum;
	}
	
	// Return true if accountType/accountSubType are eligible for PP&S
	public boolean isPPSEligible(char accountType, char accountSubType) {
		return unavailablePPSServices(accountType, accountSubType).isEmpty();
	}

	// Return the service codes of the unavailable PP&S services
	public Collection<String> unavailablePPSServices(char accountType,
			char accountSubType) {

		/*
		 * String sql = "select unique sg.soc from " + " soc_group sg " +
		 * " ,acct_type_soc_group_map atsgm  " + " where " +
		 * " trim(SG.GP_SOC) =  trim(atsgm.soc_group) " +
		 * " and atsgm.acct_type != ? " + " and atsgm.acct_sub_type != ? " +
		 * " and sg.soc in ( " + " select unique sg.soc from " +
		 * " soc_family_group sf " + " ,soc_group sg " +
		 * " where sf.soc_group= sg.gp_soc " +
		 * " and sf.family_type in ( 'V', 'S')) ";
		 */
		
		String sql = "select /*+first_rows*/ DISTINCT sg.soc from soc_group sg, soc_family_group sf " 
					+ " where sg.gp_soc = sf.soc_group " 
					+ "and sf.family_type in ( 'V', 'S')";		

		// This query is okay for nwo as ATSGM is a very small table (as of Oct 2014). Hoewver,
		// the table's SOC_GROUP column should be CHAR(9) which would then eliminate the use of RPAD()
		// the causes full table scan. This change, however, requires the deployment to POL1 first then
		// rebuilding the snapshots in REF1,2,3. This activity will cause DB outage. - Chung
		String sql2 = "select distinct sg.soc from soc_group sg , acct_type_soc_group_map atsgm "
					+ " where  SG.GP_SOC =  rpad(atsgm.soc_group,9) "  
					+ " and atsgm.acct_type = ? and atsgm.acct_sub_type = ? ";

		
		Collection<String> pps_socs = getJdbcTemplate().queryForList(sql, new Object[] {}, String.class);
		Collection<String> available_socs_for_account = getJdbcTemplate().queryForList(
						sql2,
						new Object[] { Character.toString(accountType), Character.toString(accountSubType) },
						String.class);

		Collection<String> unavailable_socs = new ArrayList<String>();

		if (!available_socs_for_account.isEmpty()) {
			for (String pps_soc : pps_socs) {
				if (!available_socs_for_account.contains(pps_soc)) {
					unavailable_socs.add(pps_soc);
				}
			}
		} else {
			unavailable_socs = pps_socs;
		}
		
		return unavailable_socs;
	}
	
	// Return the service extended information of a soc 
	// i.e. provinceCodes, account type/subtype, soc groups 
	public Collection<ServiceExtendedInfo> retrieveServiceExtendedInfo(final String[] serviceCodes) {
		
		Collection<ServiceExtendedInfo> serviceExtendedInfoList = new ArrayList<ServiceExtendedInfo>();
		if (serviceCodes == null || serviceCodes.length == 0) {
			return serviceExtendedInfoList;
		}

		// Add a common ResultSetExtractor for socGroups and provinceCodes
		ResultSetExtractor<Map<String, List<String>>> mapExtractor = new ResultSetExtractor<Map<String, List<String>>>() {
			@Override
			public Map<String, List<String>> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<String, List<String>> dataMap = new HashMap<String, List<String>>();
				while (rs.next()) {
					String serviceCode = rs.getString(1).trim();
					String value = rs.getString(2);

					List<String> dataList = dataMap.get(serviceCode);
					if (dataList == null) {
						dataList = new ArrayList<String>();
					}
					dataList.add(value);
					dataMap.put(serviceCode, dataList);
				}
				return dataMap;
			}
		};

		// Set input limit to 200
		List<String> serviceCodeList = Arrays.asList(serviceCodes);
		if (serviceCodeList.size() > 200) {
			serviceCodeList = serviceCodeList.subList(0, 200);
		}

		// Convert serviceCode array into a SQL string
		String serviceCodeString = arrayToDelimitedString(serviceCodeList.toArray(new String[0]), ",", "'", "'");

		String provinceSql = "SELECT SOC, SUB_MARKET AS VALUE " + "FROM SOC_SUBMKT_RELATION " + "WHERE SOC IN (" + serviceCodeString + ")";

		String accountSql = "SELECT SG.SOC, ATP.ACCT_TYPE, ATP.ACCT_SUB_TYPE " + "FROM PRICEPLAN_SOC_GROUPS PSG, SOC_GROUP SG, ACCT_TYPE_PRICEPLAN_SOC_GROUPS ATP "
				+ "WHERE ATP.PRICEPLAN_SOC_GROUP_ID = PSG.PRICEPLAN_SOC_GROUP_ID " + "AND PSG.PRICE_PLAN_GROUP = SG.GP_SOC " + "AND SG.SOC in (" + serviceCodeString + ")";

		String socGroupSql = "SELECT SG.SOC, SG.GP_SOC AS VALUE " + "FROM SOC_GROUP SG " + "WHERE SG.SOC IN (" + serviceCodeString + ")";

		Map<String, List<AccountTypeInfo>> accountTypeMap = getJdbcTemplate().query(accountSql, new ResultSetExtractor<Map<String, List<AccountTypeInfo>>>() {
			@Override
			public Map<String, List<AccountTypeInfo>> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<String, List<AccountTypeInfo>> accountTypeMap = new HashMap<String, List<AccountTypeInfo>>();

				while (rs.next()) {
					String serviceCode = rs.getString(1).trim();
					char[] accountTypeChs = rs.getString(2).toCharArray();
					char[] accountSubTypeChs = rs.getString(3).toCharArray();
					char accountType = accountTypeChs.length > 0 ? accountTypeChs[0] : '0';
					char accountSubType = accountSubTypeChs.length > 0 ? accountSubTypeChs[0] : '0';

					AccountTypeInfo accountTypeInfo = new AccountTypeInfo();
					accountTypeInfo.setAccountType(accountType);
					accountTypeInfo.setAccountSubType(accountSubType);

					List<AccountTypeInfo> accountTypeList = accountTypeMap.get(serviceCode);
					if (accountTypeList == null) {
						accountTypeList = new ArrayList<AccountTypeInfo>();
					}
					accountTypeList.add(accountTypeInfo);
					accountTypeMap.put(serviceCode, accountTypeList);
				}

				return accountTypeMap;
			}
		});

		Map<String, List<String>> provinceMap = getJdbcTemplate().query(provinceSql, mapExtractor);
		Map<String, List<String>> socGroupMap = getJdbcTemplate().query(socGroupSql, mapExtractor);

		for (String serviceCode : serviceCodeList) {
			List<AccountTypeInfo> accountTypes = accountTypeMap.get(serviceCode);
			List<String> provinces = provinceMap.get(serviceCode);
			List<String> socGroups = socGroupMap.get(serviceCode);
			ServiceExtendedInfo serviceExtendedInfo = new ServiceExtendedInfo();
			serviceExtendedInfo.setCode(serviceCode);
			if (accountTypes != null) {
				serviceExtendedInfo.setAccountTypes(accountTypes.toArray(new AccountTypeInfo[0]));
			}
			if (provinces != null) {
				serviceExtendedInfo.setProvinceCodes(provinces.toArray(new String[0]));
			}
			if (socGroups != null) {
				serviceExtendedInfo.setSocGroups(socGroups.toArray(new String[0]));
			}
			if (accountTypes != null || provinces != null || socGroups != null) {
				serviceExtendedInfoList.add(serviceExtendedInfo);
			}
		}
		return serviceExtendedInfoList;
	}
	
	private boolean isRoamLikeHome(String roamLikeHomeIndicator) {
		return Arrays.asList(ROAM_LIKE_HOME_VALUES).contains(roamLikeHomeIndicator);
	}
	

	public Collection<ServiceFamilyTypeInfo> retrievePPSServices() {
		String sql = "select /*+first_rows*/ DISTINCT sg.soc , sf.family_type from soc_group sg, soc_family_group sf "+ " where sg.gp_soc = sf.soc_group "+ "and sf.family_type in ( 'V', 'S')";
		
		return getJdbcTemplate().query(sql, new RowMapper<ServiceFamilyTypeInfo>() {
					@Override
					public ServiceFamilyTypeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
						ServiceFamilyTypeInfo serviceFamilyTypeInfo = new ServiceFamilyTypeInfo();
						serviceFamilyTypeInfo.setSocCode(rs.getString(1));
						serviceFamilyTypeInfo.setFamilyType(rs.getString(2));
						return serviceFamilyTypeInfo;

					}
				});
	}

	public Collection<String> retrieveServicesByAccountType(char accountType , char accountSubType) {

		String sql = "select distinct sg.soc  from soc_group sg , acct_type_soc_group_map atsgm "
				+ " where  SG.GP_SOC =  rpad(atsgm.soc_group,9)  and atsgm.acct_type = ? and atsgm.acct_sub_type = ? ";

		Collection<String> available_socs_for_account = getJdbcTemplate().queryForList(sql,new Object[] { Character.toString(accountType),Character.toString(accountSubType) },String.class);

		return available_socs_for_account;
	}


	public Collection<RoamingServiceNotificationInfo> retrieveRoamingServiceRateInfo(String[] serviceCodes) {

		String serviceCodeString = arrayToDelimitedString(serviceCodes, ",","'", "'");

		String sql = "SELECT s.SOC, s.RLH_IND, ppac.rate as ppac_rate, pprc.rate as pprc_rate FROM SOC s, PP_AC_RATE ppac, PP_RC_RATE pprc "
				+ "WHERE s.soc=ppac.soc(+) AND s.soc=pprc.soc (+) AND s.soc IN ("+ serviceCodeString + ")";
		
		return getJdbcTemplate().query(sql,new RowMapper<RoamingServiceNotificationInfo>() {
					@Override
					public RoamingServiceNotificationInfo mapRow(ResultSet rs,int rowNum) throws SQLException {
						RoamingServiceNotificationInfo roamingServiceNotificationInfo = new RoamingServiceNotificationInfo();
						roamingServiceNotificationInfo.setServiceCode(rs.getString("SOC"));
						roamingServiceNotificationInfo.setRlhInd(rs.getString("RLH_IND"));
						
						int ppac_rate = rs.getInt("ppac_rate");
						//Naresh  :  don't set ppac_rate as default "zero" value if db column is null .
						if (rs.wasNull() == false) {
							roamingServiceNotificationInfo.setPpacRate(String.valueOf(ppac_rate));
						}
						
						int pprc_rate = rs.getInt("pprc_rate");
						// Naresh  :  don't set pprc_rate as default "zero" value if db column is null .
						if (rs.wasNull() == false) {
						roamingServiceNotificationInfo.setPprcRate(String.valueOf(pprc_rate));
						}
						return roamingServiceNotificationInfo;

					}
				});
	}

	public Collection<ReferenceInfo> retrieveMarketingDescriptions() {
		
		String sql = "select soc, marketing_desc_en, marketing_desc_fr from csa_soc_desc csd, logical_date ld where ld.logical_date_type = 'O' and csd.effective_date <= ld.logical_date "
				+ "and (csd.expiration_date is null or csd.expiration_date > ld.logical_date)";
		
		return getJdbcTemplate().query(sql, new ResultSetExtractor<Collection<ReferenceInfo>>() {
			@Override
			public Collection<ReferenceInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<ReferenceInfo> result = new ArrayList<ReferenceInfo>();
				while (rs.next()) {
					ReferenceInfo referenceInfo = new ReferenceInfo();
					referenceInfo.setCode(rs.getString("soc").trim());
					referenceInfo.setDescription(rs.getString("marketing_desc_en"));
					referenceInfo.setDescriptionFrench(rs.getString("marketing_desc_fr"));
					result.add(referenceInfo);
				}
				return result;
			}
		});
	}
}