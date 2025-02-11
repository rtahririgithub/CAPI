/*
 * $Id$
 *
 * Copyright AbstractNotions 2001, 2002.  All rights reserved.  Proprietary and Confidential.
 */

package com.telus.eas.account.info;

import com.telus.api.account.AccountSummary;
import com.telus.api.account.IDENSubscriber;
import com.telus.api.fleet.Fleet;
import com.telus.api.fleet.TalkGroup;
import com.telus.eas.framework.info.Info;

@Deprecated
public class TalkGroupInfo extends Info implements TalkGroup
{

	 static final long serialVersionUID = 1L;

    private String name;
    private int talkGroupId;
    private FleetIdentityInfo fleetIdentity;
    private int priority;
    private int ownerBanId;
    private String originalName;

    public TalkGroupInfo(FleetIdentityInfo fleetIdentity) {
      this.fleetIdentity = fleetIdentity;
    }

    public TalkGroupInfo() {
      this(new FleetIdentityInfo());
    }

    public String getName(){
        return name;
    }

  public void setName(String name) {
  	if (this.name == null)	this.originalName = name;
  	this.name = name;
  }

  public String getOriginalName(){
    return originalName;
}

  public int getTalkGroupId(){
    return talkGroupId;
}

  public void setTalkGroupId(int talkGroupId) {
  	this.talkGroupId = talkGroupId;
  }

  public int getPriority(){
    return priority;
  }

  public void setPriority(int priority) {
  	this.priority = priority;
  }

  public int getOwnerBanId(){
    return ownerBanId;
  }

  public void setOwnerBanId(int ownerBanId){
    this.ownerBanId = ownerBanId;
  }

  public com.telus.eas.account.info.FleetIdentityInfo getFleetIdentity() {
    return fleetIdentity;
  }

    public String toString()
    {
        StringBuffer s = new StringBuffer(128);

        s.append("TalkGroupInfo:[\n");
        s.append("    name=[").append(name).append("]\n");
        s.append("    talkGroupId=[").append(talkGroupId).append("]\n");
        s.append("    fleetIdentity=[").append(fleetIdentity).append("]\n");
        s.append("    priority=[").append(priority).append("]\n");
        s.append("    ownerBanId=[").append(ownerBanId).append("]\n");
        s.append("]");

        return s.toString();
    }

    public Fleet getFleet(){
        throw new UnsupportedOperationException("Method not implemented here");
      }
    public AccountSummary getOwner(){
        throw new UnsupportedOperationException("Method not implemented here");
      }
    public int getAttachedSubscriberCount(int banId){
        throw new UnsupportedOperationException("Method not implemented here");
      }
    public IDENSubscriber[] getAttachedSubscribers(int banId){
        throw new UnsupportedOperationException("Method not implemented here");
      }
    public void save(){
        throw new UnsupportedOperationException("Method not implemented here");
      }
}


