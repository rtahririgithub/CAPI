package com.telus.eas.contactevent.info;

import com.telus.api.contactevent.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class SMSNotificationInfo extends NotificationInfo implements SMSNotification {

 static final long serialVersionUID = 1L;

  public static final String SMS_STATUS_CD_INITIAL = "INITIAL";
  public static final int SMS_GATEWAY_MESSAGE_ID_UNKNOWN = 0;

  private String phoneNumber;
  private String equipmentSerialNumber;
  private int teamMemberId;

  public SMSNotificationInfo() {
  }

  public String getEquipmentSerialNumber() {
    return equipmentSerialNumber;
  }

  public void setEquipmentSerialNumber(String equipmentSerialNumber) {
    this.equipmentSerialNumber = equipmentSerialNumber;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
  
  public int getTeamMemberId() {
	  return teamMemberId;
  }
  
  public void setTeamMemberId(int teamMemberId) {
	  this.teamMemberId = teamMemberId;
  }

  public String toString() {
  StringBuffer string = new StringBuffer();

  // ancestor
  string.append( super.toString() );
  // this
  string.append("\n SMS:");
  string.append("\t   phoneNumber=" + phoneNumber);
  string.append("\t   equipmentSerialNumber=" + equipmentSerialNumber);
  string.append("\t   teamMemberId=" + teamMemberId);

  return string.toString();
}

}
