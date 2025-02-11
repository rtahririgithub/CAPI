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

  public class UsageRateMethodInfo extends Info implements UsageRateMethod {

  static final long serialVersionUID = 1L;

  protected String code;
  protected String description ;
  protected String descriptionFrench;

  protected static UsageRateMethod[] list  = {
  new UsageRateMethodInfo("F", "Flat", "Tarif fixe"),
  new UsageRateMethodInfo("S", "Stepped", "Tarif échelonné"),
  new UsageRateMethodInfo("T", "Separate Tiers", "Groupe de tarifs distincts"),
  new UsageRateMethodInfo("C", "Common Tiers", "Groupe de tarifs communs"),
  new UsageRateMethodInfo("M", "Minimum Commitment Stepped", "Échelle d'engagement minimal"),

  };

  public UsageRateMethodInfo(){}

  public UsageRateMethodInfo(String code, String description, String descriptionFrench){
     this.code = code;
    this.description = description;
    this.descriptionFrench = descriptionFrench;
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

  public  static UsageRateMethod[] getAll(){
         return list;
  }

}