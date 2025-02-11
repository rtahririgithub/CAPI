/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.*;
import com.telus.eas.framework.info.*;
import java.util.Date;

public class MemoCriteriaInfo extends Info implements MemoCriteria {

  static final long serialVersionUID = 1L;

  private int banId;
  private String[] subscriberIds;
  private String manualText;
  private String systemText;
  private Date fromDate;
  private Date toDate;
  private String[] types;
  private int searchLimit;

  public void clear() {
    banId = 0;
    subscriberIds = null;
    systemText = null;
    manualText = null;
    fromDate = null;
    toDate = null;
    types = null;
  }

  public int getBanId() {
    return banId;
  }
  
  public String getSubscriberId() {
    if (subscriberIds != null && subscriberIds.length > 0)
      return subscriberIds[0];
      
    return null;
  }
  
  public String[] getSubscriberIds(){
    return subscriberIds;
  }
  
  public String getSystemText() {
    return systemText;
  }
  
  public String getManualText() {
    return manualText;
  }
  
  public Date getDateFrom() {
    return fromDate;
  }
  
  public Date getDateTo() {
    return toDate;
  }
  
  public String getType() {
    if (types != null && types.length > 0)
      return types[0];
      
    return null;
  }
  
  public String[] getTypes() {
    return types;
  }
  
  public void setBanId(int value) {
    banId = value;
  }
  
  public void setSubscriberId(String value) {
    if (subscriberIds == null)
      subscriberIds = new String[1];
      
    subscriberIds[0] = value;
  }
  
  public void setSubscriberIds(String[] value) {
    subscriberIds = value;
  }
  
  public void setSystemText(String value) {
    systemText = value;
  }
  
  public void setManualText(String value) {
    manualText = value;
  }
  
  public void setDateFrom(Date value) {
    fromDate = value;
  }
  
  public void setDateTo(Date value) {
    toDate = value;
  }
  
  public void setType(String value) {
    if (types == null)
      types = new String[1];
      
    types[0] = value;
  }
  
  public void setTypes(String[] value) {
    types = value;
  }
  
  public void copyFrom(MemoCriteria memoCriteria) {}

  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("MemoCriteria:{\n");
    s.append("    banId=[").append(banId).append("]\n");
    for (int i = 0; i < (subscriberIds != null ? subscriberIds.length : 0); i++)
      s.append("    subscribers[" + i + "]=[").append(subscriberIds[i]).append("]\n");
    s.append("    systemText=[").append(systemText).append("]\n");
    s.append("    manualText=[").append(manualText).append("]\n");
    s.append("    fromDate=[").append(fromDate).append("]\n");
    s.append("    toDate=[").append(toDate).append("]\n");
    for (int i = 0; i < (types != null ? types.length : 0); i++)
    s.append("    types[" + i + "]=[").append(types[i]).append("]\n");
    s.append("    searchLimit=[").append(searchLimit).append("]\n");
    s.append("}");

    return s.toString();
  }
/**
 * @return Returns the searchLimit.
 */
public int getSearchLimit() {
	return searchLimit;
}
/**
 * @param searchLimit The searchLimit to set.
 */
public void setSearchLimit(int searchLimit) {
	this.searchLimit = searchLimit;
}
}
