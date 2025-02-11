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

public class LanguageInfo extends ReferenceInfo implements Language{

	static final long serialVersionUID = 1L;


 

  protected static Language[] list   = { new LanguageInfo("EN", "English", "Anglais"), new LanguageInfo("FR", "French", "Français")};


  public LanguageInfo(){}

  public LanguageInfo(String code, String description, String descriptionFrench){
     this.code = code;
	 this.description = description;
	 this.descriptionFrench = descriptionFrench;

  }

 

  public  static Language[] getAll(){
         return list;
  }
}