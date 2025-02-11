package com.telus.eas.utility.info;


import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;


public class ServicePeriodUsageRatesInfo  extends Info implements ServicePeriodUsageRates {

  static final long serialVersionUID = 1L;

  public ServicePeriodUsageRatesInfo() {
  }
  private String direction;
  private String method;
  private int usageRatingFrequency;
  private UsageRateInfo[] usageRates = new UsageRateInfo[0];

  public String getDirection() {
    return direction;
  }
  public void setDirection(String direction) {
    this.direction = direction;
  }
  public void setMethod(String method) {
    this.method = method;
  }
  public String getMethod() {
    return method;
  }
  public void setUsageRatingFrequency(int usageRatingFrequency) {
    this.usageRatingFrequency = usageRatingFrequency;
  }
  public int getUsageRatingFrequency() {
    return usageRatingFrequency;
  }
  public void setUsageRates(UsageRateInfo[] usageRates) {
    this.usageRates = usageRates;
  }
  public UsageRate[] getUsageRates() {
    return usageRates;
  }
}