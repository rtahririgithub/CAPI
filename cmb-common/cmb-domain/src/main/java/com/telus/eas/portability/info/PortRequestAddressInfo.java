package com.telus.eas.portability.info;

import com.telus.api.portability.PortRequestAddress;
import com.telus.eas.framework.info.Info;

public class PortRequestAddressInfo extends Info implements PortRequestAddress{
	private String streetNumber;
	private String streetName;
	private String streetDirection;
	private String city;
	private String province;
	private String postalCode;
	private String country;
	
	public String getStreetNumber(){
	  return streetNumber;		
	}
	public void setStreetNumber(String streetNumber){
		this.streetNumber = streetNumber;
	}
	public String getStreetName(){
	   return streetName;   		
	}
	public void setStreetName(String streetName){
		this.streetName = streetName;
	}
	public String getStreetDirection(){
		return streetDirection;		
	}
	public void setStreetDirection(String newStreetDirection){
		this.streetDirection = newStreetDirection;
	}
	public String getCity(){
		return city;
	}
	public void setCity(String city){
		this.city = city;
	}
	public String getProvince(){
		return province;
	}
	public void setProvince(String province){
		this.province = province;
	}
	public String getPostalCode(){
		return postalCode;
	}
	public void setPostalCode(String postalCode){
		this.postalCode = postalCode;
	}
	public String getCountry(){
		return country;
	}
	public void setCountry(String country){
		this.country = country;
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer(128);

		s.append("PortRequestAddressInfo:[\n");

		s.append("    streetNumber=[").append(streetNumber).append("]\n");
		s.append("    streetName=[").append(streetName).append("]\n");
		s.append("    streetDirection=[").append(streetDirection).append("]\n");
		s.append("    city=[").append(city).append("]\n");
		s.append("    province=[").append(province).append("]\n");
		s.append("    postalCode=[").append(postalCode).append("]\n");
		s.append("    country=[").append(country).append("]\n");
		s.append("]");

		return s.toString();

	}			
}
