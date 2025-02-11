package com.telus.cmb.account.utilities;

import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.telus.api.account.AccountSummary;
import com.telus.cmb.common.util.AttributeTranslator;

public class AccountLifecycleUtilities {
	
	private static final Logger LOGGER = Logger.getLogger(AccountLifecycleUtilities.class);

	public static Hashtable<String, Boolean> getAccountTypeIdentifiers(char accountType, char accountSubType){
		
		Hashtable<String, Boolean> accountTypeIdentifiers=new Hashtable<String, Boolean>();
		
		boolean postpaidConsumer = false;
		boolean postpaidBusinessRegular = false;
		boolean postpaidBusinessPersonal = false;
		boolean postpaidBusinessDealer = false;
		boolean postpaidBusinessOffical = false;
		boolean prepaidConsumer = false;
		boolean IDENCorporateVPN = false;
		boolean IDENCorporate = false;
		boolean postpaidLikeBusinessRegular = false;
		boolean postpaidBoxedConsumer = false;
		boolean postpaidCorporateRegional = false;
		boolean postpaidConsumerEmployee = false;
		boolean postpaidConsumerEmployeeNew = false;
		boolean postpaidCorporateAutotel = false;
		boolean postpaidCorporatePersonal = false;
		boolean postpaidCorporateRegular = false;
		boolean quebecTelPrepaidConsumer = false;
		boolean westernPrepaidConsumer = false;
		
		
		if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER  && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PAGER_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_AUTOTEL_REGULAR)){
			postpaidConsumer = true;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER  && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_TELUS_EMPLOYEE || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_TELUS_EMPLOYEE )) {
			postpaidConsumerEmployee = true;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER  && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_TELUS_EMPLOYEE_NEW || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_PERSONAL )) {
			postpaidConsumerEmployeeNew = true;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_REGULAR || accountSubType==AccountSummary.ACCOUNT_SUBTYPE_PAGER_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_AUTOTEL_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_CONNECT_REGULAR)) {
			postpaidBusinessRegular = true ;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_OFFICAL)	{
			postpaidBusinessOffical = true ;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS &&( accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_PERSONAL|| accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_PERSONAL || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_PERSONAL || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_CONNECT_PERSONAL)) {
			postpaidBusinessPersonal = true;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_DEALER || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_DEALER)) {
			postpaidBusinessDealer = true;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_PREPAID) {
			prepaidConsumer = true ;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_QUEBECTEL) { 
			quebecTelPrepaidConsumer = true ;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_WESTERN_PREPAID)	{
			westernPrepaidConsumer = true ;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_REGULAR) {
			IDENCorporate = true;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_PRIVATE_NETWORK_PLUS) {
			IDENCorporateVPN = true;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PAGER_BOXED)	{
			postpaidBoxedConsumer = true;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE  && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_REGIONAL) {
			postpaidCorporateRegional = true;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE  && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_AUTOTEL_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_AUTOTEL_EARS)) {
			postpaidCorporateAutotel = true;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE  && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_INDIVIDUAL || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_EMPLOYEE)) {
			postpaidCorporatePersonal = true;
		} else if (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE  &&
				(accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_FEDERAL_GOVERNMENT ||
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_ENTERPRISE ||
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_CORPORATE ||
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_ABORIGINAL ||
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_FUSION_EAST_CONV ||
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_KEY ||
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_OFFICIAL ||
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_CNBS ||
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_GOVERNMENT ||
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_NATIONAL_STRATEGIC ||
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_REGIONAL_STRATEGIC ||
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_TMI_AFFILIATE ||
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_TMI_DIVISION ||
						accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_ANYWHERE)) {
			postpaidCorporateRegular = true;
		} else {
			postpaidLikeBusinessRegular = true;
		}
		
		accountTypeIdentifiers.put("postpaidConsumer",postpaidConsumer);
		accountTypeIdentifiers.put("postpaidBusinessRegular",postpaidBusinessRegular);
		accountTypeIdentifiers.put("postpaidBusinessPersonal",postpaidBusinessPersonal);
		accountTypeIdentifiers.put("postpaidBusinessDealer",postpaidBusinessDealer);
		accountTypeIdentifiers.put("postpaidBusinessOffical",postpaidBusinessOffical);
		accountTypeIdentifiers.put("prepaidConsumer",prepaidConsumer);
		accountTypeIdentifiers.put("IDENCorporateVPN",IDENCorporateVPN);
		accountTypeIdentifiers.put("IDENCorporate",IDENCorporate);
		accountTypeIdentifiers.put("postpaidLikeBusinessRegular",postpaidLikeBusinessRegular);
		accountTypeIdentifiers.put("postpaidBoxedConsumer",postpaidBoxedConsumer);
		accountTypeIdentifiers.put("postpaidCorporateRegional",postpaidCorporateRegional);
		accountTypeIdentifiers.put("postpaidConsumerEmployee",postpaidConsumerEmployee);
		accountTypeIdentifiers.put("postpaidConsumerEmployeeNew",postpaidConsumerEmployeeNew);
		accountTypeIdentifiers.put("postpaidCorporateAutotel",postpaidCorporateAutotel);
		accountTypeIdentifiers.put("postpaidCorporatePersonal",postpaidCorporatePersonal);
		accountTypeIdentifiers.put("postpaidCorporateRegular",postpaidCorporateRegular);
		accountTypeIdentifiers.put("quebecTelPrepaidConsumer",quebecTelPrepaidConsumer);
		accountTypeIdentifiers.put("westernPrepaidConsumer",westernPrepaidConsumer);

		return accountTypeIdentifiers;
	}
	
	
	public static boolean addressHasChanged(amdocs.APILink.datatypes.AddressInfo oldAddress, amdocs.APILink.datatypes.AddressInfo newAddress) {

	    String methodName = "addressHasChanged";

	    LOGGER.debug("("+AccountLifecycleUtilities.class.getClass().getName()+"."+ methodName+") Printing 'old' billing address...");
	    printAmdocsAddress(oldAddress);

	    LOGGER.debug("("+AccountLifecycleUtilities.class.getClass().getName()+"."+ methodName+") Printing 'new' billing address...");
	    printAmdocsAddress(newAddress);

	    if (newAddress.type != oldAddress.type
	       ||
	       (newAddress.type == (byte)'C' &&
	         (AttributeTranslator.compare(newAddress.attention,oldAddress.attention) != 0  ||
	         AttributeTranslator.compare(newAddress.city,oldAddress.city) != 0  ||
	         AttributeTranslator.compare(newAddress.province,oldAddress.province) != 0  ||
	         AttributeTranslator.compare(newAddress.postalCode,oldAddress.postalCode) != 0  ||
	         AttributeTranslator.compare(newAddress.civicNo,oldAddress.civicNo) != 0  ||
	         AttributeTranslator.compare(newAddress.civicNoSuffix,oldAddress.civicNoSuffix) != 0  ||
	         AttributeTranslator.compare(newAddress.streetDirection,oldAddress.streetDirection) != 0  ||
	         AttributeTranslator.compare(newAddress.streetName,oldAddress.streetName) != 0  ||
	         AttributeTranslator.compare(newAddress.streetType,oldAddress.streetType) != 0  ||
	         AttributeTranslator.compare(newAddress.unitDesignator,oldAddress.unitDesignator) != 0  ||
	         AttributeTranslator.compare(newAddress.unitIdentifier,oldAddress.unitIdentifier) != 0  ||
	         AttributeTranslator.compare(newAddress.country,oldAddress.country) != 0  ||
	         AttributeTranslator.compare(newAddress.zipGeoCode,oldAddress.zipGeoCode) != 0 ))
	       ||
	       (newAddress.type == (byte)'D' &&
	         (AttributeTranslator.compare(newAddress.attention,oldAddress.attention) != 0  ||
	          AttributeTranslator.compare(newAddress.city,oldAddress.city) != 0  ||
	          AttributeTranslator.compare(newAddress.province,oldAddress.province) != 0  ||
	          AttributeTranslator.compare(newAddress.postalCode,oldAddress.postalCode) != 0  ||
	          AttributeTranslator.compare(newAddress.civicNo,oldAddress.civicNo) != 0  ||
	          AttributeTranslator.compare(newAddress.civicNoSuffix,oldAddress.civicNoSuffix) != 0  ||
	          AttributeTranslator.compare(newAddress.streetName,oldAddress.streetName) != 0  ||
	          AttributeTranslator.compare(newAddress.streetType,oldAddress.streetType) != 0  ||
	          AttributeTranslator.compare(newAddress.rrAreaNumber,oldAddress.rrAreaNumber) != 0  ||
	          AttributeTranslator.compare(newAddress.rrBox,oldAddress.rrBox) != 0  ||
	          AttributeTranslator.compare(newAddress.rrCompartment,oldAddress.rrCompartment) != 0  ||
	          AttributeTranslator.compare(newAddress.rrDeliveryType,oldAddress.rrDeliveryType) != 0  ||
	          AttributeTranslator.compare(newAddress.rrDesignator,oldAddress.rrDesignator) != 0  ||
	          AttributeTranslator.compare(newAddress.rrGroup,oldAddress.rrGroup) != 0  ||
	          AttributeTranslator.compare(newAddress.rrIdentifier,oldAddress.rrIdentifier) != 0  ||
	          AttributeTranslator.compare(newAddress.rrQualifier,oldAddress.rrQualifier) != 0  ||
	          AttributeTranslator.compare(newAddress.rrSite,oldAddress.rrSite) != 0  ||
	          AttributeTranslator.compare(newAddress.country,oldAddress.country) != 0  ||
	          AttributeTranslator.compare(newAddress.zipGeoCode,oldAddress.zipGeoCode) != 0 ))
	       ||
	       (newAddress.type == (byte)'F' &&
	         (AttributeTranslator.compare(newAddress.attention,oldAddress.attention) != 0  ||
	          AttributeTranslator.compare(newAddress.primaryLine,oldAddress.primaryLine) != 0  ||
	          AttributeTranslator.compare(newAddress.secondaryLine,oldAddress.secondaryLine) != 0  ||
	          AttributeTranslator.compare(newAddress.city,oldAddress.city) != 0  ||
	          AttributeTranslator.compare(newAddress.province,oldAddress.province) != 0  ||
	          AttributeTranslator.compare(newAddress.postalCode,oldAddress.postalCode) != 0  ||
	          AttributeTranslator.compare(newAddress.country,oldAddress.country) != 0  ||
	          AttributeTranslator.compare(newAddress.zipGeoCode,oldAddress.zipGeoCode) != 0  ||
	          AttributeTranslator.compare(newAddress.foreignState,oldAddress.foreignState) != 0 ))) {
	       return true;
	       }
	    return false;
	}
	
	public static void printAmdocsAddress(amdocs.APILink.datatypes.AddressInfo pAddress) {

		  String methodName = "printAmdocsAddress";
		  StringBuffer amdocsAddress = new StringBuffer();
		  
		  amdocsAddress.append("AmdocsAddress:{\n");
		  amdocsAddress.append("  type: " ).append( pAddress.type).append("\n");
		  amdocsAddress.append("  fieldedInd: " ).append(  pAddress.fieldedInd).append("\n");
		  amdocsAddress.append("  addressMatchInd: " ).append(  pAddress.addressMatchInd).append("\n");
		  amdocsAddress.append("  attention: " ).append(  pAddress.attention).append("\n");
		  amdocsAddress.append("  primaryLine: " ).append(  pAddress.primaryLine).append("\n");
		  amdocsAddress.append("  secondaryLine: " ).append(  pAddress.secondaryLine).append("\n");
		  amdocsAddress.append("  city: " ).append(  pAddress.city).append("\n");
		  amdocsAddress.append("  province: " ).append(  pAddress.province).append("\n");
		  amdocsAddress.append("  postalCode: " ).append( pAddress.postalCode).append("\n");
		  amdocsAddress.append("  civicNo: " ).append(  pAddress.civicNo).append("\n");
		  amdocsAddress.append("  civicNoSuffix: " ).append(  pAddress.civicNoSuffix).append("\n");
		  amdocsAddress.append("  streetDirection: " ).append(  pAddress.streetDirection).append("\n");
		  amdocsAddress.append("  streetName: " ).append(  pAddress.streetName).append("\n");
		  amdocsAddress.append("  streetType: " ).append(  pAddress.streetType).append("\n");
		  amdocsAddress.append("  unitDesignator: " ).append(  pAddress.unitDesignator).append("\n");
		  amdocsAddress.append("  unitIdentifier: " ).append(  pAddress.unitIdentifier).append("\n");
		  amdocsAddress.append("  rrAreaNumber: " ).append(  pAddress.rrAreaNumber).append("\n");
		  amdocsAddress.append("  rrBox: " ).append(  pAddress.rrBox).append("\n");
		  amdocsAddress.append("  rrCompartment: " ).append(  pAddress.rrCompartment).append("\n");
		  amdocsAddress.append("  rrDeliveryType: " ).append(  pAddress.rrDeliveryType).append("\n");
		  amdocsAddress.append("  rrDesignator: " ).append(  pAddress.rrDesignator).append("\n");
		  amdocsAddress.append("  rrGroup: " ).append(  pAddress.rrGroup).append("\n");
		  amdocsAddress.append("  rrIdentifier: " ).append(  pAddress.rrIdentifier).append("\n");
		  amdocsAddress.append("  rrQualifier: " ).append(  pAddress.rrQualifier).append("\n");
		  amdocsAddress.append("  rrSite: " ).append(  pAddress.rrSite).append("\n");
		  amdocsAddress.append("  country: " ).append(  pAddress.country).append("\n");
		  amdocsAddress.append("  zipGeoCode: " ).append(  pAddress.zipGeoCode).append("\n");
		  amdocsAddress.append("  foreignState: " ).append(  pAddress.foreignState).append("\n");
		  amdocsAddress.append("}");
		  
		  LOGGER.debug("("+AccountLifecycleUtilities.class.getClass().getName()+"."+ methodName+")"+amdocsAddress);
		}
	

}
