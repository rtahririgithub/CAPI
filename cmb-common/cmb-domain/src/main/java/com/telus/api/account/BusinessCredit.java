package com.telus.api.account;

import java.util.Date;


/**
 * <CODE>BusinessCredit</CODE> encapsulates the information used in a
 * business credit check.
 *
 */
public interface BusinessCredit {
  public String getIncorporationNumber();

  void setIncorporationNumber(String incorporationNumber);

  Date getIncorporationDate();

  void setIncorporationDate(Date incorporationDate);
}


