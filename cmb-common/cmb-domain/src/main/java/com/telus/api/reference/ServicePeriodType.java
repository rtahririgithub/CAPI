package com.telus.api.reference;

 /**
   * Defines period types for Price Plans and Services.
   * <P>Periods are used to define included minutes and overage charges.
 */

public interface ServicePeriodType extends Reference {

  public final static String PEAK             = "01";
  public final static String WEEKEND          = "02";
  public final static String EVENING          = "03";
  public final static String PERIOD           = "P";
  public final static String PERIOD_4         = "04";
  public final static String PERIOD_5         = "05";
  public final static String PERIOD_6         = "06";
  public final static String COMBINED         = "C";
  public final static String PEAK_WEEKEND     = "13";
  public final static String PEAK_EVENING     = "15";
  public final static String EVENING_WEEKEND  = "16";
  public final static String PEAK_OTHER_01 = "19";
  public final static String PEAK_OTHER_02 = "35";

}
