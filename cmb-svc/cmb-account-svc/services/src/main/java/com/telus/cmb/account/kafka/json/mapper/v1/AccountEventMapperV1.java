package com.telus.cmb.account.kafka.json.mapper.v1;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import com.telus.api.account.CreditCheckResultDeposit;
import com.telus.cmb.common.kafka.account_v1_0.AccountEvent;
import com.telus.cmb.common.kafka.account_v1_0.CreditCheckResult;
import com.telus.cmb.common.kafka.account_v1_0.CreditDeposit;
import com.telus.cmb.common.kafka.account_v1_0.SubscriberStatusChange;
import com.telus.cmb.common.kafka.account_v1_0.Subscriber;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.FollowUpUpdateInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.cmb.common.kafka.TransactionEventInfo;
import com.telus.cmb.common.kafka.TransactionEventMapper;
import com.telus.cmb.common.mapping.AbstractSchemaMapper;
import com.telus.cmb.common.util.AttributeTranslator;

public class AccountEventMapperV1 extends AbstractSchemaMapper<AccountEvent, TransactionEventInfo> implements TransactionEventMapper{
	
	public AccountEventMapperV1() {
		super(AccountEvent.class, TransactionEventInfo.class);
	}
	
	@Override
	protected AccountEvent performSchemaMapping(TransactionEventInfo source,AccountEvent target) {
		// set the account and bill detail
		target.setAccount(AccountMapper.mapAccountData(source.getAccountInfo()));
		target.setBillDetail(AccountMapper.mapBillDetail(source.getAccountInfo().getBillCycle(),source.getAccountInfo().getBillCycleCloseDay()));

		// Set the event type and other event level parameters
		target.setEventType(String.valueOf(source.getEventType()));
		target.setNotificationSuppressionInd(source.isNotificationSuppressionInd());
		target.setAuditInfo(mapAuditInfo(source.getDealerCode(),source.getSalesRepCode(),source.getAuditInfo()));
		target.setTransactionDate(source.getTransactionDate());

		//accountCancel will use only waive reason code to cancel the all subscribers under ban  
		if(source.isAccountCancel() || source.isAccountCancelPortOut()){
			target.setActivityDetail(ActivityDetailMapper.mapActivityDetail(source));
			for (String phoneNumber : source.getPhoneNumberList()) {
				SubscriberStatusChange statusChange = new SubscriberStatusChange();
				statusChange.setPhoneNumber(phoneNumber);
				if(CollectionUtils.isNotEmpty(source.getWaiveReasonCodeList())){
					statusChange.setDepositWaiveReasonCd(source.getWaiveReasonCodeList().get(0));
				}
				target.getSubscriberStatusChangeList().add(statusChange);
			}
			target.setMemo(MemoMapper.mapMemo(source));
		}

		
		// multiple subscriber cancel will use multiple waive reason codes for cancellation
		if (source.isSubscriberCancel()||source.isSubscriberCancelPortOut()) {
			target.setActivityDetail(ActivityDetailMapper.mapActivityDetail(source));
			for (int i = 0; i < source.getPhoneNumberList().size(); i++) {
				SubscriberStatusChange statusChange = new SubscriberStatusChange();
				statusChange.setPhoneNumber(source.getPhoneNumberList().get(i));
				statusChange.setDepositWaiveReasonCd(source.getWaiveReasonCodeList().get(i));
				target.getSubscriberStatusChangeList().add(statusChange);
				target.setMemo(MemoMapper.mapMemo(source));
			}
		}

				
		// make payment changes
		if(source.isMakePayment()){
			target.setPaymentDetail(PaymentDetailMapper.getInstance().mapToSchema(source.getPaymentInfo()));
			target.setDebtSummary(DebtSummaryMapper.getInstance().mapToSchema(source.getAccountInfo().getFinancialHistory0().getDebtSummary0()));
		}
		
		// payment method changes
		if (source.isPaymentMethodChange()) {
			target.setPaymentMethod(PaymentMethodMapper.mapPaymentMethod(source.getPaymentMethodInfo()));
		}
				
		// map charge & credit changes
		if(source.isCreateCredit()){
			target.setCreditDetail(CreditDetailMapper.getInstance().mapToSchema(source.getCreditInfo()));
			target.setOriginalChargeDetail(ChargeDetailMapper.getInstance().mapToSchema(source.getChargeInfo()));
			//Only applicable subscriber level credit
			if(source.getCreditInfo().getSubscriberId()!=null){
				Subscriber subscriber = new Subscriber();
				subscriber.setSubscriberId(source.getCreditInfo().getSubscriberId());
				target.setSubscriber(subscriber);	
			}
		}
		
		// follow-up approval credit changes
		if (source.isFollowUpApproval()) {
			target.setFollowUpId(source.getFollowUpUpdateInfo().getFollowUpId());
			target.setFollowUpType(source.getFollowUpUpdateInfo().getFollowUpType());

			if (source.getFollowUpUpdateInfo().isFollowUpApprovalForChargeAdj()) {
				target.setFollowUpReason(FollowUpUpdateInfo.FOLLOW_REASON_ADJUST_BAN.substring(5));
			} else if (source.getFollowUpUpdateInfo().isFollowUpApprovalForManualCredit()) {
				target.setFollowUpReason(FollowUpUpdateInfo.FOLLOW_REASON_ADJUST_CHARGE.substring(5));
			}
			//Only applicable subscriber level credit
			if(source.getFollowUpUpdateInfo().getSubscriberId()!=null){
				Subscriber subscriber = new Subscriber();
				subscriber.setSubscriberId(source.getCreditInfo().getSubscriberId());
				target.setSubscriber(subscriber);	
			}
			target.setCreditDetail(CreditDetailMapper.getInstance().mapToSchema(source.getCreditInfo()));
		}
				
		//Credit check result change 
		if (source.isCreditCheckChange()) {
			CreditCheckResult result = new CreditCheckResult();
			CreditCheckResultInfo creditcheckResultInfo = source.getCreditCheckResultInfo();
			result.setCreditClass(creditcheckResultInfo.getCreditClass());
			result.setCreditScoreNum(creditcheckResultInfo.getCreditScore());
			result.setCreditLimitAmt(creditcheckResultInfo.getLimit());
			String decisionMessage = AttributeTranslator.emptyFromNull(StringUtils.isNotBlank(creditcheckResultInfo.getBureauMessage()) 
                    ? creditcheckResultInfo.getBureauMessage() : creditcheckResultInfo.getMessage());

			result.setCreditDecisionMessage(decisionMessage);
			CreditCheckResultDeposit[] deposits = creditcheckResultInfo.getDeposits();
			if(deposits!=null){
				for (int i = 0; i < deposits.length; i++) {
					CreditDeposit creditDeposit = new CreditDeposit();
					creditDeposit.setDepositAmount(deposits[i].getDeposit());
					creditDeposit.setProductType(deposits[i].getProductType());
					result.getCreditDepositList().add(creditDeposit);
				}
			}
			target.setCreditCheckResult(result);
		}
		
		// map other event info  
		target.setNotificationSuppressionInd(source.isNotificationSuppressionInd()); 
		target.setTransactionDate(source.getTransactionDate());
		target.setAuditInfo(mapAuditInfo(source.getDealerCode(), source.getSalesRepCode(),source.getAuditInfo()));

		return super.performSchemaMapping(source, target);
	}

	public static com.telus.cmb.common.kafka.account_v1_0.AuditInfo mapAuditInfo(String dealerCode,String salesRepCode, AuditInfo auditInfo) {
		com.telus.cmb.common.kafka.account_v1_0.AuditInfo info = new com.telus.cmb.common.kafka.account_v1_0.AuditInfo();
		info.setDealerCode(dealerCode);
		info.setSalesRepCode(salesRepCode);
		if(auditInfo!=null){
			info.setOriginatorApplicationId(auditInfo.getOriginatorAppId());
			info.setKbUserId(auditInfo.getUserId());
		}
		return info;
	}
}
