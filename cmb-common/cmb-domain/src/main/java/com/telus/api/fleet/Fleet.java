
package com.telus.api.fleet;

import com.telus.api.*;

import com.telus.api.account.*;


public interface Fleet {

  public static final char TYPE_SHARED  = 'S';
  public static final char TYPE_PUBLIC  = 'B';
  public static final char TYPE_PRIVATE = 'P';


  /**
   * @link aggregationByValue
   */
  FleetIdentity getIdentity();

  /**
   * @link aggregation
   */
  AccountSummary getOwner() throws TelusAPIException;

  String getName();

  //void setName(String name);

  char getType();

  boolean isPublic();

  int getExpectedTalkGroups();

  int getExpectedSubscribers();
  int getNetworkId();
  /**
   * Returns list of all talk groups for this fleet
   *
   * <P>This method may involve a remote method call.
   *
   * @exception TelusAPIException
   */
  TalkGroup[] getTalkGroups() throws TelusAPIException;

  /**
   * Returns specific talkgroup for this fleet or null if talk group for given id not found.
   *
   * <P>This method may involve a remote method call.
   *
   * @param		talkGroupId	talk group id
   *
   * @exception TelusAPIException
   */
  TalkGroup getTalkGroup(int talkGroupId) throws TelusAPIException;

  /**
   * Creates a new talkGroup in this fleet with the specified name.
   *
   * <P>This method may involve a remote method call.
   *
   * @exception DuplicateObjectException if a TalkGroup with the specified name
   *            already exists in this fleet.
   * @exception TooManyObjectsException if the addition has exceeded the number allowed.
   */
  TalkGroup newTalkGroup(String name) throws DuplicateObjectException, TooManyObjectsException, TelusAPIException;

  /**
   * Creates a set of new talkGroups in this fleet with the specified names.
   *
   * <P>This method may involve a remote method call.
   *
   * @param name the non-null array of talkgroup names.
   *
   * @exception DuplicateObjectException if a TalkGroup with any of the specified names
   *            already exists in this fleet.
   * @exception TooManyObjectsException if the addition has exceeded the number allowed.
   */
  TalkGroup[] newTalkGroups(String[] name) throws DuplicateObjectException, TooManyObjectsException, TelusAPIException;

  //void save();

  //void refresh();


  int getMinimumMemberIdInRange();

  int getMaximumMemberIdInRange()  throws TelusAPIException;

  boolean isPTNBased()throws TelusAPIException;

  String getFleetClass();

  /**
   * Returns DAP ID .
   *
   */

  int getDAPId ()throws TelusAPIException;
  /**
   * Returns number of subscribers, attached to this fleet.
   *
   * <P>This method may involve a remote method call.
   *
   *
   */
  int getAttachedSubscribersCount(int banId)throws TelusAPIException ;
  /**
   * Returns owner Ban Id .
   *
   */
  int getOwnerBanId()throws TelusAPIException;

  /**
   * Returns owner name.
   *
   */
  String getOwnerName()throws TelusAPIException;

  /**
   * Returns number of Talk groups, associated with the fleet.
   *
   * <P>This method may involve a remote method call.
   *
   *
   */
  int getAssociatedTalkgroupsCount(int banId)throws TelusAPIException;

  /**
   * Returns number of accounts, attached to this fleet.
   *
   *
   */
  int getAssociatedAccountsCount()throws TelusAPIException ;

  public MemberIdentity[] getAvailableMemberIdentities(int max) throws TelusAPIException;
  public MemberIdentity[] getAvailableMemberIdentities(String memberIdPattern, int max) throws TelusAPIException;


}




