
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package com.telus.api.reference;

public interface MemoType extends Reference {

  String getCategory();
  String getSystemText();
  String getSystemTextFrench();
  int getNumberOfParameters();
  String getManualInd();
}