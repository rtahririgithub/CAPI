package com.telus.cmb.productequipment.lifecyclefacade.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.equipment.info.UsimProfileInfo;
import com.telus.eas.equipment.productdevice.info.ProductInfo;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface ProductDeviceServiceDao {

	public TestPointResultInfo test();	
	public ProductInfo getProduct(String productCode) throws ApplicationException;
	public ProductInfo getProduct(final String productCode, boolean isEsim) throws ApplicationException;
	public UsimProfileInfo getProductUsimProfile(long usimProductId) throws ApplicationException;
}