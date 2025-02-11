package com.telus.api.reference;

/**
 * An <tt>ApplicationSummary</tt> contains information about specific TELUS application as well as
 * application-related constants.
 *
 * @author  Vladimir Tsitrin
 * @version 1.0, 08/19/05
 * @since 2.786.0.0
 */

public interface ApplicationSummary extends Reference {
  /**
   * Constant for application name - Online Offer Manager.
   */
  static final String APP_OOM = "OFFERM";

  /**
   * Constant for application name - Corporate Store Offer Manager.
   */
  static final String APP_CSOM = "CCR";

  /**
   * Constant for application name - Smart Desktop.
   */
  static final String APP_SD = "SMARTDESKTOP";

  /**
   * Constant for application name - Swap Track.
   */
  static final String APP_SWPTRK = "SWPTRK";

  /**
   * Constant for application name - IVR.
   */
  static final String APP_IVR = "IVR_IMB";

  /**
   * Constant for application name - SelfServe.
   */
  static final String APP_SSERVE = "SSERVE";

  /**
   * Constant for application name - Corporate Store SelfServe.
   */
  static final String APP_CSSSRV = "CSSSRV";

  /**
   * Constant for application name - TOWIN Batch.
   */
  static final String APP_TOWIN_BATCH = "TOWIN_APP";

  /**
   * Constant for application name - Port Request Management.
   */
  static final String APP_PRM = "PRM";

  /**
   * Constant for application name - CMRS.
   */
  static final String APP_CMRS = "CMRS";

  /**
   * Constant for application name - AOM Exchange.
   */
  static final String APP_EXCHANGE = "EXCHANGE";
  
  /**
   * Constant for application name - AOM Exchange.
   */
  static final String APP_APOLLO = "APOLLO";

  /**
   * Constant for default application.
   */
  static final ApplicationSummary DEFAULT = ApplicationSummaryDefault.getInstance();

  int getId();

  boolean isBatch();
  
  boolean isNewAPIallowedToUse(); 
}
