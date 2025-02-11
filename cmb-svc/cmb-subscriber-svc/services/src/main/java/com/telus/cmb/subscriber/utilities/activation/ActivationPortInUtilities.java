package com.telus.cmb.subscriber.utilities.activation;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.portability.PortOutEligibility;
import com.telus.api.reference.Brand;
import com.telus.api.util.DateUtil;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
import com.telus.cmb.subscriber.utilities.AppConfiguration;
import com.telus.cmb.subscriber.utilities.BaseChangeContext;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.portability.info.PortInEligibilityInfo;

/**
 * @Author R. Fong ,Naresh.A
 * 
 */
public class ActivationPortInUtilities {
	
	private static final Log logger = LogFactory.getLog(ActivationPortInUtilities.class);
	
	// OSP values for PR, PT148/168, PT140 and ST, respectively
	private static final String[] TELUS_OSP_CODES = { "8303", "TU04", "TU08", "TU05" };
	
	/* use the this method to validate shared USIM portIn request 
	 1. OSP is TELUS (8303 in PR, TU04 in PT)
	 2. Incoming brandId should exists in supported shared sim incoming brandId's
	 3. Otgoing brandId should exists in supported shared sim outgoing brandId's */
	
	public static boolean isValidSharedUSIMPortInRequest(PortInEligibilityInfo info) {
		
		/** Read the supported shared sim incoming & outgoing brands from ldap ,this will be used as rollback for easy p2p project.
		today in production ( Oct 2019):-
		    sharedSIMIncomingBrands = Brand.BRAND_ID_KOODO  
		    sharedSIMOutgoingBrands = Brand.BRAND_ID_KOODO
		  for October 2019 :-
		    sharedSIMIncomingBrands = Brand.BRAND_ID_KOODO  
		    sharedSIMOutgoingBrands = Brand.BRAND_ID_KOODO,Brand.BRAND_ID_PC_MOBILE, Brand.BRAND_ID_PUBLIC_MOBILE ,Brand.BRAND_ID_TELUS
		  post october 2019 Or future scope projects :-
		  	sharedSIMIncomingBrands = Brand.BRAND_ID_KOODO,Brand.BRAND_ID_TELUS ;
		 	sharedSIMOutgoingBrands = Brand.BRAND_ID_KOODO,Brand.BRAND_ID_PC_MOBILE, Brand.BRAND_ID_PUBLIC_MOBILE ,Brand.BRAND_ID_TELUS*/
		
		List<Integer> sharedSIMIncomingBrands = AppConfiguration.getSupportedSharedSIMIncomingBrands();
		List<Integer> sharedSIMOutgoingBrands = AppConfiguration.getSupportedSharedSIMOutgoingBrands();
		
		return Arrays.asList(TELUS_OSP_CODES).contains(info.getCurrentServiceProvider()) && sharedSIMIncomingBrands.contains(info.getIncomingBrandId()) && sharedSIMOutgoingBrands.contains(info.getOutgoingBrandId());
	}
	
	public static boolean isExistingInterBrandUSIM(int outgoingBrandId, EquipmentInfo equipmentInfo){
		return equipmentInfo.isHSPA() && equipmentInfo.isUSIMCard() && isValidInterBrandUSIM(outgoingBrandId, equipmentInfo);
	}
	
	private static boolean isValidInterBrandUSIM(int outgoingBrandId,EquipmentInfo equipmentInfo) {
		switch (outgoingBrandId) {
		case Brand.BRAND_ID_TELUS: return equipmentInfo.isValidForBrand0(Brand.BRAND_ID_TELUS);
		case Brand.BRAND_ID_KOODO: return equipmentInfo.isValidForBrand0(Brand.BRAND_ID_KOODO);
		default: return false;
		}
	}
		
		
	public static boolean isExistingMVNEUSIM(int outgoingBrandId,EquipmentInfo equipmentInfo){
		return equipmentInfo.isHSPA() && equipmentInfo.isUSIMCard() && isValidMVNEUSIM(outgoingBrandId,equipmentInfo);
	}
	
	private static boolean isValidMVNEUSIM(int outgoingBrandId, EquipmentInfo equipmentInfo) {
		switch (outgoingBrandId) {
			case Brand.BRAND_ID_KOODO: return isKoodoPrepaidUSIM(equipmentInfo);
			case Brand.BRAND_ID_PUBLIC_MOBILE: return isPublicMobileUSIM(equipmentInfo);
			case Brand.BRAND_ID_PC_MOBILE: return isPCMobileUSIM(equipmentInfo);
			default: return false;
		}
	}
	
	private static boolean isKoodoPrepaidUSIM(EquipmentInfo equipmentInfo) {
		// Note we're calling the isValidForBrand0() shadow method, which doesn't exclude MVNE brands
		return equipmentInfo.isValidForBrand0(Brand.BRAND_ID_KOODO) && StringUtils.equalsIgnoreCase(equipmentInfo.getProductPrePostpaidFlag(), EquipmentInfo.PRODUCT_PREPOSTPAID_FLAG_PREPAID);
	}

	private static boolean isPublicMobileUSIM(EquipmentInfo equipmentInfo) {
		// Note we're calling the isValidForBrand0() shadow method, which doesn't exclude MVNE brands
		return equipmentInfo.isValidForBrand0(Brand.BRAND_ID_PUBLIC_MOBILE);
	}
	
	private static boolean isPCMobileUSIM(EquipmentInfo equipmentInfo) {
		// Note we're calling the isValidForBrand0() shadow method, which doesn't exclude MVNE brands
		return equipmentInfo.isValidForBrand0(Brand.BRAND_ID_PC_MOBILE);
	} 

	public static void validateExistingMVNEUSIM(EquipmentInfo equipmentInfo) throws ApplicationException {
		
		// USIM equipment validation rules for mvne to koodo postpaid ( when MVNE subscribers using their existing usim for portIn)
		if (equipmentInfo.isInUse()) {
			// Although the USIM is in use by Koodo Prepaid on the Redknee platform, it should not be in use on Knowbility (via related local IMSI)
			ActivationUtilities.throwEquipmentValidateFailedException("USIM card [" + equipmentInfo.getSerialNumber() + "] is in use by subscriber [" + equipmentInfo.getPhoneNumber() + "].");
		}
		if (equipmentInfo.isStolen()) {
			ActivationUtilities.throwEquipmentValidateFailedException("USIM card [" + equipmentInfo.getSerialNumber() + "] is stolen.");
		}
		if (!equipmentInfo.isAvailableUSIM()) {
			// The USIM should not be available/assignable as it is currently in use (by the mvne subscriber)
			ActivationUtilities.throwEquipmentValidateFailedException("USIM card [" + equipmentInfo.getSerialNumber() + "] is not assigned to a mvne subscriber.");
		}
		if (equipmentInfo.isExpired()) {
			ActivationUtilities.throwEquipmentValidateFailedException("USIM card [" + equipmentInfo.getSerialNumber() + "] is expired.");
		}
		if (!equipmentInfo.isPreviouslyActivated()) {
			// The USIM should currently be activated (on mnve platform)
			ActivationUtilities.throwEquipmentValidateFailedException("USIM card [" + equipmentInfo.getSerialNumber() + "] is not previously activated as expected for a mvne USIM.");
		}
	}

	public static void validateExistingInterBrandUSIM(EquipmentInfo equipmentInfo) throws ApplicationException {
		
		if (equipmentInfo.isStolen()) {
			ActivationUtilities.throwEquipmentValidateFailedException("USIM card [" + equipmentInfo.getSerialNumber() + "] is stolen.");
		}
		
		if (equipmentInfo.isExpired()) {
			ActivationUtilities.throwEquipmentValidateFailedException("USIM card [" + equipmentInfo.getSerialNumber() + "] is expired.");
		}
	}
	
	public static Date getLogicalDateTime(BaseChangeContext<?> changeContext) throws TelusException, ApplicationException {

		Date logicalDate = changeContext.getRefDataHelper().retrieveLogicalDate();
		Calendar calendar = Calendar.getInstance();
		Calendar logicalCalendar = Calendar.getInstance();
		logicalCalendar.setTime(logicalDate);
		logicalCalendar.set(logicalCalendar.get(Calendar.YEAR), logicalCalendar.get(Calendar.MONTH), logicalCalendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE));

		return logicalCalendar.getTime();
	}
	
	public static boolean isFutureDated(BaseChangeContext<?> changeContext, Date date) throws TelusException, ApplicationException {
		if (date != null) {
			return DateUtil.isAfter(date, changeContext.getRefDataHelper().retrieveLogicalDate());
		}
		return false;
	}
	
	public static PortOutEligibility testPortOutEligibility(String phoneNumber, String ndpInd, SubscriberLifecycleHelper subscriberLifecycleHelper) throws ApplicationException {
		try {
			// Checks if a phone number is eligible for porting to a different carrier or brand
			return subscriberLifecycleHelper.checkSubscriberPortOutEligibility(phoneNumber, ndpInd);
		} catch (Throwable t) {
			logger.error("[testPortOutEligibility] could not determine PortOutEligibility: " + t.getMessage(), t);
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.ACTIVATE_GENERAL_ERROR, t.getMessage(), "", t);
		}
	}
	
}