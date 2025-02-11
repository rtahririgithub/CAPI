package com.telus.api.dealer;

import com.telus.api.*;

public interface CPMSDealer{

/**
 * <CODE>CPMSDealer</CODE>       
 *
 */

  void validUser(String password) throws InvalidPasswordException, TelusAPIException;
  void resetUserPassword(String newPassword) throws InvalidPasswordException, TelusAPIException;
  void changeUserPassword(String oldPassword, String newPassword) throws InvalidPasswordException, TelusAPIException;
  String getProvinceCode();
  String getChannelCode();
  String getChannelDesc();
  String getUserCode();
  String getUserDesc();
  String getChannelOrgTypeCode();
  String getPhone(); 
  String[] getAddress();
  HoursOfOperation[] getHoursOfOperation();
  boolean isHighPriority();
  int[] getBrandIds();

}
