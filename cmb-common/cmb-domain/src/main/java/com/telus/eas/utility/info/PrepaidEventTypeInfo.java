/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.utility.info;

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;


public class PrepaidEventTypeInfo extends Info implements PrepaidEventType {

  static final long serialVersionUID = 1L;

  private String description;
  private String descriptionFrench;
  private String code;
  private boolean clientVisible;
  public static final String EVENT_ID_TOPUP_WITH_AIRTIME_CARD = "-6";
  public static final String EVENT_ID_TOPUP_WITH_CREDIT_CARD = "-7";
  public static final String EVENT_ID_TOPUP_BANKACCOUNT = "-18";
  public static final String EVENT_ID_TOPUP_OTHERS = "-19";
  public static final String EVENT_ID_TOPUP_WITH_AIRTIME_CARD_FAILED = "-26";
  public static final String EVENT_ID_TOPUP_WITH_CREDIT_CARD_FAILED = "-27";
  public static final String EVENT_ID_TOPUP_BANKACCOUNT_FAILED = "-31";
  public static final String EVENT_ID_TOPUP_OTHERS_FAILED = "-32";
  public static final String EVENT_ID_TOPUP_30DAY_IVR = "-149";
  public static final String EVENT_ID_TOPUP_30DAY = "-150";
  public static final String EVENT_ID_TOPUP_FAILED = "-300";
  public static final String EVENT_ID_TOPUP_API_CHARGE_SUCCESS = "-906";
  public static final String EVENT_ID_TOPUP_API_CHARGE_FAILED = "-907";

  public PrepaidEventTypeInfo() {
  }

  public boolean isClientVisible() {
    return clientVisible;
  }

  public String getDescription() {
    return description;
  }

  public String getDescriptionFrench() {
    return descriptionFrench;
  }

  public String getCode() {
    return code;
  }

  public void setClientVisible(boolean clientVisible) {
    this.clientVisible = clientVisible;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setDescriptionFrench(String descriptionFrench) {
    this.descriptionFrench = descriptionFrench;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public boolean isTopUpEvent() {
	  return (EVENT_ID_TOPUP_WITH_AIRTIME_CARD.equals(code) || 
			  EVENT_ID_TOPUP_WITH_CREDIT_CARD.equals(code) ||
			  EVENT_ID_TOPUP_30DAY_IVR.equals(code) ||
			  EVENT_ID_TOPUP_30DAY.equals(code) ||
			  EVENT_ID_TOPUP_FAILED.equals(code) ||
			  EVENT_ID_TOPUP_BANKACCOUNT.equals(code));

  }
  
  public boolean isBalanceAffectingTopUpEvent() {
	  return (EVENT_ID_TOPUP_WITH_AIRTIME_CARD.equals(code) || 
			  EVENT_ID_TOPUP_WITH_CREDIT_CARD.equals(code) ||
			  EVENT_ID_TOPUP_BANKACCOUNT.equals(code) ||
			  EVENT_ID_TOPUP_OTHERS.equals(code));
  }
  

    public String toString()
    {
        StringBuffer s = new StringBuffer(128);

        s.append("PrepaidEventTypeInfo:[\n");
        s.append("    clientVisible=[").append(clientVisible).append("]\n");
        s.append("    description=[").append(description).append("]\n");
        s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
        s.append("    code=[").append(code).append("]\n");
        s.append("    isTopUpEvent=[").append(isTopUpEvent()).append("]\n");
        s.append("    isBalanceAffectingTopUpEvent=[").append(isBalanceAffectingTopUpEvent()).append("]\n");
        s.append("]");

        return s.toString();
    }

}




