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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleTypes;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.telus.api.account.AccountManager;
import com.telus.eas.utility.info.AmountBarCodeInfo;
import com.telus.eas.utility.info.BrandSwapRuleInfo;
import com.telus.eas.utility.info.CoverageRegionInfo;
import com.telus.eas.utility.info.DataSharingGroupInfo;
import com.telus.eas.utility.info.EncodingFormatInfo;
import com.telus.eas.utility.info.EquipmentProductTypeInfo;
import com.telus.eas.utility.info.EquipmentStatusInfo;
import com.telus.eas.utility.info.EquipmentTypeInfo;
import com.telus.eas.utility.info.HandsetRoamingCapabilityInfo;
import com.telus.eas.utility.info.LineRangeInfo;
import com.telus.eas.utility.info.LockReasonInfo;
import com.telus.eas.utility.info.NumberRangeInfo;
import com.telus.eas.utility.info.PagerFrequencyInfo;
import com.telus.eas.utility.info.PrepaidRateProfileInfo;
import com.telus.eas.utility.info.PrepaidRechargeDenominationInfo;
import com.telus.eas.utility.info.ProvisioningPlatformTypeInfo;
import com.telus.eas.utility.info.SwapRuleInfo;

/**
 * @author Pavel Simonovsky
 *
 */
public class ReferenceDataDistDao extends JdbcDaoSupport {

	public Collection<EquipmentProductTypeInfo> retrieveEquipmentProductTypes() throws DataAccessException {
		
		String sql = " select distinct p.product_cd, pt.product_type_id " +
		" , pt.product_gp_type_id,pt.des, pt.des_french " +
		" from product p " +
		" ,product_family_group pfg " +
		" ,product_type pt  " +
		" ,product_group_type pgt " +
		" where pfg.product_id=p.product_id " +
		" and   pt.product_type_id=pfg.product_type_id " +
		" and   pt.product_gp_type_id=pfg.product_gp_type_id " +
		" and   pgt.product_gp_type_id=pfg.product_gp_type_id " +
		" and  pgt.exclusive_gp_type_ind='Y'";
		
		return getJdbcTemplate().query(sql, new RowMapper<EquipmentProductTypeInfo>() {
			
			@Override
			public EquipmentProductTypeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				EquipmentProductTypeInfo info = new EquipmentProductTypeInfo();
				
				info.setCode(rs.getString(1));
				info.setProductCode(rs.getString(1));
				info.setProductTypeID(rs.getLong(2));
				info.setProductGroupTypeID(rs.getLong(3));
				info.setDescription(rs.getString(4));
				info.setDescriptionFrench(rs.getString(5) == null ? rs.getString(4) : rs.getString(5));
				
				return info;
			}
		});
	}
	
	public Collection<CoverageRegionInfo> retrieveCoverageRegions() throws DataAccessException {
		
		String sql = "SELECT cr.coverage_region_id, cr.description, cr.province_cd, " +
		" cr.coverage_type, pf.frequency_cd" +
		" FROM coverage_region cr, paging_frequency pf" +
		" WHERE pf.frequency_id (+)= cr.frequency_id";
		
		return getJdbcTemplate().query(sql, new RowMapper<CoverageRegionInfo>() {
			
			@Override
			public CoverageRegionInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				CoverageRegionInfo info = new CoverageRegionInfo();

				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(2));
				info.setProvinceCode(rs.getString(3));
				info.setType(rs.getString(4));
				info.setFrequencyCode(rs.getString(5));
				
				return info;
			}
		});
	}
	
	public Collection<PagerFrequencyInfo> retrievePagerFrequencies() throws DataAccessException {
		
		String sql = " select frequency_cd, frequency_cd " +
		" , start_dt, end_dt " +
		" from paging_frequency p "+
		" where NVL(end_dt, SYSDATE) >= SYSDATE ";
		
		return getJdbcTemplate().query(sql, new RowMapper<PagerFrequencyInfo>() {
			
			@Override
			public PagerFrequencyInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				PagerFrequencyInfo info = new PagerFrequencyInfo();

				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(2));
				info.setStartDate(rs.getDate(3));
				info.setEndDate(rs.getDate(4));
				
				return info;
			}
		});
	}
	
	public Collection<EncodingFormatInfo> retrieveEncodingFormats() throws DataAccessException {
		
		String sql = " select encoder_format_cd, description, encoder_string from encoder_format p ";
		
		return getJdbcTemplate().query(sql, new RowMapper<EncodingFormatInfo>() {
			
			@Override
			public EncodingFormatInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				EncodingFormatInfo info = new EncodingFormatInfo();
				
				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(2));
				info.setEncoderString(rs.getString(3));
				
				return info;
			}
		});
	}
	
	public Collection<EquipmentTypeInfo> retrieveEquipmentTypes() throws DataAccessException {
		
		String sql = " select equip_type_knowbility , equip_type_knowbility_des from kb_equip_type ";
		
		return getJdbcTemplate().query(sql, new RowMapper<EquipmentTypeInfo>() {
			
			@Override
			public EquipmentTypeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {

				EquipmentTypeInfo info = new EquipmentTypeInfo();
				
				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(2));
				
				return info;
			}
		});
	}
	
	public Collection<EquipmentStatusInfo> retrieveEquipmentStatuses() throws DataAccessException {
		
		String sql = " select equipment_status_type_id , equipment_status_id " +
		" , equipment_status_des from equip_status_code ";
		
		return getJdbcTemplate().query(sql, new RowMapper<EquipmentStatusInfo>() {
			
			@Override
			public EquipmentStatusInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				EquipmentStatusInfo info = new EquipmentStatusInfo();
				
				info.setEquipmentStatusTypeID(rs.getLong(1));
				info.setEquipmentStatusID(rs.getLong(2));
				info.setDescription(rs.getString(3));
				info.setDescriptionFrench(rs.getString(3));
				
				return info;
			}
		});
	}
	
	public Collection<AmountBarCodeInfo> retrieveAmountBarCodes() throws DataAccessException {
		
		String sql = "select credit_amount, bar_code, credit_type_cd, " +
		"credit_amount_des, credit_amount_id, credit_amount_des_french from credit_amount ";
		
		return getJdbcTemplate().query(sql, new RowMapper<AmountBarCodeInfo>() {
			
			@Override
			public AmountBarCodeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				AmountBarCodeInfo info = new AmountBarCodeInfo();
				
				info.setAmount(rs.getDouble(1));
				info.setBarCode(rs.getString(2));
				info.setBarCodeReason(rs.getString(3));
				info.setDescription(rs.getString(4));
				info.setCode(String.valueOf(rs.getInt(5)));
				info.setDescriptionFrench(rs.getString(6));
				
				return info;
			}
		});
	}
	
	public Collection<EquipmentTypeInfo> retrievePagerEquipmentTypes() throws DataAccessException {
		
		String sql = " select paging_model_type_cd, description from paging_model_type p ";
		
		return getJdbcTemplate().query(sql, new RowMapper<EquipmentTypeInfo>() {
			
			@Override
			public EquipmentTypeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				EquipmentTypeInfo info = new EquipmentTypeInfo();
				info.setCode(rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(2));
				
				return info;
			}
		});
	}
	
	public Collection<NumberRangeInfo> retrieveNumberRanges() throws DataAccessException {
		
		String sql = " select pnr.npa, pnr.nxx, ltrim(pnr.line_range_start), ltrim(pnr.line_range_end), pnr.prov_platform_id"+
		" from prov_platform_type ppt, phone_number_range pnr "+
		" where ( pnr.expiry_dt is null or pnr.expiry_dt > sysdate ) " +
		" and ppt.prov_platform_id = pnr.prov_platform_id order by 1, 2, 3  ";

		return getJdbcTemplate().query(sql, new ResultSetExtractor<Collection<NumberRangeInfo>>() {
			
			@Override
			public Collection<NumberRangeInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {
				
				List<LineRangeInfo> lineRanges = new ArrayList<LineRangeInfo>();
				List<NumberRangeInfo> numberRanges = new ArrayList<NumberRangeInfo>();
				
				NumberRangeInfo numberRange = new NumberRangeInfo();
				LineRangeInfo lineRange = new LineRangeInfo();
				
				String code = "^";
				
				while (rs.next()) {
					if (!code.equals(rs.getString(1) + rs.getString(2) ) ) {
						if (!code.equals("^")) {
							numberRange.setLineRanges(lineRanges.toArray(new LineRangeInfo[lineRanges.size()]));
							numberRanges.add(numberRange);
							lineRanges = new ArrayList<LineRangeInfo>();
							lineRange = new LineRangeInfo();
						}
						numberRange = new NumberRangeInfo();
						numberRange.setNPA(rs.getInt(1));
						numberRange.setNXX(rs.getInt(2));
						lineRange.setStart(rs.getInt(3));
						lineRange.setEnd(rs.getInt(4));
						lineRange.setProvisioningPlatformId(rs.getInt(5));
						lineRanges.add(lineRange);
					}
					else {
						lineRange = new LineRangeInfo();
						lineRange.setStart(rs.getInt(3));
						lineRange.setEnd(rs.getInt(4));
						lineRange.setProvisioningPlatformId(rs.getInt(5));
						lineRanges.add(lineRange);
					}
					code = rs.getString(1) + rs.getString(2);
				}
				numberRange.setLineRanges(lineRanges.toArray(new LineRangeInfo[lineRanges.size()]));
				numberRanges.add(numberRange);
				
				return numberRanges;
			}
		});
	}
	
	public Collection<ProvisioningPlatformTypeInfo> retrieveProvisioningPlatformTypes() throws DataAccessException {
		
		String sql = "select prov_platform_group, prov_platform_id, prov_platform_cd from prov_platform_type";
		
		return getJdbcTemplate().query(sql, new RowMapper<ProvisioningPlatformTypeInfo>() {
			
			@Override
			public ProvisioningPlatformTypeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				ProvisioningPlatformTypeInfo info = new ProvisioningPlatformTypeInfo();
				
				info.setProvisioningPlatformGroup(rs.getString(1).charAt(0));
				info.setProvisioningPlatformId(rs.getInt(2));
				info.setProvisioningPlatformCd(rs.getString(3));
				
				return info;
			}
		});
	}
	
	public Collection<SwapRuleInfo> retrieveSwapRules() throws DataAccessException {
		
		String sql = "SELECT sg.swap_group_id, sg.swap_group_des, " +
		"       pcx1.product_type_knowbility, pcx1.equip_type_knowbility, pc1.product_class_cd, " +
		"       pcx2.product_type_knowbility, pcx2.equip_type_knowbility, pc2.product_class_cd, " +
		"       sg.message_id, " +
		"       sgt.swap_type_id, " +
		"       sgt.swap_parameter_cd, sgt.inclusive_ind " +
		"  FROM swap_group sg, " +
		"       swap_group_type sgt, " +
		"       product_class_xref_kb pcx1, " +
		"       product_class_xref_kb pcx2, " +
		"       product_classification pc1, " +
		"       product_classification pc2 " +
		" WHERE sg.warranty_group_id = '99' " +
		"   AND pcx1.technology_type = sg.technology_type_1 " +
		"   AND pcx1.product_class_id = sg.product_class_id_1 " +
		"   AND pcx2.technology_type = sg.technology_type_2 " +
		"   AND pcx2.product_class_id = sg.product_class_id_2 " +
		"   AND pc1.product_class_id = pcx1.product_class_id " +
		"   AND pc2.product_class_id = pcx2.product_class_id " +
		"   AND sgt.swap_group_id = sg.swap_group_id " +
		" ORDER BY sg.swap_group_id, pcx1.product_type_knowbility, pcx1.equip_type_knowbility, pc1.product_class_cd";
		
		return getJdbcTemplate().query(sql, new RowMapper<SwapRuleInfo>() {
			
			@Override
			public SwapRuleInfo mapRow(ResultSet rs, int rowNum) throws SQLException {

				SwapRuleInfo info = new SwapRuleInfo();
				
				info.setSwapGroupId(rs.getLong(1));
				info.setSwapGroupDescription(rs.getString(2));
				info.setProductType1(rs.getString(3));
				info.setEquipmentType1(rs.getString(4));
				info.setProductClass1(rs.getString(5));
				info.setProductType2(rs.getString(6));
				info.setEquipmentType2(rs.getString(7));
				info.setProductClass2(rs.getString(8));
				info.setMessageId(rs.getLong(9));
				info.setSwapType(rs.getString(10));
				info.setSwapApp(rs.getString(11));
				info.setInclusiveApp(Integer.parseInt(rs.getString(12)) == AccountManager.NUMERIC_TRUE);

				return info;
			}
		});
	}
	
	public Collection<BrandSwapRuleInfo> retrieveBrandSwapRules() throws DataAccessException {
		
		String sql = "SELECT bsr.brand_swap_rule_id, bsr.brand_swap_rule_desc_txt, " +
		"       bsr.brand_price_plan_id, bsr.brand_new_product_id, " +
		"       bsr.brand_swap_rule_message_id, " +
		"       bsrt.swap_type_id, xref.swap_parameter_cd, bsrt.inclusive_ind " +
		"  FROM brand_swap_rule bsr, " +
		"       brand_swap_rule_typ bsrt, " +
		"       tm_swap_appl_xref xref " +
		" WHERE bsrt.brand_swap_rule_id = bsr.brand_swap_rule_id " +
		"   AND xref.application_cd = bsrt.application_cd " +
		" ORDER BY bsr.brand_swap_rule_id, bsr.brand_price_plan_id";
		
		return getJdbcTemplate().query(sql, new RowMapper<BrandSwapRuleInfo>() {
			
			@Override
			public BrandSwapRuleInfo mapRow(ResultSet rs, int rowNum) throws SQLException {

				BrandSwapRuleInfo info = new BrandSwapRuleInfo();
				
				info.setBrandSwapRuleId(rs.getLong(1));
				info.setBrandSwapRuleDescription(rs.getString(2));
				info.setBrandId1(rs.getString(3));
				info.setBrandId2(rs.getString(4));
				info.setMessageId(rs.getLong(5));
				info.setSwapType(rs.getString(6));
				info.setSwapApp(rs.getString(7));
				info.setInclusiveApp(Integer.parseInt(rs.getString(8)) == AccountManager.NUMERIC_TRUE);

				return info;
			}
		});
	}
	
	public Collection<LockReasonInfo> retrieveLockReasons() throws DataAccessException {
		
		String call = "{call Client_Equipment.getPCSLockReasons(?)}";
		
		return getJdbcTemplate().execute(call, new CallableStatementCallback<Collection<LockReasonInfo>>() {
			
			@Override
			public Collection<LockReasonInfo> doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {

				List<LockReasonInfo> result = new ArrayList<LockReasonInfo>();
				
				cs.registerOutParameter(1, OracleTypes.CURSOR);;
				cs.execute();
				
				ResultSet rs = (ResultSet) cs.getObject(1);

				try {
					while (rs.next()) {
						LockReasonInfo info = new LockReasonInfo();
						info.setLockReasonId(rs.getLong(1));
						info.setDescription(rs.getString(2));
						info.setDescriptionFrench(rs.getString(2));
	
						result.add(info);
					}
					return result;
				} finally {
					rs.close();
				}
			}
		});
	}
	
	public Collection<PrepaidRechargeDenominationInfo> retrievePrepaidRechargeDenominations() throws DataAccessException {

		String sql = " select card_type_cd, denomination_amt, rate_id, effective_dt from card_type_rate " +
		"where (expiry_dt is null or expiry_dt > sysdate) and effective_dt < sysdate";

		return getJdbcTemplate().query(sql, new RowMapper<PrepaidRechargeDenominationInfo>() {
			
			@Override
			public PrepaidRechargeDenominationInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				PrepaidRechargeDenominationInfo info = new PrepaidRechargeDenominationInfo();
				
				info.setRechargeType(rs.getString(1));
				info.setAmount(rs.getDouble(2));
				info.setRateId(rs.getInt(3));
				info.setCode(info.getAmount()+"_"+info.getRechargeType()+"_"+rs.getDate(4));
				info.setDescription(new Double(info.getAmount()).toString());
				info.setDescriptionFrench(new Double(info.getAmount()).toString());
				
				return info;
			}
		});
	}
	
	public Collection<HandsetRoamingCapabilityInfo>  retrieveHandsetRoamingCapabilityInfo() throws DataAccessException {

		String sql = " select a.param_value, a.param_name, b.param_value  from p3ms_config a , p3ms_config b " +
		"where a.param_group = 'PRODUCT_ROAMING_CAPABILITY_MAP' "+
		"and b.param_group = 'PRODUCT_ROAMING_CAPABILITY_TYPE' "+
		"and a.param_name = b.param_name";
		
		return getJdbcTemplate().query(sql, new RowMapper<HandsetRoamingCapabilityInfo>() {
			
			@Override
			public HandsetRoamingCapabilityInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				HandsetRoamingCapabilityInfo info = new HandsetRoamingCapabilityInfo();
				
				info.setHandsetMode(rs.getString(1));
				info.setRoamingType(rs.getString(2));
				info.setRoamingValue(rs.getString(3));
				
				return info;
			}
		});
	}
	
	public Collection<PrepaidRateProfileInfo> retrievePrepaidRates() throws DataAccessException {
		
		String sql = " select c.prepaid_service_charge_id, r.rate_id, f.feature_id, c.charge_amt, c.country_cd, c.application_cd "+
		"from prepaid_service_charge c, prepaid_service_feature f, prepaid_service_rate r "+
		"where c.effective_start_ts < sysdate and c.effective_stop_ts > sysdate and c.service_id = f.service_id and c.prepaid_service_rate_id = r.prepaid_service_rate_id";

		return getJdbcTemplate().query(sql, new ResultSetExtractor<Collection<PrepaidRateProfileInfo>>() {
			
			@Override
			public Collection<PrepaidRateProfileInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {
				
				Map<String, PrepaidRateProfileInfo> infoMap = new HashMap<String, PrepaidRateProfileInfo>();

				while(rs.next()) {
					
					PrepaidRateProfileInfo info = new PrepaidRateProfileInfo();
					
					info.setCode(Integer.toString(rs.getInt(1)));
					info.setRateId(rs.getInt(2));
					info.setRateTypeId(rs.getString(3));
					info.setRate(rs.getDouble(4));
					info.setCountryCode(rs.getString(5));
					
					String key = info.getRateId()+info.getRateTypeId()+info.getCountryCode();
					if (infoMap.containsKey(key)) {
						info = infoMap.get(key);
						if (null != rs.getString(6) && !("".equals(rs.getString(6).trim()))) {
							info.addApplicationIds(rs.getString(6));
						}
					}
					else {
						if (null != rs.getString(6) && !("".equals(rs.getString(6).trim()))) {
							info.addApplicationIds(rs.getString(6));
						}
					}
					infoMap.put(key,info);
				}
				
				return infoMap.values();
			}
		}); 
	}
	
	public Collection<DataSharingGroupInfo> retrieveDataSharingGroups() throws DataAccessException {
		
		String sql = " select allow_sharing_group_cd, allow_sharing_group_txt, allow_sharing_group_fr_txt from allow_sharing_group ";

		return getJdbcTemplate().query(sql, new RowMapper<DataSharingGroupInfo>() {
			
			@Override
			public DataSharingGroupInfo mapRow(ResultSet rs, int rowNum) throws SQLException, DataAccessException {
				
				DataSharingGroupInfo info = new DataSharingGroupInfo();
					
				info.setCode( rs.getString(1));
				info.setDescription(rs.getString(2));
				info.setDescriptionFrench(rs.getString(3));
				return info;
			}
		}); 
	}	
}
