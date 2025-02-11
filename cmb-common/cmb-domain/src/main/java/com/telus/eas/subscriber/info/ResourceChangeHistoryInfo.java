package com.telus.eas.subscriber.info;

/**
 * Title:        Telus Domain Project
 * Description:
 * Copyright:    Copyright (c) 2005
 * Company:      Telus Mobility
 * @author       Carlos Manjarres
 * @since        May-2005
 * @version      1.0
 */

import com.telus.api.account.*;

import com.telus.eas.framework.info.*;

import java.util.*;

public class ResourceChangeHistoryInfo  extends Info   implements ResourceChangeHistory {

	static final long serialVersionUID = 1L;

  private String type;
  private String value;
  private String status;
  private Date statusDate;
  private String knowbilityOperatorID;
  private String applicationID;

  public ResourceChangeHistoryInfo() {
  }

  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }

  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    this.value = value;
  }

  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }

  public Date getStatusDate() {
    return statusDate;
  }
  public void setStatusDate(Date statusDate) {
    this.statusDate = statusDate;
  }

  public String getKnowbilityOperatorID() {
    return knowbilityOperatorID;
  }
  public void setKnowbilityOperatorID(String knowbilityOperatorID) {
    this.knowbilityOperatorID = knowbilityOperatorID;
  }

  public String getApplicationID() {
    return applicationID;
  }
  public void setApplicationID(String applicationID) {
    this.applicationID = applicationID;
  }

  public String toString() {
    StringBuffer s = new StringBuffer(128);

    s.append("ResourceChangeHistoryInfo:[\n");
    s.append("    type=[").append(type).append("]\n");
    s.append("    value=[").append(value).append("]\n");
    s.append("    status=[").append(status).append("]\n");
    s.append("    statusDate=[").append(statusDate).append("]\n");
    s.append("    applicationId=[").append(applicationID).append("]\n");
    s.append("    knowbilityOperatorId=[").append(knowbilityOperatorID).append("]\n");
    s.append("]");

    return s.toString();
  }

}
