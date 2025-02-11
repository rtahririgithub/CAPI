package com.telus.ait.tests.clientapi.constants;

import java.util.Calendar;
import java.util.Date;


public class DefaultValues {

	//Base Account
	public static final String ACCOUNT_TYPE = "I";
	public static final String ACCOUNT_SUBTYPE = "R";
	public static final String BRAND_ID_TELUS = "1";
	public static final String BRAND_ID_KOODO = "3";
	public static final String HOME_PROVINCE = "ON";
	public static final String PIN = "1111";
	public static final String EMAIL = "mjtest@telus.ca";
	public static final String LANGUAGE = "EN"; 
	public static final String DEALER_CODE = "A001000001";
	public static final String SALES_REP_CODE = "0000";
	public static final String CONTACT_PHONE = "9056543434";
	public static final String HOME_PHONE = "9056543434";
	public static final String TITLE = "MR.";
	
	//Consumer Name
	public static final String FIRST_NAME = "CAPI";
	public static final String LAST_NAME = "TAFTEST";
	public static final String NAME_FORMAT = "P";
	public static final String LEGAL_BUSINESS_NAME = "TAF TESTING CO.";
	public static final String INCORP_NUM = "123456";
	public static final Date INCORP_DATE = Calendar.getInstance().getTime();
	
	//Address
	public static final String ADDRESS_TYPE = "C";
	public static final String CITY = "Toronto";
	public static final String PROVINCE_CODE = "ON"; 
	public static final String POSTAL_CODE = "M1H3G2";
	public static final String COUNTRY = "CAN";
	public static final String STREET_NAME = "300 Consilium Pl";
	
	//Personal Credit Info
	public static final String SIN = "333786911";
	public static final String BIRTHDATE_FORMAT = "yyyy, MM, dd"; 
	public static final String BIRTHDATE = "1973, 05, 05";
		
	//Audit Info
	public static final String AUDIT_USER_ID = "296G";
	public static final String AUDIT_SALES_REP_ID = "296G";
	public static final String CHANNEL_ORG_ID = "10351326";
	public static final String OUTLET_ID = "2995";
	public static final String ORG_APP_ID = "12373";
	
	//Account header and app info
	public static final String CUST_ID = "296G";
	public static final String IP_ADDRESS = "142.63.44.30";
	public static final String APP_ID = "12373";
	public static final String USER_ID = "296G";
	public static final String APP_IP_ADDRESS = "142.63.44.30";
	
	//Reserve Phone Number
	public static final Boolean LIKE_MATCH_IND = Boolean.FALSE;
	public static final Boolean ASIAN_FRIENDLY_IND = Boolean.FALSE;
	public static final Boolean PORT_IN_RESERVE_REQUIRED_IND = Boolean.FALSE;
	public static final Boolean OFFLINE_RESERVATION_IND = Boolean.FALSE;	
	
	//Release Phone Number
	public static final Boolean CANCEL_PORT_IN_IND = Boolean.FALSE;
	
	//Prepaid account
	public static final String BUSINESS_ROLE = "DEALERACTV";
	public static final String ACTIVATION_TYPE = "1";
	public static final String ACTIVATION_CODE = "";
	public static final Integer RENEWAL_FUND_SOURCE = 2;
	public static final Integer PURCHASE_FUND_SOURCE = 2;
	public static final boolean AUTO_RENEW_IND = true;
	public static final boolean PREPAID_IND = true;	
	
	//Activate Subscriber
	public static final String ACTIVITY_REASON_CODE = "POKO";
	public static final String ACTIVATION_FEE_CHARGE_CODE = "ACT1";
	public static final String SUBSCRIPTION_ROLE_CODE = "AA";
	public static final String PRODUCT_TYPE = "C";
	public static final String REASON_CODE = "PPD";
	public static final String CONTRACT_TERM = "24";
	public static final String MEMO_TEXT = "Client API TAF Test";
	public static final Boolean WAIVE_SEARCH_FEE_IND = Boolean.FALSE;
	public static final Boolean DEALER_HAS_DEPOSIT_IND = Boolean.FALSE;
	public static final Boolean NOTIFICATION_SUPPRESSION_IND = Boolean.FALSE;
	public static final Boolean EQUIPMENT_SERVICE_MATCH_IND = Boolean.FALSE;
	public static final Boolean PRICEPLAN_SERVICE_GROUP_IND = Boolean.FALSE;
	public static final Boolean PROVINCE_SERVICE_MATCH_IND = Boolean.FALSE;
		
}
