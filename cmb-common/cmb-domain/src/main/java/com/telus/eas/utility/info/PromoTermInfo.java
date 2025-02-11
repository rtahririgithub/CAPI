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

public class PromoTermInfo extends Info implements PromoTerm{

  static final long serialVersionUID = 1L;

  public PromoTermInfo() {
  }
  private int termMonth;
  private boolean availableForChange;
  private boolean availableForActivation;
  private String regularServiceCode;

  public void setTermMonth(int termMonth) {
    this.termMonth = termMonth;
  }
  public int getTermMonth() {
    return termMonth;
  }
  public void setAvailableForChange(boolean availableForChange) {
    this.availableForChange = availableForChange;
  }
  public boolean isAvailableForChange() {
    return availableForChange;
  }
  public void setAvailableForActivation(boolean availableForActivation) {
    this.availableForActivation = availableForActivation;
  }
  public boolean isAvailableForActivation() {
    return availableForActivation;
  }
  public void setRegularServiceCode(String regularServiceCode) {
    this.regularServiceCode = regularServiceCode;
  }
  public String getRegularServiceCode() {
    return regularServiceCode;
  }
}