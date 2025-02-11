/**
 * Title:        IdenResourcesInfo<p>
 * Description:  The IdenResourcesInfo holds all resources for an IDEN subscriber.<p>
 * Copyright:    Copyright (c) Peter Frei<p>
 * Company:      Telus Mobility Inc<p>
 * @author Peter Frei
 * @version 1.0
 */

package com.telus.eas.subscriber.info;

import com.telus.eas.framework.info.*;


public class HSPAResourcesInfo extends Info {

  static final long serialVersionUID = 1L;

  private String subscriberId;
  private String phoneNumber;
  private String hspaImsi;
  private String resourceStatus;

  public HSPAResourcesInfo() {
  }

  public void setSubscriberId(String subscriberId) {
    this.subscriberId = subscriberId;
  }
  public String getSubscriberId() {
    return subscriberId;
  }
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
  public String getPhoneNumber() {
    return phoneNumber;
  }
   
  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("HSPAResourcesInfo:{\n");
    s.append("    subscriberId=[").append(subscriberId).append("]\n");
    s.append("    phoneNumber=[").append(phoneNumber).append("]\n");
    s.append("    resourceStatus=[").append(resourceStatus).append("]\n");
    s.append("    hspaImsi=[").append(hspaImsi).append("]\n");
    s.append("}");

    return s.toString();
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

public String getHspaImsi() {
	return hspaImsi;
}

public void setHspaImsi(String hspaImsi) {
	this.hspaImsi = hspaImsi;
}
}

