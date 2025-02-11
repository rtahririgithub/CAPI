package com.telus.api.reference;

public interface VendorService extends Reference {  
 
  public String getHistoryUnitOfMeasure();
  public int getHistoryCheckPeriod();
  public String getVendorName();
  public boolean isRestrictionRequired();
  public boolean isNotificationRequired();
  
}
