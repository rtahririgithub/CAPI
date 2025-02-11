package com.telus.cmb.account.payment.kafka;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telus.cmb.account.kafka.json.mapper.v1.AccountEventMapperV1;
import com.telus.cmb.common.kafka.KafkaEventType;
import com.telus.cmb.common.kafka.KafkaEventVersion;
import com.telus.cmb.common.kafka.TransactionEventInfo;
import com.telus.cmb.common.kafka.TransactionEventMapper;
import com.telus.cmb.common.kafka.account_v1_0.AccountEvent;
import com.telus.cmb.common.kafka.account_v1_0.ChargeDetail;
import com.telus.cmb.common.kafka.account_v1_0.CreditCheckResult;
import com.telus.cmb.common.kafka.account_v1_0.CreditDetail;
import com.telus.cmb.common.kafka.account_v1_0.DebtSummary;
import com.telus.cmb.common.kafka.account_v1_0.PaymentDetail;
import com.telus.cmb.common.kafka.account_v1_0.PaymentMethod;
import com.telus.cmb.common.kafka.account_v1_0.ActivityDetail;
import com.telus.cmb.common.kafka.account_v1_0.Subscriber;
import com.telus.cmb.common.kafka.account_v1_0.SubscriberStatusChange;
import com.telus.cmb.common.kafka.account_v1_0.Memo;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.FollowUpUpdateInfo;
import com.telus.eas.account.info.PaymentInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.transaction.info.AuditInfo;

public class AccountEventPayloadFactory {
	
	private static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd";

	public static String createAccountCancel(AccountInfo accountInfo,List<String> phoneNumbers, Date activityDate,String activityReasonCode,String depositReturnMethod, String waiveReason,
			String userMemoText, boolean portOutActivityInd, boolean brandPortOutActivityInd,AuditInfo auditInfo,Date transactionDate, boolean activityDueToPrimaryCancelInd,boolean notificationSuppressionInd) throws Exception{
		// set base transaction info
		TransactionEventInfo eventInfo = populateBaseTransactionEventInfo(accountInfo, transactionDate,auditInfo, null, null, notificationSuppressionInd);
		// set event specific info
		eventInfo.getPhoneNumberList().addAll(phoneNumbers);
		eventInfo.setActivityDate(activityDate);
		eventInfo.setActivityReasonCode(activityReasonCode);
		eventInfo.setDepositReturnMethod(depositReturnMethod);
		eventInfo.getWaiveReasonCodeList().add(waiveReason);
		eventInfo.setUserMemoText(userMemoText);
		eventInfo.setPortOutActivityInd(portOutActivityInd);
		eventInfo.setInterBrandPortOutInd(brandPortOutActivityInd);
		eventInfo.setActivityDueToPrimaryCancelInd(activityDueToPrimaryCancelInd);
		eventInfo.setEventType(portOutActivityInd ? KafkaEventType.ACC_CANCEL_PORT_OUT : KafkaEventType.ACC_CANCEL);
		return convertObjectToJson(getAccountEventMapper(KafkaEventVersion.ACCOUNT_STATUS_CHANGE.getVersion()).mapToSchema(eventInfo));
	}
	
	public static String createPaymentMethodChange(AccountInfo accountInfo, PaymentMethodInfo paymentMethodInfo,AuditInfo auditInfo,Date transactionDate,boolean notificationSuppressionInd) throws Exception {
		// set base transaction info
		TransactionEventInfo eventInfo = populateBaseTransactionEventInfo(accountInfo, transactionDate,auditInfo, null, null, notificationSuppressionInd);
		// set event specific info
		eventInfo.setPaymentMethodInfo(paymentMethodInfo);
		eventInfo.setEventType(KafkaEventType.PAYMENT_METHOD_CHANGE);
		return convertObjectToJson(getAccountEventMapper(KafkaEventVersion.PAYMENT_METHOD_CHANGE.getVersion()).mapToSchema(eventInfo));
	}
	
	public static String createMakePayment(AccountInfo accountInfo, PaymentInfo paymentInfo,AuditInfo auditInfo,Date transactionDate,boolean notificationSuppressionInd) throws Exception {
		TransactionEventInfo eventInfo = populateBaseTransactionEventInfo(accountInfo, transactionDate,auditInfo, null, null, notificationSuppressionInd);
		eventInfo.setPaymentInfo(paymentInfo);
		eventInfo.setEventType(KafkaEventType.MAKE_PAYMENT);
		return convertObjectToJson(getAccountEventMapper(KafkaEventVersion.MAKE_PAYMENT.getVersion()).mapToSchema(eventInfo));
	}
	
	public static String createCredit(AccountInfo accountInfo, CreditInfo creditInfo,AuditInfo auditInfo,Date transactionDate,boolean notificationSuppressionInd) throws Exception {
		TransactionEventInfo eventInfo = populateBaseTransactionEventInfo(accountInfo, transactionDate,auditInfo, null, null, notificationSuppressionInd);
		eventInfo.setCreditInfo(creditInfo);

		//set the event type
		eventInfo.setEventType(KafkaEventType.CREATE_CREDIT);
		return convertObjectToJson(getAccountEventMapper(KafkaEventVersion.CREATE_CREDIT.getVersion()).mapToSchema(eventInfo));
	}
	
	public static String createCreditCheckResult(AccountInfo accountInfo, CreditCheckResultInfo creditCheckResultInfo,
			AuditInfo auditInfo,Date transactionDate,boolean notificationSuppressionInd) throws Exception {
		TransactionEventInfo eventInfo = populateBaseTransactionEventInfo(accountInfo, transactionDate,auditInfo, null, null, notificationSuppressionInd);
		eventInfo.setCreditCheckResultInfo(creditCheckResultInfo);
		eventInfo.setEventType(KafkaEventType.CREDIT_CHECK_CREATE);
		return convertObjectToJson(getAccountEventMapper(KafkaEventVersion.CREDIT_CHECK_CHANGE.getVersion()).mapToSchema(eventInfo));
	}
	
	public static String updateCreditCheckResult(AccountInfo accountInfo, CreditCheckResultInfo creditCheckResultInfo,AuditInfo auditInfo,
			Date transactionDate,boolean notificationSuppressionInd) throws Exception {
		TransactionEventInfo eventInfo = populateBaseTransactionEventInfo(accountInfo, transactionDate,auditInfo, null, null, notificationSuppressionInd);
		eventInfo.setCreditCheckResultInfo(creditCheckResultInfo);
		//set the event type
		eventInfo.setEventType(KafkaEventType.CREDIT_CHECK_UPDATE);
		return convertObjectToJson(getAccountEventMapper(KafkaEventVersion.CREDIT_CHECK_CHANGE.getVersion()).mapToSchema(eventInfo));
	}
	
	public static String createCreditForChargeAdj(AccountInfo accountInfo, CreditInfo creditInfo,ChargeInfo chargeInfo,AuditInfo auditInfo,Date transactionDate,boolean notificationSuppressionInd) throws Exception {
		TransactionEventInfo eventInfo = populateBaseTransactionEventInfo(accountInfo, transactionDate,auditInfo, null, null, notificationSuppressionInd);
		eventInfo.setCreditInfo(creditInfo);
		eventInfo.setChargeInfo(chargeInfo);
		eventInfo.setEventType(KafkaEventType.CREATE_CREDIT);
		return convertObjectToJson(getAccountEventMapper(KafkaEventVersion.CREATE_CREDIT.getVersion()).mapToSchema(eventInfo));
	}
	
	public static String createFollowUpCredit(AccountInfo accountInfo, CreditInfo creditInfo,FollowUpUpdateInfo followUpUpdateInfo,AuditInfo auditInfo,Date transactionDate,boolean notificationSuppressionInd) throws Exception {
		TransactionEventInfo eventInfo = populateBaseTransactionEventInfo(accountInfo, transactionDate,auditInfo, null, null, notificationSuppressionInd);
		eventInfo.setCreditInfo(creditInfo);
		if (creditInfo.getNumberOfRecurring() > 1) {
			creditInfo.setRecurring(true);
		}
		eventInfo.setFollowUpUpdateInfo(followUpUpdateInfo);
		eventInfo.setEventType(KafkaEventType.FOLLOWUP_APPROVAL);
		return convertObjectToJson(getAccountEventMapper(KafkaEventVersion.FOLLOWUP_APPROVAL.getVersion()).mapToSchema(eventInfo));
	}
	
	public static String createMultiSubscriberCancel(AccountInfo accountInfo,List<String> phoneNumbers, List<String> waiveReasonCodeList,
			Date activityDate, String activityReasonCode,String depositReturnMethod,String userMemoText, AuditInfo auditInfo, Date transactionDate,
			boolean activityDueToPrimaryCancelInd,boolean notificationSuppressionInd) throws Exception {
		// set base transaction info
		TransactionEventInfo eventInfo = populateBaseTransactionEventInfo(accountInfo, transactionDate,auditInfo, null, null, notificationSuppressionInd);
		// set event specific info
		eventInfo.getPhoneNumberList().addAll(phoneNumbers);
		eventInfo.setActivityDate(activityDate);
		eventInfo.setActivityReasonCode(activityReasonCode);
		eventInfo.setDepositReturnMethod(depositReturnMethod);
		eventInfo.getWaiveReasonCodeList().addAll(waiveReasonCodeList);
		eventInfo.setUserMemoText(userMemoText);
		eventInfo.setEventType(KafkaEventType.SUB_CANCEL);
		//capi & kb does not support multiple subscriber cancel port out , for now set to false always.
		eventInfo.setPortOutActivityInd(false);
		eventInfo.setActivityDueToPrimaryCancelInd(activityDueToPrimaryCancelInd);

		return convertObjectToJson(getAccountEventMapper(KafkaEventVersion.MULTI_SUBSCRIBER_STATUS_CHANGE.getVersion()).mapToSchema(eventInfo));
	}
	
	private static String convertObjectToJson(Object obj) throws JsonProcessingException {
		//Set the date format
		ObjectMapper jsonMapper = new ObjectMapper();
		jsonMapper.addMixInAnnotations(AccountEvent.class, AccountEventMixIn.class);
		//jsonMapper.setSerializationInclusion(Include.NON_NULL);
		jsonMapper.setDateFormat(new SimpleDateFormat(SIMPLE_DATE_FORMAT));
		//Convert Object to Json
		return jsonMapper.writeValueAsString(obj);
	}
	
	
	
	private static TransactionEventInfo populateBaseTransactionEventInfo(AccountInfo accountInfo,Date transactionDate,AuditInfo auditInfo,String dealerCode,String salesRepCode,boolean notificationSuppressionInd){
		TransactionEventInfo eventInfo = new TransactionEventInfo();		
		eventInfo.setAccountInfo(accountInfo);
		eventInfo.setTransactionDate(transactionDate);
		eventInfo.setAuditInfo(auditInfo);
		eventInfo.setDealerCode(dealerCode);
		eventInfo.setSalesRepCode(salesRepCode);
		return eventInfo;
	}
	
	public static TransactionEventMapper getAccountEventMapper(String versionNum) {
		if ("1".equals(versionNum)) {
			return new AccountEventMapperV1();
		} else{
			return null;
		}
	}
	
	abstract class AccountEventMixIn {
		@JsonInclude(Include.NON_NULL)
		public PaymentMethod paymentMethod;
		@JsonInclude(Include.NON_NULL)
		public PaymentDetail paymentDetail;
		@JsonInclude(Include.NON_NULL)
		public DebtSummary debtSummary;	
		@JsonInclude(Include.NON_NULL)
		public CreditDetail creditDetail;
		@JsonInclude(Include.NON_NULL)
		public ChargeDetail originalChargeDetail;
		@JsonInclude(Include.NON_NULL)
		public Integer followUpId;
		@JsonInclude(Include.NON_NULL)
		public String followUpType;
		@JsonInclude(Include.NON_NULL)
		public String followUpReason;
		@JsonInclude(Include.NON_NULL)
		public Subscriber subscriber;
		@JsonInclude(Include.NON_NULL)
		public ActivityDetail activityDetail;
		@JsonInclude(Include.NON_EMPTY)
		public List<SubscriberStatusChange> subscriberStatusChangeList;
		@JsonInclude(Include.NON_NULL)
		public Memo memo;
		@JsonInclude(Include.NON_NULL)
		public CreditCheckResult creditCheckResult;
	}
	
}
