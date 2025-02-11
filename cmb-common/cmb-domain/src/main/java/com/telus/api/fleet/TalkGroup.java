/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.fleet;

import com.telus.api.*;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.IDENSubscriber;

public interface TalkGroup {

  /**
   * Returns the fleet this talkGroup belongs to.
   *
   * <P>This method may involve a remote method call.
   *
   * @link aggregation
   */
  Fleet getFleet() throws TelusAPIException;

  int getTalkGroupId();

  String getName();
  void setName(String newName);

  int getPriority();
  void setPriority(int newPriority);

  int getOwnerBanId();
  

  /**
   * Returns TalkGroup owner's account information.
   *
   * <P>This method may involve a remote method call.
   */
  AccountSummary getOwner()  throws TelusAPIException;
  
  /**
   * Returns count of subscribers attached to this TalkGroup for the given account.
   *
   * <P>This method may involve a remote method call.
   */
  int getAttachedSubscriberCount(int banId)  throws TelusAPIException;
  
  /**
   * Returns array of subscribers attached to this TalkGroup for the given account.
   *
   * <P>This method may involve a remote method call.
   */
  IDENSubscriber[] getAttachedSubscribers(int banId)  throws TelusAPIException;
  
  /**
   * Saves changes to the TalkGroup.
   *
   * <P>This method may involve a remote method call.
   */
  void save()  throws TelusAPIException, DuplicateObjectException;
}