/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.*;
import com.telus.api.reference.*;
import com.telus.api.equipment.*;
import java.util.*;

/**
 * <CODE>PrepaidConsumerAccount</CODE>
 * 
 */
public interface PrepaidConsumerAccount extends BasePrepaidAccount {

	public static final int ACTIVATION_TYPE_NORMAL = 0;
	public static final int ACTIVATION_TYPE_VIRTUAL_NO_CHARGE = 1;
	public static final int ACTIVATION_TYPE_VIRTUAL_WITH_CHARGE = 2;
	public static final int ACTIVATION_TYPE_WITH_ESN = 3;
	public static final int ACTIVATION_TYPE_WITH_P2P = 4;
	public static final int ACTIVATION_TYPE_INTERACT_ONLINE_DEBIT = 5; // new
																		// activation
																		// type
																		// for
																		// interact
																		// online
	public static final int ACTIVATION_TYPE_CREDIT_CARD = 6;
	public static final int ACTIVATION_TYPE_AIRTIME_CARD = 7;

	public static final int BILLING_TYPE_PER_SECOND = 1;
	public static final int BILLING_TYPE_PER_MINUTE = 60;

	public static final String FEATURE_ADD_CHARGE_REASON = "212";
	public static final String AIRTIME_CARD_TOP_UP_CHARGE_REASON = "211";
	public static final String CREDIT_CARD_TOP_UP_CHARGE_REASON = "213";

	/**
	 * @deprecated for Prepaid Gemini
	 * @link aggregationByValue
	 */
	CreditCard getTopUpCreditCard();

	/**
	 * Returns the credit card authorization number returned during the
	 * activation in the case of a 'purchase-now'
	 * 
	 * <P>
	 * This method may involve a remote method call.
	 */
	String getActivationCreditCardAuthorizationNumber();

	/**
	 * @deprecated for Prepaid Gemini
	 * Saves the Top Up Credit Card for Prepaid Subscriber
	 * 
	 * <P>
	 * This method may involve a remote method call.
	 */
	void saveTopUpCreditCard(CreditCard creditCard) throws TelusAPIException;

	/**
	 * unregisters the Top Up Credit Card for Prepaid Subscriber
	 * 
	 * <P>
	 * This method may involve a remote method call.
	 */
	void removeTopUpCreditCard() throws TelusAPIException;

	double getBalance();

	Date getBalanceExpiryDate();

	Date getMinimumBalanceDate();

	double getAirtimeRate();

	double getLongDistanceRate();

	int getBillingType();
	
	/**
	 * Returns the balance that was swiped to intrust 
	 * (if the prepaid account is passed 30 day limit)
	 * 
	 *  @return double
	 */
	
	double getSwipedAmount();
	
	/**
	 * Returns the intrust expiry date (30 days + 7days)
	 * @return Date
	 */
	
	Date getInTrustBalanceExpiryDate();

	/**
   *
   * <b>TODO</b>: any modification requested should result in deprecating this method and having Prepaid web services handle the requested changes.  For futher details, 
   * refer to Client API Design.doc design document for Prepaid Real Time Rating project
   * 
   */
	PrepaidEventHistory[] getPrepaidEventHistory(Date startdate, Date endDate) throws TelusAPIException;

	/**
   *
   * <b>TODO</b>: any modification requested should result in deprecating this method and having Prepaid web services handle the requested changes.  For futher details, 
   * refer to Client API Design.doc design document for Prepaid Real Time Rating project
   * 
   */
	PrepaidEventHistory[] getPrepaidEventHistory(Date startdate, Date endDate, PrepaidEventType[] eventTypes) throws TelusAPIException;

	/**
   *
   * <b>TODO</b>: any modification requested should result in deprecating this method and having Prepaid web services handle the requested changes.  For futher details, 
   * refer to Client API Design.doc design document for Prepaid Real Time Rating project
   * 
   */
	PrepaidCallHistory[] getPrepaidCallHistory(Date startdate, Date endDate) throws TelusAPIException;

	/**
   *
   */
	AutoTopUp getAutoTopUp() throws TelusAPIException;

	/**
	 *@deprecated use applyTopUp (double, String cardType)
	 */
	String applyTopUp(double amount) throws InvalidCreditCardException, LimitExceededException, TelusAPIException;

	String applyTopUp(double amount, String cardType) throws LimitExceededException, TelusAPIException;

	/**
   *
   */
	String applyTopUp(AirtimeCard card) throws InvalidEquipmentException, LimitExceededException, TelusAPIException;

	/**
	 * This method will apply adjustment (credit or charge) for specific reason.
	 * To apply charge pass negative amount - for credit pass positive amount.
	 * If waive reason is specified, the charge will not be applied and charge
	 * reason and waive reason will be stored in database.
	 */
	void applyAdjustment(PrepaidAdjustmentReason adjustment, double amount, String transactionId, PrepaidAdjustmentReason waive)
			throws TelusAPIException;

	/**
	 * Deprecated.
	 * 
	 * This method will apply an adjustment (credit or charge) for a specific
	 * reason. In addition, this method specifies and applies adjustments for
	 * tax with corresponding Knowbility memos.
	 * 
	 * @see #applyAdjustment(PrepaidAdjustmentReason adjustment, double amount,
	 *      String transactionId, PrepaidAdjustmentReason waive, char taxOption,
	 *      String memoText)
	 */
	void applyAdjustment(PrepaidAdjustmentReason adjustment, double amount, String transactionId, PrepaidAdjustmentReason waive, boolean taxable,
			String memoText) throws TelusAPIException;

	/**
	 * Replaces deprecated applyAdjustment method.
	 * 
	 * This method will apply an adjustment (credit or charge) for a specific
	 * reason. In addition, this method specifies and applies adjustments for
	 * tax with corresponding Knowbility memos. The tax adjustments are based on
	 * the value of the taxOption parameter.
	 * 
	 * @param adjustment - the adjustment reason
	 * @param amount - the adjustment amount
	 * @param transactionId - the transaction ID
	 * @param waive - the waive reason
	 * @param taxOption - one of the Credit interface TAX_OPTION_XXXX
	 *        constants
	 * @param memoText - the Knowbility memo for the adjustment
	 */
	void applyAdjustment(PrepaidAdjustmentReason adjustment, double amount, String transactionId, PrepaidAdjustmentReason waive, char taxOption,
			String memoText) throws TelusAPIException;

	/**
   *
   */
	void changeBalanceExpiryDate(Date date) throws TelusAPIException;

	/**
	 * @deprecated Please use changeAirtimeRate(int rateId)
	 */
	void changeAirtimeRate(double rate) throws InvalidAirtimeRateException, TelusAPIException;

	/**
	 * Activation and OOM application will be calling following methods and set
	 * the pre-paid payment arrangement information
	 * 
	 * @throws TelusAPIException
	 */
	public ActivationTopUpPaymentArrangement getActivationTopUpPaymentArrangement();

	double getOutstandingCharge();

	double getMaximumBalanceCap();

	/**
	 * @deprecated Please use getPrepaidActivationCredit(). Will be removed
	 *             after July 2008 release.
	 * @return double
	 */
	double getPrepaidActivationCharge();

	/**
	 * Returns prepaid activation credit. This would trigger a remote method
	 * call.
	 * 
	 * @return double
	 */
	double getPrepaidActivationCredit();

	/**
	 * Updates the RateId for the subscriber. This method is created to accept
	 * rate id as parameter instead of using deprecated method that accepts
	 * rate.
	 * 
	 * @param rateId
	 * @throws InvalidAirtimeRateException
	 * @throws TelusAPIException
	 */
	void changeAirtimeRate(int rateId) throws InvalidAirtimeRateException, TelusAPIException;
	
	/**
	 * Returns the reserved balance
	 * 
	 * @return the reserved balance
	 */
	double getReservedBalance();
	
	PreRegisteredPrepaidCreditCard getPreRegisteredTopUpCreditCard();
	
	void savePreRegisteredTopUpCreditCard(PreRegisteredPrepaidCreditCard preRegisteredPrepaidCreditCard) throws TelusAPIException;

	/**
	 * Returns prepaid balance cap or threshold code.
	 * 
	 * 
	 * @return String
	 */
	String getBalanceCapOrThresholdCode();
	
	/**
	 * Returns Prepaid US Long Distance Rate.
	 * 
	 * 
	 * @return double
	 */
	double getUSLongDistanceRate();
}
