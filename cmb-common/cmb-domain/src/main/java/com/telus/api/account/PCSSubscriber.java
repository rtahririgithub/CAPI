/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import java.util.Date;

import com.telus.api.InvalidMigrationRequestException;
import com.telus.api.TelusAPIException;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.IDENEquipment;
import com.telus.api.equipment.MuleEquipment;
import com.telus.api.message.ApplicationMessage;
import com.telus.api.portability.PRMSystemException;
import com.telus.api.portability.PortInEligibility;
import com.telus.api.portability.PortRequest;
import com.telus.api.portability.PortRequestException;
import com.telus.api.portability.PortRequestSummary;
import com.telus.api.servicerequest.ServiceRequestHeader;

/**
 * @author x119998
 *
 */
public interface PCSSubscriber extends Subscriber {

	String getNextPhoneNumber();

	Date getNextPhoneNumberChangeDate();

	String getPreviousPhoneNumber();

	Date getPreviousPhoneNumberChangeDate();

	@Deprecated
	boolean isFidoConversion();

	/**
	 * Returns subscriber MIN.
	 * @return String
	 */
	String getMIN();

	Equipment[] getSecondaryEquipments() throws TelusAPIException;

	/**
	 * Assigns a new primary mobile phone number to this subscriber.
	 * This subscriber must already have a phone number.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 *
	 * @param availablePhoneNumber the new phone number to assign.
	 * @param reasonCode phone number change reason code
	 * @param changeOtherNumbers indicate that other subscriber numbers (like fax)
	 *        should be changed, if they exist.
	 *
	 * @exception PhoneNumberException if the availablePhoneNumber is inappropriate for this operation.
	 * @exception PhoneNumberInUseException if the number is already being used by a subscriber.
	 *
	 * @see #findAvailablePhoneNumbers
	 */
	void changePhoneNumber(AvailablePhoneNumber availablePhoneNumber, String reasonCode, boolean changeOtherNumbers) throws TelusAPIException, PhoneNumberException, PhoneNumberInUseException;

	/**
	 * Implements primary equipment change for this subscriber and updates the set of pieces of secondary
	 * equipment: if <tt>newSecondaryEquipments</tt> is a non-empty array it will be saved as a current
	 * set of pieces of secondary equipment replacing the existing one; otherwise, <i>i.e.</i>, if
	 * <tt>newSecondaryEquipments</tt> is <tt>null</tt> or an empty array, all pieces of secondary equipment
	 * will be removed for this subscriber. Thus if you don't intend to change the secondary equipment use
	 * the appropriate method of the <tt>Subscriber</tt> interface.
	 *
	 * @param newEquipment Piece of equipment to be saved as a primary one for this subscriber.
	 * @param newSecondaryEquipments New pieces of secondary equipment for this subscriber. If <tt>null</tt>
	 * or empy array all current pieces of secondary equipment belonging to this subscriber will be removed.
	 * @param dealerCode Dealer code.
	 * @param salesRepCode Sales representative code.
	 * @param requestorId User ID.
	 * @param repairId Repair ID.
	 * @param swapType One of the valid values of Swap Type (see constants in <tt>Equipment</tt>) if the piece of
	 * new equipment is different from the old one; otherwise may be <tt>null</tt> or an empy string.
	 * @param preserveDigitalServices If <tt>true</tt> all digital services will be preserved even if the
	 * new piece of equipment doesn't support them; otherwise all such services will be removed.
	 * @param ignoreSerialNoInUse If <tt>true</tt> operation will proceed even if <tt>newEquipment</tt> is
	 * already assigned to another subscriber corresponding to the same BAN; otherwise <tt>SerialNumberInUseException</tt>
	 * will be thrown.
	 * @throws TelusAPIException If a remote call to the database or EJB fails.
	 * @throws SerialNumberInUseException If <tt>newEquipment</tt> is already assigned to another subscriber corresponding
	 * to the different BAN or even to the same BAN if <tt>ignoreSerialNoInUse</tt> is <tt>false</tt>.
	 * @throws InvalidEquipmentChangeException If requested equipment swap violates existing business rules.
	 * @see Subscriber
	 * @see Equipment
	 * @deprecated
	 */
	ApplicationMessage[] changeEquipment(Equipment newEquipment, Equipment[] newSecondaryEquipments, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, boolean preserveDigitalServices, boolean ignoreSerialNoInUse) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException ;

	/**
	 * 
	 * Changes HSPA equipment only; will throw UnsupportedEquipmentException otherwise. 
	 * 
	 * @param newEquipment
	 * @param associatedHandset
	 * @param dealerCode
	 * @param salesRepCode
	 * @param requestorId
	 * @param repairId
	 * @param swapType
	 * @param preserveDigitalServices
	 * @param allowDuplicateSerialNo
	 * @param header
	 * @return ApplicationMessage[]
	 * @throws TelusAPIException
	 * @throws SerialNumberInUseException
	 * @throws InvalidEquipmentChangeException
	 * @throws UnsupportedEquipmentException
	 */
	ApplicationMessage[] changeEquipment(Equipment newEquipment, Equipment associatedHandset, String dealerCode,
			String salesRepCode, String requestorId, String repairId, String swapType, boolean preserveDigitalServices,
			char allowDuplicateSerialNo, ServiceRequestHeader header) throws TelusAPIException, SerialNumberInUseException,
			InvalidEquipmentChangeException, UnsupportedEquipmentException;

	/**
	 * 
	 * For HSPA subscriber only to create P2P migration request
	 * 
	 * @param newEquipment
	 * @param newAssociatedHandset
	 * @param dealerCode
	 * @param salesRepCode
	 * @param requestorId
	 * @param repairId
	 * @param swapType
	 * @param allowDuplicateSerialNo
	 * @return ApplicationMessage[]
	 * @throws TelusAPIException
	 * @throws SerialNumberInUseException
	 * @throws InvalidEquipmentChangeException
	 * @throws UnsupportedEquipmentException
	 */
	ApplicationMessage[] testChangeEquipment(Equipment newEquipment, Equipment newAssociatedHandset, String dealerCode,
			String salesRepCode, String requestorId, String repairId, String swapType, char allowDuplicateSerialNo)
			throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException, UnsupportedEquipmentException;

	/**
	 * 
	 * For HSPA subscribers only to create P2P migration request
	 * 
	 * @param account
	 * @param newEquipment
	 * @param newAssociatedHandset
	 * @param pricePlanCode
	 * @return MigrationRequest
	 * @throws InvalidEquipmentChangeException
	 * @throws UnsupportedEquipmentException
	 * @throws TelusAPIException
	 */
	MigrationRequest newMigrationRequest(Account account, Equipment newEquipment, Equipment newAssociatedHandset,
			String pricePlanCode) throws InvalidEquipmentChangeException, UnsupportedEquipmentException, TelusAPIException;

	/**
	 * 
	 * For HSPA subscribers only
	 * 
	 * @param newEquipment
	 * @param newAssociatedHandset
	 * @param dealerCode
	 * @param salesRepCode
	 * @param requestorId
	 * @param repairId
	 * @param swapType
	 * @param preserveDigitalServices
	 * @return EquipmentChangeRequest
	 * @throws TelusAPIException
	 * @throws SerialNumberInUseException
	 * @throws InvalidEquipmentChangeException
	 * @throws UnsupportedEquipmentException
	 */
	EquipmentChangeRequest newEquipmentChangeRequest(Equipment newEquipment, Equipment newAssociatedHandset, String dealerCode,
			String salesRepCode, String requestorId, String repairId, String swapType, boolean preserveDigitalServices)
			throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException, UnsupportedEquipmentException;

	/**
	 * Implements primary equipment change for this subscriber and updates the set of pieces of secondary
	 * equipment: if <tt>newSecondaryEquipments</tt> is a non-empty array it will be saved as a current
	 * set of pieces of secondary equipment replacing the existing one; otherwise, <i>i.e.</i>, if
	 * <tt>newSecondaryEquipments</tt> is <tt>null</tt> or an empty array, all pieces of secondary equipment
	 * will be removed for this subscriber. Thus if you don't intend to change the secondary equipment use
	 * the appropriate method of the <tt>Subscriber</tt> interface.
	 *
	 * @param newEquipment Piece of equipment to be saved as a primary one for this subscriber.
	 * @param newSecondaryEquipments New pieces of secondary equipment for this subscriber. If <tt>null</tt>
	 * or empy array all current pieces of secondary equipment belonging to this subscriber will be removed.
	 * @param dealerCode Dealer code.
	 * @param salesRepCode Sales representative code.
	 * @param requestorId User ID.
	 * @param repairId Repair ID.
	 * @param swapType One of the valid values of Swap Type (see constants in <tt>Equipment</tt>) if the piece of
	 * new equipment is different from the old one; otherwise may be <tt>null</tt> or an empy string.
	 * @param preserveDigitalServices If <tt>true</tt> all digital services will be preserved even if the
	 * new piece of equipment doesn't support them; otherwise all such services will be removed.
	 * @param allowDuplicateSerialNo one of value of SWAP allow duplicate serial no flag( see contants in <tt>Subscriber</tt>)
	 *      When <tt>SWAP_DUPLICATESERIALNO_ALLOWOTHERBAN</tt> is set, the operation will bypass checking duplicate serial no.
	 *      When <tt>SWAP_DUPLICATESERIALNO_ALLOWSAMEBAN</tt> is set, <tt>SerialNumberInUseException</tt> will be thrown
	 *        if <tt>newEquipment</tt> is already assigned to another subscriber in any other BAN
	 *      When <tt>SWAP_DUPLICATESERIALNO_DONOTALLOW</tt> is set, <tt>SerialNumberInUseException</tt> will be thrown
	 *        if <tt>newEquipment</tt> is already assigned to another subscriber in any BAN
	 * @throws TelusAPIException If a remote call to the database or EJB fails.
	 * @throws SerialNumberInUseException If <tt>newEquipment</tt> is already assigned to another subscriber corresponding
	 * to the different BAN or even to the same BAN if <tt>ignoreSerialNoInUse</tt> is <tt>false</tt>.
	 * @throws InvalidEquipmentChangeException If requested equipment swap violates existing business rules.
	 * @see Subscriber
	 * @see Equipment
	 */
	ApplicationMessage[] changeEquipment(Equipment newEquipment, Equipment[] newSecondaryEquipments, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, boolean preserveDigitalServices, char allowDuplicateSerialNo) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException ;

	/**
	 * Implements  change to Virtual equipment for this subscriber and updates the set of pieces of secondary
	 * equipment: if <tt>newSecondaryEquipments</tt> is a non-empty array it will be saved as a current
	 * set of pieces of secondary equipment replacing the existing one; otherwise, <i>i.e.</i>, if
	 * <tt>newSecondaryEquipments</tt> is <tt>null</tt> or an empty array, all pieces of secondary equipment
	 * will be removed for this subscriber. Thus if you don't intend to change the secondary equipment use
	 * the appropriate method of the <tt>Subscriber</tt> interface.
	 *
	 * @param newSecondaryEquipments New pieces of secondary equipment for this subscriber. If <tt>null</tt>
	 * or empy array all current pieces of secondary equipment belonging to this subscriber will be removed.
	 * @param dealerCode Dealer code.
	 * @param salesRepCode Sales representative code.
	 * @param requestorId User ID.
	 * 
	 * @throws TelusAPIException If a remote call to the database or EJB fails.
	 * @throws SerialNumberInUseException If <tt>newEquipment</tt> is already assigned to another subscriber corresponding
	 * to the different BAN or even to the same BAN if <tt>ignoreSerialNoInUse</tt> is <tt>false</tt>.
	 * @throws InvalidEquipmentChangeException If requested equipment swap violates existing business rules.
	 * @see Subscriber
	 * @see Equipment
	 */
	void changeEquipmentToVirtual(Equipment[] newSecondaryEquipments, String dealerCode, String salesRepCode, String requestorId) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException ;

	/**
	 * Returns an instance of <tt>EquipmentChangeRequest</tt> that includes not only the new primary equipment
	 * but also new set of pieces of secondary equipment. If <tt>newSecondaryEquipments</tt> is a non-empty array
	 * it will be saved as a current set of pieces of secondary equipment replacing the existing one; otherwise,
	 * <i>i.e.</i>, if <tt>newSecondaryEquipments</tt> is <tt>null</tt> or an empty array, all pieces of secondary
	 * equipment will be removed for this subscriber. Thus if you don't intend to change the secondary equipment use
	 * the appropriate method of the <tt>Subscriber</tt> interface.
	 *
	 * @param newEquipment Piece of equipment to be saved as a primary one for this subscriber.
	 * @param newSecondaryEquipments New pieces of secondary equipment for this subscriber. If <tt>null</tt>
	 * or empy array all current pieces of secondary equipment belonging to this subscriber will be removed.
	 * @param dealerCode Dealer code.
	 * @param salesRepCode Sales representative code.
	 * @param requestorId User ID.
	 * @param repairId Repair ID.
	 * @param swapType One of the valid values of Swap Type (see constants in <tt>Equipment</tt>) if the piece of
	 * new equipment is different from the old one; otherwise may be <tt>null</tt> or an empy string.
	 * @param preserveDigitalServices If <tt>true</tt> all digital services will be preserved even if the
	 * new piece of equipment doesn't support them; otherwise all such services will be removed.
	 * @return new equipment change request.
	 * @throws TelusAPIException If a remote call to the database or EJB fails.
	 * @throws SerialNumberInUseException If <tt>newEquipment</tt> is already assigned to another subscriber.
	 * @throws InvalidEquipmentChangeException If requested equipment swap violates existing business rules.
	 */
	EquipmentChangeRequest newEquipmentChangeRequest(Equipment newEquipment, Equipment[] newSecondaryEquipments, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, boolean preserveDigitalServices) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException;

	/**
	 * Returns an instance of <tt>EquipmentChangeRequest</tt> that includes not only the new primary equipment
	 * but also new set of pieces of secondary equipment. If <tt>newSecondaryEquipments</tt> is a non-empty array
	 * it will be saved as a current set of pieces of secondary equipment replacing the existing one; otherwise,
	 * <i>i.e.</i>, if <tt>newSecondaryEquipments</tt> is <tt>null</tt> or an empty array, all pieces of secondary
	 * equipment will be removed for this subscriber. Thus if you don't intend to change the secondary equipment use
	 * the appropriate method of the <tt>Subscriber</tt> interface.
	 *
	 * @param newEquipment Piece of equipment to be saved as a primary one for this subscriber.
	 * @param newSecondaryEquipments New pieces of secondary equipment for this subscriber. If <tt>null</tt>
	 * or empy array all current pieces of secondary equipment belonging to this subscriber will be removed.
	 * @param dealerCode Dealer code.
	 * @param salesRepCode Sales representative code.
	 * @param requestorId User ID.
	 * @param repairId Repair ID.
	 * @param swapType One of the valid values of Swap Type (see constants in <tt>Equipment</tt>) if the piece of
	 * new equipment is different from the old one; otherwise may be <tt>null</tt> or an empy string.
	 * @param preserveDigitalServices If <tt>true</tt> all digital services will be preserved even if the
	 * new piece of equipment doesn't support them; otherwise all such services will be removed.
	 * @param allowDuplicateSerialNo one of the SWAP_DUPLICATESERIALNO_xxxx constants.
	 * 
	 * @return new equipment change request.
	 * @throws TelusAPIException If a remote call to the database or EJB fails.
	 * @throws SerialNumberInUseException If <tt>newEquipment</tt> is already assigned to another subscriber.
	 * @throws InvalidEquipmentChangeException If requested equipment swap violates existing business rules.
	 */
	EquipmentChangeRequest newEquipmentChangeRequest(Equipment newEquipment, Equipment[] newSecondaryEquipments, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, boolean preserveDigitalServices, char allowDuplicateSerialNo) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException;

	/**
	 * Implements validations for the proposed swap. First, validates the primary equipment swap using appropriate
	 * method in <tt>Subscriber</tt>, then validates swaps for all pieces of the secondary equipment.
	 *
	 * @param newEquipment Piece of equipment to be saved as a primary one for this subscriber.
	 * @param newSecondaryEquipments New pieces of secondary equipment for this subscriber. If <tt>null</tt> only the
	 * validation for the primary equipment will be implemented and the method is identical to the appropriate method
	 * in <tt>Subscriber</tt>.
	 * @param dealerCode Dealer code.
	 * @param salesRepCode Sales representative code.
	 * @param requestorId User ID.
	 * @param repairId Repair ID.
	 * @param swapType One of the valid values of Swap Type (see constants in <tt>Equipment</tt>) if the piece of
	 * new equipment is different from the old one; otherwise may be <tt>null</tt> or an empy string.
	 * @param ignoreSerialNoInUse If <tt>true</tt> operation will be valid even if <tt>newEquipment</tt> is
	 * already assigned to another subscriber corresponding to the same BAN; otherwise <tt>SerialNumberInUseException</tt>
	 * will be thrown.
	 * @throws TelusAPIException If a remote call to the database or EJB fails.
	 * @throws SerialNumberInUseException If <tt>newEquipment</tt> or any piece of the secondary equipment is already
	 * assigned to another subscriber corresponding to the different BAN or even the same BAN is <tt>ignoreSerialNoInUse</tt>
	 * is <tt>false</tt>.
	 * @throws InvalidEquipmentChangeException If proposed equipment swap violates existing business rules.
	 * @deprecated
	 */
	ApplicationMessage[] testChangeEquipment(Equipment newEquipment, Equipment[] newSecondaryEquipments, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, boolean ignoreSerialNoInUse) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException;

	/**
	 * Implements validations for the proposed swap. First, validates the primary equipment swap using appropriate
	 * method in <tt>Subscriber</tt>, then validates swaps for all pieces of the secondary equipment.
	 *
	 * @param newEquipment Piece of equipment to be saved as a primary one for this subscriber.
	 * @param newSecondaryEquipments New pieces of secondary equipment for this subscriber. If <tt>null</tt> only the
	 * validation for the primary equipment will be implemented and the method is identical to the appropriate method
	 * in <tt>Subscriber</tt>.
	 * @param dealerCode Dealer code.
	 * @param salesRepCode Sales representative code.
	 * @param requestorId User ID.
	 * @param repairId Repair ID.
	 * @param swapType One of the valid values of Swap Type (see constants in <tt>Equipment</tt>) if the piece of
	 * new equipment is different from the old one; otherwise may be <tt>null</tt> or an empy string.
	 * @param allowDuplidateSerialNo one of value of SWAP allow duplicate serial no flag( see contants in <tt>Subscriber</tt>)
	 *      When <tt>SWAP_DUPLICATESERIALNO_ALLOWOTHERBAN</tt> is set, the operation will bypass checking duplicate serial no.
	 *      When <tt>SWAP_DUPLICATESERIALNO_ALLOWSAMEBAN</tt> is set, <tt>SerialNumberInUseException</tt> will be thrown
	 *        if <tt>newEquipment</tt> is already assigned to another subscriber in any other BAN
	 *      When <tt>SWAP_DUPLICATESERIALNO_DONOTALLOW</tt> is set, <tt>SerialNumberInUseException</tt> will be thrown
	 *        if <tt>newEquipment</tt> is already assigned to another subscriber in any BAN
	 * @throws TelusAPIException If a remote call to the database or EJB fails.
	 * @throws SerialNumberInUseException If <tt>newEquipment</tt> or any piece of the secondary equipment is already
	 * assigned to another subscriber corresponding to the different BAN or even the same BAN is <tt>ignoreSerialNoInUse</tt>
	 * is <tt>false</tt>.
	 * @throws InvalidEquipmentChangeException If proposed equipment swap violates existing business rules.
	 */
	ApplicationMessage[] testChangeEquipment(Equipment newEquipment, Equipment[] newSecondaryEquipments, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType, char allowDuplidateSerialNo) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException;

	/**
	 * Create a new MigrationRequest object
	 *
	 * <P>This method may involve a remote method call.</P>
	 *
	 * @param account have to be created or already existing account
	 * @param newEquipment might be <CODE>null</CODE>. if new Account support the same handset.
	 * @param pricePlanCode  customer new price plan
	 * For Example PrePaid to Postpaid.
	 * For Example Postpaid to PrePaid.
	 */
	MigrationRequest newMigrationRequest(Account account, Equipment newEquipment, String pricePlanCode)throws InvalidEquipmentChangeException, TelusAPIException;

	/**
	 * Create a new MigrationRequest object
	 *
	 * <P>This method may involve a remote method call.</P>
	 *
	 * @param account have to be created or already existing account
	 * @param newEquipment new IDEN sim card.
	 * @param associatedMule associated mule equipment.
	 * @param pricePlanCode  customer new price plan
	 */
	MigrationRequest newMigrationRequest(Account account, IDENEquipment newEquipment, MuleEquipment associatedMule, String pricePlanCode)throws InvalidEquipmentChangeException, TelusAPIException;

	/**
	 * Returns new Subscriber,
	 * May involve a remote call.
	 * @param migrationRequest
	 * @param dealerCode
	 * @param salesRepCode
	 * @param requestorId
	 * @exception TelusAPIException
	 */
	public Subscriber migrate(MigrationRequest migrationRequest, String dealerCode, String salesRepCode, String requestorId) throws InvalidMigrationRequestException, TelusAPIException;

	/**
	 * test to Migration will work
	 * May involve a remote call.
	 * @param migrationRequest
	 * @param dealerCode
	 * @param salesRepCode
	 * @param requestorId
	 * @exception TelusAPIException
	 */
	public void testMigrate(MigrationRequest migrationRequest, String dealerCode, String salesRepCode, String requestorId) throws InvalidMigrationRequestException, TelusAPIException;
	
	/**
	 * Business Connect migrate seat test functionality. This method validates the request data.
	 * 
	 * @param MigrateSeatRequest request
	 * @param String dealerCode
	 * @param String salesRepCode
	 * @param String requestorId
	 * 
	 * @exception InvalidMigrationRequestException
	 * @exception TelusAPIException
	 */
	public void testMigrate(MigrateSeatRequest migrateSeatRequest, String dealerCode, String salesRepCode, String requestorId) throws InvalidMigrationRequestException, TelusAPIException;
	
	/**
	 * Business Connect create new migrate seat request for outbound migration from BC mobile seat to Postpaid - may involve a remote call.
	 * 
	 * @param Account account existing target account for migration
	 * @param String pricePlanCode new price plan valid for target account
	 * 
	 * @exception InvalidMigrationRequestException
	 * @exception TelusAPIException
	 */
	public MigrateSeatRequest newMigrationRequest(Account newAccount, String pricePlanCode) throws InvalidMigrationRequestException, TelusAPIException;
	
	/**
	 * Business Connect create new migrate seat request for inbound migration Postpaid to BC mobile seat - may involve a remote call.
	 * 
	 * @param Account account existing target account for migration
	 * @param String pricePlanCode new price plan valid for target account
	 * @param String targetSeatTypeCode target seat type (July 2014 - only mobile seat type supported)
	 * @param String targetSeatGroupId target seat group ID
	 * 
	 * @exception InvalidMigrationRequestException
	 * @exception TelusAPIException
	 */
	public MigrateSeatRequest newMigrationRequest(Account newAccount, String pricePlanCode, String targetSeatTypeCode, String targetSeatGroupId) throws InvalidMigrationRequestException, TelusAPIException;
	
	/**
	 * Business Connect migrate seat functionality - may involve a remote call.
	 * 
	 * @param MigrateSeatRequest request
	 * @param String dealerCode
	 * @param String salesRepCode
	 * @param String requestorId
	 * 
	 * @exception InvalidMigrationRequestException
	 * @exception TelusAPIException
	 */
	public Subscriber migrate(MigrateSeatRequest migrateSeatRequest, String dealerCode, String salesRepCode, String requestorId) throws InvalidMigrationRequestException, TelusAPIException;
	
	/**
	 * Returns most recent Port Request for specific subscriber
	 */
	public PortRequest getPortRequest() throws PRMSystemException, TelusAPIException;
	public PortRequest newPortRequest(String phoneNumber, String NPDirectionIndicator, boolean prePopulate) throws TelusAPIException;
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
	public void activate(String reason, Date startServiceDate, String memoText, boolean isPortIn, boolean modifyPortRequest) throws PortRequestException, PRMSystemException, TelusAPIException;
	public void activate(String reason, Date startServiceDate, String memoText, boolean isPortIn, boolean modifyPortRequest, ServiceRequestHeader header) throws PortRequestException, PRMSystemException, TelusAPIException;
	public void unreserve(boolean cancelPortIn) throws PRMSystemException,  TelusAPIException;
	public void restore(Date activityDate, String reason, String memoText, String portOption, PortInEligibility portInEligibility) throws PortRequestException, PRMSystemException, TelusAPIException;

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
	public void activate(ServicesValidation srvValidation,String reason, String memoText) throws TelusAPIException; 

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
	 *                         
	 */
	public void activate(String reason, Date startServiceDate, String memoText, boolean isPortIn, boolean modifyPortRequest, ServicesValidation srvValidation)throws PortRequestException, PRMSystemException, TelusAPIException;

	/**
	 * Implements primary equipment change for this subscriber and updates the set of pieces of secondary
	 * equipment: if <tt>newSecondaryEquipments</tt> is a non-empty array it will be saved as a current
	 * set of pieces of secondary equipment replacing the existing one; otherwise, <i>i.e.</i>, if
	 * <tt>newSecondaryEquipments</tt> is <tt>null</tt> or an empty array, all pieces of secondary equipment
	 * will be removed for this subscriber. Thus if you don't intend to change the secondary equipment use
	 * the appropriate method of the <tt>Subscriber</tt> interface.
	 *
	 * @param newEquipment Piece of equipment to be saved as a primary one for this subscriber.
	 * @param newSecondaryEquipments New pieces of secondary equipment for this subscriber. If <tt>null</tt>
	 * or empy array all current pieces of secondary equipment belonging to this subscriber will be removed.
	 * @param dealerCode Dealer code.
	 * @param salesRepCode Sales representative code.
	 * @param requestorId User ID.
	 * @param repairId Repair ID.
	 * @param swapType One of the valid values of Swap Type (see constants in <tt>Equipment</tt>) if the piece of
	 * new equipment is different from the old one; otherwise may be <tt>null</tt> or an empy string.
	 * @param preserveDigitalServices If <tt>true</tt> all digital services will be preserved even if the
	 * new piece of equipment doesn't support them; otherwise all such services will be removed.
	 * @param allowDuplicateSerialNo one of value of SWAP allow duplicate serial no flag( see contants in <tt>Subscriber</tt>)
	 *      When <tt>SWAP_DUPLICATESERIALNO_ALLOWOTHERBAN</tt> is set, the operation will bypass checking duplicate serial no.
	 *      When <tt>SWAP_DUPLICATESERIALNO_ALLOWSAMEBAN</tt> is set, <tt>SerialNumberInUseException</tt> will be thrown
	 *        if <tt>newEquipment</tt> is already assigned to another subscriber in any other BAN
	 *      When <tt>SWAP_DUPLICATESERIALNO_DONOTALLOW</tt> is set, <tt>SerialNumberInUseException</tt> will be thrown
	 *        if <tt>newEquipment</tt> is already assigned to another subscriber in any BAN
	 * @param header -- <code>ServiceRequestHeader</code> if not null, will be write to SRPDS
	 * @throws TelusAPIException If a remote call to the database or EJB fails.
	 * @throws SerialNumberInUseException If <tt>newEquipment</tt> is already assigned to another subscriber corresponding
	 * to the different BAN or even to the same BAN if <tt>ignoreSerialNoInUse</tt> is <tt>false</tt>.
	 * @throws InvalidEquipmentChangeException If requested equipment swap violates existing business rules.
	 * @see Subscriber
	 * @see Equipment
	 */
	ApplicationMessage[] changeEquipment(Equipment newEquipment, Equipment[] newSecondaryEquipments, 
			String dealerCode, String salesRepCode, String requestorId, String repairId, 
			String swapType, boolean preserveDigitalServices, char allowDuplicateSerialNo, 
			ServiceRequestHeader header) 
	throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException ;
	
	/**
	 * Assigns a new primary mobile phone number to this subscriber.
	 * This subscriber must already have a phone number.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 *
	 * @param availablePhoneNumber the new phone number to assign.
	 * @param reasonCode phone number change reason code
	 * @param changeOtherNumbers indicate that other subscriber numbers (like fax)
	 *        should be changed, if they exist.
	 * @param dealerCode
	 * @param salesRepCode
	 * @param portInEligibility --If this parameter is not NULL, subscriber is ported in
	 * @param header -- <code>ServiceRequestHeader</code> if not null, will be write to SRPDS
	 *
	 * @exception PhoneNumberException if the availablePhoneNumber is inappropriate for this operation.
	 * @exception PhoneNumberInUseException if the number is already being used by a subscriber.
	 *
	 * @see #findAvailablePhoneNumbers
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
	 * @param header -- <code>ServiceRequestHeader</code> if not null, will be write to SRPDS
	 * @throws PortRequestException
	 * @throws PRMSystemException
	 * @throws TelusAPIException
	 */
	public void restore(Date activityDate, String reason, String memoText, 
			String portOption, PortInEligibility portInEligibility, ServiceRequestHeader header) 
	throws PortRequestException, PRMSystemException, TelusAPIException;
		
	/**
	   * Reserves the OnHoldPhoneNumber for this subscriber to support offline activation process .
	   *
	   * <P>This method may involve a remote method call.
	   *
	   * @param phoneNumberReservation the criteria for the new mobile number.
	   *
	   * @exception NumberMatchException when no number satisfied the phoneNumberReservation.
	   *
	   * @see #getPhoneNumber
	   */
	public void reserveOnHoldPhoneNumber(PhoneNumberReservation phoneNumberReservation) throws TelusAPIException;
	
}