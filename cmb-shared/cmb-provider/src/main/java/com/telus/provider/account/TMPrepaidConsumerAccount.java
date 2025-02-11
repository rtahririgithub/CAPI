/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import java.util.Date;
import java.util.List;

import com.telus.api.LimitExceededException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.ActivationTopUpPaymentArrangement;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.AutoTopUp;
import com.telus.api.account.Credit;
import com.telus.api.account.CreditCard;
import com.telus.api.account.InvalidAirtimeRateException;
import com.telus.api.account.InvalidCreditCardException;
import com.telus.api.account.InvalidEquipmentException;
import com.telus.api.account.PreRegisteredPrepaidCreditCard;
import com.telus.api.account.PrepaidCallHistory;
import com.telus.api.account.PrepaidConsumerAccount;
import com.telus.api.account.PrepaidEventHistory;
import com.telus.api.account.TaxSummary;
import com.telus.api.equipment.AirtimeCard;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.PrepaidAdjustmentReason;
import com.telus.api.reference.PrepaidEventType;
import com.telus.api.reference.PrepaidRateProfile;
import com.telus.api.util.SessionUtil;
import com.telus.api.util.TelusExceptionTranslator;
import com.telus.eas.account.info.AutoTopUpInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.account.info.TaxSummaryInfo;
import com.telus.eas.subscriber.info.PrepaidCallHistoryInfo;
import com.telus.eas.subscriber.info.PrepaidEventHistoryInfo;
import com.telus.eas.subscriber.info.PrepaidSubscriberInfo;
import com.telus.eas.utility.info.PrepaidEventTypeInfo;
import com.telus.provider.TMProvider;
import com.telus.provider.equipment.TMCard;
import com.telus.provider.util.AppConfiguration;
import com.telus.provider.util.Logger;
import com.telus.provider.util.ProviderCreditCardExceptionTranslator;

public class TMPrepaidConsumerAccount extends TMBasePrepaidAccount implements PrepaidConsumerAccount {

	private static final long serialVersionUID = 1L;

	private final PrepaidConsumerAccountInfo delegate;
	private final TMCreditCard topUpCreditCard;
	private final TMCreditCard activationCreditCard;
	private TMAutoTopUp autoTopUp;
	private TMActivationTopUpPaymentArrangement actTopUpPaymentArrangement;

	/*
	 * --Commented the Prepaid 5.1 rel changes private static HashMap
	 * prepaidLocalRates = new HashMap(); private static HashMap
	 * prepaidLongDistanceRates = new HashMap();
	 * 
	 * static { getPrepaidRates(); }
	 */
	public TMPrepaidConsumerAccount(TMProvider provider, PrepaidConsumerAccountInfo delegate, Equipment equipment) {
		super(provider, delegate, equipment);
		this.delegate = delegate;
		autoTopUp = new TMAutoTopUp(provider, delegate.getAutoTopUp0(), delegate.getExistingAutoTopUp());
		topUpCreditCard = new TMCreditCard(provider, delegate.getTopUpCreditCard0(), delegate);
		activationCreditCard = new TMCreditCard(provider, delegate.getActivationCreditCard0(), delegate);
		actTopUpPaymentArrangement = new TMActivationTopUpPaymentArrangement(provider, delegate.getActivationTopUpPaymentArrangement0(), this);
	}

	public TMPrepaidConsumerAccount(TMProvider provider, PrepaidConsumerAccountInfo delegate) {
		super(provider, delegate);
		this.delegate = delegate;
		autoTopUp = new TMAutoTopUp(provider, delegate.getAutoTopUp0(), delegate.getExistingAutoTopUp());
		topUpCreditCard = new TMCreditCard(provider, delegate.getTopUpCreditCard0(), delegate);
		activationCreditCard = new TMCreditCard(provider, delegate.getActivationCreditCard0(), delegate);
		actTopUpPaymentArrangement = new TMActivationTopUpPaymentArrangement(provider, delegate.getActivationTopUpPaymentArrangement0(), this);
	}

	// --------------------------------------------------------------------
	// Decorative Methods
	// --------------------------------------------------------------------
	public double getBalance() {
		return delegate.getBalance();
	}

	public Date getBalanceExpiryDate() {
		return delegate.getBalanceExpiryDate();
	}

	public Date getMinimumBalanceDate() {
		return delegate.getMinimumBalanceDate();
	}

	public double getAirtimeRate() {
		return delegate.getAirtimeRate();
	}

	public double getLongDistanceRate() {
		return delegate.getLongDistanceRate();
	}

	public int getBillingType() {
		return delegate.getBillingType();
	}
	
	public double getSwipedAmount() {
		return delegate.getSwipedAmount();
	}

	public Date getInTrustBalanceExpiryDate() {
		return delegate.getInTrustBalanceExpiryDate();
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	public String toString() {
		return delegate.toString();
	}

	// --------------------------------------------------------------------
	// Service Methods
	// --------------------------------------------------------------------
	/**
	 * @deprecated for Prepaid Gemini
	 */
	public CreditCard getTopUpCreditCard() {
		return topUpCreditCard;
	}

	public TMCreditCard getTopUpCreditCard0() {
		return topUpCreditCard;
	}

	public CreditCard getActivationCreditCard() {
		return activationCreditCard;
	}

	public TMCreditCard getActivationCreditCard0() {
		return activationCreditCard;
	}

	public String getActivationCreditCardAuthorizationNumber() {
		return activationCreditCard.getAuthorizationCode();
	}

	public void refresh() throws TelusAPIException {
		int activationType = delegate.getActivationType();
		String activationCode = delegate.getActivationCode();

		super.refresh();
		
		delegate.setActivationType(activationType);
		delegate.setActivationCode(activationCode);
		
		//Copy the autoTopUp from the delegate after refresh
		if (autoTopUp != null && autoTopUp.getDelegate() != null && delegate.getAutoTopUp0() != null)
			autoTopUp.getDelegate().copyFrom(delegate.getAutoTopUp0());
	}
	/**
	 * @deprecated for Prepaid Gemini
	 */
	public void saveTopUpCreditCard(CreditCard creditCard) throws TelusAPIException {

		if (creditCard == null) {
			return;
		}

		// do this for existing account only
		if (getBanId() != 0) {
			try {
				PaymentMethodInfo info = new PaymentMethodInfo();

				info.setPaymentMethod(PaymentMethodInfo.PAYMENT_METHOD_REGULAR);
				info.setCreditCard0(((TMCreditCard) creditCard).getDelegate());
				info.setStatus(PaymentMethodInfo.DIRECT_DEBIT_STATUS_HOLD);
				info.setStatusReason(PaymentMethodInfo.DIRECT_DEBIT_REASON_CHANGE_BILL_DATA);

				provider.getAccountLifecycleFacade().updatePaymentMethod(getBanId(), this.getDelegate0(), 
							 info, getTransientNotificationSuppressionInd(), null, SessionUtil.getSessionId(provider.getAccountLifecycleFacade()));

				//PCI changes:
				//compare the two card's number / expiryYear / expiryMonth
				boolean updatePrepaid = ! topUpCreditCard.getDelegate().isSame( ((TMCreditCard) creditCard).getDelegate());
				boolean registerPrepaidCreditcard = false;
				//If the existing topUpCreditCard token is empty, register the new creditCard
				if (topUpCreditCard.getDelegate().getToken() == null ||
						topUpCreditCard.getDelegate().getToken().trim().length()==0)
					registerPrepaidCreditcard = true;

				if (updatePrepaid ) {

					/*
					 * On April 2, 2007, OOM meeting: Current OOM business work
					 * flow: create prepaid account -> call
					 * saveTopUpCreditCard() before subscriber is created
					 * Therefore, we have to check getSubscriber0() != null here
					 * for OOM to avoid NullPointerException
					 */
					if (getSubscriber0() != null && getSubscriber0().getEquipment() != null) {
						Logger.debug("== PrepaidConsumer :: " + getSubscriber0().getPhoneNumber() + 
								" :: registerPrepaidCreditcard :: " + registerPrepaidCreditcard + 
								" :: (new) CreditCard :: " + creditCard.getTrailingDisplayDigits() + 
								" :: " + creditCard.getExpiryYear() + "/"+creditCard.getExpiryMonth());

						provider.getAccountLifecycleManager().updateTopupCreditCard(String.valueOf(getBanId()), getSubscriber0().getPhoneNumber(),
								//getSubscriber0().getEquipment().getSerialNumber(),//Serial number is not needed for April/2014 release Surepay Retirement 
								((TMCreditCard) creditCard).getDelegate(), //PCI change: pass in CreditCardInfo
								provider.getUser(), false, registerPrepaidCreditcard);
					} else if (getSubscriber0() == null) {
						Logger.debug("== PrepaidConsumer :: getSubscriber0() == null");
					} else if (getSubscriber0().getEquipment() == null) {
						Logger.debug("== PrepaidConsumer :: getSubscriber0().getEquipment() == null");
					}

				}

				// If successfull - update credit card
				topUpCreditCard.copyFrom(creditCard);

			} catch (Throwable e) {
				provider.getExceptionHandler().handleException(e);
			}
		}
	}

	protected void validateCreditCards(String businessRole) throws TelusAPIException, InvalidCreditCardException {
		TMCreditCard card1 = topUpCreditCard;
		if (card1.getNeedsValidation()) {
			card1.copyCreditInformation(this);
			card1.validate("validate top up", businessRole);
		}
	}

	public void removeTopUpCreditCard() throws TelusAPIException {
		// do this for existing account only that has credit card information
		if (getBanId() != 0 && getTopUpCreditCard0() != null && getTopUpCreditCard0().hasToken() ) {
			try {
				PaymentMethodInfo info = new PaymentMethodInfo();
				info.setPaymentMethod(PaymentMethodInfo.PAYMENT_METHOD_REGULAR);
				info.setCreditCard0(getTopUpCreditCard0().getDelegate());
				info.setStatus(PaymentMethodInfo.DIRECT_DEBIT_STATUS_CANCELED);
				info.setStatusReason(PaymentMethodInfo.DIRECT_DEBIT_REASON_CHANGE_TO_REGULAR);

				provider.getAccountLifecycleFacade().updatePaymentMethod(getBanId(), this.getDelegate0(), info, getTransientNotificationSuppressionInd(), null, SessionUtil.getSessionId(provider.getAccountLifecycleFacade()));

				// remove credit card information in Prepaid DB
				if (getSubscriber0() != null && getSubscriber0().getEquipment() != null) {
					String creditCardDateInPrepaidFormat = new java.text.DecimalFormat("00").format(getTopUpCreditCard0().getExpiryMonth())
							+ String.valueOf(getTopUpCreditCard0().getExpiryYear()).substring(2);
					provider.getAccountLifecycleManager().removeTopupCreditCard(getSubscriber0().getPhoneNumber());
					Logger.debug("== PrepaidConsumer :: " + getSubscriber0().getPhoneNumber() + " :: (remove) CreditCard :: "
							+ getTopUpCreditCard0().getTrailingDisplayDigits() + " :: " + creditCardDateInPrepaidFormat);
				} else if (getSubscriber0() == null) {
					Logger.debug("== PrepaidConsumer :: getSubscriber0() == null");
				} else if (getSubscriber0().getEquipment() == null) {
					Logger.debug("== PrepaidConsumer :: getSubscriber0().getEquipment() == null");
				}

				topUpCreditCard.clear();

			} catch (Throwable e) {
				provider.getExceptionHandler().handleException(e);
			}
		}

		return;
	}

	public void save() throws TelusAPIException, InvalidCreditCardException {

		boolean existingAccount = (getBanId() != 0);

		super.save();

		// ----------------------------------------
		// Ensure this is only called on create.
		// ----------------------------------------
		if (!existingAccount && !delegate.isValidForMigration()) {
			try {
				//PCI change
				AuditHeader auditHeader = null;
				if ( activationCreditCard!=null && activationCreditCard.hasToken() ) {
					auditHeader=provider.appendToAuditHeader(activationCreditCard.getCreditCardTransactionInfo().getAuditHeader());
				}
				String creditCardReferenceNumber = provider.getAccountLifecycleManager().performPostAccountCreationPrepaidTasks(getBanId(), delegate, auditHeader,
					 SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
				delegate.getActivationCreditCard().setAuthorizationCode(creditCardReferenceNumber);
			} catch (Throwable t) {
				delegate.setBanId(0);
				TelusExceptionTranslator telusExceptionTranslator= new ProviderCreditCardExceptionTranslator(delegate.getActivationCreditCard());
				provider.getExceptionHandler().handleException(t,telusExceptionTranslator);
			}
		}

	}

	public PrepaidEventHistory[] getPrepaidEventHistory(Date startdate, Date endDate) throws TelusAPIException {
		try {
			List tempList = provider.getSubscriberLifecycleHelper().retrievePrepaidEventHistory(
					getSubscriber0().getPhoneNumber(), startdate, endDate);
			return provider.getAccountManager0().decorate((PrepaidEventHistoryInfo[]) tempList.toArray(new PrepaidEventHistoryInfo[tempList.size()]));
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public PrepaidEventHistory[] getPrepaidEventHistory(Date startdate, Date endDate, PrepaidEventType[] eventTypes) throws TelusAPIException {
		try {
			PrepaidEventTypeInfo[] info = new PrepaidEventTypeInfo[eventTypes.length];
			System.arraycopy(eventTypes, 0, info, 0, eventTypes.length);
			
			List tempList = provider.getSubscriberLifecycleHelper().retrievePrepaidEventHistory(
					getSubscriber0().getPhoneNumber(), startdate, endDate, info);
			return provider.getAccountManager0().decorate(	(PrepaidEventHistoryInfo[]) tempList.toArray(new PrepaidEventHistoryInfo[tempList.size()])	);
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public PrepaidCallHistory[] getPrepaidCallHistory(Date startdate, Date endDate) throws TelusAPIException {
		try {
			List tempList = provider.getSubscriberLifecycleHelper().retrievePrepaidCallHistory(
					getSubscriber0().getPhoneNumber(), startdate, endDate);
			return provider.getAccountManager0().convertRateIdToRate((PrepaidCallHistoryInfo[]) tempList.toArray(new PrepaidCallHistoryInfo[tempList.size()]));
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public AutoTopUp getAutoTopUp() throws TelusAPIException {
		/*
		 * - changed by Vladimir for caching of TopUp info. try { AutoTopUpInfo
		 * info =provider.getAccountHelperEJB().
		 * retrieveAutoTopUpInfoForPayAndTalkSubscriber(getBanId(),
		 * getSubscriber0().getPhoneNumber()); boolean existingAutoTopUp = (info
		 * != null);
		 * 
		 * if (info == null) { info = new AutoTopUpInfo();
		 * info.setBan(getBanId());
		 * info.setPhoneNumber(getSubscriber0().getPhoneNumber()); }
		 * 
		 * return provider.getAccountManager0().decorate(info,
		 * existingAutoTopUp); } catch (Throwable e) { throw new
		 * TelusAPIException(e); }
		 */

		return getAutoTopUp(false);
	}

	public AutoTopUp getAutoTopUp(boolean refresh) throws TelusAPIException {
		try {
			
			if (autoTopUp == null || autoTopUp.getDelegate() == null || refresh) {
				
				AutoTopUpInfo autoTopUpInfo = provider.getAccountInformationHelper().retrieveAutoTopUpInfoForPayAndTalkSubscriber(getBanId(),
								getSubscriber0().getPhoneNumber());
				boolean existingAutoTopUp = (autoTopUpInfo != null);

				if (!existingAutoTopUp) {
					autoTopUpInfo = new AutoTopUpInfo();
					autoTopUpInfo.setBan(getBanId());

					TMSubscriber subscriber = getSubscriber0();
					String phoneNumber = subscriber != null ? subscriber.getPhoneNumber() : null;
					autoTopUpInfo.setPhoneNumber(phoneNumber);
				}

				delegate.setAutoTopUp(autoTopUpInfo);
				delegate.setExistingAutoTopUp(existingAutoTopUp);

				autoTopUp = new TMAutoTopUp(provider, autoTopUpInfo, existingAutoTopUp);
			}

			
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
		return autoTopUp;
	}

	/**
	 * For credit card top-up
	 */
	public String applyTopUp(double amount) throws InvalidCreditCardException, LimitExceededException, TelusAPIException {
		return applyTopUp(amount, "");
	}

	/**
	 * For debit card top-up.
	 * 
	 * @param amount
	 * @param orderNumber
	 *            - equal to applyTopUp(double) if this is null/emptystring
	 */
	public String applyTopUp(double amount, String orderNumber) throws LimitExceededException, TelusAPIException {
		char cardType = 'D';
		String txId = "";
		
		if (orderNumber == null || orderNumber.equals("")) {
			cardType = 'C';
		}

		try {
			txId = provider.getAccountLifecycleManager().applyTopUpForPayAndTalkSubscriber(getBanId(), getSubscriber0().getPhoneNumber(),
						amount, orderNumber, provider.getUser(), provider.getApplication());
			provider.getInteractionManager0().prepaidAccountTopUp(this, new Double(amount), cardType, 'O');
			
		} catch (Throwable t) {
			if (TMProvider.messageContains(t, "PR_SUB_2009")) {
				throw new LimitExceededException(t);
			} else {
				TelusExceptionTranslator telusExceptionTranslator= new ProviderCreditCardExceptionTranslator(getTopUpCreditCard());
				provider.getExceptionHandler().handleException(t,telusExceptionTranslator);
			}
		}
		return txId;
	}

	public String applyTopUp(AirtimeCard card) throws InvalidEquipmentException, LimitExceededException, TelusAPIException {
		String txId = "";
		try {
			//After Prepaid Surepay Retirement (April/2014), txId will be always an empty string. 
			//SubscriptionManagementService.rechargeBalanceByVoucher does not return txId anymore.
			txId = provider.getAccountLifecycleManager().applyTopUpForPayAndTalkSubscriber(getBanId(), getSubscriber0().getPhoneNumber(),
						((TMCard) card).getDelegate(), provider.getUser(), provider.getApplication());
			provider.getInteractionManager0().prepaidAccountTopUp(this, new Double(card.getAmount()), 'A', 'O');
		} catch (Throwable e) {
			if (TMProvider.messageContains(e, "PR_SUB_2009")) {
				throw new LimitExceededException(e);
			} else if (TMProvider.messageContains(e, "PR_CARD_1013")) {
				throw new InvalidEquipmentException(e);
			} else {
				provider.getExceptionHandler().handleException(e);
			}
		}
		
		return txId;
	}

	public void applyAdjustment(PrepaidAdjustmentReason adjustment, double amount, String transactionId, PrepaidAdjustmentReason waive)
			throws TelusAPIException {
		applyAdjustment(adjustment, amount, transactionId, waive, Credit.TAX_OPTION_NO_TAX, null);
	}

	public void applyAdjustment(PrepaidAdjustmentReason adjustment, double amount, String transactionId, PrepaidAdjustmentReason waive,
			boolean taxable, String memoText) throws TelusAPIException {
		if (taxable) {
			applyAdjustment(adjustment, amount, transactionId, waive, Credit.TAX_OPTION_ALL_TAXES, memoText);
		} else {
			applyAdjustment(adjustment, amount, transactionId, waive, Credit.TAX_OPTION_NO_TAX, memoText);
		}
	}

	public void applyAdjustment(PrepaidAdjustmentReason adjustment, double amount, String transactionId, PrepaidAdjustmentReason waive,
			char taxOption, String memoText) throws TelusAPIException {

		if (waive != null) {
			try {
				provider.getInteractionManager0().subscriberNewCharge(getSubscriber0(), adjustment.getCode(), waive.getCode());
			} catch (Throwable e) {
				// Ignore error - this is for reporting reasons only.
			}
		} else {
			try {
				// Calculate taxes and create memo.
				if (taxOption != Credit.TAX_OPTION_NO_TAX) {
					TaxSummary taxSummary = provider.getAccountManager().calculateTax(amount, getSubscriber0().getMarketProvince(),
							delegate.isGSTExempt(), delegate.isPSTExempt(), delegate.isHSTExempt());
					provider.getAccountLifecycleManager().adjustBalanceForPayAndTalkSubscriber(getBanId(), getSubscriber0().getPhoneNumber(),
							provider.getUser(), amount, adjustment.getCode(), transactionId, (TaxSummaryInfo) taxSummary, taxOption);
				} else {
					provider.getAccountLifecycleManager().adjustBalanceForPayAndTalkSubscriber(getBanId(), getSubscriber0().getPhoneNumber(),
							provider.getUser(), amount, adjustment.getCode(), transactionId);
				}
				// Create the memo if it exists.
				if (memoText != null && !memoText.equalsIgnoreCase("")) {
					createMemo("PRCR", memoText);
				}
				refresh();
			} catch (Throwable e) {
				provider.getExceptionHandler().handleException(e);
			}
		}
	}

	public void changeBalanceExpiryDate(Date expiryDate) throws TelusAPIException {
		try {
				PrepaidSubscriberInfo prepaidSubscriberInfo = new PrepaidSubscriberInfo();
				prepaidSubscriberInfo.setBan(getSubscriber0().getBanId());
				prepaidSubscriberInfo.setPhoneNumber(getSubscriber0().getPhoneNumber());
				prepaidSubscriberInfo.setExpiryDate(expiryDate);
				provider.getSubscriberLifecycleManager().updatePrepaidSubscriber(prepaidSubscriberInfo);
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
	}

	/**
	 * @deprecated use changeAirtimeRate(int rateId)
	 */
	public void changeAirtimeRate(double rate) throws InvalidAirtimeRateException, TelusAPIException {
		long rateId = 0;

		try {
			PrepaidRateProfile[] prepaidRateProfiles = provider.getReferenceDataManager().getPrepaidRatesbyAppId(
					PrepaidRateProfile.APPLICATION_CODE_FOR_PREPAID_RATE_SD);

			for (int k = 0; k < prepaidRateProfiles.length; k++) {
				if (prepaidRateProfiles[k].getRate() == rate) {
					rateId = prepaidRateProfiles[k].getRateId();
					break;
				}
			}

			if (rateId == 0)
				throw new InvalidAirtimeRateException(rate);
				PrepaidSubscriberInfo prepaidSubscriberInfo = new PrepaidSubscriberInfo();
				prepaidSubscriberInfo.setBan(getSubscriber0().getBanId());
				prepaidSubscriberInfo.setPhoneNumber(getSubscriber0().getPhoneNumber());
				prepaidSubscriberInfo.setRateId(rateId);
				provider.getSubscriberLifecycleManager().updatePrepaidSubscriber(prepaidSubscriberInfo);
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
	}

	public ActivationTopUpPaymentArrangement getActivationTopUpPaymentArrangement() {
		return actTopUpPaymentArrangement;
	}

	public double getOutstandingCharge() {
		return delegate.getOutstandingCharge();
	}

	public double getMaximumBalanceCap() {
		return delegate.getMaximumBalanceCap();
	}

	/**
	 * @deprecated Use getPrepaidActivationCredit()
	 */
	public double getPrepaidActivationCharge() {
		return getPrepaidActivationCredit();
	}

	public double getPrepaidActivationCredit() {
		try {
			return provider.getAccountInformationHelper().getPrepaidActivationCredit(provider.getApplication(), provider.getUser(),delegate);
		} catch (Throwable e) {
			return 0;
		}
	}

	public void changeAirtimeRate(int rateId) throws InvalidAirtimeRateException, TelusAPIException {

		if (rateId == 0)
			throw new InvalidAirtimeRateException(rateId);
		try {
				PrepaidSubscriberInfo prepaidSubscriberInfo = new PrepaidSubscriberInfo();
				prepaidSubscriberInfo.setBan(getSubscriber0().getBanId());
				prepaidSubscriberInfo.setPhoneNumber(getSubscriber0().getPhoneNumber());
				prepaidSubscriberInfo.setRateId(rateId);
				provider.getSubscriberLifecycleManager().updatePrepaidSubscriber(prepaidSubscriberInfo);
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.api.account.PrepaidConsumerAccount#getReservedBalance()
	 */
	public double getReservedBalance() {
		return delegate.getReservedBalance();
	}
	
	/*
	 * --Commented the Prepaid 5.1 rel changes private static void
	 * getPrepaidRates(){ String prepaidRates =
	 * TMReferenceDataManager.getPrepaidRates(); if (null != prepaidRates) {
	 * StringTokenizer stringTokenizer = new StringTokenizer(prepaidRates,";");
	 * while (stringTokenizer.hasMoreTokens()) { String token =
	 * stringTokenizer.nextToken(); int equalIndex = token.indexOf("="); int
	 * slashIndex = token.indexOf("/"); String rateId =
	 * token.substring(0,equalIndex); String localRate =
	 * token.substring(equalIndex+1,slashIndex); String longDsitanceRate =
	 * token.substring(slashIndex+1);
	 * 
	 * prepaidLocalRates.put(rateId.trim(),localRate.trim());
	 * prepaidLongDistanceRates.put(rateId.trim(),longDsitanceRate.trim()); } }
	 * else {Logger.debug(
	 * "== PrepaidConsumerAccount :: TMReferenceDataManager.getPrepaidRates() == null"
	 * ); } }
	 */

	public PreRegisteredPrepaidCreditCard getPreRegisteredTopUpCreditCard(){
		return delegate.getPreRegisteredTopUpCreditCard();
	}

	public void savePreRegisteredTopUpCreditCard(
			PreRegisteredPrepaidCreditCard preRegisteredPrepaidCreditCard)
			throws TelusAPIException {

		if (preRegisteredPrepaidCreditCard == null) {
			return;
		}

		if (getBanId() != 0) {
			try {
				PaymentMethodInfo info = new PaymentMethodInfo();

				info.setPaymentMethod(PaymentMethodInfo.PAYMENT_METHOD_REGULAR);
				info.setCreditCard0((CreditCardInfo)preRegisteredPrepaidCreditCard);
				info.setStatus(PaymentMethodInfo.DIRECT_DEBIT_STATUS_HOLD);
				info.setStatusReason(PaymentMethodInfo.DIRECT_DEBIT_REASON_CHANGE_BILL_DATA);

				provider.getAccountLifecycleFacade().updatePaymentMethod(getBanId(), this.getDelegate0(), 
							 info, getTransientNotificationSuppressionInd(), null, SessionUtil.getSessionId(provider.getAccountLifecycleFacade()));

				boolean updatePrepaid = ! topUpCreditCard.getDelegate().isSame((CreditCardInfo) preRegisteredPrepaidCreditCard);
				boolean registerPrepaidCreditcard = false;
				//If the existing topUpCreditCard token is empty, register the new preRegisteredPrepaidCreditCard
				if (topUpCreditCard.getDelegate().getToken() == null ||
						topUpCreditCard.getDelegate().getToken().trim().length()==0)
					registerPrepaidCreditcard = true;

				if (updatePrepaid ) {

					if (getSubscriber0() != null && getSubscriber0().getEquipment() != null) {
						Logger.debug("== PrepaidConsumer :: " + getSubscriber0().getPhoneNumber() + 
								" :: registerPrepaidCreditcard :: " + registerPrepaidCreditcard + 
								" :: (new) CreditCard :: " + preRegisteredPrepaidCreditCard.getTrailingDisplayDigits() + 
								" :: " + preRegisteredPrepaidCreditCard.getExpiryYear() + "/"+preRegisteredPrepaidCreditCard.getExpiryMonth());
						
						provider.getAccountLifecycleManager().updateTopupCreditCard(String.valueOf(getBanId()), getSubscriber0().getPhoneNumber(),
								//getSubscriber0().getEquipment().getSerialNumber(),//Serial number is not needed for April/2014 release Surepay Retirement 
								(CreditCardInfo) preRegisteredPrepaidCreditCard,
								provider.getUser(), false, registerPrepaidCreditcard);
					} else if (getSubscriber0() == null) {
						Logger.debug("== PrepaidConsumer :: getSubscriber0() == null");
					} else if (getSubscriber0().getEquipment() == null) {
						Logger.debug("== PrepaidConsumer :: getSubscriber0().getEquipment() == null");
					}

				}

				topUpCreditCard.copyFrom(preRegisteredPrepaidCreditCard);

			} catch (Throwable e) {
				provider.getExceptionHandler().handleException(e);
			}
		}
	}
	
	public String getBalanceCapOrThresholdCode() {
		return delegate.getBalanceCapOrThresholdCode();
	}

	public void setBalanceCapOrThresholdCode(String balanceCapOrThresholdCode) {
		this.delegate.setBalanceCapOrThresholdCode(balanceCapOrThresholdCode);
	}

	public double getUSLongDistanceRate() {
		return delegate.getUSLongDistanceRate();
	}

	public void setUSLongDistanceRate(double usLongDistanceRate) {
		delegate.setUSLongDistanceRate(usLongDistanceRate);
	}
}