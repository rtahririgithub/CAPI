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


  public class CountryInfo implements Country{

  static final long serialVersionUID = 1L;

  protected String code;
  protected String description ;
  protected String descriptionFrench;

  protected static Country[] list  = { new CountryInfo("CAN", "CANADA", "CANADA"), new CountryInfo("USA", "UNITED STATES", "ÉTATS-UNIS")};


  public CountryInfo(){}

  public CountryInfo(String code, String description, String descriptionFrench){
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

  public  static Country[] getAll(){
         return list;
  }
}