 /*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import java.util.Date;

import com.telus.api.HistorySearchException;
import com.telus.api.InvalidPricePlanChangeException;
import com.telus.api.TelusAPIException;
import com.telus.api.equipment.Card;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.EquipmentWarrantyNotAvailableException;
import com.telus.api.equipment.IDENEquipment;
import com.telus.api.equipment.MuleEquipment;
import com.telus.api.message.ApplicationMessage;
import com.telus.api.reference.DiscountPlan;
import com.telus.api.reference.Feature;
import com.telus.api.reference.InvoiceCallSortOrderType;
//import com.telus.api.reference.Letter;
import com.telus.api.reference.MigrationType;
import com.telus.api.reference.NumberGroup;
import com.telus.api.reference.PricePlan;
import com.telus.api.reference.PricePlanSummary;
import com.telus.api.reference.ReasonType;
import com.telus.api.reference.ServiceSummary;
import com.telus.api.servicerequest.ServiceRequestHeader;

/**
 * <CODE>Subscriber</CODE> encapsulates information on an individual mobile
 *  phone user.
 *
 */
public interface Subscriber {

  public static final String SUPPORT_LEVEL_CORPORATE_HELP_DESK = Feature.CATEGORY_CODE_CORPORATE_HELP_DESK;
  public static final String SUPPORT_LEVEL_ENHANCED_CALL_CENTRE = Feature.CATEGORY_CODE_ENHANCED_CALL_CENTRE;
  public static final String SUPPORT_LEVEL__BASIC = "BSC";
  public static final String SUPPORT_LEVEL_STANDARD = "STANDARD";

  String TTR_SLA_LEVEL = Feature.CATEGORY_CODE_TTR_SLA;
  String NON_SLA= "NONSLA";


  public final static char PROVISIONING_PLATFORM_WPS = 'P';
  public final static char PROVISIONING_PLATFORM_WIN = 'W';
  public final static char PROVISIONING_PLATFORM_OTHER = 'O';
  public final static String PROVISIONING_PLATFORM_CODE_OTHER = "OTHER";

  public static final String PRODUCT_TYPE_PCS = "C";
  @Deprecated
  public static final String PRODUCT_TYPE_PAGER = "P";
  @Deprecated
  public static final String PRODUCT_TYPE_IDEN = "I";
  @Deprecated
  public static final String PRODUCT_TYPE_TANGO = "T"; 
  @Deprecated
  public static final String PRODUCT_TYPE_CDPD = "D";

  public static final char STATUS_RESERVED  = 'R';
  public static final char STATUS_ACTIVE    = 'A';
  public static final char STATUS_SUSPENDED = 'S';
  public static final char STATUS_CANCELED  = 'C';

  public static final String SWAP_TYPE_REPAIR      = "REPAIR";
  public static final String SWAP_TYPE_REPLACEMENT = "REPLACEMENT";
  public static final String SWAP_TYPE_LOANER      = "LOANER";

  public static final String DUMMY_REPAIR_ID = "DUMMY0";

  public static final char ACCOUNT_STATUS_NO_CHANGE = '\0';

  public static final int TERM_PRESERVE_COMMITMENT = -1;
  public static final int TERM_MONTH_TO_MONTH = 0;
  public static final int TERM_1_YEAR = 12;
  public static final int TERM_2_YEARS = 24;
  public static final int TERM_3_YEARS = 36;
  public static final int TERM_4_YEARS = 48;
  public static final int TERM_5_YEARS = 60;
  public static final int FORM_EQUIPMENT_WARRANTY = 100;
  public static final int FORM_PAP = 101;
  public static final String LANGUAGE_ENGLISH = "EN";
  public static final String LANGUAGE_FRENCH = "FR";

  public static final String PROVISINING_STATUS_COMPLETE = "CS";
  public static final String PROVISINING_STATUS_ERROR = "CE";
  public static final String PROVISINING_STATUS_PENDING = "PE";
  public static final String PROVISINING_STATUS_PROCESSING = "PR";
  public static final String PROVISINING_STATUS_WAITING_TO_BE_PROCESSED = "IN";
  public static final String PROVISINING_STATUS_WAITING_FOR_REPLY = "WR";

  // Taken from the table: RESOURCE_TYPE
  public static final String RESOURCE_TYPE_PHONE_NUMBER = "N";   //PTN
  public static final String RESOURCE_TYPE_MIKE_NUMBER = "H";    //UFMI
  public static final String RESOURCE_TYPE_IP = "X";             //COIP
  public static final String RESOURCE_TYPE_IP_PRIVATE = "Y";     //PRIVATE
  public static final String RESOURCE_TYPE_ALL = "*";
  public static final String RESOURCE_STATUS_ACTIVE = "A";
  public static final String RESOURCE_STATUS_SUSPENDED = "S";
  public static final String RESOURCE_STATUS_CANCELED = "C";

  
  public final static String RESOURCE_TYPE_HSIA = "I";
  public final static String RESOURCE_TYPE_PRIMARY_VOIP = "V";
  public final static String RESOURCE_TYPE_ADDITIONAL_VOIP = "L";
  public final static String RESOURCE_TYPE_TOLLFREE_VOIP = "O";
  public final static String RESOURCE_TYPE_PROVISIONING = "provision";
  
  public static final String PORT_TYPE_PORT_IN = "I";
  public static final String PORT_TYPE_PORT_OUT = "O";
  public static final String PORT_TYPE_PORT_IN_OUT = "B";

  public static final String PORT_OPTION_NO_PORT = "N";
  public static final String PORT_OPTION_WINBACK = "W";
  public static final String PORT_OPTION_INADVERTENT_PORT = "I";
  public static final String PORT_OPTION_INTER_BRAND_ROLLBACK = "R";

  public static final char SWAP_DUPLICATESERIALNO_ALLOWSAMEBAN = 'S';
  public static final char SWAP_DUPLICATESERIALNO_ALLOWOTHERBAN = 'O';
  public static final char SWAP_DUPLICATESERIALNO_DONOTALLOW = 'X';
  
  /**
   * Saves all attributes of this subscriber, including the contract, to the
   * datastore using the subscriber's dealerCode and salesRepCode.  If this
   * is the subscriber's first save, it will be placed in a reserved state.
   *
   * <P>This method may involve a remote method call.
   *
   * @see #activate()
   * @see #save(boolean)
   */
  void save() throws TelusAPIException;

  //void save(String dealerCode, String salesRepCode) throws TelusAPIException;


  /**
   * Saves all attributes of this subscriber, including the contract,  to the
   * datastore using the subscriber's dealerCode and salesRepCode.  If this is
   * <B>NOT</B> the subscriber's first save, the <CODE>activate</CODE> argument
   * will be ignored.
   *
   * <P>This method may involve a remote method call.
   *
   * @param activate whether to activate this subscriber or leave it in a
   *                 reserved state.  This parameter is only used for a
   *                 subscriber's first save.
   */
  void save(boolean activate) throws TelusAPIException;

  //void save(boolean activate, String dealerCode, String salesRepCode) throws TelusAPIException;

  /**
   * Saves all attributes of this subscriber, including the contract,  to the
   * datastore using the subscriber's dealerCode and salesRepCode.  If this is
   * <B>NOT</B> the subscriber's first save or if the <CODE>startServiceDate</CODE>
   * argument is in the past (or <CODE>null</CODE>), the <CODE>startServiceDate</CODE>
   * argument will be ignored and this subscriber will be activated now.
   *
   * <P>This method may involve a remote method call.
   *
   * @param startServiceDate the future date to activate this subscriber.  This
   *                         parameter is only used for a subscriber's first save. This value should be <b>null</b> if it's not a future-dated transaction.
   */
  void save(Date startServiceDate) throws TelusAPIException;

  //void save(Date startServiceDate, String dealerCode, String salesRepCode) throws TelusAPIException;


  /* *
   * Saves a new Contract for this subscriber to the datastore using the
   * subscriber's dealerCode and salesRepCode.
   *
   * <P>This method may involve a remote method call.
   *
   */
  //void saveContract(Contract newContract) throws TelusAPIException;

  /* *
   * Saves a new Contract for this subscriber to the datastore using the
   * specified dealerCode and salesRepCode.
   *
   * @param dealerCode the dealer's code or <CODE>null</CODE>.
   * @param salesRepCode the salesRep's code or <CODE>null</CODE>.  A non-<CODE>null</CODE>
   *        <CODE>salesRepCode</CODE> requires a non-<CODE>null</CODE> <CODE>dealerCode</CODE>.
   *
   * <P>This method may involve a remote method call.
   *
   */
  //void saveContract(Contract newContract, String dealerCode, String salesRepCode) throws TelusAPIException;

  /**
   * Reloads this subscriber from the datastore, discarding any modifications
   * made since its last retrieval.
   *
   * <P>This method may involve a remote method call.
   */
  void refresh() throws TelusAPIException;

  /**
   * Puts this subscriber into an active state/status immediately.
   *
   * <P>This method may involve a remote method call.
   *
   * @deprecated replaced by activate(String reason) or activate(String reason, String memoText)
   *
   */
  void activate() throws TelusAPIException;


  /**
   * Puts this subscriber into an active state/status immediately.
   *
   * <P>This method may involve a remote method call.
   *
   * @param reason -- The code indicating the reason for the activation.
   *
   */
  void activate(String reason) throws TelusAPIException;


  /**
   * Puts this subscriber into an active state/status immediately.
   *
   * <P>This method may involve a remote method call.
   *
   * @param reason -- The code indicating the reason for the activation.
   * @param memoText -- Text to be added to memo
   *
   */
  void activate(String reason, String memoText) throws TelusAPIException;


  /**
   * Puts this subscriber into an active state/status some time in the future.
   *
   * <P>This method may involve a remote method call.
   *
   * @param startServiceDate the future date to activate this subscriber. This value should be <b>null</b> if it's not a future-dated transaction. 
   *
   * @deprecated replaced by activate(String reason, Date startServiceDate)
   *                      or activate(String reason, Date startServiceDate, String memoText)
   *
   */
  void activate(Date startServiceDate) throws TelusAPIException;


  /**
   * Puts this subscriber into an active state/status some time in the future.
   *
   * <P>This method may involve a remote method call.
   *
   * @param reason -- The code indicating the reason for the activation.
   * @param startServiceDate the future date to activate this subscriber. This value should be <b>null</b> if it's not a future-dated transaction.
   *
   */
  void activate(String reason, Date startServiceDate) throws TelusAPIException;


  /**
   * Puts this subscriber into an active state/status some time in the future.
   *
   * <P>This method may involve a remote method call.
   *
   * @param reason -- The code indicating the reason for the activation.
   * @param startServiceDate the future date to activate this subscriber. This value should be <b>null</b> if it's not a future-dated transaction.
   * @param memoText -- Text to be added to memo
   *
   */
  void activate(String reason, Date startServiceDate, String memoText ) throws TelusAPIException;


  /**
   * Releases this reserved subscriber and its associated resources.
   * Once this method completes the subscriber will be in the same state
   * it was when <CODE>acount.newXXXSubscriber</CODE> was called.
   *
   * <P>This method may involve a remote method call.
   *
   */
  void unreserve() throws TelusAPIException;


  /**
   * Reserves the primary mobile number for this subscriber.  Use <CODE>getPhoneNumber()</CODE>
   * to retrieve the newely reserved number.
   *
   * <P>This method may involve a remote method call.
   *
   * @param phoneNumberReservation the criteria for the new mobile number.
   *
   * @exception NumberMatchException when no number satisfied the phoneNumberReservation.
   *
   * @see #getPhoneNumber
   */
  void reservePhoneNumber(PhoneNumberReservation phoneNumberReservation) throws TelusAPIException, NumberMatchException;

  int getBanId();


  /**
   * Returns the unique id of this subscriber or null.
   *
   */
  String getSubscriberId();


  /**
   * Returns the mobile number associated with this subscriber or null.
   * Use reserveMobileNumber to assign the initial mobile number.
   *
   * @see #reservePhoneNumber
   *
   */
  String getPhoneNumber();

  /**
   * @deprecated to be removed in July 2006
   * @see #getConsumerName
   */
  String getFirstName();

  /**
   * This method is ostensibly rendered redundant by the addition of ConsumerName to the
   * interface, however Activations' requirements prevent deprecation of this method.  For
   * all other purposes, name methods should be through ConsumerName.
   *
   * @see #getConsumerName
   */
  void setFirstName(String firstName);

  /**
   * @deprecated to be removed in July 2006
   * @see #getConsumerName
   */
  String getMiddleInitial();

  /**
   * This method is ostensibly rendered redundant by the addition of ConsumerName to the
   * interface, however Activations' requirements prevent deprecation of this method.  For
   * all other purposes, name methods should be through ConsumerName.
   *
   * @see #getConsumerName
   */
  void setMiddleInitial(String middleInitial);

  /**
   * @deprecated to be removed in July 2006
   * @see #getConsumerName
   */
  String getLastName();

  /**
   * This method is ostensibly rendered redundant by the addition of ConsumerName to the
   * interface, however Activations' requirements prevent deprecation of this method.  For
   * all other purposes, name methods should be through ConsumerName.
   *
   * @see #getConsumerName
   */
  void setLastName(String lastName);

  /**
   * Returns the ConsumerName object associated with this subscriber.
   * Replaces the following deprecated methods:
   *
   * 	- getFirstName
   *	- setFirstName*
   *	- getMiddleInitial
   *	- setMiddleInitial*
   *	- getLastName
   *	- setLastName*
   */
  ConsumerName getConsumerName();

  String getLanguage();

  void setLanguage(String language);

  void setVoiceMailLanguage(String voiceMailLanguage);

  String getVoiceMailLanguage();

  void setDealerHasDeposit(boolean dealerHasDeposit);
  
  boolean getDealerHasDeposit();
  
  String getSerialNumber();

  String[] getSecondarySerialNumbers();
	/**
	 * Returns Client Segment
	 * Value source from User_seg in Subscriber table  
	 * Has logic to only return the letter after @
	 * 
	 * See AccountSummay.getBanSegment() for BAN associated segment
	 */
  String  getUserValueRating();
  
  SubscriptionPreference getSubscriptionPreference(int preferenceTopicId) throws TelusAPIException;

  /**
   * Returns one of the STATUS_xxx constants.
   *
   */
  char getStatus();

  String getMarketProvince();

  /**
   * Returns one of the PRODUCT_TYPE_xxx constants.
   *
   */
  String getProductType();

  /**
   * Returns the service order code (SOC) for this priceplan or <CODE>null</CODE>.
   * Use <CODE>saveContract()</CODE> or <CODE>setContract()</CODE> to associate a
   * new priceplan with this subscriber.
   *
   *
   * @see #getContract
   * @see com.telus.api.reference.PricePlan
   *
   */
  String getPricePlan();

  String getDealerCode();
  
  /*
   * This method should be called for new activation. For any other transactions, dealer Code  should be passed in, when the Client API method is invoked
   */

  void setDealerCode(String dealerCode);
 

  String getSalesRepId();
  
  /*
   * This method should be called for new activation. For any other transactions, SalesRep code should be passed in, when the Client API method is invoked
   */
  void setSalesRepId(String salesRepId);

  Date getBirthDate();

  void setBirthDate(Date birthDate);

  /**
   * Returns the owner of this subscriber.
   *
   * <P>The returned object will be a specific type of account (i.e. IDENPostpaidConsumerAccount,
   * PCSPostpaidBusinessPersonalAccount, etc.).  Use the isXXX() methods, the getAccountType() &
   * getAccountSubType() methods or <CODE>instanceof</CODE> to determine which one.
   *
   * <P>This method may involve a remote method call.
   *
   */
  Account getAccount() throws TelusAPIException;


  //===================================================================================
  // Activations and Priceplan changes
  //===================================================================================


  /**
   * Creates a new unsaved contract for this subscriber.
   *
   * <P>This method may involve a remote method call.
   *
   * @param pricePlan the pricePlan attached to this new agreement.
   *
   * InvalidPricePlanChangeException
   *
   */
  Contract newContract(PricePlan pricePlan, int term) throws InvalidPricePlanChangeException, TelusAPIException;

  /**
   * Creates a new unsaved contract for this subscriber with an associated equipment change.
   * Validations will be performed against the new equipment instead of the existing one.
   *
   * <P>This method may involve a remote method call.
   *
   * @param pricePlan the pricePlan attached to this new agreement.
   * @param equipmentChangeRequest the equipment change to associate with this priceplan change.
   *
   * @exception InvalidPricePlanChangeException
   *
   * @see #changeEquipment
   *
   */
  Contract newContract(PricePlan pricePlan, int term, EquipmentChangeRequest equipmentChangeRequest) throws InvalidPricePlanChangeException, TelusAPIException;


  //===================================================================================
  // Contract Renewals
  //===================================================================================


  /**
   * Creates a new unsaved contract for the purpose of renewing the agreement with this subscriber.
   *
   * <P>This method may involve a remote method call.
   *
   * @param term the pricePlan attached to this new agreement.
   *
   * @exception InvalidPricePlanChangeException
   *
   */
  Contract renewContract(int term) throws TelusAPIException;

  /**
   * Creates a new unsaved contract for the purpose of renewing the agreement with this subscriber.
   *
   * <P>This method may involve a remote method call.
   *
   * @param pricePlan the pricePlan attached to this new agreement.
   *
   * @exception InvalidPricePlanChangeException
   *
   */
  Contract renewContract(PricePlan pricePlan, int term) throws InvalidPricePlanChangeException, TelusAPIException;

  /**
   * Creates a new unsaved contract for the purpose of renewing the agreement with this subscriber.
   * Validations will be performed against the new equipment instead of the existing one.
   *
   * <P>This method may involve a remote method call.
   *
   * @param pricePlan the pricePlan attached to this new agreement.
   * @param equipmentChangeRequest the equipment change to associate with this priceplan change.
   *
   * @exception InvalidPricePlanChangeException
   *
   * @see #changeEquipment
   *
   */
  Contract renewContract(PricePlan pricePlan, int term, EquipmentChangeRequest equipmentChangeRequest) throws InvalidPricePlanChangeException, TelusAPIException;


  /**
   * Returns the contract associated with this subscriber or <CODE>null</CODE>.
   * The contract includes: price plan, optional & included socs, features, and term.
   *
   * <P>This method may involve a remote method call.
   *
   * @link aggregationByValue
   */
  Contract getContract() throws TelusAPIException;

  String getEmailAddress();

  void setEmailAddress(String emailAddress);

  String getFaxNumber();

  /**
   * Returns the equipment associated with this subscriber's serial number.
   *
   * <P>This method may involve a remote method call.
   *
   *
   * @exception UnknownSerialNumberException
   *
   */
  Equipment getEquipment() throws UnknownSerialNumberException, TelusAPIException;

  /**
   * Returns the date this account was created.
   *
   */
  Date getCreateDate();


  /**
   * Returns the date this account was moved to an active status, in other
   * words, the first time a subscriber was activated.
   */
  Date getStartServiceDate();

  /**
   * Sets the activity reason for the subscriber
   */
  void setActivityReasonCode(String activityReasonCode);

  /**
   * Gets the activity code for the subscriber
   */
  String getActivityCode();

  /**
   * Gets the activity reason code for the subscriber
   */
  String getActivityReasonCode();

  @Deprecated
  boolean isIDEN();

  boolean isPCS();

  /**
   * Retrieves mobile phone numbers this subscriber may assign.
   * This subscriber must already have a phone number.
   * Use <CODE>changePhoneNumber()</CODE> to assign the selected number.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   *
   * @param phoneNumberReservation the criteria for the new mobile number.
   * @param maximum the maximum number of AvailablePhoneNumber to return.
   *
   * @exception PhoneNumberException if the search criteria is inappropriate for this operation.
   *
   * @see #changePhoneNumber
   */
  AvailablePhoneNumber[] findAvailablePhoneNumbers(PhoneNumberReservation phoneNumberReservation, int maximum) throws TelusAPIException, PhoneNumberException;

  /**
   * Assigns a new primary mobile phone number to this subscriber.
   * This subscriber must already have a phone number.
   *
   * <P>This method may involve a remote method call.
   *
   *
   * @param availablePhoneNumber the new phone number to assign.
   * @param changeOtherNumbers indicate that other subscriber numbers (like fax)
   *        should be changed, if they exist.
   *
   * @exception PhoneNumberException if the availablePhoneNumber is inappropriate for this operation.
   * @exception PhoneNumberInUseException if the number is already being used by a subscriber.
   *
   * @see #findAvailablePhoneNumbers
   */
  void changePhoneNumber(AvailablePhoneNumber availablePhoneNumber, boolean changeOtherNumbers) throws TelusAPIException, PhoneNumberException, PhoneNumberInUseException;

  /**
   * Assigns a new primary mobile phone number to this subscriber.
   * This subscriber must already have a phone number.
   *
   * <P>This method may involve a remote method call.
   *
   *
   * @param availablePhoneNumber the new phone number to assign.
   * @param changeOtherNumbers indicate that other subscriber numbers (like fax)
   *        should be changed, if they exist.
   * @param dealerCode the new dealer code.
   * @param salesRepCode the new sales rep code.
   *
   * @exception PhoneNumberException if the availablePhoneNumber is inappropriate for this operation.
   * @exception PhoneNumberInUseException if the number is already being used by a subscriber.
   *
   * @see #findAvailablePhoneNumbers
   */
  void changePhoneNumber(AvailablePhoneNumber availablePhoneNumber, boolean changeOtherNumbers, String dealerCode, String salesRepCode) throws TelusAPIException, PhoneNumberException, PhoneNumberInUseException;

  /**
   * Assigns a new primary mobile phone number to this subscriber.
   * This subscriber must already have a phone number.
   *
   * <P>This method may involve a remote method call.
   *
   *
   * @param availablePhoneNumber the new phone number to assign.
   * @param changeOtherNumbers indicate that other subscriber numbers (like fax)
   *        should be changed, if they exist.
   * @param dealerCode the new dealer code.
   * @param salesRepCode the new sales rep code.
   * @param reasonCode phone number change reason code
   *
   * @exception PhoneNumberException if the availablePhoneNumber is inappropriate for this operation.
   * @exception PhoneNumberInUseException if the number is already being used by a subscriber.
   *
   * @see #findAvailablePhoneNumbers
   */
  void changePhoneNumber(AvailablePhoneNumber availablePhoneNumber, boolean changeOtherNumbers, String dealerCode, String salesRepCode, String reasonCode) throws TelusAPIException, PhoneNumberException, PhoneNumberInUseException;

  /**
   * Assigns additional phone numbers to this subscriber.
   * This subscriber must already have a phone number.
   *
   * <P>This method may involve a remote method call.
   *
   *
   * @param availablePhoneNumber the phone number to associate.
   *
   * @exception PhoneNumberException if the availablePhoneNumber is inappropriate for this operation.
   * @exception PhoneNumberInUseException if the number is already being used by a subscriber.
   *
   * @see #findAvailablePhoneNumbers
   */
  void reserveAdditionalPhoneNumber(AvailablePhoneNumber availablePhoneNumber) throws TelusAPIException, PhoneNumberException, PhoneNumberInUseException;

  /**
   * Creates a new unsaved memo associated with this subscriber.
   *
   * <P>This method may involve a remote method call.
   *
   * @see Memo#create
   */
  Memo newMemo() throws TelusAPIException;

  /**
   * Creates a new unsaved FollowUp associated with this subscriber.
   *
   * <P>This method may involve a remote method call.
   *
   * @see FollowUp#create
   */
  FollowUp newFollowUp() throws TelusAPIException;

  /**
   * Creates a new unsaved Charge associated with this subscriber.
   *
   * <P>This method may involve a remote method call.
   *
   * @see Charge#apply
   */
  Charge newCharge() throws TelusAPIException;

  /**
   * Creates a new unsaved and untaxable Credit associated with this subscriber.
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
   * subscriber.  A credit may still not be taxed when the <CODE>taxable</CODE>
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
   */
  Credit newCredit(char taxOption) throws TelusAPIException;

  /**
   * Creates a new unsaved Discount associated with this subscriber.
   *
   * <P>This method may involve a remote method call.
   *
   * @see Discount#apply
   */
  Discount newDiscount() throws TelusAPIException;

  /**
   * Returns existing and future discounts for this subscriber.
   *
   * <P>This method may involve a remote method call.
   *
   * @see #newDiscount
   * @see Discount#apply
   */
  Discount[] getDiscounts() throws TelusAPIException;


  /**
   * Test changeEquipment() method.
   *
   * <P>This method may involve a remote method call.
   *
   * @param newEquipment the subscrber's new equipment.
   * @param dealerCode
   * @param salesRepCode
   * @param requestorId
   * @param repairId Repair ID is mandatory except for 'Replacement' or SIM to SIM swaps. DUMMY_REPAIR_ID can be used for 'Repair' swaps performed by clients.
   * @param swapType one of the SWAP_TYPE_xxx constants.
   *
   * @exception SerialNumberInUseException when serialnumber is already in use
   * @exception InvalidEquipmentChangeException the equipment swap was inappropriate.
   *
   * @see #getEquipment
   */
  ApplicationMessage[] testChangeEquipment(Equipment newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException;

  /**
   * Test changeEquipment() method.
   *
   * <P>This method may involve a remote method call.
   *
   * @param newEquipment the subscrber's new equipment.
   * @param dealerCode
   * @param salesRepCode
   * @param requestorId
   * @param repairId Repair ID is mandatory except for 'Replacement' or SIM to SIM swaps. DUMMY_REPAIR_ID can be used for 'Repair' swaps performed by clients.
   * @param swapType one of the SWAP_TYPE_xxx constants.
   * @param ignoreSerialNoInUse
   *
   * @exception SerialNumberInUseException when serialnumber is already in use
   * @exception InvalidEquipmentChangeException the equipment swap was inappropriate.
   *
   * @see #getEquipment
   * @deprecated
   */
  ApplicationMessage[] testChangeEquipment(Equipment newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, boolean ignoreSerialNoInUse) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException;

  /**
   * Test changeEquipment() method.
   *
   * <P>This method may involve a remote method call.
   *
   * @param newEquipment the subscrber's new equipment.
   * @param dealerCode
   * @param salesRepCode
   * @param requestorId
   * @param repairId Repair ID is mandatory except for 'Replacement' or SIM to SIM swaps. DUMMY_REPAIR_ID can be used for 'Repair' swaps performed by clients.
   * @param swapType one of the SWAP_TYPE_xxx constants.
   * @param allowDuplicateSerialNo one of   SWAP_DUPLICATESERIALNO_ALLOWSAMEBAN,SWAP_DUPLICATESERIALNO_ALLOWOTHERBAN or
   *  		SWAP_DUPLICATESERIALNO_DONOTALLOW
   *      When <tt>SWAP_DUPLICATESERIALNO_ALLOWOTHERBAN</tt> is set, the operation will bypass checking duplicate serial no.
   *      When <tt>SWAP_DUPLICATESERIALNO_ALLOWSAMEBAN</tt> is set, <tt>SerialNumberInUseException</tt> will be thrown
   *        if <tt>newEquipment</tt> is already assigned to another subscriber in any other BAN
   *      When <tt>SWAP_DUPLICATESERIALNO_DONOTALLOW</tt> is set, <tt>SerialNumberInUseException</tt> will be thrown
   *        if <tt>newEquipment</tt> is already assigned to another subscriber in any BAN
   *
   * @exception SerialNumberInUseException when serialnumber is already in use
   * @exception InvalidEquipmentChangeException the equipment swap was inappropriate.
   *
   * @see #getEquipment
   */
  ApplicationMessage[] testChangeEquipment(Equipment newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, char allowDuplicateSerialNo) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException;

  /**
   * Test changeEquipment() method for IDENEquipment.
   *
   * <P>This method may involve a remote method call.
   *
   * @param newIDENEquipment the subscrber's new equipment.
   * @param dealerCode
   * @param salesRepCode
   * @param requestorId
   * @param repairId Repair ID is mandatory except for 'Replacement' or SIM to SIM swaps. DUMMY_REPAIR_ID can be used for 'Repair' swaps performed by clients.
   * @param swapType one of the SWAP_TYPE_xxx constants.
   * @param associatedMuleEquipment
   *
   * @exception SerialNumberInUseException when serialnumber is already in use
   * @exception InvalidEquipmentChangeException the equipment swap was inappropriate.
   *
   * @see #getEquipment
   * @deprecated
   */
  void testChangeEquipment(IDENEquipment newIDENEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, MuleEquipment associatedMuleEquipment) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException;

  /**
   * Test changeEquipment() method for IDENEquipment.
   *
   * <P>This method may involve a remote method call.
   *
   * @param newIDENEquipment the subscrber's new equipment.
   * @param dealerCode
   * @param salesRepCode
   * @param requestorId
   * @param repairId Repair ID is mandatory except for 'Replacement' or SIM to SIM swaps. DUMMY_REPAIR_ID can be used for 'Repair' swaps performed by clients.
   * @param swapType one of the SWAP_TYPE_xxx constants.
   * @param associatedMuleEquipment
   * @param ignoreSerialNoInUse
   *
   * @exception SerialNumberInUseException when serialnumber is already in use
   * @exception InvalidEquipmentChangeException the equipment swap was inappropriate.
   *
   * @see #getEquipment
   * @deprecated
   */
  void testChangeEquipment(IDENEquipment newIDENEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, MuleEquipment associatedMuleEquipment, boolean ignoreSerialNoInUse) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException;

  /**
   * Test changeEquipment() method for IDENEquipment.
   *
   * <P>This method may involve a remote method call.
   *
   * @param newIDENEquipment the subscrber's new equipment.
   * @param dealerCode
   * @param salesRepCode
   * @param requestorId
   * @param repairId Repair ID is mandatory except for 'Replacement' or SIM to SIM swaps. DUMMY_REPAIR_ID can be used for 'Repair' swaps performed by clients.
   * @param swapType one of the SWAP_TYPE_xxx constants.
   * @param associatedMuleEquipment
   * @param allowDuplicateSerialNo one of   SWAP_DUPLICATESERIALNO_ALLOWSAMEBAN,SWAP_DUPLICATESERIALNO_ALLOWOTHERBAN or
   *  		SWAP_DUPLICATESERIALNO_DONOTALLOW
   *      When <tt>SWAP_DUPLICATESERIALNO_ALLOWOTHERBAN</tt> is set, the operation will bypass checking duplicate serial no.
   *      When <tt>SWAP_DUPLICATESERIALNO_ALLOWSAMEBAN</tt> is set, <tt>SerialNumberInUseException</tt> will be thrown
   *        if <tt>newIDENEquipment</tt> is already assigned to another subscriber in any other BAN
   *      When <tt>SWAP_DUPLICATESERIALNO_DONOTALLOW</tt> is set, <tt>SerialNumberInUseException</tt> will be thrown
   *        if <tt>newIDENEquipment</tt> is already assigned to another subscriber in any BAN
   *
   * @exception SerialNumberInUseException when serialnumber is already in use
   * @exception InvalidEquipmentChangeException the equipment swap was inappropriate.
   *
   * @see #getEquipment
   * @deprecated
   */
  void testChangeEquipment(IDENEquipment newIDENEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, MuleEquipment associatedMuleEquipment, char allowDuplicateSerialNo) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException;

  /**
   * Assigns a new equipment to this subscriber.
   *
   * <P>This method may involve a remote method call.
   *
   * @param newEquipment the subscrber's new equipment.
   * @param dealerCode
   * @param salesRepCode
   * @param requestorId
   * @param repairId Repair ID is mandatory except for 'Replacement' or SIM to SIM swaps. DUMMY_REPAIR_ID can be used for 'Repair' swaps performed by clients.
   * @param swapType one of the SWAP_TYPE_xxx constants.
   *
   * @exception SerialNumberInUseException when serialnumber is already in use
   * @exception InvalidEquipmentChangeException the equipment swap was inappropriate.
   *
   * @see #getEquipment
   */
  ApplicationMessage[] changeEquipment(Equipment newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException;


  /**
   * Assigns a new equipment to this subscriber.
   *
   * <P>This method may involve a remote method call.
   *
   * @param newEquipment the subscrber's new equipment.
   * @param dealerCode
   * @param salesRepCode
   * @param requestorId
   * @param repairId Repair ID is mandatory except for 'Replacement' or SIM to SIM swaps. DUMMY_REPAIR_ID can be used for 'Repair' swaps performed by clients.
   * @param swapType one of the SWAP_TYPE_xxx constants.
   * @param preserveDigitalServices if true preserves digital services for digital to analog equipment changes.
   *
   * @exception SerialNumberInUseException when serialnumber is already in use
   * @exception InvalidEquipmentChangeException the equipment swap was inappropriate.
   *
   * @see #getEquipment
   */
  ApplicationMessage[] changeEquipment(Equipment newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, boolean preserveDigitalServices) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException;

  /**
   * Assigns a new equipment to this subscriber.
   *
   * <P>This method may involve a remote method call.
   *
   * @param newEquipment the subscrber's new equipment.
   * @param dealerCode
   * @param salesRepCode
   * @param requestorId
   * @param repairId Repair ID is mandatory except for 'Replacement' or SIM to SIM swaps. DUMMY_REPAIR_ID can be used for 'Repair' swaps performed by clients.
   * @param swapType one of the SWAP_TYPE_xxx constants.
   * @param preserveDigitalServices if true preserves digital services for digital to analog equipment changes.
   * @param ignoreSerialNoInUse if true can assign more than one subscriber to a given ESN.
   *
   * @exception SerialNumberInUseException when serialnumber is already in use
   * @exception InvalidEquipmentChangeException the equipment swap was inappropriate.
   *
   * @see #getEquipment
   * @deprecated
   */
  ApplicationMessage[] changeEquipment(Equipment newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, boolean preserveDigitalServices, boolean ignoreSerialNoInUse) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException;

  /**
   * Assigns a new equipment to this subscriber.
   *
   * <P>This method may involve a remote method call.
   *
   * @param newEquipment the subscrber's new equipment.
   * @param dealerCode
   * @param salesRepCode
   * @param requestorId
   * @param repairId Repair ID is mandatory except for 'Replacement' or SIM to SIM swaps. DUMMY_REPAIR_ID can be used for 'Repair' swaps performed by clients.
   * @param swapType one of the SWAP_TYPE_xxx constants.
   * @param preserveDigitalServices if true preserves digital services for digital to analog equipment changes.
   * @param allowDuplicateSerialNo one of   SWAP_DUPLICATESERIALNO_ALLOWSAMEBAN,SWAP_DUPLICATESERIALNO_ALLOWOTHERBAN or
   *  		SWAP_DUPLICATESERIALNO_DONOTALLOW
   *      When <tt>SWAP_DUPLICATESERIALNO_ALLOWOTHERBAN</tt> is set, the operation will bypass checking duplicate serial no.
   *      When <tt>SWAP_DUPLICATESERIALNO_ALLOWSAMEBAN</tt> is set, <tt>SerialNumberInUseException</tt> will be thrown
   *        if <tt>newEquipment</tt> is already assigned to another subscriber in any other BAN
   *      When <tt>SWAP_DUPLICATESERIALNO_DONOTALLOW</tt> is set, <tt>SerialNumberInUseException</tt> will be thrown
   *        if <tt>newEquipment</tt> is already assigned to another subscriber in any BAN
   *
   * @exception SerialNumberInUseException when serialnumber is already in use
   * @exception InvalidEquipmentChangeException the equipment swap was inappropriate.
   *
   * @see #getEquipment
   */
  ApplicationMessage[] changeEquipment(Equipment newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, boolean preserveDigitalServices, char allowDuplicateSerialNo) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException;

  /**
   * Assigns a new equipment to this subscriber.
   *
   * <P>This method may involve a remote method call.
   *
   * @param newEquipment the subscrber's new equipment.
   * @param dealerCode
   * @param salesRepCode
   * @param requestorId
   * @param repairId Repair ID is mandatory except for 'Replacement' or SIM to SIM swaps. DUMMY_REPAIR_ID can be used for 'Repair' swaps performed by clients.
   * @param swapType one of the SWAP_TYPE_xxx constants.
   * @param preserveDigitalServices if true preserves digital services for digital to analog equipment changes.
   * @param allowDuplicateSerialNo one of   SWAP_DUPLICATESERIALNO_ALLOWSAMEBAN,SWAP_DUPLICATESERIALNO_ALLOWOTHERBAN or
   *  		SWAP_DUPLICATESERIALNO_DONOTALLOW
   *      When <tt>SWAP_DUPLICATESERIALNO_ALLOWOTHERBAN</tt> is set, the operation will bypass checking duplicate serial no.
   *      When <tt>SWAP_DUPLICATESERIALNO_ALLOWSAMEBAN</tt> is set, <tt>SerialNumberInUseException</tt> will be thrown
   *        if <tt>newEquipment</tt> is already assigned to another subscriber in any other BAN
   *      When <tt>SWAP_DUPLICATESERIALNO_DONOTALLOW</tt> is set, <tt>SerialNumberInUseException</tt> will be thrown
   *        if <tt>newEquipment</tt> is already assigned to another subscriber in any BAN
   *  @param header
   *
   * @exception SerialNumberInUseException when serial number is already in use
   * @exception InvalidEquipmentChangeException the equipment swap was inappropriate.
   *
   * @see #getEquipment
   */
  ApplicationMessage[] changeEquipment(Equipment newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, boolean preserveDigitalServices, char allowDuplicateSerialNo, ServiceRequestHeader header) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException;

  /**
   * Assigns a new IDEN equipment to this subscriber.
   *
   * <P>This method may involve a remote method call.
   *
   * @param newIDENEquipment the subscrber's new equipment.
   * @param dealerCode
   * @param salesRepCode
   * @param requestorId
   * @param repairId Repair ID is mandatory except for 'Replacement' or SIM to SIM swaps. DUMMY_REPAIR_ID can be used for 'Repair' swaps performed by clients.
   * @param swapType one of the SWAP_TYPE_xxx constants.
   * @param associatedMuleEquipment
   *
   * @exception SerialNumberInUseException when serialnumber is already in use
   * @exception InvalidEquipmentChangeException the equipment swap was inappropriate.
   *
   * @see #getEquipment
   * @deprecated
   */
  void changeEquipment(IDENEquipment newIDENEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, MuleEquipment associatedMuleEquipment) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException;

  /**
   * Assigns a new IDEN equipment to this subscriber.
   *
   * <P>This method may involve a remote method call.
   *
   * @param newIDENEquipment the subscrber's new equipment.
   * @param dealerCode
   * @param salesRepCode
   * @param requestorId
   * @param repairId Repair ID is mandatory except for 'Replacement' or SIM to SIM swaps. DUMMY_REPAIR_ID can be used for 'Repair' swaps performed by clients.
   * @param swapType one of the SWAP_TYPE_xxx constants.
   * @param associatedMuleEquipment
   * @param ignoreSerialNoInUse if true can assign more than one subscriber to a given ESN.
   *
   * @exception SerialNumberInUseException when serialnumber is already in use
   * @exception InvalidEquipmentChangeException the equipment swap was inappropriate.
   *
   * @see #getEquipment
   * @deprecated
   */
  void changeEquipment(IDENEquipment newIDENEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, MuleEquipment associatedMuleEquipment, boolean ignoreSerialNoInUse) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException;

  /**
   * Assigns a new IDEN equipment to this subscriber.
   *
   * <P>This method may involve a remote method call.
   *
   * @param newIDENEquipment the subscrber's new equipment.
   * @param dealerCode
   * @param salesRepCode
   * @param requestorId
   * @param repairId Repair ID is mandatory except for 'Replacement' or SIM to SIM swaps. DUMMY_REPAIR_ID can be used for 'Repair' swaps performed by clients.
   * @param swapType one of the SWAP_TYPE_xxx constants.
   * @param associatedMuleEquipment
   * @param allowDuplicateSerialNo one of   SWAP_DUPLICATESERIALNO_ALLOWSAMEBAN,SWAP_DUPLICATESERIALNO_ALLOWOTHERBAN or
   *  		SWAP_DUPLICATESERIALNO_DONOTALLOW
   *      When <tt>SWAP_DUPLICATESERIALNO_ALLOWOTHERBAN</tt> is set, the operation will bypass checking duplicate serial no.
   *      When <tt>SWAP_DUPLICATESERIALNO_ALLOWSAMEBAN</tt> is set, <tt>SerialNumberInUseException</tt> will be thrown
   *        if <tt>newIDENEquipment</tt> is already assigned to another subscriber in any other BAN
   *      When <tt>SWAP_DUPLICATESERIALNO_DONOTALLOW</tt> is set, <tt>SerialNumberInUseException</tt> will be thrown
   *        if <tt>newIDENEquipment</tt> is already assigned to another subscriber in any BAN
   *
   * @exception SerialNumberInUseException when serialnumber is already in use
   * @exception InvalidEquipmentChangeException the equipment swap was inappropriate.
   *
   * @see #getEquipment
   * @deprecated
   */
  void changeEquipment(IDENEquipment newIDENEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, MuleEquipment associatedMuleEquipment, char allowDuplicateSerialNo) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException;

  /**
   * Creates a new equipment swap request for use in associating with contract changes.  No change is made to this subscriber or its equipement.
   *
   * <P>This method may involve a remote method call.
   *
   * @param newEquipment the subscrber's new equipment.
   * @param requestorId
   * @param repairId Repair ID is mandatory except for 'Replacement' or SIM to SIM swaps. DUMMY_REPAIR_ID can be used for 'Repair' swaps performed by clients.
   * @param swapType one of the SWAP_TYPE_xxx constants.
   *
   * @exception SerialNumberInUseException when serialnumber is already in use
   * @exception InvalidEquipmentChangeException the equipment swap was inappropriate.
   *
   * @see #getEquipment
   */
  EquipmentChangeRequest newEquipmentChangeRequest(Equipment newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException;

  /**
   * Creates a new equipment swap request for use in associating with contract changes.  No change is made to this subscriber or its equipement.
   *
   * <P>This method may involve a remote method call.
   *
   * @param newEquipment the subscrber's new equipment.
   * @param requestorId
   * @param repairId Repair ID is mandatory except for 'Replacement' or SIM to SIM swaps. DUMMY_REPAIR_ID can be used for 'Repair' swaps performed by clients.
   * @param swapType one of the SWAP_TYPE_xxx constants.
   *
   * @exception SerialNumberInUseException when serialnumber is already in use
   * @exception InvalidEquipmentChangeException the equipment swap was inappropriate.
   *
   * @see #getEquipment
   */
  EquipmentChangeRequest newEquipmentChangeRequest(Equipment newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, boolean preserveDigitalServices) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException;

  /**
   * Creates a new equipment swap request for use in associating with contract changes.  No change is made to this subscriber or its equipement.
   *
   * <P>This method may involve a remote method call.
   *
   * @param newEquipment the subscrber's new equipment.
   * @param requestorId
   * @param repairId Repair ID is mandatory except for 'Replacement' or SIM to SIM swaps. DUMMY_REPAIR_ID can be used for 'Repair' swaps performed by clients.
   * @param swapType one of the SWAP_TYPE_xxx constants.
   * @param allowDuplicateSerialNo one of the SWAP_DUPLICATESERIALNO_xxxx constants.
   *
   * @exception SerialNumberInUseException when serialnumber is already in use
   * @exception InvalidEquipmentChangeException the equipment swap was inappropriate.
   *
   * @see #getEquipment
   */
  EquipmentChangeRequest newEquipmentChangeRequest(Equipment newEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, boolean preserveDigitalServices, char allowDuplicateSerialNo) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException;

  /**
   * Reverts this subscriber's voice mail password to its default value.
   *
   * <P>This method may involve a remote method call.
   *
   */
  void resetVoiceMailPassword() throws TelusAPIException;

  /**
   * Returns one of the PROVISIONING_PLATFORM_xxx constants.
   */
  int getProvisioningPlatformId() throws TelusAPIException;

  /**
   * Return an array of provisioning transactions for the specified time period.
   * @param startDate
   * @param endDate
   * @return ProvisioningTransaction[]
   */
  ProvisioningTransaction[] getProvisioningTransactions( Date startDate, Date endDate ) throws TelusAPIException;

  /**
   * Returns a list of billed calls.
   *
   * @param		billSeqNo
   *
   * @return 	CallList
   */
  CallList getBilledCalls(int billSeqNo) throws TelusAPIException;

  /**
   * Returns a CallList
   *
   * @param billSeqNo int
   * @param callType char
   * @throws TelusAPIException
   * @return CallList
   */
  CallList getBilledCalls(int billSeqNo, char callType) throws TelusAPIException;

  /**
   * Returns a CallList
   * @param billSeqNo int
   * @param callType char
   * @param from Date
   * @param to Date
   * @param getAll boolean
   * @throws TelusAPIException
   * @return CallList
   */
  CallList getBilledCalls(int billSeqNo, char callType, Date from, Date to, boolean getAll) throws TelusAPIException;

  /**
   * Returns a list of billed calls.
   *
   * @param		billSeqNo
   * @param		from
   * @param		to
   * @param		getAll - if getAll is false, 600 billed call records are retrieved by default.
   *
   * @return 	CallList
   */
  CallList getBilledCalls(int billSeqNo, Date from, Date to, boolean getAll) throws TelusAPIException;

  /**
   * Returns a list of unbilled calls.
   *
   * @return 	CallList
   */
  CallList getUnbilledCalls() throws TelusAPIException;

  NumberGroup[] getAvailableNumberGroups() throws TelusAPIException;

  /**
   * Retrieve number group specific to market area
   *
   * <P>This method may involve a remote method call.
   *
   */
  NumberGroup[] getAvailableNumberGroups(String marketArea) throws TelusAPIException;

  /**
   * Retrieve number group specific to market area
   *
   * <P>This method may involve a remote method call.
   *
   */
  NumberGroup[] getAvailableNumberGroupsGivenNumberLocation(String numberLocation) throws TelusAPIException;

  /**
   * Retrieve contract change (acquisitions/renewals) history, given a specific date range.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * @return the list of transactions within the date range.
   *
   * @exception HistorySearchException if the search criteria is invalid
   *
   */
  ContractChangeHistory [] getContractChangeHistory (Date from, Date to) throws TelusAPIException, HistorySearchException;

  /**
   * Retrieve handset exchange history, given a specific date range.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * @return the list of transactions within the date range.
   *
   * @exception HistorySearchException if the search criteria is invalid
   *
   */
  HandsetChangeHistory [] getHandsetChangeHistory (Date from, Date to) throws TelusAPIException, HistorySearchException;

  /**
   * Retrieve price plan change history, given a specific date range.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * @return the list of transactions within the date range.
   *
   * @exception HistorySearchException if the search criteria is invalid
   *
   */
  PricePlanChangeHistory [] getPricePlanChangeHistory (Date from, Date to) throws TelusAPIException, HistorySearchException;

  /**
   * Retrieve service change (ie add/remove optional SOCs on a contract) history, given a specific date range.
   * Retrieves only optional services (in KNOWbility terms "regular")
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * @return the list of transactions within the date range.
   *
   * @exception HistorySearchException if the search criteria is invalid
   *
   */
  ServiceChangeHistory [] getServiceChangeHistory (Date from, Date to) throws TelusAPIException, HistorySearchException;

  /**
   * Retrieve service change  history, given a specific date range.
   * Retrieves all services
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * @return the list of transactions within the date range.
   *
   * @exception HistorySearchException if the search criteria is invalid
   *
   */
  ServiceChangeHistory [] getServiceChangeHistory (Date from, Date to,boolean includeAllServices) throws TelusAPIException, HistorySearchException;

  /**
   * Retrieve resource change (ie phone number, mike number or IP address) history,
   * given a resource type and specific date range.
   * NOTE that this method currently support MIKE only.  PCS will be supported
   * once all PCS resources (phone number and IP address) are available in knowbility.
   * Keep in mind that PCSSubscriber can return previous phone number information.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method will involve a remote method call.
   *
   * @see com.telus.api.account.PCSSubscriber
   * @return the list of transactions within the date range for the given resource type.
   *
   * @exception HistorySearchException if the search criteria is invalid
   *
   */
  ResourceChangeHistory [] getResourceChangeHistory (String type, Date from, Date to) throws TelusAPIException, HistorySearchException;

  /**
   * Applies a credit to this subscriber for the value of the card.
   *
   * <P>This method may involve a remote method call.
   *
   * @see Card#getAmount
   * @see Card#getAdjustmentCode
   *
   */
  void applyCredit(Card card) throws TelusAPIException;

  /**
   * Returns the cards activated on this subscriber.
   *
   * <P>The returned objects will be specific types of cards (i.e. FeatureCard, GameCard, or MinuteCard).
   * Use the isXXX() methods or <CODE>instanceof</CODE> to determine which ones.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.  It may also be freely modified.
   *
   * <P>This method may involve a remote method call.
   *
   *
   */
  Card[] getCards() throws TelusAPIException;

  /**
   * Returns the cards, of a specific type, activated on this subscriber.
   *
   * <P>The returned objects will be specific types of cards (i.e. FeatureCard, GameCard, or MinuteCard).
   * Use the isXXX() methods or <CODE>instanceof</CODE> to determine which ones.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.  It may also be freely modified.
   *
   * <P>This method may involve a remote method call.
   *
   *
   * @param cardType the only type of cards to return.  One of the Card.TYPE_xxx constants.
   *
   * @see com.telus.api.equipment.Card
   *
   */
  Card[] getCards(String cardType) throws TelusAPIException;


  /**
   * Returns the priceplans appropriate for this subscriber.  If this is an existing
   * subscriber (and not a new activation) the plans will be filtered by the resources
   * supported by the current contract.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.  It may also be freely modified.
   *
   * <P>This method may involve a remote method call.
   *
   *
   */
  PricePlanSummary[] getAvailablePricePlans() throws TelusAPIException;  
  
  /**
   * Equivalent to calling getAvailablePricePlans(false, String equipmentType). Returns the priceplans appropriate for this subscriber <b>with the specified Equipment Type</b>.  If this is an existing
   * subscriber (and not a new activation) the plans will be filtered by the resources
   * supported by the current contract.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.  It may also be freely modified.
   *
   * <P>This method may involve a remote method call.
   *

   *
   */
  PricePlanSummary[] getAvailablePricePlans(String equipmentType) throws TelusAPIException;


  /**
   * 
   * Will create a new query that takes equipment type into account and also network type.
   * 
   * @param getAll
   * @param equipmentType
   * @return PricePlanSummary[]
   * @throws TelusAPIException
   */
  PricePlanSummary[] getAvailablePricePlans(boolean getAll, String equipmentType) throws TelusAPIException;

  /**
   * 
   * This is an overloaded method that will retrieve Price Plan summary taking equipment type into account.
   * Will create a new query that takes equipment type into account and also network type.
   * 
   * @param telephonyEnabled
   * @param dispatchEnabled
   * @param webEnabled
   * @param term
   * @param isCurrentOnly
   * @param isActivationOnly
   * @param equipmentType
   * @return PricePlanSummary[]
   * @throws TelusAPIException
   */
  PricePlanSummary[] getAvailablePricePlans(boolean telephonyEnabled, boolean dispatchEnabled, boolean webEnabled, int term, 
  boolean isCurrentOnly, boolean isActivationOnly, String equipmentType) throws TelusAPIException;

  
  /**
   * Retrieve deposit history.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * @return the list of deposits .
   *
   * @exception HistorySearchException if the search criteria is invalid
   *
   */
  public DepositHistory[] getDepositHistory () throws TelusAPIException, HistorySearchException;

  /**
   * Retrieve refund history.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * @return the list of refunds within the date range.
   *
   * @exception HistorySearchException if the search criteria is invalid
   *
   */


  PricePlan getAvailablePricePlan(String pricePlanCode) throws TelusAPIException;

  PricePlan getAvailablePricePlan(ServiceSummary pricePlan) throws TelusAPIException;

  PricePlanSummary[] getAvailablePricePlans(boolean getAll) throws TelusAPIException;

  /**
   * Returns array of standard airtime unbilled usage summary. The standard
   * Airtime usage is identified in the database as featureCode = "STD".
   *
   * Use this method if you want to retrieve airtime usage only.
   * For other usages, please use getVoiceUsageSummary(String featureCode) instead.
   *
   * @return VoiceUsageSummary
   * @throws VoiceUsageSummaryException
   * @throws TelusAPIException
   */
  VoiceUsageSummary getVoiceUsageSummary() throws VoiceUsageSummaryException, TelusAPIException;

  /**
   * Returns array of unbilled usage summary for the specified feature code.
   *
   * @param featureCode
   * @return VoiceUsageSummary
   * @throws VoiceUsageSummaryException
   * @throws TelusAPIException
   */
  VoiceUsageSummary getVoiceUsageSummary(String featureCode) throws VoiceUsageSummaryException, TelusAPIException;

  /**
   * Returns WebUsageSummary object. Currently this method is not implemented.
   *
   * @return WebUsageSummary
   * @throws TelusAPIException
   */
  WebUsageSummary getWebUsageSummary() throws TelusAPIException;

  @Deprecated
  boolean isPager();

  @Deprecated
  boolean isTango();

  @Deprecated
  boolean isCDPD();



  /**
   * Returns subscription role code, and dealer code as stored in the CRDB table SUBSCRIPTION ROLE.
   * If no subscription role is found, the method will return null.
   * If a cached subscription role object is found in the Subscriber, this
   * object will be returned.
   * May involve a remote call.
   * @exception TelusAPIException
   */
  public SubscriptionRole getSubscriptionRole() throws TelusAPIException;

  /**
   * Sets the subscription role for the subscriber in CODS/CRDB. The method uses
   * the ban (banId) and subscriber number (subscriberId) as the primary key to
   * update the CRDB staging table STAGED_SUBSCRIPTION_ROLE. These are retrieved
   * from the Subscriber implicitly.
   * May involve a remote call.
   * @exception TelusAPIException
   * @param subscriptionRole The new role/delear code pair to be set.
   */

  void setSubscriptionRole(SubscriptionRole subscriptionRole) throws TelusAPIException;

  public SubscriptionRole newSubscriptionRole() throws TelusAPIException;

  /**
    * Returns the termination fee.  This is the fee that is charged to the account if the subscriber is cancelled.
    * (note:  this fee can be waived).
    *
    * <P>This method may involve a remote method call.
    *
    * @return double
    * @see #cancel
    */
  double getTerminationFee() throws TelusAPIException;

  /**
    * Cancels the subscriber.  The termination fee is not waived.
    *
    * <P>This method may involve a remote method call.
    *
    * @param reason -- The code indicating the reason the subscriber was cancelled.
    * @param depositReturnMethod -- The code indicating how the deposit (if any) should be returned.
    *    valid values are:  O (cover open debts), R (refund entire amount), or E (refund excess amount)
    */
  void cancel(String reason, char depositReturnMethod) throws TelusAPIException;

  /**
    * Cancels the subscriber.  The termination fee is waived if the waiver reason is given (it is not null
    * or empty).
    *
    * <P>If this is the only non-cancelled subscriber, then the entire account will be cancelled.
    *
    * <P>This method may involve a remote method call.
    *
    * @param reason -- The code indicating the reason the subscriber was cancelled.
    * @param depositReturnMethod -- The code indicating ow the deposit (if any) should be returned.
    *    valid values are:  O (cover open debts), R (refund entire amount), or E (refund excess amount)
    * @param waiverReason -- The code indicating the reason the termination fee was waived (or null if the fee is not waived).
    * @see #cancel
    */
  void cancel(String reason, char depositReturnMethod, String waiverReason) throws TelusAPIException;

  void cancel(Date activityDate, String reason, char depositReturnMethod, String waiverReason, String memoText) throws TelusAPIException;

  /**
    * Suspends this subscriber.
    *
    * <P>If this is the last active subscriber, then the entire account will be suspended.
    *
    * <P>This method may involve a remote method call.
    *
    * @param reason -- The code indicating the reason the subscriber was suspended.
    */
  void suspend(String reason) throws TelusAPIException;

  void suspend(Date activityDate, String reason, String memoText) throws TelusAPIException;

  /**
    * Restores the subscriber from either a suspended or cancelled state.  Note, the account must
    * be open before the subscriber can be restored.
    *
    * <P>This method may involve a remote method call.
    *
    * @param reason -- The code indicating why the subscriber is being restored.
    */
  void restore(String reason) throws TelusAPIException;

  void restore(Date activityDate, String reason, String memoText) throws TelusAPIException;

  /**
    * Returns the NumberGroup associated with this subscriber's phonenumber.
    *
    * <P>This method may involve a remote method call.
    *
    */
  public NumberGroup getNumberGroup() throws TelusAPIException;

  /**
   * Returns appropriate reasons for cancelling this subscriber.
   *
   * <P><P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   */
  ReasonType[] getAvailableCancellationReasons() throws TelusAPIException;

  /**
   * Returns appropriate reasons for suspending this subscriber.
   *
   * <P><P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   */
  ReasonType[] getAvailableSuspensionReasons() throws TelusAPIException;

  /**
   * Returns appropriate reasons for resuming this subscriber from cancellation or suspension.
   *
   * <P><P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   */
  ReasonType[] getAvailableResumptionReasons() throws TelusAPIException;

  /**
    * Determines what the account status will be if this subscriber is suspended and returns it
    * If the status will not change (or it is known it will fail), this returns the value
    * ACCOUNT_STATUS_NO_CHANGE.
    *
    * @return char -- One of the AccountSummary.STATUS_XXX constants or ACCOUNT_STATUS_NO_CHANGE
    */
  char getAccountStatusChangeAfterSuspend() throws TelusAPIException;

  /**
    * Returns what the status of the owning account will be if this subscriber is canceled,
    * or ACCOUNT_STATUS_NO_CHANGE if the status will not change (or it is known it will fail)
    *
    * @return char -- One of the AccountSummary.STATUS_XXXX constants or ACCOUNT_STATUS_NO_CHANGE.
    */
  char getAccountStatusChangeAfterCancel() throws TelusAPIException;

  /**
   * Returns tax exemption details:
   * whether subscriber is GST, PST or HST exempt, and the respective exemption expiry dates
   * @return TaxExemption
   * @throws TelusAPIException
   */
  TaxExemption getTaxExemption() throws TelusAPIException;

  /**
   * Returns the most recent memo of the specified type for this subscriber.
   *
   * <P>This method may involve a remote method call.
   *
   * @exception TelusAPIException if no memo of the type exists on this
   *            account, or other uncaught exception.
   *
   */
  Memo getLastMemo(String memoType) throws TelusAPIException;

  /**
   * Retrieve subscriber history, given a specific date range.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * @return the list of transactions within the date range.
   *
   * @exception HistorySearchException if the search criteria is invalid
   *
   */
  SubscriberHistory [] getHistory (Date from, Date to) throws TelusAPIException, HistorySearchException;

  /**
   * Moves subscriber from current BAN to new BAN (TOWN)
   * Subscriber and BANs need to be active.
   *
   * <P>This method may involve a remote method call.
   *
   * Only subscribers with active status can be moved from BAN to BAN and the target BAN for moving a
   * subscriber can be in any status except ‘close’.
   * transferOwnership is set to true - User name and user address of the moved subscriber will
   * default to the billing name and billing address of the target BAN. Credit evaluation is
   * required for the target BAN to create a new deposit accordingly.
   * transferOwnership is set to false -The user name and user address is not affected by the move
   * and credit evaluation need not be performed. However, a credit class has to exist in the target
   * BAN. The deposit held in the source BAN on behalf of the moved subscriber will be moved to the
   * target BAN.
   */
  void move(Account account, boolean transferOwnership, String reasonCode, String memoText) throws TelusAPIException;


  /**
   * Moves subscriber from current BAN to new BAN (TOWN)
   * Subscriber and BANs need to be active.
   *
   * <P>This method may involve a remote method call.
   *
   * Only subscribers with active status can be moved from BAN to BAN and the target BAN for moving a
   * subscriber can be in any status except ‘close’.
   * transferOwnership is set to true - User name and user address of the moved subscriber will
   * default to the billing name and billing address of the target BAN. Credit evaluation is
   * required for the target BAN to create a new deposit accordingly.
   * transferOwnership is set to false -The user name and user address is not affected by the move
   * and credit evaluation need not be performed. However, a credit class has to exist in the target
   * BAN. The deposit held in the source BAN on behalf of the moved subscriber will be moved to the
   * target BAN.
   * @exception TelusAPIException
   * @param account - the account to which the subscriber is being moved
   * @param transferOwnership - transfer Ownership
   * @param reasonCode - the reason Code
   * @param memoText - the Memo Text
   * @param dealerCode - the Knowbility Dealer Code
   * @param salesRepCode - the Knowbility Sales Rep Code
   */
  void move(Account account, boolean transferOwnership, String reasonCode, String memoText,String dealerCode, String salesRepCode) throws TelusAPIException;

  /** Returns list of Price Plans, which subscriber can be changed to
   * during suspension with chosen reason
    *
    * <P>This method may involve a remote method call.
    *
    */
  PricePlanSummary[] getSuspensionPricePlans(String reasonCode) throws TelusAPIException;

  /**
   * Returns the date this subscriber's status was last changed.
   */
  Date getStatusDate();

  /**
   * Sends a fax to the fax number specified.
   *
   * @param form int The form to be used for fax
   * @param faxNumber String The fax number in the form of 4162340000
   * @param language String The language preference
   * @throws TelusAPIException
   */
  void sendFax(final int form, String faxNumber, String language) throws EquipmentWarrantyNotAvailableException,  TelusAPIException;

  /**
   * Sends an email to the address specified.
   *
   * @param form int The form to be used for email.
   * @param email String The email address.
   * @param language String The language preference
   *
   * @throws TelusAPIException
   */
  void sendEmail(final int form, String email, String language) throws EquipmentWarrantyNotAvailableException, TelusAPIException;

  /**
   * Refreshes the switch for an acive subscriber
   *
   * <P>This method may involve a remote method call.
   *
   */
  void refreshSwitch() throws TelusAPIException;

  /**
   * Retrieves the provisioning status of a subscriber by its account number and phonenumber or <CODE>null</CODE>.
   * Returns one of the PROVISINING_STATUS_xxx constants.
   *
   * <P>This method may involve a remote method call.
   *
   */
  String getProvisioningStatus() throws TelusAPIException;

  /**
   * This method is re-instated as part of Handset Transparency April 2011 release along with the decommissioning of Billing Inquiry Service.
   * @return CancellationPenalty
   * @throws TelusAPIException
   */
  CancellationPenalty getCancellationPenalty() throws TelusAPIException;

  /**
   * Returns one of the SUPPORT_LEVEL_xxx constants.
   *
   *
   * <P>This method may involve a remote method call.
   *
   */
  String getSupportLevel() throws TelusAPIException;

/**
   * Returns one of the SLA_LEVEL_xxx constants.
   *
   *
   * <P>This method may involve a remote method call.
   *
   */
  String getSLALevel() throws TelusAPIException;

  InvoiceCallSortOrderType getInvoiceCallSortOrder() throws TelusAPIException;

  void setInvoiceCallSortOrder(String invoiceCallSortOrder) throws TelusAPIException;

  void createDeposit(double Amount, String memoText) throws TelusAPIException;

//  LMSLetterRequest newLMSLetterRequest(Letter letter) throws TelusAPIException;

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

  //Credit[] getPromotionalCredits() throws TelusAPIException;

  /**
   * Return a <code>Credit</code> array for this subscriber - subscriber-level credits
   * @param from Date
   * @param to Date
   * @param billState String State of the credit ie. billed, unbilled or all (@see Account.BILL_STATE_BILLED, @see Account.BILL_STATE_UNBILLED, @see Account.BILL_STATE_ALL)
   * @return Credit[]
   * @throws TelusAPIException
   */
  Credit[] getCredits(Date from, Date to, String billState) throws TelusAPIException;

  /**
   * Return a <code>Credit</code> array for this subscriber - subscriber-level credits
   * @param from Date
   * @param to Date
   * @param billState State of the credit ie. billed, unbilled or all (@see Account.BILL_STATE_BILLED, @see Account.BILL_STATE_UNBILLED, @see Account.BILL_STATE_ALL)
   * @param reasonCode String
   * @param knowbilityOperatorId ie. particular kb operator id to filter the credits to. If null, method will retrieve all credits regardless of kb operator id.
   * @return Credit[]
   */
  Credit[] getCreditsByReasonCode(Date from, Date to, String billState, String reasonCode, String knowbilityOperatorId) throws TelusAPIException;

  /**
   * Return a <code>Credit</code> array for this subscriber - subscriber-level credits
   * @param from Date
   * @param to Date
   * @param billState State of the credit ie. billed, unbilled or all (@see Account.BILL_STATE_BILLED, @see Account.BILL_STATE_UNBILLED, @see Account.BILL_STATE_ALL)
   * @param reasonCode String
   * @return Credit[]
   */
  Credit[] getCreditsByReasonCode(Date from, Date to, String billState, String reasonCode) throws TelusAPIException;
  
  /**
   * Retrieves address associated with the given subscriber. Remote call.
   *
   * @return Address
   * @throws AddressNotFoundException
   */
  Address getAddress() throws TelusAPIException, AddressNotFoundException;

  /**
   * Retrieves address associated with the given subscriber. Remote call.
   *
   * @param refresh
   * @return Address
   * @throws AddressNotFoundException
   */
  Address getAddress(boolean refresh) throws TelusAPIException, AddressNotFoundException;

  /**
   * Set new subscriber address. Actual DB update is implemented when save() is called.
   *
   * @param newAddress
   * @throws TelusAPIException
   */
  void setAddress(Address newAddress) throws TelusAPIException;

  /**
   * Removes the furture-dated price plan change.
   *
   * <P>This method may involve a remote method call.
   *
   * @throws TelusAPIException
   */
  void removeFutureDatedPricePlanChange() throws TelusAPIException;

  /**
   * Retrieves the equipment change history given a specific date range.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   * @deprecated Deprecated this method since SD will call SEMS new Web Service for PCS equipment change history
   * @param from
   * @param to
   *
   * @return the list of transactions within the date range.
   *
   * @throws TelusAPIException
   * @throws HistorySearchException if the search criteria is invalid.
   */
  EquipmentChangeHistory[] getEquipmentChangeHistory(Date from, Date to) throws TelusAPIException, HistorySearchException;

  SubscriberCommitment getCommitment();

  InvoiceTax getInvoiceTax(int billSeqNo) throws TelusAPIException;

  UsageProfileListsSummary getUsageProfileListsSummary(int billSeqNo) throws TelusAPIException;


  /**
   * Returns a boolean value.
   * @return boolean
   */
  boolean isHotlined();

  
  /**
   * Returns a boolean value.
   * @return boolean
   * Method isValidMigrationTypeForPhoneNumber (…) will make a remote call to the Resource Management System
   * and will throw a TelusAPIException in case of failure
   */

  public boolean isValidMigrationForPhoneNumber(MigrationType migrationType, String sourceNetowrkType, String targetNetworkType) throws TelusAPIException;

  /**
   * Returns migration date.
   * @return Date
   */
  Date getMigrationDate();

  /**
   * Returns reference MigrationType object.
   * @return MigrationType
   */
  MigrationType getMigrationType();

  public QueueThresholdEvent[] getQueueThresholdEvents(Date from, Date to) throws TelusAPIException;

  public long getSubscriptionId() throws TelusAPIException;

  public double getRequestedSecurityDeposit();

  /**
   * Saves all attributes of this subscriber, including the contract,  to the
   * datastore using the subscriber's dealerCode and salesRepCode.  If this is
   * <B>NOT</B> the subscriber's first save, the <CODE>activate</CODE> argument
   * will be ignored.
   *
   * <P>This method may involve a remote method call.
   *
   * @param activate whether to activate this subscriber or leave it in a
   *                 reserved state.  This parameter is only used for a
   *                 subscriber's first save.
   * @param selectedOption  The activation option selected by client.
   */
  void save(boolean activate, ActivationOption selectedOption) throws TelusAPIException;

  /**
   * Saves all attributes of this subscriber, including the contract,  to the
   * datastore using the subscriber's dealerCode and salesRepCode.  If this is
   * <B>NOT</B> the subscriber's first save or if the <CODE>startServiceDate</CODE>
   * argument is in the past (or <CODE>null</CODE>), the <CODE>startServiceDate</CODE>
   * argument will be ignored and this subscriber will be activated now.
   *
   * <P>This method may involve a remote method call.
   *
   * @param startServiceDate the future date to activate this subscriber.  This
   *                         parameter is only used for a subscriber's first save. This value should be <b>null</b> if it's not a future-dated transaction.
   * @param selectedOption  The activation option selected by client.
   */
  void save(Date startServiceDate, ActivationOption selectedOption) throws TelusAPIException;

  /**
   * Moves subscriber from current BAN to new BAN (TOWN)
   * Subscriber and BANs need to be active.
   *
   * <P>This method may involve a remote method call.
   *
   * Only subscribers with active status can be moved from BAN to BAN and the target BAN for moving a
   * subscriber can be in any status except ‘close’.
   * transferOwnership is set to true - User name and user address of the moved subscriber will
   * default to the billing name and billing address of the target BAN. Credit evaluation is
   * required for the target BAN to create a new deposit accordingly.
   * transferOwnership is set to false -The user name and user address is not affected by the move
   * and credit evaluation need not be performed. However, a credit class has to exist in the target
   * BAN. The deposit held in the source BAN on behalf of the moved subscriber will be moved to the
   * target BAN.
   */
  void move(Account account, boolean transferOwnership, String reasonCode, String memoText, ActivationOption selectedOption) throws TelusAPIException;


  /**
   * Moves subscriber from current BAN to new BAN (TOWN)
   * Subscriber and BANs need to be active.
   *
   * <P>This method may involve a remote method call.
   *
   * Only subscribers with active status can be moved from BAN to BAN and the target BAN for moving a
   * subscriber can be in any status except ‘close’.
   * transferOwnership is set to true - User name and user address of the moved subscriber will
   * default to the billing name and billing address of the target BAN. Credit evaluation is
   * required for the target BAN to create a new deposit accordingly.
   * transferOwnership is set to false -The user name and user address is not affected by the move
   * and credit evaluation need not be performed. However, a credit class has to exist in the target
   * BAN. The deposit held in the source BAN on behalf of the moved subscriber will be moved to the
   * target BAN.
   * @exception TelusAPIException
   * @param account - the account to which the subscriber is being moved
   * @param transferOwnership - transfer Ownership
   * @param reasonCode - the reason Code
   * @param memoText - the Memo Text
   * @param dealerCode - the Knowbility Dealer Code
   * @param salesRepCode - the Knowbility Sales Rep Code
   * @param selectedOption - activation option selected by client
   */
  void move(Account account, boolean transferOwnership, String reasonCode, String memoText,String dealerCode, String salesRepCode, ActivationOption selectedOption) throws TelusAPIException;

  double getPaidSecurityDeposit() throws TelusAPIException;

	void setSerialNumber(String serialNumber) throws TelusAPIException;
	void setSecondarySerialNumbers(String[ ] secondarySerialNumbers) throws TelusAPIException;

  Subscriber  retrieveSubscriber(String phoneNumber) throws TelusAPIException;
  
  /**
   * Returns the brand ID (ie. telus, Amp'd, )
   */
  int getBrandId();

  /**
   * retrieve Calling circle phone number list history within given time frame
   *
   * @param from - start date
   * @param	to - end date
   * @return array of CallingCirclePhoneList
   *
   * @throws TelusAPIException
   **/
  CallingCirclePhoneList[] getCallingCirclePhoneNumberListHistory( Date from, Date to ) throws TelusAPIException;
  
  /**
   * retrieve Feature parameter history for given parameter names within given time frame
   *
   * @param parameterNames -  array of parameter name
   * @param from - start date
   * @param	to - end date
   * @return array of FeatureParameterHistory
   *
   * @throws TelusAPIException
   **/
  FeatureParameterHistory[] getFeatureParameterHistory( String[] parameterNames, Date from, Date to ) throws TelusAPIException;
  
  /**
   * retrieve Feature parameter change history within given time frame
   *
   * @param from - start date
   * @param	to - end date
   * @return array of FeatureParameterHistory
   *
   * @throws TelusAPIException
   **/
  FeatureParameterHistory[] getFeatureParameterChangeHistory(Date from, Date to) throws TelusAPIException;

  /**
   * Returns the set of subscriber inter-brand porting reason codes for activation, cancellation and
   * resume from cancellation activities.
   * 
   * @return array of String
   */
  String[] getSubscriberInterBrandPortActivityReasonCodes() throws TelusAPIException;
  
  /**
   * Returns the set of SOCs with promotions already applied to subsbcriber's profile during the
   * restricted period of time.
   */
  VendorServiceChangeHistory[] getVendorServiceChangeHistory(String SOC) throws TelusAPIException;
  
  /**
   * Assigns a new IDEN equipment to this subscriber.
   *
   * <P>This method may involve a remote method call.
   *
   * @param newIDENEquipment the subscrber's new equipment.
   * @param dealerCode
   * @param salesRepCode
   * @param requestorId
   * @param repairId Repair ID is mandatory except for 'Replacement' or SIM to SIM swaps. DUMMY_REPAIR_ID can be used for 'Repair' swaps performed by clients.
   * @param swapType one of the SWAP_TYPE_xxx constants.
   * @param associatedMuleEquipment
   * @param allowDuplicateSerialNo one of   SWAP_DUPLICATESERIALNO_ALLOWSAMEBAN,SWAP_DUPLICATESERIALNO_ALLOWOTHERBAN or
   *  		SWAP_DUPLICATESERIALNO_DONOTALLOW
   *      When <tt>SWAP_DUPLICATESERIALNO_ALLOWOTHERBAN</tt> is set, the operation will bypass checking duplicate serial no.
   *      When <tt>SWAP_DUPLICATESERIALNO_ALLOWSAMEBAN</tt> is set, <tt>SerialNumberInUseException</tt> will be thrown
   *        if <tt>newIDENEquipment</tt> is already assigned to another subscriber in any other BAN
   *      When <tt>SWAP_DUPLICATESERIALNO_DONOTALLOW</tt> is set, <tt>SerialNumberInUseException</tt> will be thrown
   *        if <tt>newIDENEquipment</tt> is already assigned to another subscriber in any BAN
   *
   * @param header <code>ServiceRequestHeader</code> if not null, will be write to SRPDS
   * @exception SerialNumberInUseException when serialnumber is already in use
   * @exception InvalidEquipmentChangeException the equipment swap was inappropriate.
   *
   * @see #getEquipment
   * @deprecated
   */
  void changeEquipment(IDENEquipment newIDENEquipment, String dealerCode, String salesRepCode, 
		  String requestorId, String repairId, String swapType, MuleEquipment associatedMuleEquipment, 
		  char allowDuplicateSerialNo, ServiceRequestHeader header) 
  throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException;

  /**
   * Assigns a new primary mobile phone number to this subscriber and update SRPDS if header is not null.
   * This subscriber must already have a phone number.
   *
   * <P>This method may involve a remote method call.
   *
   *
   * @param availablePhoneNumber the new phone number to assign.
   * @param changeOtherNumbers indicate that other subscriber numbers (like fax)
   *        should be changed, if they exist.
   * @param dealerCode the new dealer code.
   * @param salesRepCode the new sales rep code.
   * @param reasonCode phone number change reason code
   * @param header <code>ServiceRequestHeader</code> if not null, will be write to SRPDS
   *
   * @exception PhoneNumberException if the availablePhoneNumber is inappropriate for this operation.
   * @exception PhoneNumberInUseException if the number is already being used by a subscriber.
   *
   * @see #findAvailablePhoneNumbers
   */
  void changePhoneNumber(AvailablePhoneNumber availablePhoneNumber, 
		  boolean changeOtherNumbers, String dealerCode, String salesRepCode, 
		  String reasonCode, ServiceRequestHeader header) 
  throws TelusAPIException, PhoneNumberException, PhoneNumberInUseException;

  /**
   * Cancels the subscriber.  The termination fee is waived if the waiver reason is given (it is not null
   * or empty).
   *
   * <P>If this is the only non-cancelled subscriber, then the entire account will be cancelled.
   *
   * <P>This method may involve a remote method call.
   *
   * @param activityDate -- date
   * @param reason -- The code indicating the reason the subscriber was cancelled.
   * @param depositReturnMethod -- The code indicating ow the deposit (if any) should be returned.
   *    valid values are:  O (cover open debts), R (refund entire amount), or E (refund excess amount)
   * @param waiverReason -- The code indicating the reason the termination fee was waived (or null if the fee is not waived).
   * @param memoText -- the Memo Text
   * @param header <code>ServiceRequestHeader</code> if not null, will be write to SRPDS
   * @see #cancel
   */
  void cancel(Date activityDate, String reason, char depositReturnMethod, 
		  String waiverReason, String memoText, ServiceRequestHeader header) throws TelusAPIException;
  
  /**
   * Moves subscriber from current BAN to new BAN (TOWN)
   * Subscriber and BANs need to be active.
   *
   * <P>This method may involve a remote method call.
   *
   * Only subscribers with active status can be moved from BAN to BAN and the target BAN for moving a
   * subscriber can be in any status except ‘close’.
   * transferOwnership is set to true - User name and user address of the moved subscriber will
   * default to the billing name and billing address of the target BAN. Credit evaluation is
   * required for the target BAN to create a new deposit accordingly.
   * transferOwnership is set to false -The user name and user address is not affected by the move
   * and credit evaluation need not be performed. However, a credit class has to exist in the target
   * BAN. The deposit held in the source BAN on behalf of the moved subscriber will be moved to the
   * target BAN.
   * @exception TelusAPIException
   * @param account - the account to which the subscriber is being moved
   * @param transferOwnership - transfer Ownership
   * @param reasonCode - the reason Code
   * @param memoText - the Memo Text
   * @param dealerCode - the Knowbility Dealer Code
   * @param salesRepCode - the Knowbility Sales Rep Code
   * @param selectedOption - activation option selected by client
   * @param header <code>ServiceRequestHeader</code> if not null, will be write to SRPDS
   */
  void move(Account account, boolean transferOwnership, String reasonCode, 
		  String memoText,String dealerCode, String salesRepCode, 
		  ActivationOption selectedOption, ServiceRequestHeader header) throws TelusAPIException;

  /**
   * Puts this subscriber into an active state/status some time in the future.
   *
   * <P>This method may involve a remote method call.
   *
   * @param reason -- The code indicating the reason for the activation.
   * @param startServiceDate the futrure date to activate this subscriber.
   * @param memoText -- Text to be added to memo
   * @param header <code>ServiceRequestHeader</code> if not null, will be write to SRPDS
   *
   */
  void activate(String reason, Date startServiceDate, String memoText, 
		  ServiceRequestHeader header ) throws TelusAPIException;

  /**
   * Restores the subscriber from either a suspended or cancelled state and write service request to SRPDS.  
   * Note, the account must be open before the subscriber can be restored. 
   *
   * <P>This method may involve a remote method call.
   *
   * @param activityDate -- date
   * @param reason -- The code indicating why the subscriber is being restored.
   * @param memoText -- the memo text
   * @param header -- <code>ServiceRequestHeader</code> if not null, will be write to SRPDS
   */
  void restore(Date activityDate, String reason, String memoText, 
		  ServiceRequestHeader header) throws TelusAPIException;
  
  /**
   * Return the end state contract. This method is to be used to retrieve end state contract prior to commitment 
   * for an equipment change. The returned contract is nto the actaul contract associated with the subscriber, save(..) on the returned 
   * Contract shall never be called. To complete the equiopment change, existing methods and process should be followed.  
   * 
   * @param equipmentChangeRequest - a request object that caputre all the change parameter for a equipment change.
   * @return end state contract prior to commitment for an equipment change.  
   *  @throws TelusAPIException
   */
  Contract getContractForEquipmentChange( EquipmentChangeRequest equipmentChangeRequest ) throws TelusAPIException;
  
  /**
   * This method will return Airtime Rate as defined for subscriber rate plan (including if the subscriber has exception airtime rate). 
   * In case that multiple airtime rates exist for this subscriber, the method will throw InvalidAirtimeRateException. 
   * Subscriber has to be in Active or Suspended status otherwise InvalidSubscriberStatusException will be thrown.
   * This does not apply to prepaid.
   * 
   * @return Airtime Rate.  
   * @throws InvalidAirtimeRateException if no single flat airtime rate fetched
   * @throws InvalidSubscriberStatusException if subscriber is NOT active or suspended
   * @throws TelusAPIException 
   */
  
  double getAirtimeRate() throws InvalidAirtimeRateException, InvalidSubscriberStatusException, TelusAPIException;
  
     void setSeatData(SeatData seatData);
     
     /**
      * This method return all seat resource data associated with Business connect subscriber.
      * @return SeatData
      */
      
	 SeatData getSeatData();
  
 
}
