
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package com.telus.api.reference;

public interface InvoiceSuppressionLevel extends Reference {
  static final String PRINT_ALL = "1";
  static final String GROUP_DISPATCH = "2";
  static final String SUPPRESS_ALL = "9";

  boolean isAvailableForUpdate();
}
