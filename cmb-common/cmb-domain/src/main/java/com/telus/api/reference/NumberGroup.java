
/**
 * Title:       NumberGroupInfo <p>
 * Description:  The  NumberGroupInfo holds all Number Group(City) ralated info<p>
 * Copyright:    Copyright (c) Ludmila Pomirche <p>
 * Company:     Telus Mobilty INC. <p>
 * @author Ludmila Pomirche
 * @version 1.0
 */
package com.telus.api.reference;

public interface NumberGroup extends Reference {

	String MARKET_NPA_NXX_LR_RESOURCE_TYPE_PTN = "N";

	String getProvinceCode();
	String[] getNpaNXX();
	int getNetworkId();
	String getNumberLocation();
	String getDefaultDealerCode();
	String getDefaultSalesCode() ;
}