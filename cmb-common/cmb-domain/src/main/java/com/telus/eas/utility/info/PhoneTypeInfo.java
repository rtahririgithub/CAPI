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

public class PhoneTypeInfo implements PhoneType {

	static final long serialVersionUID = 1L;	


  protected String code="";
  protected String description="" ;
  protected String descriptionFrench = "" ;

  protected static PhoneType[] list  = { new PhoneTypeInfo("FAX", "FAX", "TÉLÉCOPIE"), new PhoneTypeInfo("PAGER", "PAGER", "PAGETTE"), new PhoneTypeInfo("MOBILE", "MOBILE", "MOBILE")};

    public PhoneTypeInfo(String code, String description, String descriptionFrench){
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

   public static  PhoneType[] getAll(){
         return list;
  }

}