package com.telus.cmb.common.kafka;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.PaymentInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.framework.info.Info;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.eas.account.info.FollowUpUpdateInfo;

public class TransactionEventInfo extends Info {
	
	private static final long serialVersionUID = 1L;
	
	private KafkaEventType eventType;
	private Date transactionDate;
	private boolean notificationSuppressionInd = false;
	private AuditInfo auditInfo;
	private String dealerCode;
	private String salesRepCode;
	private String userMemoText;
	private String sessionId;

	//account & subscriber details
	private AccountInfo accountInfo;
	private SubscriberInfo subscriberInfo;
	
	// service agreement activity details
	private SubscriberContractInfo newContractInfo;
	private SubscriberContractInfo oldContractInfo;
	private EquipmentInfo equipmentInfo;
	
	//payment activity detail
	private PaymentMethodInfo paymentMethodInfo;
	private PaymentInfo paymentInfo;
	
	//charge and credit detail
	private CreditInfo creditInfo;
	private ChargeInfo chargeInfo;
	private FollowUpUpdateInfo FollowUpUpdateInfo;
	
	// activity detail
	private Date activityDate;
	private String activityReasonCode;

	//move activity detail
	private int targetBan;
	private boolean transferOwnership;

	//phone number activity detail
	private String oldPhoneNumber;

	//cancellation activity detail
	private List<String> phoneNumberList = new ArrayList<String>();
	private List<String> waiveReasonCodeList = new ArrayList<String>();
	private String depositReturnMethod;

	//portIn detail
	private boolean portIn;
	private String portProcessType;

	//portOut detail
	private boolean portOutActivityInd = false;
	private boolean interBrandPortOutInd = false;
	private boolean activityDueToPrimaryCancelInd = false;
	
	//credit check result changes
	private CreditCheckResultInfo creditCheckResultInfo;

	public void setSalesRepCode(String salesRepCode) {
		this.salesRepCode = salesRepCode;
	}

	
	public KafkaEventType getEventType() {
		return eventType;
	}

	public void setEventType(KafkaEventType eventType) {
		this.eventType = eventType;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public AccountInfo getAccountInfo() {
		return accountInfo;
	}

	public void setAccountInfo(AccountInfo accountInfo) {
		this.accountInfo = accountInfo;
	}

	public SubscriberInfo getSubscriberInfo() {
		return subscriberInfo;
	}

	public void setSubscriberInfo(SubscriberInfo subscriberInfo) {
		this.subscriberInfo = subscriberInfo;
	}

	public SubscriberContractInfo getNewContractInfo() {
		return newContractInfo;
	}

	public void setNewContractInfo(SubscriberContractInfo newContractInfo) {
		this.newContractInfo = newContractInfo;
	}

	public SubscriberContractInfo getOldContractInfo() {
		return oldContractInfo;
	}

	public void setOldContractInfo(SubscriberContractInfo oldContractInfo) {
		this.oldContractInfo = oldContractInfo;
	}

	public EquipmentInfo getEquipmentInfo() {
		return equipmentInfo;
	}

	public void setEquipmentInfo(EquipmentInfo equipmentInfo) {
		this.equipmentInfo = equipmentInfo;
	}

	
	public PaymentMethodInfo getPaymentMethodInfo() {
		return paymentMethodInfo;
	}

	public void setPaymentMethodInfo(PaymentMethodInfo paymentMethodInfo) {
		this.paymentMethodInfo = paymentMethodInfo;
	}
	
	public AuditInfo getAuditInfo() {
		return auditInfo;
	}

	public void setAuditInfo(AuditInfo auditInfo) {
		this.auditInfo = auditInfo;
	}

	public boolean isPortIn() {
		return portIn;
	}


	public void setPortIn(boolean portIn) {
		this.portIn = portIn;
	}
	
	public String getPortProcessType() {
		return portProcessType;
	}


	public void setPortProcessType(String portProcessType) {
		this.portProcessType = portProcessType;
	}
	
	public boolean isActivation() {
		return KafkaEventType.SUBSCRIBER_ACTIVATE.equals(eventType);
	}

	public boolean isServiceAgreementChange() {
		return KafkaEventType.SERVICE_AGREEMENT_CHANGE.equals(eventType);
	}
	
	public boolean isPaymentMethodChange() {
		return KafkaEventType.PAYMENT_METHOD_CHANGE.equals(eventType);
	}
	
	public boolean isCreateCredit() {
		return KafkaEventType.CREATE_CREDIT.equals(eventType);
	}
	
	public boolean isFollowUpApproval() {
		return KafkaEventType.FOLLOWUP_APPROVAL.equals(eventType);
	}
	
	public boolean isMakePayment() {
		return KafkaEventType.MAKE_PAYMENT.equals(eventType);
	}
	
	public boolean isSubscriberMove() {
		return KafkaEventType.MOVE.equals(eventType);
	}
	
	public boolean isSubscriberCancel() {
		return KafkaEventType.SUB_CANCEL.equals(eventType);
	}
	
	public boolean isSubscriberCancelPortOut() {
		return KafkaEventType.SUB_CANCEL_PORT_OUT.equals(eventType);
	}
	
	public boolean isAccountCancel() {
		return KafkaEventType.ACC_CANCEL.equals(eventType);
	}
	
	public boolean isAccountCancelPortOut() {
		return KafkaEventType.ACC_CANCEL_PORT_OUT.equals(eventType);
	}
	
	public boolean isPhoneNumberChange() {
		return KafkaEventType.PHONENUMBER_CHANGE_REGULAR.equals(eventType)
				|| KafkaEventType.PHONENUMBER_CHANGE_INTER_BRAND.equals(eventType)
				|| KafkaEventType.PHONENUMBER_CHANGE_INTER_MVNE.equals(eventType)
				|| KafkaEventType.PHONENUMBER_CHANGE_INTER_CARRIER.equals(eventType);
	}
	
	public boolean isCreditCheckChange() {
		return KafkaEventType.CREDIT_CHECK_CREATE.equals(eventType) ||
				KafkaEventType.CREDIT_CHECK_UPDATE.equals(eventType);
	}
	
	public String getDealerCode() {
		return dealerCode;
	}

	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}

	public String getSalesRepCode() {
		return salesRepCode;
	}
	
	public String getSessionId() {
		return sessionId;
	}


	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	
	public boolean isNotificationSuppressionInd() {
		return notificationSuppressionInd;
	}


	public void setNotificationSuppressionInd(boolean notificationSuppressionInd) {
		this.notificationSuppressionInd = notificationSuppressionInd;
	}

	public Date getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}


	public String getActivityReasonCode() {
		return activityReasonCode;
	}


	public void setActivityReasonCode(String activityReasonCode) {
		this.activityReasonCode = activityReasonCode;
	}


	public String getDepositReturnMethod() {
		return depositReturnMethod;
	}


	public void setDepositReturnMethod(String depositReturnMethod) {
		this.depositReturnMethod = depositReturnMethod;
	}


	public String getUserMemoText() {
		return userMemoText;
	}


	public void setUserMemoText(String userMemoText) {
		this.userMemoText = userMemoText;
	}

	
	public void setPortOutActivityInd(boolean portOutActivityInd) {
		this.portOutActivityInd = portOutActivityInd;
	}
	
	public boolean isPortOutActivityInd() {
		return portOutActivityInd;
	}
	
	public int getTargetBan() {
		return targetBan;
	}

	public void setTargetBan(int targetBan) {
		this.targetBan = targetBan;
	}
	
	public boolean isTransferOwnership() {
		return transferOwnership;
	}


	public void setTransferOwnership(boolean transferOwnership) {
		this.transferOwnership = transferOwnership;
	}
	
	public List<String> getPhoneNumberList() {
		return phoneNumberList;
	}


	public void setPhoneNumberList(List<String> phoneNumberList) {
		this.phoneNumberList = phoneNumberList;
	}
	
	public List<String> getWaiveReasonCodeList() {
		return waiveReasonCodeList;
	}


	public void setWaiveReasonCodeList(List<String> waiveReasonCodeList) {
		this.waiveReasonCodeList = waiveReasonCodeList;
	}
	
	public ChargeInfo getChargeInfo() {
		return chargeInfo;
	}

	public CreditInfo getCreditInfo() {
		return creditInfo;
	}

	public void setCreditInfo(CreditInfo creditInfo) {
		this.creditInfo = creditInfo;
	}
	
	public void setChargeInfo(ChargeInfo chargeInfo) {
		this.chargeInfo = chargeInfo;
	}


	public FollowUpUpdateInfo getFollowUpUpdateInfo() {
		return FollowUpUpdateInfo;
	}


	public void setFollowUpUpdateInfo(FollowUpUpdateInfo followUpUpdateInfo) {
		FollowUpUpdateInfo = followUpUpdateInfo;
	}
	public PaymentInfo getPaymentInfo() {
		return paymentInfo;
	}


	public void setPaymentInfo(PaymentInfo paymentInfo) {
		this.paymentInfo = paymentInfo;
	}
	public boolean isInterBrandPortOutInd() {
		return interBrandPortOutInd;
	}


	public void setInterBrandPortOutInd(boolean interBrandPortOutInd) {
		this.interBrandPortOutInd = interBrandPortOutInd;
	}
	
	public boolean isActivityDueToPrimaryCancelInd() {
		return activityDueToPrimaryCancelInd;
	}

	public void setActivityDueToPrimaryCancelInd(boolean activityDueToPrimaryCancelInd) {
		this.activityDueToPrimaryCancelInd = activityDueToPrimaryCancelInd;
	}
	
	public CreditCheckResultInfo getCreditCheckResultInfo() {
		return creditCheckResultInfo;
	}


	public void setCreditCheckResultInfo(CreditCheckResultInfo creditCheckResultInfo) {
		this.creditCheckResultInfo = creditCheckResultInfo;
	}
	
	public String getOldPhoneNumber() {
		return oldPhoneNumber;
	}


	public void setOldPhoneNumber(String oldPhoneNumber) {
		this.oldPhoneNumber = oldPhoneNumber;
	}
}
