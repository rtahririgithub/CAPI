package com.telus.api.reference;

import java.sql.*;

 /**
   * Used to define one time period in one day for which Service or Price Plan Minutes
   * and Overage Charges are defined
 */

public interface ServicePeriodHours {

  int   getDay();
  Time  getFrom();
  Time  getTo();

  public final static int HOLIDAY   = 0;
  public final static int MONDAY    = 1;
  public final static int TUESDAY   = 2;
  public final static int WEDNESDAY = 3;
  public final static int THURSDAY  = 4;
  public final static int FRIDAY    = 5;
  public final static int SATURDAY  = 6;
  public final static int SUNDAY    = 7;

}
