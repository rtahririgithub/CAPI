/**
 * Title:        ClientStateReasonInfo<p>
 * Description:  Represent the Activity/Reason Codes  mapped in CRDB as ClientStateReasonID<p>
 * Copyright:    Copyright (c)<p>
 * Company:      Telus Mobility INC. <p>
 * @author       Carlos Manjarres
 * @version      1.0
 */

package com.telus.eas.utility.info;

import com.telus.eas.framework.info.Info;
import com.telus.api.reference.*;

public class ClientStateReasonInfo extends Info implements ClientStateReason {

  static final long serialVersionUID = 1L;

  private String code;
  private String description ;
  private String descriptionFrench ;
  private long reasonId;

  public String getCode(){
    return code;
  }

  public String getDescription() {
    return description;
  }

  public String getDescriptionFrench() {
    return descriptionFrench;
  }

  public long getReasonId() {
    return reasonId;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public void setDescription(String newDescription) {
    description = newDescription;
  }

  public void setDescriptionFrench(String newDescriptionFrench) {
    descriptionFrench = newDescriptionFrench;
  }

  public void setReasonId(long reasonId) {
    this.reasonId = reasonId;
  }
}
