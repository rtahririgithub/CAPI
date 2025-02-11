/*
 * Created on 28-Oct-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.telus.eas.utility.info;

import com.telus.api.reference.Route;

/**
 * @author x119734
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RouteInfo implements Route {
	
	private String switchId;
	private String routeId;
	private String serveSID;
	private String city;
	private String state;
	private String country;
	private String code;
	private String description;
	private String descriptionFrench;

	
	
	/**
	 * @return Returns the city.
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city The city to set.
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return Returns the country.
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country The country to set.
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return Returns the routeId.
	 */
	public String getRouteId() {
		return routeId;
	}
	/**
	 * @param routeId The routeId to set.
	 */
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	/**
	 * @return Returns the serveSID.
	 */
	public String getServeSID() {
		return serveSID;
	}
	/**
	 * @param serveSID The serveSID to set.
	 */
	public void setServeSID(String serveSID) {
		this.serveSID = serveSID;
	}
	/**
	 * @return Returns the state.
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state The state to set.
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return Returns the switchId.
	 */
	public String getSwitchId() {
		return switchId;
	}
	/**
	 * @param switchId The switchId to set.
	 */
	public void setSwitchId(String switchId) {
		this.switchId = switchId;
	}
	
	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code The code to set.
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the descriptionFrench.
	 */
	public String getDescriptionFrench() {
		return descriptionFrench;
	}
	/**
	 * @param descriptionFrench The descriptionFrench to set.
	 */
	public void setDescriptionFrench(String descriptionFrench) {
		this.descriptionFrench = descriptionFrench;
	}
	
}
