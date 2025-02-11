package com.telus.api.portability;

public interface PortRequestAddress {
	String getStreetNumber();
	void setStreetNumber(String streetNumber);

	String getStreetName();
	void setStreetName(String streetName);

	String getStreetDirection();
	void setStreetDirection(String newStreetDirection);

	String getCity();
	void setCity(String city);

	String getProvince();
	void setProvince(String province);

	String getPostalCode();
	void setPostalCode(String postalCode);

	String getCountry();
	void setCountry(String country);

}
