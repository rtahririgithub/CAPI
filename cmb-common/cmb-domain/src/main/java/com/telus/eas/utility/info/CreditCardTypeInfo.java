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

  public class CreditCardTypeInfo implements CreditCardType {

  static final long serialVersionUID = 1L;

  protected String code;
  protected String description ;
  protected String descriptionFrench;

  protected static CreditCardType[] list  = { new CreditCardTypeInfo("0", "Visa", "Visa"), new CreditCardTypeInfo("1", "Master Card", "Master Card"), new CreditCardTypeInfo("2", "American Express", "American Express")};


  public CreditCardTypeInfo(){}

  public CreditCardTypeInfo(String code, String description, String descriptionFrench){
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

  public  static CreditCardType[] getAll(){
         return list;
  }

}