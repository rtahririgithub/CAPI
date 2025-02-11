package com.telus.cmb.subscriber.utilities;

import amdocs.APILink.sessions.interfaces.NewCdpdConv;
import amdocs.APILink.sessions.interfaces.NewCellularConv;
import amdocs.APILink.sessions.interfaces.NewIdenConv;
import amdocs.APILink.sessions.interfaces.NewPagerConv;
import amdocs.APILink.sessions.interfaces.NewProductConv;
import amdocs.APILink.sessions.interfaces.NewTangoConv;
import amdocs.APILink.sessions.interfaces.UpdateCdpdConv;
import amdocs.APILink.sessions.interfaces.UpdateCellularConv;
import amdocs.APILink.sessions.interfaces.UpdateIdenConv;
import amdocs.APILink.sessions.interfaces.UpdatePagerConv;
import amdocs.APILink.sessions.interfaces.UpdateProductConv;
import amdocs.APILink.sessions.interfaces.UpdateTangoConv;

import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.account.Subscriber;

public class AmdocsConvBeanClassFactory {
	
	public static Class<? extends NewProductConv> getNewProductConvBean(String productType) {
		if (productType.equals(Subscriber.PRODUCT_TYPE_PCS)) {
			return NewCellularConv.class;
		} else if (productType.equals(Subscriber.PRODUCT_TYPE_PAGER)) {
			return NewPagerConv.class;
		} else if (productType.equals(Subscriber.PRODUCT_TYPE_IDEN)) {
			return NewIdenConv.class;
		} else if (productType.equals(Subscriber.PRODUCT_TYPE_TANGO)) {
			return NewTangoConv.class;
		} else if (productType.equals(Subscriber.PRODUCT_TYPE_CDPD)) {
			return NewCdpdConv.class;
		} else {
			throw new SystemException(SystemCodes.CMB_EJB, "Invalid Product Type : " + productType,"");
		}
	}
	
	public static Class<? extends UpdateProductConv> getUpdateProductConvBean(String productType) {
		if (productType.equals(Subscriber.PRODUCT_TYPE_PCS)) {
			return UpdateCellularConv.class;
		} else if (productType.equals(Subscriber.PRODUCT_TYPE_PAGER)) {
			return UpdatePagerConv.class;
		} else if (productType.equals(Subscriber.PRODUCT_TYPE_IDEN)) {
			return UpdateIdenConv.class;
		} else if (productType.equals(Subscriber.PRODUCT_TYPE_TANGO)) {
			return UpdateTangoConv.class;
		} else if (productType.equals(Subscriber.PRODUCT_TYPE_CDPD)) {
			return UpdateCdpdConv.class;
		} else {
			throw new SystemException(SystemCodes.CMB_EJB, "Invalid Product Type : " + productType,"");
		}
	}
	

}
