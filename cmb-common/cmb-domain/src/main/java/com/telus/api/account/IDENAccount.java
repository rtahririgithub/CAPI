/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.*;
import com.telus.api.fleet.*;

@Deprecated
public interface IDENAccount
{

  /**
   * Creates a new unsaved IDEN/Mike subscriber.  No activation fee will be
   * charged when the new subscriber is activated.
   *
   * <P>This method may involve a remote method call.
   *
   * @param serialNumber the serialNumber/IMEI to be validated and attached to
   *        the new subscriber.
   * @param dealerHasDeposit If a deposit was assessed, this indicates that
   *        it was paid to the dealer and should be deducted from the his
   *        commission.
   *
   * @exception UnknownSerialNumberException the number does not exist in our datastore.
   * @exception SerialNumberInUseException the number is already being used by a subscriber.
   * @exception InvalidSerialNumberException the number is inappropriate for this
   *            operation.
   */
  IDENSubscriber newIDENSubscriber(String serialNumber, boolean dealerHasDeposit, String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException;

  /**
   * Creates a new unsaved IDEN/Mike subscriber.  No activation fee will be
   * charged when the new subscriber is activated.
   *
   * <P>This method may involve a remote method call.
   *
   * @param serialNumber the serialNumber/IMEI to be validated and attached to
   *        the new subscriber.
   * @param muleNumber the Mule(IMEI) number to be validated and attached to SIM handset
   *        of the new subscriber.
   * @param dealerHasDeposit If a deposit was assessed, this indicates that
   *        it was paid to the dealer and should be deducted from the his
   *        commission.
   *
   * @exception UnknownSerialNumberException the number does not exist in our datastore.
   * @exception SerialNumberInUseException the number is already being used by a subscriber.
   * @exception InvalidSerialNumberException the number is inappropriate for this
   *            operation.
   */
  IDENSubscriber newIDENSubscriber(String serialNumber, String muleNumber, boolean dealerHasDeposit, String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException;

  /**
   * Creates a new unsaved IDEN/Mike subscriber.
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
  IDENSubscriber newIDENSubscriber(String serialNumber, boolean dealerHasDeposit, String activationFeeChargeCode, String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException;

  /**
   * Creates a new unsaved IDEN/Mike subscriber.
   *
   * <P>This method may involve a remote method call.
   *
   * @param serialNumber the serialNumber/ESN to be validated and attached to
   *        the new subscriber.
   * @param muleNumber the Mule(IMEI) number to be validated and attached to SIM handset
   *        of the new subscriber.
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
  IDENSubscriber newIDENSubscriber(String serialNumber, String muleNumber, boolean dealerHasDeposit, String activationFeeChargeCode, String voiceMailLanguage) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException;

  /**
   * Creates a new Fleet with this account as the owner.  The fleet is automatically
   * associated with this account.
   *
   * <P>This method may involve a remote method call.
   *
   * @param networkId the network this fleet belong to.
   * @param name the textual alias of the new fleet.
   * @param numberOfSubscriber the expected number of subscriber initially on
   *        the new fleet.
   *
   * @exception InvalidNetworkException if the network doesn't exist or is
   *            inappropriate for this operation.
   * @exception UnknownBANException if this account doesn't already exist.
   * @exception DuplicateObjectException if a fleet with the specified name
   *            already exists in this network.
   */
  Fleet newFleet(int networkId, String name, int numberOfSubscriber) throws InvalidNetworkException, UnknownBANException, DuplicateObjectException, TelusAPIException;

  /**
   * Associates a set of shared or/and public fleets with this account.
   *
   * <P>This method can safely be called for fleets that have already been added.
   *
   * <P>This method may involve a remote method call.
   *
   * @param fleet the non-null array of fleets.
   *
   * @exception UnknownBANException if this account doesn't already exist.
   * @exception InvalidFleetException if any fleet inappropriate for the current
   *            operation (possibly trying to add a private, non-shared, fleet).
   */
  void addFleets(Fleet[] fleet) throws UnknownBANException, InvalidFleetException, TelusAPIException;

  /**
   * Associates a single shared or public fleet with this account.
   *
   * <P>This method can safely be called for a fleet that has already been added.
   *
   * <P>This method may involve a remote method call.
   *
   * @exception UnknownBANException if this account doesn't already exist.
   * @exception InvalidFleetException if fleet inappropriate for the current
   *            operation (possibly trying to add a private, non-shared, fleet).
   */
  void addFleet(Fleet fleet) throws UnknownBANException, InvalidFleetException, TelusAPIException;

  /**
   * Associates a set of talkGroups with this account.  This method will also
   * associate the talkGroups' fleets to the account if not already done.
   *
   * <P>This method can safely be called for talkgroups that have already been added.
   *
   * <P>This method may involve a remote method call.
   *
   * @param talkGroup the non-null array of talkGroups.
   *
   * @exception UnknownBANException if this account doesn't already exist.
   * @exception InvalidFleetException if any talkgroup's fleet is inappropriate
   *            for the current operation (possibly trying to add a private,
   *            non-shared, fleet).
   */
  void addTalkGroups(TalkGroup[] talkGroup) throws UnknownBANException, InvalidFleetException, TelusAPIException;

  /**
   * Associates a single talkGroup with this account.  This method will also
   * associate the talkGroup's fleet to the account if not already done.
   *
   * <P>This method can safely be called for a talkgroup that has already been added.
   *
   * <P>This method may involve a remote method call.
   *
   * @exception UnknownBANException if this account doesn't already exist.
   * @exception InvalidFleetException if the talkgroup's fleet is inappropriate
   *            for the current operation (possibly trying to add a private,
   *            non-shared, fleet).
   */
  void addTalkGroup(TalkGroup talkGroup) throws UnknownBANException, InvalidFleetException, TelusAPIException;

  /**
   * Returns the fleets already associated with this account.
   *
   * <P>This method may involve a remote method call.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * @exception UnknownBANException if this account doesn't already exist.
   *
   * @link aggregation
   */
  Fleet[] getFleets() throws UnknownBANException, TelusAPIException;

  /**
   * Returns the talkgroup(s) associated with this account.
   *
   * <P>This method may involve a remote method call.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * @link aggregation
   */
  TalkGroup[] getTalkGroups() throws UnknownBANException, TelusAPIException;

  /**
   * Returns the talkgroup(s) for a given fleet associated with this account.
   *
   * <P>This method may involve a remote method call.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * @link aggregation
   */
  TalkGroup[] getTalkGroups(int urbanId, int fleetId) throws UnknownBANException, TelusAPIException;

  /**
   * Returns <CODE>true</CODE> if the specified fleet is already associated
   * with this account, otherwise <CODE>false</CODE>.
   *
   * <P>This method may involve a remote method call.
   *
   * @exception UnknownBANException if this account doesn't already exist.
   */
  boolean contains(Fleet fleet) throws UnknownBANException, TelusAPIException;

  /**
   * Returns <CODE>true</CODE> if the specified talkGroup is already associated
   * with this account, otherwise <CODE>false</CODE>.
   *
   * <P>This method may involve a remote method call.
   *
   * @exception UnknownBANException if this account doesn't already exist.
   */
  boolean contains(TalkGroup talkGroup) throws UnknownBANException, TelusAPIException;
  
  /**
   * Disassociates a fleet from this account.  This method will also
   * disassociate any talkGroups associated with the fleet, if applicable.
   *
   * <P>This method can safely be called for a talkgroup that has already been added.
   *
   * <P>This method may involve a remote method call.
   *
   * @exception InvalidFleetException 	The fleet is inappropriate for the current operation.
   */
  void removeFleet(Fleet fleet) throws InvalidFleetException, TelusAPIException;
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
  IDENSubscriber[] getSubscribers(int urbanId, int fleetId, int maximum) throws UnknownBANException, TelusAPIException;

  /**
   * Disassociates a talkGroup from this account.  
   *
   * <P>This method may involve a remote method call.
   *
   * @exception TooManyObjectsException 	The talkgroup cannot be disassociated from this account if there are still subscribers attached to it.
   */
  void removeTalkGroup(TalkGroup talkGroup) throws TooManyObjectsException, TelusAPIException;

  /**
   * Refreshes Talk Group Array
   *
   * <P>This method may involve a remote method call.
   *
   */
  void refreshTalkGroups() throws TelusAPIException;
  
  /**
   * Refreshes Fleets Array
   *
   * <P>This method may involve a remote method call.
   *
   */
  
  public void refreshFleets() throws TelusAPIException; 


}


