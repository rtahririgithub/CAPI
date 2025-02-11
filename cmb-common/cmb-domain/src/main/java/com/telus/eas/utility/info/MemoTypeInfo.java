
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package com.telus.eas.utility.info;

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;

public class MemoTypeInfo extends Info implements MemoType{
	static final long serialVersionUID = 1L;

protected String code;
  protected String description ;
  protected String descriptionFrench  ;
  protected String category;
  protected String systemText ;
  protected String systemTextFrench;
  protected int numberOfParameters ;
  protected String manualInd ;

  public String getCode() {
    return code;
  }



  public String getDescription() {
    return description;
  }


  public String getDescriptionFrench() {
    return descriptionFrench;
  }




  public String getCategory() {
    return category;
  }


  public String getSystemText() {
    return systemText;
  }


  public String getSystemTextFrench() {
    return systemTextFrench;
  }


  public int getNumberOfParameters() {
    return numberOfParameters;
  }


  public String getManualInd() {
    return manualInd;
  }



  public void setCode(String newCode) {
    code = newCode;
  }

  public void setDescription(String newDescription) {
    description = newDescription;
  }


  public void setDescriptionFrench(String newDescriptionFrench) {
    descriptionFrench = newDescriptionFrench;
  }




  public void setCategory(String newCategory) {
    category = newCategory;
  }


  public void setSystemText(String newSystemText) {
    systemText = newSystemText;
  }


  public void setSystemTextFrench(String newSystemTextFrench) {
    systemTextFrench = newSystemTextFrench;
  }


  public void setNumberOfParameters(int newNumberOfParameters) {
    numberOfParameters = newNumberOfParameters;
  }


  public void setManualInd(String newManualInd) {
    manualInd = newManualInd;
  }











}