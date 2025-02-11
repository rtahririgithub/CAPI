package com.telus.cmb.common.prepaid;

import java.math.BigInteger;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import com.telus.api.ClientAPI;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.common_domain_types_3.CardNumberDisplay;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.common_domain_types_3.CardType;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.common_domain_types_3.PaymentInstrument;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.common_domain_types_3.PaymentInstrumentType;
import com.telus.tmi.xmlschema.xsd.customer.customer.subscribertypes_v4.RealTimeBalance;
public class PrepaidUtils {

	public final static String RECHARGE_TYPE_INTERVAL = "interval";
	public final static String RECHARGE_TYPE_THRESHOLD = "threshold";
	public final static String INSTRUMENT_TYPE_CREDITCARD = "CC";
	public final static String INSTRUMENT_TYPE_DEBITCARD = "DC";
	public final static String BALANCE_TYPE_PRIMARY = "primary";
	public final static String BALANCE_TYPE_RESERVED = "reserved";
	public final static String BALANCE_TYPE_INTRUST = "inTrust";
	public final static String BALANCE_TYPE_OSCHARGE = "oscharge";
	public final static String RATE_PROFILE_TYPE_LOCAL = "local";
	public final static String RATE_PROFILE_TYPE_LONG_DISTANCE = "ld";
	public final static String RATE_PROFILE_TYPE_US_LONG_DISTANCE = "ld-us";
	public final static String BILLING_TYPE_MINUTE = "minute";
	public final static String RATE_UNIT_CENT = "cent";
	public final static String INVOCATION_TYPE_SOFT_VALIDATION = "0";
	public final static String INVOCATION_TYPE_HARD_VALIDATION = "1";
	public final static String SOURCE_WEB = "0";
	public final static String SOURCE_OTHER = "4";
	public final static String PRIMARY_TYPE = "primary";
	public final static String UNITS_CAD = "CAD";
	public final static String CREDIT_TYPE_ACR = "ACR";//ACTIVATION CREDIT
	public final static String CREDIT_TYPE_P2P = "P2P";//P2P
	public final static String RECHARGEPAYMENT_TYPE_CREDITCARD ="CREDITCARD";
	public final static String RECHARGEPAYMENT_TYPE_DEBITCARD ="DEBITCARD";
	public static  String IP_ADDRESS="127.0.0.1";
	
	public static String getSourceFromAppId(String applicationId) {
		String source = null;
		if (applicationId.equalsIgnoreCase("sserve") || applicationId.equalsIgnoreCase("GEMINI")) {
			source = PrepaidUtils.SOURCE_WEB;//self service
		}  else {
			source = PrepaidUtils.SOURCE_OTHER;//full service
		}
		return source;
	}

	public static OriginatingUserType createOriginatingUserTypeObj() {
		return createOriginatingUserTypeObj(null, null);
	}
	
	public static OriginatingUserType createOriginatingUserTypeObj(String custId, String ipAddress) {
		OriginatingUserType result = new OriginatingUserType();
		result.setCustId(StringUtils.isNotEmpty(custId)? custId:null);
		result.setIpAddress(StringUtils.isNotEmpty(ipAddress)? ipAddress:null);
		return result;
	}
	
	public static double getRealTimeBalanceAmount(RealTimeBalance balance) {
		double result = 0;
		if (balance != null && balance.getBalanceAmount() != null) {
			result = balance.getBalanceAmount().getAmount();
		}
		return result;
	}
	
	public static double getRealTimeBalanceAmountForSmsService(RealTimeBalance balance) {
		double result = 0;
		if (balance != null && balance.getBalanceAmount() != null) {
			result = balance.getBalanceAmount().getAmount();
		}
		return result;
	}
	public static OriginatingUserType createOriginatingUserType() {
		return createOriginatingUserType(null, null);
	}
	
	public static OriginatingUserType createOriginatingUserType(String custId, String ipAddress) {
		OriginatingUserType result = new OriginatingUserType();
		result.setCustId(StringUtils.isNotEmpty(custId)? custId:null);
		result.setIpAddress(StringUtils.isNotEmpty(ipAddress)? ipAddress:null);
		return result;
	}
	
	public static PaymentInstrument createPaymentInstrumentObj(PaymentInstrumentType paymentInstrumentType,
			CardType cardType,
			String cardNumber,
			CardNumberDisplay cardNumberDisplay,
			String expiryMonth,
			String expiryYear) {
		PaymentInstrument result = new PaymentInstrument();
		if (paymentInstrumentType != null)
			result.setPaymentInstrumentType(paymentInstrumentType);
		if (cardType != null)
			result.setCardType(cardType);
		if (StringUtils.isNotBlank(cardNumber))
			result.setCardNumber(cardNumber);
		if (cardNumberDisplay != null)
			result.setCardNumberDisplay(cardNumberDisplay);
		if (StringUtils.isNotBlank(expiryMonth))
			result.setExpiryMonth(expiryMonth);
		if (StringUtils.isNotBlank(expiryYear))
			result.setExpiryYear(expiryYear);
		return result;
	}
	
	public static CardNumberDisplay createCardNumberDisplayObj(String first6Digits, String last4Digits, String marskedCardNumber) {
		CardNumberDisplay result = new CardNumberDisplay();
		if (StringUtils.isNotBlank(first6Digits))
			result.setFirst6Digits(first6Digits);
		if (StringUtils.isNotBlank(last4Digits))
			result.setLast4Digits(last4Digits);
		if (StringUtils.isNotBlank(marskedCardNumber))
			result.setMaskedCardNumber(marskedCardNumber);
		return result;
	}
	
	public static String encodeBase64String(String text) {
		byte[] result = Base64.encodeBase64(text.trim().getBytes());
		return new String(result);
	}
	
	public static String decodeBase64String(String encodedText) {
		byte[] result = Base64.decodeBase64(encodedText);
		return new String(result);
	}
	public static OriginatingUserType createOriginatingUserTypeWithAppInfoPopulated(String UserId){
		
		OriginatingUserType originatingUserType=new OriginatingUserType();
		OriginatingUserType.AppInfo appInfo=new OriginatingUserType.AppInfo();
		appInfo.setIpAddress(IP_ADDRESS);
		appInfo.setUserId(UserId);
		appInfo.setApplicationId(BigInteger.valueOf(ClientAPI.CMDB_ID));
		originatingUserType.getAppInfo().add(appInfo);
		
		return originatingUserType;
	}
	
}
