package com.telus.cmb.jws.mapping.customer_management_common_30;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.BusinessCreditInformation;


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

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected BusinessCreditInformation performSchemaMapping(BusinessCreditInfo source, BusinessCreditInformation target) {
		if (source != null) {
			target.setIncorporationNumber(source.getIncorporationNumber());
			target.setIncorporationDate(source.getIncorporationDate());
		}
		return super.performSchemaMapping(source, target);
	}

	@Override
	protected BusinessCreditInfo performDomainMapping(BusinessCreditInformation source, BusinessCreditInfo target) {
        target.setIncorporationDate(source.getIncorporationDate());
        target.setIncorporationNumber(source.getIncorporationNumber());
		
		return super.performDomainMapping(source, target);
	}
}