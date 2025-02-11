/*

 * $Id$

 * %E% %W%

 * Copyright (c) Clearnet Inc. All Rights Reserved.

 */



package com.telus.api.account;



import com.telus.api.*;

import com.telus.api.equipment.*;

import java.util.*;



public interface HandsetChangeHistory {



  Date getDate();



  String getNewSerialNumber();



  String getOldSerialNumber();

  String getOldTechnologyType();
  String getNewTechnologyType();
  String getOldProductCode();
  String getNewProductCode();

  /**

   * Returns the new Equipment associated with this change.

   *

   * <P>The returned object will never be <CODE>null</CODE>.

   *

   * <P>This method may involve a remote method call.

   *

   */

  Equipment getNewEquipment() throws TelusAPIException;



  /**

   * Returns the old Equipment associated with this change.

   *

   * <P>The returned object will never be <CODE>null</CODE>.

   *

   * <P>This method may involve a remote method call.

   *

   */

  Equipment getOldEquipment() throws TelusAPIException;



}

