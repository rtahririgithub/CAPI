package com.telus.api.contactevent;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

/**
 * Encapsulates specific SMS notification attributes
 */
public interface SMSNotification extends Notification {

  /**
   * Phone Number of the device capable of receiving SMS messages
   */
  String getPhoneNumber();
  void setPhoneNumber(String phoneNumber);

  /**
   * ESN assigned to the equipment of the subscriber capable of receiving SMS messages
   */
  String getEquipmentSerialNumber();
  void setEquipmentSerialNumber(String equipmentSerialNumber);
  
  /**
   * Telus Team Member ID of the user sending the SMS messages
   */
  int getTeamMemberId();
  void setTeamMemberId(int teamMemberId);
}
