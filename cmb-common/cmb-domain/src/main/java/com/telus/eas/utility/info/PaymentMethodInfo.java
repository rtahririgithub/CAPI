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

public class PaymentMethodInfo implements PaymentMethod{

  static final long serialVersionUID = 1L;

  protected String code;
  protected String description ;
  protected String descriptionFrench;

  protected static PaymentMethod[] list  = { new PaymentMethodInfo(com.telus.api.account.PaymentMethod.PAYMENT_METHOD_REGULAR, "Standard", "Facturation sur le relevé"), new PaymentMethodInfo(com.telus.api.account.PaymentMethod.PAYMENT_METHOD_PRE_AUTHORIZED_CREDITCARD, "Pre-Authorized Credit Card", "Carte de crédit enregistrée")};


  public PaymentMethodInfo(){}

  public PaymentMethodInfo(String code, String description, String descriptionFrench){
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

  public static  PaymentMethod[] getAll(){
         return list;
  }
}