/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.reference;

import java.sql.Date;


public interface ServiceRelation {

  public static final String TYPE_PROMOTION          = "F";
  public static final String TYPE_FEATURE_CARD       = "C";
  public static final String TYPE_BOUND              = "M";
  public static final String TYPE_SEQUENTIALLY_BOUND = "S";

  String getType();

  ServiceSummary getService();

  boolean isOptional();
  
  Date getExpirationDate();
}




