/*

 * $Id$

 * %E% %W%

 * Copyright (c) Clearnet Inc. All Rights Reserved.

 */



package com.telus.api.fleet;



import com.telus.api.*;



import com.telus.api.account.*;





public interface FleetManager {



  /**

   * Returns all public fleets for a given network.

   *

   * <P>This method may involve a remote method call.

   *

   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>

   * elements, but may contain no (zero) elements.

   *

   * @exception InvalidNetworkException if the network doesn't exist or is

   *            inappropriate for this operation.

   *

   * @link aggregation

   */

  Fleet[] getPublicFleets(int networkId) throws InvalidNetworkException, TelusAPIException;



  /**

   * Returns all fleets associated to a given account.

   *

   * <P>This method may involve a remote method call.

   *

   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>

   * elements, but may contain no (zero) elements.

   *

   */

  Fleet[] getFleetsByBan(int banId) throws UnknownBANException, TelusAPIException;



  /**

   * Returns the fleet assigned to a subscriber or <CODE>null</CODE>.

   *

   * <P>This method may involve a remote method call.

   *

   */

  Fleet getFleetByPhoneNumber(String phoneNumber) throws UnknownSubscriberException, TelusAPIException;



  /**

   * Returns the fleet with a given id.

   *

   * <P>This method may involve a remote method call.

   *

   * @exception UnknownObjectException if a fleet with the specified identity

   *            does not exist.

   *

   */

  Fleet getFleetById(int urbanID, int fleetId) throws UnknownObjectException, TelusAPIException;



}









