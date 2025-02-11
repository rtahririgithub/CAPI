package com.telus.api.reference;

 /**
   * Defines usage rate methods
 */
public interface UsageRateMethod  extends Reference {

public final static String FLAT = "F";
public final static String STEPPED = "S";
public final static String SEPARATE_TIERS = "T";
public final static String COMMON_TIERS = "C";
public final static String MINIMUM_COMMITMENT_STEPPED = "M";
}