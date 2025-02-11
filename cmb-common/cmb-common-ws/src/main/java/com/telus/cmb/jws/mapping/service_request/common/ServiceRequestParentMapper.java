package com.telus.cmb.jws.mapping.service_request.common;

import java.sql.Timestamp;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.servicerequest.info.ServiceRequestParentInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.servicerequestcommontypes_v1.ServiceRequestParent;

public class ServiceRequestParentMapper extends AbstractSchemaMapper<ServiceRequestParent, ServiceRequestParentInfo> {

	public ServiceRequestParentMapper() {
		super (ServiceRequestParent.class, ServiceRequestParentInfo.class);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performDomainMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected ServiceRequestParentInfo performDomainMapping(ServiceRequestParent source, ServiceRequestParentInfo target) {
		target.setParentId(source.getParentId());
		target.setRelationshipTypeId(source.getRelationshipTypeId());
		target.setTimestamp(new Timestamp(source.getTimestamp().getTime()));
		return super.performDomainMapping(source, target);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected ServiceRequestParent performSchemaMapping(ServiceRequestParentInfo source, ServiceRequestParent target) {
		target.setParentId(source.getServiceRequestParentId());
		target.setRelationshipTypeId(source.getServiceRequestRelationshipTypeId());
		target.setTimestamp(source.getServiceRequestTimestamp());
		return super.performSchemaMapping(source, target);
	}
	
	
}
