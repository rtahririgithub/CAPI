package com.telus.provider.util;

import java.util.Collection;
import java.util.Date;

import com.telus.api.account.AvailablePhoneNumber;
import com.telus.api.account.CallingCircleParameters;
import com.telus.api.account.ServicesValidation;
import com.telus.api.util.SessionUtil;
import com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifecycleManager;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AvailablePhoneNumberInfo;
import com.telus.eas.account.info.CancellationPenaltyInfo;
import com.telus.eas.account.info.FleetIdentityInfo;
import com.telus.eas.account.info.MigrationRequestInfo;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.account.info.PricePlanValidationInfo;
import com.telus.eas.account.info.TalkGroupInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.info.DiscountInfo;
import com.telus.eas.subscriber.info.AdditionalMsiSdnFtrInfo;
import com.telus.eas.subscriber.info.CallInfo;
import com.telus.eas.subscriber.info.CallListInfo;
import com.telus.eas.subscriber.info.CommitmentInfo;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.eas.subscriber.info.SearchResultByMsiSdn;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.SubscriptionPreferenceInfo;
import com.telus.eas.subscriber.info.UsageProfileListsSummaryInfo;

/**
 * This class provides implementation of SubscriberManagerBean, who delegate all its work to new
 * CMB EJB - SubscriberLifecycleManager 
 *
 */
public class SubscriberManagerBeanCmbImpl implements SubscriberManagerBean {
	
	private static final long serialVersionUID = 1L;
	private SubscriberLifecycleManager subscriberLifecycleManager;
	private String sessionId;
	
	public SubscriberManagerBeanCmbImpl( SubscriberLifecycleManager manager) {
		this.subscriberLifecycleManager = manager;
	}

	public String getSessionId() throws Throwable {
		if ( sessionId==null) {
			sessionId = SessionUtil.getSessionId(subscriberLifecycleManager);
		} 
		return sessionId;
	}

	public void saveSubscriptionPreference(
			SubscriptionPreferenceInfo preferenceInfo, String user) {
		subscriberLifecycleManager.saveSubscriptionPreference(preferenceInfo,
				user);
	}

	public void activateReservedSubscriber(SubscriberInfo subscriberInfo,
			SubscriberContractInfo subscriberContractInfo,
			Date startServiceDate, String activityReasonCode,
			ServicesValidation srvValidation, String portProcessType,
			int oldBanId, String oldSubscriberId)
			throws Throwable {
		subscriberLifecycleManager.activateReservedSubscriber(subscriberInfo,
				subscriberContractInfo, startServiceDate, activityReasonCode,
				srvValidation, portProcessType, oldBanId, oldSubscriberId,
				getSessionId());
	}

	public void adjustCall(int ban, String subscriberId, String productType,
			int billSeqNo, Date channelSeizureDate, String messageSwitchId,
			double adjustmentAmount, String adjustmentReasonCode,
			String memoText, String usageProductType)
			throws Throwable {
		subscriberLifecycleManager.adjustCall(ban, subscriberId, productType,
				billSeqNo, channelSeizureDate, messageSwitchId,
				adjustmentAmount, adjustmentReasonCode, memoText,
				usageProductType, getSessionId());
	}

	public UsageProfileListsSummaryInfo getUsageProfileListsSummary(int ban,
			String subscriberId, int billSeqNo, String productType
			) throws Throwable {
		return subscriberLifecycleManager.getUsageProfileListsSummary(ban,
				subscriberId, billSeqNo, productType, getSessionId());
	}

	public CallListInfo retrieveBilledCallsList(int ban, String subscriberId,
			String productType, int billSeqNo, char callType, Date fromDate,
			Date toDate, boolean getAll)
			throws Throwable {
		return subscriberLifecycleManager.retrieveBilledCallsList(ban,
				subscriberId, productType, billSeqNo, callType, fromDate,
				toDate, getAll, getSessionId());
	}

	public CallInfo retrieveCallDetails(int ban, String subscriberId,
			String productType, int billSeqNo, Date channelSeizureDate,
			String messageSwitchId, String callProductType)
			throws Throwable {
		return subscriberLifecycleManager.retrieveCallDetails(ban,
				subscriberId, productType, billSeqNo, channelSeizureDate,
				messageSwitchId, callProductType, getSessionId());
	}

	public CallListInfo retrieveUnbilledCallsList(int ban, String subscriberId,
			String productType, Date fromDate, Date toDate, boolean getAll
			) throws Throwable {
		return subscriberLifecycleManager.retrieveUnbilledCallsList(ban,
				subscriberId, productType, fromDate, toDate, getAll, getSessionId());
	}

	/**
	 * @deprecated
	 * Deprecated as of October 2016, please use cis-wls-rated-airtime-usage-inquiry-svc web service instead.
	 */
	public CallListInfo retrieveUnbilledCallsList(int ban, String subscriberId,
			String productType) throws Throwable {
		return subscriberLifecycleManager.retrieveUnbilledCallsList(ban,
				subscriberId, productType, getSessionId());
	}

	public DiscountInfo[] retrieveDiscounts(int ban, String subscriberId,
			String productType) throws Throwable {
		return subscriberLifecycleManager.retrieveDiscounts(ban, subscriberId,
				productType, getSessionId());
	}

	public CancellationPenaltyInfo retrieveCancellationPenalty(int ban,
			String subscriberId, String productType)
			throws Throwable {
		return subscriberLifecycleManager.retrieveCancellationPenalty(ban,
				subscriberId, productType, getSessionId());
	}

	public AvailablePhoneNumberInfo[] retrieveAvailablePhoneNumbers(int ban,
			String subscriberId,
			PhoneNumberReservationInfo phoneNumberReservation, int maxNumbers
			) throws Throwable {
		return subscriberLifecycleManager.retrieveAvailablePhoneNumbers(ban,
				subscriberId, phoneNumberReservation, maxNumbers, getSessionId());
	}

	public void createSubscriber(SubscriberInfo subscriberInfo,
			SubscriberContractInfo subscriberContractInfo, boolean activate,
			boolean overridePatternSearchFee, String activationFeeChargeCode,
			boolean dealerHasDeposit, boolean portedIn,
			ServicesValidation srvValidation, String portProcessType,
			int oldBanId, String oldSubscriberId)
			throws Throwable {
		subscriberLifecycleManager.createSubscriber(subscriberInfo,
				subscriberContractInfo, activate, overridePatternSearchFee,
				activationFeeChargeCode, dealerHasDeposit, portedIn,
				srvValidation, portProcessType, oldBanId, oldSubscriberId,
				getSessionId());
	}

	public void updateAddress(int ban, String subscriber, String productType,
			AddressInfo addressInfo)
			throws Throwable {
		subscriberLifecycleManager.updateAddress(ban, subscriber, productType,
				addressInfo, getSessionId());
	}

	public void changePhoneNumber(SubscriberInfo subscriberInfo,
			AvailablePhoneNumberInfo newPhoneNumber, String reasonCode
			) throws Throwable {
		this.changePhoneNumber(subscriberInfo, newPhoneNumber, reasonCode, null, null);
	}

	public void changePhoneNumber(SubscriberInfo subscriberInfo,
			AvailablePhoneNumberInfo newPhoneNumber, String reasonCode,
			String dealerCode, String salesRepCode)
			throws Throwable {
		subscriberLifecycleManager
				.changePhoneNumber(subscriberInfo, newPhoneNumber, reasonCode,
						dealerCode, salesRepCode, getSessionId());
	}

	public void moveSubscriber(SubscriberInfo subscriberInfo, int targetBan,
			Date activityDate, boolean transferOwnership,
			String activityReasonCode, String userMemoText, String dealerCode,
			String salesRepCode) throws Throwable {
		subscriberLifecycleManager.moveSubscriber(subscriberInfo, targetBan,
				activityDate, transferOwnership, activityReasonCode,
				userMemoText, dealerCode, salesRepCode, getSessionId());
	}

	public void moveSubscriber(SubscriberInfo subscriberInfo, int targetBan,
			Date activityDate, boolean transferOwnership,
			String activityReasonCode, String userMemoText)
			throws Throwable {
		subscriberLifecycleManager.moveSubscriber(subscriberInfo, targetBan,
				activityDate, transferOwnership, activityReasonCode,
				userMemoText, getSessionId());
	}

	public void releaseSubscriber(SubscriberInfo subscriberInfo
			) throws Throwable {
		subscriberLifecycleManager.releaseSubscriber(subscriberInfo, getSessionId());
	}

	public SubscriberInfo reserveLikePhoneNumber(SubscriberInfo subscriberInfo,
			PhoneNumberReservationInfo phoneNumberReservation)
			throws Throwable {
		return subscriberLifecycleManager.reserveLikePhoneNumber(
				subscriberInfo, phoneNumberReservation, getSessionId());
	}

	public SubscriberInfo reservePhoneNumber(SubscriberInfo subscriberInfo,
			PhoneNumberReservationInfo phoneNumberReservation)
			throws Throwable {
		return subscriberLifecycleManager.reservePhoneNumber(subscriberInfo,
				phoneNumberReservation, false ,getSessionId());
	}

	public void cancelAdditionalMsisdn(
			AdditionalMsiSdnFtrInfo[] additionalMsiSdnFtrInfo,
			String additionalMsisdn)
			throws Throwable {
		subscriberLifecycleManager.cancelAdditionalMsisdn(
				additionalMsiSdnFtrInfo, additionalMsisdn, getSessionId());
	}

	public void cancelPortedInSubscriber(int banNumber, String phoneNumber,
			String deactivationReason, Date activityDate, String portOutInd,
			boolean isBrandPort) throws Throwable {
		subscriberLifecycleManager.cancelPortedInSubscriber(banNumber,
				phoneNumber, deactivationReason, activityDate, portOutInd,
				isBrandPort, getSessionId());
	}

	public void changeAdditionalPhoneNumbers(SubscriberInfo subscriberInfo
			) throws Throwable {
		subscriberLifecycleManager.changeAdditionalPhoneNumbers(subscriberInfo,
				getSessionId());
	}

	public void changeAdditionalPhoneNumbersForPortIn(
			SubscriberInfo subscriberInfo)
			throws Throwable {
		subscriberLifecycleManager.changeAdditionalPhoneNumbersForPortIn(
				subscriberInfo, getSessionId());
	}

	public void changeFaxNumber(SubscriberInfo subscriber)
			throws Throwable {
		subscriberLifecycleManager.changeFaxNumber(subscriber, getSessionId());
	}

	public void changeFaxNumber(SubscriberInfo subscriber,
			AvailablePhoneNumberInfo newFaxNumber)
			throws Throwable {
		subscriberLifecycleManager.changeFaxNumber(subscriber, newFaxNumber,
				getSessionId());
	}

	/* 
	 * TODO The parameter productType is not used in this implementation, shall be removed from the interface after 
	 * WL10 upgrade
	 */
	public void changeIMSI(int ban, String productType, String subscriberId)
			throws Throwable {
		subscriberLifecycleManager.changeIMSI(ban, subscriberId, getSessionId());
	}

	/* 
	 * TODO The parameter productType is not used in this implementation, shall be removed from the interface after 
	 * WL10 upgrade
	 */
	public void changeIP(int ban, String subscriberId, String productType, String newIp,
			String newIpType, String newIpCorpCode)
			throws Throwable {
		subscriberLifecycleManager.changeIP(ban, subscriberId, newIp,
				newIpType, newIpCorpCode, getSessionId());
	}

	/* 
	 * TODO The parameter productType is not used in this implementation, shall be removed from the interface after 
	 * WL10 upgrade
	 */
	public void reserveAdditionalPhoneNumber(int ban, String productType, String subscriberId,
			AvailablePhoneNumberInfo additionalPhoneNumber)
			throws Throwable {
		subscriberLifecycleManager.reserveAdditionalPhoneNumber(ban,
				subscriberId, additionalPhoneNumber, getSessionId());
	}

	public SearchResultByMsiSdn searchSubscriberByAdditionalMsiSdn(
			String additionalMsisdn)
			throws Throwable {
		return subscriberLifecycleManager.searchSubscriberByAdditionalMsiSdn(
				additionalMsisdn, getSessionId());
	}

	public void migrateSubscriber(SubscriberInfo srcSubscriberInfo,
			SubscriberInfo newSubscriberInfo, Date activityDate,
			SubscriberContractInfo subscriberContractInfo,
			EquipmentInfo newPrimaryEquipmentInfo,
			EquipmentInfo[] newSecondaryEquipmentInfo,
			MigrationRequestInfo migrationRequestInfo)
			throws Throwable {
		subscriberLifecycleManager.migrateSubscriber(srcSubscriberInfo,
				newSubscriberInfo, activityDate, subscriberContractInfo,
				newPrimaryEquipmentInfo, newSecondaryEquipmentInfo,
				migrationRequestInfo, getSessionId());
	}

	public void setSubscriberPortIndicator(String phoneNumber)
			throws Throwable {
		subscriberLifecycleManager.setSubscriberPortIndicator(phoneNumber,
				getSessionId());
	}

	public void setSubscriberPortIndicator(String phoneNumber, Date portDate
			) throws Throwable {
		subscriberLifecycleManager.setSubscriberPortIndicator(phoneNumber,
				portDate, getSessionId());
	}

	public void snapBack(String phoneNumber)
			throws Throwable {
		subscriberLifecycleManager.snapBack(phoneNumber, getSessionId());
	}

	/* 
	 * TODO The parameter productType is not used in this implementation, shall be removed from the interface after 
	 * WL10 upgrade
	 */
	public void sendTestPage(int ban, String subscriberId, String productType)
			throws Throwable {
		subscriberLifecycleManager.sendTestPage(ban, subscriberId, getSessionId());
	}

	public void portChangeSubscriberNumber(SubscriberInfo subscriberInfo,
			AvailablePhoneNumberInfo newPhoneNumber, String reasonCode,
			String dealerCode, String salesRepCode, String portProcessType,
			int oldBanId, String oldSubscriberId)
			throws Throwable {
		subscriberLifecycleManager.portChangeSubscriberNumber(subscriberInfo,
				newPhoneNumber, reasonCode, dealerCode, salesRepCode,
				portProcessType, oldBanId, oldSubscriberId, getSessionId());
	}

	public void releasePortedInSubscriber(SubscriberInfo subscriberInfo
			) throws Throwable {
		subscriberLifecycleManager.releasePortedInSubscriber(subscriberInfo,
				getSessionId());
	}

	public void suspendPortedInSubscriber(int banNumber, String phoneNumber,
			String deactivationReason, Date activityDate, String portOutInd
			) throws Throwable {
		subscriberLifecycleManager.suspendPortedInSubscriber(banNumber,
				phoneNumber, deactivationReason, activityDate, portOutInd,
				getSessionId());
	}

	public SubscriberInfo reservePortedInPhoneNumber(
			SubscriberInfo subscriberInfo,
			PhoneNumberReservationInfo phoneNumberReservation,
			boolean reserveNumberOnly)
			throws Throwable {
		
		if ( subscriberInfo.isIDEN() ) {
			
			//This is to adapt SubscriberLifecycleManager method change:
			//in the old SubscriberManagerEJB, reservePortedInPhoneNumber handle both PCS and MiKE
			//but the SubscriberLifecycleManager divide the function into two methods:
			// reservePortedInPhoneNumber for PCS, reservePortedInPhoneNumberForIden for MiKE
			// reservePortedInPhoneNumberForIden is combination of SubscriberManagerEJB's setIDENResourcesForPortIn and reservePortedInPhoneNumber
			//Tested against old ECA EJB, it seems that during MiKE port-in change phone number flow, TMSubscriber directly invoke 
			//reservePortedInPhoneNumber without first call setIDENResourcesForPortIn. This is equivalent to pass the following parameters 
			//to the new reservePortedInPhoneNumberForIden() method.
			
			boolean reserveUfmi = false;
			boolean ptnBased = false;
			byte  ufmiReserveMethod = 0;
			int urbanId = 0;
			int fleetId = 0;
			int memberId  = 0;
			AvailablePhoneNumber availPhoneNumber = null;
			
			return subscriberLifecycleManager.reservePortedInPhoneNumberForIden(
					subscriberInfo, phoneNumberReservation, reserveNumberOnly,
					reserveUfmi, ptnBased, ufmiReserveMethod, urbanId, fleetId,
					memberId, availPhoneNumber, getSessionId());
			
		} else {
			return subscriberLifecycleManager.reservePortedInPhoneNumber(
					subscriberInfo, phoneNumberReservation, reserveNumberOnly,
					getSessionId());
		}
	}

	public SubscriberInfo reservePortedInPhoneNumberForIden(
			SubscriberInfo subscriberInfo,
			PhoneNumberReservationInfo phoneNumberReservation,
			boolean reserveNumberOnly, boolean reserveUfmi, boolean ptnBased,
			byte ufmiReserveMethod, int urbanId, int fleetId, int memberId,
			AvailablePhoneNumber availPhoneNumber)
			throws Throwable {
		return subscriberLifecycleManager.reservePortedInPhoneNumberForIden(
				subscriberInfo, phoneNumberReservation, reserveNumberOnly,
				reserveUfmi, ptnBased, ufmiReserveMethod, urbanId, fleetId,
				memberId, availPhoneNumber, getSessionId());
	}

	public void changePricePlan(SubscriberInfo subscriberInfo,
			SubscriberContractInfo subscriberContractInfo, String dealerCode,
			String salesRepCode, PricePlanValidationInfo pricePlanValidation
			) throws Throwable {
		subscriberLifecycleManager.changePricePlan(subscriberInfo,
				subscriberContractInfo, dealerCode, salesRepCode,
				pricePlanValidation, getSessionId());
	}

	public void changeServiceAgreement(SubscriberInfo subscriberInfo,
			SubscriberContractInfo subscriberContractInfo, String dealerCode,
			String salesRepCode, PricePlanValidationInfo pricePlanValidation
			) throws Throwable {
		subscriberLifecycleManager.changeServiceAgreement(subscriberInfo,
				subscriberContractInfo, dealerCode, salesRepCode,
				pricePlanValidation, getSessionId());
	}

	public void addMemberIdentity(IDENSubscriberInfo subscriberInfo,
			SubscriberContractInfo subscriberContractInfo, String dealerCode,
			String salesRepCode, int urbanId, int fleetId, String memberId,
			boolean pricePlanChange)
			throws Throwable {
		subscriberLifecycleManager.addMemberIdentity(subscriberInfo,
				subscriberContractInfo, dealerCode, salesRepCode, urbanId,
				fleetId, memberId, pricePlanChange, getSessionId());
	}

	public void changeMemberId(IDENSubscriberInfo idenSubscriberInfo,
			String newMemberId) throws Throwable {
		subscriberLifecycleManager.changeMemberId(idenSubscriberInfo,
				newMemberId, getSessionId());
	}

	public void changeMemberIdentity(IDENSubscriberInfo idenSubscriberInfo,
			int newUrbanId, int newFleetId, String newMemberId)
			throws Throwable {
		subscriberLifecycleManager.changeMemberIdentity(idenSubscriberInfo,
				newUrbanId, newFleetId, newMemberId, getSessionId());
	}

	public void changeTalkGroups(IDENSubscriberInfo idenSubscriberInfo,
			TalkGroupInfo[] addedTalkGroups, TalkGroupInfo[] removedTalkGroups
			) throws Throwable {
		subscriberLifecycleManager.changeTalkGroups(idenSubscriberInfo,
				addedTalkGroups, removedTalkGroups, getSessionId());
	}

	public int[] getAvailableMemberIDs(int urbanId, int fleetId,
			String memberIdPattern, int max)
			throws Throwable {
		return subscriberLifecycleManager.getAvailableMemberIDs(urbanId,
				fleetId, memberIdPattern, max, getSessionId());
	}

	public void removeMemberIdentity(IDENSubscriberInfo subscriberInfo,
			SubscriberContractInfo subscriberContractInfo, String dealerCode,
			String salesRepCode, boolean pricePlanChange)
			throws Throwable {
		subscriberLifecycleManager.removeMemberIdentity(subscriberInfo,
				subscriberContractInfo, dealerCode, salesRepCode,
				pricePlanChange, getSessionId());
	}

	public IDENSubscriberInfo reserveMemberId(
			IDENSubscriberInfo idenSubscriberInfo,
			FleetIdentityInfo fleetIdentityInfo, String wildCard
			) throws Throwable {
		return subscriberLifecycleManager.reserveMemberId(idenSubscriberInfo,
				fleetIdentityInfo, wildCard, getSessionId());
	}

	public IDENSubscriberInfo reserveMemberId(
			IDENSubscriberInfo idenSubscriberInfo)
			throws Throwable {
		return subscriberLifecycleManager.reserveMemberId(idenSubscriberInfo,
				getSessionId());
	}

	public String[] retrieveAvailableMemberIds(int urbanId, int fleetId,
			String memberIdPattern, int maxMemberIds)
			throws Throwable {
		return subscriberLifecycleManager.retrieveAvailableMemberIds(urbanId,
				fleetId, memberIdPattern, maxMemberIds, getSessionId());
	}

	public Collection retrieveTalkGroupsBySubscriber(int ban,
			String subscriberId) throws Throwable {
		return subscriberLifecycleManager.retrieveTalkGroupsBySubscriber(ban,
				subscriberId, getSessionId());
	}

	public void deleteFutureDatedPricePlan(int ban, String subscriberId,
			String productType) throws Throwable {
		subscriberLifecycleManager.deleteFutureDatedPricePlan(ban,
				subscriberId, productType, getSessionId());
	}

	public void resetVoiceMailPassword(int ban, String subscriberId,
			String productType) throws Throwable {
		subscriberLifecycleManager.resetVoiceMailPassword(ban, subscriberId,
				productType, getSessionId());
	}

	public CallingCircleParameters retrieveCallingCircleParameters(int banId,
			String subscriberNo,String productType, String soc, 
			String featureCode) throws Throwable {
		return subscriberLifecycleManager.retrieveCallingCircleParameters(
				banId, subscriberNo, soc, featureCode, productType, getSessionId());
	}

	public void deleteMsisdnFeature(AdditionalMsiSdnFtrInfo ftrInfo
			) throws Throwable {
		subscriberLifecycleManager.deleteMsisdnFeature(ftrInfo, getSessionId());
	}

	public void updateBirthDate(SubscriberInfo subscriberInfo)
			throws Throwable {
		subscriberLifecycleManager.updateBirthDate(subscriberInfo, getSessionId());
	}

	public void changeEquipment(SubscriberInfo subscriberInfo,
			EquipmentInfo oldPrimaryEquipmentInfo,
			EquipmentInfo newPrimaryEquipmentInfo,
			EquipmentInfo[] newSecondaryEquipmentInfo, String dealerCode,
			String salesRepCode, String requesterId, String swapType,
			SubscriberContractInfo subscriberContractInfo,
			PricePlanValidationInfo pricePlanValidation)
			throws Throwable {
		subscriberLifecycleManager.changeEquipment(subscriberInfo,
				oldPrimaryEquipmentInfo, newPrimaryEquipmentInfo,
				newSecondaryEquipmentInfo, dealerCode, salesRepCode,
				requesterId, swapType, subscriberContractInfo,
				pricePlanValidation, getSessionId());
	}

	public void updateCommitment(SubscriberInfo pSubscriberInfo,
			CommitmentInfo pCommitmentInfo, String dealerCode,
			String salesRepCode) throws Throwable {
		subscriberLifecycleManager.updateCommitment(pSubscriberInfo,
				pCommitmentInfo, dealerCode, salesRepCode, getSessionId());
	}

	public void suspendSubscriber(SubscriberInfo subscriberInfo,
			Date activityDate, String activityReasonCode, String userMemoText
			) throws Throwable {
		subscriberLifecycleManager.suspendSubscriber(subscriberInfo,
				activityDate, activityReasonCode, userMemoText, getSessionId());
	}

	public void createDeposit(SubscriberInfo pSubscriberInfo, double amount,
			String memoText) throws Throwable {
		subscriberLifecycleManager.createDeposit(pSubscriberInfo, amount,
				memoText, getSessionId());
	}

	public void updatePortRestriction(int ban, String subscriberNo,
			boolean restrictPort, String userID) throws Throwable {
		subscriberLifecycleManager.updatePortRestriction(ban, subscriberNo,				restrictPort, userID);
	}

	public SubscriberInfo updateSubscriber(SubscriberInfo subscriberInfo
			) throws Throwable {
		return subscriberLifecycleManager.updateSubscriber(subscriberInfo,
				getSessionId());
	}

	public void updateSubscriptionRole(int ban, String subscriberNo,
			String subscriptionRoleCode, String dealerCode,
			String salesRepCode, String csrId) throws Throwable {
		subscriberLifecycleManager.updateSubscriptionRole(ban, subscriberNo,
				subscriptionRoleCode, dealerCode, salesRepCode, csrId);
	}

	public void refreshSwitch(int ban, String subscriberId, String productType
			) throws Throwable {
		subscriberLifecycleManager.refreshSwitch(ban, subscriberId,
				productType, getSessionId());
	}

	public void restoreSuspendedSubscriber(SubscriberInfo subscriberInfo,
			Date activityDate, String activityReasonCode, String userMemoText,
			boolean portIn) throws Throwable {
		subscriberLifecycleManager.restoreSuspendedSubscriber(subscriberInfo,
				activityDate, activityReasonCode, userMemoText, portIn,
				getSessionId());
	}

	public Collection retrieveSubscribersByMemberIdentity(int urbanId,
			int fleetId, int memberId)
			throws Throwable {
		return subscriberLifecycleManager.retrieveSubscribersByMemberIdentity(
				urbanId, fleetId, memberId, getSessionId());
	}

}
