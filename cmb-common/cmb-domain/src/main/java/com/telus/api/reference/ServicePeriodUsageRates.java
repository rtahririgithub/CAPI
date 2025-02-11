package com.telus.api.reference;

 /**
   * Used to define usage rates for one Price Plan in one Service Period
 */

public interface ServicePeriodUsageRates {

 String getDirection();
 String getMethod();
 int getUsageRatingFrequency();
 UsageRate[] getUsageRates();
}