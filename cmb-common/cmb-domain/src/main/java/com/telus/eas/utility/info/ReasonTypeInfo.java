package com.telus.eas.utility.info;

/**
 * Title:        Telus Domain Project -KB61
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */


import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;

public class ReasonTypeInfo extends Info implements ReasonType {

  static final long serialVersionUID = 1L;

  protected String code="";
  protected String description="" ;
  protected String descriptionFrench = "" ;
  private String featureCode;
  private String direction;
  private String processCode;
  private boolean pricePlanChangeRequired;

  public ReasonTypeInfo() {
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




  public String getCode() {
    return code;
  }


  public String getDescription() {
    return description;
  }

  public String getDescriptionFrench() {
    return descriptionFrench;
  }
  public void setFeatureCode(String featureCode) {
    this.featureCode = featureCode;
  }
  public String getFeatureCode() {
    return featureCode;
  }
  public void setDirection(String direction) {
    this.direction = direction;
  }
  public String getDirection() {
    return direction;
  }

  public String getProcessCode(){
    return processCode;
  }

  public void setProcessCode(String processCode){
    this.processCode = processCode;
  }
  public boolean isPricePlanChangeRequired() {
    return pricePlanChangeRequired;
  }
  public void setPricePlanChangeRequired(boolean pricePlanChangeRequired) {
    this.pricePlanChangeRequired = pricePlanChangeRequired;
  }
  
  public String toString()
  {
      StringBuffer s = new StringBuffer(128);
      s.append("ReasonTypeInfo:[\n");
      s.append("    code=[").append(code).append("]\n");
      s.append("    description=[").append(description).append("]\n");
      s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
      s.append("    featureCode=[").append(featureCode).append("]\n");
      s.append("    direction=[").append(direction).append("]\n");
      s.append("    processCode=[").append(processCode).append("]\n");      
      s.append("    pricePlanChangeRequired=[").append(String.valueOf(pricePlanChangeRequired)).append("]\n");           
      s.append("]");
      return s.toString();
  }  
}