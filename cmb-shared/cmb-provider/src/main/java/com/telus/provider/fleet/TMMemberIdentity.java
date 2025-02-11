/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.fleet;


import com.telus.api.*;
import com.telus.api.fleet.*;
import com.telus.provider.*;
import com.telus.eas.account.info.*;



public class TMMemberIdentity extends BaseProvider implements MemberIdentity {
  /**
   * @link aggregation
   */
  private final MemberIdentityInfo delegate;
  private final TMFleetIdentity fleetIdentity;
  private boolean inRange;
  private String resourceStatus;

  public TMMemberIdentity(TMProvider provider, MemberIdentityInfo delegate, TMFleetIdentity fleetIdentity) {
    super(provider);

    this.delegate = delegate;
    this.fleetIdentity = fleetIdentity;
  }



  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------
  public String getMemberId() {
    return delegate.getMemberId();
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
  public MemberIdentityInfo getDelegate() {
    return delegate;
  }

  public void clear() {
    delegate.clear();
    fleetIdentity.clear();
  }

  public boolean isValid() {
    return fleetIdentity.isValid() && 
	delegate.getMemberId() != null
	;
  }

  public FleetIdentity getFleetIdentity() {
    return fleetIdentity;
  }

  public TMFleetIdentity getFleetIdentity0() {
    return fleetIdentity;
  }

  public boolean isInRange() throws  TelusAPIException{

  try {
  return (Integer.parseInt(getMemberId())<=(provider.getFleetManager().getFleetById(getFleetIdentity().getUrbanId(),getFleetIdentity().getFleetId())).getMaximumMemberIdInRange());
   }catch (Throwable e) {
      throw new TelusAPIException(e);
    }
  }
/**
 * @return Returns the resourceStatus.
 */
public String getResourceStatus() {
	return delegate.getResourceStatus();
}
}




