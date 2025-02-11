/*
 * Created on 16-Sep-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.telus.api.reference;

/**
 * @author x119734
 *
 * The Route is the interface holds the city/province information for each cell site.
 */
public interface Route extends Reference {
	
	/**
	 * This method will return the city for the cell site 
	 * @return String
	 */
	String getCity();
	
	/**
	 * This method will return the state code for the cell site 
	 * @return String
	 */
	String getState();
	
	/**
	 * This method will return the country code for the cell site 
	 * @return String
	 */
	String getCountry();
	
	/**
	 * This method will return the switch id for the cell site 
	 * @return String
	 */
	String getSwitchId();
	
	/**
	 * This method will return the route id for the cell site 
	 * @return String
	 */
	String getRouteId();
	
	/**
	 * This method will return the SID for the cell site 
	 * @return String
	 */
	String getServeSID();

}
