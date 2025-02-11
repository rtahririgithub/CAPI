/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.reference.svc;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import com.telus.api.reference.Service;
import com.telus.eas.contactevent.info.RoamingServiceNotificationInfo;
import com.telus.eas.utility.info.AccountTypeInfo;
import com.telus.eas.utility.info.AdjustmentReasonInfo;
import com.telus.eas.utility.info.KnowbilityOperatorInfo;
//import com.telus.eas.utility.info.LetterCategoryInfo;
//import com.telus.eas.utility.info.LetterSubCategoryInfo;
import com.telus.eas.utility.info.LicenseInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.RatedFeatureInfo;
import com.telus.eas.utility.info.ReferenceInfo;
import com.telus.eas.utility.info.ServiceEditionInfo;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.eas.utility.info.ServiceSetInfo;

/**
 * @author Pavel Simonovsky
 *
 */

@Test
@ContextConfiguration(locations = "classpath:application-context-test.xml")
//@ActiveProfiles({"remote","st101a"})
//@ActiveProfiles("standalone")
//@ActiveProfiles({ "remote", "pt140" })
@ActiveProfiles({"remote", "local"})
public class ReferenceDataFacadeTest extends AbstractTestNGSpringContextTests {

	static {

		System.setProperty("weblogic.Name", "standalone");

		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("weblogic.security.SSL.ignoreHostnameVerification", "true");

	}

	@Autowired
	private ReferenceDataFacade facade;

	@Autowired
	private ReferenceServiceController controller;

	@SuppressWarnings("rawtypes")
	@Test
	public void getAdjustmentReason() throws Throwable {
		AdjustmentReasonInfo info = facade.getAdjustmentReason("MTSMIG");
		System.out.println(info);
	}
	@Test
	public void getDataSharingPricingGroups() throws Exception {
		Map map = facade.getDataSharingPricingGroups();
		System.out.println(map);
	}

	@Test
	public void getAccountTypes() throws Exception {
		System.out.println(facade.getAccountTypes());
	}

	@Test
	public void getpriceplans() throws Exception {
		
		String productType = "C";
		String provinceCode = "ON";
		String accountType = "I";
		String accountSubType = "R";
		String networkType = "9";
		String equipmentType = "P";
		int brandId = 1;
		PricePlanInfo[] pricePlanInfo = facade.getPricePlans(productType, equipmentType, provinceCode, accountType, accountSubType, brandId);
		for (PricePlanInfo pricePlanInfo2 : pricePlanInfo) {
			System.out.println(pricePlanInfo2.getCode() + " Description \t" + pricePlanInfo2.getDescription());
		}
	}

	@Test
	public void getKBOperator() throws Throwable {
		KnowbilityOperatorInfo info = facade.getKnowbilityOperator("9870329");
		System.out.println(info);
	}

	@Test
	public void getPDAMandatoryServiceList() throws Exception {
		ServiceSetInfo[] infos = facade.getMandatoryServices("PVC50NAT", "PDA", "C", "P", "ON", "I", "R", 1);
		assertEquals("PDA", infos[0].getCode());
	}

	@Test
	public void getPDAMandatoryServiceListDescription() throws Exception {
		
		ServiceSetInfo[] infos = facade.getMandatoryServices("PTS45", "PDA", "C", "P", "ON", "I", "R", 1);

		for (ServiceSetInfo serviceSetInfoObject : infos) {
			ArrayList<ServiceInfo> serviceInfos = new ArrayList<ServiceInfo>();
			for (Service service : serviceSetInfoObject.getServices()) {
				serviceInfos.add((ServiceInfo) service);
			}
			ServiceInfo[] list = serviceInfos.toArray(new ServiceInfo[serviceInfos.size()]);
			for (ServiceInfo li : list) {
				System.out.println(li);
			}
		}
		assertEquals("PDA Mandatory Services", infos[0].getDescription());
	}

	@Test
	public void getRIMMandatoryServiceList() throws Exception {
		ServiceSetInfo[] infos = facade.getMandatoryServices("PTS45", "RIM", "C", "Z", "ON", "I", "R", 1);
		assertEquals("RIM", infos[0].getCode());
	}

	@Test
	public void getRIMMandatoryServiceListDescription() throws Exception {
		ServiceSetInfo[] infos = facade.getMandatoryServices("PTS45", "RIM", "C", "Z", "ON", "I", "R", 1);
		assertEquals("RIM Mandatory Services", infos[0].getDescription());
	}

	@Test
	public void getProvinceCodeByKBProvinceCode() throws Exception {
		
		String code = facade.getProvinceCodeByKBProvinceCode("ON");
		System.out.println("facade.getProvinceCodeByKBProvinceCode(ON) " + code);
		code = facade.getProvinceCodeByKBProvinceCode("AB");
		System.out.println("facade.getProvinceCodeByKBProvinceCode(AB) " + code);
		code = facade.getProvinceCodeByKBProvinceCode("BC");
		System.out.println("facade.getProvinceCodeByKBProvinceCode(BC) " + code);
		code = facade.getProvinceCodeByKBProvinceCode("MB");
		System.out.println("facade.getProvinceCodeByKBProvinceCode(MB) " + code);
		code = facade.getProvinceCodeByKBProvinceCode("NB");
		System.out.println("facade.getProvinceCodeByKBProvinceCode(NB) " + code);
		code = facade.getProvinceCodeByKBProvinceCode("NF");
		System.out.println("facade.getProvinceCodeByKBProvinceCode(NF) " + code);
		code = facade.getProvinceCodeByKBProvinceCode("NS");
		System.out.println("facade.getProvinceCodeByKBProvinceCode(NS) " + code);
		code = facade.getProvinceCodeByKBProvinceCode("NT");
		System.out.println("facade.getProvinceCodeByKBProvinceCode(NT) " + code);
	}

	@Test
	public void getLogicalDate() throws Exception {
		
		System.out.println("Billing Date [" + facade.getLogicalDate() + "]");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		System.out.println("current Date [" + cal.getTime() + "]");
	}

	@Test
	public void clearCache() throws Exception {
				controller.clearCache("PricePlan");
		controller.clearCache("IncludedPromotion");
		controller.clearCache("PricePlanSummary");
	}

		
	@Test
	public void refreshAllCache() throws Exception {
		//t3://um-generalutilities-pr-a.tmi.telus.com:52152
		//t3://um-generalutilities2-pr-a.tmi.telus.com:52153
		System.out.println("Running..");
		controller.refreshAllCache();
	}

	@Test
	public void refreshCache() throws Exception {
		controller.refreshCache("RegularService");
	}

	@Test
	public void refreshRefPdsCache() throws Exception {
		controller.refreshCache("REFPDS");
	}

	@Test
	public void getVOLTEServiceInfo() throws Exception {
		
		System.out.println("start getVOLTEServiceInfo");
		ServiceInfo serviceInfo = facade.getRegularService("SVOLTE");
		System.out.println("****** isavilable" + serviceInfo.isAvailable());
		System.out.println("*************************" + serviceInfo);
		System.out.println("end getVOLTEServiceInfo");
	}

//	@Test
//	public void getLetterSubCategory() throws Exception {
//		
////		System.out.println("start getLetterSubCategory1");
////		LetterSubCategoryInfo subCategory = facade.getLetterSubCategory("KPS", "PPR");
////		System.out.println("code" + subCategory.getCode() + "LetterCategory : " + subCategory.getLetterCategory() + "LetterSubCategory : " + subCategory.getLetterSubCategory());
////		System.out.println("end getLetterSubCategory");
//	}

//	@Test
//	public void getLetterCategory() throws Exception {
//		
////		System.out.println("start getLetterCategory");
////		LetterCategoryInfo letterCateforyInfo = facade.getLetterCategory("KPS");
////		System.out.println(letterCateforyInfo);
////		System.out.println("end getLetterCategory");
//	}
//
//	@Test
//	public void retrieveLetterSubCategories() throws Exception {
//		
////		System.out.println("start retrieveLetterSubCategories");
////		LetterSubCategoryInfo[] letterSubCategoryInfoArray = facade.getLetterSubCategories("KPS");
////		for (LetterSubCategoryInfo subCategory : letterSubCategoryInfoArray) {
////			// if (subCategory.getLetterCategory().equals("KPS")) {
////			System.out.println("code" + subCategory.getCode() + "LetterCategory : " + subCategory.getLetterCategory() + "LetterSubCategory : " + subCategory.getLetterSubCategory());
////			// }
////		}
////		System.out.println("end retrieveLetterSubCategories");
//	}

	@SuppressWarnings("rawtypes")
	@Test
	public void containsSwitchCode() throws Exception {
		
		PricePlanInfo info = facade.getPricePlan("XPBCMPN1"); // XPBCMPN1 PBCOFE
		List serviceEditions = facade.getServiceEditions();
		System.out.println("PricePlanInfo: [" + info.toString() + "]");
		for (Object serviceEdition : serviceEditions) {
			ServiceEditionInfo serviceEditionInfo = (ServiceEditionInfo) serviceEdition;
			System.out.println("ServiceEditionInfo: [" + serviceEditionInfo.toString() + "]");
			System.out.println("Testing RC edition switch code: [" + serviceEditionInfo.getCode() + "] against price plan [" + info.getCode().trim() + "]...");
			if (info.getSeatType().equals("STRT")) {
				if (info.containsSwitchCode(serviceEditionInfo.getCode())) {
					System.out.println("Price plan [" + info.getCode().trim() + "] CONTAINS RC edition switch code: [" + serviceEditionInfo.getCode() + "].");
				} else {
					System.out.println("Price plan [" + info.getCode().trim() + "] DOES NOT CONTAIN RC edition switch code: [" + serviceEditionInfo.getCode() + "].");
				}
			} else if (info.getSeatType().equals("OFFC")) {
				if (Arrays.asList(info.getFamilyTypes()).contains((serviceEditionInfo.getFamilyType()))) {
					System.out.println("Price plan [" + info.getCode().trim() + "] IS THE SAME FAMILY TYPE AS RC edition switch code: [" + serviceEditionInfo.getCode() + "].");
				} else {
					System.out.println("Price plan [" + info.getCode().trim() + "] IS NOT THE SAME FAMILY TYPE AS RC edition switch code: [" + serviceEditionInfo.getCode() + "].");
				}
			}
		}
	}

	@Test
	public void retrieveRegularServices() throws Exception {
		ServiceInfo service = facade.getRegularService("XSERI10C ");
		System.out.println(service.getCode());
		for (RatedFeatureInfo feature : service.getFeatures0()) {
			System.out.println(feature);
		}
//		for (ServiceInfo service : serviceList) {
//			System.out.println(service.getCode()+","+service.getFeatureCount()+","+service.getDataSharingGroups().length+","+service.getFamilyTypes().length+","+service.getServiceEquipmentTypeInfo().size()+","+service.getAllowedPurchaseFundSourceArray().length+","+service.getAllowedRenewalFundSourceArray().length);
//		}
	}
	
	@Test
	public void printRetrieveRegularServicesSql() throws Throwable {
//		String featureCategory = null;
//		String productType = null;
//		boolean available = false;
//		boolean current = false;
//		String featureCategoryClause = "";
//		String productTypeClause = "";
//		String currentClause = "";
//		String availableClause = "";
//
//		if (featureCategory == null || featureCategory == "") {
//			featureCategoryClause = " and fcr.feature_code(+) = f.feature_code " ;
//		} else {
//			featureCategoryClause = " and fcr.feature_code = f.feature_code " + 
//			" and category_code = '" + featureCategory + "'" ;
//		}
//
//		if (productType != null && !productType.equals("")) {
//			productTypeClause = " and s.product_type = '" + productType + "'";
//		}
//
//		if (available) {
//			availableClause = "and trunc(s.sale_eff_date) <= trunc(sysdate) " +
//			" and (trunc(s.sale_exp_date) > trunc(sysdate) " +
//			" or s.sale_exp_date is null) " +
//			" and s.soc_status = 'A' " +
//			" and s.for_sale_ind = 'Y' ";
//		}
//		
//		if (current) {
//			currentClause = " and s.current_ind = 'Y'";
//		}
//
//		final String sql = "select distinct s.soc " + //1
//		" , s.soc_description " + //2
//		" , s.soc_description_f " + //3
//		" , s.service_type " + //4
//		" , nvl(s.minimum_no_months, 0) " + //5
//		" , '' " + //6
//		" , s.rda_ind " + //7
//		" , s.bl_zero_chrg_suppress_ind " + //8
//		" , f.feature_code " + //9
//		" , f.feature_desc " + //10
//		" , f.feature_desc_f " + //11
//		" , f.dup_ftr_allow_ind " + //12
//		",  f.csm_param_req_ind " + //13
//		" , rf.rc_freq_of_pym " + //14
//		" , nvl(rc.rate, 0) " + //15
//		" , nvl(uc.rate, 0) " + //16
//		" , s.product_type " + //17
//		" , s.soc_status " + //18
//		" , s.for_sale_ind " + //19
//		" , s.sale_eff_date " + //20
//		" , s.sale_exp_date " + //21
//		" , s.current_ind " + //22
//		" , decode(s.product_type, 'C', 'Y', s.inc_cel_ftr_ind) " + //23
//		" , s.inc_dc_ftr_ind " + //24
//		" , s.inc_pds_ftr_ind " + //25
//		" , f.msisdn_ind " + //26
//		" , nvl(f.ftr_service_type, ' ') " + //27
//		" , soc_rel.relation_type " + //28
//		" , '' " + //29
//		" , decode(uspt.soc,null, 'N', 'Y') promo " + //30
//		" , uspt.act_avail_ind " + // 31
//		" , uspt.chng_avail_ind " + //32
//		" , uspt.included_promo_ind " + //33
//		" , s.period_set_code " + //34
//		" , fcr.category_code " + //35
//		" , brc.soc " + //36
//		" , f.switch_code " + //37
//		" , ' ' " + //38
//		" , s.soc_level_code " + //39
//		" , f.product_type " + //40
//		" , ucrf.mpc_ind " + //41
//		" , ucrf.calling_circle_size " + //42
//		" , soc_rel_1.relation_type relation_type_sm" + //43
//		" , f.feature_type " + //44
//		" , f.pool_group_id " + //45
//		" , s.coverage_type " + //46
//		" , f.def_sw_params " +
//		" , s.bill_cycle_treatment_cd " +
//		" , nvl(s.brand_id, 1) brand_id " +	//TBS change
//		" , s.soc_service_type " +	//BF 2.0
//		" , s.soc_duration_hours " + // RLH support
//		" , s.rlh_ind " + // RLH support
//		" , s.soc_discount_group " +
//		" from pp_rc_rate rc " +
//		" , batch_pp_rc_rate brc " +
//		" , pp_uc_rate uc " +
//		" , feature f " +
//		" , rated_feature rf " +
//		" , uc_rated_feature ucrf " +
//		" , feature_category_relation fcr " +
//		" , soc s " +
//		" , (select soc_src, soc_dest, relation_type " +
//		"   from soc_relation " +
//		"   where relation_type = 'F' " +
//		"   union " +
//		"   select soc_src, soc_dest, 'W' " +
//		"   from soc_relation_ext " +
//		"   where relation_type = 'M' " +
//		"   union " +
//		"   select soc_src, soc_dest, 'X' " +
//		"   from soc_relation_ext " +
//		"   where relation_type = 'S') soc_rel " +
//		" , (select soc_dest, soc_src, relation_type " +
//		"   from soc_relation_ext " +
//		"   where relation_type in ('M','S')) soc_rel_1 " + 
//		" , uc_soc_promo_terms uspt " +
//		" where rc.soc(+) = rf.soc  " +
//		" and rc.effective_date(+) = rf.effective_date " +
//		" and rc.feature_code(+) = rf.feature_code " +
//		" and uc.soc(+) = rf.soc " +
//		" and uc.effective_date(+) = rf.effective_date " +
//		" and uc.feature_code(+) = rf.feature_code  " +
//		" and uc.rate_version_num(+) = 0 " +
//		" and f.feature_code = rf.feature_code " +
//		" and f.feature_group = 'SF' " +
//		featureCategoryClause +
//		" and rf.soc = s.soc " +
//		" and exists (select 1 from soc_equip_relation ser where ser.soc = s.soc)" +		
//		" and ucrf.soc(+) = rf.soc " +
//		" and ucrf.feature_code(+) = rf.feature_code " +
//		" and ucrf.effective_date(+) = rf.effective_date " +
//		" and (ucrf.action = (select min(action) " +
//		"     from uc_rated_feature ucrf1 " +
//		"     where ucrf1.soc(+) = ucrf.soc " +
//		"     and ucrf1.effective_date(+) = ucrf.effective_date " +
//		"     and ucrf1.feature_code(+) = ucrf.feature_code) " +
//		"     or ucrf.action is null) " +
//		" and s.service_type != 'P' " +
//		productTypeClause +
//		availableClause +
//		currentClause +
//		" and soc_rel.soc_dest(+) = s.soc " +
//		" and soc_rel_1.soc_src(+) = s.soc " +  // new  
//		" and uspt.soc(+) = s.soc " +
//		" and rc.soc = brc.soc(+) " +
//		" and rc.feature_code = brc.feature_code(+) " +
//		" order by s.soc ";
//		
//		System.out.println(sql);
	}

	@Test
	public void retrieveMarketingDescriptions() throws Exception {
		ReferenceInfo reference = facade.retrieveMarketingDescriptionBySoc("PYCUNLPR");
		System.out.println(reference.toString());
	}

	@Test
	public void retrieveRoamingServiceNotificationInfo() throws Exception {
		
		System.out.println("start retrieveRoamingServiceNotificationInfo ");
		RoamingServiceNotificationInfo[] roamingServiceNotificationInfos = facade.retrieveRoamingServiceNotificationInfo(new String[] { "SCUBADT50", "0SERI15  " });
		for (RoamingServiceNotificationInfo roamingServiceNotificationInfo : roamingServiceNotificationInfos) {
			System.out.println(roamingServiceNotificationInfo.toString());
		}
		System.out.println("end retrieveRoamingServiceNotificationInfo ");
	}

	@Test
	public void getNotificationTemplateSchemaVerison() throws Exception {
		
		System.out.println("start getNotificationTemplateSchemaVerison SRVC_CHNG..");
		String transactionType = "SRVC_CHNG";
		int brandId = 1;
		String accountType = "BR";
		String banSegment = "TCSO";
		String productType = "C";
		String deliveryChannel = "EMAIL";
		String language = "EN";

		String schemaVerison = facade.getNotificationTemplateSchemaVerison(transactionType, brandId, accountType, banSegment, productType, deliveryChannel, language);

		System.out.println("schema template verison from ref pds is " + schemaVerison);
		System.out.println("end getNotificationTemplateSchemaVerison ");

	}

	public void isNotificationEligible() throws Exception {
		
		System.out.println("start isNotificationEligible  SRVC_ADD_ROAM");

		String transactionType = "SRVC_CHNG";
		String originatingeApp = "SMARTDESKTOP";
		int brandId = 1;
		String accountType = "IR";
		String banSegment = "TCSO";

		String productType = "C";

		boolean isNotificationEligible = facade.isNotificationEligible(transactionType, originatingeApp, brandId, accountType, banSegment, productType);

		System.out.println("isNotificationEligible " + isNotificationEligible);
		System.out.println("end isNotificationEligible SRVC_ADD_ROAM ");
	}

	@Test
	public void getIncludedPromotions() throws Throwable {
		
		String pPricePlanCD = "P40SKVM";
		String pEquipmentType = "P";
		String pNetworkType = "H";
		String pProvinceCD = "SK";
		int term = 36;

		long startTime = Calendar.getInstance().getTimeInMillis();
		ServiceInfo[] promotionList = facade.getIncludedPromotions(pPricePlanCD, pEquipmentType, pNetworkType, pProvinceCD, term);
		long endTime = Calendar.getInstance().getTimeInMillis();
		System.out.println(promotionList.length);
		System.out.println("Time elapsed: " + (endTime - startTime));
		for (ServiceInfo s : promotionList) {
			System.out.println(s.getCode());
		}
	}
		
	@Test
	public void isCDASupportedAccountTypeSubType() throws Exception {
		System.out.println("start isCDASupportedAccountTypeSubType...");
		AccountTypeInfo[] accountTypes = facade.getAccountTypes();
		for (AccountTypeInfo info : accountTypes) {
			String accountTypeSubType = String.valueOf(info.getAccountType()) + String.valueOf(info.getAccountSubType());
			System.out.println("isCDASupportedAccountTypeSubType [" + accountTypeSubType + "] = [" + facade.isCDASupportedAccountTypeSubType(accountTypeSubType) + "].");
		}
		System.out.println("end isCDASupportedAccountTypeSubType");
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getLicenses() throws Exception {
		System.out.println("start getLicenses...");
		ServiceInfo bcLicenseService= facade.getRegularService("SBCLM100");
		List<LicenseInfo> licenses = facade.getLicenses();
		for (LicenseInfo info : licenses) {
		//	if(bcLicenseService.getService().containsSwitchCode(info.getCode()))
			System.out.println("find match" +info.getCode());
		}
		System.out.println("end getLicenses");
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getServiceEditions() throws Exception {
		System.out.println("start getServiceEditions...");
		List<ServiceEditionInfo> serviceEditions = facade.getServiceEditions();
		for (ServiceEditionInfo info : serviceEditions) {
			System.out.println(info);
		}
		System.out.println("end getServiceEditions.");
	}

}