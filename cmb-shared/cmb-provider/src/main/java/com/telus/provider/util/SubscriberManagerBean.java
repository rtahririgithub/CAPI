package com.telus.provider.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import com.telus.api.account.AvailablePhoneNumber;
import com.telus.api.account.CallingCircleParameters;
import com.telus.api.account.ServicesValidation;
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
import com.telus.eas.subscriber.info.CallInfo;
import com.telus.eas.subscriber.info.CallListInfo;
import com.telus.eas.subscriber.info.CommitmentInfo;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.UsageProfileListsSummaryInfo;

/**
 * This class is created to ease off the effort of migrating SubscriberManagerEJB to
 * SubscriberLifecycleManager.
 * 
 * This interface is single view of both SubscriberManagerEJBRemote and
 * SubscriberLifecycleManager, it basically mimic SubscriberManagerEJBRemote
 * interface: but throw Throwable for all methods.
 * 
 */
public interface SubscriberManagerBean extends Serializable {

	void changePricePlan(SubscriberInfo subscriberInfo,
			SubscriberContractInfo subscriberContractInfo, String dealerCode,
			String salesRepCode, PricePlanValidationInfo pricePlanValidation)
			throws Throwable;

	void changeServiceAgreement(SubscriberInfo subscriberInfo,
			SubscriberContractInfo subscriberContractInfo, String dealerCode,
			String salesRepCode, PricePlanValidationInfo pricePlanValidation)
			throws Throwable;

	void changeEquipment(SubscriberInfo subscriberInfo,
			EquipmentInfo oldPrimaryEquipmentInfo,
			EquipmentInfo newPrimaryEquipmentInfo,
			EquipmentInfo[] newSecondaryEquipmentInfo, String dealerCode,
			String salesRepCode, String requesterId, String swapType,
			SubscriberContractInfo subscriberContractInfo,
			PricePlanValidationInfo pricePlanValidation) throws Throwable;

	SubscriberInfo reservePortedInPhoneNumber(SubscriberInfo subscriberInfo,
			PhoneNumberReservationInfo phoneNumberReservation,
			boolean reserveNumberOnly) throws Throwable;

	void setSubscriberPortIndicator(String phoneNumber) throws Throwable;

	void setSubscriberPortIndicator(String phoneNumber, Date portDate)
			throws Throwable;

	void activateReservedSubscriber(SubscriberInfo subscriberInfo,
			SubscriberContractInfo subscriberContractInfo,
			Date startServiceDate, String activityReasonCode,
			ServicesValidation srvValidation, String portProcessType,
			int oldBanId, String oldSubscriberId) throws Throwable;

	void releaseSubscriber(SubscriberInfo subscriberInfo) throws Throwable;

	void releasePortedInSubscriber(SubscriberInfo subscriberInfo)
			throws Throwable;

	void changePhoneNumber(SubscriberInfo subscriberInfo,
			AvailablePhoneNumberInfo newPhoneNumber, String reasonCode,
			String dealerCode, String salesRepCode) throws Throwable;

	void changePhoneNumber(SubscriberInfo subscriberInfo,
			AvailablePhoneNumberInfo newPhoneNumber, String reasonCode)
			throws Throwable;

	void portChangeSubscriberNumber(SubscriberInfo subscriberInfo,
			AvailablePhoneNumberInfo newPhoneNumber, String reasonCode,
			String dealerCode, String salesRepCode, String portProcessType,
			int oldBanId, String oldSubscriberId) throws Throwable;

	void changeAdditionalPhoneNumbers(SubscriberInfo subscriberInfo)
			throws Throwable;

	void changeAdditionalPhoneNumbersForPortIn(SubscriberInfo subscriberInfo)
			throws Throwable;

	void createDeposit(SubscriberInfo subscriberInfo, double Amount,
			String memoText) throws Throwable;

	AvailablePhoneNumberInfo[] retrieveAvailablePhoneNumbers(int ban,
			String subscriberId,
			PhoneNumberReservationInfo phoneNumberReservation, int maxNumbers)
			throws Throwable;

	CallListInfo retrieveBilledCallsList(int ban, String subscriberId,
			String productType, int billSeqNo, char callType, Date fromDate,
			Date toDate, boolean getAll) throws Throwable;

	CallListInfo retrieveUnbilledCallsList(int ban, String subscriberId,
			String productType, Date fromDate, Date toDate, boolean getAll)
			throws Throwable;

	CancellationPenaltyInfo retrieveCancellationPenalty(int ban,
			String subscriberId, String productType) throws Throwable;

	DiscountInfo[] retrieveDiscounts(int ban, String subscriberId,
			String productType) throws Throwable;

	UsageProfileListsSummaryInfo getUsageProfileListsSummary(int ban,
			String subscriberId, int billSeqNo, String productType)
			throws Throwable;

	void restoreSuspendedSubscriber(SubscriberInfo subscriberInfo,
			Date activityDate, String activityReasonCode,
			String userMemoText, boolean portIn) throws Throwable;

	void cancelPortedInSubscriber(int banNumber, String phoneNumber,
			String deactivationReason, Date activityDate, String portOutInd,
			boolean isBrandPort) throws Throwable;

	void createSubscriber(SubscriberInfo subscriberInfo,
			SubscriberContractInfo subscriberContractInfo, boolean activate,
			boolean overridePatternSearchFee, String activationFeeChargeCode,
			boolean dealerHasDeposit, boolean portedIn,
			ServicesValidation srvValidation, String portProcessType,
			int oldBanId, String oldSubscriberId) throws Throwable;

	SubscriberInfo reserveLikePhoneNumber(SubscriberInfo subscriberInfo,
			PhoneNumberReservationInfo phoneNumberReservation)
			throws Throwable;

	SubscriberInfo reservePhoneNumber(SubscriberInfo subscriberInfo,
			PhoneNumberReservationInfo phoneNumberReservation)
			throws Throwable;

	void migrateSubscriber(SubscriberInfo srcSubscriberInfo,
			SubscriberInfo newSubscriberInfo, Date activityDate,
			SubscriberContractInfo subscriberContractInfo,
			EquipmentInfo newPrimaryEquipmentInfo,
			EquipmentInfo[] newSecondaryEquipmentInfo,
			MigrationRequestInfo migrationRequestInfo) throws Throwable;

	void moveSubscriber(SubscriberInfo subscriberInfo, int targetBan,
			Date activityDate, boolean transferOwnership,
			String activityReasonCode, String userMemoText) throws Throwable;

	void moveSubscriber(SubscriberInfo subscriberInfo, int targetBan,
			Date activityDate, boolean transferOwnership,
			String activityReasonCode, String userMemoText,
			String dealerCode, String salesRepCode) throws Throwable;

	void refreshSwitch(int ban, String subscriberId, String productType)
			throws Throwable;

	void deleteFutureDatedPricePlan(int ban, String subscriberId,
			String productType) throws Throwable;

	/**
	 * @param ban
	 * @param productType
	 *            - this parameter is only used in SubscriberManagerEJB not used
	 *            in SubscriberLifecycleManager, TODO shall be removed after WL10
	 *            upgrade.
	 * @param subscriberId
	 * @param additionalPhoneNumber
	 * @throws Throwable
	 */
	void reserveAdditionalPhoneNumber(int ban, String productType,
			String subscriberId,
			AvailablePhoneNumberInfo additionalPhoneNumber) throws Throwable;

	void resetVoiceMailPassword(int ban, String subscriberId,
			String productType) throws Throwable;

	void suspendSubscriber(SubscriberInfo subscriberInfo, Date activityDate,
			String activityReasonCode, String userMemoText) throws Throwable;

	void updateAddress(int ban, String subscriber, String productType,
			AddressInfo addressInfo) throws Throwable;

	void updatePortRestriction(int ban, String subscriberNo,
			boolean restrictPort, String userID) throws Throwable;

	void updateSubscriptionRole(int ban, String subscriberNo,
			String subscriptionRoleCode, String dealerCode,
			String salesRepCode, String csrId) throws Throwable;

	SubscriberInfo updateSubscriber(SubscriberInfo subscriberInfo)
			throws Throwable;

	CallInfo retrieveCallDetails(int ban, String subscriberId,
			String productType, int billSeqNo, Date channelSeizureDate,
			String messageSwitchId, String callProductType) throws Throwable;

	void adjustCall(int ban, String subscriberId, String productType,
			int billSeqNo, Date channelSeizureDate, String messageSwitchId,
			double adjustmentAmount, String adjustmentReasonCode,
			String memoText, String usageProductType) throws Throwable;

	CallingCircleParameters retrieveCallingCircleParameters(int banId,
			String subscriberNo, String productType, String soc,
			String featureCode) throws Throwable;

	/**
	 * @param ban
	 * @param subscriberId
	 * @param productType  - this parameter is only used in SubscriberManagerEJB not used
	 *            in SubscriberLifecycleManager, TODO shall be removed after WL10
	 *            upgrade.
	 * @throws Throwable
	 */
	void sendTestPage(int ban, String subscriberId, String productType)
			throws Throwable;

	int[] getAvailableMemberIDs(int urbanId, int fleetId,
			String memberIdPattern, int max) throws Throwable;

	void updateCommitment(SubscriberInfo subscriberInfo, CommitmentInfo commitmentInfo,
			String dealerCode, String salesRepCode) throws Throwable;

	void changeTalkGroups(IDENSubscriberInfo iDENSubscriberInfo, TalkGroupInfo[] addedTalkGroups,
			TalkGroupInfo[] removedTalkGroups) throws Throwable;

	void addMemberIdentity(IDENSubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo,
			String dealerCode, String salesRepCode, int urbanId, int fleetId, String memberId,
			boolean pricePlanChange) throws Throwable;

	void changeFaxNumber(SubscriberInfo subscriber, AvailablePhoneNumberInfo newFaxNumber)
			throws Throwable;

	void changeFaxNumber(SubscriberInfo subscriber)
			throws Throwable;

	/**
	 * @param banId
	 * @param productType
	 *            - this parameter is only used in SubscriberManagerEJB not used
	 *            in SubscriberLifecycleManager, TODO shall be removed after WL10
	 *            upgrade.
	 * @param subscriberId
	 * @throws Throwable
	 */
	void changeIMSI(int banId, String productType, String subscriberId)
			throws Throwable;

	/**
	 * @param banId
	 * @param subscriberId
	 * @param productType
	 *            - this parameter is only used in SubscriberManagerEJB not used
	 *            in SubscriberLifecycleManager, TODO shall be removed after WL10
	 *            upgrade.
	 * @param newIp
	 * @param newIpType
	 * @param newIpCorpCode
	 * @throws Throwable
	 */
	void changeIP(int banId, String subscriberId, String productType,
			String newIp, String newIpType, String newIpCorpCode) throws Throwable;

	void changeMemberIdentity(IDENSubscriberInfo iDENSubscriberInfo, int newUrbanId,
			int newFleetId, String newMemberId) throws Throwable;

	void changeMemberId(IDENSubscriberInfo iDENSubscriberInfo, String newMemberId)
			throws Throwable;

	IDENSubscriberInfo reserveMemberId(IDENSubscriberInfo iDENSubscriberInfo, FleetIdentityInfo fleetIdentityInfo,
			String wildCard) throws Throwable;

	IDENSubscriberInfo reserveMemberId(IDENSubscriberInfo iDENSubscriberInfo)
			throws Throwable;

	void removeMemberIdentity(IDENSubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo,
			String dealerCode, String salesRepCode, boolean pricePlanChange) throws Throwable;
	
	public SubscriberInfo reservePortedInPhoneNumberForIden(
			SubscriberInfo subscriberInfo,
			PhoneNumberReservationInfo phoneNumberReservation,
			boolean reserveNumberOnly, boolean reserveUfmi, boolean ptnBased,
			byte ufmiReserveMethod, int urbanId, int fleetId, int memberId,
			AvailablePhoneNumber availPhoneNumber)
			throws Throwable ;

	public String[] retrieveAvailableMemberIds(int urbanId, int fleetId,
			String memberIdPattern, int maxMemberIds) throws Throwable;
	
	public Collection retrieveSubscribersByMemberIdentity(int urbanId,
			int fleetId, int memberId) throws Throwable;
}
