/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */


package com.telus.api.account;

import com.telus.api.*;

public interface PCSAccount
{
	/**
	 * 
	 * Creates a new PCS Subscriber for HSPA equipment only
	 * 
	 * @param serialNumber
	 * @param associatedHandsetIMEI
	 * @param dealerHasDeposit
	 * @param activationFeeChargeCode
	 * @param voidMailLanguage
	 * @return PCSSubscriber
	 * @throws UnknownSerialNumberException
	 * @throws SerialNumberInUseException
	 * @throws InvalidSerialNumberException
	 * @throws UnsupportedEquipmentException
	 * @throws TelusAPIException
	 */
	PCSSubscriber newPCSSubscriber(String serialNumber,	String associatedHandsetIMEI, boolean dealerHasDeposit, String activationFeeChargeCode,	String voidMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, UnsupportedEquipmentException, TelusAPIException;

	/**
	 * 
	 * Creates a new unsaved PCS Subscriber from a reserved subscriber (for HSPA equipment only)
	 * NOTE: as the phone number parameter of this method represents a reserved subscriber, the returned subscriber
	 * object will have it's waiveSearchFee member set to true.
	 * 
	 * @param phoneNumber
	 * @param serialNumber
	 * @param associatedHandsetIMEI
	 * @param dealerHasDeposit
	 * @param activationFeeChargeCode
	 * @param voiceMailLanguage
	 * @return PCSSubscriber
	 * @throws UnknownSerialNumberException
	 * @throws SerialNumberInUseException
	 * @throws InvalidSerialNumberException
	 * @throws UnsupportedEquipmentException
	 * @throws TelusAPIException
	 */
	PCSSubscriber newPCSSubscriber(String phoneNumber, String serialNumber,	String associatedHandsetIMEI, boolean dealerHasDeposit, String activationFeeChargeCode,	String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, UnsupportedEquipmentException, TelusAPIException;

	/**
	 * Creates a new unsaved PCS/Cellular subscriber.  No activation fee will be
	 * charged when the new subscriber is activated.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param serialNumber the serialNumber/ESN to be validated and attached to
	 *        the new subscriber.
	 * @param dealerHasDeposit If a deposit was assessed, this indicates that
	 *        it was paid to the dealer and should be deducted from the his
	 *        commission.
	 *
	 * @exception UnknownSerialNumberException the number does not exist in our datastore.
	 * @exception SerialNumberInUseException the number is already being used by a subscriber.
	 * @exception InvalidSerialNumberException the number is inappropriate for this
	 *            operation.
	 *
	 */
	PCSSubscriber newPCSSubscriber(String serialNumber, boolean dealerHasDeposit, String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException;

	/**
	 * Creates new unsaved PCSSubscriber from a reserved subscriber (for Serial Number/ESN or SIM equipment).
	 * NOTE: as the phone number parameter of this method represents a reserved subscriber, the returned subscriber
	 * object will have it's waiveSearchFee member set to true. 
	 * 
	 * @param phoneNumber
	 * @param serialNumber the serialNumber/ESN or SIM to be validated and attached to
	 *        the new subscriber.
	 * @param dealerHasDeposit If a deposit was assessed, this indicates that
	 *        it was paid to the dealer and should be deducted from the his
	 *        commission.
	 * @param voiceMailLanguage
	 * @exception UnknownSerialNumberException the number does not exist in our data store.
	 * @exception SerialNumberInUseException the number is already being used by a subscriber.
	 * @exception InvalidSerialNumberException the number is inappropriate for this
	 *            operation.
	 * @throws TelusAPIException
	 */
	PCSSubscriber newPCSSubscriber(String phoneNumber, String serialNumber, boolean dealerHasDeposit, String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException;
	
	/**
	 * Creates a new unsaved PCS/Cellular subscriber.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param serialNumber the serialNumber/ESN to be validated and attached to
	 *        the new subscriber.
	 * @param dealerHasDeposit If a deposit was assessed, this indicates that
	 *        it was paid to the dealer and should be deducted from the his
	 *        commission.
	 * @param activationFeeChargeCode the chargeCode to use when activating the
	 *        newely created subscriber or <CODE>null</CODE> if the fee is waived.
	 *
	 * @exception UnknownSerialNumberException the number does not exist in our datastore.
	 * @exception SerialNumberInUseException the number is already being used by a subscriber.
	 * @exception InvalidSerialNumberException the number is inappropriate for this
	 *            operation.
	 *
	 */
	PCSSubscriber newPCSSubscriber(String serialNumber, boolean dealerHasDeposit, String activationFeeChargeCode, String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException;


	/**
	 * Returns the count of subscribers on shareable priceplans.
	 *  
	 * @deprecated
	 * @see #getShareablePricePlanSubscriberCount
	 */  
	PricePlanSubscriberCount[] getPricePlanSubscriberCount() throws TelusAPIException;

	/**
	 * Returns the count of subscribers on shareable priceplans for the given price plan code.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @deprecated
	 * @see #getShareablePricePlanSubscriberCount
	 */
	PricePlanSubscriberCount getPricePlanSubscriberCount(String pricePlanCode) throws TelusAPIException;

	/**
	 * Returns the count of subscribers on shareable priceplans or a zero-length array.
	 *
	 * <P>This method may involve a remote method call.
	 * 
	 * 
	 * @return PricePlanSubscriberCount[]
	 * @throws TelusAPIException
	 */
	PricePlanSubscriberCount[] getShareablePricePlanSubscriberCount() throws TelusAPIException;
  
	/**
	 * Returns the count of subscribers on shareable priceplans or a zero-length array.
	 *
	 * <P>This method may involve a remote method call.
	 * 
	 * @param refresh from the database
	 * @return PricePlanSubscriberCount[]
	 * @throws TelusAPIException
	 */
	PricePlanSubscriberCount[] getShareablePricePlanSubscriberCount(boolean refresh) throws TelusAPIException;
  
	/**
	 * Returns the count of subscribers on shareable priceplans for the given price plan code or null.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param pricePlanCode
	 * @return PricePlanSubscriberCount
	 * @throws TelusAPIException
	 */
	PricePlanSubscriberCount getShareablePricePlanSubscriberCount(String pricePlanCode)
	throws TelusAPIException;

	/**
	 * Creates new PCSSubscriber with a list of pieces of secondary equipment.
	 * @param serialNumber
	 * @param secondarySerialNumbers
	 * @param dealerHasDeposit
	 * @param activationFeeChargeCode
	 * @return PCSSubscriber
	 * @throws UnknownSerialNumberException
	 * @throws SerialNumberInUseException
	 * @throws InvalidSerialNumberException
	 * @throws TelusAPIException
	 */
	PCSSubscriber newPCSSubscriber(String serialNumber, String[] secondarySerialNumbers, boolean dealerHasDeposit, String activationFeeChargeCode, String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException;

	/**
	 * Creates a new unsaved PCSSubscriber from a reserved subscriber (using a list of secondary equipment)
	 * NOTE: as the phone number parameter of this method represents a reserved subscriber, the returned subscriber
	 * object will have it's waiveSearchFee member set to true.
	 * 
	 * @param phoneNumber
	 * @param serialNumber
	 * @param secondarySerialNumbers
	 * @param dealerHasDeposit
	 * @param activationFeeChargeCode
	 * @return PCSSubscriber
	 * @throws UnknownSerialNumberException
	 * @throws SerialNumberInUseException
	 * @throws InvalidSerialNumberException
	 * @throws TelusAPIException
	 */
	PCSSubscriber newPCSSubscriber(String phoneNumber, String serialNumber, String[] secondarySerialNumbers, boolean dealerHasDeposit, String activationFeeChargeCode, String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException;

	boolean hasHSPASubscriberInBAN() throws TelusAPIException;
	
	/**
	 * 
	 * Creates a new PCS BusinessConnect Subscriber for VOIP/HSPA subscriber
	 *  NOTE: phone number parameter is optional  if consumer does not have reserved phone number.
	 *	it will be passed only if phone number is already reserved on the ban ,CAPI will validate the number and attach the required NumberGroupInfo to subscriber Object and set subscriber status as reserved.
	 * 	and also set waiveSearchFee member set to true. 
	 * 
	 * @param seatType
	 * @param phoneNumber
	 * @param serialNumber
	 * @param dealerHasDeposit
	 * @param activationFeeChargeCode
	 * @param voidMailLanguage
	 * @param associatedHandsetIMEI
	 * @return PCSSubscriber
	 * @throws UnknownSerialNumberException
	 * @throws SerialNumberInUseException
	 * @throws InvalidSerialNumberException
	 * @throws TelusAPIException
	 */
	public PCSSubscriber newPCSBCSubscriber(String seatType,String phoneNumber,String serialNumber, boolean dealerHasDeposit, String activationFeeChargeCode,String voiceMailLanguage,String associatedHandsetIMEI) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException ;
}


