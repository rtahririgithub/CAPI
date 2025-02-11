/*
 * Created on 6-Jul-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.telus.eas.utility.info;


import com.telus.api.reference.SID;

/**
 * @author zhangji
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SIDInfo implements SID {

	static final long serialVersionUID = 1L;

	private String state;
	private String city;
	private String code;
	private String description;
	private String description_fr;
	private String country;
		
	public void setState( String state ) {
		this.state = state;
	}
	
	public void setCity( String city ) {
		this.city = city;
	}
	
	public String getState() {
		return state;	
	}
	
	public String getCity() {
		return city;	
	}

	public String getCode() {
		return code;	
	}

	public String getDescription() {
		return description;
	}

	public String getDescriptionFrench() {
		return description_fr;
	}

	public void setCode( String code ) {
		this.code = code;
	}

	public void setDescription( String description ) {
		this.description = description;
	}

	public void setDescriptionFrench( String description_fr ) {
		this.description_fr = description_fr;
	}	
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}

}
