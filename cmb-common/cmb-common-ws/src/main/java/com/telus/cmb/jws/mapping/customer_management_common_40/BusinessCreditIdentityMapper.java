package com.telus.cmb.jws.mapping.customer_management_common_40;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.BusinessCreditIdentity;

public class BusinessCreditIdentityMapper extends AbstractSchemaMapper<BusinessCreditIdentity, BusinessCreditIdentityInfo> {

	public BusinessCreditIdentityMapper() {
		super(BusinessCreditIdentity.class, BusinessCreditIdentityInfo.class);
	}
	
	@Override
	protected BusinessCreditIdentityInfo performDomainMapping(BusinessCreditIdentity source, BusinessCreditIdentityInfo target) {

			target.setCompanyName(source.getCompanyName());
			target.setMarketAccount(source.getMarketAccount());
	      
		return super.performDomainMapping(source, target);
	}

	@Override
	protected BusinessCreditIdentity performSchemaMapping(BusinessCreditIdentityInfo source, BusinessCreditIdentity target) {
		
			target.setCompanyName(source.getCompanyName());
			target.setMarketAccount(source.getMarketAccount());
		
		return super.performSchemaMapping(source, target);
	}


}
