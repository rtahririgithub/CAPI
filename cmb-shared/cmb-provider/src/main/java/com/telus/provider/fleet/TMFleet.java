/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.fleet;

import java.util.ArrayList;

import com.telus.api.DuplicateObjectException;
import com.telus.api.TelusAPIException;
import com.telus.api.TooManyObjectsException;
import com.telus.api.UnknownObjectException;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.UnknownBANException;
import com.telus.api.fleet.Fleet;
import com.telus.api.fleet.FleetIdentity;
import com.telus.api.fleet.MemberIdentity;
import com.telus.api.fleet.TalkGroup;
import com.telus.api.util.SessionUtil;
import com.telus.eas.account.info.FleetInfo;
import com.telus.eas.account.info.MemberIdentityInfo;
import com.telus.eas.account.info.TalkGroupInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;


public class TMFleet extends BaseProvider implements Fleet {
  /**
   * @link aggregation
   */
  private final FleetInfo delegate;
  private final TMFleetIdentity identity;
  private ArrayList talkGroupArray = new ArrayList();

  private final static String[] fleetClass = {"A", "B", "C", "E"};

  public TMFleet(TMProvider provider, FleetInfo delegate) {
    super(provider);

    this.delegate = delegate;
    identity = new TMFleetIdentity(provider, delegate.getIdentity0(), this);
  }


  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------
  public String getName() {
    return delegate.getName();
  }

  public char getType() {
    return delegate.getType();
  }

  public String getFleetClass(){
    return delegate.getFleetClass();
  }

  public boolean isPublic() {
    return delegate.isPublic();
  }


  public int getExpectedTalkGroups() {
    return delegate.getExpectedTalkGroups();
  }
  public int getNetworkId(){
    return delegate.getNetworkId();
  }

  public int getExpectedSubscribers() {
    return delegate.getExpectedSubscribers();
  }

  public int hashCode() {
    return delegate.hashCode();

  }



  public String toString() {

    return delegate.toString();

  }

  public FleetInfo getFleet0(){
    return delegate;
 }


 public int getMinimumMemberIdInRange() {
   return delegate.getMinimumMemberIdInRange() ;
 }

public boolean validFleetClass(String Class){
  for(int i = 0; i < fleetClass.length; i++){
    if(fleetClass[i].equals(Class)){
      return true;
    }
  }
  return false;
}

 public int getMaximumMemberIdInRange() throws TelusAPIException{
/*
   if(delegate.getFleetClass() == null || !validFleetClass(delegate.getFleetClass())){
   Fleet fleet = provider.getFleetManager().getFleetById(identity.getUrbanId(), identity.getFleetId());
    delegate.setFleetClass(fleet.getFleetClass());
}
 */
   return provider.getReferenceDataManager().getFleetClass(delegate.getFleetClass()).getMaximumMemberIdInRange();
 }


 public int getDAPId() {
	return delegate.getDAPId();
}

 public int getOwnerBanId() {
	return delegate.getBanId0();
}

 public String getOwnerName() {
 	return delegate.getOwnerName();
 }



  //--------------------------------------------------------------------
  //  Service Methods
  //--------------------------------------------------------------------

  public FleetIdentity getIdentity() {
    return getIdentity0();
  }

  public TMFleetIdentity getIdentity0() {
    return identity;
  }



  public boolean isPTNBased() throws TelusAPIException{
    if(delegate.isPTNBasedFleet() == null){
       Fleet fleet = provider.getFleetManager().getFleetById(identity.getUrbanId(), identity.getFleetId());
      delegate.setPTNBased(fleet.isPTNBased());
    }
    return delegate.isPTNBasedFleet().booleanValue();
  }


  public AccountSummary getOwner() throws TelusAPIException {
   try {
     return provider.getAccountManager().findAccountByBAN(delegate.getBanId0());
    } catch (Throwable e) {
      throw new TelusAPIException(e);
    }
  }

  private TMTalkGroup[] decorate(TalkGroup[] talkGroups) throws TelusAPIException, UnknownBANException {
    TMTalkGroup[] newTalkGroups = new TMTalkGroup[talkGroups.length];
    for(int i=0; i<talkGroups.length; i++) {
      TalkGroupInfo info = (TalkGroupInfo)talkGroups[i];
      newTalkGroups[i] = new TMTalkGroup(provider, info, new TMFleetIdentity(provider, delegate.getIdentity0()));
    }
   return newTalkGroups;
  }

   private TalkGroup decorate(TalkGroup talkGroup) throws TelusAPIException, UnknownBANException {
      TalkGroupInfo info = (TalkGroupInfo)talkGroup;
      return new TMTalkGroup(provider, info, new TMFleetIdentity(provider, delegate.getIdentity0()));
   }


  public TalkGroup[] getTalkGroups() throws TelusAPIException {
    try{
	  	if ( !talkGroupArray.isEmpty() )
			return (TMTalkGroup[])talkGroupArray.toArray(new TMTalkGroup[talkGroupArray.size()]);

	  	TalkGroup[] talkGroups = provider.getReferenceDataHelperEJB().retrieveTalkGroupsByFleetIdentity(identity.getDelegate());
	    TMTalkGroup[] tmTalkGroups = decorate(talkGroups);
	    for(int i = 0; i < tmTalkGroups.length; i++){
	     	talkGroupArray.add(tmTalkGroups[i]);
	    }
	    return tmTalkGroups;
    }catch(TelusException e){
      throw new TelusAPIException(e);
    }catch(Throwable e){
      throw new TelusAPIException(e);
    }
  }

  public TalkGroup getTalkGroup(int talkGroupId) throws UnknownObjectException, TelusAPIException {
    try{
  		// get all talk groups for this fleet
  	    TalkGroup[] allTalkGroupsForFleet = getTalkGroups();

  	    // return requested talk group (or null if id not found)
  	    for(int i = 0; i < allTalkGroupsForFleet.length; i++){
  	    	if (allTalkGroupsForFleet[i].getTalkGroupId() == talkGroupId)
  	    		return (TMTalkGroup)allTalkGroupsForFleet[i];
        }
  	    throw new UnknownObjectException("Invalid talk group id for this fleet");
    }catch(TelusException e){
      throw new TelusAPIException(e);
    }catch(Throwable e){
      throw new TelusAPIException(e);
    }
  }

  public TalkGroup newTalkGroup(String name) throws DuplicateObjectException, TooManyObjectsException, TelusAPIException {
  	try{
  		TalkGroupInfo talk =  new TalkGroupInfo(delegate.getIdentity0());
  		talk.setName(name);
  		TalkGroup newTalkGroup = provider.getAccountLifecycleManager().createTalkGroup(delegate.getBanId0(), talk,
					 SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
  		talkGroupArray.clear();
  		return newTalkGroup;
  	}catch (Throwable e) {
  		provider.getExceptionHandler().handleException(e);
  	}
  	return null;
  }

  public TalkGroup[] newTalkGroups(String[] name) throws DuplicateObjectException, TooManyObjectsException, TelusAPIException {
    ArrayList array = new ArrayList(name.length);
    for(int i = 0; i < name.length; i++){
      array.add(newTalkGroup(name[i]));
    }
     return (TalkGroup[])array.toArray(new TalkGroup[array.size()]);
  }

  public int getAssociatedTalkgroupsCount(int banId)throws TelusAPIException {
	  try{
		  return provider.getAccountInformationHelper().retrieveAssociatedTalkGroupsCount(identity.getDelegate(),banId);

	  }catch(Throwable e){
		  provider.getExceptionHandler().handleException(e);
	  }
	  return 0;
  }

  public int getAssociatedAccountsCount()throws TelusAPIException {
  	 return delegate.getAssociatedAccountsCount();
     }

  public int getAttachedSubscribersCount(int banId)throws TelusAPIException {
	  try{
		  return provider.getAccountInformationHelper().retrieveAttachedSubscribersCount(banId, identity.getDelegate());
	  }catch(Throwable e){
		  provider.getExceptionHandler().handleException(e);
	  }
	  return 0;
  }
  public MemberIdentity[] getAvailableMemberIdentities(int max) throws TelusAPIException{
	return getAvailMemberIdentities("",max);
  }

  public MemberIdentity[] getAvailableMemberIdentities(String memberIdPattern, int max) throws TelusAPIException{
	return getAvailMemberIdentities(memberIdPattern,max);
  }

  private MemberIdentity [] getAvailMemberIdentities(String memberIdPattern, int max) throws TelusAPIException{
		int [] ret = null;
		int urbanId = delegate.getIdentity().getUrbanId();
		int fleetId = delegate.getIdentity().getFleetId();
		try {
		    ret  = provider.getSubscriberManagerBean().getAvailableMemberIDs(urbanId,fleetId,memberIdPattern, max);
		    return constructMemberIdentities ( ret );
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
			return null;
		}
		
	}
	private MemberIdentity[] constructMemberIdentities(int[] memberIDs) {
		TMMemberIdentity[] result = new TMMemberIdentity[ memberIDs.length ];
		for( int i=0; i<memberIDs.length; i++ ) {
			MemberIdentityInfo info = new MemberIdentityInfo();
			info.getFleetIdentity0().copyFrom( delegate.getIdentity0() );
			info.setMemberId(String.valueOf(memberIDs[i]));
			//TODO
			info.setResourceStatus("") ;
			result[i] = new TMMemberIdentity(provider, info, identity );
		}
		return result;
	}

}









