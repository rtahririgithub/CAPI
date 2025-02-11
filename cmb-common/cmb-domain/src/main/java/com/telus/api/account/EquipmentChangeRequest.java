/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.equipment.*;

public interface EquipmentChangeRequest {
  Equipment getNewEquipment();

  String getDealerCode();

  String getSalesRepCode();

  String getRequestorId();

  String getRepairId();

  String getSwapType();

  MuleEquipment getAssociatedMuleEquipment();

  boolean preserveDigitalServices();

  Equipment[] getSecondaryEquipments();  
  
  Equipment getAssociatedHandset();
  
}




