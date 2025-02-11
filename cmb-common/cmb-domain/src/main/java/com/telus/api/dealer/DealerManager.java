package com.telus.api.dealer;

/**
 * Title:        Telus Domain Project -KB61
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */


import com.telus.api.*;
import com.telus.api.reference.*;

public interface DealerManager extends java.io.Serializable{


/**
   * Returns Dealer Info from Channel Partner Management System(PDIST)
   *
   * <P>This method may involve a remote method call.
   *
   */
CPMSDealer getCPMSDealer(String dealerCode, String salesRepCode) throws UnknownObjectException, TelusAPIException;

  /**
     * Returns Retailer Info from Channel Partner Management System(PDIST)
     * given a location telephone number.
     *
     * <P>This method may involve a remote method call.
     *
     */
  CPMSDealer getCPMSDealerByLocationTelephoneNumber(String locationTelephoneNumber) throws UnknownObjectException, TelusAPIException;

/**
   * Returns Array of long of Channel Organization Id. (PK of Channel Organization)
   * from CPMS Channel Partner Management System (in PDIST). They are associated with
   * each other in SAP Sold To Relationship. It may, and most of the time, returns empty array,
   * but never null.
   *
   * <P>This method involves a remote method call.
   *
   */
long[] getChnlOrgAssociationSAPSoldToParty(long chnlOrgId) throws TelusAPIException;

/**
 * Create a new unsaved Knowbility dealer.
 * @exception TelusAPIException
 * @return a new dealer with the given dealer code
 * @param dealerCode - the Knowbility dealer code
 */
Dealer newDealer(String dealerCode) throws TelusAPIException ;

/**
* Create a new unsaved Knowbility sales rep.
* @exception TelusAPIException
* @return a new sales rep with the given dealer and sales rep codes
* @param dealerCode - the Knowbility dealer code
* @param salesCode - the Knowbility sales rep code
*/
SalesRep newSalesRep(String dealerCode, String salesCode) throws TelusAPIException ;

/**
* Find a Knowbility dealer using it's dealer code.
* @return the dealer instance or null of not found
* @exception TelusAPIException
* @param dealerCode - the Knowbility dealer code
* */
Dealer findDealer(String dealerCode) throws TelusAPIException ;

/**
* Find a Knowbility dealer using it's dealer code.
* @return the dealer instance or null of not found
* @exception TelusAPIException
* @param dealerCode - the Knowbility dealer code
* @param expired    - boolean to indicate if include expired dealer 
* */
Dealer findDealer(String dealerCode, boolean expired) throws TelusAPIException ;

/**
* Find a Knowbility sales rep using the dealer code and sales rep code
* @exception TelusAPIException
* @return the sales rep instance or null if not found
* @param dealerCode - the Knowbility dealer code
* @param salesCode - the Knowbility sales rep code
*/
SalesRep findSalesRep(String dealerCode, String salesCode) throws TelusAPIException ;

/**
* Find a Knowbility sales rep using the dealer code and sales rep code
* @exception TelusAPIException
* @return the sales rep instance or null if not found
* @param dealerCode - the Knowbility dealer code
* @param salesCode - the Knowbility sales rep code
* @param expired    - boolean to indicate if include expired salesrep 
*/
SalesRep findSalesRep(String dealerCode, String salesCode, boolean expired) throws TelusAPIException ;


}
