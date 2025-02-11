/*
 * Created on 29-Jun-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.telus.api.reference;

/**
 * @author zhangji
 *
 * The SID is the interface holds the information for carrier network system.
 */
public interface SID extends Reference {
	String getState();
	String getCity();
	
	/**
	 * This method will return the country code for the carrier 
	 * @return String
	 */
	String getCountry();
}
