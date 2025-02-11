/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import java.util.Date;

import com.telus.api.LimitExceededException;
import com.telus.api.TelusAPIException;
import com.telus.api.reference.BillCycle;
import com.telus.api.reference.CreditCheckDepositChangeReason;
import com.telus.api.reference.DiscountPlan;
import com.telus.api.servicerequest.ServiceRequestHeader;


/**
 * <CODE>PostpaidConsumerAccount</CODE>
 *
 */
public interface PostpaidAccount extends Account {

	/**
	 * Returns the alternate credit check address information.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 */
	Address getAlternateCreditCheckAddress();

	/**
	 * Saves the PaymentMethod on this existing account and updates the property on this instance.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @throws UnknownBANException if this account doesn't already exist.
	 */
	void savePaymentMethod(PaymentMethod newPaymentMethod) throws UnknownBANException, TelusAPIException;

	/**
	 * Saves the PaymentMethod on this existing account and updates the property on this instance
	 * and logs payment methods transaction to SRPDS.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @throws UnknownBANException if this account doesn't already exist.
	 */
	void savePaymentMethod(PaymentMethod newPaymentMethod, ServiceRequestHeader header) throws UnknownBANException, TelusAPIException;
	
	/**
	 * Returns PaymentMethod.
	 *
	 * @link aggregationByValue
	 */
	PaymentMethod getPaymentMethod();

	/**
	 * Returns array of standard airtime unbilled usage summary. The standard
	 * Airtime usage is identified in the database as featureCode = "STD".
	 *
	 * Use this method if you want to retrieve airtime usage only.
	 * For other usages, please use getVoiceUsageSummary(String featureCode) instead.
	 *
	 * @return VoiceUsageSummary[]
	 * @throws VoiceUsageSummaryException
	 * @throws TelusAPIException
	 */
	VoiceUsageSummary[] getVoiceUsageSummary() throws VoiceUsageSummaryException, TelusAPIException;

	/**
	 * Returns array of unbilled usage summary for the specified feature code.
	 *
	 * @return VoiceUsageSummary[]
	 * @throws VoiceUsageSummaryException
	 * @throws TelusAPIException
	 */
	VoiceUsageSummary[] getVoiceUsageSummary(String featureCode) throws VoiceUsageSummaryException, TelusAPIException;

	/**
	 * Returns array of WebUsageSummary. Unsupported currently.
	 *
	 * @return WebUsageSummary[]
	 * @throws TelusAPIException
	 */
	WebUsageSummary[] getWebUsageSummary() throws TelusAPIException;

	/**
	 * Returns the list of bill cycles that this account is allowed to be attached to.
	 *
	 * <p>This method may involve a remote method call.
	 *
	 * @return BillCycle[] -- Available cycles, This is never null.
	 */
	BillCycle[] getAvailableBillCycles() throws TelusAPIException;

	/**
	 * Returns the cycle this account is currently attached to.
	 *
	 * <p>Note:  This replaces Account.getBillCycle() and Account.getBillCycleCloseDay()
	 *
	 * @return BillCycle
	 */
	BillCycle getBillingCycle() throws TelusAPIException;

	/**
	 * Attaches this account to the given billing cycle.  This takes effect the next billing cycle
	 * (not the current cycle).
	 *
	 * <P>This method may involve a remote method call.
	 * @deprecated
	 * @param cycle -- The new cycle.
	 * @see #getAvailableBillCycles
	 */
	void changeBillCycle(BillCycle cycle) throws TelusAPIException, InvalidBillCycleChangeException;

	/**
	 * Attaches this account to the given billing cycle.  This takes effect the next billing cycle (not the current cycle).
	 * <p>The additional boolean parameter <code>testFirstCycleRun</code> indicates if a test needs to be performed before change.
	 * 
	 * @deprecated
	 * @param cycle BillCycle
	 * @param testFirstCycleRun boolean
	 * @throws TelusAPIException
	 * @throws InvalidBillCycleChangeException
	 */
	void changeBillCycle(BillCycle cycle, boolean testFirstCycleRun) throws TelusAPIException, InvalidBillCycleChangeException;

	/**
	 * Determines if this account can change billing cycles.  The success or failure of this test
	 * is dependent on the account's billing state (specifically in relation to its current billing
	 * cycle), not on any specific cycle it may be changed to.
	 *
	 * <P>If this test passes, the account can be changed to any cycle returned by <code>getAvailableCycles</code>
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @throws InvalidCycleChangeException
	 * @see #getAvailableBillCycles
	 */
	void testCycleChange() throws InvalidBillCycleChangeException, TelusAPIException;

	InvoiceProperties getInvoiceProperties();

	void setInvoiceProperties(InvoiceProperties invoiceProperties);

	/**
	 * Creates a new unsaved Discount associated with this account.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @see Discount#apply
	 */
	Discount newDiscount() throws TelusAPIException;

	/**
	 * Returns existing and future discounts for this account.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @see #newDiscount
	 * @see Discount#apply
	 */
	Discount[] getDiscounts() throws TelusAPIException;

	/**
	 * Returns the discount plans appropriate for this subscriber.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 *
	 */
	DiscountPlan[] getAvailableDiscountPlans() throws TelusAPIException;

	/**
	 * Returns the personal credit information for this account.
	 */
	PersonalCredit getPersonalCreditInformation();

	/**
	 * Creates a new FeeWaiver object.
	 * @param typeCode String
	 * @return FeeWaiver
	 */
	FeeWaiver newFeeWaiver(String typeCode);

	/**
	 * Returns an array of FeeWaiver objects.
	 * @throws TelusAPIException
	 * @return FeeWaiver[]
	 */
	FeeWaiver[] getFeeWaivers() throws TelusAPIException;

	/**
	 * Update the Deposit amounts for the product types
	 *
	 * <P>This method may involve a remote method call.</P>
	 *
	 * @param creditCheckResultDeposits CreditCheckResultDeposit
	 * @param reasonCode CreditCheckDepositChangeReason
	 * @param reasonText String
	 * @throws TelusAPIException
	 */
	void changeCreditCheckDeposits(CreditCheckResultDeposit[] creditCheckResultDeposits, CreditCheckDepositChangeReason reasonCode, String reasonText) throws TelusAPIException;

	/**
	 * Returns the PoolingPricePlanSubscriberCount[] of all subscribers participating in all pools.
	 * 
	 * <P>NOTE: This method must NOT be called for large BANs (BANs with 200 or more subscribers).  Failure to comply may lead to KB DB degradation</P>
	 * 
 	 * <P>This method may involve a remote method call.</P>
	 * 
	 * @param refresh - refresh from the database
	 * @return PoolingPricePlanSubscriberCount[]
	 * @throws TelusAPIException
	 */
	PoolingPricePlanSubscriberCount[] getPoolingEnabledPricePlanSubscriberCount(boolean refresh) throws TelusAPIException;
	
	/**
	 * Returns the PricePlanSubscriberCount[] of all subscribers participating in pools corresponding to the given
	 * pooling group ID encapsulated in a PoolingPricePlanSubscriberCount object.
	 * 
	 * <P>This method will return null if there are no subscribers on this account for the given pooling group.</P>
	 * 
 	 * <P>NOTE: This method must NOT be called for large BANs (BANs with 200 or more subscribers).  Failure to comply may lead to KB DB degradation</P>
 	 *  
	 * <P>This method may involve a remote method call.</P>
	 * 
	 * @param poolingGroupId pooling group ID
	 * @param refresh refresh from the database
	 * @return PoolingPricePlanSubscriberCount
	 * @throws TelusAPIException
	 */
	PoolingPricePlanSubscriberCount getPoolingEnabledPricePlanSubscriberCount(int poolingGroupId, boolean refresh) throws TelusAPIException;

	/**
	 * Returns the PricePlanSubscriberCount[] of all zero-minute contributing subscribers participating in pools
	 * corresponding to the given pooling group ID encapsulated in a PoolingPricePlanSubscriberCount object.
	 * 
	 * <P>This method will return null if there are no subscribers on this account for the given pooling group.</P>
 	 * <P>NOTE: This method must NOT be called for large BANs (BANs with 200 or more subscribers).  Failure to comply may lead to KB DB degradation</P>
 	 * 
	 * <P>This method may involve a remote method call.</P>
	 * 
	 * @param poolingGroupId pooling group ID
	 * @param refresh refresh from the database
	 * @return PoolingPricePlanSubscriberCount
	 * @throws TelusAPIException
	 */
	PoolingPricePlanSubscriberCount getZeroMinutePoolingEnabledPricePlanSubscriberCount(int poolingGroupId, boolean refresh) throws TelusAPIException;
	
	/**
	 * Returns the PricePlanSubscriberCount[] of all subscribers participating in dollar pools.
	 * 
 	 * <P>NOTE: This method must NOT be called for large BANs (BANs with 200 or more subscribers).  Failure to comply may lead to KB DB degradation</P>
 	 * 
	 * <P>This method may involve a remote method call.</P>
	 * 
	 * @param refresh refresh from the database
	 * @return PricePlanSubscriberCount[]
	 * @throws TelusAPIException
	 */
	PricePlanSubscriberCount[] getDollarPoolingPricePlanSubscriberCount(boolean refresh) throws TelusAPIException;
	
	/**
	 * Refreshes the PricePlanSubscriberCount[] caches for all pooling, dollar pooling and shareable PricePlanSubscriberCounts.
	 * 
 	 * <P>NOTE: This method must NOT be called for large BANs (BANs with 200 or more subscribers).  Failure to comply may lead to KB DB degradation</P>
 	 * 
	 * <P>This method may involve a remote method call.</P>
	 *  
	 * @throws TelusAPIException
	 */
	void refreshPricePlanSubscriberCounts() throws TelusAPIException;
	
	/**
     * Get the required payment amount for restoral.
     *
     * @return double the amount
     * @throws TelusAPIException
    */
	double getRequiredPaymentForRestoral() throws TelusAPIException;
	
	
	/**
	 * Get Voice ( air time ) usage unpaid amount. 
	 * @return total unpaid airtime amount
	 * @throws TelusAPIException
	 */
	double getUnpaidAirtimeTotal() throws TelusAPIException;
	/**
	 * Returns a list of subscribers that have SOCs belonging to the given data sharing
	 * group.  This method only returns active subscribers (Subscriber.getStatus() == 
	 * Subscriber.STATUS_ACTIVE) on the account.
	 *
	 * @param codes - array of 1 or more data sharing group codes
	 * @param effectiveDate  Effective Date 
	 * @return - Array of SubscribersByDataSharingGroupResult. Length of array is same as
	 * length of input parameter codes if codes are valid.  If there are no subscribers,
	 * array length of SubscribersByDataSharingGroupResult[i]. getDataSharingSubscribers()
	 * == 0
	 *
	 * @throws LimitExceededException if too many subscribers
	 * @throws TelusAPIException if code is not a valid data sharing group code and all other
	 * exceptions
	 */
	public SubscribersByDataSharingGroupResult[] getSubscribersByDataSharingGroups(String[] codes, Date effectiveDate) throws LimitExceededException, TelusAPIException;

	
}


