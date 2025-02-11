package com.telus.api.equipment;

@Deprecated
public interface IDENEquipment extends Equipment {

  String getBrowserVersion();
  String getFirmwareVersion();
  boolean isMule();
  boolean isLegacy();
  String[] getFirmwareVersionFeatureCodes();
  String getBrowserProtocol();

}