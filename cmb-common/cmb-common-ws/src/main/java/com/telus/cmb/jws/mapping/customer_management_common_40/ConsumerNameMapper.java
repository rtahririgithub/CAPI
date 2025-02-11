package com.telus.cmb.jws.mapping.customer_management_common_40;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.ConsumerName;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.NameFormat;

public class ConsumerNameMapper extends AbstractSchemaMapper<ConsumerName, ConsumerNameInfo> {
	private static ConsumerNameMapper INSTANCE = null;
	
	protected ConsumerNameMapper(){
		super(ConsumerName.class, ConsumerNameInfo.class);
	}
	
	public synchronized static ConsumerNameMapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ConsumerNameMapper();
		}
		
		return INSTANCE;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected ConsumerName performSchemaMapping(ConsumerNameInfo source, ConsumerName target) {
		if (source != null) {
			target.setAdditionalLine(source.getAdditionalLine());
			target.setFirstName(source.getFirstName());
			if (target.getFirstName()==null) target.setFirstName("");
			target.setGeneration(source.getGeneration());
			target.setLastName(source.getLastName());
			if (target.getLastName()==null) target.setLastName("");
			target.setMiddleInitial(source.getMiddleInitial());
			target.setNameFormat(toEnum(source.getNameFormat(), NameFormat.class));
			target.setTitle(source.getTitle());
		}
		return super.performSchemaMapping(source, target);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performDomainMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected ConsumerNameInfo performDomainMapping(ConsumerName source, ConsumerNameInfo target) {
		target.setAdditionalLine(source.getAdditionalLine());
		target.setFirstName(source.getFirstName());
		target.setGeneration(source.getGeneration());
		target.setLastName(source.getLastName());
		target.setMiddleInitial(source.getMiddleInitial());
		if (source.getNameFormat() != null) {
			target.setNameFormat(source.getNameFormat().value());
		}
		target.setTitle(source.getTitle());
		return super.performDomainMapping(source, target);
	}
	
	
}
