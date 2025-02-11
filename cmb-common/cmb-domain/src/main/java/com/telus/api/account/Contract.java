/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import java.util.Calendar;
import java.util.Date;

import com.telus.api.InvalidCardChangeException;
import com.telus.api.InvalidServiceChangeException;
import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.equipment.Card;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.PricePlan;
import com.telus.api.reference.RatedFeature;
import com.telus.api.reference.Service;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.api.task.ContractChangeTask;


/**
 * <CODE>Contract</CODE>
 *
 */
public interface Contract {

  /*
  public static final String COMMITMENT_REASON_ = "EQU";
  public static final String COMMITMENT_REASON_ = "MAN";
  public static final String COMMITMENT_REASON_ = "PPD";
  public static final String COMMITMENT_REASON_ = "TTL";
  public static final String COMMITMENT_REASON_ = "VRT";
  */

  /**
   * Retrieves the PricePlan associated with this Contract.  The PricePlan may
   * no longer be in active use (grandfathered).
   *
   * <P>This method may involve a remote method call.
   *
   * @link aggregation
   */
  PricePlan getPricePlan() throws TelusAPIException;

  ContractService addService(Service service) throws InvalidServiceChangeException, TelusAPIException;

  ContractService addService(String serviceCode) throws UnknownObjectException, InvalidServiceChangeException, TelusAPIException;

  ContractService addService(Service service, Date effectiveDate, Date expiryDate) throws InvalidServiceChangeException, TelusAPIException;

  void removeService(String serviceCode) throws UnknownObjectException, InvalidServiceChangeException, TelusAPIException;
  
  void removeService(ContractService service) throws UnknownObjectException, InvalidServiceChangeException, TelusAPIException;
  
  void removeService(String serviceCode, Date effectiveDate) throws UnknownObjectException, InvalidServiceChangeException, TelusAPIException;

  int getServiceCount();


  /**
   * Returns features directly on this contract.  This method does not
   * look at features on services on this contract.
   *
   * @link aggregationByValue
   *
   * returns Contract Price Plan's Features
   *
   */
  ContractFeature[] getFeatures();

  /**
   * Returns features on this contract, possibly including those found on
   * services on this contract.
   *
   * @param includeServices <CODE>true</CODE> to include features on services.
   *
   * @link aggregationByValue
   *
   * returns Contract Price Plan's Features
   *
   */
  ContractFeature[] getFeatures(boolean includeServices);

  /**
   * @link aggregationByValue
   */
  ContractService[] getServices();

  ContractService[] getIncludedServices();

  ContractService[] getOptionalServices();

  /**
   * Returns services added to this contract.  This method should be called after changing this
   * contract, but prior to calling <CODE>save()</CODE>.  The returned array will include
   * services that were automatically added.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   */
  ContractService[] getAddedServices();

  /**
   * Returns services updated on this contract.  This method should be called after changing this
   * contract, but prior to calling <CODE>save()</CODE>.  The returned array will include
   * services that were automatically added.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   */
  ContractService[] getChangedServices();

  /**
   * Returns services removed from this contract.  This method should be called after changing this
   * contract, but prior to calling <CODE>save()</CODE>.  The returned array will include
   * services that were automatically added.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   */
  ContractService[] getDeletedServices();

  ContractFeature[] getAddedFeatures();

  ContractFeature[] getChangedFeatures();

  ContractFeature[] getDeletedFeatures();

 /**
  * Returns true if additions and deletions of shareable services
  * will be propagated down to other subscribers on the same priceplan.
  * The default is false--not to propagate changes.
  *
  */
  boolean getCascadeShareableServiceChanges();

  /**
   * Sets whether additions and deletions of shareable services
   * will be propagated down to other subscribers on the same priceplan.
   * The default is false--not to propagate changes.
   *
   */
  void setCascadeShareableServiceChanges(boolean cascadeShareableServiceChanges);

  ContractService getService(String code) throws UnknownObjectException;
  
  /**
   * Retrieves contract services with code as a key prefix and not a full key.
   * @param code
   * @return
   * @throws UnknownObjectException
   */
  ContractService[] getServices(String code) throws UnknownObjectException;

  Date getExpiryDate();

  void setExpiryDate(Date expiryDate);

  Date getEffectiveDate();

  void setEffectiveDate(Date effectiveDate);

  //String getPricePlanCode();

  double getRecurringChargeForShareableServices() throws TelusAPIException;

  double getRecurringCharge() throws TelusAPIException;

  boolean isTelephonyEnabled() throws TelusAPIException;

  boolean isDispatchEnabled() throws TelusAPIException;

  boolean isWirelessWebEnabled() throws TelusAPIException;


  /**
   * Saves this Contract to the datastore using the
   * default dealerCode and salesRepCode.
   * If default values are not found, subscriber's dealerCode
   * and salesRepCode will be used instead.
   *
   * <P>This method may involve a remote method call.
   *
   * @exception PhoneNumberException if the automatic phone number reservation
   *            for services like Fax and Voice Mail fails.
   *
   */
  void save() throws InvalidServiceChangeException, TelusAPIException, PhoneNumberException;


  /**
   * Saves this Contract to the datastore using the
   * specified dealerCode and salesRepCode.
   *
   * @param dealerCode the dealer's code or <CODE>null</CODE>.
   * @param salesRepCode the salesRep's code or <CODE>null</CODE>.  A non-<CODE>null</CODE>
   *        <CODE>salesRepCode</CODE> requires a non-<CODE>null</CODE> <CODE>dealerCode</CODE>.
   *
   * <P>This method may involve a remote method call.
   *
   * @exception PhoneNumberException if the automatic phone number reservation
   *            for services like Fax and Voice Mail fails.
   *
   */
  void save(String dealerCode, String salesRepCode) throws InvalidServiceChangeException, TelusAPIException, PhoneNumberException;

  /**
   * Reloads this contract from the datastore, discarding any modifications
   * made since its last retrieval.
   *
   * <P>This method may involve a remote method call.
   *
   */
  void refresh() throws TelusAPIException;


  /**
   * Returns silently if the service can be added to this Contract.  This method
   * performs the following checks:
   *
   * (TODO: update this list)
   *
   * <OL START="1" TYPE="1">
   *   <LI>Does this service conflict with a service/feature already on the contract?
   *   <LI>Is the service in the priceplan's optional list?
   *   <LI>Is this service already on the contract?
   *   <LI>Do the account and service have the same billing type (ie. postpaid)?
   * </OL>
   *
   * <P>This method may involve a remote method call.
   *
   * @return the service as it was passed in.
   *
   */
  Service testAddition(Service service) throws InvalidServiceChangeException, TelusAPIException;


  /**
   * Returns silently if the service can be added to this Contract.  This method
   * performs the following checks:
   *
   * (TODO: update this list)
   *
   * <OL START="1" TYPE="1">
   *   <LI>Does this service conflict with a service/feature already on the contract  taking into considerations the effective date?
   *   <LI>Is the service in the priceplan's optional list?
   *   <LI>Is this service already on the contract?
   *   <LI>Do the account and service have the same billing type (ie. postpaid)?
   * </OL>
   *
   * <P>This method may involve a remote method call.
   *
   * @return the service as it was passed in.
   *
   */

  Service testAddition(Service service,Date effectiveDate) throws InvalidServiceChangeException, TelusAPIException;


  /**
   * Returns silently if all the services can be added to this Contract.
   *
   * <P>This method may involve a remote method call.
   *
   * @return the services as they were passed in.
   *
   * @see #testAddition(Service service)
   *
   */
  Service[] testAddition(Service[] service) throws InvalidServiceChangeException, TelusAPIException;


  /**
   * Returns silently if the card and its services can be added to this Contract.
   *
   * <P>This method may involve a remote method call.
   *
   * @param card the card who's services should be tested.
   * @param autoRenew <CODE>true</CODE> if the related feature-card services should be
   *                  tested, otherwise <CODE>false</CODE>.
   *
   * @return the card's services, not including related services.
   *
   * @see #testAddition(Service service)
   *
   */
  Service[] testAddition(Card card, boolean autoRenew) throws InvalidCardChangeException, TelusAPIException;


  /**
   * Returns silently if the service can be removed from this Contract.  This method
   * performs the following checks:
   *
   * (TODO: update this list)
   *
   * <OL START="1" TYPE="1">
   *   <LI>Is this a promotional service?
   *   <LI>Is this a bound service?
   * </OL>
   *
   * <P>This method may involve a remote method call.
   *
   * @return the service as it was passed in.
   *
   */
  ContractService testRemoval(ContractService contractService) throws InvalidServiceChangeException, TelusAPIException;


  /**
   * Adds a card's services to this contract, not including its related optional services.
   * This contract is not actually updated until <CODE>save()</CODE> is called.
   *
   * @param card the card who's services should be added.
   *
   * @see #testAddition
   * @see #save
   * @see com.telus.api.equipment.Card#TYPE_FEATURE
   *
   *
   */
  ContractService[] addCard(Card card) throws InvalidCardChangeException, TelusAPIException;


  /**
   * Adds a card's services to this contract, possibly including its related feature-card, optional services.
   * This contract is not actually updated until <CODE>save()</CODE> is called.
   *
   * @param card the card who's services should be added.
   * @param autoRenew <CODE>true</CODE> if the related feature-card services should be
   *                  added, otherwise <CODE>false</CODE>.
   *
   * @see #testAddition
   * @see #save
   * @see com.telus.api.equipment.Card#TYPE_FEATURE
   *
   */
  ContractService[] addCard(Card card, boolean autoRenew) throws InvalidCardChangeException, TelusAPIException;
  /**
   *
   * @return int
   */
  int getCommitmentMonths();
  /**
   *
   * @param commitmentMonths int
   */
  void setCommitmentMonths(int commitmentMonths);
  /**
   *
   * @return Date
   */
  Date getCommitmentStartDate();
  /**
   *
   * @param commitmentStartDate Date
   */
  void setCommitmentStartDate(Date commitmentStartDate);
  /**
   *
   * @return Date
   */
  Date getCommitmentEndDate();

  //void setCommitmentEndDate(Date commitmentEndDate);
  /**
   *
   * @return String
   */
  String getCommitmentReasonCode();

  /**
   *
   * @param commitmentReasonCode String
   */
  void setCommitmentReasonCode(String commitmentReasonCode);

  /**
   *
   * @return boolean
   */
  boolean isCrossFleetRestricted();

  /**
   * Returns the pending equipment change request associated with this contract or <CODE>null</CODE>
   * if none was set.
   *
   * @see #setEquipmentChangeRequest
   *
   */
  EquipmentChangeRequest getEquipmentChangeRequest();

  /**
   * Associates an equipment change request with this contract; to be comitted when this contract is saved.
   *
   * @exception TelusAPIException if this contract has already been modified or an EquipmentChangeRequest
   *            has already been associated with this contract.
   *
   */
  void setEquipmentChangeRequest(EquipmentChangeRequest equipmentChangeRequest) throws TelusAPIException;

  /**
   *
   * @throws TelusAPIException
   * @return boolean
   */
  boolean isSuppressPricePlanRecurringCharge() throws TelusAPIException;

  /**
   *
   * @param featureCode String
   * @throws UnknownObjectException
   * @throws TelusAPIException
   */
  void removeFeature(String featureCode) throws UnknownObjectException, TelusAPIException;

  /**
   * Check to see if PTT service is included on the contract
   * @deprecated
   * @return boolean
   */
  boolean isPTTServiceIncluded();

  /**
   *
   * @param code String
   * @return boolean
   */
  boolean containsService(String code);
//  boolean containsIncludedService(String code);
//  boolean containsOptionalService(String code);

  /**
    * Returns <CODE>true</CODE> if this subscriber is on a ShareablePricePlan and happens
    * to be the primary (the one charged the full recurring fee), otherwise <CODE>false</CODE>.
    */
  boolean isShareablePricePlanPrimary() throws TelusAPIException;

  /**
    * Returns <CODE>true</CODE> if this subscriber is on a ShareablePricePlan and isn't
    * the primary (the one charged the full recurring fee), otherwise <CODE>false</CODE>.
    */
  boolean isShareablePricePlanSecondary() throws TelusAPIException;
  
  /**
   * Returns <CODE>true</CODE> if this contract is on a ShareablePricePlan as a primary or secondary,
   * otherwise <CODE>false</CODE>.
   * 
   * @return boolean
   * @throws TelusAPIException
   */
  boolean isShareable() throws TelusAPIException;
  
  /**
   * Returns <CODE>true</CODE> if this contract is on a Dollar Pooling plan, otherwise <CODE>false</CODE>.
   * 
   * @return boolean
   * @throws TelusAPIException
   */
  boolean isDollarPooling() throws TelusAPIException;

  /**
   * Returns the most recent set of cascading Subscriber/Contract
   * changes triggered by calling <code>Contract.save()</code> within
   * the current session.
   *
   * <P>Cascading changes are not attempted if the main operation failed.
   *
   * <P>The returned tasks are guaranteed to have already completed,
   * normally or otherwise.
   *
   */
  ContractChangeTask[] getCascadingContractChanges();

  /**
   * Returns <CODE>true</CODE> if the contract has been modified.
   * e.g.
   * - services/features were added/removed/changed
   * - commitment was changed
   */
  boolean isModified();

  /**
   * Return <code>ServicePromotion[]</code>
   * @param businessRole String
   * @return ServicePromotion[]
   */
  //ServicePromotion[] getServicePromotionsToAdd(String businessRole) throws TelusAPIException;

  /**
   * Return <code>ServicePromotion[]</code>
   * @param businessRole String
   * @return ServicePromotion[]
   */
  //ServicePromotion[] getServicePromotionsToRemove(String businessRole) throws TelusAPIException;


  /**
   * Adds a feature to this contract (actually to the contract's price plan).
   * This contract is not actually updated until <CODE>save()</CODE> is called.
   *
   * @param feature the feature which should be added.
   * @see #save
   */
  ContractFeature addFeature(RatedFeature feature) throws UnknownObjectException, InvalidServiceChangeException, TelusAPIException;

  /**
   * Determines if contract's price plan contains feature.
   * @param featureCode
   * @return boolean
   */
  boolean containsPricePlanFeature(String featureCode);

  /**
   * Returns multi-ring phone numbers ONLY if the subscriber is PCS and contract contains multi-ring feature.
   * @throws TelusAPIException
   * @return String[]
   */
  String[] getMultiRingPhoneNumbers();

  /**
   * Set multi-ring phone numbers
   * @param phoneNumbers String[]
   */
  void setMultiRingPhoneNumbers(String[] phoneNumbers);

  /**
   * Returns 911 contract services
   * @return ContractService[]
   */
  ContractService[] get911Services();

  /**
   * Returns a double value of 911 charges
   * @return double
   */
  double get911Charges();

  /**
   * Returns <CODE>true</CODE> if this contract contains a pooling feature with an airtime coverage type, 
   * otherwise <CODE>false</CODE>.
   * Note: This will NOT provide anyindication of the specific pooling group the feature is associated with.
   * Use the isPoolingEnabled(int poolingGroupId) method instead to determine this.
   *  
   * @return boolean
   * @see #isPoolingEnabled(int)
   */
  boolean isAirtimePoolingEnabled();
  
  /**
   * Returns <CODE>true</CODE> if this contract contains a pooling feature with an long distance coverage type,
   * otherwise <CODE>false</CODE>.
   * Note: This will NOT provide any indication of the specific pooling group the feature is associated with.
   * Use the isPoolingEnabled(int poolingGroupId) method instead to determine this.
   *  
   * @return boolean
   * @see #isPoolingEnabled(int)
   */
  boolean isLDPoolingEnabled();
  
  /**
   * Returns <CODE>true</CODE> if this contract contains a pooling feature associated with the provided
   * pooling group ID, otherwise <CODE>false</CODE>.
   * 
   * @param poolingGroupId poolingGroupId
   * @return boolean
   * @throws TelusAPIException
   */
  boolean isPoolingEnabled(int poolingGroupId) throws TelusAPIException;;
  
  PricePlanValidation getPricePlanValidation(); 
  
  CallingFeatureCycle calculatePrepaidFeatureCycleDates() throws TelusAPIException;

  /**
   * Saves this Contract to the datastore using the
   * specified dealerCode and salesRepCode and write service request to SRPDS if header is not null.
   *
   * @param dealerCode the dealer's code or <CODE>null</CODE>.
   * @param salesRepCode the salesRep's code or <CODE>null</CODE>.  A non-<CODE>null</CODE>
   *        <CODE>salesRepCode</CODE> requires a non-<CODE>null</CODE> <CODE>dealerCode</CODE>.
   * @param header <code>ServiceRequestHeader</code> if not null, will be write to SRPDS
   * 
   * <P>This method may involve a remote method call.
   *
   * @exception PhoneNumberException if the automatic phone number reservation
   *            for services like Fax and Voice Mail fails.
   *
   */
  void save(String dealerCode, String salesRepCode, ServiceRequestHeader header) throws InvalidServiceChangeException, TelusAPIException, PhoneNumberException;
  
  
  
  /**
   * Check network type compatibility of target equipment and services on contract, return a list of service currently on 
   * contract which will be removed if subscriber swaps to targetEquipment due to service not supporting network of the targetEquipment
   * @param targetEquipment
   * @return  a list of service that will be dropped. The list may include 
   * 	regular add-on services (service type ‘R’), 
   * 	included services from price plan (service type ‘O’) 
   * 	and bounded services (service type ‘G’)
   * The array can be empty array, but never be null. 
   * @throws TelusAPIException
   */
  Service[] getServicesRestrictedByNetworkType(Equipment targetEquipment) throws TelusAPIException;
  
  /**
   * Look into history to find out the most recent calling circle phone number list, use that list to 
   * populate the empty calling circle phone number list in this Contract. 
   * 
   * @throws TelusAPIException
   */
   void prepopulateCallingCircleList() throws TelusAPIException;
   
  /**
   * Return whether or not this contract contain any uncommitted change that is eligible for notification should the change is committed.
   * 
   * @return
   */
   boolean getNotificationDisplayableInd();
   
   /**
   * Used to add a duration service (x-hour SOC) to the contract – after calling save() method.  If requested
   * effectiveDate and effectiveStartTime overlap with the same x-hour SOC already on the subscriber’s profile,
   * the new ContractService will be “laddered”, that is, the effectiveDate and effectiveStartTime
   * of the returned ContractService will reflect the start time immediately following the start time of the
   * previous service instance.
   * 
   * @param service
   * @param effectiveDate
   * @param numberOfReplications
   * @return a collection of added ContractService instances
   * @throws TelusAPIException
   */
   ContractService[] addDurationServices(Service service, Calendar effectiveDate, int numberOfReplications) throws TelusAPIException;
   
   /**
	 * This method works as a proxy for service addition after PricePlan change ONLY.
	 * When the PricePlan changed - all services from an old contract are deleted
	 * and some are usually added back depending on user's request. Therefore, in this method only 
	 * duration service marked as DELETED are expected. All others will be causing exception suggesting
	 * to use addDurationService interface for adding XHOUR SOCs.
	 * 
	 * @param service
	 * @return
	 * @throws TelusAPIException
	 */
   public ContractService addService(ContractService service) throws TelusAPIException;

   /**
   * Method determines if duration service will be laddered.  If no exception is thrown, the duration service
   * specified by service can be added with no conflicts at the specified effectiveDate and effectiveStartTime.
   * If service to be added will result in the duration service being “laddered”, InvalidServiceChangeException
   * is thrown with a InvalidServiceChangeException.LADDERED_SERVICE reason code and the new laddered
   * service start date and time can be retrieved from InvalidServiceChangeException.getContractService()
   */
   void testDurationServicesAddition(Service service, Calendar effectiveDate, int numberOfReplications) throws InvalidServiceChangeException, TelusAPIException;

}
