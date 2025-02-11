/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import java.util.Date;

import com.telus.api.LimitExceededException;
import com.telus.api.TelusAPIException;
//import com.telus.api.reference.Letter;
import com.telus.api.reference.SubscriptionRoleType;

/**
 * <CODE>AccountSummary</CODE> represents the minimum intersection of all
 * account types.  It contains sufficient information to determine if this
 * account belongs to a particular client (except for birth date and SIN which
 * are specific to PostpaidConsumerAccount and PostpaidBusinessPersonalAccount).
 *
 * @see #getAccount
 *
 */
public interface AccountSummary {

	public static final char STATUS_CLOSED    = 'C';
	public static final char STATUS_CANCELED  = 'N';
	public static final char STATUS_OPEN      = 'O';
	public static final char STATUS_SUSPENDED = 'S';
	public static final char STATUS_TENTATIVE = 'T';
	
	public static final char STATUS_SEAT_GROUP_SUSPENDED = 'G';
	public static final char STATUS_SEAT_GROUP_CANCELED = 'H';

	public static final char ACCOUNT_TYPE_BUSINESS  = 'B';
	public static final char ACCOUNT_TYPE_CONSUMER  = 'I';
	public static final char ACCOUNT_TYPE_CORPORATE = 'C';

	public static final char ACCOUNT_SUBTYPE_PCS_REGULAR             = 'R';
	public static final char ACCOUNT_SUBTYPE_PCS_PERSONAL            = 'P';
	public static final char ACCOUNT_SUBTYPE_PCS_PREPAID             = 'Q';
	public static final char ACCOUNT_SUBTYPE_PCS_QUEBECTEL           = 'Y';
	public static final char ACCOUNT_SUBTYPE_PCS_WESTERN_PREPAID     = 'B';
	public static final char ACCOUNT_SUBTYPE_PCS_REGIONAL            = 'L';
	public static final char ACCOUNT_SUBTYPE_PCS_TELUS_EMPLOYEE      = 'E';
	public static final char ACCOUNT_SUBTYPE_PCS_TELUS_EMPLOYEE_NEW  = 'F';
	public static final char ACCOUNT_SUBTYPE_PCS_DEALER              = 'B';
	public static final char ACCOUNT_SUBTYPE_PCS_OFFICAL             = 'O';
	public static final char ACCOUNT_SUBTYPE_REGULAR_MEDIUM			 = 'X';

	public static final char ACCOUNT_SUBTYPE_IDEN_REGULAR        = '1';
	public static final char ACCOUNT_SUBTYPE_IDEN_TELUS_EMPLOYEE = '2';
	public static final char ACCOUNT_SUBTYPE_IDEN_PERSONAL       = '3';
	public static final char ACCOUNT_SUBTYPE_IDEN_DEALER         = '4';

	public static final char ACCOUNT_SUBTYPE_PAGER_REGULAR = 'M';
	public static final char ACCOUNT_SUBTYPE_PAGER_BOXED   = 'J';

	public static final char ACCOUNT_SUBTYPE_AUTOTEL_REGULAR = 'W';
	public static final char ACCOUNT_SUBTYPE_AUTOTEL_EARS    = '9';

	public static final char ACCOUNT_CATEGORY_REGIONAL = 'R';
	public static final char ACCOUNT_CATEGORY_NATIONAL = 'N';

	// PCS Corporate subtypes
	public static final char ACCOUNT_SUBTYPE_CORP_PCS_FEDERAL_GOVERNMENT = 'B';
	public static final char ACCOUNT_SUBTYPE_CORP_PCS_ENTERPRISE         = 'Q';
	public static final char ACCOUNT_SUBTYPE_CORP_PCS_CORPORATE          = 'X';
	public static final char ACCOUNT_SUBTYPE_CORP_PCS_ABORIGINAL         = 'A';
	public static final char ACCOUNT_SUBTYPE_CORP_PCS_EMPLOYEE           = 'E';
	public static final char ACCOUNT_SUBTYPE_CORP_PCS_FUSION_EAST_CONV   = 'H';
	public static final char ACCOUNT_SUBTYPE_CORP_PCS_KEY                = 'K';
	public static final char ACCOUNT_SUBTYPE_CORP_PCS_OFFICIAL           = 'O';
	public static final char ACCOUNT_SUBTYPE_CORP_PCS_CNBS               = 'C';
	public static final char ACCOUNT_SUBTYPE_CORP_PCS_GOVERNMENT         = 'G';
	public static final char ACCOUNT_SUBTYPE_CORP_PCS_INDIVIDUAL         = 'I';
	public static final char ACCOUNT_SUBTYPE_CORP_PCS_NATIONAL_STRATEGIC = 'N';
	public static final char ACCOUNT_SUBTYPE_CORP_PCS_REGIONAL_STRATEGIC = 'L';
	public static final char ACCOUNT_SUBTYPE_CORP_PCS_TMI_AFFILIATE      = 'U';
	public static final char ACCOUNT_SUBTYPE_CORP_PCS_TMI_DIVISION       = 'V';

	// Mike Corporate subtypes
	public static final char ACCOUNT_SUBTYPE_CORP_IDEN_REGULAR            = '1';
	public static final char ACCOUNT_SUBTYPE_CORP_IDEN_PUBLIC_SAFETY      = '2';
	public static final char ACCOUNT_SUBTYPE_CORP_IDEN_REGIONAL_STRATEGIC = '3';
	public static final char ACCOUNT_SUBTYPE_CORP_IDEN_NATIONAL_STRATEGIC = '4';
	public static final char ACCOUNT_SUBTYPE_IDEN_PRIVATE_NETWORK_PLUS    = '5';
	public static final char ACCOUNT_SUBTYPE_CORP_IDEN_DURHAM_POLICE      = '6';
	public static final char ACCOUNT_SUBTYPE_CORP_IDEN_TMI_AFFILIATE      = '7';
	public static final char ACCOUNT_SUBTYPE_CORP_IDEN_TMI_DIVISION       = '8';
	public static final char ACCOUNT_SUBTYPE_IDEN_GOVERNMENT              = 'J';
	public static final char ACCOUNT_SUBTYPE_IDEN_FEDERAL_GOVENMENT       = 'M';
	public static final char ACCOUNT_SUBTYPE_IDEN_INDIVIDUAL              = 'P';
	public static final char ACCOUNT_SUBTYPE_IDEN_ENTERPRISE              = 'R';
	
	public static final char ACCOUNT_SUBTYPE_PCS_ANYWHERE_REGULAR = 'A';
	public static final char ACCOUNT_SUBTYPE_PCS_ANYWHERE_PERSONAL = 'N';
	public static final char ACCOUNT_SUBTYPE_CORP_PCS_ANYWHERE = 'Y';
	public static final char ACCOUNT_SUBTYPE_CORP_RESELLER = 'Z';
	
	public static final char ACCOUNT_SUBTYPE_PCS_CONNECT_REGULAR = 'F';
	public static final char ACCOUNT_SUBTYPE_PCS_CONNECT_PERSONAL = 'G';

	public static final String PORT_RESTRICTED      = "Y";
	public static final String PORT_NOT_RESTRICTED  = "N";

	public static final String  BILL_SUPPRESSION_AT_ACTIVATION_YES     = "Y";
	public static final String  BILL_SUPPRESSION_AT_ACTIVATION_NO      = "N";
	public static final String  BILL_SUPPRESSION_AT_ACTIVATION_UNKNOWN = "UNKNOWN";
	
	public static final String PROCESS_TYPE_ACCOUNT_UPDATE="AccountUpdate";
	


   


	/**
	 * Returns <CODE>true</CODE> if the status of the account is STATUS_SUSPENDED and the 
	 * status activity reason code is one of these : 'SFI', 'SNP' or 'SNP1' ; otherwise returns
	 * <CODE>false</CODE>.
	 *  
	 */
	public boolean isSuspendedDueToNonPayment();

	/**
	 * Returns the BAN ID for the Account
	 * 
	 */
	int getBanId();

	/**
	 * Returns the Customer ID
	 * 
	 */
	int getCustomerId();

	/** Refactored from Account interface for Nov 2010 Scorpion project **/
	
	/**
	 * This returns null for the lightweight findLwAccountByPhoneNumber method.
	 */
	String getDealerCode();
	
	/**
	 * This returns null for the lightweight findLwAccountByPhoneNumber method.
	 */
	String getSalesRepCode();
	void setSalesRepCode(String salesRepCode);
	void setDealerCode(String dealerCode);
	/** Refactored from Account interface for Nov 2010 Scorpion project **/
	
	/**
	 * Returns the Status of the Account as one of the STATUS_xxx constants
	 *  
	 */
	char getStatus();

	/**
	 * Returns a Date value as when the current Account Status was set 
	 * 
	 */
	Date getStatusDate();


	/**
	 * Returns the displayable billing name for this account. For consumer accounts,
	 * it's firstName + " " + lastName; for business accounts it's legalBusinessName.
	 * 
	 */
	String getFullName();

	/**
	 * Returns the displayable billing address lines for this account.  The lines may include
	 * any or all of: streetNumber + " " + streetName + " suite " + suite,
	 * "PO Box " + poBox, "RR  " + rr, city + " " + province, postalCode, and
	 * country.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.  It may also be freely modified.
	 */
	String[] getFullAddress();

	/**
	 * Returns <CODE>true</CODE> if this is a Autotel account, otherwise
	 * <CODE>false</CODE>.
	 *
	 * @see #isPCS
	 * @see #isPager
	 */
	@Deprecated
	boolean isAutotel();

	/**
	 * Returns <CODE>true</CODE> if this is a brand, otherwise
	 * <CODE>false</CODE>.
	 *
	 * @param brandId the brand id.
	 */
	boolean isBrand(int brandId);

	/**
	 * Returns <CODE>true</CODE> if this is a Mike/IDEN account, otherwise
	 * <CODE>false</CODE>.
	 *
	 * @see #isPCS
	 * @see #isPager
	 */
	@Deprecated
	boolean isIDEN();

	/**
	 * Returns <CODE>true</CODE> if this is a Cellular/PCS account, otherwise
	 * <CODE>false</CODE>.
	 *
	 * @see #isIDEN
	 * @see #isPager
	 */
	boolean isPCS();

	/**
	 * Returns <CODE>true</CODE> if this is a Pager account, otherwise
	 * <CODE>false</CODE>.
	 *
	 * @see #isIDEN
	 * @see #isPCS
	 * @deprecated
	 */
	boolean isPager();

	/**
	 * Returns <CODE>true</CODE> if this is a Postpaid Employee account, otherwise
	 * <CODE>false</CODE>.
	 * 
	 */
	boolean isPostpaidEmployee();

	/**
	 * Returns <CODE>true</CODE> if this is a Postpaid Consumer account, otherwise
	 * <CODE>false</CODE>.
	 * 
	 */
	boolean isPostpaidConsumer();

	/**
	 * Returns <CODE>true</CODE> if this is a Postpaid Boxed Consumer account, otherwise
	 * <CODE>false</CODE>.
	 * 
	 */
	boolean isPostpaidBoxedConsumer();

	/**
	 * Returns <CODE>true</CODE> if this is a Postpaid Business Regular account, otherwise
	 * <CODE>false</CODE>.
	 * 
	 */
	boolean isPostpaidBusinessRegular();

	/**
	 * Returns <CODE>true</CODE> if this is a Postpaid Business Personal account, otherwise
	 * <CODE>false</CODE>.
	 * 
	 */
	boolean isPostpaidBusinessPersonal();

	/**
	 * Returns <CODE>true</CODE> if this is a Postpaid Business Dealer account, otherwise
	 * <CODE>false</CODE>.
	 * 
	 */
	boolean isPostpaidBusinessDealer();

	/**
	 * Returns <CODE>true</CODE> if this is a Postpaid Business Official account, otherwise
	 * <CODE>false</CODE>.
	 * 
	 */
	boolean isPostpaidBusinessOfficial();

	/**
	 * Returns <CODE>true</CODE> if this is a Postpaid Official account, otherwise
	 * <CODE>false</CODE>.
	 * 
	 */
	boolean isPostpaidOfficial();

	/**
	 * Returns <CODE>true</CODE> if this is a Prepaid Consumer account, otherwise
	 * <CODE>false</CODE>.
	 * 
	 */
	boolean isPrepaidConsumer();

	/**
	 * Returns <CODE>true</CODE> if this is a Quebectel Prepaid account, otherwise
	 * <CODE>false</CODE>.
	 * 
	 */
	boolean isQuebectelPrepaidConsumer();

	/**
	 * Returns <CODE>true</CODE> if this is a Western Prepaid account, otherwise
	 * <CODE>false</CODE>.
	 * 
	 */
	boolean isWesternPrepaidConsumer();

	//boolean isTelusEmployee();

	//boolean isDealer();

	/**
	 * Returns <CODE>true</CODE> if this is a Corporate Regular account, otherwise
	 * <CODE>false</CODE>.
	 * 
	 */
	boolean isCorporateRegular();

	/**
	 * Returns <CODE>true</CODE> if this is a Corporate Regional account, otherwise
	 * <CODE>false</CODE>.
	 * 
	 */
	boolean isCorporateRegional();

	/**
	 * Returns <CODE>true</CODE> if this is a Corporate Private Network Plus account, otherwise
	 * <CODE>false</CODE>.
	 * 
	 */
	boolean isCorporatePrivateNetworkPlus();

	/**
	 * Returns a TaxExemption object created using current Account info . An exception will
	 * be thrown if the Account object info cannot be loaded .  
	 * 
	 * @throws TelusAPIException
	 */
	TaxExemption getTaxExemption() throws TelusAPIException;

	//boolean isCorporatePublicSafety();

	/**
	 * Returns the brand ID (ie. telus, Amp'd)
	 * 
	 */
	int getBrandId();

	/**
	 * Returns <CODE>true</CODE> if this is an Amp'd account, otherwise false.
	 */
	@Deprecated
	boolean isAmpd();

	/**
	 * Returns <CODE>true</CODE> if this is a postpaid account, otherwise this
	 * account is prepaid.
	 *
	 */
	boolean isPostpaid();

	/**
	 *  Returns one of the ACCOUNT_TYPE_xxx constants.
	 */
	char getAccountType();

	/**
	 *  Returns one of the ACCOUNT_SUBTYPE_xxx constants.
	 */
	char getAccountSubType();

	/**
	 * Return the PIN of the Account
	 * 
	 */
	String getPin();

	/**
	 * Returns the date this account was created.  This method will return
	 * <CODE>null</CODE> if this account has not yet been saved.
	 *
	 */
	Date getCreateDate();

	/**
	 * Returns the date this account was moved to an active status, in other
	 * words, the first time a subscriber was activated.  This method will return
	 * <CODE>null</CODE> if this account has not yet been activated.
	 */
	Date getStartServiceDate();

	/**
	 * Returns Activity Code of the Account Status
	 *
	 * @see #getStatusActivityReasonCode
	 */
	String getStatusActivityCode();

	/**
	 * Returns Activity Reason Code of the Account Status
	 *
	 * @see #getStatusActivityCode
	 */
	String getStatusActivityReasonCode();

	/**
	 * Returns the complete account for this summary.
	 *
	 * <P>The returned object will be a specific type of account (i.e. IDENPostpaidConsumerAccount,
	 * PCSPostpaidBusinessPersonalAccount, etc.).  Use the isXXX() methods, the getAccountType() &
	 * getAccountSubType() methods or <CODE>instanceof</CODE> to determine which one.
	 *
	 * <P>This method may involve a remote method call.
	 */
	Account getAccount() throws TelusAPIException;

	/**
	 * Returns the most recently added, non-cancelled, subscribers for this account upto the
	 * specified maximum.
	 *
	 * The returned object will be a specific type of Subscriber (i.e. PCSSubscriber or
	 * IDENSubscriber).  Use the isIDEN() or isPCS() methods or <CODE>instanceof</CODE>
	 * to determine which one.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param maximum the total number of subscribers to be returned.
	 */
	Subscriber[] getSubscribers(int maximum) throws TelusAPIException;

	/**
	 * Returns all ported out subscribers for this account up to the
	 * specified maximum.
	 *
	 * The returned object will be a specific type of Subscriber (i.e. PCSSubscriber or
	 * IDENSubscriber).  Use the isIDEN() or isPCS() methods or <CODE>instanceof</CODE>
	 * to determine which one.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param maximum the total number of subscribers to be returned.
	 */
	Subscriber[] getPortedSubscribers(int maximum) throws TelusAPIException;

	/**
	 * Returns the most recently added subscribers for this account upto the
	 * specified maximum.
	 *
	 * The returned object will be a specific type of Subscriber (i.e. PCSSubscriber or
	 * IDENSubscriber).  Use the isIDEN() or isPCS() methods or <CODE>instanceof</CODE>
	 * to determine which one.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param maximum the total number of subscribers to be returned.
	 */
	Subscriber[] getSubscribers(int maximum, boolean includeCancelled) throws TelusAPIException;

	/**
	 * Returns the most recently added subscribers' phone number for this account upto the
	 * specified maximum.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param maxNumbers the total number of phone numbers to be returned.
	 */
	String [] getActiveSubscriberPhoneNumbers(int maxNumbers) throws TelusAPIException;

	/**
	 * Return the list of subscriber phone numbers on this account filtered by specified subscriber status upto the
	 * specified maximum.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param status the subscriber's status ( see one of the Subscriber.STATUS_xxx constants )
	 * @param maximum the total number of phone numbers to be returned.
	 */
	String[] getSubscriberPhoneNumbersByStatus(char status, int maximum) throws TelusAPIException;

	/**
	 * Returns the most recently suspended subscribers' phone number for this account upto the
	 * specified maximum.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param maxNumbers the total number of phone numbers to be returned.
	 */
	String[] getSuspendedSubscriberPhoneNumbers(int maxNumbers) throws TelusAPIException;

	/**
	 * Saves the PIN on an existing account and updates the property on this instance.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @exception UnknownBANException if this account doesn't already exist.
	 */
	void savePin(String newPin) throws UnknownBANException, TelusAPIException;

	/**
	 * Returns the most recent credit check memo for this account.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @exception TelusAPIException if no account with this id exists, no credit
	 *            check memo exists on this account, or other uncaught exception.
	 *
	 */
	public Memo getLastCreditCheckMemo() throws TelusAPIException;

	/**
	 * Creates a new unsaved memo associated with this account.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @see Memo#create
	 */
	Memo newMemo() throws TelusAPIException;

	/**
	 * Creates a new unsaved FollowUp associated with this account.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @see FollowUp#create
	 */
	FollowUp newFollowUp() throws TelusAPIException;

	/**
	 * Creates a new unsaved Charge associated with this account.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @see Charge#apply
	 */
	Charge newCharge() throws TelusAPIException;

	/**
	 * Creates a new unsaved and untaxable Credit associated with this account.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @see Credit#apply
	 */
	Credit newCredit() throws TelusAPIException;

	/**
	 * Deprecated in favour of newCredit(char taxOption).
	 *
	 * Creates a new unsaved, and possible taxable, Credit associated with this
	 * account.  A credit may still not be taxed when the <CODE>taxable</CODE>
	 * argument is set to <CODE>true</CODE> if the account is exempt.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param taxable <CODE>true</CODE> if this credit should be taxed.
	 *
	 * @see #newCredit(char taxOption)
	 */
	Credit newCredit(boolean taxable) throws TelusAPIException;

	/**
	 * Creates a new unsaved and possibly taxable Credit associated with this
	 * account.  Sets the applicable taxes for this Credit based on the
	 * <CODE>taxOption</CODE> argument.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param taxOption - the applicable taxes for this credit.
	 *
	 * @see Credit#apply
	 * @see AccountSummary#isGSTExempt()
	 * @see AccountSummary#isPSTExempt()
	 * @see AccountSummary#isHSTExempt()
	 *
	 */
	Credit newCredit(char taxOption) throws TelusAPIException;

	/**
	 * Creates a new unsaved Discount associated with this account.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @see Discount#apply
	 */
	Discount newDiscount() throws TelusAPIException;

	/**
	 * Returns the most recent memo of the specified type for this BAN.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @exception TelusAPIException if no memo of the type exists on this
	 *            account, or other uncaught exception.
	 *
	 */
	Memo getLastMemo(String memoType) throws TelusAPIException;

	/* *
	 * Saves the Status on an existing account and updates the property on this instance.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @exception UnknownBANException if this account doesn't already exist.
	 * /
	void saveStatus(String newStatus) throws UnknownBANException, TelusAPIException;
	 */

	/**
	 * Returns <CODE>true</CODE> if this account should never be charged GST,
	 * even when the underlying charge, credit, etc. requirs it.
	 *
	 */
	boolean isGSTExempt();

	/**
	 * Returns <CODE>true</CODE> if this account should never be charged PST,
	 * even when the underlying charge, credit, etc. requirs it.
	 *
	 */
	boolean isPSTExempt();

	/**
	 * Returns <CODE>true</CODE> if this account should never be charged HST,
	 * even when the underlying charge, credit, etc. requirs it.
	 *
	 */
	boolean isHSTExempt();

	/**
	 * Returns <CODE>true</CODE> if this account is linked to others in a
	 * corporate hierarchy.
	 *
	 */
	boolean isCorporateHierarchy();

	/**
	 * Returns <CODE>true</CODE> if this is a Pager Prepaid Consumer account, otherwise
	 * <CODE>false</CODE>.
	 * @deprecated
	 */
	boolean isPagerPrepaidConsumeraccount();

	/**
	 * Returns <CODE>true</CODE> if this is a Postpaid Consumer account, otherwise
	 * <CODE>false</CODE>.
	 * 
	 */
	boolean isPCSPostpaidCorporateRegularAccount();

	/**
	 * Returns the code of the Corporate Account Rep
	 * 
	 */
	String getCorporateAccountRepCode();

	/**
	 * Returns in chronological order of the last <code>count</code> number of memos for this account
	 * (or one of its subscribers).
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @return Memo[] -- Never null.
	 */
	Memo[] getMemos(int count) throws TelusAPIException;

	/**
	 * Returns in chronological order the last <code>count</code> number of followups for this account
	 * (or one of its subscribers).
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @return FollowUp[] -- Never null.
	 */
	FollowUp[] getFollowUps(int count) throws TelusAPIException;

	/**
	 * Returns Subscriber Info by Subscriber Id
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @exception UnknownSubscriberException if this subscriber doesn't exist.
	 *
	 */
	Subscriber getSubscriber(String subscriberId) throws UnknownSubscriberException, TelusAPIException;

	/**
	 * Returns Subscriber Info by Phone Number
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @exception UnknownSubscriberException if this subscriber doesn't exist.
	 *
	 */
	Subscriber getSubscriberByPhoneNumber(String phoneNumber) throws UnknownSubscriberException, TelusAPIException;
	
	/**
	 * Returns Subscriber Info by Phone Number and PhoneNumberSearchOption (PhoneNumberSearchOption  is having the flags searchbyWireless,searchByVOIP,searchByTollFree,searchByHSIA. we default searchbyWireless set to true,all remain flags set to false)
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @exception UnknownSubscriberException if this subscriber doesn't exist.
	 *
	 */
	 public Subscriber getSubscriberByPhoneNumber(String phoneNumber,PhoneNumberSearchOption phoneNumberSearchOption) throws UnknownSubscriberException, TelusAPIException;

	/**
	 * Returns Special Instructions Memo
	 *
	 * <P>This method may involve a remote method call.

	 *
	 */
	Memo getSpecialInstructionsMemo()  throws TelusAPIException;

	/**
	 * Returns the names of individuals allowed to interact with this account.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 */
	ConsumerName[] getAuthorizedNames() throws TelusAPIException;

	/**
	 * Saves the list of individuals allowed to interact with this account, deleting the old list.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @throws UnknownBANException if this account doesn't already exist.
	 */
	void saveAuthorizedNames(ConsumerName[] authorizedNames) throws UnknownBANException, TelusAPIException;

	/**
	 * Returns <CODE>true</CODE> if this is a FIDO Conversion account, otherwise
	 * <CODE>false</CODE>.
	 * @deprecated
	 */
	boolean isFidoConversion();

	/**
	 * Returns a list of all valid Subscription Role Types from the Reference DB . If the Account is
	 * not a Corporate Account , the SUBSCRIPTION_ROLE_ENTERPRISE constant will not be available as
	 * a valid constant .
	 * 
	 * @throws TelusAPIException
	 */
	SubscriptionRoleType[] getValidSubscriptionRoleTypes() throws TelusAPIException;

	/**
	 * Returns a new LMSLetterRequest object using the info from the Letter object passed
	 * as parameter 
	 * 
	 * @param letter	Used to build the new LMSLetterRequest object
	 * 
	 * @throws TelusAPIException
	 */
//	LMSLetterRequest newLMSLetterRequest(Letter letter) throws TelusAPIException;

	/**
	 * Return an existing Hotline Phone Number if one is associated to the Account
	 * 
	 * @throws TelusAPIException
	 */
	String getHotlinedSubscriberPhoneNumber() throws TelusAPIException;

	/**
	 * Returns the BAN corporate name. This might involve a remote call.
	 * 
	 * @throws TelusAPIException
	 */
	String getCorporateName() throws TelusAPIException;

	/**
	 * Returns <CODE>true</CODE> if this is a Postpaid Corporate Personal account, otherwise
	 * <CODE>false</CODE>.
	 * 
	 */
	boolean isPostpaidCorporatePersonal();

	/**
	 * Returns <CODE>true</CODE> if this is a Postpaid Corporate Regular account, otherwise
	 * <CODE>false</CODE>.
	 * 
	 */
	boolean isPostpaidCorporateRegular();
	
	/**
	 * Returns <CODE>true</CODE> if this is a Postpaid Business Connect account, otherwise
	 * <CODE>false</CODE>.
	 * 
	 */
	boolean isPostpaidBusinessConnect();

	/**
	 * Checks Transfer Blocking indicator for Wireless to Wireless port requests
	 * Returns true if in the port out is restricted at BAN level.
	 * 
	 * @throws TelusAPIException
	 */
	String getPortProtectionIndicator () throws TelusAPIException ;

	/**
	 * Applies the Transfer Blocking indicator for Wireless to Wireless port requests
	 * at BAN level.
	 * 
	 * @throws TelusAPIException
	 */

	void updatePortRestriction(boolean restrictPort) throws TelusAPIException;

	/**
	 * Returns the associated BAN Segment
	 * Value source from gl_segment in billing_account table  
	 * 
	 * See Subscriber.getUserValueRating() for client segment
	 * 
	 * @throws TelusAPIException
	 */
	String getBanSegment() throws TelusAPIException;

	/**
	 * Returns the associated BAN Sub Segment
	 * Value source from gl_subsegment in billing_account table
	 * 
	 * @throws TelusAPIException
	 */
	String getBanSubSegment() throws TelusAPIException;

	/**
	 * Returns the array of SMS/E-mail for e-Bill Notifications.
	 * 
	 * @throws TelusAPIException
	 * @deprecated replaced by BillNotificationManagementService_v3_0.getBillNotificationInfo
	 */
	BillNotificationContact[] getBillNotificationContacts() throws TelusAPIException;


	/**
	 * Returns the last EBill Notification being sent . Should return null 
	 * if the record was not found
	 * 
	 * @throws TelusAPIException
	 * @deprecated replace  - TBD – This is only consumed by Smart Desktop
	 */
	BillNotificationContact getLastEBillNotificationSent() throws TelusAPIException;

	/**
	 * Returns the Paper Bill Supression At Activation Indicator . Will return Y/N or UNKNOWN but
	 * should return null, if the record was not found
	 * 
	 * @throws TelusAPIException
	 * @deprecated replace - TBD – This is only consumed by Smart Desktop
	 */
	String getPaperBillSupressionAtActivationInd() throws TelusAPIException;

	/**
	 * Returns the most recently added subscribers for this account up to the given maximum. Since the This method 
	 * is specifically optimized for performance and memory usage, the returned array is LightWeightSubscriber array
	 * rather than Subscriber array. The LightWeightSubscriber only holds very little key informations
	 * about subscriber. 
	 * 
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method involves a remote method call.
	 *
	 * @param maximum the total number of subscribers to be returned. The value has to be positive number.
	 * @param includeCancelled indicate should result contain canceled subscriber
	 * @return LightWeightSubscriber[]
	 * @throws TelusAPIException
	 * 
	 * @see LightWeightSubscriber
	 */
	LightWeightSubscriber[] getLightWeightSubscribers(int maximum, boolean includeCancelled ) throws TelusAPIException;
	
	/**
	 * This method returns all the subscribers on an account for the given familyType.
	 * familyType is a code in the SOC_FAMILY_GROUP.family_type table.  If a subscriber
	 * on this account has a SOC with a SOC group belonging to the specified familyType,
	 * then the subscriber is returned.  The subscriber is returned if its status is active
	 * or suspended (Subscriber.status == SubscriberStatus.A || (Subscriber.status == 
	 * SubscriberStatus.S))
	 *
	 * @param 	familyType - value from SOC_FAMILY_GROUP.family_type table. 
	 * 			Use constants ServiceSummary.FAMILY_TYPE_CODE_*
	 * @param   effectiveDate  Effective Date 
	 * @return  array of subscriberId.  Array of 0 length if no subscribers. Value is never null
	 * @throws LimitExceededException if too many subscribers
	 */
	public String[] getSubscriberIdsByServiceGroupFamily(String familyType,Date effectiveDate ) throws LimitExceededException, TelusAPIException;

}
