/**
 * Title:        CommitmentInfo<p>
 * Description:  The CommitmentInfo holds all attributes related to the contractual commitment of a subscriber.<p>
 * Copyright:    Copyright (c) Peter Frei<p>
 * Company:      Telus Mobility Inc<p>
 * @author Peter Frei
 * @version 1.0
 */

package com.telus.eas.subscriber.info;

import com.telus.api.account.*;
import com.telus.eas.framework.info.*;
import java.util.*;


public class CommitmentInfo extends Info implements SubscriberCommitment {


  static final long serialVersionUID = 1L;

  private String reasonCode;
  private int months;
  private Date startDate;
  private Date endDate;
  private boolean modified;

  public CommitmentInfo() {
  }

  public String getReasonCode() {
    return reasonCode;
  }

  public void setReasonCode(String newReasonCode) {
    this.reasonCode = newReasonCode;
    modified = true;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date newStartDate) {
    this.startDate = newStartDate;
    //setEndDate();
    modified = true;
  }

  public int getMonths() {
    return months;
  }

  public void setMonths(int newMonths) {
    this.months = newMonths;
    setEndDate();
    modified = true;
  }

  public Date getEndDate() {
    return endDate;
  }

  private void setEndDate() {
    if(startDate != null)  {
      endDate = new Date(startDate.getTime());
      endDate.setMonth(endDate.getMonth() + months);
    }
  }

  public void setEndDate(Date newEndDate) {
    this.endDate = newEndDate;
    modified = true;
  }

  public boolean isModified(){
    return modified;
  }

  public boolean isValid(){
    return endDate != null;
  }

  public void setModified(boolean modified){
    this.modified = modified;
  }

  public Object clone() {
    CommitmentInfo o = (CommitmentInfo) super.clone();

    o.setReasonCode(reasonCode);
    o.setMonths(months);
    o.setStartDate( cloneDate( startDate ) );
    o.setEndDate(cloneDate( endDate) );
    o.setModified(modified);

    return o;
  }

  public String toString() {
    StringBuffer s = new StringBuffer(128);

    s.append("CommitmentInfo:{\n");
    s.append("    reasonCode=[").append(reasonCode).append("]\n");
    s.append("    months=[").append(months).append("]\n");
    s.append("    startDate=[").append(startDate).append("]\n");
    s.append("    endDate=[").append(endDate).append("]\n");
    s.append("    modified=[").append(modified).append("]\n");
    s.append("}");

    return s.toString();
  }

}

