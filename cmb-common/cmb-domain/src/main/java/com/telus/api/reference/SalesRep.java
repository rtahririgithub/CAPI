
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

public interface SalesRep extends Reference {


  String getName();
  String getDealerCode();
  Date   getEffectiveDate();
  Date   getExpiryDate();
  void   setCode(String newCode);
  void   setDescription(String newDescription);
  void   setDescriptionFrench(String newDescription);
  void   setName(String newName);
  void   setDealerCode(String aDealerCode);
  void   setEffectiveDate(Date anEffectiveDate);
  void   setExpiryDate(Date anExpiryDate);

  /**
   * Save the sales rep information
   * @exception TelusAPIException
   */
  void save() throws TelusAPIException;

  /**
 * Transfer a sales rep to a new dealer from the current dealer.
 * @param dealerCode - the Dealer to transfer to
 * @param transferDate - the effective date of the trasnfer
 * @throws TelusAPIExcpetion
 */
  void transferSalesRep(String dealerCode, Date transferDate) throws TelusAPIException;

}
