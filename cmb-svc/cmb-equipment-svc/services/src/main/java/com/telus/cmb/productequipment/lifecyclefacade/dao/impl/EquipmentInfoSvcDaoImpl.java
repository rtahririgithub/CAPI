package com.telus.cmb.productequipment.lifecyclefacade.dao.impl;


import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.productequipment.lifecyclefacade.dao.EquipmentInfoSvcDao;
import com.telus.cmb.wsclient.EquipmentInfoServicePort;
import com.telus.cmb.wsclient.PolicyException_v1;
import com.telus.eas.equipment.info.WarrantyInfo;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.equipmentinfoservicerequestresponse_v2.EquipmentWarrantySummaryType;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.equipmentinfoservicerequestresponse_v2.GetWarrantySummary;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;
import com.telus.tmi.xmlschema.xsd.resource.resource.subscriberequipmenttypes_v5.GeneralEquipmentGroupType;

public class EquipmentInfoSvcDaoImpl extends SoaBaseSvcClient implements EquipmentInfoSvcDao  {

	@Autowired
	private EquipmentInfoServicePort equipmentInfoServicePort = null;
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.productequipment.lifecyclefacade.dao.EquipmentInfoSvcDao#getWarrantySummary(java.lang.String, java.lang.String)
	 */
	@Override
	public WarrantyInfo getWarrantySummary(final String serialNumber, final String equipmentGroup) throws ApplicationException {
		
		return execute( new SoaCallback<WarrantyInfo>() {
			
			@Override
			public WarrantyInfo doCallback() throws Throwable {
				
				
				GetWarrantySummary request = new GetWarrantySummary();
				request.setEquipmentGroup(GeneralEquipmentGroupType.fromValue(equipmentGroup));
				request.setSerialNumber(serialNumber);
				
				EquipmentWarrantySummaryType eqWarrantySummary = null;

				WarrantyInfo warrantyInfo = new WarrantyInfo();
				
				try {
					eqWarrantySummary = equipmentInfoServicePort.getWarrantySummary(request).getWarrantySummary();
				} catch (PolicyException_v1 pex) {
					// do nothing
				}
				
				if(eqWarrantySummary!=null) {
					warrantyInfo.setDOAExpiryDate(eqWarrantySummary.getWarrantyDoaDate());
					warrantyInfo.setWarrantyExpiryDate(eqWarrantySummary.getWarrantyEndDate());
					warrantyInfo.setInitialActivationDate(eqWarrantySummary.getWarrantyStartDate());
				} else {
					warrantyInfo.setMessage("No valid Warranty exists");
				}
				
				return warrantyInfo;
			}
		});
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient#ping()
	 */
	@Override
	public String ping() throws ApplicationException {
		
		return execute( new SoaCallback<String>() {
			
			@Override
			public String doCallback() throws Throwable {
				return equipmentInfoServicePort.ping( new Ping()).getVersion();
			}
		});
	}
}
