package com.telus.eas.utility.info;

/**
 * Title:        Telus Domain Project -KB61
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;

public class EquipmentProductTypeInfo extends Info implements EquipmentProductType{

  static final long serialVersionUID = 1L;

  private String code;
  private String description;
  private String descriptionFrench;
  private String productCode;
  private long productTypeID;
  private long productGroupTypeID;
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
  public void setProductCode(String productCode) {
    this.productCode = productCode;
  }
  public String getProductCode() {
    return productCode;
  }
  public void setProductTypeID(long productTypeID) {
    this.productTypeID = productTypeID;
  }
  public long getProductTypeID() {
    return productTypeID;
  }
  public void setProductGroupTypeID(long productGroupTypeID) {
    this.productGroupTypeID = productGroupTypeID;
  }
  public long getProductGroupTypeID() {
    return productGroupTypeID;
  }


}