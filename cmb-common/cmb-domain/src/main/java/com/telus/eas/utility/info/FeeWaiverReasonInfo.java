package com.telus.eas.utility.info;

import com.telus.eas.framework.info.Info;
import com.telus.api.reference.FeeWaiverReason;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class FeeWaiverReasonInfo extends Info implements FeeWaiverReason {

  static final long serialVersionUID = 1L;

  private String code;
  private String description;
  private String descriptionFrench;

  public FeeWaiverReasonInfo() {
  }

  public String getCode() {
     return code;
  }

  public String getDescription() {
    return description;
  }

  public String getDescriptionFrench() {
    return descriptionFrench;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setDescriptionFrench(String descriptionFrench) {
    this.descriptionFrench = descriptionFrench;

  }

}
