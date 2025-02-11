package com.telus.eas.utility.info;

/**
 * <p>Title: Telus Domain Project</p>
 * <p>CollectionAgency </p>
 * <p>Copyright: Copyright (c) 2004</p>
 */
import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;

public class CollectionAgencyInfo extends Info implements CollectionAgency{

  static final long serialVersionUID = 1L;
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