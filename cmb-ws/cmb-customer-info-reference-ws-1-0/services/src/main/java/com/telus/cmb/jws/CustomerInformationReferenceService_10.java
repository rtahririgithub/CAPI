/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws;

import java.util.List;
import java.util.Map;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.telus.cmb.jws.mapping.reference.customer_information_10.AccountTypeMapper;
import com.telus.cmb.jws.mapping.reference.customer_information_10.MemoTypeMapper;
import com.telus.cmb.jws.mapping.reference.customer_information_10.SegmentationMapper;
import com.telus.schemas.eca.common_types_2_1.ProvinceCode;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.customer_information_reference_types_1_0.AccountType;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.customer_information_reference_types_1_0.MemoType;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.customer_information_reference_types_1_0.Segmentation;

/**
 * @author Pavel Simonovsky
 *
 */

@WebService(
		portName = "CustomerInformationReferenceServicePort", 
		serviceName = "CustomerInformationReferenceService_v1_0", 
		targetNamespace = "http://telus.com/wsdl/CMO/InformationMgmt/CustomerInformationReferenceService_1", 
		wsdlLocation = "/wsdls/CustomerInformationReferenceService_v1_0.wsdl", 
		endpointInterface = "com.telus.cmb.jws.CustomerInformationReferenceServicePort")
		
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")

public class CustomerInformationReferenceService_10 extends BaseService implements CustomerInformationReferenceServicePort {
	
	/* (non-Javadoc)
	 * @see com.telus.wsdl.cmo.informationmgmt.customerinformationreferenceservice_1_0.CustomerInformationReferenceServicePortType#getAccountType(java.lang.String, int)
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_CIRS_0001")
	public AccountType getAccountType(final String accountTypeCode, final int accountTypeBrandId) throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<AccountType>() {

			@Override
			public AccountType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new AccountTypeMapper().mapToSchema(getCustomerInformationReferenceFacade().getAccountType(accountTypeCode, accountTypeBrandId));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.cmo.informationmgmt.customerinformationreferenceservice_1_0.CustomerInformationReferenceServicePortType#getAccountTypes()
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_CIRS_0002")
	public List<AccountType> getAccountTypes() throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<List<AccountType>>() {

			@Override
			public List<AccountType> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new AccountTypeMapper().mapToSchema(getCustomerInformationReferenceFacade().getAccountTypes());
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.cmo.informationmgmt.customerinformationreferenceservice_1_0.CustomerInformationReferenceServicePortType#getMemoType(java.lang.String)
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_CIRS_0003")
	public MemoType getMemoType(final String memoTypeCode) throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<MemoType>() {

			@Override
			public MemoType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new MemoTypeMapper().mapToSchema(getCustomerInformationReferenceFacade().getMemoType(memoTypeCode));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.cmo.informationmgmt.customerinformationreferenceservice_1_0.CustomerInformationReferenceServicePortType#getMemoTypes()
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_CIRS_0004")
	public List<MemoType> getMemoTypes() throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<List<MemoType>>() {

			@Override
			public List<MemoType> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new MemoTypeMapper().mapToSchema(getCustomerInformationReferenceFacade().getMemoTypes());
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.cmo.informationmgmt.customerinformationreferenceservice_1_0.CustomerInformationReferenceServicePortType#getSegmentation(int, java.lang.String, com.telus.schemas.eca.common_types_2_1.ProvinceCode)
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_CIRS_0005")
	public Segmentation getSegmentation(final int brandId, final String accountTypeCode, final ProvinceCode provinceCode) throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<Segmentation>() {

			@Override
			public Segmentation doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new SegmentationMapper().mapToSchema(getCustomerInformationReferenceFacade().getSegmentation(brandId, accountTypeCode, provinceCode.value()));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.cmo.informationmgmt.customerinformationreferenceservice_1_0.CustomerInformationReferenceServicePortType#getSegmentations()
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_CIRS_0006")
	public List<Segmentation> getSegmentations() throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<List<Segmentation>>() {

			@Override
			public List<Segmentation> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new SegmentationMapper().mapToSchema(getCustomerInformationReferenceFacade().getSegmentations());
			}
		});
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.jws.reference.ReferenceService#enumerateRuntimeResources(java.util.Map)
	 */
	@Override
	protected Map<String, Object> enumerateRuntimeResources(Map<String, Object> resources) {
		resources.put("CustomerInformationReferenceFacade", getCustomerInformationReferenceFacade());
		return super.enumerateRuntimeResources(resources);
	}
	
}
