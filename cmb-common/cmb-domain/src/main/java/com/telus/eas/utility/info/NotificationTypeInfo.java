package com.telus.eas.utility.info;

import com.telus.eas.framework.info.Info;
import com.telus.api.reference.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class NotificationTypeInfo extends Info implements NotificationType {

	static final long serialVersionUID = 1L;

  private String code;
  private String description ;
  private String descriptionFrench ;
  private String originatingUser;
  private String deliveryReceiptRequired;
  private String billable;

  public String getCode(){
    return code;
  }

  public String getDescription() {
    return description;
  }

  public String getDescriptionFrench() {
    return descriptionFrench;
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

  public String getOriginatingUser() {
    return originatingUser;
  }

  public void setOriginatingUser(String originatingUser) {
    this.originatingUser = originatingUser;
  }

  public String getDeliveryReceiptRequired() {
    return deliveryReceiptRequired;
  }

  public void setDeliveryReceiptRequired(String deliveryReceiptRequired) {
    this.deliveryReceiptRequired = deliveryReceiptRequired;
  }

  public String getBillable() {
    return billable;
  }

  public void setBillable(String billable) {
    this.billable = billable;
  }
}
