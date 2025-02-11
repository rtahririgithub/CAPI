package com.telus.cmb.jws.mapping.customer_management_common_40;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.BusinessCreditInformation;


 public class BusinessCreditInformationMapper extends AbstractSchemaMapper<BusinessCreditInformation, BusinessCreditInfo> {

	private static BusinessCreditInformationMapper INSTANCE = null;
		
	public BusinessCreditInformationMapper(){
		super(BusinessCreditInformation.class, BusinessCreditInfo.class);
	}

	public static synchronized BusinessCreditInformationMapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new BusinessCreditInformationMapper();
		}
		
		return INSTANCE;
	}

	

	@Override
	protected BusinessCreditInfo performDomainMapping(BusinessCreditInformation source, BusinessCreditInfo target) {
        target.setIncorporationDate(source.getIncorporationDate());
        target.setIncorporationNumber(source.getIncorporationNumber());
		
		return super.performDomainMapping(source, target);
	}
}