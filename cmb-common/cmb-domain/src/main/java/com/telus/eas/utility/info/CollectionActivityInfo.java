package com.telus.eas.utility.info;

/**
 * <p>Title: Telus Domain Project</p>
 * <p>CollectionActivity </p>
 * <p>Copyright: Copyright (c) 200 4</p>
 */

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;

public class CollectionActivityInfo extends Info implements CollectionActivity{
  static final long serialVersionUID = 1L;

  public CollectionActivityInfo() {
  }

  private String code;
  private String description;
  private String descriptionFrench;

  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getDescriptionFrench() {
    return descriptionFrench;
  }
  public void setDescriptionFrench(String descriptionFrench) {
    this.descriptionFrench = descriptionFrench;
  }


}