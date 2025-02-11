package com.telus.eas.utility.info;

/**
 * Title:        Telus Domain Project
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */


import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;

public class PricePlanTermInfo extends Info implements Reference {

	static final long serialVersionUID = 1L;

  private String code;
  private String description = "";
  private String descriptionFrench = "";
  private int[] termsInMonths;


  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getDescription() {
    return description;
  }
  public void setDescriptionFrench(String descriptionFrench) {
    this.descriptionFrench = descriptionFrench;
  }
  public String getDescriptionFrench() {
    return descriptionFrench;
  }
  public void setTermsInMonths(int[] termsInMonths) {
    this.termsInMonths = termsInMonths;
  }
  public int[] getTermsInMonths() {
    return termsInMonths;
  }


}