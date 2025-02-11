package com.telus.api.account;

public interface ServicesValidation {
	
	boolean validatePricePlanServiceGrouping();
	
	boolean validateProvinceServiceMatch();
	
	boolean validateEquipmentServiceMatch();
	
	void setPricePlanServiceGrouping(boolean pricePlanServiceGrouping);
	
	void setProvinceServiceMatch(boolean provinceServiceMatch);
	
}
