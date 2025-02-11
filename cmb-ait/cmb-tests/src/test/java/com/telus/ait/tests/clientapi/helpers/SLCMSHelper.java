package com.telus.ait.tests.clientapi.helpers;

import com.telus.ait.tests.clientapi.constants.DefaultValues;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscriberlifecyclemanagementservicerequestresponse_v1.PortInEligibility;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscriberlifecyclemanagementservicerequestresponse_v1.ReleaseSubscriber;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscriberlifecyclemanagementservicerequestresponse_v1.ReservePhoneNumberForSubscriber;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.servicerequestcommontypes_v1.UserServiceProfile;

public class SLCMSHelper extends CommonHelper {
	
	public static ReservePhoneNumberForSubscriber createDefaultReservePhoneNumber(String ban, String productType, String phoneNumberPattern, String serialNumber) {
		ReservePhoneNumberForSubscriber reservePhoneNum = new ReservePhoneNumberForSubscriber();
		reservePhoneNum.setBillingAccountNumber(ban);
		reservePhoneNum.setProductType(getDefaultValue(productType, DefaultValues.PRODUCT_TYPE));
		reservePhoneNum.setPhoneNumberPattern(phoneNumberPattern);
		reservePhoneNum.setLikeMatchInd(DefaultValues.LIKE_MATCH_IND);
		reservePhoneNum.setAsianFriendlyInd(DefaultValues.ASIAN_FRIENDLY_IND);
		reservePhoneNum.setPortInReserveRequiredInd(DefaultValues.PORT_IN_RESERVE_REQUIRED_IND);
		reservePhoneNum.setOfflineReservationInd(DefaultValues.OFFLINE_RESERVATION_IND);
		reservePhoneNum.setUserProfile(createUserServiceProfile(DefaultValues.DEALER_CODE, DefaultValues.SALES_REP_CODE));
		reservePhoneNum.setPortInEligibility(null);
		reservePhoneNum.setEquipmentSerialNumber(serialNumber);
		return reservePhoneNum;
	}
	
	public static ReleaseSubscriber createDefaultReleasePhoneNumber(String ban, String subscriberId, String productType) {
		ReleaseSubscriber releasePhoneNum = new ReleaseSubscriber();
		releasePhoneNum.setBillingAccountNumber(ban);
		releasePhoneNum.setSubscriberId(subscriberId);
		releasePhoneNum.setProductType(productType);
		releasePhoneNum.setCancelPortInInd(DefaultValues.CANCEL_PORT_IN_IND);
		releasePhoneNum.setUserProfile(createUserServiceProfile(DefaultValues.DEALER_CODE, DefaultValues.SALES_REP_CODE));
		releasePhoneNum.setPortInEligibility(null);
		return releasePhoneNum;
	}
	
	public static ReservePhoneNumberForSubscriber createReservePhoneNumber(String ban, String productType, String phoneNumberPattern, String dealerCode, String salesRepCode, 
			String serialNumber, Boolean likeMatchInd, Boolean asianFriendlyInd, Boolean offlineReservationInd, Boolean portInReserveRequiredInd, PortInEligibility portInEligibility) {
		ReservePhoneNumberForSubscriber reservePhoneNum = new ReservePhoneNumberForSubscriber();
		reservePhoneNum.setBillingAccountNumber(ban);
		reservePhoneNum.setProductType(getDefaultValue(productType, DefaultValues.PRODUCT_TYPE));
		reservePhoneNum.setPhoneNumberPattern(phoneNumberPattern);
		reservePhoneNum.setLikeMatchInd(likeMatchInd);
		reservePhoneNum.setAsianFriendlyInd(asianFriendlyInd);
		reservePhoneNum.setPortInReserveRequiredInd(portInReserveRequiredInd);
		reservePhoneNum.setOfflineReservationInd(offlineReservationInd);
		reservePhoneNum.setUserProfile(createUserServiceProfile(dealerCode, salesRepCode));
		reservePhoneNum.setPortInEligibility(portInEligibility);
		reservePhoneNum.setEquipmentSerialNumber(serialNumber);
		return reservePhoneNum;
	}
	
	public static ReleaseSubscriber createReleasePhoneNumber(String ban, String subscriberId, String productType, String dealerCode, String salesRepCode, Boolean cancelPortInInd,
			PortInEligibility portInEligibility) {
		ReleaseSubscriber releasePhoneNum = new ReleaseSubscriber();
		releasePhoneNum.setBillingAccountNumber(ban);
		releasePhoneNum.setSubscriberId(subscriberId);
		releasePhoneNum.setProductType(productType);
		releasePhoneNum.setCancelPortInInd(cancelPortInInd);
		releasePhoneNum.setUserProfile(createUserServiceProfile(dealerCode, salesRepCode));
		releasePhoneNum.setPortInEligibility(portInEligibility);
		return releasePhoneNum;
	}
	
	public static UserServiceProfile createUserServiceProfile(String dealerCode, String salesRepCode) {
		UserServiceProfile userServiceProfile = new UserServiceProfile();
		userServiceProfile.setDealerCode(getDefaultValue(dealerCode, DefaultValues.DEALER_CODE));
		userServiceProfile.setSalesRepCode(getDefaultValue(salesRepCode, DefaultValues.SALES_REP_CODE));
		return userServiceProfile;
	}
		
}
