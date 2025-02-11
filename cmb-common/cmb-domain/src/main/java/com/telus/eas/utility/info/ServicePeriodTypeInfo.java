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

  public class ServicePeriodTypeInfo extends Info implements ServicePeriodType {

  static final long serialVersionUID = 1L;

  protected String code;
  protected String description ;
  protected String descriptionFrench;

  protected static ServicePeriodType[] list  = {
      new ServicePeriodTypeInfo("01", "Peak", "Heures de Pointe"),
      new ServicePeriodTypeInfo("02", "Weekend", "Week-end"),
      new ServicePeriodTypeInfo("03", "Evening", "Soir"),
      new ServicePeriodTypeInfo("04", "Period 4", "Period 4"),
      new ServicePeriodTypeInfo("05", "Period 5", "Period 5"),
      new ServicePeriodTypeInfo("06", "Period 6", "Period 6"),
      new ServicePeriodTypeInfo("C", "Combined", "minutes combinées"),
      new ServicePeriodTypeInfo("13", "Peak/Weekend",
                                "Heures de Pointe/Week-end"),
      new ServicePeriodTypeInfo("15", "Peak/Evening", "Heures de Pointe/Soir"),
      new ServicePeriodTypeInfo("16", "Evening/Weekend", "Soir/Week-end"),
      new ServicePeriodTypeInfo("19", "Peak-Other", "Heures de Pointe-Autre"),
      new ServicePeriodTypeInfo("35", "Peak-Other", "Heures de Pointe-Autre")
  };


  public ServicePeriodTypeInfo(){}

  public ServicePeriodTypeInfo(String code, String description, String descriptionFrench){
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

  public  static ServicePeriodType[] getAll(){
         return list;
  }

}
