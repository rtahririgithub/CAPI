
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package com.telus.api.reference;

import java.util.Date;
import com.telus.api.*;


public interface Dealer extends Reference {


  String   getName();
  String   getDepartmentCode();
  String   getNumberLocationCD();
  Date     getEffectiveDate();
  Date     getExpiryDate();
  void     setName(String newName);
  void     setCode(String newCode);
  void     setDescription(String newDescription);
  void     setDescriptionFrench(String newDescription);
  void     setDepartmentCode(String newDepartmentCode);
  void     setNumberLocationCD(String newNumberLocationCD);
  void     setEffectiveDate(Date newEffectiveDate);
  void     setExpiryDate(Date newExpiryDate);

  /**
  * Find a sales rep for the given dealer using the sales rep code.
  * @param salesCode - the Knowbility sales rep code
  * @return the sales rep instance or null if not found
  * @exception TelusAPIException
  */
  SalesRep findSalesRep(String salesCode) throws TelusAPIException;

  /**
 * Create a new unsaved sales rep for the given dealer.
 * @exception TelusAPIException
 * @return a new sales rep with the current dealers dealer code and given sales rep code
 * @param salesCode - the Knowbility sales rep code*/
  SalesRep newSalesRep(String salesCode) throws TelusAPIException;

  /**
  * Create a new unsaved sales rep for the given dealer, cloning the existing sales rep.
  * @exception TelusAPIException
  * @return a new sales rep with the current dealers dealer code with name and sales rep code copied from the original Sales Rep.
  * @param salesRep - an existing sales rep*/
  SalesRep newSalesRep(SalesRep salesRep) throws TelusAPIException;

  /**
  * Save the dealer information
  * @exception TelusAPIException
  */
  void     save()throws TelusAPIException;

}
