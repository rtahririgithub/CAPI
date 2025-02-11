/**
 * Title:        DiscountInfo<p>
 * Description:  The DiscountInfo holds all attributes for a discount.<p>
 * Copyright:    Copyright (c) Peter Frei<p>
 * Company:      Telus Mobility Inc<p>
 * @author Peter Frei
 * @version 1.0
 */

package com.telus.eas.framework.info;

import com.telus.api.*;
import com.telus.api.account.*;
import com.telus.api.reference.*;
//import com.telus.eas.framework.info.*;
import java.util.*;


public class DiscountInfo extends Info implements Discount {

  static final long serialVersionUID = 1L; 

  public static final char INSERT_DISCOUNT = 'I';
  public static final char UPDATE_DISCOUNT = 'U';
  public static final char DELETE_DISCOUNT = 'D';

  private int ban;
  private String subscriberId;
  private String productType;
  private String discountCode;
  private Date effectiveDate;
  private Date expiryDate;
  private int discountSequenceNo;
  private String knowbilityOperatorID;
  private String discountByUserId;
  
  private boolean forFutureSubscriberActivation = false;

  public DiscountInfo() {
  }

public String getDiscountByUserId() {
	return discountByUserId;
}
public void setDiscountByUserId(String discountByUserId) {
	this.discountByUserId = discountByUserId;
}
public String getKnowbilityOperatorID() {
	return knowbilityOperatorID;
}
public void setKnowbilityOperatorID(String knowbilityOperatorID) {
	this.knowbilityOperatorID = knowbilityOperatorID;
}
  public int getBan() {
    return ban;
  }
  public void setBan(int ban) {
    this.ban = ban;
  }
  public String getSubscriberId() {
    return subscriberId;
  }
  public void setSubscriberId(String newSubscriberId) {
    subscriberId = newSubscriberId;
  }
  public String getProductType() {
    return productType;
  }
  public void setProductType(String newProductType) {
    productType = newProductType;
  }

  public boolean isSubscriberLevel() {
    if (subscriberId == null || productType == null)  return false;
    if (subscriberId.trim().equals("") || productType.trim().equals("")) return false;
    return true;
  }

  public String getDiscountCode() {
    return discountCode;
  }
  public void setDiscountCode(String discountCode) {
    this.discountCode = discountCode;
  }
  public Date getEffectiveDate() {
    return effectiveDate;
  }
  public void setEffectiveDate(Date effectiveDate) {
    this.effectiveDate = effectiveDate;
  }
  public Date getExpiryDate() {
    return expiryDate;
  }
  public void setExpiryDate(Date expiryDate) {
    this.expiryDate = expiryDate;
  }
  public int getDiscountSequenceNo() {
    return discountSequenceNo;
  }
  public void setDiscountSequenceNo(int discountSequenceNo) {
    this.discountSequenceNo = discountSequenceNo;
  }

  
  public boolean isForFutureSubscriberActivation() {
	return forFutureSubscriberActivation;
}

public void setForFutureSubscriberActivation(
		boolean forFutureSubscriberActivation) {
	this.forFutureSubscriberActivation = forFutureSubscriberActivation;
}

public void apply() throws TelusAPIException{
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public DiscountPlan getDiscountPlan() throws TelusAPIException{
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public void expire() throws TelusAPIException{
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("DiscountInfo:{\n");
    s.append("    ban=[").append(ban).append("]\n");
    s.append("    subscriberId=[").append(subscriberId).append("]\n");
    s.append("    productType=[").append(productType).append("]\n");
    s.append("    discountCode=[").append(discountCode).append("]\n");
    s.append("    effectiveDate=[").append(effectiveDate).append("]\n");
    s.append("    expiryDate=[").append(expiryDate).append("]\n");
    s.append("    discountSequenceNo=[").append(discountSequenceNo).append("]\n");
    s.append("    forFutureSubscriberActivation=[").append(forFutureSubscriberActivation).append("]\n");
    s.append("}");

    return s.toString();
  }

}

