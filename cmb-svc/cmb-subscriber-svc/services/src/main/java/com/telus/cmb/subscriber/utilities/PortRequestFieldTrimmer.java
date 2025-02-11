package com.telus.cmb.subscriber.utilities;

import com.telus.cmb.wsclient.wlnp.crtpirs.PortInRequestData;
import com.telus.cmb.wsclient.wlnp.crtpirs.PortInRequestDataBodyType;
import com.telus.cmb.wsclient.wlnp.crtpirs.TypesAccountInfoType;
import com.telus.cmb.wsclient.wlnp.crtpirs.TypesBANInfoType;
import com.telus.cmb.wsclient.wlnp.crtpirs.TypesBillingAddressType;
import com.telus.cmb.wsclient.wlnp.crtpirs.TypesSubscriberInfoType;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.portrequestinformationservicerequestresponse_v1.PreportValidationRequestDataBodyType;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.portrequestinformationservicerequestresponse_v1.PreportValidationRequestDataType;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resourceorderservicecommontypes_v2.AccountInfoType;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resourceorderservicecommontypes_v2.BillingAddressType;

public class PortRequestFieldTrimmer {

	private final static int POLICY_NO_CHANGE = 0;
	private final static int POLICY_TRIM_WHITE_SPACE = 1;
	private final static int POLICY_TRUNCATE = 2;

	public static final int UNDEFINED_LEN = 0;

	public static final int BILL_PREFIX_LEN = 10;
	public static final int BILL_FIRST_NAME_LEN = 25;
	public static final int BILL_MIDDLE_INIT_LEN = 1;
	public static final int BILL_LAST_NAME_LEN = 25;
	public static final int BILL_SUFFIX_LEN = 10;
	public static final int BILL_BUS_NAME_LEN = 60;
	public static final int BILL_ST_NUN_LEN = 10;
	public static final int BILL_ST_NAME_LEN = 60;
	public static final int BILL_ST_DIR_LEN = 2;
	public static final int BILL_CITY_LEN = 35;
	public static final int BILL_PROVINCE_LEN = 2;
	public static final int BILL_POSTAL_LEN = 10;
	public static final int BILL_COUNTY_LEN = 3;

	public static final int BAN_BUS_NAME_LEN = 60;
	public static final int BAN_FIRST_NAME_LEN = 25;
	public static final int BAN_LAST_NAME_LEN = 25;
	public static final int BAN_POSTAL_CODE_LEN = 10;
	public static final int BAN_PROVINCE_LEN = 2;

	public static final int SUB_POSTAL_CODE = 10;
	public static final int SUB_PROVINCE = 2;

	public static final int AGENCY_NAME_LEN = 60;

	public static PortRequestFieldTrimmer newInstance(boolean isMigration) {
		if (isMigration) {
			return newTruncator();
		} else {
			return newTrimmer();
		}
	}

	public static PortRequestFieldTrimmer newTrimmer() {
		return new PortRequestFieldTrimmer(POLICY_TRIM_WHITE_SPACE);
	}

	public static PortRequestFieldTrimmer newTruncator() {
		return new PortRequestFieldTrimmer(POLICY_TRUNCATE);
	}

	public static String trucate(String originalString, int length) {
		String result = originalString;
		if (result != null) {
			result = result.trim();
			if (length != UNDEFINED_LEN && result.length() > length) {
				result = result.substring(0, length);
			}
		}
		return result;
	}

	private int enforcePolicy = POLICY_NO_CHANGE;

	public PortRequestFieldTrimmer(int enforcePolicy) {
		this.enforcePolicy = enforcePolicy;
	}

	public PortInRequestData trim(PortInRequestData requestData) {
		PortInRequestDataBodyType body = requestData.getPortInRequestBody();

		trimBillingAddress(body.getBillingAddress());
		trimBanInfo(body.getTelusBAN());
		trimSubInfo(body.getTelusSubscriber());

		trimAccInfo(body.getAcctInfo()); // WNP core

		if (body.getImplContactTelNO() != null) {
			body.setImplContactTelNO(trim(body.getImplContactTelNO(), UNDEFINED_LEN)); // WNP
																						// core
		}
		if (body.getImplContactName() != null) {
			body.setImplContactName(trim(body.getImplContactName(), UNDEFINED_LEN)); // WNP
																						// core
		}
		if (body.getAgencyAuthName() != null) { // WNP core and M2P
			// This field's value is concatenated from two fields ( account
			// first name/last name ), front-end application does not have
			// any chance to valdate it, so we have to enforce the length
			body.setAgencyAuthName(trucate(body.getAgencyAuthName(), AGENCY_NAME_LEN));
		}
		if (body.getInitiatorRepresentative() != null) {
			body.setInitiatorRepresentative(trim(body.getInitiatorRepresentative(), UNDEFINED_LEN)); // WNP
																										// core
		}
		if (body.getRemarks() != null) {
			body.setRemarks(trim(body.getRemarks(), UNDEFINED_LEN)); // WNP
		}

		return requestData;
	}


	// =========================
	// trim PreportRequestValidationService.PreportValidationRequestDataAnonType
	// =========================
	public PreportValidationRequestDataType trim(PreportValidationRequestDataType requestData) {
		
		PreportValidationRequestDataBodyType body = requestData.getPortDataBody();

		trimBillingAddress(body.getBillingAddress());

		trimAccInfo(body.getAccountInfo()); // WNP core

		if (body.getImplementContactTelephoneNumber() != null) {
			body.setImplementContactTelephoneNumber(trim(body.getImplementContactTelephoneNumber(), UNDEFINED_LEN)); // WNP
																						// core
		}
		if (body.getImplementContractName() != null) {
			body.setImplementContractName(trim(body.getImplementContractName(), UNDEFINED_LEN)); // WNP
																						// core
		}
		if (body.getAgentAuthorizedName() != null) { // WNP core
			// This field's value is concatenated from two fields ( account
			// first name/last name ), front-end application does not have
			// any chance to valdate it, so we have to enforce the length
			body.setAgentAuthorizedName(trucate(body.getAgentAuthorizedName(), AGENCY_NAME_LEN));
		}
		if (body.getInitiatorRepresentativeId() != null) {
			body.setInitiatorRepresentativeId(trim(body.getInitiatorRepresentativeId(), UNDEFINED_LEN)); // WNP
																										// core
		}
		if (body.getRemarksTxt() != null) {
			body.setRemarksTxt(trim(body.getRemarksTxt(), UNDEFINED_LEN)); // WNP
		}

		return requestData;
	}

	public String trim(String originalString, int length) {
		String result = originalString;

		if (result != null && enforcePolicy > POLICY_NO_CHANGE) {
			result = originalString.trim();
			if (enforcePolicy > POLICY_TRIM_WHITE_SPACE && length != UNDEFINED_LEN && result.length() > length) {
				result = result.substring(0, length);
			}
		}
		return result;
	}

	public void trimAccInfo(TypesAccountInfoType accInfo) {
		if (accInfo == null) {
			return;
		}
		if (accInfo.getESN() != null) {
			accInfo.setESN(trim(accInfo.getESN(), UNDEFINED_LEN));
		}
		if (accInfo.getAcctNumber() != null) {
			accInfo.setAcctNumber(trim(accInfo.getAcctNumber(), UNDEFINED_LEN));
		}
		if (accInfo.getPIN() != null) {
			accInfo.setPIN(trim(accInfo.getPIN(), UNDEFINED_LEN));
		}
	}

	public void trimAccInfo(AccountInfoType accInfo) {
		if (accInfo == null) {
			return;
		}
		if (accInfo.getEsn() != null) {
			accInfo.setEsn(trim(accInfo.getEsn(), UNDEFINED_LEN));
		}
		if (accInfo.getAccountNumber() != null) {
			accInfo.setAccountNumber(trim(accInfo.getAccountNumber(), UNDEFINED_LEN));
		}
		if (accInfo.getPin() != null) {
			accInfo.setPin(trim(accInfo.getPin(), UNDEFINED_LEN));
		}
	}

	public void trimBanInfo(TypesBANInfoType banInfo) {
		if (banInfo == null) {
			return;
		}
		if (banInfo.getBusinessName() != null) {
			banInfo.setBusinessName(trim(banInfo.getBusinessName(), BAN_BUS_NAME_LEN));
		}
		if (banInfo.getFirstName() != null) {
			banInfo.setFirstName(trim(banInfo.getFirstName(), BAN_FIRST_NAME_LEN));
		}
		if (banInfo.getLastName() != null) {
			banInfo.setLastName(trim(banInfo.getLastName(), BAN_LAST_NAME_LEN));
		}
		if (banInfo.getPostalCode() != null) {
			banInfo.setPostalCode(trim(banInfo.getPostalCode(), BAN_POSTAL_CODE_LEN));
		}
		if (banInfo.getProvince() != null) {
			banInfo.setProvince(trim(banInfo.getProvince(), BAN_PROVINCE_LEN));
		}
	}

	public void trimBillingAddress(TypesBillingAddressType billAddress) {
		if (billAddress == null) {
			return;
		}
		if (billAddress.getBillPrefix() != null) {
			billAddress.setBillPrefix(trim(billAddress.getBillPrefix(), BILL_PREFIX_LEN));
		}
		if (billAddress.getBillFirstName() != null) {
			billAddress.setBillFirstName(trim(billAddress.getBillFirstName(), BILL_FIRST_NAME_LEN));
		}
		if (billAddress.getBillMiddleInit() != null) {
			billAddress.setBillMiddleInit(trim(billAddress.getBillMiddleInit(), BILL_MIDDLE_INIT_LEN));
		}
		if (billAddress.getBillLastName() != null) {
			billAddress.setBillLastName(trim(billAddress.getBillLastName(), BILL_LAST_NAME_LEN));
		}
		if (billAddress.getBillSuffix() != null) {
			billAddress.setBillSuffix(trim(billAddress.getBillSuffix(), BILL_SUFFIX_LEN));
		}
		if (billAddress.getBusName() != null) {
			billAddress.setBusName(trim(billAddress.getBusName(), BILL_BUS_NAME_LEN));
		}
		if (billAddress.getBillStNum() != null) {
			billAddress.setBillStNum(trim(billAddress.getBillStNum(), BILL_ST_NUN_LEN));
		}
		if (billAddress.getBillStName() != null) {
			billAddress.setBillStName(trim(billAddress.getBillStName(), BILL_ST_NAME_LEN));
		}
		if (billAddress.getBillStDir() != null) {
			billAddress.setBillStDir(trim(billAddress.getBillStDir(), BILL_ST_DIR_LEN));
		}
		if (billAddress.getCity() != null) {
			billAddress.setCity(trim(billAddress.getCity(), BILL_CITY_LEN));
		}
		if (billAddress.getProvince() != null) {
			billAddress.setProvince(trim(billAddress.getProvince(), BILL_PROVINCE_LEN));
		}
		if (billAddress.getPostalCode() != null) {
			billAddress.setPostalCode(trim(billAddress.getPostalCode(), BILL_POSTAL_LEN));
		}
		if (billAddress.getCountry() != null) {
			billAddress.setCountry(trim(billAddress.getCountry(), BILL_COUNTY_LEN));
		}
	}

	public void trimBillingAddress(BillingAddressType billAddress) {
		if (billAddress == null) {
			return;
		}
		if (billAddress.getPrefixTxt() != null) {
			billAddress.setPrefixTxt(trim(billAddress.getPrefixTxt(), BILL_PREFIX_LEN));
		}
		if (billAddress.getFirstName() != null) {
			billAddress.setFirstName(trim(billAddress.getFirstName(), BILL_FIRST_NAME_LEN));
		}
		if (billAddress.getMiddleInitialTxt() != null) {
			billAddress.setMiddleInitialTxt(trim(billAddress.getMiddleInitialTxt(), BILL_MIDDLE_INIT_LEN));
		}
		if (billAddress.getLastName() != null) {
			billAddress.setLastName(trim(billAddress.getLastName(), BILL_LAST_NAME_LEN));
		}
		if (billAddress.getSuffixTxt() != null) {
			billAddress.setSuffixTxt(trim(billAddress.getSuffixTxt(), BILL_SUFFIX_LEN));
		}
		if (billAddress.getBusinessName() != null) {
			billAddress.setBusinessName(trim(billAddress.getBusinessName(), BILL_BUS_NAME_LEN));
		}
		if (billAddress.getStreetNum() != null) {
			billAddress.setStreetNum(trim(billAddress.getStreetNum(), BILL_ST_NUN_LEN));
		}

		if (billAddress.getStreetDirectionTxt() != null) {
			billAddress.setStreetDirectionTxt(trim(billAddress.getStreetDirectionTxt(), BILL_ST_DIR_LEN));
		}
		if (billAddress.getCityName() != null) {
			billAddress.setCityName(trim(billAddress.getCityName(), BILL_CITY_LEN));
		}
		if (billAddress.getProvinceCd() != null) {
			billAddress.setProvinceCd(trim(billAddress.getProvinceCd(), BILL_PROVINCE_LEN));
		}
		if (billAddress.getPostalCd() != null) {
			billAddress.setPostalCd(trim(billAddress.getPostalCd(), BILL_POSTAL_LEN));
		}
		if (billAddress.getCountryName() != null) {
			billAddress.setCountryName(trim(billAddress.getCountryName(), BILL_COUNTY_LEN));
		}
	}

	public void trimSubInfo(TypesSubscriberInfoType subInfo) {
		if (subInfo == null) {
			return;
		}
		if (subInfo.getLanguagePref() != null) {
			subInfo.setLanguagePref(trim(subInfo.getLanguagePref(), UNDEFINED_LEN));
		}
		if (subInfo.getPostalCode() != null) {
			subInfo.setPostalCode(trim(subInfo.getPostalCode(), SUB_POSTAL_CODE));
		}
		if (subInfo.getProvince() != null) {
			subInfo.setProvince(trim(subInfo.getProvince(), SUB_PROVINCE));
		}
		if (subInfo.getAlternateContactNumber() != null) {
			subInfo.setAlternateContactNumber(trim(subInfo.getAlternateContactNumber(), UNDEFINED_LEN));
		}

	}

}
