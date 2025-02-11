package com.telus.cmb.jws.mapper;

import java.util.Date;

import com.telus.eas.framework.info.CreditInfo;

public class BillingInquiryHelper {
	
	public static CreditInfo getCreditInfo( String accountNumber,  String subscriberId, String subscriberNumber, String reasonCode,  String memoText,
			Date effectiveDate,Double amount,  String balanceImpactCode,  Boolean recurringInd, Integer numberOfRecurring,  String productType, 
			Boolean prepaidInd, String taxOptionCode,  String approvalStatus,	Boolean notificationSuppressionInd,  String bypassAuthorization ){
		CreditInfo creditInfo = new CreditInfo();
		creditInfo.setBan(Integer.parseInt(accountNumber));
		creditInfo.setSubscriberId(subscriberId);
		creditInfo.setProductType(productType);
		creditInfo.setRecurring(recurringInd);
		creditInfo.setNumberOfRecurring(numberOfRecurring);
		creditInfo.setReasonCode(reasonCode);
		creditInfo.setText(memoText);
		creditInfo.setEffectiveDate(effectiveDate);
		creditInfo.setAmount(amount);
		creditInfo.setBalanceImpactFlag(balanceImpactCode);
		creditInfo.setPrepaid(prepaidInd);
		creditInfo.setTaxOption(taxOptionCode.trim().charAt(0));
		creditInfo.setApprovalStatus(approvalStatus);
		creditInfo.setBypassAuthorization(Boolean.parseBoolean(bypassAuthorization));
		
		return creditInfo;
	}

}
