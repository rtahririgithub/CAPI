/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws;

/**
 * @author Pavel Simonovsky
 *
 */
public interface ServiceErrorCodes {

	public static final String ERROR_UNKNOWN = "JWS0001";
	public static final String ERROR_SCHEMA_VALIDATION = "JWS0002";
	public static final String ERROR_UNSUPPORTED_OPERATION = "JWS0003";
	public static final String ERROR_MAX_LIMIT_EXCEEDED ="JWS0004";
	public static final String ERROR_MISSING_CREDIT_CARD_TOKEN = "JWS1001";
	public static final String ERROR_INVALID_CREDIT_CARD_TRANSACTION_TYPE = "JWS1002";
	public static final String ERROR_MISSING_AVALON_USER_HEADER = "JWS1003";
	public static final String ERROR_SOA_DELEGATE = "JWS1004";
	public static final String ERROR_SOA_DELEGATE_GETINSTANCE = "JWS1005";
	public static final String ERROR_INVALID_BAN = "JWS1006";
	public static final String ERROR_GETIING_LDAP = "JWS1007";
	public static final String ERROR_INCOMPATIBLE_PRICEPLAN_ACCOUNT="JWS1008";
	public static final String ERROR_INCOMPATIBLE_SERVICE_ACCOUNT="JWS1009"; 
	public static final String ERROR_INVALID_CT_REASON_CODE="JWS1010";
	public static final String ERROR_MISSING_BILL_SEQUENCE_NUM="JWS1011";
	public static final String ERROR_MISSING_ALL_RECURRING_INDICATOR="JWS1012";
	public static final String ERROR_INVALID_TAX_OPTION_CODE="JWS1013";
	public static final String ERROR_INVALID_BILL_STATE_CODE="JWS1014";
	public static final String ERROR_CODE_PAYMENT_METHOD_MISSING="JWS1015";
	public static final String ERROR_CODE_PAYMENT_METHOD_DUPLICATED="JWS1016";
	public static final String ERROR_MISSING_MANDATORY_PORTIN_ELIGIBILITY_PARAMETERS = "JWS1017";
	public static final String ERROR_ACCOUNT_FOR_PHONE_NOT_FOUND = "JWS1023";
	public static final String ERROR_NO_DATA_FOR_PHONE_NUMBER = "JWS1024";
	public static final String ERROR_NO_DATA_FOR_BAN_PHONE_NUMBER = "JWS1025";
	public static final String ERROR_THROWABLE = "JWS1026";
	public static final String ERROR_NO_DATA_FOR_BAN = "JWS1027";
	public static final String ERROR_OPERATION_NO_LONGER_IN_SERVICE = "JWS1028";
	
	/* 
	 *   Please add an ERROR_XXXX definition above for error code, 
	 *  in addition to ERROR_DESC_XXXX definition below for error message
	 */
	public static final String ERROR_DESC_THROWABLE = "Throwable Error occurred";
	public static final String ERROR_DESC_TUXEDO_SERVICE = "Tuxedo service error";
	public static final String ERROR_DESC_APILINK_VALIDATION = "Amdocs APILink Validation Error";
	public static final String ERROR_DESC_ECA = "ClientAPI EJB Error";
	public static final String ERROR_DESC_DATABASE = "Database Error occured";
	public static final String ERROR_DESC_INPUT = "Invalid input parameter";
	public static final String ERROR_DESC_CC_TX_FAILED = "Credit Card Transaction unsuccessful";
	public static final String ERROR_DESC_CC_TOKEN_MISSING = "Credit Card token is missing.";
	public static final String ERROR_DESC_AVALON_USER_HEADER_MISSING ="The Avalon user header is missing.";
	public static final String ERROR_DESC_SOAP_FAULT = "Sub-system soap fault error has occured";
	public static final String ERROR_DESC_SOA_DELEGATE = "Unable to get SOA Delegate";
	public static final String ERROR_DESC_NO_DATA_FOR_BAN = "There is no data for the BAN provided.";
	public static final String ERROR_DESC_NO_DATA_FOR_BAN_SUBSCRIBER = "There is no data for the BAN and Subscriber provided.";
	public static final String ERROR_DESC_GETIING_LDAP = "Unable to get LDAP";
	public static final String ERROR_DESC_INCOMPATIBLE_PRICEPLAN_ACCOUNT ="PricePlan to be added to subscriber on the account is not compatible with the account";
	public static final String ERROR_DESC_INCOMPATIBLE_SERVICE_ACCOUNT ="Service to be added to subscriber on the account is not compatible with the account";
	public static final String ERROR_DESC_INVALID_INPUT ="Input not in KB Special Numbers table.";
	public static final String ERROR_DESC_INVALID_CT_REASON_CODE= "Invalid CreditTransfer ReasonCode";
	public static final String ERROR_DESC_BILL_SEQUENCE_NUM_MISSING= "BillSequenceNo is Mandatory if billedIndicator is set to true";
	public static final String ERROR_DESC_ALL_RECURRING_INDICATOR_MISSING= "reverseAllRecurringIndicator is Mandatory if recurringIndicator is set to true";
	public static final String ERROR_DESC_INVALID_TAX_OPTION_CODE= "Invalid TaxOption Code ";
	public static final String ERROR_DESC_INVALID_BILL_STATE_CODE= "Invalid BillState Code ";
	public static final String ERROR_DESC_PAYMENT_METHOD_MISSING = "Please select at least one payment method";
	public static final String ERROR_DESC_PAYMENT_METHOD_DUPLICATED = "Only one payment method to be selected";
	public static final String ERROR_DESC_MISSING_MANDATORY_PORTIN_ELIGIBILITY_PARAMETERS = "Missing mandatory port-in eligibility parameters: platformId, incomingBrandId and outgoingBrandId";	
	public static final String ERROR_DESC_ACCOUNT_FOR_PHONE_NOT_FOUND = "Account not found for Phone Number provided";
	public static final String ERROR_DESC_NO_DATA_FOR_PHONE_NUMBER = "No data for supplied Phone Number";
	public static final String ERROR_DESC_NO_DATA_FOR_BAN_PHONE_NUMBER = "Subscriber not found for PhoneNumber and Account";
	public static final String ERROR__DESC_OPERATION_NO_LONGER_IN_SERVICE = "Operation is no longer in service";
}
