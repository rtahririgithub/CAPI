package com.telus.api.equipment;

/**
 * Title:        Telus Domain Project -KB61
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * 
 * @version 1.0
 */

public interface CellularDigitalEquipmentUpgrade {

  String getPromotionDescription();
  String getPromotionDescriptionFrench();
  String getPRLCode();
  String getBrowserVersion();
  String getFirmwareVersion();
  java.util.Date getStartDate();
  boolean isOtaspAvailable();
}