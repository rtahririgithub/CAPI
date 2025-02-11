/**
 * Title:        MemoInfo<p>
 * Description:  The MemoInfo holds all attributes for a memo.<p>
 * Copyright:    Copyright (c) Peter Frei<p>
 * Company:      Telus Mobility Inc<p>
 * @author Peter Frei
 * @version 1.0
 */

package com.telus.api.account;

import java.util.Date;

import com.telus.api.TelusAPIException;


public interface Memo {

public static final String MEMO_TYPE_SPECIAL_INSTRUCTIONS  = "3000";

  int getBanId();

  //void setBanId(int banId);

  String getMemoType();

  void setMemoType(String newMemoType);

  String getSubscriberId();

  //void setPhoneNumber(String newPhoneNumber);

  String getProductType();

  void setProductType(String newProductType);

  String getText();

  void setText(String newText);


  String getSystemText();

  Date getDate();

  void setDate(Date newDate);

  Date getModifyDate() ;

  int getOperatorId();
  
  double getMemoId();

  /**
   * Saves a new Memo with the contained information.
   *
   * <P>This method may involve a remote method call.
   *
   */
  void create() throws TelusAPIException;
  
  /**
   * Saves a new Memo with the contained information asynchronously .
   *
   * <P>This method may involve a remote method call.
   *
   */
  void create( boolean async ) throws TelusAPIException;

  void save() throws TelusAPIException;

}

