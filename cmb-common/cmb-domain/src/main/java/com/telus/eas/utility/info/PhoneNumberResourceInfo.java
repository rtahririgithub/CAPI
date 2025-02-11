package com.telus.eas.utility.info;

import com.telus.api.resource.PhoneNumberResource;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import com.telus.eas.framework.info.Info;

public class PhoneNumberResourceInfo extends Info implements PhoneNumberResource {

  static final long serialVersionUID = 1L;

  private String phoneNumber;
  private String productType;
  private String numberGroupCode;
  private String status;

  public PhoneNumberResourceInfo() {
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getProductType() {
    return productType;
  }

  public void setProductType(String productType) {
    this.productType = productType;
  }

  public String getNumberGroupCode() {
    return numberGroupCode;
  }

  public void setNumberGroupCode(String numberGroupCode) {
    this.numberGroupCode = numberGroupCode;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
