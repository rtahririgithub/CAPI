package com.telus.eas.utility.info;

import com.telus.api.reference.*;


public class RuralTypeInfo implements RuralType {


 static final long serialVersionUID = 1L;	 

  protected String code;
  protected String shortDescription ;
  protected String shortDescriptionFrench;
  protected String description ;
  protected String descriptionFrench;

  protected static RuralTypeInfo[] list   =  {
      new RuralTypeInfo("R", "RR", "RR", "Rural Route", "Route rurale"),
      new RuralTypeInfo("P", "PO BOX", "Case postale", "PO BOX", "Case postale"),
      new RuralTypeInfo("G", "GD", "PR", "General Delivery", "Poste restante"),
      new RuralTypeInfo("M", "MR", "IM", "Mobile Route", "Itinéraire motorisé"),
      new RuralTypeInfo("S", "SS", "SS", "Sub Service", "Service suburbain")

  };


  public RuralTypeInfo() {
  }

  public RuralTypeInfo(String code, String shortDescription, String shortDescriptionFrench, String description, String descriptionFrench) {
    this.code = code;
    this.shortDescription = shortDescription;
    this.shortDescriptionFrench = shortDescriptionFrench;
    this.description = description;
    this.descriptionFrench = descriptionFrench;

  }

  public String getCode() {
    return code;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public String getShortDescriptionFrench() {
    return shortDescriptionFrench;
  }



  public String getDescription() {
    return description;
  }


  public String getDescriptionFrench() {
    return descriptionFrench;
  }

  public  static RuralTypeInfo[] getAll() {
    return list;
  }

}

