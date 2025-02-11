package com.telus.api.portability;

import java.util.Date;

import com.telus.api.TelusAPIException;

/**
 * @author liaomi
 *
 */
public interface PortRequest extends PortRequestSummary{
	
	public static String DSL_IND_RETAIN_SERVICE = "R";
	public static String DSL_IND_DISCONNECT_SERVICE = "D";
	public static String DSL_IND_NO_SERVICE = "N";
	public static String DSL_IND_UNKNOWN = "U";
	public static String EXPEDITE_IND_TREATMENT_REQUESTED = "Y";
	public static String EXPEDITE_IND_TREATMENT_NOT_ALLOWED = "N";
	
	String getPortDirectionIndicator();
	void setPortDirectionIndicator(String portDirectionIndicator);


	PortRequestAddress getPortRequestAddress();
	void setPortRequestAddress (PortRequestAddress portRequestAddress);


	PortRequestName getPortRequestName();
	void setPortRequestName (PortRequestName portRequestName);

	String getBusinessName();
	void setBusinessName(String businessName);

	String getAgencyAuthorizationName();
	void  setAgencyAuthorizationName(String  agencyAuthorizationName);

	Date  getAgencyAuthorizationDate();
	void  setAgencyAuthorizationDate(Date  agencyAuthorizationDate);

	String getAuthorizationIndicator();
	void  setAgencyAuthorizationIndicator(String  agencyAuthorizationIndicator);

	String getAlternateContactNumber();
	void  setAlternateContactNumber (String  alternateContactNumber);

	Date getDesiredDateTime();
	void setDesiredDateTime(Date desiredDateTime);
		
	String getOSPSerialNumber();
	void setOSPSerialNumber (String oSPSerialNumber);

	String getOSPAccountNumber();
	void setOSPAccountNumber (String oSPAccountNumber);

	String getOSPPin();
	void setOSPPin (String oSPPin);

	String getRemarks();
	void setRemarks(String remarks);

	boolean isAutoActivate();
	void setAutoActivate(boolean  autoActivate);
	
	/**
	 * return Expedite flag 
	 * @return  <tt>EXPEDITE_IND_TREATMENT_REQUESTED</tt> Expedite treatment requested / Expedite treatment can be accommodated.                                                                                                                        
	 * @return  <tt>EXPEDITE_IND_TREATMENT_NOT_ALLOWED</tt> when Expedite treatment cannot be accommodated
	 */
	String getExpedite();
	/**
	 * set Expedite flag
	 * @param expedite 
	 *   <tt>EXPEDITE_IND_TREATMENT_REQUESTED</tt> indicate expedite treatment is requested, otherwise, leave it blank 
	 */
	void setExpedite(String expedite);
	
	/**
	 * @return String
	 */
	String getDslInd();
	
	/**
	 * set DSL indicator: indicates whether a customer’s DSL service is to be retained or disconnected.
	 * @param dslInd one of following value
	 * 	<tt>DSL_IND_RETAIN_SERVICE</tt> - customer want to retain DSL service
	 *  <tt>DSL_IND_DISCONNECT_SERVICE</tt> - customer want to disconnect DSL service
	 *  <tt>DSL_IND_NO_SERVICE</tt> - customer does not have DSL service
	 *  <tt>DSL_IND_UNKNOWN</tt> - UNKOWN
	 */
	void setDslInd(String dslInd);
	
	Integer getDslLineNumber();
	
	/**
	 * @param dslLineNumber -- can be null depneds on DslInd's value 
	 */
	void setDslLineNumber (Integer dslLineNumber);
	
	boolean getEndUserMovingInd();
	/**
	 * set the indicator that identifies when the end user is moving to a new location coincident with a change in local service provider
	 * @param endUserMoving
	 */
	void setEndUserMovingInd (boolean endUserMoving);
	
	String getOldReseller();
	/**
	 * set the name of the Old Reseller involved in the port Request (WPR) and Response (WPRR)
	 * @param oldReseller the name of the Old Reseller 
	 */
	void setOldReseller (String oldReseller);
	
	/**
	 * The validate method relies on the platform ID to be accurate. If the platform ID is incorrect,
	 * the validation may pass the wrong product type to the underlying service.
	 * 
	 * @throws PortRequestException
	 * @throws TelusAPIException
	 */
	void validate() throws PortRequestException, TelusAPIException;
	void activate() throws PortRequestException, TelusAPIException;

}
