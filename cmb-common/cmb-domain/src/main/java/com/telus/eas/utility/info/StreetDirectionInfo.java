package com.telus.eas.utility.info;

import com.telus.api.reference.*;


public class StreetDirectionInfo implements Language, StreetDirection {

	static final long serialVersionUID = 1L;


  protected String code;
  protected String description ;
  protected String descriptionFrench;

  protected static StreetDirectionInfo[] list   =  {
      new StreetDirectionInfo("N", "North", "Nord"),
      new StreetDirectionInfo("S", "South", "Sud"),
      new StreetDirectionInfo("E", "East", "Est"),
      new StreetDirectionInfo("W", "West", "Ouest"),
      new StreetDirectionInfo("NE", "North-east", "Nord-est"),
      new StreetDirectionInfo("NW", "North-west", "Nord-ouest"),
      new StreetDirectionInfo("SE", "South-east", "Sud-est"),
      new StreetDirectionInfo("SW", "South-west", "Sud-ouest")
  };


  public StreetDirectionInfo() {
  }

  public StreetDirectionInfo(String code, String description, String descriptionFrench) {
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

