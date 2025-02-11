package com.telus.api;

public class ErrorCodes {

	public static final String TM_CREDIT_CARD_API_MSG_NUM_REJECTED_TRANSACTION = "100";

	public static final String ACCOUNT_TYPE_NOT_IMPLEMENTED = "APP10001";
	public static final String BAN_NOT_FOUND = "APP10004";
	public static final String CREDIT_CHECK_MEMO_NOT_FOUND = "APP10006";
	public static final String MEMO_NOT_FOUND = "APP10011";
	public static final String FAILED_TO_GET_PRIMARY_NGP_FOR_NETWORK = "APP10012";
	public static final String SUBSCRIBER_QUERY_LIMIT_EXCEEDED = "APP10013";

	public static final String CREDIT_CARD_TYPE_NOT_SUPPORTED = "VAL10001";
	public static final String ACCOUNT_MUST_BE_TENTATIVE = "VAL10003";
	public static final String ACCOUNT_MUST_BE_INDIVIDUAL_REGULAR = "VAL10004";
	public static final String EFFECTIVE_DATE_MUST_BE_BEFORE_EXPIRY_DATE = "VAL10008";
	public static final String ACCOUNT_CANNOT_BE_TENTATIVE = "VAL10009";
	public static final String FLEET_NOT_ASSOCIATED_TO_BAN = "VAL10010";
	public static final String DUPLICATE_TALK_GROUP_NAME = "VAL10014";
	public static final String MAX_NUMBER_OF_TALK_GROUPS_EXCEEDED = "VAL10015";
	public static final String AMDOCS_FLEET_ALREADY_ASSOCIATE_TO_THIS_BAN = "VAL10016";
	public static final String ACCOUNT_TYPE_MUST_BE_CONSUMER = "VAL10020";
	public static final String ACCOUNT_STATUS_MUST_BE_OPEN_OR_SUSPENDED = "VAL10021";

	public static final String LIKE_NUMBER_MUST_BE_10_DIGITS = "VAL20003";
	public static final String TALK_GROUPS_FROM_SAME_FLEET = "VAL20005";
	public static final String DISPATCH_RESOURCE_NOT_ALLOCATED = "VAL20006";

	public static final String MEMBER_ID_NOT_FOUND = "VAL20033";
	public static final String SUBSCRIBER_CANNOT_BE_ON_PTN_FLEET = "VAL20034";
	public static final String NEW_SERIAL_IS_IN_USE = "VAL20024";
	public static final String PHONE_NUMBER_NOT_AVAILABLE = "VAL20026";
	public static final String SUBSCRIBER_DOES_NOT_HAVE_DISPATCH_SERVICE = "VAL20036";
	public static final String NO_FAX_FEATURE_FOUND = "VAL20040";
	public static final String SUBSCRIBER_DOES_NOT_HAVE_CONTRACT_START_DATE = "VAL20042";

	public static final String SERVICE_AGREEMENT_DOES_NOT_SUPPORT_DISPATCH_RESOURCE = "VAL20035";

	public static final String MISSING_AUDIT_HEADER_FOR_CC = "VAL40001";
	public static final String MISSING_CC_INFORMATION = "VAL40002";
	public static final String MISSING_CC_TOKEN = "VAL40003";
	public static final String MISSING_CC_LEADING_DISPLAY_DIGITS = "VAL40004";
	public static final String MISSING_CC_TRAILING_DISPLAY_DIGITS = "VAL40005";

	public static final String GENERIC_THROWABLE_ERROR_CODE = "SYS00003";
	public static final String CREDIT_CARD_SUBSYSTEM_UNAVAILABLE = "SYS00014";
	public static final String TUXEDO_FAILURE = "SYS00015";
	public static final String LDAP_CONFIGURATION_ERROR = "SYS00019";//SYS00019
	public static final String SUBSCRIBER_NOT_FOUND = "APP20002";
	public static final String APP20003 = "APP20003";
	public static final String SUBSCRIBER_NO_AVAILABLE_PHONENUMBERS = "APP20001";
	public static final String NO_MEMBER_IDS_AVAILABLE_MATCHING_CRITERIA = "APP20004";
	public static final String SUBSCRIBER_CANCELED_STATUS = "APP20010";
	public static final String SUBSCRIBER_SUSPENDED_STATUS = "APP20011";
	public static final String NDP_DIRECTION_INDICATOR_MISSING = "APP20012";
	public static final String BAN_OR_SUBSCRIBER_NOT_FOUND_OR_SUBSCRIBER_NOT_ACTIVE = "APP20020";

	public static final String TALK_GROUPS_EMPTY = "VAL20004";
	public static final String SUBSCRIBER_ID_REQUIRED = "VAL20002";
	public static final String SUBSCRIBER_CONTACT_INFO_REQUIRED = "VAL20030";

	public static final String PREPAID_ADDTOOMANYFEATURES_EXCEPTION = "PREPAID_ADDTOOMANYFEATURES_EXCEPTION";

	public static final String WPS_GENERIC_ERROR = "WPS";

	public static final String KB_CONNECT_ERROR = "KB00001";
	public static final String KB_AMDOCS_SESSION_ERROR = "KB00002";

	/* Product Equipment EJB*/
	public static final String CARD_INFORMATION_NOT_FOUND = "EQU30001";
	public static final String CYPHER_PIN_NOT_FOUND = "EQU30004";
	public static final String UNKNOWN_SERIAL_NUMBER = "VAL10002";
	public static final String UNKNOWN_PRODUCT_CODE = "VAL10002";
	public static final String IMSI_NOT_FOUND = "VAL10002";
	public static final String MULTIPLE_ASSOCIATED_HANDSETS = "VAL10002";
	public static final String UNKNOWN_PRODUCT = "VAL10002";
	public static final String PHONE_NUMBER_NOT_EXIST = "VAL10002";
	public static final String MULE_NOT_FOUND = "VAL20028";
	public static final String MULTIPLE_MULE_FOUND = "VAL20028";
	public static final String SIM_NOT_FOUND = "VAL20027";

	public static final String PREPAID_SYSTEM_ERROR = "SYS00008";

	public static final String INVALID_PIN = "SYS00017";
	public static final String CARD_NUMBER_NULL = "VAL10002";
	public static final String INVALID_CARD_NUMBER_LENGTH = "VAL10002";

	public static final String UNKONWN_USER_ID = "EQU30009";
	public static final String UNKONWN_PHONE_NUMBER = "EQU30010";

	public static final String SERIAL_NUMBER_NULL = "VAL10002";
	public static final String INVALID_SERAIL_NUMBER_LENGTH = "VAL10002";
	public static final String TECHNOGOTY_TYPE_NULL = "EQU30011";
	public static final String BILLING_TYPE_NULL = "EQU30012";
	public static final String CARD_TYPE_NULL = "EQU30014";
	public static final String TOO_MANY_PIN_ATTEMPT_FAILURE = "VAL30021"; ///
	public static final String INVALID_EQUIPMENT_TYPE = "EQU30017";
	public static final String NRT_EJB_CONNECTION_EXCEPTION = "SYS00010";
	public static final String GET_CARD_STATUS_FAILED = "EQU30006";
	public static final String UNKNOWN_PAGING_PRODUCT = "EQU30015";
	public static final String UNKNOWN_FREQUENCY_CODE = "EQU30016";
	public static final String EQUIPMENT_SERIAL_NUMBER_NULL = "EQU30013";
	public static final String VALIDATAION_FAILED = "EQU30008";
	public static final String INVALID_CARD_STATUS = "EQU30007";
	public static final String UNKNOWN_SERIAL_NUMBER_MANUFACTURER = "VAL10007";
	public static final String INVALID_SERAIL_NUMBER = "VAL20038";

	public static final String CREATE_SUBSCRIBER_ERROR_MEMOS_FEES_DISCOUNTS = "APPSM0001_MEMOS_FEES_DISCOUNTS";
	public static final String CREATE_SUBSCRIBER_ERROR_FEES_DISCOUNTS = "APPSM0001_FEES_DISCOUNTS";
	public static final String CREATE_SUBSCRIBER_ERROR_DISCOUNTS = "APPSM0001_DISCOUNTS";

	/**
	 * SubscriberLifecycleFacade
	 */
	public static final String INVALID_INPUT_PARAMETERS = "VAL18888";
	public static final String UNSUPPORTED_NETWORK_SWAP = "VAL18889";
	public static final String EQUIPMENT_TYPE_VIRTUAL = "300001";
	public static final String EQUIPMENT_SERIAL_NUMBER_EXPIRED = "300002";
	public static final String EQUIPMENT_SERIAL_NUMBER_IN_USE = "300003";
	public static final String EQUIPMENT_SERIAL_NUMBER_STOLEN = "300004";
	public static final String EQUIPMENT_PRODUCT_TYPE_MISMATCH = "300005";
	public static final String PREPAID_EQUIPMENT_TYPE_INVALID = "300006";
	public static final String EQUIPMENT_CHANGE_INCOMPATIBLE_TECH_TYPE = "300007";
	public static final String EQUIPMENT_CHANGE_INCOMPATIBLE_BRAND = "300008";
	public static final String EQUIPMENT_CHANGE_MANDATORY_FIELDS_MISSING = "300009";
	public static final String EQUIPMENT_CHANGE_EQUIPMENT_TYPE_IS_NULL = "300010";
	public static final String EQUIPMENT_CHANGE_IMPOSSIBLE_SWAP_TYPES = "300011";
	public static final String EQUIPMENT_CHANGE_NEW_EQUIPMENT_IS_LOST_STOLEN = "300012";
	public static final String EQUIPMENT_CHANGE_INVALID_SWAP_FOR_PREPAID_ACCOUNT = "300013";
	public static final String EQUIPMENT_CHANGE_MANDATORY_EQUIPMENT_INFO_NULL = "300014";
	public static final String EQUIPMENT_CHANGE_NO_LOANER_FOR_SIM2SIM = "300015";
	public static final String EQUIPMENT_CHANGE_REPAIR_ID_IS_MANDATORY_EXCEPT_REPLACEMENT_AND_SIM2SIM = "300016";
	public static final String EQUIPMENT_CHANGE_REPAIR_ID_NOT_UNIQUE_EXCEPT_SIM2SIM = "300017";
	public static final String EQUIPMENT_CHANGE_OLD_NEW_EQUIPMENT_TYPE_NOT_SAME_FOR_REPAIR = "300018";
	public static final String EQUIPMENT_CHANGE_UNKNOWN = "300019";
	public static final String EQUIPMENT_CHANGE_REQUESTOR_ID_IS_MANDATORY_EXCEPT_REPLACEMENT = "300020";

	public static final String INVALID_NETWORK_TYPE = "300100";
	public static final String UNKNOWN_SERVICE = "300101";
	public static final String GET_SERVICEAGREEMENT_FOR_EQUIPCHG_ERROR = "300102";
	public static final String INVALID_SERVICE_CHANGE = "300103";
	public static final String CONTRACT_CHANGE_ERROR = "300104";
	public static final String UNKNOWN_FEATURE = "300105";
	public static final String PHONE_NUMBER_ADDITIONAL_RESERVATION_FAILED = "300106";
	public static final String DUPLICATE_FEATURE = "300107";
	public static final String WPS_ADD_TOOMANY_FEATURES = "300108";
	public static final String PRICEPLAN_CHANGE_ERROR = "300109";
	public static final String INVALID_PRICE_PLAN = "300110";
	public static final String INVALID_KB_TO_PREPAID_SERVICE_ASSOCIATION = "300111";
	public static final String INVALID_PHONE_NUMBER = "300112";

	public static final String UNKNOWN_APPLICATION_CODE = "311000";
	public static final String UNKNOWN_AUDIENCE_TYPE = "311001";

	public static final String MESSAGE_HANDLER_NOT_FOUND = "MDB10001";

	public static final String USAGE_TABLE_NOT_FOUND = "400001";

	public static final String ERROR_INCOMPATIBLE_PRICEPLAN_ACCOUNT = "400002";
	public static final String ERROR_INCOMPATIBLE_SERVICE_ACCOUNT = "400003";

	public static final String INVALID_SOC_CODE = "APP00015";
	public static final String UNEXPECTED_NO_AVAILABLE_BILL_CYCLE_ERROR = "100001";

	public static final String SUBSCRIBER_PORTED_OUT = "APPCC1120";
	public static final String SUBSCRIBER_PORT_OUT_FAILED = "APPCC1030";
	public static final String GET_SUBSCRIBER_AIRTIME_RATE_ERROR = "APP20015";

	public static final String MISSING_MANDATORY_INPUT = "VAL60001";
	public static final String MISSING_BAN_PHONENUMBER_FIELD = "VAL60002";
	public static final String INVALID_ACTIVITY_DATE = "VAL60003";
	public static final String MISSING_SOC_EFFECTIVE_DATE = "VAL60004";
	public static final String XHOUR_SOC_TYPE_INCONSISTENCY = "VAL60005";

	public static final String PRM_CHECK_PORTIN_ELIGIBILITY_ERROR = "400004";
	public static final String INTER_BRAND_CANCELED_PORT_OUT_SUBSCRIBER_ERROR = "400005";
	public static final String INTER_BRAND_PORT_ACTIVITY_REASON_CODE_ERROR = "400006";

	public static final String MIGRATE_GENERAL_ERROR = "MIGRATE01";
	public static final String MIGRATE_FAILURE_ERROR = "MIGRATE02";
	public static final String INVALID_MIGRATE_REQUEST = "MIGRATE03";
	public static final String MIGRATE_SEAT_MOVE_SUBSCRIBER_ERROR = "MIGRATE04";
	public static final String MIGRATE_SEAT_CHANGE_PRICE_PLAN_ERROR = "MIGRATE05";
	public static final String MIGRATE_SEAT_CHANGE_SEAT_GROUP_ERROR = "MIGRATE06";

	public static final String ACTIVATE_GENERAL_ERROR = "ACTIVATE01";
	public static final String UNABLE_RETRIEVE_SOURCE_SUBSCRIBER = "ACTIVATE02";
	public static final String INCOMPLETE_SUBSCRIBER_CREATION_PROCESS = "ACTIVATE03";
	public static final String MISSING_REQUIRED_SERVICE = "ACTIVATE04";
	public static final String NO_PHONE_NUMBER_RESERVED = "ACTIVATE05";
	public static final String CREATE_SUBSCRIBER_FAILED = "ACTIVATE06";
	public static final String EQUIPMENT_VALIDATE_FAILED = "ACTIVATE07";

	public static final String SUBSCRIBER_RESOURCE_NOT_FOUND = "APP20021";
	public static final String SUBSCRIBER_RESOURCE_CHANGE_ERROR = "APP20022";

	public static final String MIGRATE_MRKT_PREF_SAME_UUID_ERROR = "MIGMRKTPRF01";
	
	public static final String ADDRESS_RETRIEVAL_ERROR = "PRO20020";
	public static final String INVALID_SEAT_TYPE = "PRO20021";
	public static final String BC_SEAT_ACTIVATION_VALIDATION_ERROR = "BCACTIVATE08";
	public static final String MISSING_ADDRESS_FOR_BC_SEAT_ACTIVATION = "BCACTIVATE09";
	public static final String INVALID_SEAT_TYPE_BC_SEAT_ACTIVATION = "BCACTIVATE10";
	public static final String MISSING_SEAT_DATA = "BCACTIVATE11";
	public static final String NO_SERVICE_EDITION_CHANGE = "BCSE01";
	public static final String EXPECTED_QUEBEC_SERVICE_EDITION_ERROR = "BCSE02";
	public static final String EXPECTED_NATIONAL_SERVICE_EDITION_ERROR = "BCSE03";
	public static final String MISSING_PRICE_PLAN_SWITCH_CODE_ERROR = "BCSE04";
	public static final String VOIP_LICENSE_CHANGE_ERROR = "BCLICENSE01";
	public static final String NO_UNASSIGNED_LICENSE_ERROR = "BCLICENSE02";

	public static final String SSPN_ERROR = "SSPN01";
	public static final String SSPA_ERROR = "SSPA01";
	public static final String SSPA_ELIGIBILITY_ERROR = "SSPA02";

	public static final String INVALID_ACCOUNT_TYPE = "VAL70001";
	public static final String INVALID_PAYMENT_AMOUNT = "VAL70002";

	public static final String PAYMENT_PROCESSING_ERROR = "PAY0001";
	public static final int HCD_CREDIT_CHECK_FAILED = 400007;
	public static final int HCD_SYSTEM_ERROR_OCCURED = 400008;

	public static final String MULTIPLE_CONTRIBUTING_DATA_SHARING_GROUPS = "DS00001";
	public static final String NO_AVAILABLE_ACCESSORS = "DS00002";
	public static final String CONTRIBUTING_SOC_IS_PRICE_PLAN = "DS00003";

	public static final String REFERENCE_DATA_ERROR = "REFDATA01";

	public static final String CREDIT_DECISIONING_GENERAL_ERROR = "CDA00001";

	public static final String OCS_GENERAL_ERROR = "OCS00001";
	public static final String OCS_BAN_PPU_BREACH = "OCS00002";
	public static final String OCS_PPU_ROLLBACK_ERROR = "OCS00003";

	public static final String INVALID_CONTEXT = "VAL00000";
	public static final String INVALID_ACTIVATION_OPTION = "CDA00001";

	public static final String ESIM_ERROR_NOT_ESIM_SUPPORTED = "ESIM_NOT_SUPPORTED";
	public static final String ESIM_ERROR_EQUIPMENT_SN_IN_USE = "ESIM_DEVICE_IN_USE";
	public static final String ESIM_ERROR_DEVICE_TYPE_MISMATCH = "ESIM_DEVICE_TYPE_MISMATCH";
	public static final String ESIM_ERROR_RESERVE_SIM_PROFILE = "RESERVE_SIM_FAILURE";
	public static final String ESIM_ERROR_RETRIEVE_EQUIPMENT_FAILURE = "RETRIEVE_EQUIPMENT_FAILURE";
	
	/* Reference EJB */
	public static final String REF_EJB_INVALID_PARAMETERS = "VAL30001";
	
	/* HTTP Errors */
	public static final String CMB_HTTP_ERROR_UNAUTHORIZED = "HTTP_401";
}