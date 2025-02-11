/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.*;
import com.telus.eas.framework.info.*;
import java.util.*;

public class ServiceSubscriberCountInfo extends Info implements ServiceSubscriberCount {

   static final long serialVersionUID = 1L;

  private String serviceCode;
  private String[] activeSubscribers;
  private String[] reservedSubscribers;
  private String[] canceledSubscribers;
  private boolean maximumSubscriberReached=false;
  private String[] suspendedSubscribers;

  public ServiceSubscriberCountInfo() {
  }

  public Set getActiveAndReservedSubscribers(Set set) {
    for (int i = 0; i < activeSubscribers.length; i++) {
      set.add(activeSubscribers[i]);
    }

    for (int i = 0; i < reservedSubscribers.length; i++) {
      set.add(reservedSubscribers[i]);
    }

    return set;
  }

  public String[] getActiveAndReservedSubscribers() {
    Set set = getActiveAndReservedSubscribers(new HashSet()) ;
    return (String[])set.toArray(new String[set.size()]);
  }

//  public String[] getActiveAndReservedSubscribers() {
//    String[] result = new String[activeSubscribers.length + reservedSubscribers.length];
//    System.arraycopy(activeSubscribers, 0, result, 0, activeSubscribers.length);
//    System.arraycopy(reservedSubscribers, 0, result, activeSubscribers.length, reservedSubscribers.length);
//    return result;
//  }

  public String getServiceCode() {
    return serviceCode;
  }

  public void setServiceCode(String serviceCode) {
    this.serviceCode = serviceCode;
  }

  public String[]  getActiveSubscribers() {
    return activeSubscribers;
  }

  public void setActiveSubscribers(String[] activeSubscribers){
    this.activeSubscribers = activeSubscribers;
  }

  public String[]  getReservedSubscribers() {
    return reservedSubscribers;
  }

  public void setReservedSubscribers(String[] reservedSubscribers){
    this.reservedSubscribers = reservedSubscribers;
  }

  public String[]  getCanceledSubscribers() {
    return canceledSubscribers;
  }

  public void setCanceledSubscribers(String[] canceledSubscribers){
    this.canceledSubscribers = canceledSubscribers;
  }

  public boolean isMaximumSubscriberReached(){
    return maximumSubscriberReached;
  }

  public void setMaximumSubscriberReached(boolean maximumSubscriberReached){
    this.maximumSubscriberReached = maximumSubscriberReached;
  }

  public void setSuspendedSubscribers(String[] suspendedSubscribers) {
    this.suspendedSubscribers = suspendedSubscribers;
  }

  public String[] getSuspendedSubscribers() {
    return suspendedSubscribers;
  }

  public String toString()
  {
      StringBuffer s = new StringBuffer(128);

      s.append("ServiceSubscriberCountInfo:[\n");
      s.append("    serviceCode=[").append(serviceCode).append("]\n");
      if(activeSubscribers == null)
      {
          s.append("    activeSubscribers=[null]\n");
      }
      else if(activeSubscribers.length == 0)
      {
          s.append("    activeSubscribers={}\n");
      }
      else
      {
          for(int i=0; i<activeSubscribers.length; i++)
          {
              s.append("    activeSubscribers["+i+"]=[").append(activeSubscribers[i]).append("]\n");
          }
      }
      if(reservedSubscribers == null)
      {
          s.append("    reservedSubscribers=[null]\n");
      }
      else if(reservedSubscribers.length == 0)
      {
          s.append("    reservedSubscribers={}\n");
      }
      else
      {
          for(int i=0; i<reservedSubscribers.length; i++)
          {
              s.append("    reservedSubscribers["+i+"]=[").append(reservedSubscribers[i]).append("]\n");
          }
      }
      if(canceledSubscribers == null)
      {
          s.append("    canceledSubscribers=[null]\n");
      }
      else if(canceledSubscribers.length == 0)
      {
          s.append("    canceledSubscribers={}\n");
      }
      else
      {
          for(int i=0; i<canceledSubscribers.length; i++)
          {
              s.append("    canceledSubscribers["+i+"]=[").append(canceledSubscribers[i]).append("]\n");
          }
      }
      s.append("    maximumSubscriberReached=[").append(maximumSubscriberReached).append("]\n");
      if(suspendedSubscribers == null)
      {
          s.append("    suspendedSubscribers=[null]\n");
      }
      else if(suspendedSubscribers.length == 0)
      {
          s.append("    suspendedSubscribers={}\n");
      }
      else
      {
          for(int i=0; i<suspendedSubscribers.length; i++)
          {
              s.append("    suspendedSubscribers["+i+"]=[").append(suspendedSubscribers[i]).append("]\n");
          }
      }
      s.append("]");

      return s.toString();
  }


}




