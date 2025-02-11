package com.telus.api.reference;

/**
 * @author Vladimir Tsitrin
 * @version 1.0, 20-Jan-2006
 */

public interface AudienceType extends Reference {
  /**
   * Constant for audience type - Dealer.
   */
  static final String DEALER = "DEALER";

  /**
   * Constant for audience type - Retailer.
   */
  static final String RETAILER = "RETAILER";

  /**
   * Constant for audience type - Client.
   */
  static final String CLIENT = "CLIENT";

  /**
   * Constant for audience type - Agent.
   */
  static final String AGENT = "AGENT";

  /**
   * Constant for audience type - Corporate Stores.
   */
  static final String CORP_STORES = "CORPORATESTORES";
  
  /**
   * Constant for default audience type.
   */
  static final AudienceType DEFAULT = AudienceTypeDefault.getInstance();

  int getId();
}
