package com.telus.eas.utility.info;

import com.telus.api.reference.*;


public class RuralDeliveryTypeInfo implements Language, RuralDeliveryType {

  static final long serialVersionUID = 1L;

  protected String code;
  protected String description ;
  protected String descriptionFrench;

  protected static RuralDeliveryTypeInfo[] list = {
    new RuralDeliveryTypeInfo("CDO", "Commercial Dealership Outlet", "Concession commerciale"),
    new RuralDeliveryTypeInfo("CMC", "Community Mail Center", "Centre postal communautaire"),
    new RuralDeliveryTypeInfo("LCD", "Letter Carrier Depot", "Poste de facteurs"),
    new RuralDeliveryTypeInfo("PO", "Post Office", "Bureau de poste"),
    new RuralDeliveryTypeInfo("RPO", "Postal Outlet", "Comptoir postal"),
    new RuralDeliveryTypeInfo("STN", "Station", "Succursale")
  };


  public RuralDeliveryTypeInfo() {
  }

  public RuralDeliveryTypeInfo(String code, String description, String descriptionFrench) {
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

  public  static RuralDeliveryTypeInfo[] getAll() {
    return list;
  }

}

