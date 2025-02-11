package com.telus.eas.framework.info;

import java.util.Date;

import com.telus.api.account.Subscriber;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.FollowUpUpdateInfo;
import com.telus.eas.account.info.MarketingInfo;
import com.telus.eas.account.info.PaymentArrangementInfo;
import com.telus.eas.account.info.PaymentInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.account.info.PaymentNotificationInfo;
import com.telus.eas.account.info.ServiceCancellationInfo;
import com.telus.eas.contactevent.info.NotificationServiceChangeInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;

public class TransactionDataInfo extends Info {
	
	private static final long serialVersionUID = 1L;
	
	public static final String TRANS_TYPE_PREAUTHORIZED_PAYMENTMETHOD_NEW="PYMT_MTHD_NEW";
	public static final String TRANS_TYPE_PREAUTHORIZED_PAYMENTMETHOD_UPDATE="PYMT_MTHD_UPDT";
	public static final String TRANS_TYPE_PREAUTHORIZED_PAYMENTMETHOD_REMOVAL="PYMT_MTHD_RMV";
	
	public static final String TRANS_TYPE_PAYMENT_NOTIFICATION="PYMT_NOTIF";
	public static final String TRANS_TYPE_PAYMENT_ARRANGEMENT="PYMT_ARRNGMT";
	public static final String TRANS_TYPE_MAKE_PAYMENT="PYMT_MAKE";
	
	public static final String TRANS_TYPE_ADJUST_BALANCE="BAL_ADJT";
	public static final String TRANS_TYPE_ADJUST_CHARGE="CHRG_ADJT";

	public static final String TRANS_TYPE_ADJT_CHARGE_FOLLOWUP_APPROVAL="FOL_UP_APPRVL_ADJT"; 
	public static final String TRANS_TYPE_ADJT_BALANCE_FOLLOWUP_APPROVAL="FOL_UP_APPRVL_CR";
	
	public static final String TRANS_TYPE_SERVICE_CHANGE="SRVC_CHNG"; //change service agreement
	
	public static final String TRANS_TYPE_SERVICE_CANCEL="SRVC_CNCL";
	public static final String TRANS_TYPE_ACCOUNT_CANCEL="ACCT_CNCL";
	public static final String TRANS_TYPE_TRANSFER_OWNERSHIP="SRVC_TOWN";
	
	// added for purchase roaming service add notification - October 2016 release.
	public static final String TRANS_TYPE_ADD_ROAMING_SERVICE_CHANGES="SRVC_ADD_ROAM";
      
    private String transactionType;
    private Date transactionDate; 
    private AuditInfo auditInfo;
    
    private int accountNumber;
    private boolean portingOutSubscriber;
    
    
    private AccountInfo accountInfo;
    private PaymentInfo paymentInfo;
    private PaymentMethodInfo paymentMethodInfo;
    private PaymentArrangementInfo paymentArrangementInfo;
    private PaymentNotificationInfo paymentNotificationInfo;  
    
	private String productType;
    private String subscriberId;
    private String subscriberPhoneNumber;
    private String subscriptionId;
    private NotificationServiceChangeInfo notificationServiceChangeInfo;
    private ChargeInfo chargeInfo;
    private double adjustmentId;
    private double adjustmentAmount;
    private String adjustmentReasonCode;
    private Date serviceCancelEffectiveDate; 
    
    public Date getServiceCancelEffectiveDate() {
		return serviceCancelEffectiveDate;
	}

	public void setServiceCancelEffectiveDate(Date serviceCancelEffectiveDate) {
		this.serviceCancelEffectiveDate = serviceCancelEffectiveDate;
	}

	public double getAdjustmentId() {
		return adjustmentId;
	}

	public void setAdjustmentId(double adjustmentId) {
		this.adjustmentId = adjustmentId;
	}

	public double getAdjustmentAmount() {
		return adjustmentAmount;
	}

	public void setAdjustmentAmount(double adjustmentAmount) {
		this.adjustmentAmount = adjustmentAmount;
	}

	public String getAdjustmentReasonCode() {
		return adjustmentReasonCode;
	}

	public void setAdjustmentReasonCode(String adjustmentReasonCode) {
		this.adjustmentReasonCode = adjustmentReasonCode;
	}

	public ChargeInfo getChargeInfo() {
		return chargeInfo;
	}

	public void setChargeInfo(ChargeInfo chargeInfo) {
		this.chargeInfo = chargeInfo;
	}

	private boolean isSuppressed;
    
    public boolean isSuppressed() {
		return isSuppressed;
	}

	public void setSuppressed(boolean isSuppressed) {
		this.isSuppressed = isSuppressed;
	}
	
	
    public NotificationServiceChangeInfo getNotificationServiceChangeInfo() {
		return notificationServiceChangeInfo;
	}

	public void setNotificationServiceChangeInfo(
			NotificationServiceChangeInfo notificationServiceChangeInfo) {
		this.notificationServiceChangeInfo = notificationServiceChangeInfo;
	}

	private SubscriberContractInfo contractInfo;
    private SubscriberContractInfo oldContractInfo;
    
    //private SubscriberInfo subscriberInfo; no one use it at the moment, so remove this field to reduce the data we put in the JMS queue. 
    
    private CreditInfo creditInfo;
    private FollowUpUpdateInfo followUpUpdateInfo;
    
	private int townTargetAccountNumber;
	
	private ServiceCancellationInfo cancellationInfo;
	private String originalSessionId;
    
	private MarketingInfo marketingInfo;
	
	private SubscriberInfo subscriberInfo;
	
	public SubscriberInfo getSubscriberInfo() {
		return subscriberInfo;
	}
	public MarketingInfo getMarketingInfo() {
		return marketingInfo;
	}

	public void setMarketingInfo(MarketingInfo marketingInfo) {
		this.marketingInfo = marketingInfo;
	}

	public String getSubscriptionId(){
		return subscriptionId;
	}
	
	public void setSubscriptionId(String subscriptionId){
		this.subscriptionId=subscriptionId;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	
	public int getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public boolean isPortingOutSubscriber() {
		return portingOutSubscriber;
	}

	public void setPortingOutSubscriber(boolean portingOutSubscriber) {
		this.portingOutSubscriber = portingOutSubscriber;
	}

	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getSubscriberId() {
		return subscriberId;
	}
	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}
	public String getSubscriberPhoneNumber() {
		return subscriberPhoneNumber;
	}
	public void setSubscriberPhoneNumber(String subscriberPhoneNumber) {
		this.subscriberPhoneNumber = subscriberPhoneNumber;
	}
	
	public AccountInfo getAccountInfo() {
		return accountInfo;
	}
	public void setAccountInfo(AccountInfo accountInfo) {
		this.accountInfo = accountInfo;
	}
	public PaymentInfo getPaymentInfo() {
		return paymentInfo;
	}
	public void setPaymentInfo(PaymentInfo paymentInfo) {
		this.paymentInfo = paymentInfo;
	}
	public PaymentMethodInfo getPaymentMethodInfo() {
		return paymentMethodInfo;
	}
	public void setPaymentMethodInfo(PaymentMethodInfo paymentMethodInfo) {
		this.paymentMethodInfo = paymentMethodInfo;
	}
	public SubscriberContractInfo getContractInfo() {
		return contractInfo;
	}
	public void setContractInfo(SubscriberContractInfo contractInfo) {
		this.contractInfo = contractInfo;
	}
	public int getTownTargetAccountNumber() {
		return townTargetAccountNumber;
	}
	public void setTownTargetAccountNumber(int townTargetAccountNumber) {
		this.townTargetAccountNumber = townTargetAccountNumber;
	}
	public AuditInfo getAuditInfo() {
		return auditInfo;
	}
	public void setAuditInfo(AuditInfo auditInfo) {
		this.auditInfo = auditInfo;
	}
    public CreditInfo getCreditInfo() {
		return creditInfo;
	}
	public void setCreditInfo(CreditInfo creditInfo) {
		this.creditInfo = creditInfo;
	}
	public FollowUpUpdateInfo getFollowUpUpdateInfo() {
		return followUpUpdateInfo;
	}
	public void setFollowUpUpdateInfo(FollowUpUpdateInfo followUpUpdateInfo) {
		this.followUpUpdateInfo = followUpUpdateInfo;
	}
	public ServiceCancellationInfo getCancellationInfo() {
		return cancellationInfo;
	}
	public void setCancellationInfo(ServiceCancellationInfo cancellationInfo) {
		this.cancellationInfo = cancellationInfo;
	}

	public PaymentArrangementInfo getPaymentArrangementInfo() {
		return paymentArrangementInfo;
	}

	public void setPaymentArrangementInfo(
			PaymentArrangementInfo paymentArrangementInfo) {
		this.paymentArrangementInfo = paymentArrangementInfo;
	}

	public PaymentNotificationInfo getPaymentNotificationInfo() {
		return paymentNotificationInfo;
	}

	public void setPaymentNotificationInfo(PaymentNotificationInfo paymentNotificationInfo) {
		this.paymentNotificationInfo = paymentNotificationInfo;
	}
	
//	public SubscriberInfo getSubscriberInfo() {
//		return subscriberInfo;
//	}

	/**
	 * This method pull information from SubscriberInfo and populates several subscriber related properties, 
	 * currently it does not store the SubscriberInfo itself.
	 * @param subscriberInfo
	 */
	public void setSubscriberInfo(SubscriberInfo subscriberInfo) {
		this.subscriberInfo = subscriberInfo;
		setAccountNumber( subscriberInfo.getBanId() );
		setProductType( subscriberInfo.getProductType() );
		setPortingOutSubscriber(Subscriber.PORT_TYPE_PORT_OUT.equals(subscriberInfo.getPortType()));
		setSubscriberPhoneNumber( subscriberInfo.getPhoneNumber() );
		setSubscriberId( subscriberInfo.getSubscriberId() );
		setSubscriptionId( String.valueOf(subscriberInfo.getSubscriptionId()) );
	}

	public SubscriberContractInfo getOldContractInfo() {
		return oldContractInfo;
	}

	public void setOldContractInfo(SubscriberContractInfo oldContractInfo) {
		this.oldContractInfo = oldContractInfo;
	}
	
	public String getOriginalSessionId() {
		return originalSessionId;
	}

	public void setOriginalSessionId(String originalSessionId) {
		this.originalSessionId = originalSessionId;
	}
	

	public String toKeyContextString() {
		return toString();
	}

	@Override
	public String toString() {
		
		StringBuffer sb =  new StringBuffer( "TransactionDataInfo [")
			.append("txnType=").append( getTransactionType())
			.append(", ban=").append( getAccountNumber())
			.append(", subscriberId=").append(subscriberId);
				
		if ( getAuditInfo()!=null) {
			sb.append(", orgAppId=").append( getAuditInfo().getOriginatorAppId() ); 
		}
		
		if ( getAccountInfo()!=null) {
			AccountInfo accountInfo = getAccountInfo();
			sb.append(", accountType=").append( accountInfo.getAccountType()).append(accountInfo.getAccountSubType())
			.append(", brand=").append( accountInfo.getBrandId())
			.append(", segment=").append( accountInfo.getBanSegment() );
		}
		
		if (getOldContractInfo() != null) {
			SubscriberContractInfo oldContractInfo = getOldContractInfo();
			sb.append(", oldPP=").append(oldContractInfo.getPricePlanCode());
		}
		
		sb.append( ", productType=").append( getProductType() );
		sb.append("]");

		return sb.toString();
	}
}
