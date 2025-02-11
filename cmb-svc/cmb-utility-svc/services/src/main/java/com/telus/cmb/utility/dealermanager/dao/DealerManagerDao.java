package com.telus.cmb.utility.dealermanager.dao;

import java.util.Date;

import com.telus.api.ApplicationException;
import com.telus.eas.utility.info.SalesRepInfo;


public interface DealerManagerDao {

	/**
	 *  Creates an entry for a new dealer in  the DEALER_PROFILE reference table,
	 *  and populate it with the inputted DealerInfo parameter data.
	 * @param        DealerInfo
	 * @return       void
	 */
	void addDealer(com.telus.eas.utility.info.DealerInfo dealerInfo, String sessionId) throws ApplicationException; 

	/**
	 *  Expires an existing dealer record by setting the END_DATE field
	 *  in the DEALER_PROFILE reference table with the endDate input parameter value.
	 * @param        String dealerCode [mandatory]
	 * @param        Date endDate      [default: logical date]
	 * @return       void
	 */
	void expireDealer(String dealerCode, Date endDate, String sessionId) throws ApplicationException;
	
	/**
	 * Unexpires an existing dealer record by unsetting an expiration date
	 * from END_DATE field in the DEALER_PROFILE reference table.
	 * @param        String dealerCode    [mandatory]
	 * @return       void
	 */
	void unexpireDealer(String dealerCode, String sessionId) throws ApplicationException;	


	/**
	 *  Updates existing dealer name by setting DLR_NAME field in the
	 *  DEALER_PROFILE reference table with the dealerName input parameter value
	 * @param        String dealerCode [mandatory]
	 * @param        String dealerName [mandatory]
	 * @return       void
	 */
	void changeDealerName(String dealerCode, String dealerName, String sessionId) throws ApplicationException;	

	/** Method Name: addSalesperson
	 * Creates an entry for a new salesperson in the DEALER_SALES_REP reference table,
	 * and populate it with the inputted SalespersonInfo parameter data.
	 * @param       SalespersonInfo
	 * @return      void
	 */
	void addSalesperson(SalesRepInfo salesRepInfo, String sessionId) throws ApplicationException; 
  
	/**
	 * Expires an existing salesperson record by setting the EXPIRATION_DATE field in the
	 * DEALER_SALES_REP reference table with the expirationDate input parameter value.
	 * @param        String dealerCode [mandatory]
	 * @param        String salesCode  [mandatory]
	 * @param        Date endDate      [default: logical date]
	 * @return       void
	 */
	void expireSalesperson(String dealerCode, String salesCode, Date endDate, String sessionId) throws ApplicationException;
 
	/**
	 *  Unexpire an existing salesperson record by wiping an expiration date
	 *  from the EXPIRATION_DATE field in the DEALER_SALES_REP reference.
	 * @param       String dealerCode    [mandatory]
	 * @param       String salesCode     [mandatory]
	 * @return      void
	 */
	void unexpireSalesperson(String dealerCode, String salesCode, String sessionId) throws ApplicationException;
	
	/**
	 * Updates an existing salesperson name by setting SALES_NAME field in the
	 * DEALER_SALES_REP reference table with the salespersonName input parameter value.
	 * @param       String dealerCode [mandatory]
	 * @param       String salesCode  [mandatory]
	 * @param       String salesName  [mandatory]
	 * @return      void
	 */
	void changeSalespersonName(String dealerCode, String salesCode, String salesName, String sessionId)
			throws ApplicationException;	

	/**
	 *   Transfer an existing salesperson from one dealership to another.
	 *   The implementation of this API will incorporate number of APIs: the expireSalesperson ()
	 *   and either addSalesperson() or unexpireSalesperson() methods in one single transaction.
	 *  @param       SalesRepInfo salesInfo
	 *  @param       String newDealerCode
	 *  @param       Date transferDate
	 *  @return      void
	 */
	void transferSalesperson(SalesRepInfo salesInfo, String newDealerCode, Date transferDate, String sessionId)
		throws ApplicationException;	

}
