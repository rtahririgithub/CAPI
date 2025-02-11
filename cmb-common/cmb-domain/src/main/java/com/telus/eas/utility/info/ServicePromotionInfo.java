package com.telus.eas.utility.info;

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;

public class ServicePromotionInfo extends Info implements ServicePromotion {

  static final long serialVersionUID = 1L;

  private String code;
  private String description ;
  private String descriptionFrench ;
  private AdjustmentReason[] reasons;

  public ServicePromotionInfo() {
  }

  public String getCode(){
    return code;
  }

  public String getDescription() {
    return description;
  }


  public String getDescriptionFrench() {
    return descriptionFrench;
  }

  public void setDescription(String newDescription) {
    description = newDescription;
  }


  public void setDescriptionFrench(String newDescriptionFrench) {
    descriptionFrench = newDescriptionFrench;
  }

  /**
   * Returns the SOC code
   * @return String
   */
  public String getServiceCode() {
    throw new java.lang.UnsupportedOperationException("method not implemented here");
  }

  public void setServiceCode(String code) {
    this.code = code;
  }

  /**
   * Returns an array of AdjustmentReason
   * @return AdjustmentReason[]
   */
  public AdjustmentReason[] getAdjustmentReasons() {
    throw new java.lang.UnsupportedOperationException("method not implemented here");
  }

  public void setAdjustmentReasons(AdjustmentReason[] reasons) {
    this.reasons = reasons;
  }
}
