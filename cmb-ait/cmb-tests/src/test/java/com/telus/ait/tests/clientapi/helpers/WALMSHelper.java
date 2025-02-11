package com.telus.ait.tests.clientapi.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.telus.ait.tests.clientapi.constants.DefaultValues;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType.AppInfo;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wirelessaccountlifecyclemgmtservicerequestresponse_v1.BaseWirelessAccount;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wirelessaccountlifecyclemgmtservicerequestresponse_v1.BusinessCreditIdentityList;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wirelessaccountlifecyclemgmtservicerequestresponse_v1.CreatePostpaidAccount;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wirelessaccountlifecyclemgmtservicerequestresponse_v1.CreatePrepaidAccount;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wirelessaccountlifecyclemgmtservicerequestresponse_v1.PostpaidAccountRequest;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wirelessaccountlifecyclemgmtservicerequestresponse_v1.PrepaidAccountRequest;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.Address;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.AddressType;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.BusinessCreditInformation;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.BusinessRole;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.ConsumerName;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.Language;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.NameFormat;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.PersonalCreditInformation;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.ProvinceCode;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v4.AuditInfo;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resource_order_reference_types_1_0.AccountTypeCode;

/**
 * Created by wcheong on 10/12/2015.
 */

public class WALMSHelper extends CommonHelper {
		
	public static final String ACCOUNT_TYPE_BUSINESS = "B";
	public static final String ACCOUNT_TYPE_CORPORATE = "C";
	public static final String ACCOUNT_TYPE_CONSUMER = "I";
	public static final List<String> ACCOUNT_SUBTYPE_BUSINESS_REGULAR = Arrays.asList(new String[] {"R", "1", "M", "4", "B", "O", "W", "A", "X", "F"});
	public static final List<String> ACCOUNT_SUBTYPE_CORPORATE_PERSONAL = Arrays.asList(new String[] {"I", "E"});
	public static final List<String> ACCOUNT_SUBTYPE_PREPAID = Arrays.asList(new String[] {"Q", "Y", "B"});
	
	public static CreatePostpaidAccount createDefaultPostpaidAccount(String accountType, String accountSubType, String province) throws ParseException {
		ConsumerName consumerName = createConsumerName(WILDCARD, WILDCARD, WILDCARD, WILDCARD);
		Address address = createAddress(WILDCARD, WILDCARD, province, WILDCARD, WILDCARD, WILDCARD);
		PersonalCreditInformation personalCreditInfo = createPersonalCreditInfo(WILDCARD, WILDCARD, WILDCARD);
		BaseWirelessAccount baseAccount = createBaseWirelessAccount(accountType, accountSubType, WILDCARD, province, WILDCARD, WILDCARD, WILDCARD, WILDCARD, WILDCARD, WILDCARD, WILDCARD, 
				consumerName, address, personalCreditInfo);		
		return createPostpaidAccountParameters(baseAccount, createAuditInfo(WILDCARD, WILDCARD, WILDCARD, WILDCARD, WILDCARD));
	}
	
	public static CreatePrepaidAccount createDefaultPrepaidAccount(String accountType, String accountSubType, String province, String serialNumber) throws ParseException {
		ConsumerName consumerName = createConsumerName(WILDCARD, WILDCARD, WILDCARD, WILDCARD);
		Address address = createAddress(WILDCARD, WILDCARD, province, WILDCARD, WILDCARD, WILDCARD);
		PersonalCreditInformation personalCreditInfo = createPersonalCreditInfo(WILDCARD, WILDCARD, WILDCARD);
		BaseWirelessAccount baseAccount = createBaseWirelessAccount(accountType, accountSubType, WILDCARD, province, WILDCARD, WILDCARD, WILDCARD, WILDCARD, WILDCARD, WILDCARD, WILDCARD, 
				consumerName, address, personalCreditInfo);
		return createPrepaidAccountParameters(baseAccount, createAuditInfo(WILDCARD, WILDCARD, WILDCARD, WILDCARD, WILDCARD), serialNumber, WILDCARD, WILDCARD);
	}
	
	public static OriginatingUserType createDefaultAccountHeader() {
		return createAccountHeader(WILDCARD, WILDCARD, WILDCARD, WILDCARD, WILDCARD);
	}
	
	public static CreatePostpaidAccount createPostpaidAccountParameters(BaseWirelessAccount baseAccount, AuditInfo auditInfo) {
		CreatePostpaidAccount parameters = new CreatePostpaidAccount();
		PostpaidAccountRequest baseAccountRequest = new PostpaidAccountRequest();
    	baseAccountRequest.setBusinessCreditIdentityList(new BusinessCreditIdentityList());
    	baseAccountRequest.setBaseAccount(baseAccount);	 
    	baseAccountRequest.setAuditInfo(auditInfo);
    	parameters.setBaseAccountRequest(baseAccountRequest);
    	return parameters;
	}
	
	public static CreatePrepaidAccount createPrepaidAccountParameters(BaseWirelessAccount baseAccount, AuditInfo auditInfo, String serialNumber, String activationType, String businessRole) {
		CreatePrepaidAccount parameters = new CreatePrepaidAccount();
		PrepaidAccountRequest request = new PrepaidAccountRequest();
		request.setBaseAccount(baseAccount);
		request.setAuditInfo(auditInfo);
		request.setSerialNumber(serialNumber);
		request.setActivationType(getDefaultValue(activationType, DefaultValues.ACTIVATION_TYPE));
		request.setBusinessRole(BusinessRole.valueOf(getDefaultValue(businessRole, DefaultValues.BUSINESS_ROLE)));
		request.setActivationCode(DefaultValues.ACTIVATION_CODE);
		parameters.setPrepaidAccounttRequest(request);
		return parameters;
	}
	
	public static OriginatingUserType createAccountHeader(String custId, String ipAddress, String appId, String userId, String appIpAddress) {
        OriginatingUserType header = new OriginatingUserType();
		header.setCustId(getDefaultValue(custId, DefaultValues.CUST_ID));
		header.setIpAddress(getDefaultValue(ipAddress, DefaultValues.IP_ADDRESS));
    	header.setAppInfo(createAppInfoList(appId, userId, appIpAddress));
    	return header;
	}
	
	public static PersonalCreditInformation createPersonalCreditInfo(String sin, String birthDateFormat, String birthDate) throws ParseException {
		PersonalCreditInformation personal = new PersonalCreditInformation();
    	personal.setSin(getDefaultValue(sin, DefaultValues.SIN));
    	personal.setBirthDate((new SimpleDateFormat(getDefaultValue(birthDateFormat, DefaultValues.BIRTHDATE_FORMAT))).parse(getDefaultValue(birthDate, DefaultValues.BIRTHDATE)));
		return personal;
	}
	
	public static BaseWirelessAccount createBaseWirelessAccount(String accountType, String accountSubType, String brandId, String homeProvince, String pin, String email, String language, 
			String dealerCode, String salesRepCode, String contactPhone, String homePhone, ConsumerName consumerName, Address address, PersonalCreditInformation personalCreditInfo) {    	
		BaseWirelessAccount baseAccount = new BaseWirelessAccount();
    	baseAccount.setAccountType(AccountTypeCode.fromValue(getDefaultValue(accountType, DefaultValues.ACCOUNT_TYPE)));
    	baseAccount.setAccountSubType(getDefaultValue(accountSubType, DefaultValues.ACCOUNT_SUBTYPE));
    	baseAccount.setBrandId(Integer.valueOf(getDefaultValue(brandId, DefaultValues.BRAND_ID_TELUS)).intValue());
    	baseAccount.setHomeProvince(ProvinceCode.fromValue(getDefaultValue(homeProvince, DefaultValues.HOME_PROVINCE)));
    	baseAccount.setPin(getDefaultValue(pin, DefaultValues.PIN));
    	baseAccount.setEmail(getDefaultValue(email, DefaultValues.EMAIL));
    	baseAccount.setLanguage(Language.fromValue(getDefaultValue(language, DefaultValues.LANGUAGE)));
    	baseAccount.setDealerCode(getDefaultValue(dealerCode, DefaultValues.DEALER_CODE));
    	baseAccount.setSalesRepCode(getDefaultValue(salesRepCode, DefaultValues.SALES_REP_CODE));
    	baseAccount.setContactPhone(getDefaultValue(contactPhone, DefaultValues.CONTACT_PHONE));
    	baseAccount.setHomePhone(getDefaultValue(homePhone, DefaultValues.HOME_PHONE));
    	baseAccount.setContactName(consumerName);
    	baseAccount.setAddress(address);
    	baseAccount.setPersonalCreditInformation(personalCreditInfo);
    	if (isCorporateOrBusinessRegular(accountType, accountSubType)) {
    		baseAccount.setLegalBusinessName(DefaultValues.LEGAL_BUSINESS_NAME);
    		BusinessCreditInformation businessCreditInfo = new BusinessCreditInformation();
    		businessCreditInfo.setIncorporationNumber(DefaultValues.INCORP_NUM);
    		businessCreditInfo.setIncorporationDate(DefaultValues.INCORP_DATE);
    		baseAccount.setBusinessCreditInformation(businessCreditInfo);
    	} else {
    		baseAccount.setName(consumerName);
    	}    	
		return baseAccount;
	}
	
	public static ArrayList<AppInfo> createAppInfoList(String appId, String userId, String ipAddress) {
    	ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
		AppInfo appInfo = new AppInfo();
    	appInfo.setApplicationId(Long.valueOf(getDefaultValue(appId, DefaultValues.APP_ID)));
    	appInfo.setUserId(getDefaultValue(userId, DefaultValues.USER_ID));
    	appInfo.setIpAddress(getDefaultValue(ipAddress, DefaultValues.APP_IP_ADDRESS));
    	appList.add(appInfo);
		return appList;
	}
	
	public static Address createAddress(String addressType, String city, String provinceCode, String postalCode, String country, String streetName) {
		Address address = new Address();
    	address.setAddressType(AddressType.fromValue(getDefaultValue(addressType, DefaultValues.ADDRESS_TYPE)));
    	address.setCity(getDefaultValue(city, DefaultValues.CITY));
    	address.setProvince(ProvinceCode.fromValue(getDefaultValue(provinceCode, DefaultValues.PROVINCE_CODE)));
    	address.setPostalCode(getDefaultValue(postalCode, DefaultValues.POSTAL_CODE));
    	address.setCountry(getDefaultValue(country, DefaultValues.COUNTRY));
    	address.setStreetName(getDefaultValue(streetName, DefaultValues.STREET_NAME));
		return address;
	}
	
	public static AuditInfo createAuditInfo(String userId, String salesRepId, String channelOrgId, String outletId, String originatorAppId) {
		AuditInfo auditInfo = new AuditInfo();
    	auditInfo.setUserId(getDefaultValue(userId, DefaultValues.AUDIT_USER_ID));
    	auditInfo.setSalesRepresentativeId(getDefaultValue(salesRepId, DefaultValues.AUDIT_SALES_REP_ID));
    	auditInfo.setChannelOrganizationId(getDefaultValue(channelOrgId, DefaultValues.CHANNEL_ORG_ID));
    	auditInfo.setOutletId(getDefaultValue(outletId, DefaultValues.OUTLET_ID));
    	auditInfo.setOriginatorApplicationId(getDefaultValue(originatorAppId, DefaultValues.ORG_APP_ID));
		return auditInfo;
	}
	
	public static ConsumerName createConsumerName(String title, String firstName, String lastName, String nameFormat) {
    	ConsumerName consumerName = new ConsumerName();
    	consumerName.setTitle(getDefaultValue(title, DefaultValues.TITLE));
    	consumerName.setFirstName(getDefaultValue(firstName, DefaultValues.FIRST_NAME));
    	consumerName.setLastName(getDefaultValue(lastName, DefaultValues.LAST_NAME));
    	consumerName.setNameFormat(NameFormat.fromValue(getDefaultValue(nameFormat, DefaultValues.NAME_FORMAT)));
    	return consumerName;	    	
    }
	
	public static boolean isCorporateOrBusinessRegular(String accountType, String accountSubType) {
		if (accountType.equals(ACCOUNT_TYPE_BUSINESS)) {
			return ACCOUNT_SUBTYPE_BUSINESS_REGULAR.contains(accountSubType);
		} else if (accountType.equals(ACCOUNT_TYPE_CORPORATE)) {
			return !ACCOUNT_SUBTYPE_CORPORATE_PERSONAL.contains(accountSubType);
		}
		return false;
	}
	
	public static boolean isPrepaid(String accountType, String accountSubtype) {
		return accountType.equals(ACCOUNT_TYPE_CONSUMER) && ACCOUNT_SUBTYPE_PREPAID.contains(accountSubtype);
	}
}