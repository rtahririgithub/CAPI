/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.*;
import com.telus.eas.framework.info.*;
import com.telus.eas.utility.info.TaxationPolicyInfo;


public class TaxSummaryInfo extends Info implements TaxSummary {

	static final long serialVersionUID = 1L;

  private double pstAmount;
  private double gstAmount;
  private double hstAmount;
  private String province;
  private TaxationPolicyInfo taxationPolicy;

  private static final String REASON_CODE_GST = "211";
  private static final String REASON_CODE_PST = "212";
  private static final String REASON_CODE_QST = "213";
  private static final String REASON_CODE_HST = "214";
  
  private static final String PREPAID_REASON_CODE_GST = "102";
  private static final String PREPAID_REASON_CODE_PST = "103";
  private static final String PREPAID_REASON_CODE_HST = "104";

  public void copyFrom(TaxSummary o) {
    this.pstAmount = o.getPSTAmount();
    this.gstAmount = o.getGSTAmount();
    this.hstAmount = o.getHSTAmount();
  }

  public double getPSTAmount() {
    return pstAmount;
  }

  public double getGSTAmount() {
    return gstAmount;
  }

  public double getHSTAmount() {
    return hstAmount;
  }

  public void setPSTAmount(double pstAmount){
    this.pstAmount = pstAmount;
  }

  public void setGSTAmount(double gstAmount){
    this.gstAmount = gstAmount;
  }

  public void setHSTAmount(double hstAmount){
    this.hstAmount = hstAmount;
  }

  public double getTotal(){
    return pstAmount + gstAmount + hstAmount;
  }

  public String getProvince(){
    return province;
  }

  public void setProvince(String province){
    this.province = province;
  }

  public boolean isQuebec(){
    return province != null && province.equals("PQ");
  }

  public String getGSTAdjustmentReasonCode(){
    return REASON_CODE_GST;
  }

  public String getPSTAdjustmentReasonCode(){
    return isQuebec() ? REASON_CODE_QST : REASON_CODE_PST;
  }

  public String getHSTAdjustmentReasonCode(){
    return REASON_CODE_HST;
  }
  
  public String getPrepaidGSTAdjustmentReasonCode(){
    return PREPAID_REASON_CODE_GST;
  }

  public String getPrepaidPSTAdjustmentReasonCode(){
    return PREPAID_REASON_CODE_PST;
  }

  public String getPrepaidHSTAdjustmentReasonCode(){
    return PREPAID_REASON_CODE_HST;
  }

    public String toString()
    {
        StringBuffer s = new StringBuffer(128);

        s.append("TaxSummaryInfo:[\n");
        s.append("    pstAmount=[").append(pstAmount).append("]\n");
        s.append("    gstAmount=[").append(gstAmount).append("]\n");
        s.append("    hstAmount=[").append(hstAmount).append("]\n");
        s.append("    province=[").append(province).append("]\n");
        s.append("    REASON_CODE_GST=[").append(REASON_CODE_GST).append("]\n");
        s.append("    REASON_CODE_PST=[").append(REASON_CODE_PST).append("]\n");
        s.append("    REASON_CODE_QST=[").append(REASON_CODE_QST).append("]\n");
        s.append("    REASON_CODE_HST=[").append(REASON_CODE_HST).append("]\n");
        s.append("    PREPAID_REASON_CODE_GST=[").append(PREPAID_REASON_CODE_GST).append("]\n");
        s.append("    PREPAID_REASON_CODE_PST=[").append(PREPAID_REASON_CODE_PST).append("]\n");
        s.append("    PREPAID_REASON_CODE_HST=[").append(PREPAID_REASON_CODE_HST).append("]\n");
        s.append("]");

        return s.toString();
    }

	public TaxationPolicyInfo getTaxationPolicy() {
		return taxationPolicy;
	}

	public void setTaxationPolicy(TaxationPolicyInfo taxationPolicy) {
		this.taxationPolicy = taxationPolicy;
	}


}



