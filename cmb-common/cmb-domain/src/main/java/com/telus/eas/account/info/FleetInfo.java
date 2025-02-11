/*
 * $Id$
 *
 * Copyright AbstractNotions 2001, 2002.  All rights reserved.  Proprietary and Confidential.
 */

package com.telus.eas.account.info;

import com.telus.api.DuplicateObjectException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.AccountSummary;
import com.telus.api.fleet.Fleet;
import com.telus.api.fleet.FleetIdentity;
import com.telus.api.fleet.MemberIdentity;
import com.telus.api.fleet.TalkGroup;
import com.telus.eas.framework.info.Info;

@Deprecated
public class FleetInfo extends Info implements Fleet
{

	static final long serialVersionUID = 1L;

    private FleetIdentityInfo identity = new FleetIdentityInfo();
    private int banId;
    private String name;
    private char type;
    private int expectedTalkGroups;
    private int expectedSubscribers;
    private TalkGroupInfo[] talkGroups;
    private int networkId;
    private int activeSubscribersCount;
    private int minimumMemberIdInRange = 1;
    private Boolean pTNBased;
    private String fleetClass;
    private	int DAPId ;
    private int associatedAccountsCount;
    private String ownerName;

    public FleetIdentity getIdentity(){
        return getIdentity0();
    }

    public FleetIdentityInfo getIdentity0() {
      return identity;
    }

    public AccountSummary getOwner(){
       throw new UnsupportedOperationException("Method not implemented here");
    }

    public void setBanId0(int banId){
     this.banId = banId;
    }

    public int getBanId0(){
      return this.banId;
    }

    public String getName(){
        return name;
    }

    public char getType(){
        return type;
    }

      public String getFleetClass(){
        return fleetClass;
    }

    public boolean isPublic(){
        return type == Fleet.TYPE_PUBLIC;
    }

    public int getExpectedTalkGroups(){
        return expectedTalkGroups;
    }

    public int getExpectedSubscribers(){
        return expectedSubscribers;
    }

    public TalkGroup[] getTalkGroups(){
        throw new UnsupportedOperationException("Method not implemented here");
    }

    public TalkGroup getTalkGroup(int talkGroupId){
        throw new UnsupportedOperationException("Method not implemented here");
    }

    public TalkGroup newTalkGroup(String name) throws DuplicateObjectException, TelusAPIException{
        return null;
    }

    public TalkGroup[] newTalkGroups(String[] name) throws DuplicateObjectException, TelusAPIException{
        return null;
    }

  public void setType(char type) {
    this.type = type;
  }

  public void setFleetClass(String newFleetClass) {
    this.fleetClass = newFleetClass;
  }

  public void setName(String name) {
    this.name = name;
  }
  /*
  public void setIdentity(FleetIdentityInfo identity) {
    this.identity = identity;
  }
  */
  public void setExpectedSubscribers(int expectedSubscribers) {
    this.expectedSubscribers = expectedSubscribers;
  }
  public void setExpectedTalkGroups(int expectedTalkGroups) {
    this.expectedTalkGroups = expectedTalkGroups;
  }
  public void setNetworkId(int networkId) {
    this.networkId = networkId;
  }
  public int getNetworkId() {
    return networkId;
  }
  public void setActiveSubscribersCount(int activeSubscribersCount) {
    this.activeSubscribersCount = activeSubscribersCount;
  }
  public int getActiveSubscribersCount() {
    return activeSubscribersCount;
  }

    public String toString()
    {
        StringBuffer s = new StringBuffer(128);

        s.append("FleetInfo:[\n");
        s.append("    identity=[").append(identity).append("]\n");
        s.append("    banId=[").append(banId).append("]\n");
        s.append("    name=[").append(name).append("]\n");
        s.append("    type=[").append(type).append("]\n");
        s.append("    fleetClass=[").append(fleetClass).append("]\n");
        s.append("    expectedTalkGroups=[").append(expectedTalkGroups).append("]\n");
        s.append("    expectedSubscribers=[").append(expectedSubscribers).append("]\n");
        if(talkGroups == null)
        {
            s.append("    talkGroups=[null]\n");
        }
        else if(talkGroups.length == 0)
        {
            s.append("    talkGroups={}\n");
        }
        else
        {
            for(int i=0; i<talkGroups.length; i++)
            {
                s.append("    talkGroups["+i+"]=[").append(talkGroups[i]).append("]\n");
            }
        }
        s.append("    networkId=[").append(networkId).append("]\n");
        s.append("    activeSubscribersCount=[").append(activeSubscribersCount).append("]\n");
        s.append("    DAPId=[").append(DAPId).append("]\n");
        s.append("    associatedAccountsCount=[").append(associatedAccountsCount).append("]\n");
        s.append("    ownerName=[").append(ownerName).append("]\n");

        s.append("]");

        return s.toString();
    }

  public int getMinimumMemberIdInRange()  {
     return this.minimumMemberIdInRange;
  }

  public int getMaximumMemberIdInRange() throws TelusAPIException{
     throw new UnsupportedOperationException("Method not implemented here");
  }
  public void setPTNBased(boolean pTNBased) {
    this.pTNBased = new Boolean(pTNBased);
  }
  public boolean isPTNBased() throws TelusAPIException{
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public Boolean isPTNBasedFleet(){
    return pTNBased;
  }

	/**
	 * @return Returns the dAPId.
	 */
	public int getDAPId() {
		return DAPId;
	}
	/**
	 * @param id The dAPId to set.
	 */
	public void setDAPId(int id) {
		DAPId = id;
	}
	/**
	 * @return Returns the associatedTalkgroupCount.
	 */
	public int getAssociatedTalkgroupsCount(int banId)throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}
	/**
	 * @return Returns the attachedSubscriberCount.
	 */
	public int getAttachedSubscribersCount(int banId)throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}
	/**
	 * @return Returns the ownerBanId.
	 */
	public int getOwnerBanId() {
		return getBanId0();
	}

	/**
	 * @return Returns the ownerName.
	 */
	public String getOwnerName() {
		return ownerName;
	}
	/**
	 * @param ownerName The ownerName to set.
	 */
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	/**
	 * @return Returns the associatedAccountsCount.
	 */
	public int getAssociatedAccountsCount() {
		return associatedAccountsCount;
	}
	/**
	 * @param associatedAccountsCount The associatedAccountsCount to set.
	 */
	public void setAssociatedAccountsCount(int associatedAccountsCount) {
		this.associatedAccountsCount = associatedAccountsCount;
	}

	public MemberIdentity[] getAvailableMemberIdentities(int max) throws TelusAPIException{
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public MemberIdentity[] getAvailableMemberIdentities(String memberIdPattern, int max) throws TelusAPIException{
		throw new UnsupportedOperationException("Method not implemented here");
	}

}


