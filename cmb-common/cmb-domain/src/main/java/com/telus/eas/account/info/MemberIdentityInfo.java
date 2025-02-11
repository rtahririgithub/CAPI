/*
 * $Id$
 *
 * Copyright AbstractNotions 2001, 2002.  All rights reserved.  Proprietary and Confidential.
 */

package com.telus.eas.account.info;

import com.telus.api.*;
import com.telus.api.fleet.MemberIdentity;
import com.telus.api.fleet.FleetIdentity;
import com.telus.eas.framework.info.*;

public class MemberIdentityInfo extends Info implements MemberIdentity
{

	static final long serialVersionUID = 1L;

    private FleetIdentityInfo fleetIdentity = new FleetIdentityInfo();
    private String memberId;
    private String resourceStatus = "";

    public MemberIdentityInfo() {
    }

  public void clear() {
    fleetIdentity.clear();
    memberId = null;
    resourceStatus = "";
  }

    public Object clone() {
      MemberIdentityInfo o = (MemberIdentityInfo)super.clone();

      o.fleetIdentity  = (FleetIdentityInfo)clone(fleetIdentity);

      return o;
    }

    /*
    public MemberIdentityInfo(FleetIdentityInfo fleetIdentity,String memberId) {
      this.fleetIdentity = fleetIdentity ;
      this.memberId = memberId;
    }
    */

    public FleetIdentity getFleetIdentity(){
      return fleetIdentity;
    }

    public FleetIdentityInfo getFleetIdentity0(){
      return fleetIdentity;
    }

    public String getMemberId(){
        return memberId;
    }

    public void setMemberId(String memberId){
      this.memberId = memberId;
    }

  public void copyFrom(MemberIdentityInfo o) {
    fleetIdentity.copyFrom(o.fleetIdentity);
    memberId = o.memberId;
    resourceStatus = o.resourceStatus;
  }

    public String toString()
    {
        StringBuffer s = new StringBuffer(128);

        s.append("MemberIdentityInfo:[\n");
        s.append("    fleetIdentity=[").append(fleetIdentity).append("]\n");
        s.append("    memberId=[").append(memberId).append("]\n");
        s.append("    resourceStatus=[").append(resourceStatus).append("]\n");
        s.append("]");

        return s.toString();
    }

    public boolean isInRange() throws  TelusAPIException{
    throw new UnsupportedOperationException("Method not implemented here");
  }
	/**
	 * @return Returns the resourceStatus.
	 */
	public String getResourceStatus() {
		return resourceStatus;
	}
	/**
	 * @param resourceStatus The resourceStatus to set.
	 */
	public void setResourceStatus(String resourceStatus) {
		this.resourceStatus = resourceStatus;
	}
}


