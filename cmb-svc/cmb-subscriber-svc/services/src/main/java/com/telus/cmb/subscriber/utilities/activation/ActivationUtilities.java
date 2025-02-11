package com.telus.cmb.subscriber.utilities.activation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.telus.api.ApplicationException;
import com.telus.api.BrandNotSupportedException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.SIMCardEquipment;
import com.telus.api.reference.Brand;
import com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacade;
import com.telus.cmb.common.util.DateUtil;
import com.telus.cmb.productequipment.manager.svc.ProductEquipmentManager;
import com.telus.cmb.subscriber.bo.ContractBo;
import com.telus.cmb.subscriber.bo.EquipmentBo;
import com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifecycleManager;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.subscriber.info.ServiceFeatureInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

/**
 * @Author Brandon Wen
 * 
 */
public class ActivationUtilities {
	
	private static final Log logger = LogFactory.getLog(ActivationUtilities.class);
	
	public static final String NUMBER_LOCATION_POSTPAID = "TLS";
	public static final String BILLING_SYSTEM_FAILED = "BSTF";
	public static final String SIM_IMEI_RESERVED = "RESVD";
	public static final String DEFAULT_SUBSCRIPTION_ROLE = "AA";
	public static final String ACTIVATION_MEMO_TYPE = "ACTR";
	
	public static Date attachTimeComponent(Date logicalDate) {

		Calendar calendar = Calendar.getInstance();
		Calendar logicalCalndar = Calendar.getInstance();
		logicalCalndar.setTime(logicalDate);

		logicalCalndar.set(logicalCalndar.get(Calendar.YEAR),
				logicalCalndar.get(Calendar.MONTH),
				logicalCalndar.get(Calendar.DAY_OF_MONTH),
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE));

		return logicalCalndar.getTime();
	}

	public static SubscriberInfo[] decorate(SubscriberInfo[] subscribers, boolean existingSubscriber, boolean internalUse, int[] brandIds) throws BrandNotSupportedException {
		subscribers = filterSubscribersByBrand(subscribers, brandIds, internalUse);
		for(int i=0; i<subscribers.length; i++) {
			subscribers[i].setInternalUse(internalUse);
			
			// 2013 Jan release, only support PCS activation.   
			if (!subscribers[i].isPCS()) {
				throw new IllegalArgumentException("Invalid subscriber product type: " + subscribers[i].getProductType());
			}

			if (!internalUse) {
				subscribers[i] = filterSubscriberByBrand(subscribers[i], brandIds);
			}
		}

		return subscribers;
	}

	public static SubscriberInfo[] filterSubscribersByBrand(SubscriberInfo[] subscribers, int[] brandIds, boolean internalUse) {
		if (brandIds == null || brandIds.length == 0 || internalUse)
			return subscribers;
		ArrayList<SubscriberInfo> list = new ArrayList<SubscriberInfo>(subscribers.length);
		for (int i = 0; i < subscribers.length; i++) {
			if (isBrandIdExist(subscribers[i].getBrandId(), brandIds))
				list.add(subscribers[i]);                
		}
		subscribers = (SubscriberInfo[]) java.lang.reflect.Array.newInstance(
				subscribers.getClass().getComponentType(), list.size());
		return (SubscriberInfo[]) list.toArray(subscribers);
	}

	public static SubscriberInfo filterSubscriberByBrand(SubscriberInfo subscriber, int[] brandIds) throws BrandNotSupportedException {
		if (brandIds == null || brandIds.length == 0)
			return subscriber;
		if (!isBrandIdExist(subscriber.getBrandId(), brandIds))
			throw new BrandNotSupportedException(subscriber, brandIds);
		return subscriber;
	}

	public static boolean validateBrandId(int brandId, Brand[] brands) {
		int[] brandIds = new int[brands.length];
		for (int i = 0; i < brands.length; i++) {
			brandIds[i] = brands[i].getBrandId();
		}				
		return isBrandIdExist(brandId, brandIds);
	}
	
	private static boolean isBrandIdExist(int brand, int[] brandIds){
		for(int i= 0; i < brandIds.length; i++){
			if(brand == brandIds[i]){
				return true;
			}
		}
		return false;
	}

	public static String getReasonCodeKey(Subscriber sub) throws TelusAPIException {
		return sub.getBrandId() + sub.getProductType() + (sub.getAccount().isPostpaid() == true ? "O" : "R");
	}
	
	public static boolean isFutureDated(Date refDate, Date logicalDate) {
		return refDate != null ? DateUtil.isAfter(refDate, logicalDate) : false;
	}
	
	public static void setSIMMuleRelation(boolean activate, Date startServiceDate, EquipmentBo equipment, ProductEquipmentManager productEquipmentManager) {
		
		try {
			if (equipment.isSIMCard()) {
				String sim = equipment.getSerialNumber();
				String mule = ((SIMCardEquipment) equipment.getDelegate()).getLastMuleIMEI();
				Date date = startServiceDate == null ? date = new Date() : startServiceDate;

				if (StringUtils.hasText(mule)) {
					if (activate) {
						productEquipmentManager.activateSIMMule(sim, mule, date);
					} else {
						productEquipmentManager.setSIMMule(sim, mule, date, SIM_IMEI_RESERVED);
					}
				} else {
					if (activate) {
						productEquipmentManager.startSIMMuleRelation(sim, date);
					}
				}
			}
		} catch (Throwable t) {
			// Any errors that occur are to be caught and ignored. Failure to set SIM-mule relation should not impede the activation of a subscriber.
			logger.error("[setSIMMuleRelation] " + t.getMessage(), t);
		}
	}
	
	public static void updateSubscriptionRole(SubscriberInfo subscriber, String subscriptionRoleCode, String csrId, SubscriberLifecycleManager subscriberLifecycleManager) {
		try {
			if (!StringUtils.hasText(subscriptionRoleCode)) {
				// Set the default value if subscriptionRoleCode is null or empty
				subscriptionRoleCode = DEFAULT_SUBSCRIPTION_ROLE;
			}
			subscriberLifecycleManager.updateSubscriptionRole(subscriber.getBanId(), subscriber.getSubscriberId(), subscriptionRoleCode, subscriber.getDealerCode(), subscriber.getSalesRepId(), 
					csrId);
		} catch (Throwable t) {
			// Any errors that occur are to be caught and ignored. Failure to set subscription role should not impede the activation of a subscriber.
			logger.error("[updateSubscriptionRole] " + t.getMessage(), t);
		}
	}
	
	public static void createActivationMemo(SubscriberInfo subscriber, String memoText, AccountLifecycleFacade accountLifecycleFacade, String sessionId) {
		try {
			// create memo if memoText passed in is not null
			if (StringUtils.hasText(memoText)) {
				MemoInfo memo = new MemoInfo(subscriber.getBanId(), ACTIVATION_MEMO_TYPE, subscriber.getSubscriberId(), subscriber.getProductType(), memoText);
				accountLifecycleFacade.asyncCreateMemo(memo, sessionId);
			} else {
				logger.warn("[createActivationMemo] memo text is null or empty.");
			}
		} catch (Throwable t) {
			// Any errors that occur are to be caught and ignored. Failure to create user memo should not impede the activation of a subscriber.
			logger.error("[createActivationMemo] " + t.getMessage(), t);
		}
	}
	
	public static void activationValidate(int brandId, EquipmentInfo equipmentInfo) throws ApplicationException {
		
		if (equipmentInfo.isHSPA()) {
			if (equipmentInfo.isUSIMCard()) {
				validateUSIMCard(brandId, equipmentInfo);
			} else {
				// For non-USIM HSPA equipment
				if (equipmentInfo.isStolen()) {
					throwEquipmentValidateFailedException("Equipment [" + equipmentInfo.getSerialNumber() + "] is stolen.");
				} else if (!equipmentInfo.isValidForBrand(brandId)) {
					throwEquipmentValidateFailedException("Equipment [" + equipmentInfo.getSerialNumber() + "] does not support brand [" + brandId + "].");
				}
			}
		} else {
			if (equipmentInfo.isInUse()) {
				throwEquipmentValidateFailedException("Equipment [" + equipmentInfo.getSerialNumber() + "] is in use by subscriber [" + equipmentInfo.getPhoneNumber() + "].");
			} else if (equipmentInfo.isStolen()) {
				throwEquipmentValidateFailedException("Equipment [" + equipmentInfo.getSerialNumber() + "] is stolen.");
			}
		}
	}

	public static void validateUSIMCard(int brandId, EquipmentInfo equipmentInfo) throws ApplicationException {
		
		if (equipmentInfo.isInUse()) {
			// The USIM should not be in use in Knowbility (via related IMSI)
			throwEquipmentValidateFailedException("USIM card [" + equipmentInfo.getSerialNumber() + "] is in use by subscriber [" + equipmentInfo.getPhoneNumber() + "].");
		}
		if (equipmentInfo.isStolen()) {
			throwEquipmentValidateFailedException("USIM card [" + equipmentInfo.getSerialNumber() + "] is stolen.");
		}
		if (!equipmentInfo.isAvailableUSIM()) {
			// The USIM should be available/assignable
			throwEquipmentValidateFailedException("USIM card [" + equipmentInfo.getSerialNumber() + "] is not assignable.");
		}		
		if (!equipmentInfo.isValidForBrand(brandId)) {
			// The USIM should be valid for the brand the subscriber is activating on - note that the isValidForBrand() method artificially excludes Koodo Prepaid
			throwEquipmentValidateFailedException("USIM card [" + equipmentInfo.getSerialNumber() + "] does not support brand [" + brandId + "].");
		}
		if (equipmentInfo.isExpired()) {
			throwEquipmentValidateFailedException("USIM card [" + equipmentInfo.getSerialNumber() + "] is expired.");
		}
		if (equipmentInfo.isPreviouslyActivated()) {
			// The USIM should not be previously activated, i.e., a hand-me-down USIM
			throwEquipmentValidateFailedException("USIM card [" + equipmentInfo.getSerialNumber() + "] is previously activated.");
		}
	}

	public static void throwEquipmentValidateFailedException(String message) throws ApplicationException {
		throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.EQUIPMENT_VALIDATE_FAILED, message, "");
	}
	
	public static void checkForVoicemailService(ContractBo contract) throws ApplicationException, UnknownObjectException, TelusAPIException {
		if (contract != null) {
			List<ServiceFeatureInfo> vmOrphanFeatures = contract.getVoicemailOrphanFeatures();
			if (!vmOrphanFeatures.isEmpty()) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.MISSING_REQUIRED_SERVICE, "Service " + vmOrphanFeatures.get(0).getServiceCode() + " cannot be added without VM service.", "");
			}
		}
	}
	
}