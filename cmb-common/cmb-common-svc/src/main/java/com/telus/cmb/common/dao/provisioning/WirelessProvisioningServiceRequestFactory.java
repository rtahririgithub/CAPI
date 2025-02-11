package com.telus.cmb.common.dao.provisioning;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.account.Subscriber;
import com.telus.api.reference.SeatType;
import com.telus.api.resource.Resource;
import com.telus.api.resource.ResourceActivity;
import com.telus.cmb.common.dao.provisioning.RequestParamNameConstants.BrandTranslator;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.PostpaidBusinessRegularAccountInfo;
import com.telus.eas.subscriber.info.ResourceActivityInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.utility.info.ProvisioningRequestInfo;

/**
 * @author R. Fong
 *
 */
public class WirelessProvisioningServiceRequestFactory {

	public static ProvisioningRequestInfo createAccountAddRequest(AccountInfo accountInfo, SubscriberInfo subscriberInfo, Date activityDate, AddressInfo addressInfo) throws ApplicationException {

		ProvisioningRequestInfo request = createBaseRequest(RequestParamNameConstants.PROVISIONING_ACCOUNT_ADD_ACTION_TYPE, subscriberInfo.getBanId(), subscriberInfo.getBrandId());
		request.setRequestParams(mapActionParameters(RequestParamNameConstants.PROVISIONING_ACCOUNT_ADD_ACTION_TYPE, accountInfo, subscriberInfo, activityDate, addressInfo));

		return request;
	}
	
	public static ProvisioningRequestInfo createAccountAddRequest(AccountInfo accountInfo, SubscriberInfo subscriberInfo, Date activityDate, AddressInfo addressInfo, String serviceEdition) 
			throws ApplicationException {

		ProvisioningRequestInfo request = createBaseRequest(RequestParamNameConstants.PROVISIONING_ACCOUNT_ADD_ACTION_TYPE, subscriberInfo.getBanId(), subscriberInfo.getBrandId());
		Map<String, String> params = mapActionParameters(RequestParamNameConstants.PROVISIONING_ACCOUNT_ADD_ACTION_TYPE, accountInfo, subscriberInfo, activityDate, addressInfo);
		params.put(RequestParamNameConstants.PRICE_PLAN_CODE, serviceEdition);
		request.setRequestParams(params);

		return request;
	}

	public static ProvisioningRequestInfo createAccountCancelRequest(int ban, int brandId, Date activityDate, String activityReasonCode) throws ApplicationException {

		ProvisioningRequestInfo request = createBaseRequest(RequestParamNameConstants.PROVISIONING_ACCOUNT_CANCEL_ACTION_TYPE, ban, brandId);
		Map<String, String> params = new HashMap<String, String>();
		params.put(RequestParamNameConstants.EFFECTIVE_DATE, formatDateString(activityDate));
		params.put(RequestParamNameConstants.REASON, activityReasonCode);
		// Last, but not least, set the date timestamp parameter
		params.put(RequestParamNameConstants.DATE_TIMESTAMP, formatDateString(new Date()));
		request.setRequestParams(params);

		return request;
	}

	public static ProvisioningRequestInfo createAccountSuspendRequest(int ban, int brandId, Date activityDate, String activityReasonCode) throws ApplicationException {

		ProvisioningRequestInfo request = createBaseRequest(RequestParamNameConstants.PROVISIONING_ACCOUNT_SUSPEND_ACTION_TYPE, ban, brandId);
		Map<String, String> params = new HashMap<String, String>();
		params.put(RequestParamNameConstants.EFFECTIVE_DATE, formatDateString(activityDate));
		params.put(RequestParamNameConstants.REASON, activityReasonCode);
		// Last, but not least, set the date timestamp parameter
		params.put(RequestParamNameConstants.DATE_TIMESTAMP, formatDateString(new Date()));
		request.setRequestParams(params);

		return request;
	}

	public static ProvisioningRequestInfo createAccountResumeRequest(int ban, int brandId, Date activityDate, String activityReasonCode) throws ApplicationException {
		throw new ApplicationException(SystemCodes.CMB_EJB, ErrorCodes.GENERIC_THROWABLE_ERROR_CODE, "Method not implemented yet", "");
	}

	public static ProvisioningRequestInfo createAccountRestoreRequest(int ban, int brandId, Date activityDate, String activityReasonCodee) throws ApplicationException {

		ProvisioningRequestInfo request = createBaseRequest(RequestParamNameConstants.PROVISIONING_ACCOUNT_RESUME_ACTION_TYPE, ban, brandId);
		Map<String, String> params = new HashMap<String, String>();
		params.put(RequestParamNameConstants.EFFECTIVE_DATE, formatDateString(activityDate));
		params.put(RequestParamNameConstants.REASON, activityReasonCodee);
		// Last, but not least, set the date timestamp parameter
		params.put(RequestParamNameConstants.DATE_TIMESTAMP, formatDateString(new Date()));
		request.setRequestParams(params);

		return request;
	}

	public static ProvisioningRequestInfo createAccountChangeRequest(AccountInfo accountInfo, Date activityDate, String serviceEdition) throws ApplicationException {
		
		ProvisioningRequestInfo request = createBaseRequest(RequestParamNameConstants.PROVISIONING_ACCOUNT_CHANGE_ACTION_TYPE, accountInfo.getBanId(), accountInfo.getBrandId());
		Map<String, String> params = new HashMap<String, String>();
		params.put(RequestParamNameConstants.EFFECTIVE_DATE, formatDateString(activityDate));
		params.put(RequestParamNameConstants.PRICE_PLAN_CODE, serviceEdition);
		// Last, but not least, set the date timestamp parameter
		params.put(RequestParamNameConstants.DATE_TIMESTAMP, formatDateString(new Date()));
		params.put(RequestParamNameConstants.CONTACT_FIRST_NAME, accountInfo.getContactName().getFirstName());
		params.put(RequestParamNameConstants.CONTACT_LAST_NAME, accountInfo.getContactName().getLastName());
		params.put(RequestParamNameConstants.CONTACT_PHONE_NUMBER, accountInfo.getContactPhone());
		params.put(RequestParamNameConstants.EMAIL, accountInfo.getEmail());
		params.put(RequestParamNameConstants.LANGUAGE, accountInfo.getLanguage());
		// [11-Nov-2014 - Naresh Annabathula] As per Jun and the BC project team confirmation, CAPI agree to pass below information as contactBusinessName
		String contactBusinessName = accountInfo.isPostpaidBusinessRegular() ? ((PostpaidBusinessRegularAccountInfo) accountInfo).getLegalBusinessName() : accountInfo.getContactName().getFirstName() + " " + accountInfo.getContactName().getLastName();
		params.put(RequestParamNameConstants.CONTACT_BUSINESS_NAME, contactBusinessName);
		request.setRequestParams(params);

		return request;
	}

	public static ProvisioningRequestInfo createSeatAddRequest(AccountInfo accountInfo, SubscriberInfo subscriberInfo, Date activityDate, AddressInfo addressInfo) throws ApplicationException {

		ProvisioningRequestInfo request = createBaseRequest(RequestParamNameConstants.PROVISIONING_SEAT_ADD_ACTION_TYPE, subscriberInfo.getBanId(), subscriberInfo.getBrandId());
		request.setRequestParams(mapActionParameters(RequestParamNameConstants.PROVISIONING_SEAT_ADD_ACTION_TYPE, accountInfo, subscriberInfo, activityDate, addressInfo));

		return request;
	}

	public static ProvisioningRequestInfo createSeatCancelRequest(SubscriberInfo subscriberInfo, Date activityDate) throws ApplicationException {

		ProvisioningRequestInfo request = createBaseRequest(RequestParamNameConstants.PROVISIONING_SEAT_CANCEL_ACTION_TYPE, subscriberInfo.getBanId(), subscriberInfo.getBrandId());
		request.setRequestParams(mapActionParameters(subscriberInfo, activityDate, null));

		return request;
	}

	public static ProvisioningRequestInfo createSeatSuspendRequest(SubscriberInfo subscriberInfo, Date activityDate, String activityReasonCode) throws ApplicationException {

		ProvisioningRequestInfo request = createBaseRequest(RequestParamNameConstants.PROVISIONING_SEAT_SUSPEND_ACTION_TYPE, subscriberInfo.getBanId(), subscriberInfo.getBrandId());
		request.setRequestParams(mapActionParameters(subscriberInfo, activityDate, activityReasonCode));

		return request;
	}

	public static ProvisioningRequestInfo createSeatResumeRequest(SubscriberInfo subscriberInfo, Date activityDate, String activityReasonCode) throws ApplicationException {

		ProvisioningRequestInfo request = createBaseRequest(RequestParamNameConstants.PROVISIONING_SEAT_RESUME_ACTION_TYPE, subscriberInfo.getBanId(), subscriberInfo.getBrandId());
		request.setRequestParams(mapActionParameters(subscriberInfo, activityDate, activityReasonCode));

		return request;
	}

	public static ProvisioningRequestInfo createMultiSeatCancelRequest(int ban, int brandId, List<String> subscriptionIdList, List<String> subscriberPhonerNumberList, Date activityDate)
			throws ApplicationException {

		ProvisioningRequestInfo request = createBaseRequest(RequestParamNameConstants.PROVISIONING_SEAT_CANCEL_ACTION_TYPE, ban, brandId);
		request.setRequestParams(mapActionParameters(subscriptionIdList, subscriberPhonerNumberList, activityDate, null));

		return request;
	}

	public static ProvisioningRequestInfo createMultiSeatSuspendRequest(int ban, int brandId, List<String> subscriptionIdList, List<String> subscriberPhonerNumberList, Date activityDate,
			String activityReasonCode) throws ApplicationException {

		ProvisioningRequestInfo request = createBaseRequest(RequestParamNameConstants.PROVISIONING_SEAT_SUSPEND_ACTION_TYPE, ban, brandId);
		request.setRequestParams(mapActionParameters(subscriptionIdList, subscriberPhonerNumberList, activityDate, activityReasonCode));

		return request;
	}

	public static ProvisioningRequestInfo createMultiSeatResumeRequest(int ban, int brandId, List<String> subscriptionIdList, List<String> subscriberPhonerNumberList, Date activityDate,
			String activityReasonCode) throws ApplicationException {

		ProvisioningRequestInfo request = createBaseRequest(RequestParamNameConstants.PROVISIONING_SEAT_RESUME_ACTION_TYPE, ban, brandId);
		request.setRequestParams(mapActionParameters(subscriptionIdList, subscriberPhonerNumberList, activityDate, activityReasonCode));

		return request;
	}

	private static ProvisioningRequestInfo createBaseRequest(String requestActionType, int ban, int brandId) throws ApplicationException {

		ProvisioningRequestInfo request = new ProvisioningRequestInfo();
		request.setRequestActionType(requestActionType);
		request.setBan(String.valueOf(ban));
		request.setBrand(BrandTranslator.translate(brandId));
		request.setBillingType(RequestParamNameConstants.BUSINESS_FREEDOM_BILLING_TYPE);
		request.setSourceSystemCode(RequestParamNameConstants.SOURCE_SYSTEM_CODE);

		return request;
	}

	public static ProvisioningRequestInfo createAddRemoveVOIPChangeRequest(SubscriberInfo subscriberInfo, Date activityDate, List<ResourceActivityInfo> resourceList, boolean outgoingRequestInd)
			throws ApplicationException {

		ProvisioningRequestInfo request = createBaseRequest(RequestParamNameConstants.PROVISIONING_SEAT_CHANGE_ACTION_TYPE, subscriberInfo.getBanId(), subscriberInfo.getBrandId());

		Map<String, String> params = new HashMap<String, String>();
		params.put(RequestParamNameConstants.SUBSCRIPTION_ID, Long.toString(subscriberInfo.getSubscriptionId()));
		params.put(RequestParamNameConstants.PHONE_NUMBER, subscriberInfo.getPhoneNumber());
		params.put(RequestParamNameConstants.EFFECTIVE_DATE, formatDateString(activityDate));
		List<String> voipNumbers = new ArrayList<String>();
		List<String> voipActions = new ArrayList<String>();
		List<String> voipTypes = new ArrayList<String>();
		for (ResourceActivityInfo resource : resourceList) {
			voipNumbers.add(resource.getResource().getResourceNumber());
			voipActions.add(mapChangeVOIPAction(resource.getResourceActivity()));
			voipTypes.add(RequestParamNameConstants.VOIP_PHONE_TYPE_VOICEFAX);
		}
		params.put(RequestParamNameConstants.VOIP_PHONE_NUMBER_LIST, StringUtils.collectionToCommaDelimitedString(voipNumbers));
		params.put(RequestParamNameConstants.VOIP_ACTION_LIST, StringUtils.collectionToCommaDelimitedString(voipActions));
		params.put(RequestParamNameConstants.VOIP_PHONE_TYPE_LIST, StringUtils.collectionToCommaDelimitedString(voipTypes));
		params.put(RequestParamNameConstants.IGNORE_NOTIFY_VENDOR, mapOutgoingRequestInd(outgoingRequestInd));
		// Last, but not least, set the date timestamp parameter
		params.put(RequestParamNameConstants.DATE_TIMESTAMP, formatDateString(new Date()));
		request.setRequestParams(params);

		return request;
	}

	public static ProvisioningRequestInfo createMoveVOIPChangeRequest(SubscriberInfo source, SubscriberInfo target, Date activityDate, String resourceNumber, boolean outgoingRequestInd)
			throws ApplicationException {

		ProvisioningRequestInfo request = createBaseRequest(RequestParamNameConstants.PROVISIONING_SEAT_CHANGE_ACTION_TYPE, source.getBanId(), source.getBrandId());

		Map<String, String> params = new HashMap<String, String>();
		params.put(RequestParamNameConstants.SUBSCRIPTION_ID, Long.toString(source.getSubscriptionId()));
		params.put(RequestParamNameConstants.PHONE_NUMBER, source.getPhoneNumber());
		params.put(RequestParamNameConstants.EFFECTIVE_DATE, formatDateString(activityDate));
		params.put(RequestParamNameConstants.TARGET_SUBSCRIPTION_ID_LIST, Long.toString(target.getSubscriptionId()));
		params.put(RequestParamNameConstants.TARGET_PHONE_NUMBER_LIST, target.getPhoneNumber());
		params.put(RequestParamNameConstants.VOIP_PHONE_NUMBER_LIST, resourceNumber);
		params.put(RequestParamNameConstants.VOIP_ACTION_LIST, RequestParamNameConstants.VOIP_ACTION_REASSIGN);
		params.put(RequestParamNameConstants.VOIP_PHONE_TYPE_LIST, RequestParamNameConstants.VOIP_PHONE_TYPE_VOICEFAX);
		params.put(RequestParamNameConstants.IGNORE_NOTIFY_VENDOR, mapOutgoingRequestInd(outgoingRequestInd));
		// Last, but not least, set the date timestamp parameter
		params.put(RequestParamNameConstants.DATE_TIMESTAMP, formatDateString(new Date()));
		request.setRequestParams(params);

		return request;
	}

	private static Map<String, String> mapActionParameters(List<String> subscriptionIdList, List<String> subscriberPhonerNumberList, Date activityDate, String activityReasonCode)
			throws ApplicationException {

		Map<String, String> params = new HashMap<String, String>();
		params.put(RequestParamNameConstants.SUBSCRIPTION_ID_LIST, StringUtils.collectionToCommaDelimitedString(subscriptionIdList));
		params.put(RequestParamNameConstants.PHONE_NUMBER_LIST, StringUtils.collectionToCommaDelimitedString(subscriberPhonerNumberList));
		params.put(RequestParamNameConstants.EFFECTIVE_DATE, formatDateString(activityDate));
		if (StringUtils.hasText(activityReasonCode)) {
			params.put(RequestParamNameConstants.REASON, activityReasonCode);
		}
		// Last, but not least, set the date timestamp parameter
		params.put(RequestParamNameConstants.DATE_TIMESTAMP, formatDateString(new Date()));

		return params;
	}

	private static Map<String, String> mapActionParameters(SubscriberInfo subscriberInfo, Date activityDate, String activityReasonCode) throws ApplicationException {

		Map<String, String> params = new HashMap<String, String>();
		params.put(RequestParamNameConstants.SUBSCRIPTION_ID_LIST, Long.toString(subscriberInfo.getSubscriptionId()));
		params.put(RequestParamNameConstants.PHONE_NUMBER_LIST, subscriberInfo.getPhoneNumber());
		params.put(RequestParamNameConstants.EFFECTIVE_DATE, formatDateString(activityDate));
		if (StringUtils.hasText(activityReasonCode)) {
			params.put(RequestParamNameConstants.REASON, activityReasonCode);
		}
		// Last, but not least, set the date timestamp parameter
		params.put(RequestParamNameConstants.DATE_TIMESTAMP, formatDateString(new Date()));

		return params;
	}

	private static Map<String, String> mapActionParameters(String requestActionType, AccountInfo accountInfo, SubscriberInfo subscriberInfo, Date activityDate, AddressInfo addressInfo)
			throws ApplicationException {

		Map<String, String> params = new HashMap<String, String>();
		
		// Mapping parameters for ADDACT and ADD action types
		params.put(RequestParamNameConstants.EFFECTIVE_DATE, formatDateString(activityDate));
		params.put(RequestParamNameConstants.SEAT_TYPE, convertKBSeatTypeToProvisioningSeatType(subscriberInfo.getSeatData().getSeatType()));
		params.put(RequestParamNameConstants.SUBSCRIPTION_ID, Long.toString(subscriberInfo.getSubscriptionId()));
		params.put(RequestParamNameConstants.PHONE_NUMBER, subscriberInfo.getPhoneNumber());
		List<String> voipNumberList = new ArrayList<String>();
		List<String> voipTypeList = new ArrayList<String>();
		boolean mainVoipNumberAssigned = false;
		for (Resource resource : subscriberInfo.getSeatData().getResources()) {
			String resourceType = resource.getResourceType();
			if (Subscriber.RESOURCE_TYPE_PRIMARY_VOIP.equalsIgnoreCase(resourceType) || Subscriber.RESOURCE_TYPE_ADDITIONAL_VOIP.equalsIgnoreCase(resourceType)
					|| Subscriber.RESOURCE_TYPE_TOLLFREE_VOIP.equalsIgnoreCase(resourceType)) {
				// For the ADDACT action, we map the primary VOIP as main_voip_phone number				
				// [19-Aug-2015 - Wilson Cheong] As per Business Connect CR784582, we want to set the main company number of the 
				// primary VOIP number of the primary starter seat to the first toll-free VOIP number (of the primary starter seat) 
				// instead of the primary voip phone number
				if (!mainVoipNumberAssigned && Subscriber.RESOURCE_TYPE_TOLLFREE_VOIP.equalsIgnoreCase(resourceType)
						&& RequestParamNameConstants.PROVISIONING_ACCOUNT_ADD_ACTION_TYPE.equalsIgnoreCase(requestActionType)) {
					params.put(RequestParamNameConstants.MAIN_VOIP_PHONE_NUMBER, resource.getResourceNumber());
					mainVoipNumberAssigned = true;
				} else {
					// Otherwise, we add the VOIP number to the list
					voipNumberList.add(resource.getResourceNumber());
					voipTypeList.add(RequestParamNameConstants.VOIP_PHONE_TYPE_VOICEFAX);
				}
			}
		}
		params.put(RequestParamNameConstants.VOIP_PHONE_NUMBER_LIST, StringUtils.collectionToCommaDelimitedString(voipNumberList));
		params.put(RequestParamNameConstants.VOIP_PHONE_TYPE_LIST, StringUtils.collectionToCommaDelimitedString(voipTypeList));

		// Mapping parameters for ADDACT action only - primary starter seat contact info
		if (RequestParamNameConstants.PROVISIONING_ACCOUNT_ADD_ACTION_TYPE.equalsIgnoreCase(requestActionType)) {
			params.put(RequestParamNameConstants.CONTACT_FIRST_NAME, accountInfo.getContactName().getFirstName());
			params.put(RequestParamNameConstants.CONTACT_LAST_NAME, accountInfo.getContactName().getLastName());
			params.put(RequestParamNameConstants.CONTACT_PHONE_NUMBER, accountInfo.getContactPhone());
			params.put(RequestParamNameConstants.EMAIL, accountInfo.getEmail());
			params.put(RequestParamNameConstants.LANGUAGE, accountInfo.getLanguage());
			// [11-Nov-2014 - Naresh Annabathula] As per Jun and the BC project team confirmation, CAPI agree to pass below information as contactBusinessName
			if (accountInfo.isPostpaidBusinessRegular()) {
				String contactBusinessName = ((PostpaidBusinessRegularAccountInfo) accountInfo).getLegalBusinessName();
				params.put(RequestParamNameConstants.CONTACT_BUSINESS_NAME, contactBusinessName);
			} else if (accountInfo.isPostpaidBusinessPersonal()) {
				String contactBusinessName = accountInfo.getContactName().getFirstName() + " " + accountInfo.getContactName().getLastName();
				params.put(RequestParamNameConstants.CONTACT_BUSINESS_NAME, contactBusinessName);
			}
			// Map subscriber address as account level parameters
			params.put(RequestParamNameConstants.CONTACT_BUSINESS_ADDRESS_STREET, addressInfo.getStreetName());
			params.put(RequestParamNameConstants.CONTACT_BUSINESS_ADDRESS_CITY, addressInfo.getCity());
			params.put(RequestParamNameConstants.CONTACT_BUSINESS_ADDRESS_PROVINCE, addressInfo.getProvince());
			params.put(RequestParamNameConstants.CONTACT_BUSINESS_ADDRESS_COUNTRY, addressInfo.getCountry());
			params.put(RequestParamNameConstants.CONTACT_BUSINESS_ADDRESS_POSTAL_CODE, addressInfo.getPostalCode());

		} else if (RequestParamNameConstants.PROVISIONING_SEAT_ADD_ACTION_TYPE.equalsIgnoreCase(requestActionType)) {
			// Mapping parameters for ADD action only
			// [09-Jun-2016 - R. Fong] BC 2016 Order Simplification - remove first name, last name and email attributes for secondary starter and office seats
			// params.put(RequestParamNameConstants.CONTACT_FIRST_NAME, subscriberInfo.getConsumerName().getFirstName());
			// params.put(RequestParamNameConstants.CONTACT_LAST_NAME, subscriberInfo.getConsumerName().getLastName());
			// params.put(RequestParamNameConstants.EMAIL, subscriberInfo.getEmailAddress());
			// Map subscriber address as seat level parameters
			params.put(RequestParamNameConstants.SEAT_CONTACT_ADDRESS_STREET, addressInfo.getStreetName());
			params.put(RequestParamNameConstants.SEAT_CONTACT_ADDRESS_CITY, addressInfo.getCity());
			params.put(RequestParamNameConstants.SEAT_CONTACT_ADDRESS_PROVINCE, addressInfo.getProvince());
			params.put(RequestParamNameConstants.SEAT_CONTACT_ADDRESS_COUNTRY, addressInfo.getCountry());
			params.put(RequestParamNameConstants.SEAT_CONTACT_ADDRESS_POSTAL_CODE, addressInfo.getPostalCode());
		}

		// Last, but not least, set the date timestamp parameter
		params.put(RequestParamNameConstants.DATE_TIMESTAMP, formatDateString(new Date()));

		return params;
	}

	private static String mapChangeVOIPAction(String activity) throws ApplicationException {

		if (activity.equalsIgnoreCase(ResourceActivity.ADD)) {
			return RequestParamNameConstants.VOIP_ACTION_ADD;
		}
		if (activity.equalsIgnoreCase(ResourceActivity.CANCEL)) {
			return RequestParamNameConstants.VOIP_ACTION_DELETE;
		}

		throw new ApplicationException(SystemCodes.CMB_PROVISIONING_DAO, ErrorCodes.INVALID_INPUT_PARAMETERS, "Missing or invalid parameter", "");
	}

	private static String mapOutgoingRequestInd(boolean outgoingRequestInd) throws ApplicationException {

		if (outgoingRequestInd == true) {
			return RequestParamNameConstants.IGNORE_NOTIFY_VENDOR_NO;
		}

		return RequestParamNameConstants.IGNORE_NOTIFY_VENDOR_YES;
	}

	private static String convertKBSeatTypeToProvisioningSeatType(String seatType) throws ApplicationException {

		if (SeatType.SEAT_TYPE_STARTER.equals(seatType)) {
			return RequestParamNameConstants.PROVISIONING_STARTER_SEAT_VALUE;
		} else if (SeatType.SEAT_TYPE_OFFICE.equals(seatType)) {
			return RequestParamNameConstants.PROVISIONING_OFFICE_SEAT_VALUE;
		} else if (SeatType.SEAT_TYPE_PROFESSIONAL.equals(seatType)) {
			return RequestParamNameConstants.PROVISIONING_PROFESSIONAL_SEAT_VALUE;
		} else {
			String errorMessage;
			if (seatType == null) {
				errorMessage = "Seat type should not be null for provisiong activation order";
			} else {
				errorMessage = "un supported seatType value : " + seatType;
			}
			throw new ApplicationException(SystemCodes.CMB_PROVISIONING_DAO, ErrorCodes.INVALID_SEAT_TYPE, errorMessage, "");
		}
	}

	private static String formatDateString(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		return dateFormat.format(date);
	}

}