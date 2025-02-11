package com.telus.cmb.jws.mapping.account_information_30;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.InvoicePropertiesInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v3.InvoicePropertyListType;
/**
 * InvoicePropertiesMapper
 * @author tongts
 *
 */
public class InvoicePropertyMapper extends AbstractSchemaMapper<InvoicePropertyListType, InvoicePropertiesInfo> {

	private static InvoicePropertyMapper INSTANCE = null;
	
	private InvoicePropertyMapper(){
		super(InvoicePropertyListType.class, InvoicePropertiesInfo.class);
	}
	
	public synchronized static InvoicePropertyMapper getInstance() {
		if (INSTANCE == null) {
			return new InvoicePropertyMapper();
		}
		
		return INSTANCE;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected InvoicePropertyListType performSchemaMapping(InvoicePropertiesInfo source, InvoicePropertyListType target) {
	      target.setHoldRedirectDestinationCode(source.getHoldRedirectDestinationCode());
	      target.setHoldRedirectFromDate(source.getHoldRedirectFromDate());
	      target.setHoldRedirectToDate(source.getHoldRedirectToDate());
	      target.setInvoiceSuppressionLevel(source.getInvoiceSuppressionLevel());
		
		return super.performSchemaMapping(source, target);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performDomainMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected InvoicePropertiesInfo performDomainMapping(InvoicePropertyListType source, InvoicePropertiesInfo target) {
	      target.setHoldRedirectDestinationCode(source.getHoldRedirectDestinationCode());
	      if (source.getHoldRedirectFromDate() != null)
	        target.setHoldRedirectFromDate(source.getHoldRedirectFromDate());
	      if (source.getHoldRedirectToDate() != null)
	        target.setHoldRedirectToDate(source.getHoldRedirectToDate());
	      target.setInvoiceSuppressionLevel(source.getInvoiceSuppressionLevel());

		return super.performDomainMapping(source, target);
	}

}