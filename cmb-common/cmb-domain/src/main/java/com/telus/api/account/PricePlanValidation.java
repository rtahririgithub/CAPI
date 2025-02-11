package com.telus.api.account;

public interface PricePlanValidation extends ServicesValidation{
	
	boolean validateCurrent();
	
	boolean validateForSale();
	
	void setCurrentValidation(boolean currentValidation);
	
	void setForSaleValidation(boolean forSaleValidation);

}
