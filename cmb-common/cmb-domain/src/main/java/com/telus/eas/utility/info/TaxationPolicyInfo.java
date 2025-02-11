/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.utility.info;

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;


public class TaxationPolicyInfo extends Info implements TaxationPolicy {

	static final long serialVersionUID = 1L;

  private String province;
  private double gstRate;
  private double pstRate;
  private double hstRate;
  private double minimumPSTTaxableAmount;
  private char method = METHOD_TAX_ON_BASE;
  //private String description;
  //private String descriptionFrench;
  //private String code;



  public String getCode() {
    return province;
  }

  public String getDescription() {
    return province;
  }

  public String getDescriptionFrench() {
    return province;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province){
    this.province = province;
  }

  public double getGSTRate() {
    return gstRate;
  }

  public void setGSTRate(double gstRate){
    this.gstRate = gstRate;
  }

  public double getPSTRate() {
    return pstRate;
  }

  public void setPSTRate(double pstRate){
    this.pstRate = pstRate;
  }

  public double getHSTRate() {
    return hstRate;
  }

  public void setHSTRate(double hstRate){
    this.hstRate = hstRate;
  }

  public double getMinimumPSTTaxableAmount() {
    return minimumPSTTaxableAmount;
  }

  public void setMinimumPSTTaxableAmount(double minimumPSTTaxableAmount){
    this.minimumPSTTaxableAmount = minimumPSTTaxableAmount;
  }

  public char getMethod() {
    return method;
  }

  public void setMethod(char method){
    this.method = method;
  }

    public String toString()
    {
        StringBuffer s = new StringBuffer(128);

        s.append("TaxationPolicyInfo:[\n");
        s.append("    province=[").append(province).append("]\n");
        s.append("    gstRate=[").append(gstRate).append("]\n");
        s.append("    pstRate=[").append(pstRate).append("]\n");
        s.append("    hstRate=[").append(hstRate).append("]\n");
        s.append("    minimumPSTTaxableAmount=[").append(minimumPSTTaxableAmount).append("]\n");
        s.append("    method=[").append(method).append("]\n");
        s.append("]");

        return s.toString();
    }
}



