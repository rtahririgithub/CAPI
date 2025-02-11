/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.ActivationTopUpPaymentArrangement;
import com.telus.api.account.AutoTopUp;
import com.telus.api.account.BillNotificationContact;
import com.telus.api.account.CreditCard;
import com.telus.api.account.InvalidAirtimeRateException;
import com.telus.api.account.PCSPrepaidConsumerAccount;
import com.telus.api.account.PreRegisteredPrepaidCreditCard;
import com.telus.api.account.PrepaidCallHistory;
import com.telus.api.account.PrepaidEventHistory;
import com.telus.api.account.PricePlanSubscriberCount;
import com.telus.api.account.ServiceSubscriberCount;
import com.telus.api.equipment.AirtimeCard;
import com.telus.api.reference.PrepaidAdjustmentReason;
import com.telus.api.reference.PrepaidEventType;

public class PrepaidConsumerAccountInfo extends BasePrepaidAccountInfo implements PCSPrepaidConsumerAccount {
	
	static final long serialVersionUID = 1L;

	private CreditCardInfo activationCreditCard = new CreditCardInfo();
	private PreRegisteredPrepaidCreditCardInfo preRegisteredTopUpCard = new PreRegisteredPrepaidCreditCardInfo();
	private int activationType;
	private String activationCode;
	private double balance;
	private Date balanceExpiryDate;
	private Date minimumBalanceDate;
	private double airtimeRate;
	private double longDistanceRate;
	private int billingType;
	private AutoTopUpInfo autoTopUp;
	private boolean existingAutoTopUp = false;
	private boolean validForMigration = false;
	private ActivationTopUpPaymentArrangementInfo actTopUpPaymentArrangement = new ActivationTopUpPaymentArrangementInfo();
	private double outstandingCharge;
	private double maximumBalanceCap;
	private double activationCharge;
	private double activationCreditAmount;
	private String associatedHandsetIMEI;
	private double reservedBalance;
	private String balanceCapOrThresholdCode;
	private double usLongDistanceRate;
	
	//WCoC fields for balance swipe
	private double swipedAmount;
	private Date inTrustBalanceExpiryDate;

	/*
	 * public static PrepaidConsumerAccountInfo newPCSInstance() { return new
	 * PrepaidConsumerAccountInfo(ACCOUNT_TYPE_CONSUMER,
	 * ACCOUNT_SUBTYPE_PCS_PREPAID); }
	 */
	public static PrepaidConsumerAccountInfo newPCSInstance(char accountSubtype) {
		PrepaidConsumerAccountInfo instance = new PrepaidConsumerAccountInfo(ACCOUNT_TYPE_CONSUMER, accountSubtype);
		instance.setBalanceCapOrThresholdCode("");
		return instance;
	}
	
	public boolean isValidForMigration() {
		return validForMigration;
	}

	public void setValidForMigration(boolean validForMigration) {
		this.validForMigration = validForMigration;
	}

	protected PrepaidConsumerAccountInfo(char accountType, char accountSubType) {
		super(accountType, accountSubType);
	}

	public PrepaidConsumerAccountInfo() {
		super();
	}
	/**
	 * @deprecated for Prepaid Gemini
	 */
	public CreditCard getTopUpCreditCard() {
		return preRegisteredTopUpCard;
	}
	public CreditCardInfo getTopUpCreditCard0() {
		return preRegisteredTopUpCard;
	}
	
	public PreRegisteredPrepaidCreditCard getPreRegisteredTopUpCreditCard(){
		return preRegisteredTopUpCard;
	}

	public int getActivationType() {
		return activationType;
	}

	public void setActivationType(int activationType) {
		this.activationType = activationType;
	}

	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = toUpperCase(activationCode);
	}

	public CreditCard getActivationCreditCard() {
		return activationCreditCard;
	}

	public void setActivationCreditCard(CreditCard activationCreditCard) {
		setActivationCreditCard0((CreditCardInfo) activationCreditCard);
	}

	public void setActivationCreditCard0(CreditCardInfo activationCreditCard) {
		this.activationCreditCard = activationCreditCard;
		;
	}

	public CreditCardInfo getActivationCreditCard0() {
		return activationCreditCard;
	}

	public String getActivationCreditCardAuthorizationNumber() {
		return activationCreditCard.getAuthorizationCode();
	}

	public String toString() {
		StringBuffer s = new StringBuffer();

		s.append("PrepaidConsumerAccountInfo:{\n");
		s.append(super.toString());

		s.append("activationCreditCard=[").append(activationCreditCard).append("]\n");
		s.append("preRegisteredTopUpCard=[").append(preRegisteredTopUpCard).append("]\n");
		s.append("    activationType=[").append(activationType).append("]\n");
		s.append("    activationCode=[").append(activationCode).append("]\n");
		s.append("    balance=[").append(balance).append("]\n");
		s.append("    balanceExpiryDate=[").append(balanceExpiryDate).append("]\n");
		s.append("    minimumBalanceDate=[").append(minimumBalanceDate).append("]\n");
		s.append("    airtimeRate=[").append(airtimeRate).append("]\n");
		s.append("    longDistanceRate=[").append(longDistanceRate).append("]\n");
		s.append("    billingType=[").append(billingType).append("]\n");
		s.append("    outstandingCharge=[").append(outstandingCharge).append("]\n");
		s.append("    maximumBalanceCap=[").append(maximumBalanceCap).append("]\n");
		s.append("    activationCharge=[").append(activationCharge).append("]\n");
		s.append("    reservedBalance=[").append(reservedBalance).append("]\n");
		s.append("    balanceCapOrThresholdCode=[").append(balanceCapOrThresholdCode).append("]\n");
		s.append("    swipedAmount=[").append(swipedAmount).append("]\n");
		s.append("    inTrustBalanceExpiryDate=[").append(inTrustBalanceExpiryDate).append("]\n");
		s.append("    existingAutoTopUp=[").append(existingAutoTopUp).append("]\n");
		s.append("    usLongDistanceRate=[").append(usLongDistanceRate).append("]\n");
		s.append("}");

		return s.toString();
	}

	public void copyFrom(AccountInfo o) {
		if (o instanceof PrepaidConsumerAccountInfo) {
			copyFrom((PrepaidConsumerAccountInfo) o);
		} else {
			super.copyFrom(o);
		}
	}

	public void copyFrom(PrepaidConsumerAccountInfo o) {
		super.copyFrom(o);

		preRegisteredTopUpCard.copyFrom(o.preRegisteredTopUpCard);
		activationType = o.activationType;
		setActivationCode(o.activationCode);
		// activationCreditCard.copyFrom(o.activationCreditCard);
		setBalance(o.balance);
		balanceExpiryDate = cloneDate(o.balanceExpiryDate);
		minimumBalanceDate = cloneDate(o.minimumBalanceDate);
		setAirtimeRate(o.airtimeRate);
		setLongDistanceRate(o.longDistanceRate);
		setBillingType(o.billingType);
		setAutoTopUp(o.autoTopUp);
		setOutstandingCharge(o.outstandingCharge);
		setMaximumBalanceCap(o.maximumBalanceCap);
		activationCharge = o.activationCharge;
		balanceCapOrThresholdCode = o.balanceCapOrThresholdCode;
		usLongDistanceRate = o.usLongDistanceRate;
		//WCC specific fields
		setSwipedAmount(o.swipedAmount);
		inTrustBalanceExpiryDate = cloneDate(o.inTrustBalanceExpiryDate);
	}

	public double getBalance() {
		return balance;
	}

	public Date getBalanceExpiryDate() {
		return balanceExpiryDate;
	}

	public Date getMinimumBalanceDate() {
		return minimumBalanceDate;
	}

	public double getAirtimeRate() {
		return airtimeRate;
	}

	public double getLongDistanceRate() {
		return longDistanceRate;
	}

	public int getBillingType() {
		return billingType;
	}
	
	public double getSwipedAmount() {
		return swipedAmount;
	}

	public Date getInTrustBalanceExpiryDate() {
		return inTrustBalanceExpiryDate;
	}

	public PrepaidEventHistory[] getPrepaidEventHistory(Date startdate, Date endDate) throws TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public PrepaidEventHistory[] getPrepaidEventHistory(Date startdate, Date endDate, PrepaidEventType[] eventTypes) throws TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public PrepaidCallHistory[] getPrepaidCallHistory(Date startdate, Date endDate) throws TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public String applyTopUp(double amount) throws TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public String applyTopUp(double amount, String cardType) throws TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public String applyTopUp(AirtimeCard card) throws TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}
	/**
	 * @deprecated for Prepaid Gemini
	 */
	public void saveTopUpCreditCard(CreditCard creditCard) throws TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}
	
	public void savePreRegisteredTopUpCreditCard(PreRegisteredPrepaidCreditCard preRegisteredPrepaidCreditCard) throws TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public void removeTopUpCreditCard() throws TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public void saveBalanceExpiryDate(Date balanceExpiryDate) throws TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public void saveAirtimeRate(double airtimeRate) throws TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void setBalanceExpiryDate(Date balanceExpiryDate) {
		this.balanceExpiryDate = balanceExpiryDate;
	}

	public void setMinimumBalanceDate(Date minimumBalanceDate) {
		this.minimumBalanceDate = minimumBalanceDate;
	}

	public void setAirtimeRate(double airtimeRate) {
		this.airtimeRate = airtimeRate;
	}

	public void setBillingType(int billingType) {
		this.billingType = billingType;
	}

	public void setLongDistanceRate(double longDistanceRate) {
		this.longDistanceRate = longDistanceRate;
	}
	
	public void setSwipedAmount(double swipedAmount) {
		this.swipedAmount = swipedAmount;
	}
	
	public void setInTrustBalanceExpiryDate(Date inTrustBalanceExpiryDate) {
		this.inTrustBalanceExpiryDate = inTrustBalanceExpiryDate;
	}

	public void applyAdjustment(PrepaidAdjustmentReason adjustment, double amount, String transactionId, PrepaidAdjustmentReason waive)
			throws TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public void applyAdjustment(PrepaidAdjustmentReason adjustment, double amount, String transactionId, PrepaidAdjustmentReason waive,
			boolean taxable, String memoText) throws TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public void applyAdjustment(PrepaidAdjustmentReason adjustment, double amount, String transactionId, PrepaidAdjustmentReason waive,
			char taxOption, String memoText) throws TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public void changeBalanceExpiryDate(Date date) throws TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public void changeAirtimeRate(double rate) throws InvalidAirtimeRateException, TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public AutoTopUp getAutoTopUp() {
		return autoTopUp;
	}

	public void setAutoTopUp(AutoTopUpInfo autoTopUp) {
		this.autoTopUp = autoTopUp;
	}

	public AutoTopUpInfo getAutoTopUp0() {
		return autoTopUp;
	}

	public boolean getExistingAutoTopUp() {
		return existingAutoTopUp;
	}

	public void setExistingAutoTopUp(boolean existingAutoTopUp) {
		this.existingAutoTopUp = existingAutoTopUp;
	}

	public ServiceSubscriberCount[] getServiceSubscriberCounts(String[] serviceCodes, boolean includeExpired) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public PricePlanSubscriberCount[] getAirtimeMinutePoolingEnabledPricePlanSubscriberCounts() {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public PricePlanSubscriberCount[] getLDMinutePoolingEnabledPricePlanSubscriberCounts() {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public String getPortProtectionIndicator() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");

	}

	public void updatePortRestriction(boolean restrictPort) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");

	}

	public ActivationTopUpPaymentArrangement getActivationTopUpPaymentArrangement() {
		return actTopUpPaymentArrangement;
	}

	public ActivationTopUpPaymentArrangementInfo getActivationTopUpPaymentArrangement0() {
		return actTopUpPaymentArrangement;
	}

	public BillNotificationContact[] getBillNotificationContacts() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public void saveBillNotificationDetails(long portalUserID, BillNotificationContact[] billNotificationContact) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public double getOutstandingCharge() {
		return outstandingCharge;
	}

	public void setOutstandingCharge(double outstandingCharge) {
		this.outstandingCharge = outstandingCharge;
	}

	public double getMaximumBalanceCap() {
		return maximumBalanceCap;
	}

	public void setMaximumBalanceCap(double maximumBalanceCap) {
		this.maximumBalanceCap = maximumBalanceCap;
	}

	public double getPrepaidActivationCharge() {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public double getPrepaidActivationCredit() {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public double getActivationCreditAmount() {
		return activationCreditAmount;
	}

	public void setActivationCreditAmount(double activationCreditAmount) {
		this.activationCreditAmount = activationCreditAmount;
	}

	public String getAssociatedHandsetIMEI() {
		return associatedHandsetIMEI;
	}

	public void setAssociatedHandsetIMEI(String associatedHandsetIMEI) {
		this.associatedHandsetIMEI = associatedHandsetIMEI;
	}

	public void changeAirtimeRate(int rateId) throws InvalidAirtimeRateException, TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public double getReservedBalance() {
		return reservedBalance;
	}

	public void setReservedBalance(double reservedBalance) {
		this.reservedBalance = reservedBalance;
	}
	
	public String getBalanceCapOrThresholdCode() {
		return balanceCapOrThresholdCode;
	}

	public void setBalanceCapOrThresholdCode(String balanceCapOrThresholdCode) {
		this.balanceCapOrThresholdCode = balanceCapOrThresholdCode;
	}

	public double getUSLongDistanceRate() {
		return usLongDistanceRate;
	}

	public void setUSLongDistanceRate(double usLongDistanceRate) {
		this.usLongDistanceRate = usLongDistanceRate;
	}
}
