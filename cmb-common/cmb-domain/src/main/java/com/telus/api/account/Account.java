/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import java.util.Date;

import com.telus.api.HistorySearchException;
import com.telus.api.InvalidMultiSubscriberOperationException;
import com.telus.api.LimitExceededException;
import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.portability.PRMSystemException;
import com.telus.api.portability.PortRequest;
import com.telus.api.portability.PortRequestException;
import com.telus.api.reference.ClientConsentIndicator;
import com.telus.api.servicerequest.ServiceRequestHeader;
 

/**
 * <CODE>Account</CODE> represents the maximum intersection of all
 * account types. It is the superclass for specific account types.
 *
 * @see AccountSummary
 * @see PostpaidBusinessPersonalAccount
 * @see PostpaidBusinessRegularAccount
 * @see PostpaidConsumerAccount
 * @see PostpaidCorporateRegularAccount
 * @see PrepaidConsumerAccount
 *
 */
public interface Account extends AccountSummary {

	public static final String BILL_STATE_UNBILLED = "U";
	public static final String BILL_STATE_BILLED   = "B";
	public static final String BILL_STATE_ALL      = "A";

	public static final int FORM_MIKE_SHARED_FLEET_APPLICATION = 102;
	
	//Duplicate BAN Check search levels
	public static final String DEFAULT_DUPLICATE_BAN_SEARCH_LEVEL     = "default";
	public static final String MIKE_TO_PCS_DUPLICATE_BAN_SEARCH_LEVEL = "miketopcs";
	
	public static final int ACCOUNT_LOAD_ALL = 255;
	public static final int ACCOUNT_LOAD_CDA = 2;
	public static final int ACCOUNT_LOAD_ALL_BUT_NO_CDA = ACCOUNT_LOAD_ALL - ACCOUNT_LOAD_CDA; //we can put this constants into enum later
	
	
	int getHierarchyId();
	
	boolean isBelongToHierarchy();
	
	int getNoOfInvoice() throws TelusAPIException;
	
	void setNoOfInvoice(int noOfInvoice);

	/**
	 * 
	 * Finds out the eligibly and deposit amount. Populates these values in isElegible() and setDepositAmount in 
	 * the InternationalServiceEligibilityCheckResult object and returns it to caller.   
	 * 
	 * @return InternationalServiceEligibilityCheckResult
	 * @throws TelusAPIException
	 */
	InternationalServiceEligibilityCheckResult checkInternationalServiceEligibility() throws TelusAPIException;
	
	Address getAddress();

	String getAdditionalLine();

	void setAdditionalLine(String additionalLine);

	String getEmail();

	void setEmail(String email);

	void setPin(String pin);

	String getLanguage();

	void setLanguage(String language);

	int getBillCycle();

	int getBillCycleCloseDay();

	String getHomeProvince();

	void setHomeProvince(String homeProvince);

	String getAccountCategory();

	/**
	 * Checks if the specified number of subscribers can be added by performing
	 * the addon logic.  The returned CreditCheckResult is different from that
	 * returned by getCreditCheckResult.
	 *
	 * <P>This method may involve a remote method call.
	 * @param subscriberCount How many subscriber want to activate
	 * * @param threshold Amount from offer.  If differentiated credit rule doesn’t apply pass a negative number.
	 *
	 * @see #getCreditCheckResult
	 */
	CreditCheckResult checkNewSubscriberEligibility(int subscriberCount, double thresholdAmount) throws TelusAPIException;

	/**
	 * Checks the current reward redemption eligibility for this account.
	 *
	 * <P>This method may involve a remote method call.
	 */
	RewardRedemptionResult checkRewardRedemptionEligibility() throws TelusAPIException;

	void setAccountCategory(String accountCategory);

	int getNextBillCycle();

	int getNextBillCycleCloseDay();

	Date getVerifiedDate();

	void setVerifiedDate(Date date);

	boolean  isHandledBySubscriberOnly();


	String getHomePhone();

	void setHomePhone(String homePhone);

	String getBusinessPhone();

	void setBusinessPhone(String businessPhone);

	String getBusinessPhoneExtension();

	void setBusinessPhoneExtension(String businessPhoneExtension);

	FinancialHistory getFinancialHistory();

	CreditCheckResult getCreditCheckResult();

	/**
	 * Returns the number of active subscribers where the product type
	 * matches the account type (i.e. number of all active cellular
	 * subscribers if it is a PCS account or all active pager subscribers
	 * if it is a pager account etc.)
	 */
	int getActiveSubscribersCount();

	/**
	 * Returns the number of suspended subscribers where the product type
	 * matches the account type (i.e. number of all suspended cellular
	 * subscribers if it is a PCS account or all suspended pager subscribers
	 * if it is a pager account etc.)
	 */
	int getSuspendedSubscribersCount();

	/**
	 * Returns the number of reserved subscribers where the product type
	 * matches the account type (i.e. number of all reserved cellular
	 * subscribers if it is a PCS account or all reserved pager subscribers
	 * if it is a pager account etc.)
	 */
	int getReservedSubscribersCount();

	/**
	 * Returns the number of cancelled subscribers where the product type
	 * matches the account type (i.e. number of all cancelled cellular
	 * subscribers if it is a PCS account or all cancelled pager subscribers
	 * if it is a pager account etc.)
	 */
	int getCancelledSubscribersCount();

	/**
	 * Returns the total number of subscribers in any state where the product type
	 * matches the account type (i.e. number of all cellular
	 * subscribers if it is a PCS account or all pager subscribers
	 * if it is a pager account etc.)
	 */
	int getSubscriberCount();

	/**
	 * Returns the number of all active subscribers for this account,
	 * regardless of product type.
	 */
	int getAllActiveSubscribersCount();

	/**
	 * Returns the number of all suspended subscribers for this account,
	 * regardless of product type.
	 */
	int getAllSuspendedSubscribersCount();

	/**
	 * Returns the number of all reserved subscribers for this account,
	 * regardless of product type.
	 */
	int getAllReservedSubscribersCount();

	/**
	 * Returns the number of all cancelled subscribers for this account,
	 * regardless of product type.
	 */
	int getAllCancelledSubscribersCount();

	/**
	 * Returns the total number of subscribers in any state for this account,
	 * regardless of product type.
	 */
	int getAllSubscriberCount();

	/**
	 * @deprecated replaced by {@link #getSpecialInstructionsMemo()}
	 */
	String getSpecialInstructions();

	/**
	 * @deprecated Use getFinancialHistory().isHotlined instead.
	 * since May 29, 2006
	 */
	boolean isHotlined();

	void setHotlined(boolean hotlined);

	Date getLastChangesDate();

	/**
	 * Returns accounts that appear to have the same identity as this one, not including itself.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 */
	AccountSummary[] getDuplicateAccounts() throws TelusAPIException;
	
	/**
	 * Returns accounts that appear to have the same identity as this one, not including itself.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 */
	AccountSummary[] getDuplicateAccounts(String duplicateSearchLevel) throws TelusAPIException;

	/**
	 * Returns an array of BANs that appear to have the same identity as this one, not including itself.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 */
	int[] getDuplicateAccountBANs() throws TelusAPIException;

	/**
	 * Returns an array of BANs that appear to have the same identity as this one, not including itself.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 */
	int[] getDuplicateAccountBANs(String duplicateSearchLevel) throws TelusAPIException;

	/**
	 * Saves this account.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 */
	void save() throws TelusAPIException, InvalidCreditCardException;

	/**
	 * Saves this account.
	 * @param saveCreditCheckInfo indicate whether or not save credit check info
	 *
	 * <P>This method may involve a remote method call.
	 *
	 */
	void save( boolean saveCreditCheckInfo ) throws TelusAPIException, InvalidCreditCardException;

	/**
	 * Reloads this account from the datastore, discarding any modifications
	 * made since its last retrieval.
	 *
	 * <P>This method may involve a remote method call.
	 */
	void refresh() throws TelusAPIException;

	/**
	 * Reloads the credit check data for this account.  Use <CODE>getCreditCheckResult()</CODE>
	 * to obtain the newly retrieved data.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @see #getCreditCheckResult
	 * @see CreditCheckResult
	 */
	void refreshCreditCheckResult() throws TelusAPIException;

	/**
	 * Applies a deposit to this account using a credit card.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param subscriberCount the number-of-subscribers this deposit satifies.
	 * @param amount the total amount to be debited.
	 * @param creditCard the credit card to denit.
	 * @param businessRole
	 * @param auditHeader
	 *
	 * @return the authorization number for this transaction
	 *
	 * @exception PaymentFailedException if the deposit was unsuccessful
	 * @exception InvalidCreditCardException if the deposit was unsuccessful due to a credit card issue.
	 */
	String payDeposit(int subscriberCount, double amount, CreditCard creditCard, String businessRole, AuditHeader auditHeader) throws TelusAPIException, PaymentFailedException, InvalidCreditCardException;

	/**
	 * Applies a deposit to this account using a credit card.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param subscriberCount the number-of-subscribers this deposit satifies.
	 * @param amount the total amount to be debited.
	 * @param creditCard the credit card to denit.
	 * @param sourceID
	 * @param sourceType
	 * @param businessRole
	 * @param auditHeader
	 *
	 * @return the authorization number for this transaction
	 *
	 * @exception PaymentFailedException if the deposit was unsuccessful
	 * @exception UnknownObjectException if the sourceID-sourceType pair is invalid.
	 * @exception InvalidCreditCardException if the deposit was unsuccessful due to a credit card issue.
	 */
	String payDeposit(int subscriberCount, double amount, CreditCard creditCard, String sourceID, String sourceType, String businessRole, AuditHeader auditHeader) throws TelusAPIException, PaymentFailedException, UnknownObjectException, InvalidCreditCardException;

	/**
	 * Applies a payment to this account using a credit card.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param amount the total amount to be debited.
	 * @param creditCard the credit card to denit.
	 * @param businessRole
	 * @param auditHeader
	 * 
	 * @return the authorization number for this transaction
	 *
	 * @exception PaymentFailedException if the payment was unsuccessful
	 * @exception InvalidCreditCardException if the payment was unsuccessful due to a credit card issue.
	 */
	String payBill(double amount, CreditCard creditCard, String businessRole, AuditHeader auditHeader) throws TelusAPIException, PaymentFailedException, InvalidCreditCardException;

	/**
	 * Applies a payment to this account using a credit card.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 *  @param amount the total amount to be debited.
	 *  @param creditCard the credit card to denit.
	 *  @param sourceID
	 *  @param sourceType
	 *  @param businessRole
	 *  @param auditHeader
	 * @return the authorization number for this transaction
	 *
	 * @exception PaymentFailedException if the payment was unsuccessful
	 * @exception UnknownObjectException if the sourceID-sourceType pair is invalid.
	 * @exception InvalidCreditCardException if the payment was unsuccessful due to a credit card issue.
	 *
	 */
	String payBill(double amount, CreditCard creditCard, String sourceID, String sourceType, String businessRole, AuditHeader auditHeader) throws TelusAPIException, PaymentFailedException, UnknownObjectException, InvalidCreditCardException;

	/**
	 * Retrieves a phone number by SubscriberId.
	 *
	 * @param pSubscriberId
	 * @return Phone Number
	 * @throws TelusAPIException
	 */
	String getPhoneNumberBySubscriberID(String pSubscriberId) throws TelusAPIException;

	/**
	 * Retrieve invoice history, given a specific date range.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @return the list of transactions within the date range.
	 *
	 * @exception HistorySearchException if the search criteria is invalid
	 */
	InvoiceHistory [] getInvoiceHistory (Date from, Date to) throws TelusAPIException, HistorySearchException;

	/**
	 * Retrieve payment transaction history, given a specific date range.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @return the list of transactions within the date range.
	 *
	 * @exception HistorySearchException if the search criteria is invalid
	 */
	PaymentHistory [] getPaymentHistory (Date from, Date to) throws TelusAPIException, HistorySearchException;

	/**
	 * Retrieve last payment activity.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @return the last payment transaction.
	 *
	 * @exception HistorySearchException if the search criteria is invalid
	 */
	PaymentHistory getLastPaymentActivity () throws TelusAPIException, HistorySearchException;

	/**
	 * Retrieve payment method change (PAP, PAC, etc) history, given a specific date range.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @return the list of transactions within the date range.
	 *
	 * @exception HistorySearchException if the search criteria is invalid
	 */
	PaymentMethodChangeHistory [] getPaymentMethodChangeHistory (Date from, Date to) throws TelusAPIException, HistorySearchException;

	/**
	 * Retrieve status change (new activation, suspension, etc) history, given a specific date range.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @return the list of transactions within the date range.
	 *
	 * @exception HistorySearchException if the search criteria is invalid
	 */
	StatusChangeHistory [] getStatusChangeHistory(Date from, Date to) throws TelusAPIException, HistorySearchException;

	/**
	 * Retrieve address change history, given a specific date range.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @return the list of changes within the date range.
	 *
	 * @exception HistorySearchException if the search criteria is invalid
	 */
	AddressHistory[] getAddressChangeHistory(Date from, Date to) throws TelusAPIException, HistorySearchException;

	/**
	 * Prorates a number of minutes over a set of months.  The array will be time-ordered on the
	 * expiryDate field.
	 *
	 * ProrationMinutes are calculated based on the customer's billing cycle, current date,
	 * total-number-of-months and total-number-of-minutes.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.  It may also be freely modified.
	 *
	 * <P>This method may involve a remote method call.
	 */
	ProrationMinutes[] getProrationMinutes(int months, int totalMinutes) throws TelusAPIException;

	/**
	 * Retrieve deposit history, given a specific date range.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @return the list of deposits within the date range.
	 *
	 * @exception HistorySearchException if the search criteria is invalid
	 */
	DepositHistory[] getDepositHistory (Date from, Date to) throws TelusAPIException, HistorySearchException;

	/**
	 * Retrieve refund history, given a specific date range.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @return the list of refunds within the date range.
	 *
	 * @exception HistorySearchException if the search criteria is invalid
	 */
	RefundHistory[] getRefundHistory (Date from, Date to) throws TelusAPIException, HistorySearchException;

	/**
	 * Retrieve pending charge history, given a specific date range, level (all, subscriber or account), and optionally a subscriber id.
	 * Search results are limited to the maximum specified in the argument.
	 *
	 * <P>This method may involve a remote method call.</P>
	 *
	 * @param from Date
	 * @param to Date
	 * @param level char
	 * @param subscriberId String
	 * @param maximum int
	 * 
	 * @return SearchResults
	 * 
	 * @throws TelusAPIException
	 * @throws HistorySearchException
	 */
	SearchResults getPendingChargeHistory (Date from, Date to, char level, String subscriberId, int maximum) throws TelusAPIException, HistorySearchException;

	/**
	 * Retrieve credits, given a specific date range, level (all, subscriber, or account), requested state of the credits (billed, unbilled or all), and optionally a subscriber id.
	 * Search results are limited to the maximum specified in the argument.
	 *
	 * <P>This method may involve a remote method call.</P>
	 *
	 * @param from Date
	 * @param to Date
	 * @param level char
	 * @param subscriberId String
	 * @param maximum int
	 * 
	 * @return SearchResults
	 * 
	 * @throws TelusAPIException
	 * @throws HistorySearchException
	 */
	SearchResults getCredits (Date from, Date to, String billState, char level, String subscriberId, int maximum) throws TelusAPIException;


	/**
	 * Retrieve credits, given a specific date range, level (all, subscriber, or account), requested state of the credits (billed, unbilled or all), applied by a specified user, and optionally a subscriber id.
	 * Search results are limited to the maximum specified in the argument.
	 *
	 * <P>This method may involve a remote method call.</P>
	 *
	 * @param from Date
	 * @param to Date
	 * @param billState String
	 * @param knowbilityOperatorId String
	 * @param level char
	 * @param subscriberId String
	 * @param maximum int
	 * 
	 * @return SearchResults
	 * 
	 * @throws TelusAPIException
	 * @throws HistorySearchException
	 */
	SearchResults getCredits (Date from, Date to, String billState, char level, String subscriberId, String knowbilityOperatorId, int maximum) throws TelusAPIException;

	/** Corporate ID is set to the highest node of the Corporate Hierarchy to which the account belongs.
	 *  Only accounts that belong to a corporate hierarchy will have this attribute populated.
	 *
	 */
	String getCorporateId();

	/**
	 * Cancel the whole account.
	 *
	 * <P>This method may invoke a remote method call.</P>
	 *
	 * @param activityDate date
	 * @param reason String
	 * @param depositReturnMethod char
	 * @param waiver String
	 * @param memoText String
	 * 
	 * @throws TelusAPIException
	 */
	void cancel(Date activityDate, String reason, char depositReturnMethod, String waiver, String memoText) throws TelusAPIException;
	

	/**
	 * Cancel the whole account.
	 *
	 * <P>This method may invoke a remote method call.</P>
	 *
	 * @param reason String
	 * @param depositReturnMethod char
	 * @param waiver String
	 * 
	 * @throws TelusAPIException
	 */
	void cancel(String reason, char depositReturnMethod, String waiver) throws TelusAPIException;

	/**
	 * Cancel the whole account.
	 *
	 * <P>This method may invoke a remote method call.</P>
	 *
	 * @param reason String
	 * @param depositReturnMethod char
	 * 
	 * @throws TelusAPIException
	 */
	void cancel(String reason, char depositReturnMethod) throws TelusAPIException;

	/**
	 * Suspend the whole account.
	 *
	 * <P>This method may invoke a remote method call.</P>
	 *
	 * @param activityDate Date
	 * @param reason String
	 * @param memoText String
	 * 
	 * @throws TelusAPIException
	 */
	void suspend(Date activityDate, String reason, String memoText) throws TelusAPIException;

	/**
	 * Suspend the whole account.
	 *
	 * <P>This method may invoke a remote method call.</P>
	 *
	 * @param reason String
	 * 
	 * @throws TelusAPIException
	 */
	void suspend(String reason) throws TelusAPIException;

	/**
	 * Create an account identical to the given one.
	 * @throws TelusAPIException
	 */
	Account createDuplicateAccount() throws TelusAPIException;

	String getOtherPhoneType();

	void setOtherPhoneType(String otherPhoneType);

	String getOtherPhone();

	void setOtherPhone(String otherPhone);

	String getOtherPhoneExtension();

	void setOtherPhoneExtension(String otherPhoneExtension);

	String getContactPhone();

	void setContactPhone(String contactPhone);

	String getContactPhoneExtension();

	void setContactPhoneExtension(String contactPhoneExtension);

	ConsumerName getContactName();

	Charge[] getBilledCharges(int billSeqNo) throws TelusAPIException;

	Charge[] getBilledCharges(int billSeqNo, String phoneNumber) throws TelusAPIException;

	/**
	 * Returns an array of charges based on a period of time.
	 * 
	 * @param billSeqNo int
	 * @param from Date
	 * @param to Date
	 * @throws TelusAPIException
	 * @return Charge[]
	 */
	Charge[] getBilledCharges(int billSeqNo, Date from, Date to) throws TelusAPIException;

	/**
	 * Returns an array of charges based on a period of time.
	 * 
	 * @param billSeqNo int
	 * @param phoneNumber String
	 * @param from Date
	 * @param to Date
	 * 
	 * @return Charge[]
	 * 
	 * @throws TelusAPIException
	 */
	Charge[] getBilledCharges(int billSeqNo, String phoneNumber, Date from, Date to) throws TelusAPIException;

	/**
	 * 
	 * This method is re-instated as part of Handset Transparency April 2011 release along with the decommissioning of Billing Inquiry Service.
	 * @return CancellationPenalty
	 * @throws TelusAPIException
	 */
	CancellationPenalty getCancellationPenalty() throws TelusAPIException;

	ClientConsentIndicator[] getClientConsentIndicators() throws TelusAPIException;

	void setClientConsentIndicators(ClientConsentIndicator[] clientConsentIndicators) throws TelusAPIException;

	/**
	 * @deprecated
	 */
	double getInternationalDialingDepositAmount() throws TelusAPIException;

	/**
	 * Retrieve LMS letter requests based on date range criteria, level (account, subscriber or all), and optionally subscriber id.
	 * Search is limited to the maximum number of results as specified by maximum argument.
	 *
	 * <P>This method may invoke a remote method call.</P>
	 *
	 * @param from Date
	 * @param to Date
	 * @param level char
	 * @param subscriberId String
	 * @param maximum int
	 * 
	 * @return SearchResults
	 * 
	 * @throws TelusAPIException
	 */
//	SearchResults getLMSLetterRequests(Date from, Date to, char level, String subscriberId, int maximum) throws TelusAPIException;

	/**
	 * Retrieve future status change requests.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @return the list of future status change requests.
	 */
	public FutureStatusChangeRequest[] getFutureStatusChangeRequests() throws TelusAPIException;

	/**
	 * Retrieve credits, given a specific date range, level (all, subscriber, or account), reason, requested state of the credits (billed, unbilled or all), and optionally a subscriber id.
	 * Search results are limited to the maximum specified in the argument.
	 *
	 * <P>This method may involve a remote method call.</P>
	 *
	 * @param from Date
	 * @param to Date
	 * @param billState String
	 * @param reasonCode String
	 * @param level char
	 * @param subscriberId String
	 * @param maximum int
	 * 
	 * @return SearchResults
	 * 
	 * @throws TelusAPIException
	 */
	SearchResults getCredits(Date from, Date to, String billState, String reasonCode, char level, String subscriberId, int maximum) throws TelusAPIException;

	FollowUpStatistics getFollowUpStatistics() throws TelusAPIException;

	FollowUpStatistics getFollowUpStatistics(boolean refresh) throws TelusAPIException;

	boolean validTargetAccountType(char accountType, char accountSubType);

	/**
	 * Sends an email to the address specified.
	 *
	 * @param form			The form to be used for email.
	 * @param email		The email address.
	 * @param language	The language preference
	 *
	 * @throws TelusAPIException
	 */
	void sendEmail(final int form, String email, String language) throws TelusAPIException;

	/**
	 * Sends a fax to the fax number specified.
	 *
	 * @param form		 	The form to be used for fax
	 * @param faxNumber 	The fax number in the form of 4162340000
	 * @param language 	The language preference
	 *
	 * @throws TelusAPIException
	 */
	void sendFax(final int form, String faxNumber, String language) throws TelusAPIException;

	/**
    Retrieves subscribers on a given account that have (had) certain services
	 *
	 * <P>This method may involve a remote method call.</P>
	 *
	 * @param serviceCodes String
	 * @param includeExpired boolean
	 * 
	 * @return araay of ServiceSubscriberCount
	 * 
	 * @throws TelusAPIException
	 */

	ServiceSubscriberCount[] getServiceSubscriberCounts(String[] serviceCodes, boolean includeExpired) throws TelusAPIException;

	ProductSubscriberList[] getProductSubscriberLists();

	Credit[] getCreditByFollowUpId (int followUpId) throws TelusAPIException;

	/**
	 * This method is re-instated as part of Handset Transparency April 2011 release along with the decommissioning of Billing Inquiry Service.
	 * @param subscriberId
	 * @return CancellationPenalty[]
	 * @throws TelusAPIException
	 * @throws InvalidMultiSubscriberOperationException
	 */
	CancellationPenalty[] getCancellationPenaltyList(String[] subscriberId) throws TelusAPIException,InvalidMultiSubscriberOperationException;

	/**
	 * @deprecated Use {@link #cancelSubscribers(Date, String, char, String[], String[], String, ServiceRequestHeader)}
	 * @param effectiveDate
	 * @param reason
	 * @param depMethod
	 * @param subscriberId
	 * @param waiverReason
	 * @param comment
	 * @throws TelusAPIException
	 * @throws InvalidMultiSubscriberOperationException
	 */
	void cancelSubscribers(Date effectiveDate, String reason, char depMethod, String[] subscriberId, String[] waiverReason, String comment) throws TelusAPIException,InvalidMultiSubscriberOperationException;

	void suspendSubscribers(Date effectiveDate, String reason, String[] subscriberId, String comment) throws TelusAPIException,InvalidMultiSubscriberOperationException;

	void restoreSuspendedSubscribers(Date effectiveDate, String reason, String[] subscriberId, String comment) throws TelusAPIException,InvalidMultiSubscriberOperationException;

	/**
	 * Returns the PricePlanSubscriberCount[] of all subscribers participating in pools with airtime coverage type.
	 * Deprecated in favour of the more granular PostpaidAccount.getPoolingEnabledPricePlanSubscriberCount method.
	 * 
  	 * <P>NOTE: This method must NOT be called for large BANs (BANs with 200 or more subscribers).  Failure to comply may lead to KB DB degradation</P>
	 * 
	 * @deprecated
	 * 
	 * @see PostpaidAccount#getPoolingEnabledPricePlanSubscriberCount
	 */
	PricePlanSubscriberCount[] getAirtimeMinutePoolingEnabledPricePlanSubscriberCounts() throws TelusAPIException;
	
	/**
	 * Returns the PricePlanSubscriberCount[] of all subscribers participating in pools with long distance coverage type.
	 * Deprecated in favour of the more granular PostpaidAccount.getPoolingEnabledPricePlanSubscriberCount method.
	 * 
 	 * <P>NOTE: This method must NOT be called for large BANs (BANs with 200 or more subscribers).  Failure to comply may lead to KB DB degradation</P>
 	 * 
	 * @deprecated
	 *  
	 * @see PostpaidAccount#getPoolingEnabledPricePlanSubscriberCount
	 */
	PricePlanSubscriberCount[] getLDMinutePoolingEnabledPricePlanSubscriberCounts() throws TelusAPIException;
	
	DepositAssessedHistory[] getDepositAssessedHistory() throws TelusAPIException;

	DepositAssessedHistory[] getOriginalDepositAssessedHistory() throws TelusAPIException;

	ActivationOption[] getActivationOptions() throws TelusAPIException;

	/**
	 * Retrievs most recent Port Request for every subscriber
	 * @return araay of PortRequest
	 */
	PortRequest[] getPortRequests() throws PortRequestException, PRMSystemException, TelusAPIException;

	/**
	 * The setBrandId method is provided in order to allow interface applications to change
	 * the brand indicator when user memo text is not required (ie., creating a new account).
	 * In this case, the brand indicator will be saved when Account.save() is called, with no user memo text.
	 *
	 * @param brandId
	 */
	void setBrandId(int brandId);

	/**
	 * For cases where additional user memo text is required. This call will result in a remote call to the
	 * EJBs to specifically update the brand indicator.
	 *
	 * @param brandId int
	 * @param memoText String
	 */
	void updateBrand(int brandId, String memoText) throws TelusAPIException;

	/**
	 * This method is provided in order to allow interface applications to change the ban segmentation field.
	 * The segmentation field will be saved when Account.save() is called.
	 * 
	 * @param segment
	 * 
	 * @return the ban segment
	 */
	String setBanSegment (String segment);

	/**
	 * This method is provided in order to allow interface applications to change the ban sub-segmentation field.
	 * The sub-segmentation field will be saved when Account.save() is called.
	 * 
	 * @param subsegment
	 * 
	 * @return the ban sub-segment
	 */
	String setBanSubSegment (String subsegment);

	/**
	 * Applies a payment to this account using a cheque.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param amount the total amount to be debited.
	 * @param sourceID
	 * @param sourceType
	 *
	 * @exception PaymentFailedException if the payment was unsuccessful
	 * @exception UnknownObjectException if the sourceID-sourceType pair is invalid.
	 */
	void applyPayment(double amount, Cheque cheque, String sourceID, String sourceType) throws TelusAPIException, PaymentFailedException, UnknownObjectException;

	/**
	 * Checks if the specified categoty code exist on any of BAN's subscribers 
	 * The method couldn’t be used for corporate BANs.
	 *
	 * <P>This method may involve a remote method call.
	 * 
	 * @param categoryCode
	 */
	boolean isFeatureCategoryExistOnSubscribers(String categoryCode) throws TelusAPIException;

	/**
	 * Saves this account and write service request to SRPDS if header is not null
	 * 
	 * <P>This method may involve a remote method call.
	 *
	 * @param header ServiceRequestHeader - if not null, will be write to SRPDS, note this parameter will be used only for account type changes
	 */
	public void save(ServiceRequestHeader header) throws TelusAPIException, InvalidCreditCardException;
	
	/**
	 * Cancel the whole account and write service request to SRPDS if header is not null
	 *
	 * <P>This method may invoke a remote method call.</P>
	 *
	 * @param activityDate date
	 * @param reason String
	 * @param depositReturnMethod char
	 * @param waiver String
	 * @param memoText String
	 * @param header <code>ServiceRequestHeader</code> if not null, will be write to SRPDS
	 * 
	 * @throws TelusAPIException
	 */
	public void cancel(Date activityDate, String reason, char depositReturnMethod, String waiver, String memoText, ServiceRequestHeader header) throws TelusAPIException;

	/**
	 * Cancel the whole account and write service request to SRPDS if header is not null
	 * 
	 * <P>This method may involve a remote method call.
	 * 
	 * @param effectiveDate Date
	 * @param reason String
	 * @param depMethod char
	 * @param subscriberIds String[]
	 * @param waiverReason String[]
	 * @param comment String
	 * @param header <code>ServiceRequestHeader</code> if not null, will be write to SRPDS
	 * 
	 * @throws TelusAPIException
	 * @throws InvalidMultiSubscriberOperationException
	 */
	public void cancelSubscribers(Date effectiveDate, String reason,char depMethod, String[] subscriberIds, String[] waiverReason,String comment, ServiceRequestHeader header) throws TelusAPIException,InvalidMultiSubscriberOperationException ;

	/**
	 * Returns the ManualCreditCheckRequest object used for manual credit check.
	 */
	public ManualCreditCheckRequest getManualCreditCheckRequest() throws TelusAPIException;

	
	/**
	 * This method looks for charges on the account that matches all the input parameter. 
	 * If no matching charges are found, will return empty array.
	 * If number of matching charges exceed maximum requested, will only return up to the maximum record.
	 * If the maximum requested exceeds a system defined upper bound, throws exception; operation will not be executed
	 * 
	 * @param chargeCodes list of KB charge code system is searching for, cannot be null or empty, if so will throw #java.lang.IllegalArgumentException
	 * @param billState must be one of following 3 constants:
	 * 		Account.BILL_STATE_UNBILLED - retrieve charges from pending_charge table
	 * 		Account.BILL_STATE_BILLED -  retrieve charges from charge table
	 * 		Account.BILL_STATE_ALL - retrieve union of charges from both pending_charge AND charge table
	 * 
	 * @param level must be one of 3 constants
	 * 		ChargeType.CHARGE_LEVEL_ACCOUNT	- retrieve account level charges for a given ban
	 * 		ChargeType.CHARGE_LEVEL_SUBSCRIBER - retrieve subscriber level charges for a given ban/subscriber
	 * 		ChargeType.CHARGE_LEVEL_ALL - retrieve all account and subscriber level charges for a given ban
	 * 
	 * @param subscriberId this field is ignored when level is set to ChargeType.CHARGE_LEVEL_ACCOUNT or ChargeType.CHARGE_LEVEL_ALL. This field must
	 * 		not be null or empty String when level is set to ChargeType.CHARGE_LEVEL_SUBSCRIBER
	 * 
	 * @param from system will only search for charges created on or after this date, if null, throw java.lang.IllegalArgumentException
	 * @param to system will only search for charges created on or before this date, if null, throw java.lang.IllegalArgumentException
	 * @param maximum 	maximum number of records to be returned; even if there may be more matches found. No order guarantee.
	 * 		maximum must be <= system limit (??), else throw LimitExceededException
	 * @return Charge[]
	 * @throws LimitExceededException when maximum greater than system limit (??)
	 * @throws TelusAPIException
	 */
	public Charge[] getCharges(String[] chargeCodes, String billState, char level, String subscriberId, Date from, Date to, int maximum) throws LimitExceededException, TelusAPIException;
	/*
	 * Returns the boolean value NotificationSuppression Indicator
	 */
	public boolean getTransientNotificationSuppressionInd();
	/**
	 * 
	 * @param transientNotificaitonSuppressionInd
	 */
	void setTransientNotificationSuppressionInd(boolean transientNotificaitonSuppressionInd);
	
}
