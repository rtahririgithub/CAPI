package com.telus.cmb.jws;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.telus.cmb.jws.mapping.reference.resource_order_10.NumberGroupMapper;
import com.telus.cmb.jws.utilities.AppConfiguration;
import com.telus.cmb.reference.svc.ResourceOrderReferenceFacade;
import com.telus.eas.account.info.AvailablePhoneNumberInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.ProvinceCode;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resource_order_reference_types_1_0.AccountTypeCode;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resource_order_reference_types_1_0.NumberGroup;

/**
 * @author Brandon Wen
 *
 */

@WebService(
		portName = "ResourceOrderReferenceServicePort", 
		serviceName = "ResourceOrderReferenceService_v1_1", 
		targetNamespace = "http://telus.com/wsdl/RMO/OrderMgmt/ResourceOrderReferenceService_1", 
		wsdlLocation = "/wsdls/ResourceOrderReferenceService_v1_1.wsdl", 
		endpointInterface = "com.telus.cmb.jws.ResourceOrderReferenceServicePort")
		
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")

public class ResourceOrderReferenceService_11 extends BaseService implements ResourceOrderReferenceServicePort {
	
	private ResourceOrderReferenceFacade getResourceOrderReferenceFacade() {
		return getStatelessProxy(ResourceOrderReferenceFacade.class, 
				"ResourceOrderReferenceFacade#com.telus.cmb.reference.svc.ResourceOrderReferenceFacade",
				AppConfiguration.getResourceOrderReferenceFacadeUrl());
	}
	/* (non-Javadoc)
	 * @see com.telus.wsdl.rmo.ordermgmt.resourceorderreferenceservice_1_0.ResourceOrderReferenceServicePortType#getAvailableNumberGroups(com.telus.tmi.xmlschema.xsd.resource.basetypes.resource_order_reference_types_1_0.AccountTypeCode, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@ServiceBusinessOperation(errorCode="CMB_RORS_0001")
	public List<NumberGroup> getAvailableNumberGroups(final AccountTypeCode accountType, final String accountSubType, final String productType, 
			final String equipmentType, final String marketAreaCode) throws PolicyException, ServiceException {
		
		return execute( new ServiceInvocationCallback<List<NumberGroup>>() {
			
			@Override
			public List<NumberGroup> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new NumberGroupMapper().mapToSchema(getResourceOrderReferenceFacade().getAvailableNumberGroups(accountType.value(), accountSubType, productType, equipmentType, marketAreaCode));
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
				return new NumberGroupMapper().mapToSchema(getResourceOrderReferenceFacade().getNumberGroupByPhoneNumberAndProductType(phoneNumber, productType));
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
				return new NumberGroupMapper().mapToSchema(getResourceOrderReferenceFacade().getNumberGroupByPortedInPhoneNumberAndProductType(phoneNumber, productType));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.rmo.ordermgmt.resourceorderreferenceservice_1_1.ResourceOrderReferenceServicePortType#getAvailablePhoneNumberList(...)
	 */
	@ServiceBusinessOperation(errorCode="CMB_RORS_0004")
	public List<String> getAvailablePhoneNumberList(final int accountNumber, final String subscriberId, final NumberGroup numberGroup, final String productType, 
			final String phoneNumberPattern, final Boolean asianFriendlyInd, final double maxNumber) throws PolicyException, ServiceException {

		assertValid("provinceCode", numberGroup.getProvinceCode(), ProvinceCode.values());
		
		return execute( new ServiceInvocationCallback<List<String>>() {
			
			@Override
			public List<String> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				List<String> numberList = new ArrayList<String>();
				AvailablePhoneNumberInfo[] phoneNumberList = getSubscriberLifecycleManager(context).retrieveAvailablePhoneNumbers(accountNumber, subscriberId, new NumberGroupMapper().mapToDomain(numberGroup), 
						productType, phoneNumberPattern, asianFriendlyInd, Double.valueOf(maxNumber).intValue(), context.getSubscriberLifecycleManagerSessionId());
				for (AvailablePhoneNumberInfo phoneNumber : phoneNumberList) {
					numberList.add(phoneNumber.getPhoneNumber());
				}
				return numberList;
			}
		});
	}
	

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.jws.reference.ReferenceService#enumerateRuntimeResources(java.util.Map)
	 */
	protected Map<String, Object> enumerateRuntimeResources(Map<String, Object> resources) {
		resources.put("ResourceOrderReferenceFacade", getResourceOrderReferenceFacade());
		return super.enumerateRuntimeResources(resources);
	}
	

}
