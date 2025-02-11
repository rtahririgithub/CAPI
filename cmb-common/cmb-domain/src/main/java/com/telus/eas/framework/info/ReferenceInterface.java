/**
 * Title:        Reference<p>
 * Description:  The Reference interface is used for all reference data info classes.<p>
 * Copyright:    Copyright (c) Peter Frei<p>
 * Company:      Telus Mobility Inc<p>
 * @author Peter Frei
 * @version 1.0
 */
package com.telus.eas.framework.info;

import java.util.Locale;
/**
 * <CODE>ReferenceInterface</CODE> marks any class that whishes to be used by
 * ReferenceDataHelper .
 *
 */
public interface ReferenceInterface {
  static final long serialVersionUID = 1L;

  /**
   * Returns the string representation for this object.  The string must be
   * fit for display to end users.
   */
  public String toString();

  /**
   * Returns the unique integer identifier for this object (if available).
   */
  public int getID();

  /**
   * Returns the unique string identifier for this object (if available).
   */
  public String getCode();

  /**
   * Returns the string representation for this object for the default locale.
   */
  public String getValue();

  /**
   * Returns the string representation for this object.  The string must be
   * fit for display to end users.
   */
  public String getValue(Locale locale);

  /**
   * Returns the string representation for this object.  The string must be
   * fit for display to end users.
   */
  public String getExternalValue(Locale locale);

  /**
   * Returns the string representation for this object for the default locale.
   */
  public String getExternalValue();

}
