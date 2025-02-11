package com.telus.api.account;

/**
 * <CODE>UsagePricePlan</CODE> includes price plan code and included minutes.
 */

public interface VoiceUsageService {

  /**
   * Returns price plan code.
   * @return String
   */
  String getServiceCode();

  /**
   * Returns usage record type code.
   *
   * @return String
   */
  String getUsageRecordTypeCode();

  /**
   * Returns included minute allocation indicator.
   *
   * @return String
   */
  String getIMAllocationIndicator();

  /**
   * Returns product type
   *
   * @return String
   */
  String getProductType();

  /**
   * Returns an array of usage price plan directions.
   * @return VoiceUsageServiceDirection[]
   */
  VoiceUsageServiceDirection[] getVoiceUsageServiceDirections();

}