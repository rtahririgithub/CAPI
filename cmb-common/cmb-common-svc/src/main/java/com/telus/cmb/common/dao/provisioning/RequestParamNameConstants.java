package com.telus.cmb.common.dao.provisioning;

import java.util.HashMap;
import java.util.Map;

import com.telus.api.reference.Brand;

public interface RequestParamNameConstants {

	// Seat level action types
	public static final String PROVISIONING_SEAT_ADD_ACTION_TYPE = "ADD";
	public static final String PROVISIONING_SEAT_CANCEL_ACTION_TYPE = "DAC";
	public static final String PROVISIONING_SEAT_SUSPEND_ACTION_TYPE = "SUS";
	public static final String PROVISIONING_SEAT_RESUME_ACTION_TYPE = "RES";
	public static final String PROVISIONING_SEAT_CHANGE_ACTION_TYPE = "CHG";

	// Account level action types
	public static final String PROVISIONING_ACCOUNT_ADD_ACTION_TYPE = "ADDACT";
	public static final String PROVISIONING_ACCOUNT_CANCEL_ACTION_TYPE = "DACACT";
	public static final String PROVISIONING_ACCOUNT_SUSPEND_ACTION_TYPE = "SUSACT";
	public static final String PROVISIONING_ACCOUNT_RESUME_ACTION_TYPE = "RESACT";
	public static final String PROVISIONING_ACCOUNT_CHANGE_ACTION_TYPE = "CHGACT";
	public static final String PROVISIONING_ACCOUNT_RESTORE_ACTION_TYPE = "RSTACT";
	
	// Fixed request parameter values
	public static final String BUSINESS_FREEDOM_BILLING_TYPE = "POSTPAID";
	public static final String SOURCE_SYSTEM_CODE = "CAPI_VOIP";
	public static final String PRICE_PLAN_CODE_STANDARD_VALUE = "BCPSTD";
	public static final String PRICE_PLAN_CODE_QUEBEC_STANDARD_VALUE = "BCPSQC";
	public static final String IGNORE_NOTIFY_VENDOR_YES = "Y";
	public static final String IGNORE_NOTIFY_VENDOR_NO = "N";
	public static final String DATE_TIMESTAMP = "date_timestamp";
	
	// Provisioning seat type values	
	public static final String PROVISIONING_STARTER_SEAT_VALUE = "STARTER";
	public static final String PROVISIONING_OFFICE_SEAT_VALUE = "OFFICE";
	public static final String PROVISIONING_PROFESSIONAL_SEAT_VALUE = "PROFESSIONAL";
	
	// Miscellaneous constants
	public static final String[] PROVINCE_CODES_QUEBEC = { "PQ", "QC" };
	
	// Enumerator to translate the KB Brand ID to acceptable brand name for Provisioning
	public enum BrandTranslator {
		
		TELUS (Brand.BRAND_ID_TELUS, "TELUS"),
		KOODO (Brand.BRAND_ID_KOODO, "KOODO"),
		WALMART (Brand.BRAND_ID_WALMART, "WALMART"),
		CLEARNET (Brand.BRAND_ID_CLEARNET, "CLEARNET"),
		NOT_APPLICABLE (Brand.BRAND_ID_NOT_APPLICABLE, "NOT_APPLICABLE");

		private int code;
		private String description;
		private static Map<Integer, String> map = initializeMap();

		private BrandTranslator(int code, String description) {
			this.code = code;
			this.description = description;
		}

		public int getCode() {
			return this.code;
		}

		public String getDescription() {
			return this.description;
		}

		public static String translate(int code) {
			
			String description = map.get(code);
			if (description == null) {
				description =  map.get(Brand.BRAND_ID_NOT_APPLICABLE);
			}

			return description;
		}

		private static Map<Integer, String> initializeMap() {
			
			Map<Integer, String> map = new HashMap<Integer, String>();
			for (BrandTranslator bt : BrandTranslator.values()) {
				map.put(bt.getCode(), bt.getDescription());
			}

			return map;
		}
	}
	
	// Begin: Provisioning values taken from ProvisioningGatewayRequestParameterNameConstant class
	public static final String PRICE_PLAN_CODE = "price_plan_code";
	public static final String EFFECTIVE_DATE = "effective_date";
	public static final String PHONE_NUMBER_LIST = "phone_number_list";
	public static final String PHONE_NUMBER = "phone_number";
	public static final String MAIN_VOIP_PHONE_NUMBER = "main_voip_phone_number";
	public static final String EMAIL = "email";
	public static final String COMMENT = "comment";
	public static final String TIME_ZONE = "time_zone";
	public static final String LANGUAGE = "language";
	public static final String IGNORE_NOTIFY_VENDOR = "ignore_notify_vendor";
	public static final String REASON = "reason";
	
	public static final String SUBSCRIPTION_ID = "subscription_id";
	public static final String SUBSCRIPTION_ID_LIST = "subscription_id_list";
	
	public static final String TARGET_SUBSCRIPTION_ID_LIST = "target_subscription_id_list";
	public static final String TARGET_PHONE_NUMBER_LIST = "target_phone_number_list";
	
	public static final String CONTACT_FIRST_NAME = "contact_first_name";
	public static final String CONTACT_LAST_NAME = "contact_last_name";
	public static final String CONTACT_PHONE_NUMBER = "contact_phone_number";
	public static final String CONTACT_BUSINESS_ADDRESS_STREET = "contact_business_address_street";
	public static final String CONTACT_BUSINESS_ADDRESS_CITY = "contact_business_address_city";
	public static final String CONTACT_BUSINESS_ADDRESS_PROVINCE = "contact_business_address_province";
	public static final String CONTACT_BUSINESS_ADDRESS_COUNTRY = "contact_business_address_country";
	public static final String CONTACT_BUSINESS_ADDRESS_POSTAL_CODE = "contact_business_address_postal_code";
	public static final String CONTACT_BUSINESS_NAME = "contact_business_name";

	public static final String SEAT_TYPE = "seat_type";
	public static final String SEAT_CONTACT_BUSINESS_PHONE = "seat_contact_business_phone";
	public static final String SEAT_CONTACT_COMPANY_NAME = "seat_contact_company_name";
	public static final String SEAT_CONTACT_ADDRESS_STREET = "seat_contact_address_street";
	public static final String SEAT_CONTACT_ADDRESS_CITY = "seat_contact_address_city";
	public static final String SEAT_CONTACT_ADDRESS_PROVINCE = "seat_contact_address_province";
	public static final String SEAT_CONTACT_ADDRESS_COUNTRY = "seat_contact_address_country";
	public static final String SEAT_CONTACT_ADDRESS_POSTAL_CODE = "seat_contact_address_postal_code";
	
	public static final String VOIP_PHONE_TYPE_VOICE = "VoiceOnly";
	public static final String VOIP_PHONE_TYPE_FAX = "FaxOnly";
	public static final String VOIP_PHONE_TYPE_VOICEFAX = "VoiceFax";
	public static final String VOIP_PHONE_TYPE_LIST = "voip_phone_type_list";
	public static final String VOIP_PHONE_NUMBER_LIST = "voip_phone_number_list";
	public static final String VOIP_ACTION_ADD = "ADD";
	public static final String VOIP_ACTION_DELETE = "DELETE";
	public static final String VOIP_ACTION_REASSIGN = "REASSIGN";
	public static final String VOIP_ACTION_LIST = "voip_action_list";
	// End: Provisioning values taken from ProvisioningGatewayRequestParameterNameConstant class
		
}