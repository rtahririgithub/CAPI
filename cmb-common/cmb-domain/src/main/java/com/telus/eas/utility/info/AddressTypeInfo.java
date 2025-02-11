package com.telus.eas.utility.info;

import com.telus.api.reference.*;


public class AddressTypeInfo implements Language, AddressType {


  static final long serialVersionUID = 1L;


  protected String code;
  protected String description ;
  protected String descriptionFrench;

  protected static AddressTypeInfo[] list   =  {
    new AddressTypeInfo("C", "City", "Ville"),
    new AddressTypeInfo("D", "Rural", "Rurale"),
    new AddressTypeInfo("F", "Foreign", "Étrangère")
  };


  public AddressTypeInfo() {
  }

  public AddressTypeInfo(String code, String description, String descriptionFrench) {
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

  public  static Language[] getAll() {
    return list;
  }
}

