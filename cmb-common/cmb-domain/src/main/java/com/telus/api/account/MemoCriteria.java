
package com.telus.api.account;

import java.util.Date;

/**
 * <CODE>MemoCriteria</CODE>
 */
public interface MemoCriteria {
  int getBanId();
  String getSubscriberId();
  String[] getSubscriberIds();
  String getSystemText();
  String getManualText();
  Date getDateFrom();
  Date getDateTo();
  String getType();
  String[] getTypes();
  /**
   * Returns max number of records to be retrieved
   *   
   */
  int getSearchLimit();

  void setBanId(int value);
  void setSubscriberId(String value);
  void setSubscriberIds(String[] value);
  void setSystemText(String value);
  void setManualText(String value);
  void setDateFrom(Date value);
  void setDateTo(Date value);
  void setType(String value);
  void setTypes(String[] value);
  
  void setSearchLimit(int value);
  void clear();
}