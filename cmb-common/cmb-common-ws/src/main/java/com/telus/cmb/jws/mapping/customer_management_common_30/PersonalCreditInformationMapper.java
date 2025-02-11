package com.telus.cmb.jws.mapping.customer_management_common_30;

import com.telus.api.TelusAPIException;
import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.PersonalCreditInformation;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.ProvinceCode;

public class PersonalCreditInformationMapper extends AbstractSchemaMapper<PersonalCreditInformation, PersonalCreditInfo> {
	
	private static PersonalCreditInformationMapper INSTANCE = null;
	
	public PersonalCreditInformationMapper(){
		super(PersonalCreditInformation.class, PersonalCreditInfo.class);
	}
	
	public static synchronized PersonalCreditInformationMapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PersonalCreditInformationMapper();
		}
		
		return INSTANCE;
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected PersonalCreditInformation performSchemaMapping(PersonalCreditInfo source, PersonalCreditInformation target) {
		if (source.getBirthDate() != null)
			target.setBirthDate(source.getBirthDate());
		target.setDriversLicense(source.getDriversLicense());
		if (source.getDriversLicenseExpiry() != null)
			target.setDriversLicenseExpiry(source.getDriversLicenseExpiry());
		if (source.getDriversLicenseProvince() != null)
			target.setDriversLicenseProvince(ProvinceCode.fromValue(source.getDriversLicenseProvince()));
		target.setSin(source.getSin());
		target.setCreditCard(new CreditCardMapper().mapToSchema(source.getCreditCard0()));
		
		return super.performSchemaMapping(source, target);
	}
	
	@Override
	protected PersonalCreditInfo performDomainMapping(PersonalCreditInformation source, PersonalCreditInfo target) {
		
		if (source.getCreditCard() != null) {
			String token = source.getCreditCard().getToken();
			String first6 = source.getCreditCard().getFirst6();
			String last4 = source.getCreditCard().getLast4();
			if (token != null && !token.equals("")  &&
				first6 != null && !first6.equals("")  &&	
				last4 != null && !last4.equals("")  ) {
			
				try {
					target.getCreditCard().setToken(token,first6 , last4);
				} catch (TelusAPIException e) {
					e.printStackTrace();
				}
			}
			target.getCreditCard().setExpiryMonth(source.getCreditCard().getExpiryMonth());
			target.getCreditCard().setExpiryYear(source.getCreditCard().getExpiryYear());
			if(source.getCreditCard().getCardVerificationData()!=null)
				target.getCreditCard().setCardVerificationData(source.getCreditCard().getCardVerificationData());
			if(source.getCreditCard().getHolderName()!=null)
				target.getCreditCard().setHolderName(source.getCreditCard().getHolderName());
		}
		
		if (source.getBirthDate() != null)
			target.setBirthDate(source.getBirthDate());
		target.setSin(source.getSin());
		target.setDriversLicense(source.getDriversLicense());
		if (source.getDriversLicenseExpiry() != null)
			target.setDriversLicenseExpiry(source.getDriversLicenseExpiry());
		if (source.getDriversLicenseProvince() != null)
			target.setDriversLicenseProvince(source.getDriversLicenseProvince().value());
				
		return super.performDomainMapping(source, target);
	}
	
	
}


