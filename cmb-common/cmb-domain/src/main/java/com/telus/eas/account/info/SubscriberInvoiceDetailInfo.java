package com.telus.eas.account.info;

import com.telus.api.account.SubscriberInvoiceDetail;
import com.telus.eas.framework.info.*;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class SubscriberInvoiceDetailInfo extends Info implements SubscriberInvoiceDetail {

   static final long serialVersionUID = 1L;

  private String productType;
  private String subscriberId;
  private String pricePlan;
  private String billedName;
  private double currentCharges;
  private double currentCredits;
  private double gst;
  private double pst;
  private double hst;
  private double roamingTax;
  private double totalCharge;

  public String getProductType() {
    return productType;
  }

  public String getSubscriberId() {
    return subscriberId;
  }

  public String getPricePlan() {
    return pricePlan;
  }

  public String getBilledName() {
    return billedName;
  }

  public double getTotalCharge() {
    return totalCharge;
  }

  public void setProductType(String productType) {
    this.productType = productType;
  }

  public void setSubscriberId(String subscriberId) {
    this.subscriberId = subscriberId;
  }

  public void setPricePlan(String pricePlan) {
    this.pricePlan = pricePlan;
  }

  public void setBilledName(String billedName) {
    this.billedName = billedName;
  }

  public void setTotalCharge(double totalCharge) {
    this.totalCharge = totalCharge;
  }

  /**
   * Returns current charge amount
   * @return double
   */
  public double getCurrentCharges() {
    return currentCharges;
  }

  public void setCurrentCharges(double currentCharges) {
    this.currentCharges = currentCharges;
  }

  /**
   * Returns current credit amount
   * @return double
   */
  public double getCurrentCredits() {
    return currentCredits;
  }

  public void setCurrentCredits(double currentCredits) {
    this.currentCredits = currentCredits;
  }

  /**
   * Returns GST tax
   * @return double
   */
  public double getGSTTaxAmount() {
    return gst;
  }

  public void setGSTTaxAmount(double gst) {
    this.gst = gst;
  }

  /**
   * Returns PST tax
   * @return double
   */
  public double getPSTTaxAmount() {
    return pst;
  }

  public void setPSTTaxAmount(double pst) {
    this.pst = pst;
  }

  /**
   * Returns HST tax
   * @return double
   */
  public double getHSTTaxAmount() {
    return hst;
  }

  public void setHSTTaxAmount(double hst) {
    this.hst = hst;
  }

  /**
   * Returns roaming tax
   * @return double
   */
  public double getRoamingTaxAmount() {
    return roamingTax;
  }

  public void setRoamingTaxAmount(double roamingTax) {
    this.roamingTax = roamingTax;
  }

  public String toString()
  {
      StringBuffer s = new StringBuffer(128);

      s.append("SubscriberInvoiceDetailInfo:[\n");
      s.append("    productType=[").append(productType).append("]\n");
      s.append("    subscriberId=[").append(subscriberId).append("]\n");
      s.append("    pricePlan=[").append(pricePlan).append("]\n");
      s.append("    billedName=[").append(billedName).append("]\n");
      s.append("    currentCharges=[").append(currentCharges).append("]\n");
      s.append("    currentCredits=[").append(currentCredits).append("]\n");
      s.append("    gst=[").append(gst).append("]\n");
      s.append("    pst=[").append(pst).append("]\n");
      s.append("    hst=[").append(hst).append("]\n");
      s.append("    roamingTax=[").append(roamingTax).append("]\n");
      s.append("    totalCharge=[").append(totalCharge).append("]\n");
      s.append("]");

      return s.toString();
  }  
}
