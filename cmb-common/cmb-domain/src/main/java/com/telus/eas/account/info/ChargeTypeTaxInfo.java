package com.telus.eas.account.info;

import com.telus.api.account.ChargeTypeTax;
import com.telus.eas.framework.info.Info;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ChargeTypeTaxInfo extends Info implements ChargeTypeTax {

   static final long serialVersionUID = 1L;

  private String chargeType;
  private double chargeAmount;
  private double gstTaxAmount;
  private double pstTaxAmount;
  private double hstTaxAmount;
  private double roamingTaxAmount;
  private double adjustedTaxAmount;

  public String getChargeType() {
    return this.chargeType;
  }

  public double getChargeAmount() {
    return this.chargeAmount;
  }

  public double getGSTTaxAmount() {
    return this.gstTaxAmount;
  }

  public double getPSTTaxAmount() {
    return this.pstTaxAmount;
  }

  public double getHSTTaxAmount() {
    return this.hstTaxAmount;
  }

  public double getRoamingTaxAmount() {
    return this.roamingTaxAmount;
  }

  public double getAdjustedTaxAmount() {
    return this.adjustedTaxAmount;
  }

  // mutators
  public void setChargeType(String chargeType) {
    this.chargeType = chargeType;
  }

  public void setChargeAmount(double chargeAmount) {
    this.chargeAmount = chargeAmount;
  }

  public void setGSTTaxAmount(double gstTaxAmount) {
    this.gstTaxAmount = gstTaxAmount;
  }

  public void setPSTTaxAmount(double pstTaxAmount) {
    this.pstTaxAmount = pstTaxAmount;
  }

  public void setHSTTaxAmount(double hstTaxAmount) {
    this.hstTaxAmount = hstTaxAmount;
  }

  public void setRoamingTaxAmount(double roamingTaxAmount) {
    this.roamingTaxAmount = roamingTaxAmount;
  }

  public void setAdjustedTaxAmount(double adjustedTaxAmount) {
    this.adjustedTaxAmount = adjustedTaxAmount;
  }
  
	public String toString() {

		StringBuffer s = new StringBuffer(128);
		s.append("InvoiceTaxInfo:[\n");
		s.append("    chargeType=[").append(chargeType).append("]\n");
		s.append("    chargeAmount=[").append(String.valueOf(chargeAmount)).append("]\n");
		s.append("    gstTaxAmount=[").append(String.valueOf(gstTaxAmount)).append("]\n");
		s.append("    pstTaxAmount=[").append(String.valueOf(pstTaxAmount)).append("]\n");		
		s.append("    hstTaxAmount=[").append(String.valueOf(hstTaxAmount)).append("]\n");
		s.append("    roamingTaxAmount=[").append(String.valueOf(roamingTaxAmount)).append("]\n");
		s.append("    adjustedTaxAmount=[").append(String.valueOf(adjustedTaxAmount)).append("]\n");
		s.append("]");
		return s.toString();
	}  
}
