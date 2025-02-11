package com.telus.api.reference;

public interface Province extends Reference {
	
	static final String PROVINCE_AB = "AB";
	static final String PROVINCE_BC = "BC";
	static final String PROVINCE_NL = "NL";
	
	String getCountryCode();
	String getCanadaPostCode();
}
