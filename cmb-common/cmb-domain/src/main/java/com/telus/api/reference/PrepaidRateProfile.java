
package com.telus.api.reference;

/**
 * @author x119734
 *
 * The PrepaidRatepPofile is the interface holds the prepaid rate information like
 * rate id, rate type, rate etc.
 */
public interface PrepaidRateProfile extends Reference {
	
	/**
	 * This method will return the rate id 
	 * @return int
	 */
	int getRateId();
	
	/**
	 * This method will return the rate
	 * @return double
	 */
	double getRate();
	
	/**
	 * This method will return the rate type id
	 * @return String
	 */ 
	String getRateTypeId();
	
	/**
	 * This method will return the country code
	 * @return String
	 */
	String getCountryCode();
	
	/**
	 * This method will return the array of application code
	 * @return String[]
	 */
	String[] getApplicationIds();
	
	public static final String APPLICATION_CODE_FOR_PREPAID_RATE_SD="SMARTDESKTOP";
	public static final String PREPAID_RATE_TYPE_LOCAL="-1";
	public static final String PREPAID_RATE_TYPE_LD="-2";
	public static final String PREPAID_RATE_TYPE_ROAMING="-3";
	public static final String PREPAID_RATE_TYPE_INTERNATIONAL="1";
}
