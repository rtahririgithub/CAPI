package com.telus.cmb.subscriber.lifecyclehelper.dao;

import java.util.Date;
import java.util.List;

import com.telus.eas.framework.exception.TelusSystemException;
import com.telus.eas.subscriber.info.CallingCirclePhoneListInfo;
import com.telus.eas.subscriber.info.ContractChangeHistoryInfo;
import com.telus.eas.subscriber.info.FeatureParameterHistoryInfo;
import com.telus.eas.subscriber.info.PricePlanChangeHistoryInfo;
import com.telus.eas.subscriber.info.ServiceChangeHistoryInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.VendorServiceChangeHistoryInfo;

public interface ServiceAgreementDao {


	/**
	 * retrieve Calling circle phone number list history for given subscriber and time frame
	 *
	 * @param int banId - account no
	 * @param String subscriberNo - subscriber id
	 * @param String productType - subscriber product type
	 * @param Date from - start date
	 * @param	Date to - end date
	 * @return array of CallingCirclePhoneListInfo
	 *
	 *
	 * @see com.telus.eas.subscriber.info.CallingCirclePhoneListInfo
	 *
	 */
	List<CallingCirclePhoneListInfo> retrieveCallingCirclePhoneNumberListHistory(
			int banId, String subscriberNo, String productType, Date from, Date to);

	/**
	 * Retrieve Contract Change History Info for Subscriber
	 *
	 * @param ban
	 * @param String subscriber Id
	 * @param Date from
	 * @param Date to
	 * @return Array ContractChangeHistory[]
	 *
	 */
	List<ContractChangeHistoryInfo> retrieveContractChangeHistory(int ban, String subscriberID, java.util.Date from, java.util.Date to);

	/**
	 * retrieve Feature parameter history for given subscriber and time frame
	 *
	 * @param int banId - account no
	 * @param String subscriberNo - subscriber id
	 * @param String productType - subscriber product type
	 * @param String[] parameterNames -  array of parameter name
	 * @param Date from - start date
	 * @param	Date to - end date
	 * @return array of FeatureParameterHistoryInfo
	 *
	 * @see com.telus.eas.subscriber.info.FeatureParameterHistoryInfo
	 *
	 */
	List<FeatureParameterHistoryInfo> retrieveFeatureParameterHistory(
			int banId, String subscriberNo, String productType, String[] parameterNames, Date from, Date to);

	/**
	 * Retrieve multi-ring phone numbers.
	 * @param ban int
	 * @param subscriberId String
	 * @throws TelusSystemException
	 * @return String[]
	 *
	 */
	List<String> retrieveMultiRingPhoneNumbers(String subscriberId);

	/**
	 * Retrieve Price Plan Change History Info for Subscriber
	 *
	 * @param ban
	 * @param String subscriber Id
	 * @param Date from
	 * @param Date to
	 * @return Array PricePlanChangeHistory[]
	 *
	 */
	List<PricePlanChangeHistoryInfo> retrievePricePlanChangeHistory(int ban, String subscriberID, java.util.Date from, java.util.Date to);

	/**
	 * Retrieve the subscriber current service agreement, includes all socs and there features
	 *
	 * @param String subscriber number
	 * @return Collection collection of ServiceAgreementInfo
	 *
	 * @see com.telus.eas.subscriber.info.ServiceAgreementInfo
	 *
	 */
	SubscriberContractInfo retrieveServiceAgreementBySubscriberID(String subscriberID);

	/**
	 * Retrieve Service Change History Info for Subscriber
	 *
	 * @param ban
	 * @param String subscriber Id
	 * @param Date from
	 * @param Date to
	 * @param boolean includeAllServices
	 * @return Array ServiceChangeHistory[]
	 *
	 */
	List<ServiceChangeHistoryInfo> retrieveServiceChangeHistory(int ban, String subscriberID, java.util.Date from, java.util.Date to, boolean includeAllServices);

	/**
	 * Retrieve the subscriber's Vendor Service Change History - introduced for MobileApps project (June 2008)
	 *
	 * @param int 		BAN
	 * @param String 	subscriber ID
	 * @param String[]  array of vendor category codes to search upon
	 * 
	 * @return VendorServiceChangeHistoryInfo[]
	 *
	 */
	List<VendorServiceChangeHistoryInfo> retrieveVendorServiceChangeHistory(int ban, String subscriberId, String[] categoryCodes);

	/**
	 * Retrieve the subscriber current Voice Mail feature
	 *
	 * @param String subscriber number
	 * @param String product Type
	 * @return Array, String[2] of Soc code and Feature Code  for Voice Mail
	 *
	 */
	List<String> retrieveVoiceMailFeatureByPhoneNumber(String phoneNumber,String productType);

	/**
	 * @param banId
	 * @param subscriberId
	 * @param productType
	 * @param serviceCode
	 * @param featureCode
	 * @return
	 */
	FeatureParameterHistoryInfo retrieveLastEffectiveFeatureParameter(
			int banId, String subscriberId, String productType,
			String serviceCode, String featureCode);

	List<FeatureParameterHistoryInfo> retrieveCallingCircleParametersByDate(
			int banId, String subscriberId, String productType, Date fromDate);
	/**
	 * Returns a list of four strings: SOC, feature code, feature parameter and service type
	 * @param phoneNumber
	 * @param productType
	 * @return String[]
	 */
	List<String> retrieveCSCFeatureByPhoneNumber(String phoneNumber,String productType);

}
