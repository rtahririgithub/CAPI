/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import java.util.Date;

import com.telus.api.InvalidChangeMemberIdException;
import com.telus.api.InvalidMigrationRequestException;
import com.telus.api.InvalidPricePlanChangeException;
import com.telus.api.TelusAPIException;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.IDENEquipment;
import com.telus.api.equipment.MuleEquipment;
import com.telus.api.fleet.Fleet;
import com.telus.api.fleet.MemberIdentity;
import com.telus.api.fleet.ReservePortInMemberIdentity;
import com.telus.api.fleet.TalkGroup;
import com.telus.api.portability.PRMSystemException;
import com.telus.api.portability.PortInEligibility;
import com.telus.api.portability.PortRequest;
import com.telus.api.portability.PortRequestException;
import com.telus.api.portability.PortRequestSummary;
import com.telus.api.reference.PricePlan;
import com.telus.api.servicerequest.ServiceRequestHeader;

@Deprecated
public interface IDENSubscriber extends Subscriber {
	
	//===================================================================================
	// Activations and Priceplan changes
	//===================================================================================

	/**
	 * Creates a new unsaved contract for this subscriber.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param pricePlan the pricePlan attached to this new agreement.
	 * @param dispatchOnly indicates if the new contract will be treated as dispatch only.
	 *
	 * @exception InvalidPricePlanChangeException
	 *
	 */
	Contract newContract(PricePlan pricePlan, int term, boolean dispatchOnly) throws InvalidPricePlanChangeException, TelusAPIException;

	/**
	 * Creates a new unsaved contract for this subscriber with an associated equipment change.
	 * Validations will be performed against the new equipment instead of the existing one.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param pricePlan the pricePlan attached to this new agreement.
	 * @param dispatchOnly indicates if the new contract will be treated as dispatch only.
	 * @param equipmentChangeRequest the equipment change to associate with this priceplan change.
	 *
	 * @exception InvalidPricePlanChangeException
	 *
	 * @see #changeEquipment
	 *
	 */
	Contract newContract(PricePlan pricePlan, int term, boolean dispatchOnly, EquipmentChangeRequest equipmentChangeRequest) throws InvalidPricePlanChangeException, TelusAPIException;

	//===================================================================================
	// Contract Renewals
	//===================================================================================

	/**
	 * Creates a new IDEN equipment swap request for use in associating with contract changes.  No change is made to this subscriber or its equipement.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param newIDENEquipment the subscrber's new equipment.
	 * @param requestorId
	 * @param repairId Repair ID is mandatory except for 'Replacement' or SIM to SIM swaps. DUMMY_REPAIR_ID can be used for 'Repair' swaps performed by clients.
	 * @param swapType one of the SWAP_TYPE_xxx constants.
	 * @param associatedMuleEquipment
	 *
	 * @exception SerialNumberInUseException when serialnumber is already in use
	 * @exception InvalidEquipmentChangeException the equipment swap was inappropriate.
	 *
	 * @see #getEquipment
	 */
	EquipmentChangeRequest newEquipmentChangeRequest(IDENEquipment newIDENEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, MuleEquipment associatedMuleEquipment) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException;

	/**
	 * Creates a new IDEN equipment swap request for use in associating with contract changes.  No change is made to this subscriber or its equipement.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param newIDENEquipment the subscrber's new equipment.
	 * @param requestorId
	 * @param repairId Repair ID is mandatory except for 'Replacement' or SIM to SIM swaps. DUMMY_REPAIR_ID can be used for 'Repair' swaps performed by clients.
	 * @param swapType one of the SWAP_TYPE_xxx constants.
	 * @param associatedMuleEquipment
	 * @param allowDuplicateSerialNo one of the SWAP_DUPLICATESERIALNO_xxxx constants.
	 *
	 * @exception SerialNumberInUseException when serialnumber is already in use
	 * @exception InvalidEquipmentChangeException the equipment swap was inappropriate.
	 *
	 * @see #getEquipment
	 */
	EquipmentChangeRequest newEquipmentChangeRequest(IDENEquipment newIDENEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, MuleEquipment associatedMuleEquipment, char allowDuplicateSerialNo) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException;
	
	/**
	 * Creates a new unsaved contract for the purpose of renewing the agreement with this subscriber.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param pricePlan the pricePlan attached to this new agreement.
	 * @param dispatchOnly indicates if the new contract will be treated as dispatch only.
	 *
	 * @exception InvalidPricePlanChangeException
	 *
	 */
	Contract renewContract(PricePlan pricePlan, int term, boolean dispatchOnly) throws InvalidPricePlanChangeException, TelusAPIException;

	/**
	 * Creates a new unsaved contract for the purpose of renewing the agreement with this subscriber.
	 * Validations will be performed against the new equipment instead of the existing one.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param pricePlan the pricePlan attached to this new agreement.
	 * @param dispatchOnly indicates if the new contract will be treated as dispatch only.
	 * @param equipmentChangeRequest the equipment change to associate with this priceplan change.
	 *
	 * @exception InvalidPricePlanChangeException
	 *
	 * @see #changeEquipment
	 *
	 */
	Contract renewContract(PricePlan pricePlan, int term, boolean dispatchOnly, EquipmentChangeRequest equipmentChangeRequest) throws InvalidPricePlanChangeException, TelusAPIException;

	/**
	 * Reserves a member identity using a pattern.  Use <CODE>getMemberIdentity()</CODE>
	 * to retrieve the newely reserved value.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param fleet the context fleet for the new identity.
	 * @param memberIdPatttern the criteria for the new identity.
	 *
	 * @exception UnknownSubscriberException if this subscriber doesn't already exist.
	 * @exception MemberIdMatchException when no number satisfies this reservation.
	 *
	 * @see #getMemberIdentity
	 */
	void reserveMemberId(Fleet fleet, String memberIdPatttern) throws UnknownSubscriberException, MemberIdMatchException, OutOfRangeMemberIdException, TelusAPIException;

	/**
	 * Reserves a member identity at random.
	 * Use <CODE>getMemberIdentity()</CODE> to retrieve the newely reserved value.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param fleet the context fleet for the new identity.
	 *
	 * @exception UnknownSubscriberException if this subscriber doesn't already exist.
	 * @exception MemberIdMatchException when no number satisfies this reservation.
	 *
	 * @see #getMemberIdentity
	 */
	void reserveMemberId(Fleet fleet) throws UnknownSubscriberException, TelusAPIException;

	/**
	 * Reserves a member identity at random Based on PTN Fleet.
	 * Use <CODE>getMemberIdentity()</CODE> to retrieve the newely reserved value.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 *
	 *
	 * @exception UnknownSubscriberException if this subscriber doesn't already exist.
	 *
	 *
	 * @see #getMemberIdentity
	 */
	void reservePTNBasedMemberId () throws UnknownSubscriberException, TelusAPIException;

	/**
	 * Returns the member identity assigned to this subscriber or null.
	 * Use reserveMemberIdxxx to assign a value.
	 *
	 * @see #reserveMemberId
	 * 
	 *
	 */
	MemberIdentity getMemberIdentity();

	/**
	 * Reserves an IP Address at random.
	 * Use <CODE>getIPAddress()</CODE> to retrieve the newely reserved value.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @exception UnknownSubscriberException if this subscriber doesn't already exist.
	 *
	 * @see #getIPAddress
	 */
	void reserveIPAddress() throws UnknownSubscriberException, TelusAPIException;

	/**
	 * Returns the IP Address assigned to this subscriber or null.
	 * Use reserveIPAddress to assign a value.
	 *
	 * @see #reserveIPAddress
	 *
	 */
	String getIPAddress();

	/**
	 * Returns the fleet assigned to this subscriber or <CODE>null</CODE>.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @exception UnknownSubscriberException if this subscriber doesn't already exist.
	 *
	 * @link aggregation
	 */
	Fleet getFleet() throws UnknownSubscriberException, TelusAPIException;

	/**
	 * Assigns a set of talkGroups to this subscriber.  This method will assign the
	 * fleet to the subscriber and associate it and the talkgroup to the account,
	 * if they haven't already been done.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param talkGroup the non-null array of talkGroups.
	 *
	 * @exception UnknownSubscriberException if this subscriber doesn't already exist.
	 * @exception InvalidFleetException if the talkGroups are not part of this subscriber's fleet.
	 */
	void setTalkGroups(TalkGroup[] talkGroup) throws UnknownSubscriberException, InvalidFleetException, TelusAPIException;

	/**
	 * Attaches/Detaches talkGroups to this subscriber.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param talkGroupsToAdd the array of talkGroups to be added.
	 * @param talkGroupsToRemove the array of talkGroups to be removed.
	 *
	 * @exception UnknownSubscriberException if this subscriber doesn't already exist.
	 * @exception InvalidFleetException if the talkGroups are not part of this subscriber's fleet.
	 */
	void addAndRemoveTalkGroups(TalkGroup[] talkGroupsToAdd, TalkGroup[] talkGroupsToRemove) throws UnknownSubscriberException, InvalidFleetException, TelusAPIException;

	/**
	 * Returns the talkGroups assigned to this subscriber.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * @exception UnknownSubscriberException if this subscriber doesn't already exist.
	 *
	 * @link aggregation
	 */
	TalkGroup[] getTalkGroups() throws UnknownSubscriberException, TelusAPIException;

	/**
	 * Sets the alias for the subscriber
	 */
	void setSubscriberAlias(String subscriberAlias);

	/**
	 * Returns the alias for the subscriber
	 */
	String getSubscriberAlias() ;

	/**
	 * Returns true if the Subscriber belong to the PTN BASED FLEET
	 * Return false if subscriber Phone only or NO fleet assoicate to the Ban.
	 *
	 * <P>This method may involve a remote method call.
	 */
	boolean isPTNBasedFleet() throws  TelusAPIException;

	/**
	 * Returns <CODE>true</CODE> if the specified talkGroup is already assigned
	 * to this subscriber, otherwise <CODE>false</CODE>.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @exception UnknownSubscriberException if this subscriber doesn't already exist.
	 */
	boolean contains(TalkGroup talkGroup) throws UnknownSubscriberException, TelusAPIException;

	/**
	 * Adds a ufmi resource(for ptn-based and class-based fleets). <P>
	 *
	 * When adding a ufmi to a subscriber, the subscribers contract must be dispatch-enabled, i.e.
	 * must contain dispatch service(s) and/or feature(s).
	 * That's why the new or modified contract needs to be passed in as well.
	 *
	 * <P>This method may involve a remote method call.
	 * @param newUrbanId - part of fleet identity
	 * @param newFleetId - part of fleet identity
	 * @param memberId - member id; if member id pattern is null or empty, a randomly selected number will be assigned.
	 * @param dealerCode - dealer code
	 * @param salesRepCode
	 * @param contract - new contract (i.e. price plan change) OR existing contract with dispatch services/features enabled
	 *
	 * @exception InvalidFleetException - New fleet is not associated to account.
	 * @exception MemberIdMatchException - No available member id found.
	 * @exception DispatchResourceContractMismatchException - Resource does not match contract services.
	 * @exception TelusAPIException
	 *
	 */
	void addMemberIdentity(int newUrbanId, int newFleetId, String memberId, String dealerCode, String salesRepCode, Contract contract) throws MemberIdMatchException, DispatchResourceContractMismatchException, InvalidFleetException, TelusAPIException;

	/**
	 * Removes a ufmi resource(for ptn-based and class-based fleets). <P>
	 *
	 * When removing a ufmi from a subscriber, the subscribers contract cannot be
	 * dispatch-enabled, i.e. cannot contain dispatch service(s) and/or feature(s).
	 * That's why the new or modified contract needs to be passed in as well.
	 *
	 * <P>This method may involve a remote method call.
	 * @param dealerCode - dealer code
	 * @param salesRepCode
	 * @param contract - new contract (i.e. price plan change) OR existing contract with dispatch services/features disabled
	 *
	 * @exception DispatchResourceContractMismatchException - Resource does not match contract services.
	 * @exception TelusAPIException
	 *
	 */
	void removeMemberIdentity(String dealerCode, String salesRepCode, Contract contract) throws DispatchResourceContractMismatchException, TelusAPIException;

	/**
	 * Changes member id portion of UFMI to a new member (only for class-based fleets).
	 * If member id pattern is null or empty, a randomly selected number will be assigned.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @exception MemberIdMatchException - No available member id found.
	 * @exception InvalidChangeMemberIdException - Member id cannot be changed.
	 *
	 */
	void changeMemberId(String newMemberId) throws MemberIdMatchException, InvalidChangeMemberIdException, TelusAPIException;

	/**
	 * Changes a ufmi to new fleet/member Id (for ptn-based and class-based fleets).
	 * If member id pattern is null or empty, a randomly selected number will be assigned.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @exception InvalidFleetException - New fleet is not associated to account.
	 * @exception MemberIdMatchException - No available member id found.
	 * @exception InvalidChangeMemberIdException - Member id cannot be changed.
	 *
	 */
	void changeMemberIdentity(int newUrbanId, int newFleetId, String newMemberId) throws MemberIdMatchException, InvalidChangeMemberIdException, InvalidFleetException, TelusAPIException;

	/**
	 * Returns a list of available member ids given the pattern for the subscribers
	 * current fleet.<P>
	 * If member id pattern is null or empty, a randomly selected list of available member ids
	 * will be returned.
	 *
	 * The array is never null, and never contains null elements,
	 * but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 *
	 * @param memberIdPattern - Pattern consisting of numbers and '*', maximum length is 5 characters.
	 * @param maxMemberIds - Maximum # of member ids to return, must be greater than zero.
	 *
	 */
	String[] retrieveAvailableMemberIds(String memberIdPattern, int maxMemberIds) throws TelusAPIException;

	/**
	 * Returns a list of available member ids for given fleet.<P>
	 * If member id pattern is null or empty, a randomly selected list of available member ids
	 * will be returned.
	 *
	 * The array is never null, and never contains null elements,
	 * but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 *
	 * @param urbanId - Urban Id of fleet for which available member ids shall be returned.
	 * @param fleetId - Fleet Id of fleet for which available member ids shall be returned.
	 * @param memberIdPattern - Pattern consisting of numbers and '*', maximum length is 5 characters.
	 * @param maxMemberIds - Maximum # of member ids to return, must be greater than zero.
	 *
	 */
	String[] retrieveAvailableMemberIds(int urbanId, int fleetId, String memberIdPattern, int maxMemberIds) throws TelusAPIException;

	/**
	 * Adds/changes the ip address.
	 * It assigns a randomly selected 'private' ip address (type 'Y')
	 *
	 * <P>This method may involve a remote method call.
	 *
	 */
	void changeIPAddress() throws TelusAPIException;

	/**
	 * Returns the IMSI (International Mobile Subscriber Identity) assigned to this subscriber or null.
	 */
	String getIMSI();

	/**
	 * Changes the IMSI (Internation Mobile Subscriber Identity) value.
	 * It assigns a randomly selected IMSI value.
	 *
	 * <P>This method may involve a remote method call.
	 */
	void changeIMSI() throws TelusAPIException;

	/**
	 * 
	 * @param dealerCode
	 * @param salesRepCode
	 * @param requestorId
	 * @throws TelusAPIException
	 * @throws InvalidEquipmentChangeException
	 */
	void changeEquipmentToVirtual(String dealerCode, String salesRepCode, String requestorId) throws TelusAPIException, InvalidEquipmentChangeException;

	/**
	 * Returns most recent Port Request for specific subscriber
	 */
	public PortRequest getPortRequest() throws PRMSystemException, TelusAPIException;
	public PortRequest newPortRequest(String phoneNumber, String NPDirectionIndicator, boolean prePopulate) throws TelusAPIException ;
	public String getPortType();
	public Date getPortDate();
	public PortRequestSummary getPortRequestSummary() throws PRMSystemException, TelusAPIException;
	
	/**
	 * Deprecated in favour of the new reservePhoneNumber method using the PortInEligibility object.
	 * 
	 * @deprecated
	 * 
	 * @param phoneNumberReservation
	 * @param portIn
	 * @throws PortRequestException
	 * @throws TelusAPIException
	 */
	public void reservePhoneNumber(PhoneNumberReservation phoneNumberReservation, boolean portIn) throws PortRequestException, TelusAPIException;

	public void reservePhoneNumber(PhoneNumberReservation phoneNumberReservation, PortInEligibility portInEligibility) throws PortRequestException, TelusAPIException;
	public void save(boolean activate, ActivationOption selectedOption, PortInEligibility portInEligibility) throws PortRequestException, TelusAPIException;
	public void changePhoneNumber(AvailablePhoneNumber availablePhoneNumber, String reasonCode, boolean changeOtherNumbers, String dealerCode, String salesRepCode, PortInEligibility portInEligibility) throws PhoneNumberException, PhoneNumberInUseException, PortRequestException, PRMSystemException, TelusAPIException;
	public void activate(String reason, Date startServiceDate, String memoText,boolean isPortIn, boolean modifyPortRequest) throws PortRequestException, PRMSystemException, TelusAPIException;
	public void activate(String reason, Date startServiceDate, String memoText,boolean isPortIn, boolean modifyPortRequest, ServiceRequestHeader header) throws PortRequestException, PRMSystemException, TelusAPIException;
	public void unreserve(boolean cancelPortIn) throws PRMSystemException, TelusAPIException;
	public void restore(Date activityDate, String reason, String memoText, String portOption, PortInEligibility portInEligibility) throws PortRequestException, PRMSystemException, TelusAPIException;
	public void reserveIDENResourcesForPortIn(PhoneNumberReservation phoneNumberReservation, boolean reserveUfmi, boolean ptnBased, ReservePortInMemberIdentity memberIdentity, String dealerCode) throws TelusAPIException;
	public ReservePortInMemberIdentity getReservePortInMemberIdentity() throws TelusAPIException;

	public MigrationRequest newMigrationRequest(Account newAccount, Equipment newEquipment, String pricePlanCode) throws InvalidMigrationRequestException, TelusAPIException;
	public MigrationRequest newMigrationRequest(Account newAccount, Equipment newEquipment,	Equipment newAssociatedHandset,	String pricePlanCode) throws InvalidMigrationRequestException, UnsupportedEquipmentException, TelusAPIException;

	public void testMigrate( MigrationRequest migrationRequest, String dealerCode, String salesRepCode, String requestorId) throws InvalidMigrationRequestException, TelusAPIException;
	public Subscriber migrate( MigrationRequest migrationRequest, String dealerCode, String salesRepCode, String requestorId) throws InvalidMigrationRequestException, TelusAPIException;
	
	/**
	 * Checks Transfer Blocking indicator for Wireless to Wireless port requests.
	 * Returns true if in the port out is restricted at subscriber level.
	 * 
	 * @throws TelusAPIException
	 * @return boolean
	 */
	boolean isPortRestricted(String accountPortProtectionIndicator, String subscriberPortProtectionIndicator) throws TelusAPIException;

	/**
	 * Checks Transfer Blocking indicator for Wireless to Wireless port requests.
	 * Returns true if in the port out is restricted at subscriber level.
	 * 
	 * @throws TelusAPIException
	 * @return String
	 */
	String getPortProtectionIndicator() throws TelusAPIException;

	/**
	 * Applies the Transfer Blocking indicator for Wireless to Wireless port requests
	 * at subscriber level.
	 * 
	 * @throws TelusAPIException
	 */   
	void updatePortRestriction(boolean restrictPort) throws TelusAPIException;

	/**
	 * Change the subscriber's fax number to a system-assigned number.
	 * 
	 * @throws PhoneNumberException
	 * @throws PhoneNumberInUseException
	 * @throws TelusAPIException
	 */   
	void changeFaxNumber() throws PhoneNumberException, PhoneNumberInUseException, TelusAPIException;

	/**
	 * Change the subscriber's fax number to the supplied fax number.
	 * 
	 * @param  availableFaxNumber
	 * @throws PhoneNumberInUseException
	 * @throws TelusAPIException
	 */   
	void changeFaxNumber(AvailablePhoneNumber availableFaxNumber) throws PhoneNumberInUseException, TelusAPIException;

	/**
	 * This method developed for CR267 and should be in use by Corporate Activation 
	 * application only. Method created to skip SOC Grouping validation.
	 *
	 * @param activate whether to activate this subscriber or leave it in a
	 *                 reserved state.  This parameter is only used for a
	 *                 subscriber's first save.
	 * @param selectedOption  The activation option selected by client.
	 * @param srvValidation  - contains boolean variables that allow 
	 *                         to skip following validations: SOC Grouping, 
	 *                         Equipment Type and Province match.
	 */
	public void save(boolean activate, ActivationOption selectedOption, ServicesValidation srvValidation) throws TelusAPIException;

	/**
	 * This method developed for CR267 and should be in use by Corporate Activation 
	 * application only. Method created to skip SOC Grouping validation.
	 *
	 * @param startServiceDate the future date to activate this subscriber.  This
	 *                         parameter is only used for a subscriber's first save.
	 * @param selectedOption  The activation option selected by client.
	 * @param srvValidation  - contains boolean variables that allow 
	 *                         to skip following validations: SOC Grouping, 
	 *                         Equipment Type and Province match.
	 */  
	public void save(Date startServiceDate, ActivationOption selectedOption, ServicesValidation srvValidation) throws TelusAPIException;

	/**
	 * This method developed for CR267 and should be in use by Corporate Activation 
	 * application only. Method created to skip SOC Grouping validation. 
	 * Puts this subscriber into an active state/status immediately.
	 * @param reason -- The code indicating the reason for the activation.
	 * @param memoText -- Text to be added to memo
	 * @param srvValidation  - contains boolean variables that allow 
	 *                         to skip following validations: SOC Grouping, 
	 *                         Equipment Type and Province match.
	 */
	public void activate(ServicesValidation srvValidation, String reason, String memoText) throws TelusAPIException; 

	/**
	 * This method developed for CR267 and should be in use by Corporate Activation 
	 * application only. Method created to skip SOC Grouping validation.
	 * Puts the subscriber into an active state/status some time in the future.
	 * 
	 * @param reason -- The code indicating the reason for the activation.
	 * @param startServiceDate the futrure date to activate this subscriber.
	 * @param memoText -- Text to be added to memo
	 * @param srvValidation  - contains boolean variables that allow 
	 *                         to skip following validations: SOC Grouping, 
	 *                         Equipment Type and Province match.
	 */
	public void activate(String reason, Date startServiceDate, String memoText, ServicesValidation srvValidation) throws TelusAPIException;

	/**
	 * This method developed for CR267 and should be in use by Corporate Activation 
	 * application only. Method created to skip SOC Grouping validation.
	 *
	 * @param activate whether to activate this subscriber or leave it in a
	 *                 reserved state.  This parameter is only used for a
	 *                 subscriber's first save.
	 * @param selectedOption  The activation option selected by client.
	 * @param portInEligibility  If this parameter is not NULL, subscriber is ported in
	 * @param srvValidation  - contains boolean variables that allow 
	 *                         to skip following validations: SOC Grouping, 
	 *                         Equipment Type and Province match.
	 */                        
	public void save(boolean activate, ActivationOption selectedOption, PortInEligibility portInEligibility ,ServicesValidation srvValidation) throws PortRequestException, TelusAPIException; 

	/**
	 * This method developed for CR267 and should be in use by Corporate Activation 
	 * application only. Method created to skip SOC Grouping validation.
	 * Puts the Ported In subscriber into an active state/status some 
	 * time in the future or immediately.
	 * 
	 * @param reason -- The code indicating the reason for the activation.
	 * @param startServiceDate the futrure date to activate this subscriber.
	 * @param memoText -- Text to be added to memo
	 * @param isPortIn -- Ported In indicator (could be true or false)
	 * @param srvValidation  - contains boolean variables that allow 
	 *                         to skip following validations: SOC Grouping, 
	 *                         Equipment Type and Province match.            
	 */
	public void activate(String reason, Date startServiceDate, String memoText, boolean isPortIn, boolean modifyPortRequest, ServicesValidation srvValidation)throws PortRequestException, PRMSystemException, TelusAPIException;

	/**
	 * Assigns a new primary mobile phone number to this subscriber and update SRPDS if header is not null.
	 * @param availablePhoneNumber
	 * @param reasonCode
	 * @param changeOtherNumbers
	 * @param dealerCode
	 * @param salesRepCode
	 * @param portInEligibility --If this parameter is not NULL, subscriber is ported in
	 * @param header -- <code>ServiceRequestHeader</code> if not null, will be write to SRPDS
	 * @throws PhoneNumberException
	 * @throws PhoneNumberInUseException
	 * @throws PortRequestException
	 * @throws PRMSystemException
	 * @throws TelusAPIException
	 */
	public void changePhoneNumber(AvailablePhoneNumber availablePhoneNumber, String reasonCode, 
			boolean changeOtherNumbers, String dealerCode, String salesRepCode, 
			PortInEligibility portInEligibility, ServiceRequestHeader header) 
	throws PhoneNumberException, PhoneNumberInUseException, PortRequestException, PRMSystemException, TelusAPIException;

	/**
	 * Restores the subscriber from either a suspended or cancelled state and update SRPDS if header is not null. 
	 * Note, the account must be open before the subscriber can be restored.
	 * <P>This method may involve a remote method call.
	 * 
	 * @param activityDate
	 * @param reason
	 * @param memoText
	 * @param portOption
	 * @param portInEligibility --If this parameter is not NULL, subscriber is ported in
	 * @param header <code>ServiceRequestHeader</code> if not null, will be write to SRPDS
	 * @throws PortRequestException
	 * @throws PRMSystemException
	 * @throws TelusAPIException
	 */
	public void restore(Date activityDate, String reason, String memoText, 
			String portOption, PortInEligibility portInEligibility, ServiceRequestHeader header) 
	throws PortRequestException, PRMSystemException, TelusAPIException;
}