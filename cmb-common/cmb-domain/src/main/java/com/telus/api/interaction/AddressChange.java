/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.interaction;

public interface AddressChange extends InteractionDetail {

  String getOldAddressLine();
  String getOldCity();
  String getOldProvince();
  String getOldCountry();
  String getOldPostal();

  String getNewAddressLine();
  String getNewCity();
  String getNewProvince();
  String getNewCountry();
  String getNewPostal();

}


