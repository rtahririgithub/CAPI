package com.telus.cmb.jws;

import java.util.List;
import java.util.Map;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import org.springframework.beans.factory.annotation.Autowired;

import com.telus.cmb.jws.mapping.reference.resource_order_10.NumberGroupMapper;
import com.telus.cmb.reference.svc.ResourceOrderReferenceFacade;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resource_order_reference_types_1_0.AccountTypeCode;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resource_order_reference_types_1_0.NumberGroup;

/**
 * @author Brandon Wen
 *
 */

@WebService(
		portName = "ResourceOrderReferenceServicePort", 
		serviceName = "ResourceOrderReferenceService_v1_0", 
		targetNamespace = "http://telus.com/wsdl/RMO/OrderMgmt/ResourceOrderReferenceService_1", 
		wsdlLocation = "/wsdls/ResourceOrderReferenceService_v1_0.wsdl", 
		endpointInterface = "com.telus.cmb.jws.ResourceOrderReferenceServicePort")
		
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")

public class ResourceOrderReferenceService_10 extends BaseService implements ResourceOrderReferenceServicePort {
	
	@Autowired
	private ResourceOrderReferenceFacade facade; 
	
	/* (non-Javadoc)
	 * @see com.telus.wsdl.rmo.ordermgmt.resourceorderreferenceservice_1_0.ResourceOrderReferenceServicePortType#getAvailableNumberGroups(com.telus.tmi.xmlschema.xsd.resource.basetypes.resource_order_reference_types_1_0.AccountTypeCode, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@ServiceBusinessOperation(errorCode="CMB_RORS_0001")
	public List<NumberGroup> getAvailableNumberGroups(final AccountTypeCode accountType, final String accountSubType, final String productType, 
			final String equipmentType, final String marketAreaCode) throws PolicyException, ServiceException {
		
		return execute( new ServiceInvocationCallback<List<NumberGroup>>() {
			
			@Override
			public List<NumberGroup> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new NumberGroupMapper().mapToSchema(facade.getAvailableNumberGroups(accountType.value(), accountSubType, productType, equipmentType, marketAreaCode));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.rmo.ordermgmt.resourceorderreferenceservice_1_0.ResourceOrderReferenceServicePortType#getNumberGroupByPhoneNumberAndProductType(java.lang.String, java.lang.String)
	 */
	@ServiceBusinessOperation(errorCode="CMB_RORS_0002")
	public NumberGroup getNumberGroupByPhoneNumberAndProductType(final String phoneNumber, final String productType) throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<NumberGroup>() {
			
			@Override
			public NumberGroup doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new NumberGroupMapper().mapToSchema(facade.getNumberGroupByPhoneNumberAndProductType(phoneNumber, productType));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.rmo.ordermgmt.resourceorderreferenceservice_1_0.ResourceOrderReferenceServicePortType#getNumberGroupByPortedInPhoneNumberProductType(java.lang.String, java.lang.String)
	 */
	@ServiceBusinessOperation(errorCode="CMB_RORS_0003")
	public NumberGroup getNumberGroupByPortedInPhoneNumberProductType(final String phoneNumber, final String productType) throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<NumberGroup>() {
			
			@Override
			public NumberGroup doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new NumberGroupMapper().mapToSchema(facade.getNumberGroupByPortedInPhoneNumberAndProductType(phoneNumber, productType));
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.jws.reference.ReferenceService#enumerateRuntimeResources(java.util.Map)
	 */
	protected Map<String, Object> enumerateRuntimeResources(Map<String, Object> resources) {
		resources.put("ResourceOrderReferenceFacade", facade);
		return super.enumerateRuntimeResources(resources);
	}
}
