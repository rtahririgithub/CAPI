package com.telus.cmb.jws.mapping.account_information_20;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.AddressMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.ConsumerNameMapper;
import com.telus.eas.account.info.BillingPropertyInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v2.BillingPropertyListType;

public class BillingPropertyMapper extends AbstractSchemaMapper<BillingPropertyListType, BillingPropertyInfo> {
	private static BillingPropertyMapper INSTANCE = null;
	
	private BillingPropertyMapper() {
		super (BillingPropertyListType.class, BillingPropertyInfo.class);
	}
	
	public static synchronized BillingPropertyMapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new BillingPropertyMapper();
		}
		
		return INSTANCE;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected BillingPropertyListType performSchemaMapping(BillingPropertyInfo source, BillingPropertyListType target) {
		if (source != null) {
			target.setAddress(AddressMapper.getInstance().mapToSchema(source.getAddress()));
			target.setLegalBusinessName(source.getLegalBusinessName());
			target.setName(ConsumerNameMapper.getInstance().mapToSchema(source.getName()));
			if (source.getTradeNameAttention() != null && source.getTradeNameAttention().isEmpty() == false) {
				target.setTradeNameAttention(source.getTradeNameAttention());
			}
			target.setVerifiedDate(source.getVerifiedDate());
		}
		return super.performSchemaMapping(source, target);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performDomainMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected BillingPropertyInfo performDomainMapping(BillingPropertyListType source, BillingPropertyInfo target) {
		target.setAddress(AddressMapper.getInstance().mapToDomain(source.getAddress()));
		if (source.getName() != null) {
			target.setFullName(source.getName().getFirstName() + " " + source.getName().getLastName());
		}
		target.setLegalBusinessName(source.getLegalBusinessName());
		target.setName(ConsumerNameMapper.getInstance().mapToDomain(source.getName()));
		target.setTradeNameAttention(source.getTradeNameAttention());
		target.setVerifiedDate(source.getVerifiedDate());
		return super.performDomainMapping(source, target);
	}
	
	
}
