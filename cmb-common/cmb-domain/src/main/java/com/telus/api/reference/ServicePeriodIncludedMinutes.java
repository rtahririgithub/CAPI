package com.telus.api.reference;

 /**
   * Used to define included minutes for one Price Plan in one Service Period
 */
public interface ServicePeriodIncludedMinutes {
  String getDirection();
  int getIncludedMinutes();
}