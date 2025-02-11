package com.telus.cmb.subscriber.utilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.account.Account;
import com.telus.api.account.SubscriberIdentifier;
import com.telus.api.reference.SeatType;
import com.telus.cmb.common.dao.provisioning.VOIPSupplementaryServiceDao;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.ProductSubscriberListInfo;
import com.telus.eas.account.info.VOIPAccountInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.utility.info.LicenseInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.ServiceEditionInfo;

public class BusinessConnectUtil {

	public static final String[] PROVINCE_CODES_QUEBEC = { "PQ", "QC" };	
				
	public static boolean isBusinessConnectPrimaryStarterSeatActivation(SubscriberInfo subscriberInfo, AccountInfo accountInfo) {
		return isBusinessConnectStarterSeat(subscriberInfo) && isAccountActivation(accountInfo);
	}
	
	private static boolean isAccountActivation(AccountInfo accountInfo) {
		return accountInfo.getStatus() == Account.STATUS_TENTATIVE && accountInfo.getActiveSubscribersCount() == 0 && accountInfo.getReservedSubscribersCount() == 0;
	}
	
	public static boolean isBusinessConnectStarterSeat(SubscriberInfo subscriberInfo) {
		return StringUtils.equals(SeatType.SEAT_TYPE_STARTER, subscriberInfo.getSeatData() != null ? subscriberInfo.getSeatData().getSeatType() : null);
	}
	
	public static boolean isBusinessConnectPrimaryStarterSeat(SubscriberInfo subscriberInfo, VOIPSupplementaryServiceDao voipSupplementaryServiceDao) throws ApplicationException {
		
		if (isBusinessConnectStarterSeat(subscriberInfo)) {
			String subscriptionId = voipSupplementaryServiceDao.getPrimaryStarterSeatSubscriptionId(subscriberInfo.getBanId());
			return Long.valueOf(subscriptionId).longValue() == subscriberInfo.getSubscriptionId();
		} 
		
		return false;
	}
	
	public static boolean isBusinessConnectPrimaryStarterSeat(SubscriberInfo subscriberInfo, VOIPAccountInfo voipAccountInfo) throws ApplicationException {
		return voipAccountInfo.getOperatorSubscriptionId() == subscriberInfo.getSubscriptionId();
	}
	
	public static Date getActivityDateFromPricePlan(SubscriberContractInfo subscriberContractInfo, Date logicalDate) {
		
		Date effectiveDate = subscriberContractInfo.getPricePlan0().getEffectiveDate();
		if (effectiveDate != null) {
			return effectiveDate; 
		}
		
		return logicalDate;
	}
	
	// Price plan services
	public static String getVOIPServiceEditionFromPricePlan(SubscriberContractInfo subscriberContractInfo, List<ServiceEditionInfo> serviceEditions) throws ApplicationException {
		
		for (ServiceEditionInfo serviceEdition : serviceEditions) {
			String switchCode = serviceEdition.getCode();
			if (subscriberContractInfo.getPricePlan0().containsSwitchCode(switchCode)) {
				return switchCode;
			}
		}
		
		throw new ApplicationException(ErrorCodes.GENERIC_THROWABLE_ERROR_CODE, "Service Edition switch code missing in price plan.", StringUtils.EMPTY);		
	}
	
	public static void validateServiceEditionChange(String currentServiceEdition, String newServiceEdition) throws ApplicationException {
		if (StringUtils.equalsIgnoreCase(newServiceEdition, currentServiceEdition)) {
			throw new ApplicationException(ErrorCodes.NO_SERVICE_EDITION_CHANGE, "No service edition change.", StringUtils.EMPTY);
		}
	}
	
	// Moved from the SubscriberLifecycleFacade for BC Integrations
	public static void validateBusinessConnectSeatActivationRequest(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, ProductSubscriberListInfo[] productSubscriberListInfo) throws ApplicationException {

		// Seat data validation
		if (subscriberInfo.getSeatData() == null) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.MISSING_SEAT_DATA, "Seat data is mandatory for Business Connect seat activation.");
		}
		String subscriberSeatType = subscriberInfo.getSeatData().getSeatType();
		String subscriberSeatGroup = subscriberInfo.getSeatData().getSeatGroup();

		// Business Connect seat address validation
		if (subscriberInfo.getAddress() == null) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.MISSING_ADDRESS_FOR_BC_SEAT_ACTIVATION, "Address is mandatory for Business Connect seat activation.");
		}

		// Business Connect price plan seat type validation
		String priceplanSeatType = subscriberContractInfo.getPricePlan().getSeatType();
		if (!StringUtils.equals(priceplanSeatType, subscriberSeatType)) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_SEAT_TYPE_BC_SEAT_ACTIVATION,
					"Priceplan seat type is not valid for activation; priceplan seat type [" + priceplanSeatType + "] should match subscriber seat type [" + subscriberSeatType + "].", StringUtils.EMPTY);
		}

		// Each seat group requires a starter seat in active/reserved status on the BAN before activating any non-starter seat on the same seat group. However, we allow
		// office and mobile seat subscribers to activate immediately even though the starter seat is future dated (i.e., the starter seat is in reserved status).
		if (!StringUtils.equals(SeatType.SEAT_TYPE_STARTER, subscriberSeatType) && !BusinessConnectUtil.isGroupActivatedOnBan(productSubscriberListInfo, subscriberSeatGroup)
				&& !BusinessConnectUtil.isGroupReservedOnBan(productSubscriberListInfo, subscriberSeatGroup)) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.BC_SEAT_ACTIVATION_VALIDATION_ERROR, "Seat group ID [" + subscriberSeatGroup + "] is not active on ban ["
					+ subscriberInfo.getBanId() + "]; starter seat must be active on seat group before activating any non-starter seats on the group.", StringUtils.EMPTY);
		}

		// Only one starter seat is allowed per seat group
		if (StringUtils.equals(SeatType.SEAT_TYPE_STARTER, subscriberSeatType) && (BusinessConnectUtil.isGroupActivatedOnBan(productSubscriberListInfo, subscriberSeatGroup)
				|| BusinessConnectUtil.isGroupReservedOnBan(productSubscriberListInfo, subscriberSeatGroup))) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.BC_SEAT_ACTIVATION_VALIDATION_ERROR,
					"Seat group ID [" + subscriberSeatGroup + "] already has an active starter seat on ban [" + subscriberInfo.getBanId()
							+ "]; cannot activate a second starter seat on the same group. Please choose a different seat group ID to activate new starter seat.", StringUtils.EMPTY);
		}
	}
	
	// Moved from the SubscriberLifecycleFacade for BC Integrations
	private static boolean isGroupActivatedOnBan(ProductSubscriberListInfo[] productSubscriberListInfoArray, String seatGroup) {
		
		for (ProductSubscriberListInfo productSubscriberListInfo : productSubscriberListInfoArray) {
			SubscriberIdentifier[] activeSubscriberIdentifierList = productSubscriberListInfo.getActiveSubscriberIdentifiers();
			for (SubscriberIdentifier activeSubscriberIdentifier : activeSubscriberIdentifierList) {
				if (isStarterSubscriberInGroup(seatGroup, activeSubscriberIdentifier)) {
					return true;
				}
			}
		}
		
		return false;
	}

	private static boolean isStarterSubscriberInGroup(String seatGroup, SubscriberIdentifier activeSubscriberIdentifier) {
		return StringUtils.equals(activeSubscriberIdentifier.getSeatGroup(), seatGroup) && StringUtils.equals(SeatType.SEAT_TYPE_STARTER, activeSubscriberIdentifier.getSeatType());
	}
	
	// Moved from the SubscriberLifecycleFacade for BC Integrations
	private static boolean isGroupReservedOnBan(ProductSubscriberListInfo[] productSubscriberListInfoArray, String seatGroup) {

		for (ProductSubscriberListInfo productSubscriberListInfo : productSubscriberListInfoArray) {
			SubscriberIdentifier[] reservedSubscriberIdentifierList = productSubscriberListInfo.getReservedSubscriberIdentifiers();
			for (SubscriberIdentifier reservedSubscriberIdentifier : reservedSubscriberIdentifierList) {
				if (isStarterSubscriberInGroup(seatGroup, reservedSubscriberIdentifier)) {
					return true;
				}
			}
		}

		return false;
	}

	public static String getLicenseSwitchCodeFromPricePlan(PricePlanInfo pricePlan, List<LicenseInfo> licenses) throws ApplicationException {

		for (LicenseInfo license : licenses) {
			String switchCode = license.getCode();
			if (pricePlan.containsSwitchCode(switchCode)) {
				return switchCode;
			}
		}

		return null;
	}

	public static List<String> getLicenseSwitchCodesFromAddOnServices(ServiceAgreementInfo[] contractServices, List<LicenseInfo> licenses) throws ApplicationException {

		List<String> switchCodes = new ArrayList<String>();
		for (ServiceAgreementInfo contractService : contractServices) {
			for (LicenseInfo license : licenses) {
				if (contractService.getService().containsSwitchCode(license.getCode())) {
					switchCodes.add(license.getCode());
				}
			}
		}

		return switchCodes;
	}

}