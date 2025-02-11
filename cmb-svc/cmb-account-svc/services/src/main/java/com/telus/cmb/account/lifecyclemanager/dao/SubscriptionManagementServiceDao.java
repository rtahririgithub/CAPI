package com.telus.cmb.account.lifecyclemanager.dao;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.logging.Sensitive;
import com.telus.eas.account.info.AutoTopUpInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface SubscriptionManagementServiceDao {
	
	public void registerTopupCreditCard(String phoneNumber, @Sensitive CreditCardInfo creditCard) throws ApplicationException;
	public void updateTopupCreditCard(String phoneNumber, @Sensitive CreditCardInfo creditCard) throws ApplicationException;
	public void updateAutoTopUp(String phoneNumber, AutoTopUpInfo autoTopUpInfo, boolean existingAutoTopUp, boolean existingThresholdRecharge) throws ApplicationException;
	public void removeTopupCreditCard(String phoneNumber) throws ApplicationException;
	public void applyTopUp(String phoneNumber, String voucherPin) throws ApplicationException;
	public String applyTopUpWithCreditCard(String phoneNumber, double amount) throws ApplicationException;
	public String applyTopUpWithDebitCard(String phoneNumber, double amount) throws ApplicationException;
	public TestPointResultInfo test();

}
