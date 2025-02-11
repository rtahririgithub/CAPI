/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;


import java.util.Date;

import com.telus.api.BrandNotSupportedException;
import com.telus.api.TelusAPIException;
import com.telus.api.reference.FollowUpCriteria;
import com.telus.api.reference.PrepaidEventType;


/**
 * <CODE>AccountManager</CODE> provides a simple interface into the
 * client account management system.  It exposes methods to instantiate
 * and locate accounts and subscribers.
 *
 *
 *
 * @see AccountSummary
 * @see Account
 * @see Subscriber
 *
 */
public interface AccountManager extends java.io.Serializable {

  public static final String PRODUCT_TYPE_PCS = "C";
  public static final String PRODUCT_TYPE_PAGER = "P";
  public static final String PRODUCT_TYPE_IDEN = "I";

  public static final String EQUIPMENT_TYPE_ANALOG = "A";
  public static final String EQUIPMENT_TYPE_DIGITAL = "D";
  public static final String EQUIPMENT_TYPE_1xRTT= "3";

  public static final int ACTIVATION_TYPE_NORMAL              = 0;
  public static final int ACTIVATION_TYPE_VIRTUAL_NO_CHARGE   = 1;
  public static final int ACTIVATION_TYPE_VIRTUAL_WITH_CHARGE = 2;
  public static final int ACTIVATION_TYPE_WITH_ESN            = 3;
  public static final int ACTIVATION_TYPE_WITH_P2P            = 4;
  public static final int ACTIVATION_TYPE_INTERACT_ONLINE_DEBIT = 5; // new activation type for interac online
  public static final int ACTIVATION_TYPE_CREDIT_CARD = 6;
  public static final int ACTIVATION_TYPE_AIRTIME_CARD = 7;



  public static final String SEARCH_NAME_TYPE_BILLING = "B";
  public static final String SEARCH_NAME_TYPE_USER = "U";
  public static final String SEARCH_ALL = "*";
  public static final char Search_All = '*';

  public static final int NUMERIC_TRUE = 0;
  public static final int NUMERIC_FALSE = 1;  

  // constants for Name Search functionality
  public static final String ACCOUNT_STATUS_ALL = "*";
  public static final String ACCOUNT_TYPE_ALL = "*";

  /**
   * Returns a new instance of CreditCard for use in <CODE>payDeposit</CODE>
   * and <CODE>payBill</CODE>.
   *
   * This method should only used; If Account not exist in knowbility or account holder using others credit Card
   * Address and Holder Name mandatory
   *
   * <P>This method may involve a remote method call.
   */
  CreditCard newCreditCard(String holderName) throws TelusAPIException;

  /**
   * Returns a new instance of CreditCard for use in <CODE>payDeposit</CODE>
   * and <CODE>payBill</CODE>.
   * This method should only used; If Account not exist in knowbility or credit card not belong to the account holder
   *
   *
   * <P>This method may involve a remote method call.
   *
   * @see Account#payDeposit
   * @see Account#payBill
   *
   */
  CreditCard newCreditCard(Account account) throws TelusAPIException;

  /**
   * Returns a new instance of PaymentMethod for use in <CODE>savePaymentMethod</CODE>.
   * @param account that Credit card going to apply the charge.
   *
   * <P>This method may involve a remote method call.
   *
   * @see PostpaidBusinessRegularAccount
   * @see PostpaidConsumerAccount
   *
   */
  PaymentMethod newPaymentMethod(Account account) throws TelusAPIException;
  
  /**
   * Returns a new instance of PaymentMethod for use in <CODE>savePaymentMethod</CODE>.
   * This method differs from the above method in that the PaymentMethod instance is
   * based on the old PaymentMethod and contains the CC info / check info required by
   * Amdocs.
   * 
   * @param account			the current account.
   * @param oldPaymentMethod 	the old payment method instance.
   *
   * <P>This method may involve a remote method call.
   *
   * @see PostpaidBusinessRegularAccount
   * @see PostpaidConsumerAccount
   *
   */
  PaymentMethod newPaymentMethod(Account account, PaymentMethod oldPaymentMethod) throws TelusAPIException;

  /**
   * Returns a new instance of ConsumerName for use in <CODE>saveAuthorizedNames</CODE>.
   *
   * <P>This method may involve a remote method call.
   *
   * @see AccountSummary#saveAuthorizedNames
   *
   */
  ConsumerName newAuthorizedName() throws TelusAPIException;

  /**
   * Returns a new unsaved account.
   */
  PCSPostpaidEmployeeAccount newPCSPostpaidEmployeeAccount() throws TelusAPIException;

  /**
   * Returns a new unsaved account.
   */
  PCSPostpaidConsumerAccount newPCSPostpaidConsumerAccount() throws TelusAPIException;


  /**
   * Returns a new unsaved account.
   * @deprecated use {@link #newPCSPostpaidConsumerAccount()}
   */
  PCSPostpaidConsumerAccount newPCSPostpaidConsumerAccount(boolean isFidoConversion) throws TelusAPIException;

  /**
   * Returns a new unsaved account.
   */
  PCSPostpaidBusinessRegularAccount newPCSPostpaidBusinessRegularAccount() throws TelusAPIException;

  /**
   * Returns a new unsaved account.
   */
  PCSPostpaidBusinessPersonalAccount newPCSPostpaidBusinessPersonalAccount() throws TelusAPIException;

  /**
   * Returns a new unsaved account.
   */
  PCSPostpaidBusinessOfficialAccount newPCSPostpaidBusinessOfficialAccount(boolean isFidoConversion) throws TelusAPIException;
  /**
   * Returns a new unsaved account.
   */
   PCSPostpaidBusinessDealerAccount newPCSPostpaidBusinessDealerAccount(boolean isFidoConversion) throws TelusAPIException;

   /**
   * Returns a new unsaved account.
   * @deprecated use {@link #newPCSPostpaidBusinessRegularAccount()}
   */
  PCSPostpaidBusinessRegularAccount newPCSPostpaidBusinessRegularAccount(boolean isFidoConversion) throws TelusAPIException;

  /**
   * Returns a new unsaved account.
   * @deprecated use {@link #newPCSPostpaidBusinessPersonalAccount()}
   */
  PCSPostpaidBusinessPersonalAccount newPCSPostpaidBusinessPersonalAccount(boolean isFidoConversion) throws TelusAPIException;

  /**
   * This method will return a new unsaved instance of PCSPostpaidCorporateRegularAccount.
   * On commit, system will auto-assign the least used bill cycle to account.
   * 
   * @param accountSubtype
   * 
   * @throws TelusAPIException
   *
   */
  PCSPostpaidCorporateRegularAccount newPCSPostpaidCorporateRegularAccount(char accountSubtype) throws TelusAPIException;
  
  /**
   * This method will create a new unsaved instance of PCSPostpaidCorporateRegularAccount
   * allowing calling application to specify the bill cycle for the account.
   *
   * @param accountSubtype
   * @param billCycle
   * 
   * @throws InvalidBillCycleException
   * @throws TelusAPIException
   *
   */
  PCSPostpaidCorporateRegularAccount newPCSPostpaidCorporateRegularAccount(char accountSubtype, int billCycle) throws TelusAPIException, InvalidBillCycleException;
  
  /**
   * Validates a serialNumber, and depending on the activationType, validates an activationCode
   * or a creditCard, then returns a new unsaved Subscriber with the parameters
   * attached.
   *
   * <P>This method may involve a remote method call.
   *
   * @param serialNumber the serial number to validate.
   * @param activationType one of: ACTIVATION_TYPE_NORMAL, ACTIVATION_TYPE_VIRTUAL_NO_CHARGE, or
   *        ACTIVATION_TYPE_VIRTUAL_WITH_CHARGE.
   * @param activationCode the code to validate if activationType == ACTIVATION_TYPE_NORMAL, otherwise <CODE>null</CODE>.
   * @param creditCard the credit card to debit if activationType == ACTIVATION_TYPE_VIRTUAL_WITH_CHARGE, otherwise <CODE>null</CODE>.
   * @param auditHeader this field is mandatory is creditCard is not null.
   *
   * @exception UnknownSerialNumberException the number does not exist in our datastore.
   * @exception SerialNumberInUseException the number is already being used by a subscriber.
   * @exception InvalidActivationCodeException the code is unknown or already in use.
   * @exception InvalidCreditCardException the card cannot be debited.
   * 
   */
  PCSPrepaidConsumerAccount newPCSPrepaidConsumerAccount(String serialNumber, int activationType, String activationCode, CreditCard creditCard, String businessRole, AuditHeader auditHeader)
  throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, InvalidActivationCodeException,
  InvalidCreditCardException, InvalidActivationTypeException, TelusAPIException;

  PCSPrepaidConsumerAccount newPCSPrepaidConsumerAccount(String serialNumber, int activationType, String activationCode, CreditCard creditCard, String businessRole, boolean isFidoConversion, AuditHeader auditHeader )
  throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, InvalidActivationCodeException,
  InvalidCreditCardException, InvalidActivationTypeException, TelusAPIException;

  /**
   * 
   * For HSPA equipment only; will throw UnsupportedEquipmentException otherwise.
   * Will validate airtime card pin; will throw InvalidAirtimeCardPINException otherwise.
   * Will validate the amount to meet minimum of application; will throw InvalidActivationAmountException otherwise.
   *
   * @param serialNumber
   * @param associatedHandsetIMEI Optional, can be empty or null
   * @param activationType
   * @param activationCode For airtime card activation, pass in pin
   * @param creditCard
   * @param businessRole
   * @param activationAmount Optional. For ACTIVATION_TYPE_AIRTIME_CARD, this amount will be overridden by actual amount on the airtime card. For other activation types, this amount will be past through.
   * @return PCSPrepaidConsumerAccount
   * @throws UnknownSerialNumberException
   * @throws SerialNumberInUseException
   * @throws InvalidSerialNumberException
   * @throws InvalidActivationCodeException
   * @throws InvalidCreditCardException
   * @throws InvalidAirtimeCardPINException
   * @throws InvalidActivationAmountException
   * @throws UnsupportedEquipmentException
   * @throws InvalidActivationTypeException
   * @throws TelusAPIException
   */
  PCSPrepaidConsumerAccount newPCSPrepaidConsumerAccount(String serialNumber, String associatedHandsetIMEI, int activationType, String activationCode, 			
		  CreditCard creditCard, String businessRole, double activationAmount, AuditHeader auditHeader) throws UnknownSerialNumberException, SerialNumberInUseException, 
		  InvalidSerialNumberException, InvalidActivationCodeException, InvalidCreditCardException, InvalidAirtimeCardPINException, 
		  InvalidActivationAmountException, UnsupportedEquipmentException, InvalidActivationTypeException, TelusAPIException;

  /**
   * This account creation only valid for Migration or Move.  This method cannot be use to activate Pre-paid subscriber
   * Want to Activate a new prepaid account use the Following method <CODE>newPCSPrepaidConsumerAccount(String serialNumber, int activationType, String activationCode, CreditCard creditCard, String businessRole)</CODE>
   *
   * <P>This method may involve a remote method call.
   *
   *
   */
  PCSPrepaidConsumerAccount newPCSPrepaidConsumerAccount();

//  PCSQuebectelPrepaidConsumerAccount newPCSQuebectelPrepaidConsumerAccount(String productType, String serialNumber)
//  throws UnknownSerialNumberException, SerialNumberInUseException, NotAnalogSerialNumberException, TelusAPIException;

  /**
   * Returns a new unsaved account.
   */
  @Deprecated
  IDENPostpaidConsumerAccount newIDENPostpaidConsumerAccount() throws TelusAPIException;

  /**
   * Returns a new unsaved account.
   */
  @Deprecated
  IDENPostpaidEmployeeAccount newIDENPostpaidEmployeeAccount() throws TelusAPIException;

  /**
   * Returns a new unsaved account.
   */
  @Deprecated
  IDENPostpaidBusinessRegularAccount newIDENPostpaidBusinessRegularAccount() throws TelusAPIException;

  /**
   * Returns a new unsaved account.
   */
  @Deprecated
  IDENPostpaidBusinessDealerAccount newIDENPostpaidBusinessDealerAccount() throws TelusAPIException;

  /**
   * Returns a new unsaved account.
   */
  @Deprecated
  IDENPostpaidBusinessPersonalAccount newIDENPostpaidBusinessPersonalAccount() throws TelusAPIException;

  /**
   * This method will return a new unsaved instance of PCSPostpaidCorporateRegularAccount.
   * On commit, system will auto-assign the least used bill cycle to account.
   * 
   * @param accountSubtype
   * 
   * @throws TelusAPIException
   *
   */
  @Deprecated
  IDENPostpaidCorporateRegularAccount newIDENPostpaidCorporateRegularAccount(char accountSubtype) throws TelusAPIException;
  
  /**
   * This method will create a new unsaved instance of IDENPostpaidCorporateRegularAccount
   * allowing calling application to specify the bill cycle for the account.
   *
   * @param accountSubtype
   * @param billCycle
   * 
   * @throws InvalidBillCycleException
   * @throws TelusAPIException
   *
   */
  @Deprecated
  IDENPostpaidCorporateRegularAccount newIDENPostpaidCorporateRegularAccount(char accountSubtype, int billCycle) throws TelusAPIException, InvalidBillCycleException;

  /**
   * Returns a new unsaved account.
   */
  //IDENPostpaidCorporateVPNAccount newIDENPostpaidCorporateVPNAccount() throws TelusAPIException;

  /**
   * Returns a new unsaved account.
   */
  @Deprecated
  PagerPostpaidConsumerAccount newPagerPostpaidConsumerAccount() throws TelusAPIException;

    /**
     * Returns a new unsaved account.
     */
  @Deprecated
  PagerPostpaidBusinessRegularAccount newPagerPostpaidBusinessRegularAccount() throws TelusAPIException;

    /**
     * Returns a new unsaved account.
     */
  @Deprecated
  PagerPostpaidBoxedConsumerAccount newPagerPostpaidBoxedConsumerAccount() throws TelusAPIException;

    /**
     * Returns a new unsaved account.
     */
//  PagerPostpaidBusinessPersonalAccount newPagerPostpaidBusinessPersonalAccount() throws TelusAPIException;




  /**
   * Returns the complete account identified by the id.
   *
   * <P>The returned object will be a specific type of account (i.e. IDENPostpaidConsumerAccount,
   * PCSPostpaidBusinessPersonalAccount, etc.).  Use the isXXX() methods, the getAccountType() &
   * getAccountSubType() methods or <CODE>instanceof</CODE> to determine which one.
   *
   * <P>The returned object will never be <CODE>null</CODE>.
   *
   * <P>This method may involve a remote method call.
   *
   * @param banId the id to search for.
   *
   * @exception UnknownBANException if no account with this id exists.
   *
   * @see AccountSummary#isPostpaidConsumer
   * @see AccountSummary#isPostpaidBusinessRegular
   * @see AccountSummary#isPostpaidBusinessPersonal
   * @see AccountSummary#isPrepaidConsumer
   */
  Account findAccountByBAN(int banId) throws BrandNotSupportedException, TelusAPIException, UnknownBANException;


  /**
   * Returns the complete accounts identified by a set of IDs.
   *
   *
   * <P>The returned objects will be a specific types of accounts (i.e. IDENPostpaidConsumerAccount,
   * PCSPostpaidBusinessPersonalAccount, etc.).  Use the isXXX() methods, the getAccountType() &
   * getAccountSubType() methods or <CODE>instanceof</CODE> to determine which ones.
   *
   * <P>If a given ID refers to a non-existent ban, it will simply have no entry
   * in the resulting array (no exception will be thrown).
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   *
   * <P>This method may involve a remote method call.
   *
   * @param banId the IDs to search for.
   *
   * @return the located accounts.  The accounts will be returned in the same
   *         order as the ids were passed in.
   *
   * @see AccountSummary#isPostpaidConsumer
   * @see AccountSummary#isPostpaidBusinessRegular
   * @see AccountSummary#isPostpaidBusinessPersonal
   * @see AccountSummary#isPrepaidConsumer
   */
  Account[] findAccountsByBANs(int[] banId) throws TelusAPIException;


  /**
   * Returns the account currently associated with a mobile number.
   *
   * <P>The returned object will be a specific type of account (i.e. IDENPostpaidConsumerAccount,
   * PCSPostpaidBusinessPersonalAccount, etc.).  Use the isXXX() methods, the getAccountType() &
   * getAccountSubType() methods or <CODE>instanceof</CODE> to determine which one.
   *
   * <P>The returned object will never be <CODE>null</CODE>.
   *
   * <P>This method may involve a remote method call.
   *
   * @param phoneNumber the mobile number to search on.
   *
   * @exception UnknownBANException no matching ban was found.
   *
   */
  Account findAccountByPhoneNumber(String phoneNumber) throws BrandNotSupportedException, TelusAPIException, UnknownBANException;
  
  /**
   * Returns the account currently associated with a mobile number/seat resource number.
   *
   * <P>The returned object will be a specific type of account (i.e. IDENPostpaidConsumerAccount,
   * PCSPostpaidBusinessPersonalAccount, etc.).  Use the isXXX() methods, the getAccountType() &
   * getAccountSubType() methods or <CODE>instanceof</CODE> to determine which one.
   *
   * <P>The returned object will never be <CODE>null</CODE>.
   *
   * <P>This method may involve a remote method call.
   *
   *@param phoneNumber ,this may be wireless phone number Or seat resource number (VOIP/TOLLFREE/HSIA)
   * @param PhoneNumberSearchOption ,phoneNumberSearchOption is having the flags searchbyWireless,searchByVOIP,searchByTollFree,searchByHSIA.  by default to searchbyWireless set to true,all remain flags set to false.
   *
   * @exception UnknownBANException no matching ban was found.
   *
   */
  Account findAccountByPhoneNumber(String phoneNumber,PhoneNumberSearchOption phoneNumberSearchOption) throws BrandNotSupportedException, TelusAPIException, UnknownBANException;

  /**
   * Light-weighted method of findAccoutnByPhoneNumber(String)
   * @param phoneNumber
   * @return Account
   * @throws BrandNotSupportedException
   * @throws TelusAPIException
   * @throws UnknownBANException
   */
  Account findLwAccountByPhoneNumber(String phoneNumber) throws BrandNotSupportedException, TelusAPIException, UnknownBANException;
  
  
  /**
   * Light-weighted method of findAccoutnByPhoneNumber(String)
   * @param phoneNumber, ,this may be wireless phone number Or seat resource number (VOIP/TOLLFREE/HSIA)
   * @param PhoneNumberSearchOption,PhoneNumberSearchOption  is having the flags searchbyWireless,searchByVOIP,searchByTollFree,searchByHSIA. we default searchbyWireless set to true,all remain flags set to false
   * @return Account
   * @throws BrandNotSupportedException
   * @throws TelusAPIException
   * @throws UnknownBANException
   */
  Account findLwAccountByPhoneNumber(String phoneNumber,PhoneNumberSearchOption phoneNumberSearchOption) throws BrandNotSupportedException, TelusAPIException, UnknownBANException;
  
  /**
   * Returns all  accounts, which have active subscriber associated with a serialNumber.
   * 
   * Recognized serial number include:
   * 	- any CDMA handset serial number currently or previously used
   * 	- any IDEN SIM card or IDEN handset serial number currently or previously used as primary equipment
   * 	- any Pager equipment serial number currently or previously used
   * 	- any HSPA USIM id currently associated to subscriber
   * 	- any IMEI currently still associated to subscriber
   * 
   * NOTE: If serial number is HSPA USIM ID, search will return only accounts currently associated with
   * given IMEI resource; regardless of subscriber status
   * 
   * NOTE: If serial number is HSPA IMEI, search will return only accounts with subscriber currently associated
   * to USIM id last associated with IMEI; regardless of subscriber status
   *
   *  <P><P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * @param serialNumber  
   * 
   * @exception TelusAPIException
   * 
   */
  AccountSummary[] findAccountsBySerialNumber(String serialNumber) throws TelusAPIException;
  
  /**
   * Returns all accounts, which have subscribers currently associated with IMSI.
   * Subscriber can be in any status.  Search only supports HSPA USIM card IMSIs.
   *
   * <P><P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * @param imsi - can be remote or home imsi for HSPA USIM cards
   * 
   * @throws TelusAPIException
   */
  AccountSummary[] findAccountsByImsi(String imsi) throws TelusAPIException;

  /**
   * Returns all accounts satisfying the Search by Business Name conditions.
   *
   * @param nameType
   * @param legalBusinessName
   * @param legalBusinessNameExactMatch
   * @param accountStatus
   * @param accountType
   * @param provinceCode
   * @param maximum
   * @return SearchResults
   * @throws TelusAPIException
   */
  SearchResults findAccountsByBusinessName(String nameType, String legalBusinessName,
                                              boolean legalBusinessNameExactMatch, char accountStatus,
                                              char accountType, String provinceCode, int maximum) throws TelusAPIException;

  /**
   * Returns all accounts satisfying the Search by Business Name conditions (with brand).
   * 
   * @param nameType
   * @param legalBusinessName
   * @param legalBusinessNameExactMatch
   * @param accountStatus
   * @param accountType
   * @param provinceCode
   * @param brandId
   * @param maximum
   * @return SearchResults
   * @throws TelusAPIException
   */
  SearchResults findAccountsByBusinessName(String nameType, String legalBusinessName,
          boolean legalBusinessNameExactMatch, char accountStatus,
          char accountType, String provinceCode, int brandId, int maximum) throws TelusAPIException;

  
  /**
   * Returns all accounts satisfying the Search by Name conditions.
   *
   * @param nameType
   * @param firstName
   * @param firstNameExactMatch
   * @param lastName
   * @param lastNameExactMatch
   * @param accountStatus
   * @param accountType
   * @param provinceCode
   * @param maximum
   * @return SearchResults
   * @throws TelusAPIException
   */
  SearchResults findAccountsByName(String nameType, String firstName, boolean firstNameExactMatch,
                                      String lastName, boolean lastNameExactMatch, char accountStatus,
                                      char accountType, String provinceCode, int maximum) throws TelusAPIException;

  /**
   * Returns all accounts satisfying the Search by Name conditions with brand.
   * 
   * @param nameType
   * @param firstName
   * @param firstNameExactMatch
   * @param lastName
   * @param lastNameExactMatch
   * @param accountStatus
   * @param accountType
   * @param provinceCode
   * @param brandId
   * @param maximum
   * @return SearchResults
   * @throws TelusAPIException
   * 
   */
  SearchResults findAccountsByName(String nameType, String firstName, boolean firstNameExactMatch,
          String lastName, boolean lastNameExactMatch, char accountStatus,
          char accountType, String provinceCode, int brandId, int maximum) throws TelusAPIException;

  /**
   * Returns all accounts ever associated with the phone Number
   *
   * <P><P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   */
  AccountSummary[] findAccountsByPhoneNumber(String phoneNumber) throws TelusAPIException;

   /**
   * Returns all accounts ever associated with the phone Number
   *
   * <P><P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * @param phoneNumber the phone number to search on.
   * @param includePastAccounts include Past Accounts, on which subscriber cancelled
   * @param onlyLastAccount only Last Account
   *
   * <P>This method may involve a remote method call.
   *
     */
    AccountSummary[] findAccountsByPhoneNumber(String phoneNumber, boolean includePastAccounts, boolean onlyLastAccount) throws TelusAPIException;

    /**
     * Returns last  (active/cancel/suspend)  account associated with the phone Number /voip number/toll free number  ( based on criteria passed in for PhoneNumberSearchOption )
     *
     * <P><P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
     * elements, but may contain no (zero) elements.
     *
     * @param phoneNumber the phone number/voip number/toll free number to search on.
     * @param PhoneNumberSearchOption ,phoneNumberSearchOption is having the flags searchbyWireless,searchByVOIP,searchByTollFree,searchByHSIA.  by default to searchbyWireless set to true,all remain flags set to false.
     *
     * <P>This method may involve a remote method call.
     *
       */
     AccountSummary[] findLastAccountsByPhoneNumber(String phoneNumber, PhoneNumberSearchOption phoneNumberSearchOption) throws TelusAPIException;
      
   /**
   * Returns all accounts associated with this postal code and last name.
   *
   * <P><P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   */
  AccountSummary[] findAccountsByPostalCode(String lastName, String postalCode, int maximum) throws TelusAPIException;

  /**
   * Returns all accounts that were created, or had a subscriber added, by a given
   * dealership from a specific date.
   *
   *
   * <P>The returned objects will be a specific types of accounts (i.e. IDENPostpaidConsumerAccount,
   * PCSPostpaidBusinessPersonalAccount, etc.).  Use the isXXX() methods, the getAccountType() &
   * getAccountSubType() methods or <CODE>instanceof</CODE> to determine which ones.
   *
   * <P><P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * <P> Not all attributes of AccountInfo will be populated
   * (against Dele Taylor's recomendation  )
   * List of populated attributes : banID, status, accountType, accountSubType,
   *                                dealerCode, salesRepCode, lastChangesDate, fullName
   *                                creditCheckResult
   *
   *
   * @param accountStatus - the account Status
   * @param dealerCode the dealership to search on.
   * @param startDate date to start searching from.
   * @param maximum the total number of bans to retrieve.
   *
   */
  Account[] findAccountsByDealership(char accountStatus,String dealerCode, Date startDate, int maximum) throws TelusAPIException;

   /**
   * Returns all accounts that were created, or had a subscriber added, by a given
   * dealership within a specified date range.
   *
   * <P>The returned objects will be specific types of accounts (i.e. IDENPostpaidConsumerAccount,
   * PCSPostpaidBusinessPersonalAccount, etc.).  Use the isXXX() methods, the getAccountType() &
   * getAccountSubType() methods or <CODE>instanceof</CODE> to determine which ones.
   *
   * <P><P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * <P> Not all attributes of AccountInfo will be populated
   * (against Dele Taylor's recomendation  )
   * List of populated attributes : banID, status, accountType, accountSubType,
   *                                dealerCode, salesRepCode, lastChangesDate, fullName
   *                                creditCheckResult
   *
   * @param accountStatus - the account Status
   * @param dealerCode the dealership to search on.
   * @param startDate date to start searching from.
   * @param endDate   date to search to.
   * @param maximum the total number of bans to retrieve.
   *
   */
  Account[] findAccountsByDealership(char accountStatus,String dealerCode, Date startDate, Date endDate,int maximum) throws TelusAPIException;

  /**
   * Returns all accounts that were created, or had a subscriber added, by a given
   * salesRep from a specific date.
   *
   * <P>The returned objects will be specific types of accounts (i.e. IDENPostpaidConsumerAccount,
   * PCSPostpaidBusinessPersonalAccount, etc.).  Use the isXXX() methods, the getAccountType() &
   * getAccountSubType() methods or <CODE>instanceof</CODE> to determine which ones.
   *
   * <P><P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * <P> Not all attributes of AccountInfo will be populated
   * (against Dele Taylor's recomendation  )
   * List of populated attributes : banID, status, accountType, accountSubType,
   *                                dealerCode, salesRepCode, lastChangesDate, fullName
   *                                creditCheckResult
   *
   * @param accountStatus - the account Status
   * @param dealerCode the dealership to search on.
   * @param salesRepCode the sales representative to search on.
   * @param startDate date to start searching from.
   * @param maximum the total number of bans to retrieve.
   *
   */
  Account[] findAccountsBySalesRep(char accountStatus, String dealerCode, String salesRepCode, Date  startDate , int maximum) throws TelusAPIException;

 /**
   * Returns all accounts that were created, or had a subscriber added, by a given
   * salesRep within a specified date range.
   *
   * <P>The returned objects will be specific types of accounts (i.e. IDENPostpaidConsumerAccount,
   * PCSPostpaidBusinessPersonalAccount, etc.).  Use the isXXX() methods, the getAccountType() &
   * getAccountSubType() methods or <CODE>instanceof</CODE> to determine which ones.
   *
   * <P><P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * <P> Not all attributes of AccountInfo will be populated
   * (against Dele Taylor's recomendation  )
   * List of populated attributes : banID, status, accountType, accountSubType,
   *                                dealerCode, salesRepCode, lastChangesDate, fullName
   *                                creditCheckResult
   *
   * @param accountStatus - the account Status
   * @param dealerCode the dealership to search on.
   * @param salesRepCode the sales representative to search on.
   * @param startDate date to start searching from.
   * @param endDate   date to search to.
   * @param maximum the total number of bans to retrieve.
   *
   */
  Account[] findAccountsBySalesRep(char accountStatus,String dealerCode, String salesRepCode, Date  startDate ,Date  endDate , int maximum) throws TelusAPIException;

  /**
   * Returns the subscriber currently associated with a mobile number.
   *
   * Supports just PCS and IDEN subscribers
   *
   * The returned object will be a specific type of Subscriber (i.e. PCSSubscriber or
   * IDENSubscriber).  Use the isIDEN() or isPCS() methods or <CODE>instanceof</CODE>
   * to determine which one.
   *
   * <P>The returned object will never be <CODE>null</CODE>.
   *
   * <P>This method may involve a remote method call.
   *
   * @exception UnknownSubscriberException no matching subscriber was found.
   *
   */
  Subscriber findSubscriberByPhoneNumber(String phoneNumber) throws BrandNotSupportedException, TelusAPIException, UnknownSubscriberException;
 
  /**
	 * Returns Subscriber Info by Phone Number and PhoneNumberSearchOption 
	 *
	 * <P>This method may involve a remote method call.
	 *
	 *@param phoneNumber ,this may be wireless phone number Or seat resource number (VOIP/TOLLFREE/HSIA)
     * @param PhoneNumberSearchOption , this is having the flags searchbyWireless,searchByVOIP,searchByTollFree,searchByHSIA.  by default to searchbyWireless set to true,all remain flags set to false.
	 * @exception UnknownSubscriberException if this subscriber doesn't exist.
	 *
	 */
  
  public Subscriber findSubscriberByPhoneNumber(String phoneNumber,PhoneNumberSearchOption phoneNumberSearchOption) throws TelusAPIException, UnknownSubscriberException;

/**
   * Returns the List of subscribers ever associated with a mobile number.
   *
   * The returned object will be a specific type of Subscriber (i.e. PCSSubscriber).
   *
   * <P>The returned object will never be <CODE>null</CODE>.
   *
   * <P>This method may involve a remote method call.
   *
   * 
   * @param phoneNumber
   * @param maximum  0 = ALL, however this should NOT be used for a big ban. If it is non-zero and greater than system defined maximum, LimitExceededException will be thrown.
   * @param includeCancelled
   * @return
   * @throws TelusAPIException
   * @throws UnknownSubscriberException
   */
  Subscriber[] findSubscribersByPhoneNumber(String phoneNumber,int maximum, boolean includeCancelled) throws TelusAPIException, UnknownSubscriberException;

  /**
   * Returns the List of subscribers ever associated with a mobile number.
   *
   * The returned object will be a specific type of Subscriber (i.e. PCSSubscriber).
   *
   * <P>The returned object will never be <CODE>null</CODE>.
   *
   * <P>This method may involve a remote method call.
   *
   * 
   * @param phoneNumber ,this may be wireless phone number Or seat resource number (VOIP/TOLLFREE/HSIA)
   * @param PhoneNumberSearchOption , this is having the flags searchbyWireless,searchByVOIP,searchByTollFree,searchByHSIA.  by default to searchbyWireless set to true,all remain flags set to false.
   * @param maximum  0 = ALL, however this should NOT be used for a big ban. If it is non-zero and greater than system defined maximum, LimitExceededException will be thrown.
   * @param includeCancelled
   * @return
   * @throws TelusAPIException
   * @throws UnknownSubscriberException
   */
  public Subscriber[] findSubscribersByPhoneNumber(String phoneNumber, PhoneNumberSearchOption phoneNumberSearchOption, int maximum, boolean includeCancelled) throws TelusAPIException, UnknownBANException;
  /**
   * Returns all non-cancelled subscriber associated with a serialNumber.
   * 
   * Recognized serial number include:
   * 	- any CDMA handset serial number currently or previously used
   * 	- any IDEN SIM card or IDEN handset serial number currently or previously used as primary equipment
   * 	- any Pager equipment serial number currently or previously used
   * 	- any HSPA USIM id currently associated to subscriber
   * 	- any IMEI currently still associated to subscriber
   * 
   * NOTE: If serial number is HSPA USIM ID, search will return only accounts currently associated with
   * given IMEI resource
   * 
   * NOTE: If serial number is HSPA IMEI, search will return only accounts with subscriber currently associated
   * to USIM id last associated with IMEI
   *
   *  <P><P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * @param serialNumber  
   * 
   * @exception UnknownSubscriberException
   * @exception TelusAPIException
   * 
   */
  Subscriber[] findSubscribersBySerialNumber(String serialNumber) throws UnknownSubscriberException, TelusAPIException;

  /**
   * Returns all subscriber associated with a serialNumber.
   * 
   * Recognized serial number include:
   * 	- any CDMA handset serial number currently or previously used
   * 	- any IDEN SIM card or IDEN handset serial number currently or previously used as primary equipment
   * 	- any Pager equipment serial number currently or previously used
   * 	- any HSPA USIM id currently associated to subscriber
   * 	- any IMEI currently still associated to subscriber
   * 
   * NOTE: If serial number is HSPA USIM ID, search will return only accounts currently associated with
   * given IMEI resource; regardless of subscriber status
   * 
   * NOTE: If serial number is HSPA IMEI, search will return only accounts with subscriber currently associated
   * to USIM id last associated with IMEI; regardless of subscriber status
   *
   *  <P><P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * @param serialNumber  
   * 
   * @exception UnknownSubscriberException
   * @exception TelusAPIException
   * 
   */
  Subscriber[] findSubscribersBySerialNumber(String serialNumber, boolean includeCancelled ) throws UnknownSubscriberException, TelusAPIException;

  /**
   * Returns all subscribers currently associated with IMSI.
   * Subscriber can be in cancelled status if includingCancelled == true.  
   * Search only supports HSPA USIM card IMSIs.
   *
   * <P><P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * @param imsi - can be remote or home imsi for HSPA USIM cards
   * 
   * @throws TelusAPIException
   */
  Subscriber[] findSubscribersByImsi(String imsi, boolean includingCancelled) throws TelusAPIException;
  
  /**
   * Returns the most recently added, non-cancelled, subscribers associated with the account for
   * this id up to a maximum.
   *
   * The returned objects will be specific types of Subscribers (i.e. PCSSubscriber or
   * IDENSubscriber).  Use the isIDEN() or isPCS() methods or <CODE>instanceof</CODE>
   * to determine which ones.
   *
   *  Supports  PCS , IDEN,  Pager  subscribers
   * <P><P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * @param banId
   * @param maximum 0 = ALL, however this should NOT be used for a big ban. If it is non-zero and greater than system defined maximum, LimitExceededException will be thrown.
   * @return
   * @throws TelusAPIException
   * @throws UnknownBANException
   * @exception UnknownBANException if no account with this id exists.
   *
   */
  Subscriber[] findSubscribersByBAN(int banId, int maximum) throws TelusAPIException, UnknownBANException;

  /**
   * Returns the ported out subscribers associated with the account for
   * this id up to a maximum.
   *
   * The returned objects will be specific types of Subscribers (i.e. PCSSubscriber or
   * IDENSubscriber).  Use the isIDEN() or isPCS() methods or <CODE>instanceof</CODE>
   * to determine which ones.
   *
   *  Supports  PCS , IDEN,  Pager  subscribers
   * <P><P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * @param banId
   * @param maximum 0 = ALL, however this should NOT be used for a big ban. If it is non-zero and greater than system defined maximum, LimitExceededException will be thrown.
   * @return
   * @throws TelusAPIException
   * @throws UnknownBANException
   * @exception UnknownBANException if no account with this id exists.
   *
   */
  Subscriber[] findPortedSubscribersByBAN(int banId, int maximum) throws TelusAPIException, UnknownBANException;
  
  /**
   * Returns the most recently added subscribers associated with the account for
   * this id up to a maximum.
   *
   * The returned objects will be specific types of Subscribers (i.e. PCSSubscriber or
   * IDENSubscriber).  Use the isIDEN() or isPCS() methods or <CODE>instanceof</CODE>
   * to determine which ones.
   *
   *  Supports  PCS , IDEN,  Pager  subscribers
   * <P><P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * @param banId
   * @param maximum  0 = ALL, however this should NOT be used for a big ban. If it is non-zero and greater than system defined maximum, LimitExceededException will be thrown.
   * @param includeCancelled
   * @return
   * @throws TelusAPIException
   * @throws UnknownBANException
   * @exception UnknownBANException if no account with this id exists.
   *
   */
  Subscriber[] findSubscribersByBAN(int banId, int maximum, boolean includeCancelled) throws TelusAPIException, UnknownBANException;

  /*
  public FollowUpInfo retrieveFollowUpInfoByBanFollowUpID(int pBan, int pFollowUpID) throws TelusException, RemoteException;
  public int retrieveLastFollowUpIDByBanFollowUpType(int pBan, String pFollowUpType) throws TelusException, RemoteException;
  */

  /**
   * Calculates tax for the amount passed in by using the province Code passed in
   * to retrieve the tax policy used to base the tax calculation on.  It is important
   * to note that the GST and PST Exempt parameter values will be ignored for HST
   * provinces.
   * 
   * @param amount The amount to base tax calculation on
   * @param provinceCode The province code used to retrieve the tax policy to base tax calculations on
   * @param isGSTExempt Boolean value used to determine if GST should be exempt.  Ignored for HST provinces
   * @param isPSTExempt Boolean value used to determine if PST should be exempt.  Ignored for HST provinces
   * @param isHSTExempt Boolean value used to determine if HST should be exempt.  Ignored for non-HST provinces
   * 
   * @throws TelusAPIException
   */
  TaxSummary calculateTax(double amount, String provinceCode, boolean isGSTExempt, boolean isPSTExempt, boolean isHSTExempt) throws TelusAPIException;


  /**
   * Calculates tax for the amount passed in by using the province Code (associated with
   * the account object passed in) to retrieve the tax policy used to base the 
   * tax calculation on.
   * 
   * @param amount The amount to base tax calculation on
   * @param account The account object where province code and tax exempt values can be obtained from.
   * 
   * @throws TelusAPIException
   */

  TaxSummary calculateTax(double amount, AccountSummary account) throws TelusAPIException;
  
  /**
   * Create ActivationCredit with the given parameters
   *
   * The returned object will be a ActivationCredit.
   *      
   * @param creditType The credit type of the credit
   * @param contractTerm The contract term of the credit
   * @param effectiveDate The expiration date of the credit
   * @param expiryDate The expiration date of the credit
   * @param description 
   * @param descriptionFrench 
   * @param amount The amount of the credit
   * @param barcode The bar code of the credit
   * @param productType The product type of the equipment
   *
   *
   */  
  public ActivationCredit newActivationCredit( String creditType, int contractTerm, 
	                                       Date effectiveDate, Date expiryDate, 
		                               String description, String descriptionFrench, 
		                               double amount, String barcode, String productType ) 
         throws TelusAPIException;
  
  /**
   * Create ActivationCredit with the given parameters
   *
   * The returned object will be a ActivationCredit.
   * 
   * @param offerCode code The offer code
   * @param creditType The credit type of the credit
   * @param contractTerm The contract term of the credit
   * @param effectiveDate The expiration date of the credit
   * @param expiryDate The expiration date of the credit
   * @param description 
   * @param descriptionFrench 
   * @param amount The amount of the credit
   * @param barcode The bar code of the credit
   * @param productType The product type of the equipment
   *
   *
   */  
  public ActivationCredit newActivationCredit( String offerCode, String creditType, int contractTerm, 
	                                       Date effectiveDate, Date expiryDate, 
		                               String description, String descriptionFrench, 
		                               double amount, String barcode, String productType ) 
         throws TelusAPIException;
         
 
  /**
   * Returns all ActivationCredits for the specified criteria.
   *
   * <P><P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * @param serialNumber The handset's ESN (PCS) or IMEI (IDEN).
   * @param province Activaiton province
   * @param npa Activation NPA
   * @param contractTermMonths Subscriber contract term (in months)
   * @param activationDate Activation date
   *
   *
   */
  ActivationCredit[] getActivationCreditsByDate(String serialNumber, String province, String npa, int contractTermMonths, Date activationDate) throws TelusAPIException;

  /**
   * Returns all ActivationCredits for the specified criteria.
   *
   * <P><P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * @param serialNumber The handset's ESN (PCS) or IMEI (IDEN).
   * @param province Activaiton province
   * @param npa Activation NPA
   * @param contractTermMonths Subscriber contract term (in months)
   * @param pricePlan Subscriber Price (Rate) Plan Code
   * @param fidoConversion FIDO conversion indicator
   * 
   * @deprecated use P3MS EJB method
   */
  ActivationCredit[] getActivationCredits(String serialNumber, String province, String npa, int contractTermMonths, String pricePlan, boolean fidoConversion) throws TelusAPIException;

  /**
   * Returns all ActivationCredits for the specified criteria.
   *
   * <P><P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * @param serialNumber The handset's ESN (PCS) or IMEI (IDEN).
   * @param province Activaiton province
   * @param npa Activation NPA
   * @param contractTermMonths Subscriber contract term (in months)
   * @param activationDate Activation date
   * @param fidoConversion FIDO conversion indicator
   */
  ActivationCredit[] getActivationCreditsByDate(String serialNumber, String province, String npa, int contractTermMonths, Date activationDate, boolean fidoConversion) throws TelusAPIException;

  /**
   * Returns the ActivationCredits for the Credit Type.
   *
   * <P><P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * @param serialNumber Handset serial number.  For SIM based handsets, IMEI required.
   * @param province Activaiton province
   * @param npa Activation NPA
   * @param creditType Activation credit type required
   *
   * <P>Valid Credit Types:
   * ActivationCredit.PROMOTION_CREDIT
   * ActivationCredit.CONTRACT_TERM_CREDIT
   * ActivationCredit.NEW_ACTIVATION_CREDIT
   * ActivationCredit.PRICE_PLAN_CREDIT <new>
   * ActivationCredit.FIDO_CREDIT <new>
   *
   */
  ActivationCredit[] getActivationCreditsByCreditType(String serialNumber, String province, String npa, String creditType) throws TelusAPIException;

  
  /**
   * Returns the ActivationCredits Map for the list of Product Code
   *
   * <P><P>The HashMap is never <CODE>null</CODE>, and contains an array of activation credit 
   * for the product code. 
   * <li> If the map contains the array, the array is never <CODE>null</CODE>, 
   * and never contains <CODE>null</CODE> elements, but may contain no (zero) elements. 
   * <li> If the map does not contain the array of activation credit for the product code, the map
   * may be <CODE>null</CODE>
   *
   * <P>This method may involve a remote method call.
   *
   * @param productCodes a list of Handset Product Code.  
   * @param province Activaiton province
   * @param npa Activation NPA
   * @param contractTermMonths Subscriber contract term (in months)
   * @param productTypes a list of Handset Product Type
   * @param isInitialActivation Initial activation indicator
   * 
   * @deprecated use P3MS EJB method 
   */  
  java.util.HashMap getActivationCreditsByProductCodes(String[] productCodes, String province, String npa, int contractTermMonths, String[] productTypes, boolean isInitialActivation) throws TelusAPIException;
	  
  
  /**
   * Returns the PhoneNumberReservation Nothing would be set.
   *
   * <P>This method may involve a remote method call.
  */

  public PhoneNumberReservation newPhoneNumberReservation();
  
  /**
   * Returns the PhoneNumberSearchOption , this will be used as input for subscriber retrieval search to set VOIP and Wirerless search flags.
   *
  */

  public PhoneNumberSearchOption newPhoneNumberSearchOption();
  

  /**
   * Returns an array of PrepaidEventHistory objects for a given phone number and date range.
   * NOTE: The phone number is NOT validated.
   *
   * <br><br>
   * <b>TODO</b>: any modification requested should result in deprecating this method and having Prepaid web services handle the requested changes.  For futher details, 
   * refer to Client API Design.doc design document for Prepaid Real Time Rating project
   * 
   * @param phoneNumber phone number of Prepaid handset.
   * @param startDate starting date of history
   * @param endDate ending date of history
   *
   */
  public PrepaidEventHistory[] getPrepaidEventHistory(String phoneNumber, Date startDate, Date endDate) throws TelusAPIException;

  /**
   * Returns an array of PrepaidEventHistory objects for a given phone number, date range, and specified subset of event types.
   * NOTE: The phone number is NOT validated.
   *
   * <br><br>
   * <b>TODO</b>: any modification requested should result in deprecating this method and having Prepaid web services handle the requested changes.  For futher details, 
   * refer to Client API Design.doc design document for Prepaid Real Time Rating project
   * 
   * @param phoneNumber phone number of Prepaid handset.
   * @param startDate starting date of history
   * @param endDate ending date of history
   * @param eventTypes array of event types to retrieve
   *
   */
  public PrepaidEventHistory[] getPrepaidEventHistory(String phoneNumber, Date startDate, Date endDate, PrepaidEventType[] eventTypes) throws TelusAPIException;

  /**
   * Returns an array of PrepaidCallHistory objects for a given phone number and date range.
   * NOTE: The phone number is NOT validated.
   * 
   * <br><br>
   * <b>TODO</b>: any modification requested should result in deprecating this method and having Prepaid web services handle the requested changes.  For futher details, 
   * refer to Client API Design.doc design document for Prepaid Real Time Rating project
   * 
   * @param phoneNumber phone number of Prepaid handset.
   * @param startdate starting date of history
   * @param endDate ending date of history
   *
   */
  public PrepaidCallHistory[] getPrepaidCallHistory(String phoneNumber, Date startdate, Date endDate) throws TelusAPIException;

  /**
   * Returns an array of follow ups corresponding to the provided search criteria.
   *
   * @param followUpCriteria
   * @return FollowUp[]
   * @throws TelusAPIException
   */
  public FollowUp[] findFollowUps(FollowUpCriteria followUpCriteria) throws TelusAPIException;

  /**
   * Returns an array of Memo objects for a given set of criteria as specified in the MemoCriteria object.
   * The criteria will be validated and an exception will be thrown if found to be invalid.
   *
   * <P><P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   */
  Memo[] findMemos(MemoCriteria criteria) throws TelusAPIException;

  /**
   * Returns a new instance of MemoCriteria for use in <CODE>findMemos</CODE>.
   */
  MemoCriteria newMemoCriteria() throws TelusAPIException;

  /**
   * Returns a new instance of FollowUpCriteria for use in <CODE>findFollowUps</CODE>.
   */
  FollowUpCriteria newFollowUpCriteria() throws TelusAPIException;

  /**
   * Changes account type and returns the new object.
   *
   * @param account
   * @param accountType
   * @param accountSubType
   * @return Account
   * @throws InvalidAccountTypeChangeException
   * @throws TelusAPIException
   */
  Account changeAccountType(Account account, char accountType, char accountSubType) throws InvalidAccountTypeChangeException, TelusAPIException;

  /**
   * Retrieves a list of Mike accounts for a given Talkgroup.
   *
   * @param urbanId
   * @param fleetId
   * @param talkGroupId
   * @return AccountSummary[]
   * @throws TelusAPIException
   */
  AccountSummary[] findAccountsByTalkGroup(int urbanId, int fleetId, int talkGroupId) throws TelusAPIException;

  /**
   * Retrieves a list of Mike subscribers for a given Talkgroup.
   *
   * @param urbanId
   * @param fleetId
   * @param memberId
   * @return IDENSubscriber[]
   * @throws TelusAPIException
   */
  IDENSubscriber[] findSubscribersByMemberIdentity(int urbanId, int fleetId, String memberId) throws TelusAPIException;
  /**
   * Returns the most recently added, non-cancelled, subscribers for this account/fleet
   * up to the specified maximum.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * @param maximum # of rows to retrieve; zero means 'all'.
   */
  IDENSubscriber[] findSubscribersByBanAndFleet(int banId, int urbanId, int fleetId, int maximum) throws UnknownBANException, TelusAPIException;
  /**
   * Returns the most recently added, non-cancelled, subscribers for this account/talkgroup
   * up to the specified maximum.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * @param maximum # of rows to retrieve; zero means 'all'.
   */
  IDENSubscriber[] findSubscribersByBanAndTalkGroup(int banId, int urbanId, int fleetId, int talkGroupId, int maximum) throws UnknownBANException, TelusAPIException;
  

  /**
   * Returns the ActivationTopup
   *
   *
   * <P>This method may involve a remote method call.
   *
   */
  public ActivationTopUp newActivationTopUp();
  
  public QueueThresholdEvent getQueueThresholdEventbyCallCentreConnectionId(long callCentreConnectionId) throws TelusAPIException;
  
  PaymentTransfer newPaymentTransfer(int targetBanId, String reason, double amount) throws TelusAPIException;

  public PCSAccount newPCSAccount(IDENAccount oldAccount) throws AccountMatchException, TelusAPIException;
  @Deprecated
  public IDENAccount newIDENAccount(PCSAccount oldAccount) throws AccountMatchException, TelusAPIException;

  String[] findPartiallyReservedSubscribersByBan(int ban, int maximum) throws TelusAPIException;

  /**
   * This method will return a new unsaved instance of PCSPostpaidCorporatePersonalAccount.
   * On commit, system will auto-assign the least used bill cycle to account.
   * 
   * @param accountSubType
   * 
   * @throws TelusAPIException
   *
   */
  PCSPostpaidCorporatePersonalAccount newPCSPostpaidCorporatePersonalAccount(char accountSubType) throws TelusAPIException;

  /**
   * This method will create a new unsaved instance of PCSPostpaidCorporatePersonalAccount
   * allowing calling application to specify the bill cycle for the account.
   *
   * @param accountSubtype
   * @param billCycle
   * 
   * @throws InvalidBillCycleException
   * @throws TelusAPIException
   *
   */
  PCSPostpaidCorporatePersonalAccount newPCSPostpaidCorporatePersonalAccount(char accountSubtype, int billCycle) throws TelusAPIException, InvalidBillCycleException;
  
  
  /**
   * Returns a new instance of ServicesValidation .
   */
  public ServicesValidation newServicesValidation();
  
  
  /**
   * This method will allow the creation of a Subscriber object using a 
   * serialized Subscriber from another session
   *
   * @param subscriber
   * @return Subscriber
   * @throws TelusAPIException
   */
  public Subscriber newSubscriber(Subscriber subscriber) throws TelusAPIException;
  
  
  	/**
  	 * This method create new AuditHeader instance
  	 * @return a empty AuditHeader instance
  	 */
  	public AuditHeader newAuditHeader() ;
  	
  	/**
  	 * This method returns an Account subclass with the specified account sub-type
  	 * representing a postpaid business personal account
  	 *
  	 * @param accountSubType - one of the constants from AccountSummary.ACCOUNT_SUBTYPE_*
  	 * @throws TelusAPIException if input account sub-type is not valid for this account type
  	 */
  	PCSPostpaidBusinessPersonalAccount newPCSPostpaidBusinessPersonalAccount(char accountSubType) throws TelusAPIException;

  	/**
  	 * This method returns an Account subclass with the specified account sub-type
  	 * representing a postpaid business regular account
  	 *
  	 * @param accountSubType - one of the constants from AccountSummary.ACCOUNT_SUBTYPE_*
  	 * @throws TelusAPIException if input account sub-type is not valid for this account  type
  	 */
  	PCSPostpaidBusinessRegularAccount newPCSPostpaidBusinessRegularAccount(char accountSubType) throws TelusAPIException;
  	/*
  	 * Instantiates a local copy of a PCSAccount by copying all the fields on oldAccount
  	 * and setting the account's type and sub-type to accountType and accountSubType,
  	 * respectively.  The banId is set to 0.
  	 * This method returns the same Account subclass as {@link #newPCSAccount(IDENAccount)
  	 * newPCSAccount} with the passed in accountType and accountSubtype.
  	 *
  	 * @param oldAccount Account object to copy fields from
  	 * @param accountType new Account's account type
  	 * @param accountSubType new Account's account sub-type
  	 * @return PCSAccount subclass representing the new account
  	 * @throws AccountMatchException if accountType, accountSubType results in an unknown
  	 * returned Account subclass
  	 */
  	public PCSAccount newPCSAccount(IDENAccount oldAccount, char accountType, char accountSubType) throws AccountMatchException, TelusAPIException;

  	public EnterpriseAddress newEnterpriseAddress() throws TelusAPIException;
  	
  	public Address newAddress() throws TelusAPIException;
  	
  	public SeatData newSeatData() throws TelusAPIException;
}
