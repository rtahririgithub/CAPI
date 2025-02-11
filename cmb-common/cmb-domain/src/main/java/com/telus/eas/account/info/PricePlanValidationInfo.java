package com.telus.eas.account.info;

import com.telus.api.account.PricePlanValidation;



public class PricePlanValidationInfo extends ServicesValidationInfo implements PricePlanValidation{
	
	private boolean currentValidation = false;
	private boolean forSaleValidation = true;

	public boolean isModified(){
		if (  validateCurrent() ||
			 !validateForSale() ||
			 !validateEquipmentServiceMatch() ||
			 !validatePricePlanServiceGrouping() ||
			 !validateProvinceServiceMatch())
			return true;
		else
			return false;
		
	}
	
	public boolean validateCurrent (){
		return currentValidation;
	}
	
	public boolean validateForSale(){
		return forSaleValidation;
	}
	
	public void setCurrentValidation (boolean currentValidation){
		this.currentValidation = currentValidation;
	}
	
	public void setForSaleValidation (boolean forSaleValidation){
		this.forSaleValidation = forSaleValidation;
	}


}
