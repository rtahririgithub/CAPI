package com.telus.eas.account.info;

import com.telus.api.account.AddressHistory;
import com.telus.eas.framework.info.*;
import java.util.Date;
import com.telus.api.account.Address;

public class AddressHistoryInfo extends Info implements AddressHistory {

  static final long serialVersionUID = 1L;

  private Date effectiveDate;
  private Date expirationDate;
  private AddressInfo address;

  public AddressHistoryInfo() {
  }
  public java.util.Date getEffectiveDate() {
    return effectiveDate;
  }
  public void setEffectiveDate(java.util.Date effectiveDate) {
    this.effectiveDate = effectiveDate;
  }
  public java.util.Date getExpirationDate() {
    return expirationDate;
  }
  public void setExpirationDate(java.util.Date expirationDate) {
    this.expirationDate = expirationDate;
  }
  public Address getAddress() {
    return address;
  }
  public AddressInfo getAddress0() {
    return address;
  }
  public void setAddressInfo(AddressInfo address) {
    this.address = address;
  }

  public String toString()
  {
      StringBuffer s = new StringBuffer(128);

      s.append("AddressHistoryInfo:[\n");
      s.append("    effectiveDate=[").append(effectiveDate).append("]\n");
      s.append("    expirationDate=[").append(expirationDate).append("]\n");
      s.append("    address=[").append(address).append("]\n");
      s.append("]");

      return s.toString();
  }
}