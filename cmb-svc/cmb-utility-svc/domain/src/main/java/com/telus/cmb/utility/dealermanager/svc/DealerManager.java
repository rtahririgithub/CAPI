package com.telus.cmb.utility.dealermanager.svc;

import java.util.Date;

import com.telus.api.ApplicationException;
import com.telus.eas.equipment.info.CPMSDealerInfo;
import com.telus.eas.utility.info.DealerInfo;
import com.telus.eas.utility.info.SalesRepInfo;


public interface DealerManager {

	/**
	 * Obtain sessionId for the given user credentials
	 * 
	 * @param userId The user id.
	 * @param password The password.
	 * @param applicationId The application id.
	 */
	String openSession(String userId, String password, String applicationId) throws ApplicationException;

	
	void addDealer(DealerInfo dealerInfo, String sessionId) throws ApplicationException; 

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
	
	/**
	 * Adds an entry for a new salesperson in the DEALER_SALES_REP reference table,
	 * and populates it with the inputted SalespersonInfo parameter data.
	 * @param     SalesRepInfo    all attributes related to a sales rep
	 * @see       SalesRepInfo
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

	void expireSalesperson(String dealerCode, String salesCode,
			Date endDate, String sessionId) throws ApplicationException;

	/**
	 *  Unexpire an existing salesperson record by wiping an expiration date
	 *  from the EXPIRATION_DATE field in the DEALER_SALES_REP reference.
	 * @param       String dealerCode    [mandatory]
	 * @param       String salesCode     [mandatory]
	 * @return      void
	 */
	
	void unexpireSalesperson(String dealerCode, String salesCode, 
			String sessionId) throws ApplicationException;

	/**
	 * Updates an existing salesperson name by setting SALES_NAME field in the
	 * DEALER_SALES_REP reference table with the salespersonName input parameter value.
	 * @param       String dealerCode [mandatory]
	 * @param       String salesCode  [mandatory]
	 * @param       String salesName  [mandatory]
	 * @return      void
	 */
	void changeSalespersonName(String dealerCode, String salesCode,
			String salesName, String sessionId) throws ApplicationException;

	/**
	 *   Transfer an existing salesperson from one dealership to another.
	 *   The implementation of this API will incorporate number of APIs: the expireSalesperson ()
	 *   and either addSalesperson() or unexpireSalesperson() methods in one single transaction.
	 *   @param       SalesRepInfo salesInfo
	 *   @param       String newDealerCode
	 *   @param       Date transferDate
	 *   @return      void
	 */
	void transferSalesperson(SalesRepInfo salesInfo, String newDealerCode,
			Date transferDate, String sessionId) throws ApplicationException;
	
	void validUser(String pChannelCode, String pUserCode, String pPassword)throws ApplicationException;
	 
	void changeUserPassword(String channelCode,String userCode,String oldPassword,
			 String newPassword) throws ApplicationException;
	 
	void resetUserPassword(String channelCode,String userCode,String newPassword)   
	 	throws ApplicationException;
	
	CPMSDealerInfo getCPMSDealerInfo(String pChannelCode, String pUserCode) throws ApplicationException;

	
	CPMSDealerInfo getCPMSDealerByKBDealerCode(String pKBDealerCode,
			 String pKBSalesRepCode)  throws ApplicationException;

	
	CPMSDealerInfo getCPMSDealerByLocationTelephoneNumber(String pLocationTelephoneNumber)  
 		throws ApplicationException;
	
	long[] getChnlOrgAssociationSAPSoldToParty(long chnlOrgId) throws ApplicationException;
	
	long[] getChnlOrgAssociation(long chnlOrgId, String associateReasonCd) throws ApplicationException;
}
