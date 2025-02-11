package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import amdocs.APILink.sessions.interfaces.UpdateProductConv;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.dao.amdocs.AmdocsDaoSupport;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionCallback;
import com.telus.cmb.common.dao.amdocs.AmdocsTransactionContext;
import com.telus.cmb.subscriber.lifecyclemanager.dao.AddressDao;
import com.telus.cmb.subscriber.utilities.AmdocsConvBeanClassFactory;

import amdocs.APILink.datatypes.AddressInfo;

public class AddressDaoImpl extends AmdocsDaoSupport implements AddressDao {

	@Override
	public void updateAddress(final int ban, final String subscriber,final String productType,
			final com.telus.eas.account.info.AddressInfo addressInfo,final String sessionId) throws ApplicationException {
		
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {
			
			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				
				 if (addressInfo != null) {
					 UpdateProductConv amdocsUpdateProductConv = transactionContext.createBean(AmdocsConvBeanClassFactory.getUpdateProductConvBean(productType));
					 
				        // Set ProductPK (which also retrieves the Subscriber)
		                amdocsUpdateProductConv.setProductPK(ban, subscriber);

		                // map Telus class to Amdocs class
		                AddressInfo amdocsAddressInfo = new AddressInfo();

		                amdocsAddressInfo.type = (byte) addressInfo.getAddressType().charAt(0);
		                amdocsAddressInfo.city = addressInfo.getCity() != null ? addressInfo.getCity() : "";
		                amdocsAddressInfo.province = addressInfo.getProvince() != null ? addressInfo.getProvince() : "";
		                amdocsAddressInfo.postalCode = addressInfo.getPostalCode() != null ? addressInfo.getPostalCode() : "";
		                amdocsAddressInfo.country = addressInfo.getCountry() != null ? addressInfo.getCountry() : "";
		                amdocsAddressInfo.zipGeoCode = addressInfo.getZipGeoCode() != null ? addressInfo.getZipGeoCode() : "";
		                amdocsAddressInfo.foreignState = addressInfo.getForeignState() != null ? addressInfo.getForeignState() : "";
		                amdocsAddressInfo.civicNo = addressInfo.getCivicNo() != null ? addressInfo.getCivicNo() : "";
		                amdocsAddressInfo.civicNoSuffix = addressInfo.getCivicNoSuffix() != null ? addressInfo.getCivicNoSuffix() : "";
		                amdocsAddressInfo.streetDirection = addressInfo.getStreetDirection() != null ? addressInfo.getStreetDirection() : "";
		                amdocsAddressInfo.streetName = addressInfo.getStreetName() != null ? addressInfo.getStreetName() : "";
		                amdocsAddressInfo.streetType = addressInfo.getStreetType() != null ? addressInfo.getStreetType() : "";
		                amdocsAddressInfo.primaryLine = addressInfo.getPrimaryLine() != null ? addressInfo.getPrimaryLine() : "";
		                amdocsAddressInfo.secondaryLine = addressInfo.getSecondaryLine() != null ? addressInfo.getSecondaryLine() : "";
		                amdocsAddressInfo.rrDesignator = addressInfo.getRrDesignator() != null ? addressInfo.getRrDesignator() : "";
		                amdocsAddressInfo.rrIdentifier = addressInfo.getRrIdentifier() != null ? addressInfo.getRrIdentifier() : "";
		                amdocsAddressInfo.rrBox = addressInfo.getPoBox() != null ? addressInfo.getPoBox() : "";
		                amdocsAddressInfo.unitDesignator = addressInfo.getUnitDesignator() != null ? addressInfo.getUnitDesignator() : "";
		                amdocsAddressInfo.unitIdentifier = addressInfo.getUnitIdentifier() != null ? addressInfo.getUnitIdentifier() : "";
		                amdocsAddressInfo.rrAreaNumber = addressInfo.getRrAreaNumber() != null ? addressInfo.getRrAreaNumber() : "";
		                amdocsAddressInfo.rrQualifier = addressInfo.getRuralQualifier() != null ? addressInfo.getRuralQualifier() : "";
		                amdocsAddressInfo.rrSite = addressInfo.getRuralSite() != null ? addressInfo.getRuralSite() : "";
		                amdocsAddressInfo.rrCompartment = addressInfo.getRrCompartment() != null ? addressInfo.getRrCompartment() : "";
		                amdocsAddressInfo.attention = addressInfo.getAttention() != null ? addressInfo.getAttention() : "";
		                amdocsAddressInfo.rrDeliveryType = addressInfo.getRrDeliveryType() != null ? addressInfo.getRrDeliveryType() : "";
		                amdocsAddressInfo.rrGroup = addressInfo.getRrGroup() != null ? addressInfo.getRrGroup() : "";

		                // update address
		                amdocsUpdateProductConv.changeUserAddress(amdocsAddressInfo);
		            }
				
				return null;
			}
		});
	}
	
	
	

}
