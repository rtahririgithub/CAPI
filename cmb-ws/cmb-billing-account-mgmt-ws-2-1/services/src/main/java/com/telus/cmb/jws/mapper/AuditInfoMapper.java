package com.telus.cmb.jws.mapper;


import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.transaction.info.AuditInfo;

public class AuditInfoMapper extends AbstractSchemaMapper<com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.AuditInfo, AuditInfo> 
	{

	private static AuditInfoMapper INSTANCE = null;
	public AuditInfoMapper() {
		super(com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.AuditInfo.class,AuditInfo.class);

	}
	
	
	public static synchronized AuditInfoMapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AuditInfoMapper();
		}
		
		return INSTANCE;
	}

	@Override
	protected AuditInfo performDomainMapping(com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.AuditInfo source, AuditInfo target) {
		
		target.setChannelOrgId(source.getChannelOrganizationId());
		target.setCorrelationId(source.getCorrelationId());
		target.setOriginatorAppId(source.getOriginatorApplicationId());
		target.setOutletId(source.getOutletId());
		target.setSalesRepId(source.getSalesRepresentativeId());
		target.setTimestamp(source.getTimestamp());
		target.setUserId(source.getUserId());
		target.setUserTypeCode(source.getUserTypeCode());
		
		return super.performDomainMapping(source, target);
	}

}
