package com.telus.eas.account.info;

import com.telus.api.account.ServicesValidation;
import com.telus.eas.framework.info.Info;

public class ServicesValidationInfo extends Info implements ServicesValidation{
   /*
    	true is the default value for all types of validation. 
    	This value means that validation will not be skipped. 
    	If Interface application wants to skip some validation, 
    	the value should be changed to false
   */ 
	
	private boolean pricePlanServiceGrouping = true;
	private boolean provinceServiceMatch = true;
	private boolean equipmentServiceMatch= true;
	
	
	public boolean validateEquipmentServiceMatch() {
		return equipmentServiceMatch;
	}
	
	public void setEquipmentServiceMatch(boolean equipmentServiceMatch) {
		this.equipmentServiceMatch = equipmentServiceMatch;
	}
	
	public boolean validatePricePlanServiceGrouping() {
		return pricePlanServiceGrouping;
	}
	public void setPricePlanServiceGrouping(boolean pricePlanServiceGrouping) {
		this.pricePlanServiceGrouping = pricePlanServiceGrouping;
	}
	
	public boolean validateProvinceServiceMatch() {
		return provinceServiceMatch;
	}
	
	public void setProvinceServiceMatch(boolean provinceServiceMatch) {
		this.provinceServiceMatch = provinceServiceMatch;
	}

}
