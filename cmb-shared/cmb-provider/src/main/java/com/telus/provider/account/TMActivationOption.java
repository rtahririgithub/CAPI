/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.ActivationOption;
import com.telus.api.account.ActivationOptionType;
import com.telus.api.account.CreditCheckResultDeposit;
import com.telus.api.account.Subscriber;
import com.telus.api.util.SessionUtil;
import com.telus.eas.account.info.ActivationOptionInfo;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.provider.TMProvider;
import com.telus.provider.util.ProviderCDAExceptionTranslator;

public class TMActivationOption implements ActivationOption {

	private static final String ACTIVATION_MEMO_TYPE = "ACTR";
	private static final String CLP_ACTIVATION_MEMO_TYPE = "CLMA";
	private static final String DEPOSIT_CHANGE_REASON_CODE = "01";
	private static final String CLP_DEPOSIT_CHANGE_REASON_CODE = "14";
	private static final String[] CLP_CREDIT_CLASSES = { "X", "L" };
	
	/**
	 * @link aggregation
	 */
	private final TMProvider provider;
	private final ActivationOptionInfo activationOption;
	private final Account account;
	private final Subscriber subscriber;
	private CreditCheckResultDepositInfo[] originalDeposits;
	private boolean isDifferentiated;
	private boolean isTown;
	private boolean limitChanged;
	private boolean depositChanged;
	
	private static final NumberFormat formatter = new DecimalFormat("0.00");

	public TMActivationOption(TMProvider provider, ActivationOption activationOption, Account account, Subscriber subscriber, boolean isTown) {
		this.provider = provider;
		this.activationOption = (ActivationOptionInfo) activationOption;
		this.account = account;
		this.subscriber = subscriber;
		if (activationOption != null && account.getCreditCheckResult() != null) {
			this.limitChanged = activationOption.getCreditLimit() != account.getCreditCheckResult().getLimit()
					|| !activationOption.getCreditClass().equalsIgnoreCase(account.getCreditCheckResult().getCreditClass());
			this.depositChanged = account.getCreditCheckResult().getDeposits() == null || account.getCreditCheckResult().getDeposits().length == 0
					|| activationOption.getDeposit() != account.getCreditCheckResult().getDeposit();
		}
		this.isTown = isTown;
	}

	public ActivationOptionInfo getDelegate() {
		return activationOption;
	}

	// --------------------------------------------------------------------
	// Decorative Methods
	// --------------------------------------------------------------------

	public ActivationOptionType getOptionType() {
		return activationOption.getOptionType();
	}

	public double getDeposit() {
		return activationOption.getDeposit();
	}

	public double getCreditLimit() {
		return activationOption.getCreditLimit();
	}

	public String getCreditClass() {
		return activationOption.getCreditClass();
	}

	public int getMaxContractTerm() {
		return activationOption.getMaxContractTerm();
	}
	
	public double getCLPPricePlanLimitAmount() {
		return activationOption.getCLPPricePlanLimitAmount();
	}

	public String toString() {
		return activationOption.toString();
	}

	// --------------------------------------------------------------------
	// Service Methods
	// --------------------------------------------------------------------

	public void apply() throws TelusAPIException {

		if (activationOption == null || activationOption.getOptionType() == null) {
			if (provider.getReferenceDataFacade().isCDASupportedAccountTypeSubType(String.valueOf(account.getAccountType()) + String.valueOf(account.getAccountSubType()))) {
				// In the CDA 1B world, activation option cannot be null - throw an exception
				throw new TelusAPIException("Invalid or null activation option.");
			} else {
				// Otherwise, if this isn't a CDA-supported account type or the CDA 1B dormant flag is 'ON', null values are acceptable so do nothing and return
				return;
			}			
		}
		if (ActivationOptionType.DEPOSIT.equalsIgnoreCase(activationOption.getOptionType().getName()) || ActivationOptionType.DECLINED.equalsIgnoreCase(activationOption.getOptionType().getName())) {
			updateDeposit();
			return;
		}
		if (ActivationOptionType.CREDIT_LIMIT.equalsIgnoreCase(activationOption.getOptionType().getName())
				|| ActivationOptionType.CREDIT_LIMIT_AND_DEPOSIT.equalsIgnoreCase(activationOption.getOptionType().getName())) {
			updateCLP();
			return;
		}
		if (ActivationOptionType.DEALER_DEPOSIT_CHANGE.equalsIgnoreCase(activationOption.getOptionType().getName())) {
			updateDealerDeposit();
			return;
		}
		if (!isDifferentiated && ActivationOptionType.DIFFERENTIATED_CREDIT.equalsIgnoreCase(activationOption.getOptionType().getName())) {
			updateDifferentiate();
			return;
		}
		if (ActivationOptionType.NDP.equalsIgnoreCase(activationOption.getOptionType().getName())) {
			updateNDP();
			return;
		}
		if (ActivationOptionType.DECLINED.equalsIgnoreCase(activationOption.getOptionType().getName())) {
			updateDeclined();
			return;
		}
		// If the activation option is none of the above, throw an exception
		throw new TelusAPIException("Unknown activation option type [" + activationOption.getOptionType().getName() + "].");
	}
	
	private void validateUpdate(String creditProfileChangeText, String creditResultChangeText, String depositChangeReasonCode, CreditCheckResultDepositInfo[] deposits) throws TelusAPIException {
		try {
			provider.getAccountLifecycleFacade().updateCreditWorthiness(account.getBanId(), activationOption.getOptionType().getName(), activationOption.getCreditClass(),
					activationOption.getCreditLimit(), creditProfileChangeText, limitChanged, deposits, depositChangeReasonCode, creditResultChangeText, depositChanged, false, null, null,
					SessionUtil.getSessionId(provider.getAccountLifecycleFacade()));
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t, new ProviderCDAExceptionTranslator());
		}
	}
	
	private void updateDeposit() throws TelusAPIException {
		
		StringBuffer messageText = new StringBuffer();
		messageText.append("Activation option = Deposit client activation.");
		
		validateUpdate(messageText.toString(), getCreditResultChangeText(messageText), DEPOSIT_CHANGE_REASON_CODE, updateCreditCheckDeposits(activationOption.getDeposit(), account.getCreditCheckResult().getDeposits()));
	}
	
	private void updateDeclined() throws TelusAPIException {
		
		StringBuffer messageText = new StringBuffer();
		messageText.append("Activation option = Declined client activation.");
		
		validateUpdate(messageText.toString(), getCreditResultChangeText(messageText), DEPOSIT_CHANGE_REASON_CODE, updateCreditCheckDeposits(activationOption.getDeposit(), account.getCreditCheckResult().getDeposits()));
	}
	
	private void updateNDP() throws TelusAPIException {
		
		StringBuffer messageText = new StringBuffer();
		messageText.append("Activation option = NDP client activation.");
		
		validateUpdate(messageText.toString(), getCreditResultChangeText(messageText), DEPOSIT_CHANGE_REASON_CODE, updateCreditCheckDeposits(activationOption.getDeposit(), account.getCreditCheckResult().getDeposits()));
	}
	
	private void updateDealerDeposit() throws TelusAPIException {

		if (depositChanged) {
			MemoInfo memo = new MemoInfo(account.getBanId(), ACTIVATION_MEMO_TYPE, subscriber.getSubscriberId(), subscriber.getProductType(), "Deposit change due to activation option type = [DealerDepositChange].");
			try {
				provider.getAccountLifecycleFacade().asyncCreateMemo(memo, SessionUtil.getSessionId(provider.getAccountLifecycleFacade()));
			} catch (Throwable t) {
				provider.getExceptionHandler().handleException(t, new ProviderCDAExceptionTranslator());
			}
		}
		
		StringBuffer messageText = new StringBuffer();
		messageText.append("Activation option = Dealer Deposit Change.");
		String creditProfileChangeText = messageText.toString();

		messageText = activationOption.getDeposit() > account.getCreditCheckResult().getDeposit() ? messageText.append(" Deposit increase from ") : messageText.append(" Deposit restore from ");
		messageText.append(formatter.format(account.getCreditCheckResult().getDeposit()));
		messageText.append(" to ");
		messageText.append(formatter.format(activationOption.getDeposit())).append(".");
		String creditResultChangeText = messageText.toString();

		validateUpdate(creditProfileChangeText, creditResultChangeText, DEPOSIT_CHANGE_REASON_CODE, updateCreditCheckDeposits(activationOption.getDeposit(), account.getCreditCheckResult().getDeposits()));
	}
	
	private void updateCLP() throws TelusAPIException {

		StringBuffer messageText = new StringBuffer();
		messageText = isTown ? messageText.append("Activation option = CLP client TOWN.") :	messageText.append("Activation option = CLP client activation.");
		
		validateUpdate(messageText.toString(), getCreditResultChangeText(messageText), CLP_DEPOSIT_CHANGE_REASON_CODE, updateCreditCheckDeposits(activationOption.getDeposit(), account.getCreditCheckResult().getDeposits()));

		// Create CLP memo
		StringBuffer memoText = new StringBuffer();
		if (Arrays.asList(CLP_CREDIT_CLASSES).contains(activationOption.getCreditClass())) {
			memoText.append("Credit Limit client activation:");
			memoText.append(" original deposit assessed: " + account.getCreditCheckResult().getDeposit());
			memoText.append(" original credit class assessed: " + account.getCreditCheckResult().getCreditClass());
			memoText.append(" original credit limit assessed: " + account.getCreditCheckResult().getLimit());
			memoText.append(" deposit assigned: " + activationOption.getDeposit());
			memoText.append(" credit class assigned: " + activationOption.getCreditClass());
			memoText.append(" credit limit assigned: " + activationOption.getCreditLimit()).append(".");
			MemoInfo memo = new MemoInfo(account.getBanId(), CLP_ACTIVATION_MEMO_TYPE, subscriber.getSubscriberId(), subscriber.getProductType(), memoText.toString());
			try {
				provider.getAccountLifecycleFacade().asyncCreateMemo(memo, SessionUtil.getSessionId(provider.getAccountLifecycleFacade()));
			} catch (Throwable t) {
				provider.getExceptionHandler().handleException(t, new ProviderCDAExceptionTranslator());
			}
		
//			// Create CLP letter
//			// TODO do we still need Amp'd and Clearnet?
//			String letterCategory, letterCode;
//			switch (account.getBrandId()) {
//			case Brand.BRAND_ID_AMPD:
//				letterCategory = "ACL";
//				letterCode = Subscriber.LANGUAGE_FRENCH.equalsIgnoreCase(subscriber.getLanguage()) ? "CLMF" : "CLME";
//				break;
//			case Brand.BRAND_ID_CLEARNET:
//				letterCategory = "DCL";
//				letterCode = Subscriber.LANGUAGE_FRENCH.equalsIgnoreCase(subscriber.getLanguage()) ? "DLPF" : "DLPE";
//				break;
//			case Brand.BRAND_ID_KOODO:
//				letterCategory = "KCL";
//				letterCode = Subscriber.LANGUAGE_FRENCH.equalsIgnoreCase(subscriber.getLanguage()) ? "CLMF" : "CLME";
//				break;
//			case Brand.BRAND_ID_TELUS:
//				letterCategory = "CLP";
//				letterCode = Subscriber.LANGUAGE_FRENCH.equalsIgnoreCase(subscriber.getLanguage()) ? "CLMF" : "CLME";
//				break;			
//			default:
//				return;
//			}
//			// Create the letter request
//			Letter letter = provider.getReferenceDataManager().getLetter(letterCategory, letterCode);
//			LMSLetterRequest newLetter = account.newLMSLetterRequest(letter);
//			newLetter.save();
		}
	}

	private void updateDifferentiate() throws TelusAPIException {
		
		// Transform and store the current account credit check deposits for use in the setBackOrginalDeposit method later
		originalDeposits = (CreditCheckResultDepositInfo[]) account.getCreditCheckResult().getDeposits();

		StringBuffer messageText = new StringBuffer();
		messageText.append("Activation option = Differentiated Credit.");

		validateUpdate(messageText.toString(), getCreditResultChangeText(messageText), DEPOSIT_CHANGE_REASON_CODE, updateCreditCheckDeposits(activationOption.getDeposit(), account.getCreditCheckResult().getDeposits()));
		
		// Set the isDifferentiated flag to track the change
		isDifferentiated = originalDeposits != null && depositChanged;
	}

	public void setBackOriginalDepositIfDifferentiated() throws TelusAPIException {

		if (isDifferentiated) {

			double amount = 0;
			for (CreditCheckResultDepositInfo info : originalDeposits) {
				if (subscriber.getProductType().equalsIgnoreCase(info.getProductType())) {
					amount = info.getDeposit();
				}
			}

			StringBuffer messageText = new StringBuffer();
			messageText.append("Activation option = Differentiated Credit. Set back deposit from ");
			messageText.append(formatter.format(activationOption.getDeposit()));
			messageText.append(" to ");
			messageText.append(formatter.format(amount)).append(".");

			try {
				provider.getAccountLifecycleFacade().updateCreditCheckResult(account.getBanId(), activationOption.getCreditClass(), originalDeposits, CLP_DEPOSIT_CHANGE_REASON_CODE,
						messageText.toString(), SessionUtil.getSessionId(provider.getAccountLifecycleFacade()));
			} catch (Throwable t) {
				provider.getExceptionHandler().handleException(t, new ProviderCDAExceptionTranslator());
			}
		}
	}

	private CreditCheckResultDepositInfo[] updateCreditCheckDeposits(double newDeposit, CreditCheckResultDeposit[] creditCheckDeposits) {

		CreditCheckResultDepositInfo[] updatedDeposits;
		if (creditCheckDeposits != null && creditCheckDeposits.length > 0) {
			// The updated deposits array should be the same size as the old deposits array
			updatedDeposits = new CreditCheckResultDepositInfo[creditCheckDeposits.length];
			for (int i = 0; i < creditCheckDeposits.length; i++) {
				updatedDeposits[i] = new CreditCheckResultDepositInfo();
				updatedDeposits[i].setDeposit(newDeposit);
				updatedDeposits[i].setProductType(creditCheckDeposits[i].getProductType());
			}
		} else {
			// Create a default deposits array to hold the new deposit value
			updatedDeposits = new CreditCheckResultDepositInfo[1];
			updatedDeposits[0] = new CreditCheckResultDepositInfo();
			updatedDeposits[0].setDeposit(newDeposit);
			updatedDeposits[0].setProductType(CreditCheckResultInfo.PRODUCT_TYPE_CELLULAR);
		}

		return updatedDeposits;
	}
	
	private String getCreditResultChangeText(StringBuffer messageText) {
		
		messageText.append(" Deposit changed from ");
		messageText.append(formatter.format(account.getCreditCheckResult().getDeposit()));
		messageText.append(" to ");
		messageText.append(formatter.format(activationOption.getDeposit())).append(".");
		
		return messageText.toString();
	}
	
	public boolean validate() throws TelusAPIException {
		
		if (activationOption == null || activationOption.getOptionType() == null) {
			// If this isn't a CDA-supported account type, return true
			if (!provider.getReferenceDataFacade().isCDASupportedAccountTypeSubType(String.valueOf(account.getAccountType()) + String.valueOf(account.getAccountSubType()))) {
				return true;
			}
			// In the CDA world, activation option cannot be null - return false
			return false;
		}
		// Check to see if the activation option is one of the valid types
		return (ActivationOptionType.DEPOSIT.equalsIgnoreCase(activationOption.getOptionType().getName())
				|| ActivationOptionType.CREDIT_LIMIT.equalsIgnoreCase(activationOption.getOptionType().getName())
				|| ActivationOptionType.CREDIT_LIMIT_AND_DEPOSIT.equalsIgnoreCase(activationOption.getOptionType().getName())
				|| ActivationOptionType.DEALER_DEPOSIT_CHANGE.equalsIgnoreCase(activationOption.getOptionType().getName())
				|| ActivationOptionType.DIFFERENTIATED_CREDIT.equalsIgnoreCase(activationOption.getOptionType().getName())
				|| ActivationOptionType.NDP.equalsIgnoreCase(activationOption.getOptionType().getName()) 
				|| ActivationOptionType.DECLINED.equalsIgnoreCase(activationOption.getOptionType().getName()));
	}
	
}