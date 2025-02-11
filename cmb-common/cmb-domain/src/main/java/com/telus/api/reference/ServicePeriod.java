package com.telus.api.reference;

 /**
   * Used to define one period (like 'Peak', 'Weekend') for Price Plans and Services.
   * Periods are used to define included minutes and overage charges.
 */

public interface ServicePeriod extends Reference {

 public final static String DIRECTION_BOTH     = "0";
  public final static String DIRECTION_INCOMING = "1";
  public final static String DIRECTION_OUTGOING = "2";

//  ServicePeriodType               getServicePeriodType();
  ServicePeriodHours[]            getServicePeriodHours();
  ServicePeriodIncludedMinutes[]  getServicePeriodIncludedMinutes();
  ServicePeriodUsageRates[]       getServicePeriodUsageRates();
}
