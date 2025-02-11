package com.telus.eas.utility.info;

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;


public class UsageRateInfo extends Info implements UsageRate {

	static final long serialVersionUID = 1L;

  public UsageRateInfo() {
  }
  private int fromQuantity;
  private int toQuantity;
  private String usageUnitCode = "M";
  private double rate;

  public int getFrom() {
    return fromQuantity;
  }
  public void setFrom(int fromQuantity) {
    this.fromQuantity = fromQuantity;
  }
  public void setTo(int toQuantity) {
    this.toQuantity = toQuantity;
  }
  public int getTo() {
    return toQuantity;
  }
  public void setUsageUnitCode(String usageUnitCode) {
    this.usageUnitCode = usageUnitCode;
  }
  public String getUsageUnitCode() {
    return usageUnitCode;
  }
  public void setRate(double rate) {
    this.rate = rate;
  }
  public double getRate() {
    return rate;
  }
}