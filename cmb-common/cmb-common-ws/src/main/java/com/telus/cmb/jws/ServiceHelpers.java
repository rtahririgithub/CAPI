package com.telus.cmb.jws;

import java.math.BigInteger;
import java.util.List;

import com.telus.cmb.common.util.StringUtil;
import com.telus.eas.utility.info.PricePlanSelectionCriteriaInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.ProvinceCode;

public class ServiceHelpers {

	/**
	 * To be used by services to construct the PricePlanSelectionCriteriaInfo object from individual input parameters.
	 */
	public static PricePlanSelectionCriteriaInfo constructPricePlanSelectionCriteria(String productType, String equipmentType, ProvinceCode provinceCode, String accountType, 
			String accountSubType, Integer brandId, String activityCode, boolean currentPlansOnlyInd, boolean availableForActivationOnlyInd, 
			String activityReasonCode, String term, String networkType, boolean initialActivationInd, 
			List<Long> productPromoTypes, String seatTypeCd) {
		
		Long[] productPromoTypeArray = new Long[0];
		if (productPromoTypes != null) {
			productPromoTypeArray = productPromoTypes.toArray(new Long[productPromoTypes.size()]);
		}
		
		PricePlanSelectionCriteriaInfo criteriaInfo = new PricePlanSelectionCriteriaInfo();
		criteriaInfo.setProductType(productType);
		criteriaInfo.setEquipmentType(equipmentType);
		criteriaInfo.setProvinceCode(provinceCode.value());
		criteriaInfo.setAccountType(StringUtil.toChar(accountType));
		criteriaInfo.setAccountSubType(StringUtil.toChar(accountSubType));
		criteriaInfo.setBrandId(brandId);
		criteriaInfo.setActivityCode(activityCode);
		criteriaInfo.setCurrentPlansOnly(currentPlansOnlyInd);
		criteriaInfo.setAvailableForActivationOnly(availableForActivationOnlyInd);
		criteriaInfo.setActivityReasonCode(activityReasonCode);
		criteriaInfo.setTerm(Integer.valueOf(term));
		criteriaInfo.setNetworkType(networkType);
		criteriaInfo.setInitialActivation(initialActivationInd);
		criteriaInfo.setProductPromoTypes(productPromoTypeArray);
		criteriaInfo.setSeatTypeCode(seatTypeCd);
		
		return criteriaInfo;
	}
	
}
