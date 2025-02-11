/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.*;
import java.util.*;

public class PricePlanSubscriberCountInfo extends ServiceSubscriberCountInfo implements PricePlanSubscriberCount {

  public static final long serialVersionUID = 7215727533501515874L;

  private ServiceSubscriberCountInfo[] serviceSubscriberCounts = new ServiceSubscriberCountInfo[0];
  private String pricePlanCode;
  private String[] futureDatedSubscribers;

  public PricePlanSubscriberCountInfo() {
  }

//  public Set getActiveAndReservedSubscribers(Set set) {
//    for (int i = 0; i < serviceSubscriberCounts.length; i++) {
//      serviceSubscriberCounts[i].getActiveAndReservedSubscribers(set);
//    }
//
//    return set;
//  }

  public String[] getActiveAndReservedSubscribers() {
    Set set = getActiveAndReservedSubscribers(new HashSet()) ;
    return (String[])set.toArray(new String[set.size()]);
  }



  public String[]  getFutureDatedSubscribers() {
    return futureDatedSubscribers;
  }

  public void setFutureDatedSubscribers(String[] futureDatedSubscribers){
    this.futureDatedSubscribers = futureDatedSubscribers;
  }

  public String getPricePlanCode() {
    return pricePlanCode;
  }

  public void setPricePlanCode(String pricePlanCode){
    this.pricePlanCode = pricePlanCode;
  }

  public ServiceSubscriberCount[] getServiceSubscriberCounts() {
    return serviceSubscriberCounts;
  }

  public void setServiceSubscriberCounts(ServiceSubscriberCountInfo[] serviceSubscriberCountsArray){
    this.serviceSubscriberCounts = serviceSubscriberCountsArray;
  }

  public String toString()
  {
      StringBuffer s = new StringBuffer(128);

      s.append("PricePlanSubscriberCountInfo:[\n");
//    s.append("    futureDatedSubscribers=[").append(futureDatedSubscribers).append("]\n");
      if(futureDatedSubscribers == null)
      {
          s.append("    futureDatedSubscribers=[null]\n");
      }
      else if(futureDatedSubscribers.length == 0)
      {
          s.append("    futureDatedSubscribers={}\n");
      }
      else
      {
          for(int i=0; i<futureDatedSubscribers.length; i++)
          {
              s.append("    futureDatedSubscribers["+i+"]=[").append(futureDatedSubscribers[i]).append("]\n");
          }
      }
      s.append("    pricePlanCode=[").append(pricePlanCode).append("]\n");
      s.append("    super=[").append(super.toString()).append("]\n");
      s.append("]");

      return s.toString();
  }


}




