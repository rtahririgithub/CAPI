package com.telus.api.resource;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public interface PhoneNumberResource {

  String STATUS_ACTIVE_AVAILABLE = "AA";
  String STATUS_ACTIVE_IN_USE = "AI";
  String STATUS_AGING = "AG";
  String STATUS_SUSPENDED = "AS";

  /**
   * Returns phone number
   * @return String
   */
  String getPhoneNumber();

  /**
   * Returns product type
   * @return String
   */
  String getProductType();

  /**
   * Returns number group code
   * @return String
   */
  String getNumberGroupCode();

  /**
   * Returns status
   * @return String
   */
  String getStatus();

}
