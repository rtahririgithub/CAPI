package com.telus.api;

public class ErrorIds {
	
	public static final int BAN_NOT_FOUND = 0;
	public static final int OLD_PASSWORD_INCORRECT = 1;
	public static final int INVALID_PASSWORD_LENGTH = 2;
	public static final int INVALID_PASSWORD_FIRST_LAST_DIGIT = 3;
	public static final int INVALID_PASSWORD_NO_DIGIT = 4;
	public static final int INVALID_PASSWORD_ALREADY_USED = 5;
	
	public static final int TM_CREDIT_CARD_API_MSG_NUM_REJECTED_TRANSACTION = 100;
	
	public static final int CREDIT_CARD_TYPE_NOT_SUPPORTED = 10001;
	public static final int TRANSACTION_NOT_SUCCESSFUL = 10003;
	public static final int INVALID_ACCOUNT_TYPE = 10004;
	public static final int EFFECTIVE_DATE_LESS_THAN_EXPIRY_DATE = 10008;
	public static final int ACCOUNT_CANNOT_BE_TENTATIVE = 10009;
	public static final int FAILED_TO_GET_PRIMARY_NGP_FOR_NETWORK = 10012;
	public static final int DUPLICATE_TALK_GROUP_NAME = 10014;
	public static final int MAX_NUMBER_OF_TALK_GROUPS_EXCEEDED = 10015;
	public static final int AMDOCS_FLEET_ALREADY_ASSOCIATE_TO_THIS_BAN = 10016;
	
	
	public static final int MISSING_AUDIT_HEADER_FOR_CC = 40001;
	public static final int MISSING_CC_INFORMATION = 40002;
	public static final int MISSING_CC_TOKEN = 40003;
	public static final int MISSING_CC_LEADING_DISPLAY_DIGITS = 40004;
	public static final int MISSING_CC_TRAILING_DISPLAY_DIGITS = 40005;
	
	public static final int PREPAID_ADDTOOMANYFEATURES_EXCEPTION=4023;

	/* Product Equipment */
	public static final int UNKNOWN_SERIAL_NUMBER = 10002; //VAL10002
	public static final int UNKNOWN_PRODUCT = 10002; //VAL10002
	public static final int MULTIPLE_ASSOCIATED_HANDSETS = 10002; //VAL10002	
	public static final int PHONE_NUMBER_NOT_EXIST = 10002; //VAL10002
	public static final int SUBSCRIBER_ID_REQUIRED = 20002;// VAL20002
	public static final int TALK_GROUPS_EMPTY = 20004; //"VAL20004"
	public static final int MULE_NOT_FOUND = 20028; //VAL20028	
	public static final int MULTIPLE_MULE_FOUND = 20028; //VAL20028
	public static final int SIM_NOT_FOUND = 20027; //VAL20027
	public static final int SUBSCRIBER_CONTACT_INFO_REQUIRED= 20030; //VAL20030
	public static final int UNKONWN_SERIAL_NUMBER_MANUFACTURER = 10007; //VAL10007
	
	public static final int IMSI_NOT_FOUND = 10002; //VAL10002
	public static final int CARD_NUMBER_NULL = 10002; //VAL10002	
	public static final int INVALID_CARD_NUMBER_LENGTH = 10002; //VAL10002	
	public static final int UNKONWN_USER_ID = 30009; //EQU30009	
	public static final int UNKONWN_PHONE_NUMBER = 30010; //EQU30010
	public static final int PHONE_NUMBER_NULL = 30013; //EQU30013	
	public static final int CARD_PIN_ATTEMPT_FAILURE = 30020; 	
	public static final int TOO_MANY_PIN_ATTEMPT_FAILURE = 30021; 
	public static final int CARD_INFORMATION_NOT_FOUND = 30001; //EQU30001
	public static final int CYPHER_PIN_NOT_FOUND = 30004; //EQU30004
	public static final int INVALID_PIN = 30005; //SYS00017
	
	public static final int EQUIPMENT_SERIAL_NUMBER_NULL = 30013; //EQU30013	
	public static final int SERIAL_NUMBER_NULL = 10002; //VAL10002	
	public static final int INVALID_SERAIL_NUMBER_LENGTH= 10002; //VAL10002	
	public static final int INVALID_SERAIL_NUMBER= 20038; //VAL20038	
	public static final int TECHNOGOTY_TYPE_NULL = 30011; //EQU30011	
	public static final int BILLING_TYPE_NULL = 30012; //EQU30012
	public static final int CARD_TYPE_NULL = 30014; //EQU30014	
	public static final int INVALID_CARD_STATUS = 30007;//EQU30007
	public static final int VALIDATAION_FAILED = 30008;//EQU30008
	public static final int GET_CARD_STATUS_FAILED = 30006;//EQU30006
	
	public static final int UNKNOWN_PAGING_PRODUCT = 30015;//EQU30015
	public static final int UNKNOWN_FREQUENCY_CODE = 30016;//EQU30016
	public static final int INVALID_EQUIPMENT_TYPE = 30017;//EQU30017
	
	public static final int PREPAID_SYSTEM_ERROR = 100008;//SYS00008
	public static final int LDAP_CONFIGURATION_ERROR = 100019;//SYS00019
	public static final int NRT_EJB_CONNECTION_EXCEPTION = 100010;//SYS00010
	
	public static final int SUBSCRIBER_NOT_FOUND = 20002;//APP20002
	public static final int SUBSCRIBER_CANCELED_STATUS = 20010;//APP20010
	public static final int SUBSCRIBER_SUSPENDED_STATUS = 20011;//APP20011
	

	
	
}
