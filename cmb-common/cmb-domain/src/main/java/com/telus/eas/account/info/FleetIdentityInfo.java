/*
 * $Id$
 *
 * Copyright AbstractNotions 2001, 2002.  All rights reserved.  Proprietary and Confidential.
 */

package com.telus.eas.account.info;

import com.telus.api.*;
import com.telus.api.fleet.FleetIdentity;
import com.telus.api.fleet.Fleet;
import com.telus.eas.framework.info.*;

@Deprecated
public class FleetIdentityInfo extends Info implements FleetIdentity
{

	static final long serialVersionUID = 1L;

    private int urbanId;
    private int fleetId;

    public FleetIdentityInfo() {
    }

  public void clear() {
    urbanId = 0;
    fleetId = 0;
  }


    public FleetIdentityInfo(int pUrbanId, int pFleetId) {
      setUrbanId(pUrbanId);
      setFleetId(pFleetId);
    }

    public int getUrbanId(){
        return urbanId;
    }

    public int getFleetId(){
        return fleetId;
    }

    public Fleet getFleet() throws TelusAPIException {
         throw new UnsupportedOperationException("Method not implemented here");
    }

    public boolean equals(Object o){
      return o instanceof FleetIdentity &&
      equals((FleetIdentity)o);
    }

    public boolean equals(FleetIdentity o){
     return o != null &&
     o.getFleetId() == getFleetId() &&
     o.getUrbanId() == getUrbanId();

    }

    public boolean equals0(FleetIdentityInfo o){
     return o != null &&
     o.getFleetId() == getFleetId() &&
     o.getUrbanId() == getUrbanId();
    }
  public void setFleetId(int fleetId) {
    this.fleetId = fleetId;
  }
  public void setUrbanId(int urbanId) {
    this.urbanId = urbanId;
  }

  public void copyFrom(FleetIdentityInfo o) {
    urbanId = o.urbanId;
    fleetId = o.fleetId;
  }

    public String toString()
    {

        StringBuffer s = new StringBuffer(128);



        s.append("FleetIdentityInfo:[\n");

        s.append("    urbanId=[").append(urbanId).append("]\n");

        s.append("    fleetId=[").append(fleetId).append("]\n");

        s.append("]");



        return s.toString();

    }


}


