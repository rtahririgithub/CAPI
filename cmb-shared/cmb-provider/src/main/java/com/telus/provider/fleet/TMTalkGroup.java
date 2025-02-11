/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.fleet;

import java.util.Collection;

import com.telus.api.TelusAPIException;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.IDENSubscriber;
import com.telus.api.account.UnknownBANException;
import com.telus.api.fleet.Fleet;
import com.telus.api.fleet.FleetIdentity;
import com.telus.api.fleet.TalkGroup;
import com.telus.api.util.SessionUtil;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
import com.telus.eas.account.info.TalkGroupInfo;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.account.TMIDENSubscriber;


public class TMTalkGroup extends BaseProvider implements TalkGroup {
  /**
   * @link aggregation
   */
  private final TalkGroupInfo delegate;
  private final TMFleetIdentity fleetIdentity;

  public TMTalkGroup(TMProvider provider, TalkGroupInfo delegate, TMFleetIdentity fleetIdentity) {
    super(provider);
    this.delegate = delegate;
    this.fleetIdentity = fleetIdentity;
  }

  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------
  public int getTalkGroupId() {
    return delegate.getTalkGroupId();
  }

  public int getPriority() {
    return delegate.getPriority();
  }

  public void setPriority(int newPriority) {
    delegate.setPriority(newPriority);
  }

  public String getName() {
    return delegate.getName();
  }

  public void setName(String newName) {
    delegate.setName(newName);
  }

  public int getOwnerBanId() {
    return delegate.getOwnerBanId();
  }

  public int hashCode() {
    return delegate.hashCode();
  }

  public String toString() {
    return delegate.toString();
  }

  //--------------------------------------------------------------------
  //  Service Methods
  //--------------------------------------------------------------------
  public TalkGroupInfo getDelegate() {
    return delegate;
  }

  public FleetIdentity getFleetIdentity() {
    return fleetIdentity;
  }

  public TMFleetIdentity getFleetIdentity0() {
    return fleetIdentity;
  }

  public Fleet getFleet() throws TelusAPIException {
    return fleetIdentity.getFleet();
  }

  public TalkGroupInfo getTalkGroup0(){
    return delegate;
 }

  public int getAttachedSubscriberCount(int banId) throws TelusAPIException {

	  try {
		  return  provider.getAccountInformationHelper().retrieveAttachedSubscribersCountForTalkGroup(fleetIdentity.getUrbanId(),
				  fleetIdentity.getFleetId(), delegate.getTalkGroupId(), banId);
	  } catch (Throwable e){
		  provider.getExceptionHandler().handleException(e); 
	  }
	  return 0;
  }
  public IDENSubscriber[] getAttachedSubscribers(int banId)
			throws TelusAPIException {
		try {
			Collection c = provider.getSubscriberLifecycleHelper().retrieveSubscriberListByBanAndTalkGroup(banId,
				fleetIdentity.getUrbanId(),	fleetIdentity.getFleetId(),	delegate.getTalkGroupId(), 0);
			IDENSubscriber[] subscribers = (IDENSubscriber[])c.toArray(new IDENSubscriber[c.size()]);
			return decorate(subscribers, true);
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}
  public AccountSummary getOwner() throws TelusAPIException {
    try {
      if (getOwnerBanId() != 0)
      	return provider.getAccountManager().findAccountByBAN(getOwnerBanId());
      else
      	return null;
     } catch (Throwable e) {
       throw new TelusAPIException(e);
     }
   }
  public void save() throws TelusAPIException {
	  try{
		  provider.getAccountLifecycleManager().updateTalkGroup(delegate.getOwnerBanId(), delegate, 
				  SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
	  }catch (Throwable e) {
		  provider.getExceptionHandler().handleException(e);
	  }
  }
  private IDENSubscriber[] decorate(IDENSubscriber[] subscribers,
			boolean existingSubscriber) throws TelusAPIException,
			UnknownBANException {
		for (int i = 0; i < subscribers.length; i++) {
			subscribers[i] = new TMIDENSubscriber(provider,
					(IDENSubscriberInfo) subscribers[i], !existingSubscriber,
					null);
		}
		return subscribers;
	}

}
