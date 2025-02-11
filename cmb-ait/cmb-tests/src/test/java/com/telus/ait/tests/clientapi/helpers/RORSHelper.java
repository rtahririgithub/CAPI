package com.telus.ait.tests.clientapi.helpers;

import com.telus.ait.tests.clientapi.constants.DefaultValues;
import com.telus.tmi.xmlschema.srv.rmo.ordermgmt.resourceorderreferenceservicerequestresponse_1_0.GetAvailableNumberGroups;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resource_order_reference_types_1_0.AccountTypeCode;

public class RORSHelper extends CommonHelper {

	public static GetAvailableNumberGroups createNumberGroups(String accountType, String accountSubType, String productType, String equipmentType, String marketAreaCode) {
		GetAvailableNumberGroups numberGroups = new GetAvailableNumberGroups();
		numberGroups.setAccountType(AccountTypeCode.valueOf(accountType));
		numberGroups.setAccountSubType(accountSubType);
		numberGroups.setProductType(getDefaultValue(productType, DefaultValues.PRODUCT_TYPE));
		numberGroups.setEquipmentType(equipmentType);
		numberGroups.setMarketAreaCode(getDefaultValue(marketAreaCode, DefaultValues.PROVINCE_CODE));
		return numberGroups;
	}
	
}
